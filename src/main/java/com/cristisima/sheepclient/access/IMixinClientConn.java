package com.cristisima.sheepclient.access;

import net.minecraft.network.packet.Packet;
import net.minecraft.network.PacketCallbacks;
import org.jetbrains.annotations.Nullable;

public interface IMixinClientConn {
    void sendImm(Packet<?> packet, @Nullable PacketCallbacks callbacks);

}
