package com.alet.common.structure.type.trigger.conditions;

import com.alet.common.structure.type.trigger.LittleTriggerBoxStructureALET;
import com.alet.common.structure.type.trigger.LittleTriggerObject;
import com.creativemd.creativecore.common.gui.CoreControl;
import com.creativemd.creativecore.common.gui.container.GuiParent;
import com.creativemd.creativecore.common.gui.controls.gui.GuiPanel;
import com.creativemd.creativecore.common.gui.controls.gui.GuiTextfield;

import net.minecraft.nbt.NBTTagCompound;

public class LittleTriggerConditionTickDelay extends LittleTriggerCondition {
    
    public int effectPerTick = 0;
    
    public LittleTriggerConditionTickDelay(int id) {
        super(id);
    }
    
    @Override
    public boolean conditionPassed(LittleTriggerBoxStructureALET structure) {
        
        if (effectPerTick <= structure.tick) {
            structure.tick = 0;
            this.completed = true;
            return true;
        }
        structure.tick++;
        return false;
    }
    
    @Override
    public LittleTriggerObject createFromNBT(NBTTagCompound nbt) {
        this.effectPerTick = nbt.getInteger("effectPerTick");
        return this;
    }
    
    @Override
    public NBTTagCompound createNBT(NBTTagCompound nbt) {
        nbt.setInteger("effectPerTick", effectPerTick);
        return nbt;
    }
    
    @Override
    public void updateControls(GuiParent parent) {
        GuiPanel panel = (GuiPanel) parent.get("content");
        panel.addControl(new GuiTextfield("preTick", effectPerTick + "", 85, 0, 40, 14).setNumbersOnly());
        
    }
    
    @Override
    public void updateValues(CoreControl source) {
        if (source instanceof GuiTextfield) {
            this.effectPerTick = Integer.parseInt(((GuiTextfield) source).text);
        }
        
    }
    
}
