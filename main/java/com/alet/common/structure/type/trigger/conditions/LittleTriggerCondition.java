package com.alet.common.structure.type.trigger.conditions;

import com.alet.common.structure.type.trigger.LittleTriggerObject;
import com.alet.common.structure.type.trigger.events.LittleTriggerEvent;

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
