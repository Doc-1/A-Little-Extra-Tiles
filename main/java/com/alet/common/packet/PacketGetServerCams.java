package com.alet.common.packet;

import java.util.ArrayList;

import com.creativemd.cmdcam.server.CMDCamServer;
import com.creativemd.creativecore.common.packet.CreativeCorePacket;
import com.creativemd.creativecore.common.packet.PacketHandler;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public class PacketGetServerCams extends CreativeCorePacket {
	
	public int layer;
	public String selected;
	
	public PacketGetServerCams() {
		
	}
	
	public PacketGetServerCams(int layer) {
		this.layer = layer;
		this.selected = "";
	}
	
	public PacketGetServerCams(int layer, String selected) {
		this.layer = layer;
		if (selected.isEmpty())
			this.selected = "";
		else
			this.selected = selected;
	}
	
	@Override
	public void writeBytes(ByteBuf buf) {
		buf.writeInt(layer);
		writeString(buf, selected);
	}
	
	@Override
	public void readBytes(ByteBuf buf) {
		layer = buf.readInt();
		selected = readString(buf);
	}
	
	@Override
	public void executeClient(EntityPlayer player) {
		
	}
	
	@Override
	public void executeServer(EntityPlayer player) {
		ArrayList<String> newList = new ArrayList<>(CMDCamServer.getSavedPaths(player.world));
		String[] ids = newList.toArray(new String[0]);
		PacketHandler.sendPacketToPlayer(new PacketSendServerCams(ids, selected, layer), (EntityPlayerMP) player);
	}
	
}
