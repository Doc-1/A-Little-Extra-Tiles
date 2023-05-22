package com.alet.common.structure.type.trigger.conditions;

import com.alet.common.structure.type.trigger.LittleTriggerObject;
import com.creativemd.creativecore.common.gui.CoreControl;
import com.creativemd.creativecore.common.gui.container.GuiParent;
import com.creativemd.creativecore.common.gui.controls.gui.GuiPanel;
import com.creativemd.creativecore.common.gui.controls.gui.GuiTextfield;
import com.creativemd.littletiles.common.tile.preview.LittlePreviews;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class LittleTriggerConditionTickDelay extends LittleTriggerCondition {
    
    public int effectPerTick = 0;
    
    public LittleTriggerConditionTickDelay(int id) {
        super(id);
        this.shouldLoop = true;
    }
    
    @Override
    public boolean conditionPassed() {
        if (effectPerTick - 1 <= structure.tick) {
            structure.tick = 0;
            return true;
        }
        structure.tick++;
        return false;
    }
    
    @Override
    public LittleTriggerObject deserializeNBT(NBTTagCompound nbt) {
        this.effectPerTick = nbt.getInteger("effectPerTick");
        return this;
    }
    
    @Override
    public NBTTagCompound serializeNBT(NBTTagCompound nbt) {
        nbt.setInteger("effectPerTick", effectPerTick);
        return nbt;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void guiChangedEvent(CoreControl source) {
        if (source instanceof GuiTextfield) {
            this.effectPerTick = Integer.parseInt(((GuiTextfield) source).text);
        }
        
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void createGuiControls(GuiParent parent, LittlePreviews previews) {
        GuiPanel panel = (GuiPanel) parent.get("content");
        panel.addControl(new GuiTextfield("preTick", effectPerTick + "", 85, 0, 40, 14).setNumbersOnly());
        
    }
    
}
