package com.alet.common.structure.type.trigger;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;

import com.creativemd.creativecore.common.gui.CoreControl;
import com.creativemd.creativecore.common.gui.GuiControl;
import com.creativemd.creativecore.common.gui.container.GuiParent;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;

public abstract class LittleTriggerEvent {
    
    public String id = "";
    public int tick = 0;
    public static LinkedHashMap<String, Class<? extends LittleTriggerEvent>> registeredEvents = new LinkedHashMap<String, Class<? extends LittleTriggerEvent>>();
    
    static {
        registerEvent("Modify Health", LittleTriggerModifyHealth.class);
        registerEvent("Modify Motion", LittleTriggerModifyMotion.class);
        registerEvent("Modify Scoreboard", LittleTriggerEventModifyScoreboard.class);
        registerEvent("Execute Command", LittleTriggerExecuteCommand.class);
    }
    
    public LittleTriggerEvent(String id) {
        this.id = id;
    }
    
    public abstract String getName();
    
    public static void registerEvent(String name, Class<? extends LittleTriggerEvent> event) {
        if (!registeredEvents.containsKey(name))
            registeredEvents.put(name, event);
        else
            System.err.println("Warning name, " + name + "already exists");
    }
    
    public static LittleTriggerEvent getLittleTrigger(String name, String id) {
        try {
            return registeredEvents.get(name).getConstructor(String.class).newInstance(id);
        } catch (IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException e) {}
        return null;
    }
    
    public static LittleTriggerEvent createFromNBT(NBTTagCompound nbt) {
        
        String id = nbt.getString("trigger");
        String[] split = id.split("(?<=\\D)(?=\\d)");
        String name = split[0];
        
        return null;
    }
    
    public abstract NBTTagCompound createNBT();
    
    public abstract void updateControls(GuiParent parent);
    
    public abstract void updateValues(CoreControl source);
    
    public abstract void runEvent(HashSet<Entity> entities, Integer tick);
    
    public static void wipeControls(GuiParent panel) {
        panel.controls = new ArrayList<GuiControl>();
    }
}
