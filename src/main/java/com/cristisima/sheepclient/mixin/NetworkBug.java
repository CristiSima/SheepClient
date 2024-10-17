package com.cristisima.sheepclient.mixin;

import com.cristisima.sheepclient.Flags;
import com.cristisima.sheepclient.SheepClient;
import com.cristisima.sheepclient.Variables;
import com.cristisima.sheepclient.access.IMixinAdvancement_Builder;
import com.cristisima.sheepclient.access.IMixinClientConn;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.advancement.Advancement;
import net.minecraft.network.*;
import net.minecraft.network.packet.*;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.s2c.play.AdvancementUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.network.packet.s2c.play.WorldBorderInitializeS2CPacket;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.network.packet.s2c.play.GameStateChangeS2CPacket;

import java.lang.reflect.Field;
import java.util.Map;
//import net.minecraft.network.listener.



@Mixin(ClientConnection.class)
public abstract class NetworkBug implements IMixinClientConn {
    @Inject(at=@At("HEAD"), method = "channelActive")
    private void connectionReset(ChannelHandlerContext context, CallbackInfo ci)
    {
        Variables.last_sync_id=0;
    }

    @Shadow protected abstract void sendImmediately(Packet<?> packet, @Nullable PacketCallbacks callbacks);

    @Shadow public abstract void send(Packet<?> packet);

    @Shadow public abstract void send(Packet<?> packet, @Nullable PacketCallbacks callbacks);

    @Inject(at = @At("HEAD"), method = "send(Lnet/minecraft/network/packet/Packet;)V", cancellable = true)
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

        if(Flags.noPositionPacket())
            if(packet.getClass().equals(PlayerMoveC2SPacket.Full.class) ||
                packet.getClass().equals(PlayerMoveC2SPacket.PositionAndOnGround.class) ||
                packet.getClass().equals(PlayerMoveC2SPacket.LookAndOnGround.class) ||
                packet.getClass().equals(PlayerMoveC2SPacket.OnGroundOnly.class))
            {
//                if(packet.getClass().equals(PlayerMoveC2SPacket.Full.class))
//                    send(new PlayerMoveC2SPacket.LookAndOnGround(
//                            ((PlayerMoveC2SPacket) packet).getYaw(0),
//                            ((PlayerMoveC2SPacket) packet).getPitch(0),
//                            ((PlayerMoveC2SPacket) packet).isOnGround()
//                    ));
//                System.out.println("Cancel "+packet.getClass().getSimpleName());
                ci.cancel();
                return;
            }

        if(Variables.injectedYVelocity!=0)
        {
            int injected=Variables.injectedYVelocity;
            Variables.injectedYVelocity=0;


        }


