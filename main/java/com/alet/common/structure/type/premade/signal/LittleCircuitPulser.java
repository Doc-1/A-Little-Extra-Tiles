package com.alet.common.structure.type.premade.signal;

import com.alet.common.utils.SignalingUtils;
import com.creativemd.littletiles.common.structure.exception.CorruptedConnectionException;
import com.creativemd.littletiles.common.structure.exception.NotYetConnectedException;
import com.creativemd.littletiles.common.structure.registry.LittleStructureType;
import com.creativemd.littletiles.common.structure.type.premade.signal.LittleSignalInput;
import com.creativemd.littletiles.common.structure.type.premade.signal.LittleSignalOutput;
import com.creativemd.littletiles.common.tile.parent.IStructureTileList;

import net.minecraft.nbt.NBTTagCompound;

public class LittleCircuitPulser extends LittleCircuitPremade {
    private boolean start = false;
    private int max = 0;
    private int tickCount = 0;
    
    public LittleCircuitPulser(LittleStructureType type, IStructureTileList mainBlock) {
        super(type, mainBlock, 1);
        this.setInputIndexes(0);
    }
    
    private boolean tickCounting() {
        tickCount++;
        
        if (tickCount >= max + 1) {
            tickCount = 0;
            start = false;
            return false;
        } else if (tickCount >= max) {
            return true;
        }
        return false;
    }
    
    @Override
    protected void loadFromNBTExtra(NBTTagCompound nbt) {
        this.start = nbt.getBoolean("start");
        this.max = nbt.getInteger("max");
        this.tickCount = nbt.getInteger("tickCount");
    }
    
    @Override
    protected void writeToNBTExtra(NBTTagCompound nbt) {
        nbt.setBoolean("start", this.start);
        nbt.setInteger("max", this.max);
        nbt.setInteger("tickCount", this.tickCount);
    }
    
    @Override
    public void tick() {
        if (!this.isClient() && start) {
            try {
                LittleSignalOutput pulse = (LittleSignalOutput) this.children.get(2).getStructure();
                pulse.updateState(new boolean[] { tickCounting() });
            } catch (CorruptedConnectionException | NotYetConnectedException e) {}
        }
    }
    
    @Override
    public void trigger(int clockValue) {
        if (!start)
            try {
                LittleSignalInput count = (LittleSignalInput) this.children.get(0).getStructure();
                max = SignalingUtils.boolToInt(count.getState().clone());
                this.start = true;
            } catch (CorruptedConnectionException | NotYetConnectedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        
    }
    
}
