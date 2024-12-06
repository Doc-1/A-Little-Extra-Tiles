package com.alet.components.structures.type.programable.basic.conditions;

import com.alet.components.structures.type.programable.basic.LittleTriggerObject;

import net.minecraft.nbt.NBTTagCompound;

public abstract class LittleTriggerCondition extends LittleTriggerObject {
    
    public boolean completed = false;
    public boolean shouldLoop = false;
    
    public LittleTriggerCondition(int id) {
        super(id);
    }
    
    @Override
    public NBTTagCompound createNBT() {
        NBTTagCompound nbt = super.createNBT();
        return serializeNBT(nbt);
    }
    
    public boolean conditionRunEvent() {
        if (completed)
            return true;
        return conditionPassed();
    }
    
    public abstract boolean conditionPassed();
    
}
