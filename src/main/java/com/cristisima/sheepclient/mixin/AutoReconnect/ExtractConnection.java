package com.cristisima.sheepclient.mixin.AutoReconnect;

import com.cristisima.sheepclient.Variables;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ConnectScreen;
import net.minecraft.client.network.ServerAddress;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ConnectScreen.class)
public class ExtractConnection {

    @Inject(method = "connect(Lnet/minecraft/client/MinecraftClient;Lnet/minecraft/client/network/ServerAddress;)V", at=@At("HEAD"))
    private void connect(MinecraftClient client, ServerAddress address, CallbackInfo ci) {
        Variables.AutoReconnect.lastServer=address;

//        unrelated to auto reconnect
        if(Variables.InitialHeightOffset.active)
            Variables.InitialHeightOffset.initialized=false;
//        System.out.println(Variables.InitialHeightOffset.initialized);
        Variables.InitialHeightOffset.initializedCount =Variables.InitialHeightOffset.DefaultInitializedCount;
    }
}
