package com.alet.common.packet;

import com.creativemd.creativecore.common.packet.CreativeCorePacket;
import com.creativemd.littletiles.common.action.LittleAction;
import com.creativemd.littletiles.common.action.LittleActionException;
import com.creativemd.littletiles.common.structure.LittleStructure;
import com.creativemd.littletiles.common.tile.math.location.StructureLocation;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class PacketUpdateStructureFromClient extends CreativeCorePacket {
    
    public StructureLocation location;
    public NBTTagCompound structureNBT;
    
    public PacketUpdateStructureFromClient() {
        
    }
    
    public PacketUpdateStructureFromClient(StructureLocation location, NBTTagCompound structureNBT) {
        this.location = location;
        this.structureNBT = structureNBT;
    }
    
    @Override
    public void writeBytes(ByteBuf buf) {
        LittleAction.writeStructureLocation(location, buf);
        writeNBT(buf, structureNBT);
    }
    
    @Override
    public void readBytes(ByteBuf buf) {
        location = LittleAction.readStructureLocation(buf);
        structureNBT = readNBT(buf);
    }
    
    @Override
    public void executeClient(EntityPlayer player) {
        
    }
    
    @Override
    public void executeServer(EntityPlayer player) {
        try {
            LittleStructure structure = location.find(player.world);
            
            structure.loadFromNBT(structureNBT);
            
            structure.updateStructure();
            structure.updateSignaling();
        } catch (LittleActionException e) {}
        
    }
    
}
