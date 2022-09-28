package com.alet.common.structure.type.trigger.getters;

import com.alet.common.structure.type.trigger.LittleTriggerObject;
import com.creativemd.littletiles.common.structure.signal.component.ISignalStructureComponent;

import net.minecraft.nbt.NBTTagCompound;

public abstract class LittleTriggerGetter extends LittleTriggerObject {
    
    int outputIndex;
    
    public LittleTriggerGetter(int id) {
        super(id);
    }
    
    @Override
    public NBTTagCompound createNBT() {
        NBTTagCompound nbt = super.createNBT();
        return createNBT(nbt);
    }
    
    public ISignalStructureComponent getOutput() {
        
        return null;
    }
}
