package com.alet.regestries;

import com.alet.packets.PacketDropItem;
import com.alet.packets.PacketGetServerCams;
import com.alet.packets.PacketGetServerScoreboard;
import com.alet.packets.PacketLeftClick;
import com.alet.packets.PacketPlaySound;
import com.alet.packets.PacketSendGuiToClient;
import com.alet.packets.PacketSendServerCams;
import com.alet.packets.PacketSendSound;
import com.alet.packets.PacketUpdateBreakBlock;
import com.alet.packets.PacketUpdateMutateFromServer;
import com.alet.packets.PacketUpdateNBT;
import com.alet.packets.PacketUpdateStructureFromClient;
import com.creativemd.creativecore.common.packet.CreativeCorePacket;

import net.minecraftforge.fml.common.Loader;

public class PacketRegistery {
    public static void registerPackets() {
        
        CreativeCorePacket.registerPacket(PacketUpdateNBT.class);
        CreativeCorePacket.registerPacket(PacketSendSound.class);
        CreativeCorePacket.registerPacket(PacketPlaySound.class);
        CreativeCorePacket.registerPacket(PacketUpdateStructureFromClient.class);
        CreativeCorePacket.registerPacket(PacketSendGuiToClient.class);
        CreativeCorePacket.registerPacket(PacketDropItem.class);
        CreativeCorePacket.registerPacket(PacketUpdateBreakBlock.class);
        CreativeCorePacket.registerPacket(PacketUpdateMutateFromServer.class);
        CreativeCorePacket.registerPacket(PacketLeftClick.class);
        CreativeCorePacket.registerPacket(PacketGetServerScoreboard.class);
        
        if (Loader.isModLoaded("cmdcam")) {
            CreativeCorePacket.registerPacket(PacketSendServerCams.class);
            CreativeCorePacket.registerPacket(PacketGetServerCams.class);
        }
        
    }
}
