package com.alet.packets;

import java.util.ArrayList;

import com.creativemd.cmdcam.server.CMDCamServer;
import com.creativemd.creativecore.common.packet.CreativeCorePacket;
import com.creativemd.creativecore.common.packet.PacketHandler;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public class PacketGetServerCams extends CreativeCorePacket {
    
    public String selected;
    
    public PacketGetServerCams() {
        
    }
    
    public PacketGetServerCams(String selected) {
        if (selected.isEmpty())
            this.selected = "";
        else
            this.selected = selected;
    }
    
    @Override
    public void writeBytes(ByteBuf buf) {
        writeString(buf, selected);
    }
    
    @Override
    public void readBytes(ByteBuf buf) {
        selected = readString(buf);
    }
    
    @Override
    public void executeClient(EntityPlayer player) {
        
    }
    
    @Override
    public void executeServer(EntityPlayer player) {
        ArrayList<String> newList = new ArrayList<>(CMDCamServer.getSavedPaths(player.world));
        String[] ids = newList.toArray(new String[0]);
        PacketHandler.sendPacketToPlayer(new PacketSendServerCams(ids, selected), (EntityPlayerMP) player);
    }
    
}
