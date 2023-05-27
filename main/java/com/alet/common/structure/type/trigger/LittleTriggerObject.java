package com.alet.common.structure.type.trigger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.alet.common.structure.type.trigger.conditions.LittleTriggerCondition;
import com.creativemd.creativecore.common.gui.CoreControl;
import com.creativemd.creativecore.common.gui.GuiControl;
import com.creativemd.creativecore.common.gui.container.GuiParent;
import com.creativemd.creativecore.common.gui.controls.gui.GuiPanel;
import com.creativemd.littletiles.common.tile.preview.LittlePreviews;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class LittleTriggerObject {
    
    public int id;
    public LittleTriggerBoxStructureALET structure;
    
    public LittleTriggerObject(int id) {
        this.id = id;
    }
    
    public String getName() {
        return LittleTriggerRegistrar.getName(this.getClass());
    }
    
    public HashSet<Entity> getEntities() {
        return this.structure.entities;
    }
    
    public static boolean hasCondition(List<LittleTriggerObject> triggerObjs) {
        for (LittleTriggerObject triggerObj : triggerObjs) {
            if (triggerObj instanceof LittleTriggerCondition)
                return true;
        }
        return false;
    }
    
    public abstract LittleTriggerObject deserializeNBT(NBTTagCompound nbt);
    
    public NBTTagCompound createNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setString("trigger", getName() + id);
        return nbt;
    }
    
    @SideOnly(Side.CLIENT)
    public GuiPanel getPanel(GuiParent parent) {
        return (GuiPanel) parent.get("settings");
    }
    
    public abstract NBTTagCompound serializeNBT(NBTTagCompound nbt);
    
    @SideOnly(Side.CLIENT)
    public abstract void createGuiControls(GuiPanel panel, LittlePreviews previews);
    
    @SideOnly(Side.CLIENT)
    public abstract void guiChangedEvent(CoreControl source);
    
    @SideOnly(Side.CLIENT)
    public static void wipeControls(GuiParent panel) {
        panel.controls = new ArrayList<GuiControl>();
        panel.refreshControls();
    }
}
