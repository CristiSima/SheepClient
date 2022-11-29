package com.cristisima.sheepclient.mixin;

import com.cristisima.sheepclient.SheepClient;
import com.cristisima.sheepclient.Variables;
import com.cristisima.sheepclient.access.IMixinAdvancement_Builder;
import com.cristisima.sheepclient.access.IMixinClientConn;
import com.sun.jna.platform.win32.OaIdl;
import io.netty.buffer.ByteBuf;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementDisplay;
import net.minecraft.advancement.AdvancementRewards;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.PacketCallbacks;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.s2c.play.AdvancementUpdateS2CPacket;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.launch.platform.container.IContainerHandle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.network.packet.c2s.play.AdvancementTabC2SPacket;

import java.util.Map;
//import net.minecraft.network.listener.



@Mixin(ClientConnection.class)
public abstract class NetworkBug implements IMixinClientConn {


    @Shadow protected abstract void sendImmediately(Packet<?> packet, @Nullable PacketCallbacks callbacks);

    @Shadow public abstract void send(Packet<?> packet);

    @Inject(at = @At("HEAD"), method = "send(Lnet/minecraft/network/Packet;)V", cancellable = true)
    public void sendBug(Packet<?> packet, CallbackInfo ci) {
//        if(packet_name.equals)
//        if(!Variables.packets_seen.contains(packet_name))
//        {
//            SheepClient.LOGGER.info("Network Bug found(SEND): "+packet_name);
//            Variables.packets_seen.add(packet_name);
//
//        }
//        if(packet.getClass()==)


//        if(packet_name.equals("Full"))
//        {
//            PlayerMoveC2SPacket.Full fullPacket=(PlayerMoveC2SPacket.Full)packet;
//            System.out.println(fullPacket.changesPosition());
//            System.out.println(fullPacket.getX(0));
//        }

        if(Variables.noPositionPacket)
            if(packet.getClass().equals(PlayerMoveC2SPacket.Full.class) ||
                packet.getClass().equals(PlayerMoveC2SPacket.PositionAndOnGround.class) ||
                packet.getClass().equals(PlayerMoveC2SPacket.LookAndOnGround.class) ||
                packet.getClass().equals(PlayerMoveC2SPacket.OnGroundOnly.class))
            {
//                System.out.println("Cancel "+packet_name);
                ci.cancel();
                return;
            }

        if(Variables.injectedYVelocity!=0)
        {
            int injected=Variables.injectedYVelocity;
            Variables.injectedYVelocity=0;

//            send(Veloci);

        }


        String packet_name=packet.getClass().getSimpleName();
        SheepClient.LOGGER.info("Network Bug found(SEND): "+packet_name);
    }

    @Inject(at=@At("HEAD"), method = "handlePacket")
    private static <T extends PacketListener> void handlePacket(Packet<T> packet, PacketListener listener, CallbackInfo ci) {
        String packet_name=packet.getClass().getSimpleName();
        if(!Variables.packets_seen.contains(packet_name))
        {
            SheepClient.LOGGER.info("Network Bug found(RECV): "+packet_name);
            Variables.packets_seen.add(packet_name);
        }
    }


    @Inject(at=@At("HEAD"), method = "handlePacket")
    private static <T extends PacketListener> void handleAdvancementUpdateS2CPacket(Packet<T> orgPacket, PacketListener listener, CallbackInfo ci) {
        if(orgPacket.getClass()!=AdvancementUpdateS2CPacket.class)
            return;

        AdvancementUpdateS2CPacket packet=(AdvancementUpdateS2CPacket)orgPacket;
        System.out.println("Got another Advancement Update");
        for (Map.Entry<Identifier, Advancement.Builder> entry:
             packet.getAdvancementsToEarn().entrySet()){
            if(entry.getKey().toString().startsWith("minecraft:recipes"))
                continue;
            Advancement.Builder val=(Advancement.Builder)entry.getValue();
            SheepClient.LOGGER.info(((IMixinAdvancement_Builder)(Object)val).getDisplay().getTitle().getString()
                    + "\t|\t"
                    + (((IMixinAdvancement_Builder)(Object)val).getDisplay().getDescription().getString())
            );

        }
    }

    public void sendImm(Packet<?> packet, @Nullable PacketCallbacks callbacks)
    {
        sendImmediately(packet, callbacks);
    }

}

