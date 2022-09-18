package com.alet.common.structure.type.trigger;

import java.lang.reflect.InvocationTargetException;

import com.alet.common.structure.type.trigger.conditions.LittleTriggerConditionTickDelay;
import com.alet.common.structure.type.trigger.conditions.LittleTriggerConditionWhileCollided;
import com.alet.common.structure.type.trigger.events.LittleTriggerEventExecuteCommand;
import com.alet.common.structure.type.trigger.events.LittleTriggerEventModifyHealth;
import com.alet.common.structure.type.trigger.events.LittleTriggerEventModifyInventory;
import com.alet.common.structure.type.trigger.events.LittleTriggerEventModifyMotion;
import com.alet.common.structure.type.trigger.events.LittleTriggerEventModifyScoreboard;
import com.alet.common.structure.type.trigger.events.LittleTriggerEventPlaySound;
import com.alet.common.structure.type.trigger.events.LittleTriggerEventSetSpawn;
import com.creativemd.creativecore.common.utils.type.PairList;

import net.minecraft.nbt.NBTTagCompound;

public class LittleTriggerRegistrar {
    //public static LinkedHashMap<String, Class<? extends LittleTriggerEvent>> registeredEvents = new LinkedHashMap<String, Class<? extends LittleTriggerEvent>>();
    // public static LinkedHashMap<String, Class<? extends LittleTriggerCondition>> registeredConditions = new LinkedHashMap<String, Class<? extends LittleTriggerCondition>>();
    public static PairList<String, PairList<String, Class<? extends LittleTriggerObject>>> triggerables = new PairList<>();
    static {
        registerTriggerObject("codition", "tick_delay", LittleTriggerConditionTickDelay.class);
        registerTriggerObject("codition", "count_tick", LittleTriggerConditionWhileCollided.class);
        registerTriggerObject("event", "modify_health", LittleTriggerEventModifyHealth.class);
        registerTriggerObject("event", "modify_motion", LittleTriggerEventModifyMotion.class);
        registerTriggerObject("event", "modify_inventory", LittleTriggerEventModifyInventory.class);
        registerTriggerObject("event", "modify_scoreboard", LittleTriggerEventModifyScoreboard.class);
        registerTriggerObject("event", "execute_command", LittleTriggerEventExecuteCommand.class);
        registerTriggerObject("event", "set_spawn", LittleTriggerEventSetSpawn.class);
        registerTriggerObject("event", "play_sound", LittleTriggerEventPlaySound.class);
    }
    
    public static void registerTriggerObject(String category, String name, Class<? extends LittleTriggerObject> clazz) {
        category = "trigger.category." + category + ".name";
        PairList<String, Class<? extends LittleTriggerObject>> categoryList = triggerables.getValue(category);
        if (categoryList == null) {
            categoryList = new PairList<>();
            triggerables.add(category, categoryList);
        }
        name = "trigger." + name + ".name";
        LittleTriggerObject.setName(name, clazz);
        categoryList.add(name, clazz);
        
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
        String trigger = nbt.getString("trigger");
        String[] arr = trigger.split("(?<=\\D)(?=\\d)");
        String name = arr[0];
        int index = Integer.parseInt(arr[1]);
        LittleTriggerObject event = getTriggerObject(LittleTriggerObject.getTriggerClass(name), index);
        event.createFrom(nbt);
        return event;
    }
}
