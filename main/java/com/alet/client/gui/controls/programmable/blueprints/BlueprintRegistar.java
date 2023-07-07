package com.alet.client.gui.controls.programmable.blueprints;

import java.util.ArrayList;
import java.util.List;

import com.alet.client.gui.controls.menu.GuiTreePart;
import com.alet.client.gui.controls.menu.GuiTreePart.EnumPartType;
import com.alet.client.gui.controls.menu.GuiTreePartHolder;
import com.alet.client.gui.controls.programmable.blueprints.GuiBlueprint.BlueprintType;
import com.alet.client.gui.controls.programmable.blueprints.activators.BlueprintRightClick;
import com.alet.client.gui.controls.programmable.blueprints.activators.BlueprintTileCollision;
import com.alet.client.gui.controls.programmable.blueprints.conditions.BlueprintEqualTo;
import com.alet.client.gui.controls.programmable.blueprints.events.BlueprintMessage;
import com.alet.client.gui.controls.programmable.blueprints.events.BlueprintModifyMotion;
import com.alet.client.gui.controls.programmable.blueprints.flow.BlueprintBranch;
import com.alet.client.gui.controls.programmable.blueprints.getters.BlueprintGetEntityName;
import com.alet.client.gui.controls.programmable.blueprints.getters.BlueprintGetEntityPos;
import com.alet.client.gui.controls.programmable.blueprints.values.BlueprintCastDoubleToInt;
import com.alet.client.gui.controls.programmable.blueprints.values.BlueprintDouble;
import com.alet.client.gui.controls.programmable.blueprints.values.BlueprintFloat;
import com.alet.client.gui.controls.programmable.blueprints.values.BlueprintInteger;
import com.alet.client.gui.controls.programmable.blueprints.values.BlueprintJoinString;
import com.alet.client.gui.controls.programmable.blueprints.values.BlueprintString;
import com.creativemd.creativecore.common.utils.mc.ColorUtils;
import com.creativemd.creativecore.common.utils.type.Pair;
import com.creativemd.creativecore.common.utils.type.PairList;

import net.minecraft.util.text.translation.I18n;

public class BlueprintRegistar {
    public static PairList<String, PairList<String, Class<? extends GuiBlueprint>>> guiBlueprints = new PairList<>();
    public static PairList<Class<? extends GuiBlueprint>, String> names = new PairList<>();
    public static PairList<Class<? extends GuiBlueprint>, Integer> colors = new PairList<>();
    public static PairList<Class<? extends GuiBlueprint>, BlueprintType> blueprintType = new PairList<>();
    
    static {
        registerTriggerObject("flow_control", "branch", 0xFFF4806D, BlueprintType.RECEIVER, BlueprintBranch.class);
        registerTriggerObject("value", "integer", 0xFFF4806D, BlueprintType.NEITHER, BlueprintInteger.class);
        registerTriggerObject("value", "string", 0xFFF4806D, BlueprintType.NEITHER, BlueprintString.class);
        registerTriggerObject("value", "double", 0xFFF4806D, BlueprintType.NEITHER, BlueprintDouble.class);
        registerTriggerObject("value", "float", 0xFFF4806D, BlueprintType.NEITHER, BlueprintFloat.class);
        registerTriggerObject("value", "join_string", 0xFFF4806D, BlueprintType.NEITHER, BlueprintJoinString.class);
        registerTriggerObject("activator", "tile_collision", ColorUtils.WHITE, BlueprintType.NEITHER, BlueprintTileCollision.class);
        registerTriggerObject("activator", "right_click", ColorUtils.WHITE, BlueprintType.NEITHER, BlueprintRightClick.class);
        registerTriggerObject("condition", "equal_to", 0xFF87CEFA, BlueprintType.BOTH, BlueprintEqualTo.class);
        registerTriggerObject("cast", "double_to_int", 0xFFCF9FFF, BlueprintType.NEITHER, BlueprintCastDoubleToInt.class);
        registerTriggerObject("event", "modify_motion", 0xFFADFF2F, BlueprintType.BOTH, BlueprintModifyMotion.class);
        registerTriggerObject("event", "message", 0xFFADFF2F, BlueprintType.BOTH, BlueprintMessage.class);
        registerTriggerObject("getter", "get_entity_position", 0xFFADFF2F, BlueprintType.BOTH, BlueprintGetEntityPos.class);
        registerTriggerObject("getter", "get_entity_name", 0xFFADFF2F, BlueprintType.BOTH, BlueprintGetEntityName.class);
    }
    
    public static void registerTriggerObject(String category, String name, int color, BlueprintType type, Class<? extends GuiBlueprint> clazz) {
        category = "trigger.advanced.category." + category + ".name";
        PairList<String, Class<? extends GuiBlueprint>> categoryList = guiBlueprints.getValue(category);
        if (categoryList == null) {
            categoryList = new PairList<>();
            guiBlueprints.add(category, categoryList);
        }
        name = "trigger.advanced." + name + ".name";
        setColor(color, clazz);
        setName(name, clazz);
        setBlueprintType(type, clazz);
        categoryList.add(name, clazz);
    }
    
    public static String getTranslatedName(String name) {
        return I18n.translateToLocal(name);
    }
    
    public static List<GuiTreePart> treeList() {
        List<GuiTreePart> treeList = new ArrayList<GuiTreePart>();
        for (Pair<String, PairList<String, Class<? extends GuiBlueprint>>> headerPair : guiBlueprints) {
            GuiTreePart header = new GuiTreePart(getTranslatedName(headerPair.key), EnumPartType.Branch);
            for (Pair<String, Class<? extends GuiBlueprint>> pair : headerPair.value) {
                header.addMenu(new GuiTreePartHolder<Class<? extends GuiBlueprint>>(pair.key, getTranslatedName(pair.key), EnumPartType.Leaf, pair.value));
            }
            treeList.add(header);
        }
        return treeList;
    }
    
    public static Class<? extends GuiBlueprint> getClass(String name, boolean translate) {
        for (Pair<Class<? extends GuiBlueprint>, String> pair : names) {
            if (translate) {
                if (getTranslatedName(pair.value).equals(name))
                    return pair.key;
            } else if (pair.value.equals(name))
                return pair.key;
        }
        return null;
    }
    
    public static String getName(Class<? extends GuiBlueprint> clazz) {
        return names.getValue(clazz);
    }
    
    public static BlueprintType getBlueprintType(Class<? extends GuiBlueprint> clazz) {
        return blueprintType.getValue(clazz);
    }
    
    public static int getColor(Class<? extends GuiBlueprint> clazz) {
        return colors.getValue(clazz);
    }
    
    public static void setColor(int color, Class<? extends GuiBlueprint> clazz) {
        colors.add(clazz, color);
    }
    
    public static void setName(String name, Class<? extends GuiBlueprint> clazz) {
        names.add(clazz, name);
    }
    
    public static void setBlueprintType(BlueprintType type, Class<? extends GuiBlueprint> clazz) {
        blueprintType.add(clazz, type);
    }
}
