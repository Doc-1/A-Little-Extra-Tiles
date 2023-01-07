package com.alet.common.structure.type.premade.signal;

import com.creativemd.littletiles.common.structure.registry.LittleStructureType;
import com.creativemd.littletiles.common.tile.parent.IStructureTileList;

import net.minecraft.nbt.NBTTagCompound;

public class LittleCircuitROM extends LittleCircuitPremade {
    
    public boolean[] state;
    
    public LittleCircuitROM(LittleStructureType type, IStructureTileList mainBlock) {
        super(type, mainBlock, 1);
        this.checkForZero = true;
    }
    
    @Override
    public void trigger(int clockValue) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    protected void loadFromNBTExtra(NBTTagCompound nbt) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    protected void writeToNBTExtra(NBTTagCompound nbt) {
        // TODO Auto-generated method stub
        
    }
    
}
