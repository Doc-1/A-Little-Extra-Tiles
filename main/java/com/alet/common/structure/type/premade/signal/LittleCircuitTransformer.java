package com.alet.common.structure.type.premade.signal;

import com.creativemd.littletiles.common.structure.exception.CorruptedConnectionException;
import com.creativemd.littletiles.common.structure.exception.NotYetConnectedException;
import com.creativemd.littletiles.common.structure.registry.LittleStructureType;
import com.creativemd.littletiles.common.structure.type.premade.LittleStructurePremade;
import com.creativemd.littletiles.common.structure.type.premade.signal.LittleSignalInput;
import com.creativemd.littletiles.common.structure.type.premade.signal.LittleSignalOutput;
import com.creativemd.littletiles.common.tile.parent.IStructureTileList;

import net.minecraft.nbt.NBTTagCompound;

public class LittleCircuitTransformer extends LittleStructurePremade {
    
    public LittleCircuitTransformer(LittleStructureType type, IStructureTileList mainBlock) {
        super(type, mainBlock);
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
    
    public void highToLow() {
        
        try {
            LittleSignalInput input = (LittleSignalInput) this.children.get(0).getStructure();
            int bandwith = input.getState().length / 4;
            for (int i = 0; i < 4; i++) {
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
    
    public void lowToHigh() {
        try {
            LittleSignalOutput output = (LittleSignalOutput) this.children.get(0).getStructure();
            int inBandwith = output.getState().length / 4;
            int outBandwith = output.getState().length;
            
            boolean[] state = new boolean[outBandwith];
            for (int i = 0; i < 4; i++) {
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
    public void tick() {
        if (!this.isClient()) {
            if (this.type.id.equals("4_to_1_transformer") || this.type.id.equals("16_to_4_transformer") || this.type.id.equals("32_to_16_transformer"))
                highToLow();
            
            else if (this.type.id.equals("1_to_4_transformer") || this.type.id.equals("4_to_16_transformer") || this.type.id.equals("16_to_32_transformer"))
                lowToHigh();
        }
    }
}
