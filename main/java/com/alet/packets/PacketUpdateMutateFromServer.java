package com.alet.packets;

import com.alet.components.structures.type.LittleStateMutatorALET;
import com.creativemd.creativecore.common.packet.CreativeCorePacket;
import com.creativemd.littletiles.common.action.LittleAction;
import com.creativemd.littletiles.common.action.LittleActionException;
import com.creativemd.littletiles.common.tile.math.location.StructureLocation;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

public class PacketUpdateMutateFromServer extends CreativeCorePacket {
    
    public StructureLocation location;
    
    public PacketUpdateMutateFromServer() {
        
    }
    
    public PacketUpdateMutateFromServer(StructureLocation location) {
        this.location = location;
    }
    
    @Override
    public void writeBytes(ByteBuf buf) {
        LittleAction.writeStructureLocation(location, buf);
    }
    
    @Override
    public void readBytes(ByteBuf buf) {
        location = LittleAction.readStructureLocation(buf);
    }
    
    @Override
    public void executeClient(EntityPlayer player) {
        try {
            LittleStateMutatorALET structure = (LittleStateMutatorALET) location.find(player.world);
            structure.changeMaterialState();
        } catch (LittleActionException e) {}
        
    }
    
    @Override
    public void executeServer(EntityPlayer player) {}
    
}
