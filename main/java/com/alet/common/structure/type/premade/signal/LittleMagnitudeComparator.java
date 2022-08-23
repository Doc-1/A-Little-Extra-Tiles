package com.alet.common.structure.type.premade.signal;

import com.alet.common.util.SignalingUtils;
import com.creativemd.creativecore.common.utils.math.BooleanUtils;
import com.creativemd.littletiles.common.structure.exception.CorruptedConnectionException;
import com.creativemd.littletiles.common.structure.exception.NotYetConnectedException;
import com.creativemd.littletiles.common.structure.registry.LittleStructureType;
import com.creativemd.littletiles.common.structure.type.premade.LittleStructurePremade;
import com.creativemd.littletiles.common.structure.type.premade.signal.LittleSignalInput;
import com.creativemd.littletiles.common.structure.type.premade.signal.LittleSignalOutput;
import com.creativemd.littletiles.common.tile.parent.IStructureTileList;

import net.minecraft.nbt.NBTTagCompound;

public class LittleMagnitudeComparator extends LittleStructurePremade {
    
    public LittleMagnitudeComparator(LittleStructureType type, IStructureTileList mainBlock) {
        super(type, mainBlock);
    }
    
    @Override
    protected void loadFromNBTExtra(NBTTagCompound nbt) {
        
    }
    
    @Override
    protected void writeToNBTExtra(NBTTagCompound nbt) {}
    
    @Override
    public void tick() {
        if (!this.isClient())
            try {
                LittleSignalInput k1 = (LittleSignalInput) this.children.get(0).getStructure();
                LittleSignalInput k2 = (LittleSignalInput) this.children.get(1).getStructure();
                LittleSignalInput comparator = (LittleSignalInput) this.children.get(2).getStructure();
                LittleSignalOutput outK1 = (LittleSignalOutput) this.children.get(3).getStructure();
                LittleSignalOutput outK2 = (LittleSignalOutput) this.children.get(4).getStructure();
                LittleSignalOutput out = (LittleSignalOutput) this.children.get(5).getStructure();
                int bits1 = BooleanUtils.boolToInt(SignalingUtils.mirrorState(k1.getState()));
                int bits2 = BooleanUtils.boolToInt(SignalingUtils.mirrorState(k2.getState()));
                
                int logic = BooleanUtils.boolToInt(SignalingUtils.mirrorState(comparator.getState()));
                switch (logic) {
                case 0:
                    if (bits1 < bits2) {
                        outK1.updateState(BooleanUtils.SINGLE_FALSE);
                        outK2.updateState(BooleanUtils.SINGLE_TRUE);
                        out.updateState(k2.getState());
                    } else {
                        outK1.updateState(BooleanUtils.SINGLE_FALSE);
                        outK2.updateState(BooleanUtils.SINGLE_FALSE);
                        out.updateState(SignalingUtils.allFalse(k1.getBandwidth()));
                    }
                    break;
                
                case 1:
                    if (bits1 > bits2) {
                        outK1.updateState(BooleanUtils.SINGLE_TRUE);
                        outK2.updateState(BooleanUtils.SINGLE_FALSE);
                        out.updateState(k1.getState());
                    } else {
                        outK1.updateState(BooleanUtils.SINGLE_FALSE);
                        outK2.updateState(BooleanUtils.SINGLE_FALSE);
                        out.updateState(SignalingUtils.allFalse(k1.getBandwidth()));
                    }
                    break;
                case 2:
                    if (bits1 <= bits2) {
                        outK1.updateState(BooleanUtils.SINGLE_FALSE);
                        outK2.updateState(BooleanUtils.SINGLE_TRUE);
                        out.updateState(k2.getState());
                    } else {
                        outK1.updateState(BooleanUtils.SINGLE_FALSE);
                        outK2.updateState(BooleanUtils.SINGLE_FALSE);
                        out.updateState(SignalingUtils.allFalse(k1.getBandwidth()));
                    }
                    
                    break;
                case 3:
                    if (bits1 >= bits2) {
                        outK1.updateState(BooleanUtils.SINGLE_TRUE);
                        outK2.updateState(BooleanUtils.SINGLE_FALSE);
                        out.updateState(k1.getState());
                    } else {
                        outK1.updateState(BooleanUtils.SINGLE_FALSE);
                        outK2.updateState(BooleanUtils.SINGLE_FALSE);
                        out.updateState(SignalingUtils.allFalse(k1.getBandwidth()));
                    }
                    
                    break;
                case 4:
                    if (bits1 == bits2) {
                        outK1.updateState(BooleanUtils.SINGLE_TRUE);
                        outK2.updateState(BooleanUtils.SINGLE_TRUE);
                        out.updateState(k2.getState());
                    } else {
                        outK1.updateState(BooleanUtils.SINGLE_FALSE);
                        outK2.updateState(BooleanUtils.SINGLE_FALSE);
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
