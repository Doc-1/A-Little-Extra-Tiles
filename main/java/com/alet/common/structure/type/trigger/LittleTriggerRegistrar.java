package com.alet.common.structure.type.trigger;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;

import com.alet.common.structure.type.trigger.events.LittleTriggerEvent;
import com.alet.common.structure.type.trigger.events.LittleTriggerEventExecuteCommand;
import com.alet.common.structure.type.trigger.events.LittleTriggerEventModifyHealth;
import com.alet.common.structure.type.trigger.events.LittleTriggerEventModifyInventory;
import com.alet.common.structure.type.trigger.events.LittleTriggerEventModifyMotion;
import com.alet.common.structure.type.trigger.events.LittleTriggerEventModifyScoreboard;
import com.alet.common.structure.type.trigger.events.LittleTriggerEventSetSpawn;

import net.minecraft.nbt.NBTTagCompound;

public class LittleTriggerRegistrar {
    public static LinkedHashMap<String, Class<? extends LittleTriggerEvent>> registeredEvents = new LinkedHashMap<String, Class<? extends LittleTriggerEvent>>();
    
    static {
        registerEvent("Modify Health", LittleTriggerEventModifyHealth.class);
        registerEvent("Modify Motion", LittleTriggerEventModifyMotion.class);
        registerEvent("Modify Inventory", LittleTriggerEventModifyInventory.class);
        registerEvent("Modify Scoreboard", LittleTriggerEventModifyScoreboard.class);
        registerEvent("Execute Command", LittleTriggerEventExecuteCommand.class);
        registerEvent("Set Spawn", LittleTriggerEventSetSpawn.class);
        
    }
    
    public static void registerEvent(String name, Class<? extends LittleTriggerEvent> event) {
        if (!registeredEvents.containsKey(name))
            registeredEvents.put(name, event);
        else
            System.err.println("Warning name, " + name + "already exists");
        
    }
    
    public static LittleTriggerEvent getLittleTrigger(String name, String id) {
        try {
            return registeredEvents.get(name).getConstructor(String.class).newInstance(id);
        } catch (IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static LittleTriggerEvent getFromNBT(NBTTagCompound nbt) {
        String id = nbt.getString("trigger");
        String name = id.replaceAll("\\d", "");
        LittleTriggerEvent event = getLittleTrigger(name, id);
        event.createFrom(nbt);
        return event;
    }
}
