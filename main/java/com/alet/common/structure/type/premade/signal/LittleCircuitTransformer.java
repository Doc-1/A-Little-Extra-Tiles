package com.alet.common.structure.type.premade.signal;

import com.creativemd.littletiles.common.structure.exception.CorruptedConnectionException;
import com.creativemd.littletiles.common.structure.exception.NotYetConnectedException;
import com.creativemd.littletiles.common.structure.registry.LittleStructureType;
import com.creativemd.littletiles.common.structure.type.premade.signal.LittleSignalInput;
import com.creativemd.littletiles.common.structure.type.premade.signal.LittleSignalOutput;
import com.creativemd.littletiles.common.tile.parent.IStructureTileList;

import net.minecraft.nbt.NBTTagCompound;

public class LittleCircuitTransformer extends LittleCircuitPremade {
    
    public LittleCircuitTransformer(LittleStructureType type, IStructureTileList mainBlock) {
        super(type, mainBlock, -1);
        if (this.type.id.contains("32"))
            if (this.type.id.contains("splitter"))
                setInputIndexes(0);
            else
                setInputIndexes(1, 2);
        else {
            if (this.type.id.contains("splitter"))
                setInputIndexes(0);
            else
                setInputIndexes(1, 2, 3, 4);
        }
    }
    
    @Override
    protected void loadFromNBTExtra(NBTTagCompound nbt) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    protected void writeToNBTExtra(NBTTagCompound nbt) {
        // TODO Auto-generated method stub
        
    }
    
    public void highToLow(int splitValue) {
        
        try {
            LittleSignalInput input = (LittleSignalInput) this.children.get(0).getStructure();
            int bandwith = input.getState().length / splitValue;
            for (int i = 0; i < splitValue; i++) {
                boolean[] state = new boolean[bandwith];
                LittleSignalOutput output = (LittleSignalOutput) this.children.get(i + 1).getStructure();
                int start = i * bandwith;
                int counter = 0;
                for (int j = start; j < start + bandwith; j++) {
                    state[counter] = input.getState()[j];
                    counter++;
                }
                output.updateState(state);
            }
        } catch (CorruptedConnectionException | NotYetConnectedException e) {}
        
    }
    
    public void lowToHigh(int splitValue) {
        try {
            LittleSignalOutput output = (LittleSignalOutput) this.children.get(0).getStructure();
            int inBandwith = output.getState().length / splitValue;
            int outBandwith = output.getState().length;
            
            boolean[] state = new boolean[outBandwith];
            for (int i = 0; i < splitValue; i++) {
                LittleSignalInput input = (LittleSignalInput) this.children.get(i + 1).getStructure();
                int counter = 0;
                int start = (i * inBandwith);
                for (int j = start; j < start + inBandwith; j++) {
                    state[j] = input.getState()[counter];
                    counter++;
                }
            }
            output.updateState(state);
        } catch (CorruptedConnectionException | NotYetConnectedException e) {}
        
    }
    
    @Override
    public void trigger(int clockValue) {
        if (this.type.id.contains("splitter")) {
            if (this.type.id.equals("splitter_4_to_1") || this.type.id.equals("splitter_16_to_4"))
                highToLow(4);
            else if (this.type.id.equals("splitter_32_to_16"))
                highToLow(2);
        } else if (this.type.id.contains("combiner")) {
            if (this.type.id.equals("combiner_1_to_4") || this.type.id.equals("combiner_4_to_16"))
                lowToHigh(4);
            else if (this.type.id.equals("combiner_16_to_32"))
                lowToHigh(2);
        }
        
    }
}
