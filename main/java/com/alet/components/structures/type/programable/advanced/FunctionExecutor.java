package com.alet.components.structures.type.programable.advanced;

import java.util.List;

import com.alet.components.structures.type.programable.advanced.activators.FunctionActivator;
import com.alet.components.structures.type.programable.advanced.nodes.values.NodeValue;

import net.minecraft.world.WorldServer;

public class FunctionExecutor {
    
    public static void startScript(LittleAdvancedProgramableStructure executer, FunctionActivator activator, int index) {}
    
    public static void resumeScript(LittleAdvancedProgramableStructure executer, Function startingBlueprint) {}
    
    private static Function findParent(Function function, Class<? extends Function> clazz) {
        return function;
    }
    
    public static void nextMethod(LittleAdvancedProgramableStructure executer, Function blueprint, WorldServer server, int index) {}
    
    private static void buildValues(List<NodeValue> nodes, WorldServer server) {}
}
