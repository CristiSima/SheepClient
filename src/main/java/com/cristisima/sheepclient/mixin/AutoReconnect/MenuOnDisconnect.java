package com.cristisima.sheepclient.mixin.AutoReconnect;

import com.cristisima.sheepclient.SheepClient;
import com.cristisima.sheepclient.Variables;
import net.minecraft.client.font.MultilineText;
import net.minecraft.client.gui.screen.DisconnectedScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DisconnectedScreen.class)
public class MenuOnDisconnect extends Screen {

    @Shadow
    private Screen parent;

    @Shadow
    private int reasonHeight;

    @Shadow @Final private Text reason;
    @Shadow private MultilineText reasonFormatted;
    ButtonWidget reconnectWidget;

    float reconnectTimer;

    boolean reconnectEnabled=false;

    protected MenuOnDisconnect(Text title) {
        super(title);
    }

    String getReconnectIn()
    {
        return String.format("Reconnect in: %.1f" , reconnectTimer);
    }

    @Inject(method = "init", at = @At("TAIL"))
    public void init(CallbackInfo ci) {
        if(reason.getString().equals("The server is full!"))
        {
            reconnectEnabled=true;
        }
        else
        {
            return;
        }


        int height = this.height / 2 + reasonHeight / 2 + 10 + 20 + 10;
        int width = 100;
        int center = this.width / 2 - width / 2;

        reconnectTimer = Variables.AutoReconnect.reconnectAfter;
        SheepClient.LOGGER.info("Init Auto reconnect");

        reconnectWidget = addDrawableChild(new ButtonWidget(center, height, width, 20,
                Text.literal(getReconnectIn()), (button) -> {
            Variables.AutoReconnect.reconnectOnMultiplayerMenu=true;
            this.client.setScreen(parent);
        }));
    }

    @Inject(method = "render", at = @At("HEAD"))
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if(!reconnectEnabled)
            return;

        if (reconnectTimer > 0) {
            float deltaSeconds=delta/20;
            reconnectTimer -= deltaSeconds;
            if(reconnectTimer<0)
                reconnectTimer=0;

            reconnectWidget.setMessage(Text.literal(getReconnectIn()));
            return;
        }

        if(reconnectTimer<0)
            return;

        reconnectTimer=-1;

        if(!(parent instanceof MultiplayerScreen))
        {
            SheepClient.LOGGER.error("Disconnect back is not MultiplayerScreen, I have failed you.");
            System.out.println(parent.getClass().getSimpleName());
            reconnectWidget.setMessage(Text.literal("Reconnect error"));
            return;
        }
//        MultiplayerScreen multiplayerScreen= (MultiplayerScreen) parent;
//        multiplayerScreen.connect();
        Variables.AutoReconnect.reconnectOnMultiplayerMenu=true;
        client.setScreen(parent);
    }
}