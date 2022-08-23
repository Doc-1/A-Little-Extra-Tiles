package com.alet.common.structure.type.premade.signal;

import com.creativemd.littletiles.common.structure.exception.CorruptedConnectionException;
import com.creativemd.littletiles.common.structure.exception.NotYetConnectedException;
import com.creativemd.littletiles.common.structure.registry.LittleStructureType;
import com.creativemd.littletiles.common.structure.signal.component.ISignalComponent;
import com.creativemd.littletiles.common.structure.type.premade.LittleStructurePremade;
import com.creativemd.littletiles.common.tile.parent.IStructureTileList;

public abstract class LittleCircuitPremade extends LittleStructurePremade {
    
    boolean pulse = false;
    private boolean oldPulse = false;
    private final int clockIndex;
    
    public LittleCircuitPremade(LittleStructureType type, IStructureTileList mainBlock, int clockIndex) {
        super(type, mainBlock);
        this.clockIndex = clockIndex;
    }
    
    @Override
    public String info() {
        return this.type.id;
    }
    
    @Override
    public void changed(ISignalComponent changed) {
        super.changed(changed);
        if (this.clockIndex != -1)
            try {
                if (this.getChild(clockIndex).getStructure().equals(changed)) {
                    pulse = changed.getState()[0];
                }
            } catch (CorruptedConnectionException | NotYetConnectedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
    }
}
