package com.cristisima.sheepclient.mixin;

import com.cristisima.sheepclient.Flags;
import com.cristisima.sheepclient.SheepClient;
import com.cristisima.sheepclient.Utils;
import com.cristisima.sheepclient.Variables;
import com.cristisima.sheepclient.access.IMixinClientConn;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.input.Input;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.c2s.play.TeleportConfirmC2SPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class MixinClientPlayerEntity {


    @Shadow @Final protected MinecraftClient client;

    @Shadow @Final public ClientPlayNetworkHandler networkHandler;


    @Shadow public Input input;

    @Inject(method = "tick", at = @At("HEAD"))
    void posBefore(CallbackInfo ci)
    {
//        SheepClient.LOGGER.info("before "+client.player.getPos().toString());
    }

    @Inject(method = "tick", at = @At("HEAD"))
    void fixPos(CallbackInfo ci)
    {
        if(!Variables.fixPositionActive)
            return;

        double maxDist=0.0035;

        Vec3d diff=client.player.getPos().subtract(new Vec3d(
                Utils.precisionRound(client.player.getX(), Variables.PositionPrecision.PRECISION),
                client.player.getY(),
                Utils.precisionRound(client.player.getZ(), Variables.PositionPrecision.PRECISION)
        ));

        double distX;
        double distZ;

        if(diff.getX()<0)
            distX=Math.max(-maxDist, diff.getX());
        else
            distX=Math.min(maxDist, diff.getX());

        maxDist-=Math.abs(distX);

        if(diff.getZ()<0)
            distZ=Math.max(-maxDist, diff.getZ());
        else
            distZ=Math.min(maxDist, diff.getZ());

        System.out.println(distX+" "+distZ);

        if(distX!=0 || distZ!=0)
            client.player.setPosition(client.player.getPos().subtract(
                distX,
                0,
                distZ
            ));

        IMixinClientConn clientConn= (IMixinClientConn) networkHandler.getConnection();

        PlayerMoveC2SPacket packet=new PlayerMoveC2SPacket.PositionAndOnGround(
                client.player.getPos().getX(),
                client.player.getPos().getY(),
                client.player.getPos().getZ(),
                false
        );
        clientConn.sendImm(packet, null);

        packet=new PlayerMoveC2SPacket.PositionAndOnGround(
            client.player.getPos().getX(),
            client.player.getPos().getY()+1000,
            client.player.getPos().getZ(),
            false
        );
        clientConn.sendImm(packet, null);

        Variables.fixPositionActive=false;
        if(distX==0 && distZ==0)
        {
        }
    }

    int uneventfulFlyCounter=0;
    @Inject(method = "tick", at = @At("TAIL"))
    void uneventfulMove(CallbackInfo ci)
    {
        if(!Flags.uneventfulMove())
            return;

        uneventfulFlyCounter++;
        if(uneventfulFlyCounter>20) {
            uneventfulAntiFly();
            return;
        } else if (uneventfulFlyCounter==1) {
            uneventfulAntiFlyResync();
        }

        if(client.player.getX() == client.player.prevX &&
            client.player.getZ() == client.player.prevZ &&
            client.player.getY() == client.player.prevY
        )
            return;

        double maxDist=0.0035;
        maxDist=0.06;

        Vec3d prevPos= new Vec3d(client.player.prevX, client.player.prevY,client.player.prevZ);
        Vec3d curPos=prevPos;
        Vec3d diff=client.player.getPos().subtract(prevPos);
        client.player.setPosition(curPos);
        if(input.jumping)
            diff=new Vec3d(0, 1, 0);
        else if (input.sneaking)
            diff=new Vec3d(0, -1, 0);
        else if(input.pressingBack||input.pressingForward||input.pressingRight||input.pressingLeft)
            diff=diff.subtract(0, diff.y, 0);
        else
            return;

        Vec3d diffDir=diff.normalize();

        double dist=diff.length();

        double stepDist;
        IMixinClientConn clientConn= (IMixinClientConn) networkHandler.getConnection();

        for(int i=0;i<Variables.uneventfulMove.max_rate;i++)
        {
            stepDist=Math.min(dist, maxDist);
            curPos=curPos.add(diffDir.multiply(stepDist));
            client.player.setPosition(curPos);
            clientConn.sendImm(new PlayerMoveC2SPacket.PositionAndOnGround(
                    curPos.getX(),
                    curPos.getY(),
                    curPos.getZ(),
                    false
            ), null);
            clientConn.sendImm(new PlayerMoveC2SPacket.PositionAndOnGround(
                    curPos.getX(),
                    curPos.getY()-1000,
                    curPos.getZ(),
                    true
            ), null);
            dist-=stepDist;

            // sync pos
            Variables.last_sync_id++;
            clientConn.sendImm(new TeleportConfirmC2SPacket(
                    Variables.last_sync_id
            ), null);
//            System.out.println("Predicted sync "+Variables.last_sync_id);
        }
    }

    void uneventfulAntiFly()
    {
        Vec3d curPos= new Vec3d(client.player.prevX, client.player.prevY,client.player.prevZ);
        client.player.setPosition(curPos);

        uneventfulFlyCounter=0;
        Vec3d diffDir=new Vec3d(0, -0.06, 0);


        IMixinClientConn clientConn= (IMixinClientConn) networkHandler.getConnection();

//        sync pos
        clientConn.sendImm(new PlayerMoveC2SPacket.PositionAndOnGround(
                curPos.getX(),
                curPos.getY(),
                curPos.getZ(),
                false
        ), null);
        clientConn.sendImm(new PlayerMoveC2SPacket.PositionAndOnGround(
                curPos.getX(),
                curPos.getY()-1000,
                curPos.getZ(),
                true
        ), null);
        Variables.last_sync_id++;
        clientConn.sendImm(new TeleportConfirmC2SPacket(
                Variables.last_sync_id
        ), null);

//        move down
        curPos=curPos.add(diffDir);
        clientConn.sendImm(new PlayerMoveC2SPacket.PositionAndOnGround(
                curPos.getX(),
                curPos.getY(),
                curPos.getZ(),
                false
        ), null);
        clientConn.sendImm(new PlayerMoveC2SPacket.PositionAndOnGround(
                curPos.getX(),
                curPos.getY()-1000,
                curPos.getZ(),
                true
        ), null);
        Variables.last_sync_id++;
        clientConn.sendImm(new TeleportConfirmC2SPacket(
                Variables.last_sync_id
        ), null);

//        move back
        curPos=curPos.subtract(diffDir);
        client.player.setPosition(curPos);


    }

    void uneventfulAntiFlyResync()
    {
        IMixinClientConn clientConn= (IMixinClientConn) networkHandler.getConnection();
        Vec3d curPos= new Vec3d(client.player.prevX, client.player.prevY,client.player.prevZ);
        client.player.setPosition(curPos);

        clientConn.sendImm(new PlayerMoveC2SPacket.PositionAndOnGround(
                curPos.getX(),
                curPos.getY(),
                curPos.getZ(),
                false
        ), null);
        clientConn.sendImm(new PlayerMoveC2SPacket.PositionAndOnGround(
                curPos.getX(),
                curPos.getY()-1000,
                curPos.getZ(),
                true
        ), null);
        Variables.last_sync_id++;
        clientConn.sendImm(new TeleportConfirmC2SPacket(
                Variables.last_sync_id
        ), null);
    }
}
