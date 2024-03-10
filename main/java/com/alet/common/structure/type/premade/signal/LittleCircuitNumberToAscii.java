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

public class LittleCircuitNumberToAscii extends LittleCircuitPremade {
    
    public LittleCircuitNumberToAscii(LittleStructureType type, IStructureTileList mainBlock) {
        super(type, mainBlock, 3);
        this.setInputIndexes(0);
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
            LittleSignalOutput leftover = (LittleSignalOutput) this.children.get(2).getStructure();
            int z = BooleanUtils.boolToInt(SignalingUtils.mirrorState(input.getState()));
            char c = (z + "").charAt(0);
            boolean[] ascii = new boolean[8];
            BooleanUtils.intToBool((int) c, ascii);
            output.updateState(SignalingUtils.mirrorState(ascii));
            if ((z + "").length() > 1) {
                String extra = (z + "").substring(1);
                boolean[] extraBits = new boolean[32];
                BooleanUtils.intToBool(Integer.parseInt(extra), extraBits);
                leftover.updateState(SignalingUtils.mirrorState(extraBits));
            } else {
                leftover.updateState(SignalingUtils.allFalse(32));
            }
            
        } catch (NumberFormatException | CorruptedConnectionException | NotYetConnectedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
    
}
