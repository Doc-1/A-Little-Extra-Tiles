package com.alet.common.structure.type.premade.signal;

import com.creativemd.littletiles.common.structure.exception.CorruptedConnectionException;
import com.creativemd.littletiles.common.structure.exception.NotYetConnectedException;
import com.creativemd.littletiles.common.structure.registry.LittleStructureType;
import com.creativemd.littletiles.common.structure.signal.component.ISignalComponent;
import com.creativemd.littletiles.common.structure.type.premade.LittleStructurePremade;
import com.creativemd.littletiles.common.tile.parent.IStructureTileList;

import net.minecraft.util.text.translation.I18n;

public abstract class LittleCircuitPremade extends LittleStructurePremade {
    
    private final int clockIndex;
    private int[] triggerIndexes = new int[] { -1 };
    
    public LittleCircuitPremade(LittleStructureType type, IStructureTileList mainBlock, int enableIndex) {
        super(type, mainBlock);
        this.clockIndex = enableIndex;
    }
    
    public void setTriggerIndexes(int... triggerIndexes) {
        int[] temp = new int[triggerIndexes.length + 2];
        for (int i = 0; i < triggerIndexes.length; i++) {
            temp[i] = triggerIndexes[i];
        }
        temp[triggerIndexes.length + 1] = clockIndex;
        this.triggerIndexes = temp;
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public String info() {
        return I18n.translateToLocal("item.LTPremade." + this.type.id + ".name");
    }
    
    public abstract void trigger();
    
    @Override
    public void changed(ISignalComponent changed) {
        super.changed(changed);
        if (!this.isClient())
            try {
                boolean flag = true;
                if (clockIndex != -1) {
                    ISignalComponent clock = (ISignalComponent) this.getChild(clockIndex).getStructure();
                    if (!clock.getState()[0])
                        flag = false;
                }
                if (flag) {
                    boolean flag1 = false;
                    for (int x : this.triggerIndexes) {
                        if (this.getChild(x) != null) {
                            if (x == -1) {
                                flag1 = true;
                                break;
                            }
                            if (this.getChild(x).getStructure().equals(changed)) {
                                flag1 = true;
                                break;
                            }
                        }
                    }
                    if (flag1)
                        this.trigger();
                    
                    /*
                      if (this.clockOutIndex != -1) {
                         ISignalComponent outClock = (ISignalComponent) this.getChild(clockOutIndex).getStructure();
                         outClock.updateState(changed.getState());
                      }*/
                }
            } catch (CorruptedConnectionException | NotYetConnectedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
    }
}
