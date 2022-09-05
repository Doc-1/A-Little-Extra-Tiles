package com.alet.common.structure.type.trigger;

import java.lang.reflect.InvocationTargetException;

import com.alet.common.structure.type.trigger.conditions.LittleTriggerConditionTickDelay;
import com.alet.common.structure.type.trigger.events.LittleTriggerEventExecuteCommand;
import com.alet.common.structure.type.trigger.events.LittleTriggerEventModifyHealth;
import com.alet.common.structure.type.trigger.events.LittleTriggerEventModifyInventory;
import com.alet.common.structure.type.trigger.events.LittleTriggerEventModifyMotion;
import com.alet.common.structure.type.trigger.events.LittleTriggerEventModifyScoreboard;
import com.alet.common.structure.type.trigger.events.LittleTriggerEventSetSpawn;
import com.creativemd.creativecore.common.utils.type.PairList;

import net.minecraft.nbt.NBTTagCompound;

public class LittleTriggerRegistrar {
    //public static LinkedHashMap<String, Class<? extends LittleTriggerEvent>> registeredEvents = new LinkedHashMap<String, Class<? extends LittleTriggerEvent>>();
    // public static LinkedHashMap<String, Class<? extends LittleTriggerCondition>> registeredConditions = new LinkedHashMap<String, Class<? extends LittleTriggerCondition>>();
    public static PairList<String, PairList<String, Class<? extends LittleTriggerObject>>> triggerables = new PairList<>();
    static {
        registerTriggerObject("codition", "tick_delay", LittleTriggerConditionTickDelay.class);
        registerTriggerObject("event", "modify_health", LittleTriggerEventModifyHealth.class);
        registerTriggerObject("event", "modify_motion", LittleTriggerEventModifyMotion.class);
        registerTriggerObject("event", "modify_inventory", LittleTriggerEventModifyInventory.class);
        registerTriggerObject("event", "modify_scoreboard", LittleTriggerEventModifyScoreboard.class);
        registerTriggerObject("event", "execute_command", LittleTriggerEventExecuteCommand.class);
        registerTriggerObject("event", "set_spawn", LittleTriggerEventSetSpawn.class);
    }
    
    public static void registerTriggerObject(String category, String id, Class<? extends LittleTriggerObject> event) {
        category = "trigger.category." + category + ".name";
        PairList<String, Class<? extends LittleTriggerObject>> categoryList = triggerables.getValue(category);
        if (categoryList == null) {
            categoryList = new PairList<>();
            triggerables.add(category, categoryList);
        }
        categoryList.add("trigger." + id + ".name", event);
        
    }
    
    public static LittleTriggerObject getTriggerObject(Class<? extends LittleTriggerObject> clazz, int index) {
        try {
            return clazz.getConstructor(int.class).newInstance(index);
        } catch (IllegalArgumentException | SecurityException | InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static LittleTriggerObject getFromNBT(NBTTagCompound nbt) {
        String id = nbt.getString("trigger");
        String name = id.replaceAll("\\d", "");
        LittleTriggerObject event = getTriggerObject(LittleTriggerObject.getTriggerClass(name), 0);
        event.createFrom(nbt);
        return event;
    }
}