        String packet_name=packet.getClass().getSimpleName();
//        SheepClient.LOGGER.info("Network Bug found(SEND): "+packet_name);
    }

    @Inject(at=@At("HEAD"), method = "handlePacket")
    private static <T extends PacketListener> void handlePacket(Packet<T> packet, PacketListener listener, CallbackInfo ci) {
        String packet_name=packet.getClass().getSimpleName();
        if(!Variables.packets_seen.contains(packet_name))
        {
//            SheepClient.LOGGER.info("Network Bug found(RECV): "+packet_name);
            Variables.packets_seen.add(packet_name);
        }
//        else
//            SheepClient.LOGGER.info("Network Bug found(RECV): "+packet_name);
    }

    @Inject(at=@At("HEAD"), method = "handlePacket")
    private static <T extends PacketListener> void packetLogger(Packet<T> packet, PacketListener listener, CallbackInfo ci) {
        if(Variables.log_packets)
        {
            String packet_name=packet.getClass().getSimpleName();
            StringBuilder packet_info = new StringBuilder("Packet Logger got: ");
            packet_info.append(packet_name);
            packet_info.append(" { ");
            for (Field field: packet.getClass().getFields()) {
                packet_info.append(field.getName());
                packet_info.append("=[");
                try {
                    packet_info.append(field.get(packet));
                } catch (Exception e) {
                    packet_info.append(e.toString());
                }
                packet_info.append("] ");
            }
            packet_info.append("}");

            SheepClient.LOGGER.info(packet_info.toString());
        }
    }

    @Inject(at=@At("HEAD"), method = "handlePacket", cancellable = true)
    private static <T extends PacketListener> void handlePositionSync(Packet<T> packet, PacketListener listener, CallbackInfo ci) {
        if(!packet.getClass().equals(PlayerPositionLookS2CPacket.class))
            return;

        PlayerPositionLookS2CPacket eventPacket= (PlayerPositionLookS2CPacket) packet;

        Variables.last_sync_pos=new Vec3d(eventPacket.getX(), eventPacket.getY(), eventPacket.getZ());
        if(eventPacket.getTeleportId()<=Variables.last_sync_id) {
            ci.cancel();
            return;
        }

        Variables.last_sync_id=eventPacket.getTeleportId();
        System.out.println("Sync("+eventPacket.getTeleportId()+") @ "+eventPacket.getX()+","+eventPacket.getY()+","+eventPacket.getZ());

    }

    @Inject(at=@At("HEAD"), method = "handlePacket", cancellable = true)
    private static <T extends PacketListener> void handleDemo(Packet<T> packet, PacketListener listener, CallbackInfo ci) {
        if(!Variables.noDemo)
            return;

        if(!packet.getClass().equals(GameStateChangeS2CPacket.class))
            return;

        GameStateChangeS2CPacket eventPacket= (GameStateChangeS2CPacket) packet;

        if(eventPacket.getReason()==GameStateChangeS2CPacket.DEMO_MESSAGE_SHOWN)
            ci.cancel();
    }
    @Inject(at=@At("HEAD"), method = "handlePacket", cancellable = true)
    private static <T extends PacketListener> void handleChangeGamemode(Packet<T> packet, PacketListener listener, CallbackInfo ci) {
        if(!packet.getClass().equals(GameStateChangeS2CPacket.class))
            return;

        GameStateChangeS2CPacket eventPacket= (GameStateChangeS2CPacket) packet;

        if(eventPacket.getReason()!=GameStateChangeS2CPacket.GAME_MODE_CHANGED)
            return;
        if(Variables.noCreative &&
                eventPacket.getValue()==1)
            ci.cancel();
    }
    @Inject(at=@At("HEAD"), method = "handlePacket", cancellable = true)
    private static <T extends PacketListener> void handleWorldBorder(Packet<T> packet, PacketListener listener, CallbackInfo ci) {
        if(!packet.getClass().equals(WorldBorderInitializeS2CPacket.class))
            return;

//        WorldBorderInitializeS2CPacket borderPacket= (WorldBorderInitializeS2CPacket) packet;
//
//        if(eventPacket.getReason()!=GameStateChangeS2CPacket.GAME_MODE_CHANGED)
//            return;
        ci.cancel();
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
            Advancement.Builder val = entry.getValue();
            SheepClient.LOGGER.info(((IMixinAdvancement_Builder)val).getDisplay().getTitle().getString()
                    + "\t|\t"
                    + (((IMixinAdvancement_Builder)val).getDisplay().getDescription().getString())
            );

        }
    }

//    @Inject(at=@At("HEAD"), method = "handlePacket", cancellable = true)
//    private static <T extends PacketListener> void handleServerReset(Packet<T> packet, PacketListener listener, CallbackInfo ci) {
//        if(!packet.getClass().equals(PlayerPositionLookS2CPacket.class))
//            return;
//
//        if(!Flags.uneventfulMove())
//            return;
////        WorldBorderInitializeS2CPacket borderPacket= (WorldBorderInitializeS2CPacket) packet;
////
////        if(eventPacket.getReason()!=GameStateChangeS2CPacket.GAME_MODE_CHANGED)
////            return;
////        ci.cancel();
////        System.out.println(((PlayerPositionLookS2CPacket) packet).getX());
//
////        sendImm(new TeleportConfirmC2SPacket(((PlayerPositionLookS2CPacket) packet).getTeleportId()));
//
////        eventPacket.
//    }


    public void sendImm(Packet<?> packet, @Nullable PacketCallbacks callbacks)
    {
        sendImmediately(packet, callbacks);
    }

}


