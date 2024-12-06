package com.alet.components.structures.type.premade.signal.circuit;

import com.creativemd.littletiles.common.structure.registry.LittleStructureType;
import com.creativemd.littletiles.common.tile.parent.IStructureTileList;

import net.minecraft.nbt.NBTTagCompound;

public class LittleCircuitButton extends LittleCircuitPremade {
    
    public LittleCircuitButton(LittleStructureType type, IStructureTileList mainBlock) {
        super(type, mainBlock, -1);
        // TODO Auto-generated constructor stub
    }
    
    @Override
    public void trigger(int clockValue) {
        
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
