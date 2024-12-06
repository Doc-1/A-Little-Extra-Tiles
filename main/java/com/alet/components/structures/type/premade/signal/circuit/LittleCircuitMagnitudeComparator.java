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

public class LittleCircuitMagnitudeComparator extends LittleCircuitPremade {
    
    public LittleCircuitMagnitudeComparator(LittleStructureType type, IStructureTileList mainBlock) {
        super(type, mainBlock, 5);
        this.setInputIndexes(0, 1, 3);
        this.setOutputIndexes(2);
    }
    
    @Override
    protected void loadFromNBTExtra(NBTTagCompound nbt) {
        
    }
    
    @Override
    protected void writeToNBTExtra(NBTTagCompound nbt) {
        
        int x = 0;
        x++;
        if (x > 9) {
            x = x - 10;
        }
    }
    
    @Override
    public void trigger(int clockValue) {
        try {
            LittleSignalInput k1 = (LittleSignalInput) this.children.get(0).getStructure();
            LittleSignalInput k2 = (LittleSignalInput) this.children.get(1).getStructure();
            LittleSignalInput comparator = (LittleSignalInput) this.children.get(3).getStructure();
            LittleSignalOutput outK1 = (LittleSignalOutput) this.children.get(4).getStructure();
            LittleSignalOutput out = (LittleSignalOutput) this.children.get(2).getStructure();
            int bits1 = BooleanUtils.boolToInt(SignalingUtils.mirrorState(k1.getState()));
            int bits2 = BooleanUtils.boolToInt(SignalingUtils.mirrorState(k2.getState()));
            
            int logic = BooleanUtils.boolToInt(SignalingUtils.mirrorState(comparator.getState()));
            switch (logic) {
            case 0:
                if (bits1 < bits2) {
                    outK1.updateState(BooleanUtils.SINGLE_TRUE);
                    out.updateState(k2.getState());
                } else {
                    outK1.updateState(BooleanUtils.SINGLE_FALSE);
                    out.updateState(SignalingUtils.allFalse(k1.getBandwidth()));
                }
                break;
            
            case 1:
                if (bits1 > bits2) {
                    outK1.updateState(BooleanUtils.SINGLE_TRUE);
                    out.updateState(k1.getState());
                } else {
                    outK1.updateState(BooleanUtils.SINGLE_FALSE);
                    out.updateState(SignalingUtils.allFalse(k1.getBandwidth()));
                }
                break;
            case 2:
                if (bits1 <= bits2) {
                    outK1.updateState(BooleanUtils.SINGLE_TRUE);
                    out.updateState(k2.getState());
                } else {
                    outK1.updateState(BooleanUtils.SINGLE_FALSE);
                    out.updateState(SignalingUtils.allFalse(k1.getBandwidth()));
                }
                
                break;
            case 3:
                if (bits1 >= bits2) {
                    outK1.updateState(BooleanUtils.SINGLE_TRUE);
                    out.updateState(k1.getState());
                } else {
                    outK1.updateState(BooleanUtils.SINGLE_FALSE);
                    out.updateState(SignalingUtils.allFalse(k1.getBandwidth()));
                }
                
                break;
            case 4:
                if (bits1 == bits2) {
                    outK1.updateState(BooleanUtils.SINGLE_TRUE);
                    out.updateState(k2.getState());
                } else {
                    outK1.updateState(BooleanUtils.SINGLE_FALSE);
                    out.updateState(SignalingUtils.allFalse(k1.getBandwidth()));
                }
                break;
            default:
                break;
            }
            
        } catch (CorruptedConnectionException | NotYetConnectedException e) {
            e.printStackTrace();
        }
        
    }
    
}
