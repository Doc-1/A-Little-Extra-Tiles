package com.alet.components.structures.type.programable.basic.conditions;

import com.alet.components.structures.type.programable.basic.LittleTriggerObject;
import com.creativemd.creativecore.common.gui.CoreControl;
import com.creativemd.creativecore.common.gui.controls.gui.GuiPanel;
import com.creativemd.littletiles.common.tile.preview.LittlePreviews;

import net.minecraft.nbt.NBTTagCompound;

public class LittleTriggerKeyListener extends LittleTriggerCondition {
    
    private int keyToListenFor;
    
    public LittleTriggerKeyListener(int id) {
        super(id);
    }
    
    @Override
    public NBTTagCompound serializeNBT(NBTTagCompound nbt) {
        return nbt;
    }
    
    @Override
    public void createGuiControls(GuiPanel panel, LittlePreviews previews) {}
    
    @Override
    public void guiChangedEvent(CoreControl source) {
        
    }
    
    @Override
    public LittleTriggerObject deserializeNBT(NBTTagCompound nbt) {
        return this;
    }
    
    @Override
    public boolean conditionPassed() {
        // TODO Auto-generated method stub
        return false;
    }
    
}
