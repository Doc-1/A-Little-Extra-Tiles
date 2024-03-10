package com.alet.common.structure.type.premade.signal;

import com.alet.common.utils.SignalingUtils;
import com.creativemd.creativecore.common.utils.math.BooleanUtils;
import com.creativemd.littletiles.common.structure.exception.CorruptedConnectionException;
import com.creativemd.littletiles.common.structure.exception.NotYetConnectedException;
import com.creativemd.littletiles.common.structure.registry.LittleStructureType;
import com.creativemd.littletiles.common.structure.type.premade.signal.LittleSignalInput;
import com.creativemd.littletiles.common.structure.type.premade.signal.LittleSignalOutput;
import com.creativemd.littletiles.common.tile.parent.IStructureTileList;

import net.minecraft.nbt.NBTTagCompound;

public class LittleCircuitRandomNumber extends LittleCircuitPremade {
    
    public LittleCircuitRandomNumber(LittleStructureType type, IStructureTileList mainBlock) {
        super(type, mainBlock, 3);
        this.setInputIndexes(1, 2);
    }
    
    public void oneBitRand() {
        try {
            LittleSignalOutput out = (LittleSignalOutput) this.children.get(0).getStructure();
            out.updateState(SignalingUtils.randState(1));
            
        } catch (CorruptedConnectionException | NotYetConnectedException e) {
            e.printStackTrace();
        }
    }
    
    public void anyBitRand(int bandwidth) {
        
        try {
            LittleSignalOutput out = (LittleSignalOutput) this.children.get(0).getStructure();
            LittleSignalInput min = (LittleSignalInput) this.children.get(1).getStructure();
            LittleSignalInput max = (LittleSignalInput) this.children.get(2).getStructure();
            
            boolean[] state = SignalingUtils
                    .randState(BooleanUtils.boolToInt(SignalingUtils.mirrorState(min.getState())), BooleanUtils.boolToInt(SignalingUtils.mirrorState(max.getState())), bandwidth);
            out.updateState(state);
            
        } catch (CorruptedConnectionException | NotYetConnectedException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    protected void loadFromNBTExtra(NBTTagCompound nbt) {}
    
    @Override
    protected void writeToNBTExtra(NBTTagCompound nbt) {}
    
    @Override
    public void trigger(int clockValue) {
        if (clockValue == 1) {
            if (this.type.id.equals("random_generator_1bit"))
                oneBitRand();
            else if (this.type.id.equals("random_generator_4bit"))
                anyBitRand(4);
            else if (this.type.id.equals("random_generator_16bit"))
                anyBitRand(16);
            else if (this.type.id.equals("random_generator_32bit"))
                anyBitRand(32);
        } else {
            try {
                LittleSignalOutput out = (LittleSignalOutput) this.children.get(0).getStructure();
                out.updateState(SignalingUtils.allFalse(32));
            } catch (CorruptedConnectionException | NotYetConnectedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        
    }
    
}
