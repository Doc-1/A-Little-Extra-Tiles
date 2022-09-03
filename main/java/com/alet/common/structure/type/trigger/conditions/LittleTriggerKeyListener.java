package com.alet.common.structure.type.trigger.conditions;

import com.alet.common.structure.type.trigger.LittleTriggerObject;
import com.creativemd.creativecore.common.gui.CoreControl;
import com.creativemd.creativecore.common.gui.container.GuiParent;

import net.minecraft.nbt.NBTTagCompound;

public class LittleTriggerKeyListener extends LittleTriggerCondition {
    
    private int keyToListenFor;
    
    public LittleTriggerKeyListener(String id) {
        super(id);
    }
    
    public LittleTriggerKeyListener(String id, double xStrength, double yStrength, double zStrength, double forward) {
        super(id);
    }
    
    @Override
    public NBTTagCompound createNBT(NBTTagCompound nbt) {
        nbt.setString("trigger", id);
        return nbt;
    }
    
    @Override
    public void updateControls(GuiParent parent) {
        
    }
    
    @Override
    public void updateValues(CoreControl source) {
        
    }
    
    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public LittleTriggerObject createFromNBT(NBTTagCompound nbt) {
        return this;
    }
    
    @Override
    public boolean conditionPassed() {
        // TODO Auto-generated method stub
        return false;
    }
    
}
