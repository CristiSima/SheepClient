package com.cristisima.sheepclient.mixin;

import com.cristisima.sheepclient.Flags;
import com.cristisima.sheepclient.Utils;
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
        if (Flags.positionPrecision()) {
            x= Utils.precisionRound(x,Variables.PositionPrecision.PRECISION);
//            if(Utils.getDecimal(x,Variables.PositionPrecision.PRECISION+1)!=0)
//                System.out.println("ConstructorX round err");
        }
        return x;
    }
    @ModifyVariable(method = "<init>", at = @At("HEAD"), ordinal = 2)
    private static double ConstructorZ(double z) {
        if (Flags.positionPrecision()) {
            z= Utils.precisionRound(z,Variables.PositionPrecision.PRECISION);
//            if(Utils.getDecimal(z,Variables.PositionPrecision.PRECISION+1)!=0)
//                System.out.println("ConstructorZ round err");
        }
        return z;
    }

}
