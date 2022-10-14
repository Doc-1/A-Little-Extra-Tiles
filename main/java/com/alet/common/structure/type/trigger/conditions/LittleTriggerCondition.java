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
    
    public boolean conditionRunEvent(LittleTriggerObject nextTriggerObj) {
        if (completed) {
            return true;
        }
        if (conditionPassed()) {
            
            this.completed = true;
            if (nextTriggerObj instanceof LittleTriggerEvent) {
                LittleTriggerEvent triggerEvent = (LittleTriggerEvent) nextTriggerObj;
                triggerEvent.runEvent();
                return true;
            } else if (nextTriggerObj instanceof LittleTriggerCondition)
                return true;
        }
        return false;
    }
    
    public abstract boolean conditionPassed();
    
}
