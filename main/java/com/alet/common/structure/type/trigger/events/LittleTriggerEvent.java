package com.alet.common.structure.type.trigger.events;

import com.alet.common.structure.type.trigger.LittleTriggerObject;

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
