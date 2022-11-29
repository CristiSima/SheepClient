package com.cristisima.sheepclient.mixin;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import com.cristisima.sheepclient.Variables;

@Mixin(Entity.class)
//public abstract class PositionPresicion implements Nameable, EntityLike, CommandOutput
public abstract class PositionPrecision
{
    @ModifyVariable(method = "setPos", at=@At("HEAD"), ordinal = 0)
    public double setPosX(double x)
    {
        if(this.getClass().getSimpleName() == ClientPlayerEntity.class.getSimpleName() && Variables.PositionPrecision.active)
        {
            double scale = Math.pow(10, Variables.PositionPrecision.PRECISION);
            return Math.round(x*scale)/scale;
        }
        return x;
    }

    @ModifyVariable(method = "setPos", at=@At("HEAD"), ordinal = 2)
    public double setPosZ(double z)
    {
        if(this.getClass().getSimpleName() == ClientPlayerEntity.class.getSimpleName() && Variables.PositionPrecision.active)
        {
            double scale = Math.pow(10, Variables.PositionPrecision.PRECISION);
            return Math.round(z*scale)/scale;
        }
        return z;
    }

    @ModifyVariable(method = "setPos", at=@At("HEAD"), ordinal = 1)
    public double setPosY(double y)
    {
        if(this.getClass().getSimpleName() != ClientPlayerEntity.class.getSimpleName())
            return y;

        if(!Variables.InitialHeightOffset.active)
            return y;
//        System.out.println("peek "+y);

        if(Variables.InitialHeightOffset.initialized)
            return y;

        if(y==0)
            return 0;
        if(y==65)
            return 65;

//        System.out.println("initializing "+y);
        if(--Variables.InitialHeightOffset.initializedCount==0)
            Variables.InitialHeightOffset.initialized=true;

//        System.out.println("upped "+(y+Variables.InitialHeightOffset.offset));

        return y + Variables.InitialHeightOffset.offset;
    }

//    @Shadow public abstract void setPos(double x, double y, double z);
//    @Inject(method = "setPos", at=@At("TAIL"), cancellable = true)
//    public void roundPos(double x, double y, double z, CallbackInfo ci)
//    {
//        if(this.getClass().getSimpleName() == ClientPlayerEntity.class.getSimpleName())
//        {
//            double scale = Math.pow(10, Variables.PositionPrecision.PRESICION);
//
//            double Rx=Math.round(x*scale)/scale;
////            double Ry=Math.round(y*scale)/scale;
//            double Rz=Math.round(z*scale)/scale;
//
//            if(Rx!=x || Rz!=z)
//            {
//                this.setPos(Rx,y,Rz);
//
////                SheepClient.LOGGER.info("SET POS "+this.getClass().getSimpleName());
//                ci.cancel();
////                return;
//            }
//            else
//                SheepClient.LOGGER.info("NO LOOP TODAY");
//
//        }
//    }
}