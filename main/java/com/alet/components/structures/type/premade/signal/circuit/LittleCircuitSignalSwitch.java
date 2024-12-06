package com.alet.components.structures.type.premade.signal.circuit;

import com.alet.common.utils.SignalingUtils;
import com.creativemd.littletiles.common.structure.exception.CorruptedConnectionException;
import com.creativemd.littletiles.common.structure.exception.NotYetConnectedException;
import com.creativemd.littletiles.common.structure.registry.LittleStructureType;
import com.creativemd.littletiles.common.structure.type.premade.signal.LittleSignalInput;
import com.creativemd.littletiles.common.structure.type.premade.signal.LittleSignalOutput;
import com.creativemd.littletiles.common.tile.parent.IStructureTileList;

import net.minecraft.nbt.NBTTagCompound;

public class LittleCircuitSignalSwitch extends LittleCircuitPremade {
    
    public LittleCircuitSignalSwitch(LittleStructureType type, IStructureTileList mainBlock) {
        super(type, mainBlock, 2);
        this.checkForZero = true;
        this.setInputIndexes(0);
        this.setOutputIndexes(1);
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
    public void trigger(int clockValue) {
        try {
            LittleSignalInput source = (LittleSignalInput) this.children.get(0).getStructure();
            LittleSignalOutput output = (LittleSignalOutput) this.children.get(1).getStructure();
            if (clockValue == 1)
                output.updateState(source.getState());
            else
                output.updateState(SignalingUtils.allFalse(output.getBandwidth()));
        } catch (CorruptedConnectionException | NotYetConnectedException e) {
            e.printStackTrace();
        }
    }
    
}
