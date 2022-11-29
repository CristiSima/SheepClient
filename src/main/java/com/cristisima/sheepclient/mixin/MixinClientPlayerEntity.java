package com.cristisima.sheepclient.mixin;

import com.cristisima.sheepclient.SheepClient;
import com.cristisima.sheepclient.Variables;
import com.cristisima.sheepclient.access.IMixinClientConn;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
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

    @Inject(method = "tick", at = @At("HEAD"))
    void posBefore(CallbackInfo ci)
    {
//        SheepClient.LOGGER.info("before "+client.player.getPos().toString());
    }

    double precisionRound(double nr)
    {
        double scale = Math.pow(10, Variables.PositionPrecision.PRECISION);
        return Math.round(nr*scale)/scale;
    }


    @Inject(method = "tick", at = @At("HEAD"))
    void fixPos(CallbackInfo ci)
    {
        if(!Variables.fixPositionActive)
            return;

        double maxDist=0.0035;

        Vec3d diff=client.player.getPos().subtract(new Vec3d(
                precisionRound(client.player.getX()),
                client.player.getY(),
                precisionRound(client.player.getZ())
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

        if(distX==0 && distZ==0)
        {
            Variables.fixPositionActive=false;
            return;
        }

        client.player.setPosition(client.player.getPos().subtract(
            distX,
            0,
            distZ
        ));

        PlayerMoveC2SPacket packet=new PlayerMoveC2SPacket.PositionAndOnGround(
                client.player.getPos().getX(),
                client.player.getPos().getY(),
                client.player.getPos().getZ(),
                false
        );

        ((IMixinClientConn)networkHandler.getConnection()).sendImm(packet, null);packet=new PlayerMoveC2SPacket.PositionAndOnGround(
            client.player.getPos().getX(),
            client.player.getPos().getY()+1000,
            client.player.getPos().getZ(),
            false
        );

        ((IMixinClientConn)networkHandler.getConnection()).sendImm(packet, null);

//        Variables.fixPositionActive=false;
    }


    @Inject(method = "tick", at = @At("TAIL"))
    void posAfter(CallbackInfo ci)
    {
//        SheepClient.LOGGER.info("after"+client.player.getPos().toString());
    }
}
