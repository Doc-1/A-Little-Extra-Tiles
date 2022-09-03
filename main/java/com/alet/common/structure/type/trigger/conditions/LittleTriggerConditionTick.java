package com.alet.common.structure.type.trigger.conditions;

import com.alet.common.structure.type.trigger.LittleTriggerObject;
import com.creativemd.creativecore.common.gui.CoreControl;
import com.creativemd.creativecore.common.gui.container.GuiParent;
import com.creativemd.creativecore.common.gui.controls.gui.GuiPanel;
import com.creativemd.creativecore.common.gui.controls.gui.GuiTextfield;

import net.minecraft.nbt.NBTTagCompound;

public class LittleTriggerConditionTick extends LittleTriggerCondition {
    
    public int tick = 0;
    public int effectPerTick = 0;
    
    public LittleTriggerConditionTick(String id) {
        super(id);
        // TODO Auto-generated constructor stub
    }
    
    @Override
    public boolean conditionPassed() {
        if (this.effectPerTick <= this.tick) {
            this.tick = 0;
        }
        this.tick++;
        return false;
    }
    
    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return null;
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
