package com.cristisima.sheepclient.mixin;

import com.cristisima.sheepclient.Variables;
import net.minecraft.block.AbstractBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractBlock.AbstractBlockState.class)
public abstract class CatEyes {

    @Inject(method = "getLuminance", at=@At("HEAD"), cancellable = true)
    public void getLuminance(CallbackInfoReturnable<Integer> cir) {
        if(Variables.xRay.active)
        {
//            Light lvl override for xRay
            cir.setReturnValue(Variables.CatEyes.MAX);
            return;
        }
//        https://www.desmos.com/calculator/xxmgwwwunc
//        f(x)=max(x, x^2+a  / a+1)
//        f: [0,1] -> [0,1]

//        g(x):m*f(x/m)
//        g:[0, max] ->[m, max]

//        a=m/(max - m)

        if(!Variables.CatEyes.active)
            return;

        if(Variables.CatEyes.MAX==Variables.CatEyes.forced_min) {
            cir.setReturnValue(Variables.CatEyes.MAX);
            return;
        }

        double min_dependent_const=(double) Variables.CatEyes.forced_min/(Variables.CatEyes.MAX-Variables.CatEyes.forced_min);

        double curveValue=Math.max((double)Variables.CatEyes.forced_min/Variables.CatEyes.MAX,
                (Math.pow((double)Variables.CatEyes.forced_min/Variables.CatEyes.MAX, 2)+min_dependent_const)
                    /(Variables.CatEyes.forced_min+min_dependent_const));

        curveValue*=Variables.CatEyes.MAX;
        cir.setReturnValue((int) Math.round(curveValue));
    }
}
