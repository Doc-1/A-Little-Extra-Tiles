package com.alet.common.structure.type.premade.signal;

import com.creativemd.littletiles.common.structure.exception.CorruptedConnectionException;
import com.creativemd.littletiles.common.structure.exception.NotYetConnectedException;
import com.creativemd.littletiles.common.structure.registry.LittleStructureType;
import com.creativemd.littletiles.common.structure.type.premade.LittleStructurePremade;
import com.creativemd.littletiles.common.structure.type.premade.signal.LittleSignalOutput;
import com.creativemd.littletiles.common.tile.parent.IStructureTileList;

import net.minecraft.nbt.NBTTagCompound;

public class LittleCircuitClock extends LittleStructurePremade {
    
    private boolean pulse = false;
    private int tickCount = 1;
    
    public LittleCircuitClock(LittleStructureType type, IStructureTileList mainBlock) {
        super(type, mainBlock);
        
    }
    
    @Override
    public void tick() {
        if (!isClient())
            try {
                LittleSignalOutput out = (LittleSignalOutput) this.children.get(0).getStructure();
                pulse = tickCounting();
                boolean[] state = { pulse };
                out.updateState(state);
            } catch (CorruptedConnectionException | NotYetConnectedException e) {
                e.printStackTrace();
            }
    }
    
    private boolean tickCounting() {
        String id = this.type.id;
        tickCount++;
        if (id.equals("clock_10hz")) {
            if (tickCount > 1) {
                tickCount = 0;
                return true;
            }
        } else if (id.equals("clock_5hz")) {
            if (tickCount > 2) {
                if (tickCount > 3)
                    tickCount = 0;
                return true;
            }
        } else if (id.equals("clock_2hz")) {
            if (tickCount > 4) {
                if (tickCount > 7)
                    tickCount = 0;
                return true;
            }
        } else if (id.equals("clock_1hz")) {
            if (tickCount > 10) {
                if (tickCount > 19)
                    tickCount = 0;
                return true;
            }
        }
        return false;
    }
    
    @Override
    protected void loadFromNBTExtra(NBTTagCompound nbt) {
        if (nbt.hasKey("pulse"))
            pulse = nbt.getBoolean("pulse");
        if (nbt.hasKey("tick"))
            tickCount = nbt.getInteger("tick");
    }
    
    @Override
    protected void writeToNBTExtra(NBTTagCompound nbt) {
        nbt.setBoolean("pulse", pulse);
        nbt.setInteger("tick", tickCount);
    }
    
}
