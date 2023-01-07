package com.alet.common.structure.type.premade.signal;

import com.alet.common.util.SignalingUtils;
import com.creativemd.littletiles.common.structure.exception.CorruptedConnectionException;
import com.creativemd.littletiles.common.structure.exception.NotYetConnectedException;
import com.creativemd.littletiles.common.structure.registry.LittleStructureType;
import com.creativemd.littletiles.common.structure.signal.component.ISignalComponent;
import com.creativemd.littletiles.common.structure.type.premade.LittleStructurePremade;
import com.creativemd.littletiles.common.structure.type.premade.signal.LittleSignalInput;
import com.creativemd.littletiles.common.structure.type.premade.signal.LittleSignalOutput;
import com.creativemd.littletiles.common.tile.parent.IStructureTileList;

import net.minecraft.util.text.translation.I18n;

public abstract class LittleCircuitPremade extends LittleStructurePremade {
    
    private final int clockIndex;
    private int[] inputIndexes;
    private int[] outputIndexes;
    public boolean checkForZero = false;
    
    public LittleCircuitPremade(LittleStructureType type, IStructureTileList mainBlock, int clockIndex) {
        super(type, mainBlock);
        this.clockIndex = clockIndex;
    }
    
    public void setInputIndexes(int... inputIndexes) {
        int[] temp = new int[inputIndexes.length + 2];
        for (int i = 0; i < inputIndexes.length; i++) {
            temp[i] = inputIndexes[i];
        }
        temp[inputIndexes.length + 1] = clockIndex;
        this.inputIndexes = temp;
    }
    
    public void setOutputIndexes(int... outputIndexes) {
        this.outputIndexes = outputIndexes;
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public String info() {
        return I18n.translateToLocal("item.LTPremade." + this.type.id + ".name");
    }
    
    @Override
    protected void afterPlaced() {
        activate();
    }
    
    @Override
    public boolean queueTick() {
        activate();
        
        return false;
    }
    
    public void activate() {
        if (!this.isClient())
            try {
                LittleSignalInput clock = (LittleSignalInput) this.children.get(this.clockIndex).getStructure();
                if (clock.getState()[0])
                    this.trigger(1);
                else {
                    if (this.outputIndexes != null)
                        for (int ouputIndex : this.outputIndexes) {
                            LittleSignalOutput output = (LittleSignalOutput) this.children.get(ouputIndex).getStructure();
                            output.updateState(SignalingUtils.allFalse(output.getBandwidth()));
                        }
                    if (this.checkForZero) {
                        this.trigger(0);
                    }
                }
            } catch (CorruptedConnectionException | NotYetConnectedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
    }
    
    public abstract void trigger(int clockValue);
    
    @Override
    public void changed(ISignalComponent changed) {
        super.changed(changed);
        if (!this.isClient()) {
            try {
                if (clockIndex != -1) {
                    
                    boolean flag1 = false;
                    for (int x : this.inputIndexes) {
                        if (x == -1) {
                            flag1 = true;
                            break;
                        }
                        if (this.getChild(x) != null) {
                            if (this.getChild(x).getStructure().equals(changed)) {
                                flag1 = true;
                                break;
                            }
                        }
                    }
                    if (flag1) {
                        this.queueForNextTick();
                        
                    }
                }
            } catch (CorruptedConnectionException | NotYetConnectedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}

/*

        super.changed(changed);
        if (!this.isClient())
            try {
                if (clockIndex != -1) {
                    boolean flag = false;
                    ISignalComponent clock = (ISignalComponent) this.getChild(clockIndex).getStructure();
                    if (clock.equals(changed)) {
                        newPulse = clock.getState()[0];
                        if (clock.getState()[0]) {
                            flag = true;
                        }
                        oldPulse = newPulse;
                    }
                    if (flag) {
                        boolean flag1 = false;
                    for (int x : this.triggerIndexes) {
                            if (x == -1) {
                                flag1 = true;
                                break;
                            }
                            if (this.getChild(x) != null) {
                                if (this.getChild(x).getStructure().equals(changed)) {
                                    flag1 = true;
                                    break;
                                }
                            }
                        }
                        if (flag1 && clock.getState()[0]) {
                            this.trigger();
                        }
                    }
                }
                
            } catch (CorruptedConnectionException | NotYetConnectedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
    
*/