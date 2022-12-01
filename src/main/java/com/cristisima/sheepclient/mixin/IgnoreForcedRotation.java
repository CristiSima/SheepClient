package com.cristisima.sheepclient.mixin;

import com.cristisima.sheepclient.Flags;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;

@Mixin(PlayerPositionLookS2CPacket.class)
public class IgnoreForcedRotation {
    @Inject(method = "getYaw", at=@At("HEAD"), cancellable = true)
    void getYaw(CallbackInfoReturnable<Float> cir)
    {
        if(!Flags.ignoreForcedRotation())
            return;

        cir.setReturnValue((float) 0);
    }
    @Inject(method = "getPitch", at=@At("HEAD"), cancellable = true)
    void getPitch(CallbackInfoReturnable<Float> cir)
    {
        if(!Flags.ignoreForcedRotation())
            return;

        cir.setReturnValue((float) 0);
    }

    @Inject(method = "getFlags", at=@At("RETURN"), cancellable = true)
    void setRots(CallbackInfoReturnable<Set<PlayerPositionLookS2CPacket.Flag>> cir)
    {
        if(!Flags.ignoreForcedRotation())
            return;

        var flags=cir.getReturnValue();
        flags.add(PlayerPositionLookS2CPacket.Flag.Y_ROT);
        flags.add(PlayerPositionLookS2CPacket.Flag.X_ROT);
        cir.setReturnValue(flags);
    }

}
