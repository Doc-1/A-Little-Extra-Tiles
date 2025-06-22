package com.alet.common.packets;

import com.alet.common.packets.tool.tapemeasure.PacketGenAndSyncUUID;
import com.alet.common.packets.tool.tapemeasure.PacketIncrementalNBTSync;
import com.alet.common.packets.tool.tapemeasure.PacketUpdateNBT;
import com.creativemd.creativecore.common.packet.CreativeCorePacket;

import net.minecraftforge.fml.common.Loader;

public class PacketRegistery {
    public static void registerPackets() {
        
        CreativeCorePacket.registerPacket(PacketIncrementalNBTSync.class);
        CreativeCorePacket.registerPacket(PacketGenAndSyncUUID.class);
        CreativeCorePacket.registerPacket(PacketUpdateNBT.class);
        CreativeCorePacket.registerPacket(PacketSendSound.class);
        CreativeCorePacket.registerPacket(PacketPlaySound.class);
        CreativeCorePacket.registerPacket(PacketUpdateStructureFromClient.class);
        CreativeCorePacket.registerPacket(PacketSendGuiToClient.class);
        CreativeCorePacket.registerPacket(PacketDropItem.class);
        CreativeCorePacket.registerPacket(PacketUpdateBreakBlock.class);
        CreativeCorePacket.registerPacket(PacketUpdateMutateFromServer.class);
        CreativeCorePacket.registerPacket(PacketGetServerScoreboard.class);
        
        if (Loader.isModLoaded("cmdcam")) {
            CreativeCorePacket.registerPacket(PacketSendServerCams.class);
            CreativeCorePacket.registerPacket(PacketGetServerCams.class);
        }
        
    }
}
