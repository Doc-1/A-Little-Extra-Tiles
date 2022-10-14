package com.alet.common.structure.type.premade.signal;

import com.alet.common.util.SignalingUtils;
import com.creativemd.littletiles.common.structure.exception.CorruptedConnectionException;
import com.creativemd.littletiles.common.structure.exception.NotYetConnectedException;
import com.creativemd.littletiles.common.structure.registry.LittleStructureType;
import com.creativemd.littletiles.common.structure.type.premade.signal.LittleSignalInput;
import com.creativemd.littletiles.common.structure.type.premade.signal.LittleSignalOutput;
import com.creativemd.littletiles.common.tile.parent.IStructureTileList;

import net.minecraft.nbt.NBTTagCompound;

public class LittleCircuitPulser extends LittleCircuitPremade {
    private boolean start = true;
    private int counter = 0;
    private int max = 0;
    private int tickCount = 0;
    
    public LittleCircuitPulser(LittleStructureType type, IStructureTileList mainBlock) {
        super(type, mainBlock, 1);
    }
    
    private boolean tickCounting() {
        tickCount++;
        int tickMax = 5;
        if (tickCount > tickMax * 2) {
            tickCount = 0;
            counter++;
            return false;
        } else if (tickCount > tickMax) {
            return true;
        }
        
        return false;
    }
    
    @Override
    protected void loadFromNBTExtra(NBTTagCompound nbt) {
        this.start = nbt.getBoolean("start");
        this.counter = nbt.getInteger("counter");
        this.max = nbt.getInteger("max");
        this.tickCount = nbt.getInteger("tickCount");
    }
    
    @Override
    protected void writeToNBTExtra(NBTTagCompound nbt) {
        nbt.setBoolean("start", this.start);
        nbt.setInteger("counter", this.counter);
        nbt.setInteger("max", this.max);
        nbt.setInteger("tickCount", this.tickCount);
    }
    
    @Override
    public boolean queueTick() {
        try {
            LittleSignalOutput pulse = (LittleSignalOutput) this.children.get(2).getStructure();
            pulse.updateState(new boolean[] { tickCounting() });
            
            if (counter >= max) {
                counter = 0;
                start = true;
            } else if (counter <= max) {
                return true;
            }
            
        } catch (CorruptedConnectionException | NotYetConnectedException e) {}
        
        return false;
    }
    
    @Override
    public void trigger() {
        if (start)
            try {
                LittleSignalInput count = (LittleSignalInput) this.children.get(0).getStructure();
                max = SignalingUtils.boolToInt(count.getState().clone());
                this.queueForNextTick();
                this.start = false;
            } catch (CorruptedConnectionException | NotYetConnectedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        
    }
    
}
