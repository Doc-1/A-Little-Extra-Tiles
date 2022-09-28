package com.alet.common.structure.type.trigger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.alet.common.structure.type.trigger.conditions.LittleTriggerCondition;
import com.creativemd.creativecore.common.gui.CoreControl;
import com.creativemd.creativecore.common.gui.GuiControl;
import com.creativemd.creativecore.common.gui.container.GuiParent;
import com.creativemd.creativecore.common.gui.controls.gui.GuiPanel;
import com.creativemd.creativecore.common.utils.type.Pair;
import com.creativemd.creativecore.common.utils.type.PairList;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;

public abstract class LittleTriggerObject {
    
    public int id;
    public LittleTriggerBoxStructureALET structure;
    public static PairList<Class<? extends LittleTriggerObject>, String> names = new PairList<Class<? extends LittleTriggerObject>, String>();
    
    public LittleTriggerObject(int id) {
        this.id = id;
    }
    
    public static void setName(String name, Class<? extends LittleTriggerObject> clazz) {
        names.add(clazz, name);
    }
    
    public static Class<? extends LittleTriggerObject> getTriggerClass(String name) {
        for (Pair<Class<? extends LittleTriggerObject>, String> pair : names)
            if (pair.getValue().equals(name))
                return pair.key;
            
        return null;
    }
    
    public String getName() {
        return names.getValue(this.getClass());
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
    
    public abstract LittleTriggerObject createFromNBT(NBTTagCompound nbt);
    
    public NBTTagCompound createNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setString("trigger", getName() + id);
        return nbt;
    }
    
    public GuiPanel getPanel(GuiParent parent) {
        return (GuiPanel) parent.get("content");
    }
    
    public abstract NBTTagCompound createNBT(NBTTagCompound nbt);
    
    public abstract void updateControls(GuiParent parent);
    
    public abstract void updateValues(CoreControl source);
    
    public static void wipeControls(GuiParent panel) {
        panel.controls = new ArrayList<GuiControl>();
    }
}
