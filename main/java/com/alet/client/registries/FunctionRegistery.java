package com.alet.client.registries;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import com.alet.common.gui.controls.menu.GuiTreePart;
import com.alet.common.gui.controls.menu.GuiTreePart.EnumPartType;
import com.alet.common.gui.controls.menu.GuiTreePartHolder;
import com.alet.common.gui.controls.programmable.GuiFunction;
import com.alet.components.structures.type.programable.advanced.Function;
import com.alet.components.structures.type.programable.advanced.activators.FunctionOnRightClick;
import com.alet.components.structures.type.programable.advanced.events.FunctionDebugMessage;
import com.alet.components.structures.type.programable.advanced.flows.FunctionForLoop;
import com.creativemd.creativecore.common.utils.type.Pair;
import com.creativemd.creativecore.common.utils.type.PairList;

import net.minecraft.util.text.translation.I18n;

public class FunctionRegistery {
    public static PairList<String, PairList<String, GuiFunction>> guiFunctions = new PairList<>();
    
    public static PairList<String, Class<? extends Function>> functions = new PairList<String, Class<? extends Function>>();
    
    public static void registerFunctions() {
        registerFunction("activator", new FunctionOnRightClick(0));
        registerFunction("flow_control", new FunctionForLoop(0));
        registerFunction("event", new FunctionDebugMessage(0));
        /*
        registerTriggerObject("flow_control", "branch", 0xFFF4806D, BlueprintType.RECEIVER, BlueprintBranch.class);
        registerTriggerObject("flow_control", "sleep", 0xFFF4806D, BlueprintType.RECEIVER, BlueprintSleep.class);
        registerTriggerObject("flow_control", "loop_end", 0xFFF4806D, BlueprintType.RECEIVER, BlueprintLoopEnd.class);
        registerTriggerObject("flow_control", "for_loop", 0xFFF4806D, BlueprintType.RECEIVER, BlueprintForLoop.class);
        registerTriggerObject("value", "integer", 0xFFF4806D, BlueprintType.NEITHER, BlueprintInteger.class);
        registerTriggerObject("value", "string", 0xFFF4806D, BlueprintType.NEITHER, BlueprintString.class);
        registerTriggerObject("value", "double", 0xFFF4806D, BlueprintType.NEITHER, BlueprintDouble.class);
        registerTriggerObject("value", "float", 0xFFF4806D, BlueprintType.NEITHER, BlueprintFloat.class);
        registerTriggerObject("value", "join_string", 0xFFF4806D, BlueprintType.NEITHER, BlueprintJoinString.class);
        registerTriggerObject("activator", "tile_collision", ColorUtils.WHITE, BlueprintType.NEITHER, BlueprintTileCollision.class);
        registerTriggerObject("activator", "right_click", ColorUtils.WHITE, BlueprintType.NEITHER, BlueprintRightClick.class);
        registerTriggerObject("condition", "equal_to", 0xFF87CEFA, BlueprintType.BOTH, BlueprintEqualTo.class);
        registerTriggerObject("cast", "double_to_int", 0xFFCF9FFF, BlueprintType.NEITHER, BlueprintCastDoubleToInt.class);
        registerTriggerObject("cast", "integer_to_int", 0xFFCF9FFF, BlueprintType.NEITHER, BlueprintCastIntegerToString.class);
        registerTriggerObject("event", "modify_motion", 0xFFADFF2F, BlueprintType.BOTH, BlueprintModifyMotion.class);
        registerTriggerObject("event", "message", 0xFFADFF2F, BlueprintType.BOTH, BlueprintMessage.class);
        registerTriggerObject("getter", "get_entity_position", 0xFFADFF2F, BlueprintType.BOTH, BlueprintGetEntityPos.class);
        registerTriggerObject("getter", "get_entity_name", 0xFFADFF2F, BlueprintType.BOTH, BlueprintGetEntityName.class);
        */
    }
    
    public static void registerFunction(String category, Function function) {
        category = "programmable.advanced.category." + category + ".name";
        PairList<String, GuiFunction> categoryList = guiFunctions.getValue(category);
        if (categoryList == null)
            categoryList = new PairList<>();
        guiFunctions.add(category, categoryList);
        String name = function.getName();
        name = "programmable.advanced." + name + ".name";
        
        categoryList.add(name, new GuiFunction(name, getTranslatedName(name), function.getColor(), function
                .isMethodSender(), function.isMethodReciever(), function.setGuiNodes()));
        functions.add(new Pair<String, Class<? extends Function>>(function.getName(), function.getClass()));
    }
    
    public static Function getFunctionFromGui(GuiFunction guiFunction) {
        try {
            Function function = functions.getValue(guiFunction.name).getConstructor(int.class).newInstance(guiFunction.id);
            return function;
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static String getTranslatedName(String name) {
        return I18n.translateToLocal(name);
    }
    
    public static List<GuiTreePart> treeList() {
        List<GuiTreePart> treeList = new ArrayList<GuiTreePart>();
        for (Pair<String, PairList<String, GuiFunction>> headerPair : guiFunctions) {
            GuiTreePart header = new GuiTreePart(getTranslatedName(headerPair.key), EnumPartType.Branch);
            for (Pair<String, GuiFunction> pair : headerPair.value) {
                header.addMenu(new GuiTreePartHolder<String>(pair.key, getTranslatedName(
                    pair.key), EnumPartType.Leaf, pair.value.FUNCTION_TITLE));
            }
            treeList.add(header);
        }
        return treeList;
    }
    
    public static GuiFunction createFunctionGui(String name, boolean translate, int id) {
        for (Pair<String, PairList<String, GuiFunction>> mainPair : guiFunctions) {
            for (Pair<String, GuiFunction> pair : mainPair.value) {
                if (translate) {
                    if (getTranslatedName(pair.key).equals(name))
                        return pair.value.clone(id);
                } else if (pair.key.equals(name))
                    return pair.value.clone(id);
            }
            
        }
        
        return null;
    }
    
}
