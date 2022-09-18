package com.alet.common.structure.type.trigger.conditions;

import com.alet.common.structure.type.trigger.LittleTriggerBoxStructureALET;
import com.alet.common.structure.type.trigger.LittleTriggerObject;
import com.creativemd.creativecore.common.gui.CoreControl;
import com.creativemd.creativecore.common.gui.container.GuiParent;

import net.minecraft.nbt.NBTTagCompound;

public class LittleTriggerConditionHasItem extends LittleTriggerCondition {
    
    public LittleTriggerConditionHasItem(int id) {
        super(id);
        // TODO Auto-generated constructor stub
    }
    
    @Override
    public boolean conditionPassed(LittleTriggerBoxStructureALET structure) {
        // TODO Auto-generated method stub
        return false;
    }
    
    @Override
    public LittleTriggerObject createFromNBT(NBTTagCompound nbt) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public NBTTagCompound createNBT(NBTTagCompound nbt) {
        // TODO Auto-generated method stub
        return null;
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
