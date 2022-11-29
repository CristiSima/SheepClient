package com.cristisima.sheepclient.mixin;

import com.cristisima.sheepclient.Variables;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.cristisima.sheepclient.Utils.shouldSkippBLock;

@Mixin(Block.class)
public class XRay {


    @Inject(method = "shouldDrawSide", at=@At("HEAD"), cancellable = true)
    private static void shouldDrawSide(BlockState state, BlockView world, BlockPos pos, Direction side, BlockPos otherPos, CallbackInfoReturnable<Boolean> cir)
    {
        if(!Variables.xRay.active)
            return;

        String blockKey=state.getBlock().getTranslationKey();
        String otherBlockKey = world.getBlockState(otherPos).getBlock().getTranslationKey();



        if(shouldSkippBLock(blockKey))
        {
            cir.setReturnValue(false);
            return;
        }
        cir.setReturnValue(true);

//        if((Variables.xRay.debug_count--)<0)
//            return;
//        System.out.println(pos+" "+otherPos);
//        System.out.println(state.getBlock().getTranslationKey());
    }
}
