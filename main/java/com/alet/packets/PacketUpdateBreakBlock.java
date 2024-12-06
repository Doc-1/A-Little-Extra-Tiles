package com.alet.packets;

import com.creativemd.creativecore.common.packet.CreativeCorePacket;
import com.creativemd.littletiles.common.action.LittleAction;
import com.creativemd.littletiles.common.tile.math.location.StructureLocation;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

public class PacketUpdateBreakBlock extends CreativeCorePacket {
    
    public StructureLocation location;
    
    public PacketUpdateBreakBlock() {
        
    }
    
    public PacketUpdateBreakBlock(StructureLocation location) {
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
        
    }
    
    @Override
    public void executeServer(EntityPlayer player) {
        /*
        try {
            LittleTriggerBoxStructureALET structure = (LittleTriggerBoxStructureALET) location.find(player.world);
            structure.breakBlock = LittleAction.isUsingSecondMode(player);
            structure.getInput(2).updateState(BooleanUtils.asArray(!structure.getInput(2).getState()[0]));
            if (!structure.breakBlock) {
                structure.entities.add(player);
                structure.queueForNextTick();
            }
        } catch (LittleActionException e) {}
        */
    }
    
}
