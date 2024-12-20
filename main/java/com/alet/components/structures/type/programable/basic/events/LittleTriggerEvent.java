package com.alet.components.structures.type.programable.basic.events;

import com.alet.components.structures.type.programable.basic.LittleTriggerObject;

import net.minecraft.nbt.NBTTagCompound;

public abstract class LittleTriggerEvent extends LittleTriggerObject {
    
    public LittleTriggerEvent(int id) {
        super(id);
    }
    
    @Override
    public NBTTagCompound createNBT() {
        NBTTagCompound nbt = super.createNBT();
        return serializeNBT(nbt);
    }
    
    public abstract boolean runEvent();
    
}
