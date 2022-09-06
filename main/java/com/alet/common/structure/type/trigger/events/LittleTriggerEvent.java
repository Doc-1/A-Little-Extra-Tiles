package com.alet.common.structure.type.trigger.events;

import java.util.HashSet;

import javax.annotation.Nullable;

import com.alet.common.structure.type.trigger.LittleTriggerObject;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;

public abstract class LittleTriggerEvent extends LittleTriggerObject {
    
    public LittleTriggerEvent(int id) {
        super(id);
    }
    
    @Override
    public LittleTriggerObject createFrom(NBTTagCompound nbt) {
        return createFromNBT(nbt);
    }
    
    @Override
    public NBTTagCompound createNBT() {
        NBTTagCompound nbt = super.createNBT();
        return createNBT(nbt);
    }
    
    public void eventPassed(@Nullable HashSet<Entity> entities) {
        this.complete = runEvent(entities);
    }
    
    protected abstract boolean runEvent(@Nullable HashSet<Entity> entities);
    
}
