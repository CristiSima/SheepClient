package com.cristisima.sheepclient.mixin;

import com.cristisima.sheepclient.SheepClient;
import com.cristisima.sheepclient.SheepScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.OnlineOptionsScreen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.gui.screen.Screen;

@Mixin(OptionsScreen.class)
public class SheepClientMenu_Setings extends Screen {

    protected SheepClientMenu_Setings(Text title) {
        super(title);
    }

    @Inject(method = "render", at=@At("TAIL"))
    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci)
    {
//        SheepClient.LOGGER.info("What are you setting up?");
    }

    @Inject(method = "init", at=@At("TAIL"))
    public void addButtons(CallbackInfo ci)
    {
        SheepClient.LOGGER.info("Ok, so I think you deserve this UI?");

//        @Shadow int i;
        int i=2;
        this.addDrawableChild(new ButtonWidget(this.width / 2 + 5+150/4, this.height / 6 - 12 + 24 * (i >> 1), 150/2, 20, Text.literal("SheepClient"), (button) -> {
//            this.client.setScreen(new OnlineOptionsScreen(this, this.settings));
            SheepClient.LOGGER.info("Ohoo, what's this, you think you can configure me??");

            SheepClient.LOGGER.info("Lets see if you have what it takes");
            this.client.setScreen(new SheepScreen(this));
        }));
    }
}

