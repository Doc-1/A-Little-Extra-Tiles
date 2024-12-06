package com.alet.components.structures.type.premade.signal.circuit;

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
        if (!isClient()) {
            try {
                LittleSignalOutput out = (LittleSignalOutput) this.children.get(0).getStructure();
                
                pulse = tickCounting();
                boolean[] state = { pulse };
                
                out.updateState(state);
            } catch (CorruptedConnectionException | NotYetConnectedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
        }
    }
    
    private boolean tickCounting() {
        String id = this.type.id;
        tickCount++;
        
        if (id.equals("clock_10hz")) {
            if (tickCount < 1) {
                return false;
            } else if (tickCount >= 1) {
                if (tickCount >= 2) {
                    tickCount = 0;
                    return false;
                }
                return true;
            }
        } else if (id.equals("clock_5hz")) {
            if (tickCount < 5) {
                return false;
            } else if (tickCount >= 5) {
                if (tickCount >= 10) {
                    tickCount = 0;
                    return false;
                }
                return true;
            }
        } else if (id.equals("clock_2hz")) {
            if (tickCount < 4) {
                return false;
            } else if (tickCount >= 4) {
                if (tickCount >= 8) {
                    tickCount = 0;
                    return false;
                }
                return true;
            }
        } else if (id.equals("clock_1hz")) {
            if (tickCount < 10) {
                return false;
            } else if (tickCount >= 10) {
                if (tickCount >= 20) {
                    tickCount = 0;
                    return false;
                }
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
