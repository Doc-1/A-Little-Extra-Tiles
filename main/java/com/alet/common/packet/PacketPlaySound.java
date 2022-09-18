package com.alet.common.packet;

import com.creativemd.creativecore.common.packet.CreativeCorePacket;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PacketPlaySound extends CreativeCorePacket {
    
    private float pitch;
    private float volume;
    private boolean local;
    private BlockPos pos;
    private String resourcePath;
    
    public PacketPlaySound() {
        // TODO Auto-generated constructor stub
    }
    
    public PacketPlaySound(float pitch, float volume, boolean local, BlockPos pos, String resourcePath) {
        this.pitch = pitch;
        this.volume = volume;
        this.local = local;
        this.pos = pos;
        this.resourcePath = resourcePath;
    }
    
    @Override
    public void writeBytes(ByteBuf buf) {
        buf.writeFloat(pitch);
        buf.writeFloat(volume);
        buf.writeBoolean(local);
        writeNBT(buf, NBTUtil.createPosTag(pos));
        writeString(buf, resourcePath);
    }
    
    @Override
    public void readBytes(ByteBuf buf) {
        this.pitch = buf.readFloat();
        this.volume = buf.readFloat();
        this.local = buf.readBoolean();
        this.pos = NBTUtil.getPosFromTag(readNBT(buf));
        this.resourcePath = readString(buf);
    }
    
    @Override
    public void executeClient(EntityPlayer player) {
        World world = player.world;
        SoundEvent event = new SoundEvent(new ResourceLocation(this.resourcePath));
        world.playSound(player, pos, event, SoundCategory.NEUTRAL, volume, pitch);
    }
    
    @Override
    public void executeServer(EntityPlayer player) {
        // TODO Auto-generated method stub
        
    }
    
}
