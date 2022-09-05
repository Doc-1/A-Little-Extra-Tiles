package com.alet.common.structure.type.trigger.conditions;

import com.alet.common.structure.type.trigger.LittleTriggerBoxStructureALET;
import com.alet.common.structure.type.trigger.LittleTriggerObject;
import com.creativemd.creativecore.common.gui.CoreControl;
import com.creativemd.creativecore.common.gui.container.GuiParent;
import com.creativemd.creativecore.common.gui.controls.gui.GuiPanel;
import com.creativemd.creativecore.common.gui.controls.gui.GuiTextfield;

import net.minecraft.nbt.NBTTagCompound;

public class LittleTriggerConditionTickDelay extends LittleTriggerCondition {
    
    public int tick = 0;
    public int effectPerTick = 0;
    
    public LittleTriggerConditionTickDelay(int id) {
        super(id);
    }
    
    @Override
    public boolean conditionPassed(LittleTriggerBoxStructureALET structure) {
        if (this.effectPerTick <= this.tick) {
            this.tick = 0;
        }
        this.tick++;
        return false;
    }
    
    @Override
    public String getName() {
        return "Tick Delay";
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
        wipeControls(panel);
        panel.addControl(new GuiTextfield("preTick", effectPerTick + "", 85, 15, 40, 14).setNumbersOnly());
        
    }
    
    @Override
    public void updateValues(CoreControl source) {
        // TODO Auto-generated method stub
        
    }
    
}
