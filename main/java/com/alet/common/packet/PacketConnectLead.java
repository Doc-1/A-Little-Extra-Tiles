package com.alet.common.packet;

import com.alet.entity.EntityLeadConnection;
import com.creativemd.creativecore.common.packet.CreativeCorePacket;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class PacketConnectLead extends CreativeCorePacket {
	
	private int connection;
	private int player;
	
	public PacketConnectLead() {
		
	}
	
	public PacketConnectLead(Entity connection, Entity player) {
		System.out.println(connection.getEntityId());
		this.connection = connection.getEntityId();
		this.player = player.getEntityId();
	}
	
	@Override
	public void writeBytes(ByteBuf buf) {
		buf.writeInt(connection);
		buf.writeInt(player);
	}
	
	@Override
	public void readBytes(ByteBuf buf) {
		this.connection = buf.readInt();
		this.player = buf.readInt();
	}
	
	@Override
	public void executeClient(EntityPlayer player) {
		WorldClient world = (WorldClient) player.getEntityWorld();
		Entity entity = world.getEntityByID(this.connection);
		Entity entity1 = world.getEntityByID(this.player);
		
		System.out.println(entity);
		if (entity instanceof EntityLeadConnection) {
			if (entity1 != null) {
				((EntityLeadConnection) entity).setLeashHolder(entity1, null, false);
			} else {
				((EntityLeadConnection) entity).clearLeashed(false, false);
			}
		}
	}
	
	@Override
	public void executeServer(EntityPlayer player) {
		// TODO Auto-generated method stub
		
	}
	
}
