package com.alet.common.packet;

import com.alet.client.sounds.Notes;
import com.creativemd.creativecore.common.packet.CreativeCorePacket;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;

public class PacketSendSound extends CreativeCorePacket {
	
	BlockPos pos;
	int pitch;
	String resourceLocation;
	
	public PacketSendSound() {
		// TODO Auto-generated constructor stub
	}
	
	public PacketSendSound(int pitch, BlockPos pos, String resourceLocation) {
		this.pitch = pitch;
		this.pos = pos;
		this.resourceLocation = resourceLocation;
	}
	
	@Override
	public void writeBytes(ByteBuf buf) {
		
		writeNBT(buf, NBTUtil.createPosTag(pos));
		buf.writeInt(pitch);
		writeString(buf, resourceLocation);
		
	}
	
	@Override
	public void readBytes(ByteBuf buf) {
		pos = NBTUtil.getPosFromTag(readNBT(buf));
		pitch = buf.readInt();
		resourceLocation = readString(buf);
	}
	
	@Override
	public void executeClient(EntityPlayer player) {
		Notes note = Notes.getNoteFromPitch(pitch);
		Minecraft mc = Minecraft.getMinecraft();
		SoundEvent sound = new SoundEvent(new ResourceLocation(note.getResourceLocation(resourceLocation)));
		//m.sndHandler = new SoundHandler(m, mc.gameSettings);
		//System.out.println(sound);
		player.playSound(sound, 1.0F, note.getPitch());
		//mc.world.playSound(player, player.posX, player.posY, player.posZ, sound, SoundCategory.AMBIENT, 1.0F, note.getPitch());
		//mc.world.playSound(pos, sound, SoundCategory.AMBIENT, 1.0F, note.getPitch(), true);
		//mc.getSoundHandler().playSound(new StationarySound(sound, pos, 1.0F, pitch, SoundCategory.AMBIENT));
	}
	
	@Override
	public void executeServer(EntityPlayer player) {
		
	}
	
}
