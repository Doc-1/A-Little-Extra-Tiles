package com.alet.common.structure.type.trigger.events;

import java.util.HashSet;

import com.creativemd.creativecore.common.gui.CoreControl;
import com.creativemd.creativecore.common.gui.container.GuiParent;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;

public class LittleTriggerEventModifyInventory extends LittleTriggerEvent {
    
    public LittleTriggerEventModifyInventory(int id) {
        super(id);
        // TODO Auto-generated constructor stub
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
    
    @Override
    public boolean runEvent(HashSet<Entity> entities) {
        return false;
    }
    
    @Override
    public LittleTriggerEvent createFromNBT(NBTTagCompound nbt) {
        // TODO Auto-generated method stub  
        return this;
    }
}
