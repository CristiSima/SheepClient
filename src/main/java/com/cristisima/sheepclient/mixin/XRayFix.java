package com.cristisima.sheepclient.mixin;

import com.cristisima.sheepclient.Variables;
import net.minecraft.client.render.chunk.ChunkOcclusionData;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChunkOcclusionData.class)
public class XRayFix {
    @Inject(method = "isVisibleThrough", at=@At("TAIL"), cancellable = true)
    public void isVisibleThrough(Direction from, Direction _to, CallbackInfoReturnable<Boolean> cir) {
        if(!Variables.xRay.active)
            return;

        cir.setReturnValue(true);
    }
}


