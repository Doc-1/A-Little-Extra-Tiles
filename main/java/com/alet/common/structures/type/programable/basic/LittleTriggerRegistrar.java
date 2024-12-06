package com.alet.common.structures.type.programable.basic;

import java.lang.reflect.InvocationTargetException;

import com.alet.common.structures.type.programable.basic.activator.LittleTriggerActivator;
import com.alet.common.structures.type.programable.basic.activator.LittleTriggerActivatorCollisionArea;
import com.alet.common.structures.type.programable.basic.activator.LittleTriggerActivatorCollisionTiles;
import com.alet.common.structures.type.programable.basic.activator.LittleTriggerActivatorGlobal;
import com.alet.common.structures.type.programable.basic.activator.LittleTriggerActivatorRightClick;
import com.alet.common.structures.type.programable.basic.activator.LittleTriggerActivatorTargetSelector;
import com.alet.common.structures.type.programable.basic.conditions.LittleTriggerConditionDoorTick;
import com.alet.common.structures.type.programable.basic.conditions.LittleTriggerConditionHasItem;
import com.alet.common.structures.type.programable.basic.conditions.LittleTriggerConditionIsEntity;
import com.alet.common.structures.type.programable.basic.conditions.LittleTriggerConditionIsSignal;
import com.alet.common.structures.type.programable.basic.conditions.LittleTriggerConditionSuccessfulCommand;
import com.alet.common.structures.type.programable.basic.conditions.LittleTriggerConditionTickDelay;
import com.alet.common.structures.type.programable.basic.events.LittleTriggerEventExecuteCommand;
import com.alet.common.structures.type.programable.basic.events.LittleTriggerEventModifyHealth;
import com.alet.common.structures.type.programable.basic.events.LittleTriggerEventModifyInventory;
import com.alet.common.structures.type.programable.basic.events.LittleTriggerEventModifyMotion;
import com.alet.common.structures.type.programable.basic.events.LittleTriggerEventModifyScoreboard;
import com.alet.common.structures.type.programable.basic.events.LittleTriggerEventPlaySound;
import com.alet.common.structures.type.programable.basic.events.LittleTriggerEventSetSignal;
import com.alet.common.structures.type.programable.basic.events.LittleTriggerEventSetSpawn;
import com.creativemd.creativecore.common.utils.type.Pair;
import com.creativemd.creativecore.common.utils.type.PairList;

import net.minecraft.nbt.NBTTagCompound;

public class LittleTriggerRegistrar {
    //public static LinkedHashMap<String, Class<? extends LittleTriggerEvent>> registeredEvents = new LinkedHashMap<String, Class<? extends LittleTriggerEvent>>();
    // public static LinkedHashMap<String, Class<? extends LittleTriggerCondition>> registeredConditions = new LinkedHashMap<String, Class<? extends LittleTriggerCondition>>();
    public static PairList<String, PairList<String, Class<? extends LittleTriggerObject>>> triggerableObjects = new PairList<>();
    public static PairList<String, PairList<String, Class<? extends LittleTriggerActivator>>> triggerActivators = new PairList<>();
    public static PairList<Class<? extends LittleTriggerObject>, String> names = new PairList<>();
    
    static {
        registerTriggerActivator("simple", "right_click", LittleTriggerActivatorRightClick.class);
        registerTriggerActivator("simple", "collision_tiles", LittleTriggerActivatorCollisionTiles.class);
        registerTriggerActivator("simple", "global", LittleTriggerActivatorGlobal.class);
        registerTriggerActivator("advanced", "collision_area", LittleTriggerActivatorCollisionArea.class);
        registerTriggerActivator("advanced", "trigger_selector", LittleTriggerActivatorTargetSelector.class);
        //registerTriggerActivator("advanced", "is_scoreboard", LittleTriggerActivatorIsScoreboard.class);
        registerTriggerObject("codition", "tick_delay", LittleTriggerConditionTickDelay.class);
        registerTriggerObject("codition", "has_item", LittleTriggerConditionHasItem.class);
        registerTriggerObject("codition", "is_door_tick", LittleTriggerConditionDoorTick.class);
        registerTriggerObject("codition", "successful_command", LittleTriggerConditionSuccessfulCommand.class);
        registerTriggerObject("codition", "is_entity", LittleTriggerConditionIsEntity.class);
        registerTriggerObject("codition", "is_signal", LittleTriggerConditionIsSignal.class);
        //registerTriggerObject("codition", "scoreboard", LittleTriggerConditionScoreboard.class);
        registerTriggerObject("event", "modify_health", LittleTriggerEventModifyHealth.class);
        registerTriggerObject("event", "modify_motion", LittleTriggerEventModifyMotion.class);
        registerTriggerObject("event", "modify_inventory", LittleTriggerEventModifyInventory.class);
        registerTriggerObject("event", "modify_scoreboard", LittleTriggerEventModifyScoreboard.class);
        registerTriggerObject("event", "execute_command", LittleTriggerEventExecuteCommand.class);
        registerTriggerObject("event", "set_spawn", LittleTriggerEventSetSpawn.class);
        registerTriggerObject("event", "play_sound", LittleTriggerEventPlaySound.class);
        //registerTriggerObject("event", "play_camera", LittleTriggerEventPlayCamera.class);
        registerTriggerObject("event", "set_signal", LittleTriggerEventSetSignal.class);
    }
    
    public static void registerTriggerObject(String category, String name, Class<? extends LittleTriggerObject> clazz) {
        category = "trigger.category." + category + ".name";
        PairList<String, Class<? extends LittleTriggerObject>> categoryList = triggerableObjects.getValue(category);
        if (categoryList == null) {
            categoryList = new PairList<>();
            triggerableObjects.add(category, categoryList);
        }
        name = "trigger." + name + ".name";
        setName(name, clazz);
        categoryList.add(name, clazz);
    }
    
    public static void registerTriggerActivator(String category, String name, Class<? extends LittleTriggerActivator> clazz) {
        category = "trigger.category." + category + ".name";
        PairList<String, Class<? extends LittleTriggerActivator>> categoryList = triggerActivators.getValue(category);
        if (categoryList == null) {
            categoryList = new PairList<>();
            triggerActivators.add(category, categoryList);
        }
        name = "trigger." + name + ".name";
        setName(name, clazz);
        categoryList.add(name, clazz);
    }
    
    public static String getName(Class<? extends LittleTriggerObject> clazz) {
        return names.getValue(clazz);
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
    
    public static LittleTriggerActivator getTriggerActivator(Class<? extends LittleTriggerActivator> clazz) {
        try {
            return clazz.getConstructor().newInstance();
        } catch (IllegalArgumentException | SecurityException | InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
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
        
        String trigger = "";
        if (nbt.hasKey("trigger"))
            trigger = nbt.getString("trigger");
        else if (nbt.hasKey("activator"))
            trigger = nbt.getString("activator");
        
        String[] arr = trigger.split("(?<=\\D)(?=\\d)");
        String name = arr[0];
        int index = Integer.parseInt(arr[1]);
        LittleTriggerObject event = null;
        if (nbt.hasKey("trigger"))
            event = getTriggerObject(getTriggerClass(name), index);
        else if (nbt.hasKey("activator"))
            event = getTriggerActivator((Class<? extends LittleTriggerActivator>) getTriggerClass(name));
        
        event.deserializeNBT(nbt);
        return event;
    }
}
