package com.cristisima.sheepclient.mixin;

import com.cristisima.sheepclient.Variables;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(PlayerMoveC2SPacket.class)
public class PositionPrecisionMovePacket {
    @ModifyVariable(method = "<init>", at = @At("HEAD"), ordinal = 0)
    private static double ConstructorX(double x) {
        if (Variables.PositionPrecision.active) {
            double scale = Math.pow(10, Variables.PositionPrecision.PRECISION);
            return Math.round(x * scale) / scale;
        }
        return x;
    }
    @ModifyVariable(method = "<init>", at = @At("HEAD"), ordinal = 2)
    private static double ConstructorZ(double z) {
        if (Variables.PositionPrecision.active) {
            double scale = Math.pow(10, Variables.PositionPrecision.PRECISION);
            return Math.round(z * scale) / scale;
        }
        return z;
    }

}
