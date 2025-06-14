package com.alet.components.structures.type;

import com.creativemd.littletiles.common.structure.LittleStructure;
import com.creativemd.littletiles.common.structure.attribute.LittleStructureAttribute;
import com.creativemd.littletiles.common.structure.registry.LittleStructureType;
import com.creativemd.littletiles.common.tile.parent.IStructureTileList;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

public class LittleAlwaysOnLight extends LittleStructure {
    
    public int level;
    
    public LittleAlwaysOnLight(LittleStructureType type, IStructureTileList mainBlock) {
        super(type, mainBlock);
    }
    
    @Override
    protected void loadFromNBTExtra(NBTTagCompound nbt) {
        level = nbt.getInteger("level");
    }
    
    @Override
    protected void writeToNBTExtra(NBTTagCompound nbt) {
        nbt.setInteger("level", level);
    }
    
    @Override
    public int getLightValue(BlockPos pos) {
        return level;
    }
    
    @Override
    public int getAttribute() {
        return super.getAttribute() | LittleStructureAttribute.EMISSIVE;
    }
    
}
