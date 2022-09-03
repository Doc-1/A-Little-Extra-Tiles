package com.alet.common.structure.type.trigger.conditions;

import com.alet.common.structure.type.trigger.LittleTriggerObject;

import net.minecraft.nbt.NBTTagCompound;

public abstract class LittleTriggerCondition extends LittleTriggerObject {
    
    public boolean canContinue = false;
    
    public LittleTriggerCondition(String id) {
        super(id);
        // TODO Auto-generated constructor stub
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
    
    public abstract boolean conditionPassed();
    
}
