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

public class LittleCircuitPulser extends LittleCircuitPremade {
    private boolean start = false;
    private int counter = 0;
    private int max = 0;
    private int tickCount = 0;
    
    public LittleCircuitPulser(LittleStructureType type, IStructureTileList mainBlock) {
        super(type, mainBlock, 2);
    }
    
    private boolean tickCounting() {
        tickCount++;
        if (tickCount > 4) {
            tickCount = 0;
            return true;
        }
        return false;
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
    public void tick() {
        if (!this.isClient())
            try {
                if (pulse && !start) {
                    LittleSignalInput count = (LittleSignalInput) this.children.get(0).getStructure();
                    start = true;
                    max = SignalingUtils.boolToInt(count.getState().clone());
                }
                LittleSignalOutput pulse = (LittleSignalOutput) this.children.get(1).getStructure();
                if (start) {
                    System.out.println(max);
                    if (tickCounting()) {
                        counter++;
                        pulse.updateState(BooleanUtils.SINGLE_TRUE);
                    } else
                        pulse.updateState(BooleanUtils.SINGLE_FALSE);
                    
                    if (counter >= max) {
                        counter = 0;
                        start = false;
                    }
                } else
                    pulse.updateState(BooleanUtils.SINGLE_FALSE);
                
            } catch (CorruptedConnectionException | NotYetConnectedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        
    }
    
}
