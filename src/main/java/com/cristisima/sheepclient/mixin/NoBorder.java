package com.cristisima.sheepclient.mixin;

import com.cristisima.sheepclient.SheepClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.border.WorldBorder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

import net.minecraft.advancement.AdvancementDisplay;

@Mixin(WorldBorder.class)
public class NoBorder {
    /**
     * @author Cristi
     * @reason no world border dmg
     */
    @Overwrite
    public boolean contains(Box box) {
        return true;
    }

    /**
     * @author Cristi
     * @reason no world border collision
     */
    @Overwrite
    public double getDistanceInsideBorder(double x, double z) {
        return 5;
    }

    /**
     * @author Cristi
     * @reason
     */
//    @Overwrite
    public boolean contains(BlockPos pos)
    {
        return true;
    }
}
