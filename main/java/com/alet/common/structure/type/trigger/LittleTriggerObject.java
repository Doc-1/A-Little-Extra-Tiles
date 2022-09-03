package com.alet.common.structure.type.trigger;

import java.util.ArrayList;

import com.creativemd.creativecore.common.gui.CoreControl;
import com.creativemd.creativecore.common.gui.GuiControl;
import com.creativemd.creativecore.common.gui.container.GuiParent;

import net.minecraft.nbt.NBTTagCompound;

public abstract class LittleTriggerObject {
    
    public String id = "";
    public boolean complete = false;
    
    public LittleTriggerObject(String id) {
        this.id = id;
    }
    
    public abstract String getName();
    
    public abstract LittleTriggerObject createFrom(NBTTagCompound nbt);
    
    public abstract LittleTriggerObject createFromNBT(NBTTagCompound nbt);
    
    public NBTTagCompound createNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setString("trigger", id);
        return nbt;
    }
    
    public abstract NBTTagCompound createNBT(NBTTagCompound nbt);
    
    public abstract void updateControls(GuiParent parent);
    
    public abstract void updateValues(CoreControl source);
    
    public static void wipeControls(GuiParent panel) {
        panel.controls = new ArrayList<GuiControl>();
    }
}
