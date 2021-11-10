package com.alet.common.packet;

import java.util.ArrayList;
import java.util.List;

import com.alet.entity.EntityLeadConnection;
import com.creativemd.creativecore.common.packet.CreativeCorePacket;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;

public class PacketConnectLead extends CreativeCorePacket {
	
	private int connection;
	private List<Integer> connectors = new ArrayList<Integer>();
	private int arraySize;
	
	public PacketConnectLead() {
		
	}
	
	public PacketConnectLead(EntityLeadConnection connection) {
		this.connection = connection.getEntityId();
		for (int id : connection.connectIDs) {
			this.connectors.add(id);
		}
		this.arraySize = this.connectors.size();
	}
	
	@Override
	public void writeBytes(ByteBuf buf) {
		buf.writeInt(this.connection);
		buf.writeInt(this.arraySize);
		for (int i = 0; i < this.arraySize; i++) {
			buf.writeInt(this.connectors.get(i));
		}
	}
	
	@Override
	public void readBytes(ByteBuf buf) {
		this.connection = buf.readInt();
		this.arraySize = buf.readInt();
		for (int i = 0; i < this.arraySize; i++) {
			this.connectors.add(buf.readInt());
		}
	}
	
	@Override
	public void executeClient(EntityPlayer player) {
		WorldClient world = (WorldClient) player.getEntityWorld();
		EntityLeadConnection connection = (EntityLeadConnection) world.getEntityByID(this.connection);
		connection.connectIDs.clear();
		for (int id : connectors) {
			connection.connectIDs.add(id);
		}
		System.out.println(connection.connectIDs);
	}
	
	@Override
	public void executeServer(EntityPlayer player) {
		// TODO Auto-generated method stub
		
	}
	
}
