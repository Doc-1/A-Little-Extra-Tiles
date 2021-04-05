package com.alet.common.packet;

import java.util.ArrayList;
import java.util.List;

import com.alet.common.structure.type.LittleCamPlayer;
import com.creativemd.creativecore.common.packet.CreativeCorePacket;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class PacketSendServerCams extends CreativeCorePacket {
	
	public String[] ids;
	public NBTTagCompound nbt;
	
	public PacketSendServerCams(String[] id) {
		this.ids = id;
		this.nbt = new NBTTagCompound();
	}
	
	public PacketSendServerCams() {
		
	}
	
	@Override
	public void writeBytes(ByteBuf buf) {
		NBTTagList list = new NBTTagList();
		for (int i = 0; i < ids.length; i++) {
			NBTTagCompound tempNBT = new NBTTagCompound();
			tempNBT.setString(i + "", ids[i]);
			list.appendTag(tempNBT);
		}
		nbt.setTag("ids", list);
		writeNBT(buf, nbt);
	}
	
	@Override
	public void readBytes(ByteBuf buf) {
		nbt = readNBT(buf);
		
	}
	
	@Override
	public void executeClient(EntityPlayer player) {
		NBTTagList list = nbt.getTagList("ids", 10);
		List<String> t = new ArrayList<String>();
		int i = 0;
		for (NBTBase each : list) {
			t.add(((NBTTagCompound) each).getString(i + ""));
			i++;
		}
		LittleCamPlayer.camIDs = t.toArray(new String[0]);
		
	}
	
	@Override
	public void executeServer(EntityPlayer player) {
		// TODO Auto-generated method stub
		
	}
	
}
