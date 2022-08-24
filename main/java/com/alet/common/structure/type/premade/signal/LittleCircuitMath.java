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

public class LittleCircuitMath extends LittleCircuitPremade {
    
    public LittleCircuitMath(LittleStructureType type, IStructureTileList mainBlock) {
        super(type, mainBlock, 4, 5);
        // TODO Auto-generated constructor stub
    }
    
    public void math(char operator) throws CorruptedConnectionException, NotYetConnectedException {
        LittleSignalInput in1 = (LittleSignalInput) this.children.get(1).getStructure();
        LittleSignalInput in2 = (LittleSignalInput) this.children.get(2).getStructure();
        
        LittleSignalOutput out1 = (LittleSignalOutput) this.children.get(3).getStructure();
        int x1 = SignalingUtils.boolToInt(in1.getState());
        int x2 = SignalingUtils.boolToInt(in2.getState());
        if (operator == '+')
            out1.updateState(BooleanUtils.toBits(x1 + x2, out1.getBandwidth()));
        else if (operator == '-')
            out1.updateState(BooleanUtils.toBits(x1 - x2, out1.getBandwidth()));
        else if (operator == '*')
            out1.updateState(BooleanUtils.toBits(x1 * x2, out1.getBandwidth()));
        else if (operator == '/')
            out1.updateState(BooleanUtils.toBits(x1 / x2, out1.getBandwidth()));
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
    public void trigger() {
        try {
            LittleSignalInput math = (LittleSignalInput) this.children.get(0).getStructure();
            int logic = SignalingUtils.boolToInt(math.getState());
            switch (logic) {
            case 0:
                math('+');
                break;
            case 1:
                math('-');
                break;
            case 2:
                math('*');
                break;
            case 3:
                math('/');
                break;
            
            default:
                break;
            }
        } catch (CorruptedConnectionException | NotYetConnectedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
    
}
