package com.cristisima.sheepclient.mixin.AutoReconnect;

import com.cristisima.sheepclient.Variables;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MultiplayerScreen.class)
public abstract class ReconnectOnMenu {
    @Shadow public abstract void connect();

    @Inject(method = "tick", at=@At("HEAD"))
    void tick(CallbackInfo ci)
    {
        if(!Variables.AutoReconnect.reconnectOnMultiplayerMenu)
            return;
        Variables.AutoReconnect.reconnectOnMultiplayerMenu=false;
        connect();

    }
}
