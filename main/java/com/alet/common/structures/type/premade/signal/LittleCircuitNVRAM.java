package com.alet.common.structures.type.premade.signal;

import com.alet.common.utils.SignalingUtils;
import com.creativemd.creativecore.common.utils.math.BooleanUtils;
import com.creativemd.littletiles.common.structure.exception.CorruptedConnectionException;
import com.creativemd.littletiles.common.structure.exception.NotYetConnectedException;
import com.creativemd.littletiles.common.structure.registry.LittleStructureType;
import com.creativemd.littletiles.common.structure.type.premade.signal.LittleSignalInput;
import com.creativemd.littletiles.common.structure.type.premade.signal.LittleSignalOutput;
import com.creativemd.littletiles.common.tile.parent.IStructureTileList;

import net.minecraft.nbt.NBTTagCompound;

public class LittleCircuitNVRAM extends LittleCircuitPremade {
    
    boolean[] state;
    
    public LittleCircuitNVRAM(LittleStructureType type, IStructureTileList mainBlock) {
        super(type, mainBlock, 4);
        this.setInputIndexes(0, 1, 3);
        this.setOutputIndexes(2);
    }
    
    @Override
    protected void loadFromNBTExtra(NBTTagCompound nbt) {
        if (nbt.hasKey("memory"))
            state = BooleanUtils.toBits(nbt.getInteger("memory"), 32);
    }
    
    @Override
    protected void writeToNBTExtra(NBTTagCompound nbt) {
        if (state != null)
            nbt.setInteger("memory", SignalingUtils.boolToInt(state));
        
    }
    
    @Override
    public void trigger(int clockValue) {
        try {
            LittleSignalOutput output = (LittleSignalOutput) this.children.get(2).getStructure();
            LittleSignalInput wipe = (LittleSignalInput) this.children.get(0).getStructure();
            LittleSignalInput save = (LittleSignalInput) this.children.get(3).getStructure();
            LittleSignalInput input = (LittleSignalInput) this.children.get(1).getStructure();
            if (wipe.getState()[0]) {
                state = SignalingUtils.allFalse(output.getBandwidth());
            } else if (save.getState()[0]) {
                state = input.getState().clone();
            }
            
            if (state != null) {
                output.updateState(state);
            }
        } catch (CorruptedConnectionException | NotYetConnectedException e) {
            e.printStackTrace();
        }
    }
    
}
