package com.alet.common.structure.type.premade.signal;

import com.alet.common.util.SignalingUtils;
import com.creativemd.littletiles.common.structure.exception.CorruptedConnectionException;
import com.creativemd.littletiles.common.structure.exception.NotYetConnectedException;
import com.creativemd.littletiles.common.structure.registry.LittleStructureType;
import com.creativemd.littletiles.common.structure.type.door.LittleAdvancedDoor;
import com.creativemd.littletiles.common.structure.type.premade.signal.LittleSignalInput;
import com.creativemd.littletiles.common.structure.type.premade.signal.LittleSignalOutput;
import com.creativemd.littletiles.common.tile.parent.IStructureTileList;

import net.minecraft.nbt.NBTTagCompound;

public class LittleCircuitSignalSwitch extends LittleCircuitPremade {
    
    public LittleCircuitSignalSwitch(LittleStructureType type, IStructureTileList mainBlock) {
        super(type, mainBlock, 2, 3);
        // TODO Auto-generated constructor stub
    }
    
    @Override
    protected void loadFromNBTExtra(NBTTagCompound nbt) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    protected void writeToNBTExtra(NBTTagCompound nbt) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void trigger() {}
    
    @Override
    public void tick() {
        if (!this.isClient()) {
            try {
                LittleSignalInput source = (LittleSignalInput) this.children.get(0).getStructure();
                LittleSignalOutput output = (LittleSignalOutput) this.children.get(1).getStructure();
                if (pulse)
                    output.updateState(source.getState());
                else
                    output.updateState(SignalingUtils.allFalse(output.getBandwidth()));
                LittleAdvancedDoor door = (LittleAdvancedDoor) this.children.get(4).getStructure();
                door.getOutput(0).updateState(new boolean[] { pulse });
            } catch (CorruptedConnectionException | NotYetConnectedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        
    }
}
