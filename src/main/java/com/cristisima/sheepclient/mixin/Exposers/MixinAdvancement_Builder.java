package com.cristisima.sheepclient.mixin.Exposers;

import com.cristisima.sheepclient.access.IMixinAdvancement_Builder;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementDisplay;
import net.minecraft.advancement.AdvancementRewards;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Advancement.Builder.class)
class MixinAdvancement_Builder implements IMixinAdvancement_Builder
{
    @Shadow
    private AdvancementDisplay display;
    @Shadow
    private AdvancementRewards rewards;

    public AdvancementDisplay getDisplay() {
        return display;
    }

    public AdvancementRewards getRewards() {
        return rewards;
    }
}
