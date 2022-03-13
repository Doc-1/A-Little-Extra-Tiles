package com.alet.common.structure.type.trigger;

import java.util.HashSet;

import com.creativemd.creativecore.common.gui.CoreControl;
import com.creativemd.creativecore.common.gui.container.GuiParent;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;

public class LittleTriggerEventModifyInventory extends LittleTriggerEvent {
    
    public LittleTriggerEventModifyInventory(String id) {
        super(id);
        // TODO Auto-generated constructor stub
    }
    
    @Override
    public NBTTagCompound createNBT() {
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
    
    @Override
    public void runEvent(HashSet<Entity> entities, Integer tick) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return null;
    }
}
