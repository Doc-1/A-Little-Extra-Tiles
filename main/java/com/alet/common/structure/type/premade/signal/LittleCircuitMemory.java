package com.alet.common.structure.type.premade.signal;

import com.alet.common.util.SignalingUtils;
import com.creativemd.creativecore.common.utils.math.BooleanUtils;
import com.creativemd.littletiles.common.structure.exception.CorruptedConnectionException;
import com.creativemd.littletiles.common.structure.exception.NotYetConnectedException;
import com.creativemd.littletiles.common.structure.registry.LittleStructureType;
import com.creativemd.littletiles.common.structure.type.premade.signal.LittleSignalInput;
import com.creativemd.littletiles.common.structure.type.premade.signal.LittleSignalOutput;
import com.creativemd.littletiles.common.tile.parent.IStructureTileList;

import net.minecraft.nbt.NBTTagCompound;

public class LittleCircuitMemory extends LittleCircuitPremade {
    
    boolean[] state;
    
    public LittleCircuitMemory(LittleStructureType type, IStructureTileList mainBlock) {
        super(type, mainBlock, 1, 4);
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
    public void trigger() {
        try {
            LittleSignalOutput output = (LittleSignalOutput) this.children.get(3).getStructure();
            LittleSignalInput wipe = (LittleSignalInput) this.children.get(0).getStructure();
            LittleSignalInput input = (LittleSignalInput) this.children.get(2).getStructure();
            if (!wipe.getState()[0]) {
                state = input.getState().clone();
            } else {
                state = SignalingUtils.allFalse(output.getBandwidth());
            }
            
            if (state != null) {
                output.updateState(state);
            }
        } catch (CorruptedConnectionException | NotYetConnectedException e) {
            e.printStackTrace();
        }
    }
    
}
