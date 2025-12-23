package com.docvin.alet.common.registries;

import com.creativemd.creativecore.common.packet.CreativeCorePacket;
import com.docvin.alet.common.packets.PacketSendSound;

public class ALETPackets {
    public static void registerPackets() {
        CreativeCorePacket.registerPacket(PacketSendSound.class);
    }
}
