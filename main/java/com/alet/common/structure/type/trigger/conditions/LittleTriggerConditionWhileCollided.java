package com.alet.common.structure.type.trigger.conditions;

import com.alet.common.structure.type.trigger.LittleTriggerBoxStructureALET;
import com.alet.common.structure.type.trigger.LittleTriggerObject;
import com.creativemd.creativecore.common.gui.CoreControl;
import com.creativemd.creativecore.common.gui.container.GuiParent;

import net.minecraft.nbt.NBTTagCompound;

public class LittleTriggerConditionWhileCollided extends LittleTriggerCondition {
    
    public LittleTriggerConditionWhileCollided(int id) {
        super(id);
    }
    
    @Override
    public boolean conditionPassed(LittleTriggerBoxStructureALET structure) {
        if (!structure.entities.isEmpty()) {
            if (50 <= structure.tick) {
                structure.tick = 0;
                return true;
            }
        } else {
            structure.tick = 0;
        }
        structure.entities.clear();
        structure.tick++;
        return false;
    }
    
    @Override
    public LittleTriggerObject createFromNBT(NBTTagCompound nbt) {
        // TODO Auto-generated method stub
        return this;
    }
    
    @Override
    public NBTTagCompound createNBT(NBTTagCompound nbt) {
        // TODO Auto-generated method stub
        return nbt;
    }
    
    @Override
    public void updateControls(GuiParent parent) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void updateValues(CoreControl source) {
        // TODO Auto-generated method stub
        
    }
    
}
