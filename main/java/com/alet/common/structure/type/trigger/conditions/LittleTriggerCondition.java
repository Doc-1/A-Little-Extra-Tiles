package com.alet.common.structure.type.trigger.conditions;

import com.alet.common.structure.type.trigger.LittleTriggerBoxStructureALET;
import com.alet.common.structure.type.trigger.LittleTriggerObject;
import com.alet.common.structure.type.trigger.events.LittleTriggerEvent;

import net.minecraft.nbt.NBTTagCompound;

public abstract class LittleTriggerCondition extends LittleTriggerObject {
    
    public boolean completed = false;
    
    public LittleTriggerCondition(int id) {
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
    
    public boolean conditionRunEvent(LittleTriggerBoxStructureALET structure, LittleTriggerObject nextTriggerObj) {
        if (completed)
            return true;
        if (conditionPassed(structure))
            if (nextTriggerObj instanceof LittleTriggerEvent) {
                LittleTriggerEvent triggerEvent = (LittleTriggerEvent) nextTriggerObj;
                triggerEvent.runEvent(structure.entities);
                return true;
            }
        return false;
    }
    
    public abstract boolean conditionPassed(LittleTriggerBoxStructureALET structure);
    
}
