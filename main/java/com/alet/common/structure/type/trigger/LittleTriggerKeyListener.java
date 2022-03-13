package com.alet.common.structure.type.trigger;

import java.util.HashSet;

import com.creativemd.creativecore.common.gui.CoreControl;
import com.creativemd.creativecore.common.gui.container.GuiParent;
import com.creativemd.creativecore.common.gui.controls.gui.GuiPanel;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;

public class LittleTriggerKeyListener extends LittleTriggerEvent {
    
    private int keyToListenFor;
    
    public LittleTriggerKeyListener(String id) {
        super(id);
    }
    
    public LittleTriggerKeyListener(String id, double xStrength, double yStrength, double zStrength, double forward) {
        super(id);
    }
    
    @Override
    public NBTTagCompound createNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setString("trigger", id);
        return nbt;
    }
    
    @Override
    public void updateControls(GuiParent parent) {
        GuiPanel panel = (GuiPanel) parent.get("content");
        wipeControls(panel);
        
    }
    
    @Override
    public void updateValues(CoreControl source) {
        
    }
    
    @Override
    public void runEvent(HashSet<Entity> entities, Integer tick) {
        for (Entity entity : entities) {
            
        }
    }
    
    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return null;
    }
    
}
