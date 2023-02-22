package com.cristisima.sheepclient.mixin;
import com.cristisima.sheepclient.Flags;
import com.cristisima.sheepclient.Variables;
import com.cristisima.sheepclient.access.IMixinClientConn;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static java.lang.Math.max;

@Mixin(ClientPlayerEntity.class)
public class FlyHack {
    @Shadow @Final protected MinecraftClient client;

    @Shadow @Final public ClientPlayNetworkHandler networkHandler;
    double bypassDiff =0.1;

    double prevY;
    int flyCount=0;
    int fallCount=0;


    @Inject(method = "tick", at = @At("HEAD"))
    void savePrevY(CallbackInfo ci)
    {
        prevY=client.player.getPos().getY();
    }

    @Inject(method = "tick", at = @At("TAIL"))
    void antiAntiFly(CallbackInfo ci)
    {
        client.player.getAbilities().allowFlying= Flags.flyActive();

        if(!Flags.flyActive() || !client.player.getAbilities().flying)
        {
            flyCount=0;
            return;
        }

        if((++flyCount)%20!=0)
            return;

        double newY=prevY;

        if(client.player.getVelocity().getY()<-0.1)
            return;

        newY-= bypassDiff;

        PlayerMoveC2SPacket packet=new PlayerMoveC2SPacket.PositionAndOnGround(
                client.player.getPos().getX(),
                newY,
                client.player.getPos().getZ(),
                false
        );

        ((IMixinClientConn)networkHandler.getConnection()).sendImm(packet, null);

    }

    @Inject(method = "tick", at = @At("TAIL"))
    void noFallDmg(CallbackInfo ci)
    {
        if(!Flags.noFallDmg())
            return;

        if(client.player.getAbilities().flying || client.player.getVelocity().getY()>-0.5)
            return;

        var blockBellow1=client.player.world.getBlockState(new BlockPos(client.player.getPos().subtract(0, 1, 0)));
        var blockBellow2=client.player.world.getBlockState(new BlockPos(client.player.getPos().subtract(0, 2, 0)));
        if(blockBellow1.isAir() && blockBellow2.isAir()) {
            fallCount=0;
            return;
        }

        if((fallCount++)%10!=0)
            return;

        double newY=prevY+bypassDiff;

        client.player.setVelocity(
                client.player.getVelocity().getX(),
                0.000,
                client.player.getVelocity().getZ()
        );

        PlayerMoveC2SPacket packet=new PlayerMoveC2SPacket.PositionAndOnGround(
                client.player.getPos().getX(),
                newY,
                client.player.getPos().getZ(),
                false
        );

        ((IMixinClientConn)networkHandler.getConnection()).sendImm(packet, null);
    }
}
