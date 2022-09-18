package com.alet.common.packet;

import com.alet.common.event.ALETEventHandler;
import com.creativemd.creativecore.common.packet.CreativeCorePacket;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;

public class PacketLeftClick extends CreativeCorePacket {
    BlockPos pos;
    
    public PacketLeftClick() {
        
    }
    
    public PacketLeftClick(BlockPos pos) {
        this.pos = pos;
    }
    
    @Override
    public void writeBytes(ByteBuf buf) {
        writePos(buf, pos);
    }
    
    @Override
    public void readBytes(ByteBuf buf) {
        pos = readPos(buf);
    }
    
    @Override
    public void executeClient(EntityPlayer player) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void executeServer(EntityPlayer player) {
        ALETEventHandler.isStructure(player.getEntityWorld(), pos, player);
    }
    
}
