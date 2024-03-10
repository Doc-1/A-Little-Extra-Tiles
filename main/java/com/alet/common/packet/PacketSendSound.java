package com.alet.common.packet;

import com.alet.client.sounds.Notes;
import com.creativemd.creativecore.common.packet.CreativeCorePacket;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;

public class PacketSendSound extends CreativeCorePacket {
    
    BlockPos pos;
    int pitch;
    int volume;
    String instermentName;
    boolean local;
    
    public PacketSendSound() {
        // TODO Auto-generated constructor stub
    }
    
    public PacketSendSound(int pitch, int volume, boolean local, BlockPos pos, String instermentName) {
        this.pitch = pitch;
        this.local = local;
        this.volume = volume;
        this.pos = pos;
        this.instermentName = instermentName;
    }
    
    @Override
    public void writeBytes(ByteBuf buf) {
        buf.writeBoolean(local);
        writeNBT(buf, NBTUtil.createPosTag(pos));
        buf.writeInt(pitch);
        buf.writeInt(volume);
        writeString(buf, instermentName);
        
    }
    
    @Override
    public void readBytes(ByteBuf buf) {
        local = buf.readBoolean();
        pos = NBTUtil.getPosFromTag(readNBT(buf));
        pitch = buf.readInt();
        volume = buf.readInt();
        instermentName = readString(buf);
    }
    
    @Override
    public void executeClient(EntityPlayer player) {
        Notes note = Notes.getNoteFromPitch(pitch);
        Minecraft mc = Minecraft.getMinecraft();
        SoundEvent sound = new SoundEvent(new ResourceLocation(note.getResourceLocation(instermentName)));
        if (local)
            mc.world.playSound(pos, sound, SoundCategory.RECORDS, volume, note.getPitch(), true);
        else
            mc.world.playSound(player.getPosition(), sound, SoundCategory.RECORDS, volume, note.getPitch(), true);
    }
    
    @Override
    public void executeServer(EntityPlayer player) {
        
    }
    
}
