package com.alet.components.structures.type.premade.signal.circuit;

import com.alet.common.utils.SignalingUtils;
import com.creativemd.creativecore.common.utils.math.BooleanUtils;
import com.creativemd.littletiles.common.structure.exception.CorruptedConnectionException;
import com.creativemd.littletiles.common.structure.exception.NotYetConnectedException;
import com.creativemd.littletiles.common.structure.registry.LittleStructureType;
import com.creativemd.littletiles.common.structure.type.premade.signal.LittleSignalInput;
import com.creativemd.littletiles.common.structure.type.premade.signal.LittleSignalOutput;
import com.creativemd.littletiles.common.tile.parent.IStructureTileList;

import net.minecraft.nbt.NBTTagCompound;

public class LittleCircuitDecimalToAscii extends LittleCircuitPremade {
    
    public LittleCircuitDecimalToAscii(LittleStructureType type, IStructureTileList mainBlock) {
        super(type, mainBlock, 3);
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
    public void trigger(int clockValue) {
        try {
            LittleSignalInput input = (LittleSignalInput) this.children.get(0).getStructure();
            LittleSignalOutput output = (LittleSignalOutput) this.children.get(1).getStructure();
            int z = BooleanUtils.boolToInt(SignalingUtils.mirrorState(input.getState()));
            char c = (char) z;
            boolean[] ascii = new boolean[8];
            BooleanUtils.intToBool((int) c, ascii);
        } catch (CorruptedConnectionException | NotYetConnectedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
    
}
