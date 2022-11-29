package com.cristisima.sheepclient.access;

import net.minecraft.advancement.AdvancementDisplay;
import net.minecraft.advancement.AdvancementRewards;

public interface IMixinAdvancement_Builder
{
    AdvancementDisplay getDisplay();

    AdvancementRewards getRewards();
}
