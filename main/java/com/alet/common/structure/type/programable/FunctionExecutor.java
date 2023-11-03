package com.alet.common.structure.type.programable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import com.alet.common.structure.type.programable.functions.Function;
import com.alet.common.structure.type.programable.functions.activators.FunctionActivator;
import com.alet.common.structure.type.programable.functions.conditions.FunctionCondition;
import com.alet.common.structure.type.programable.functions.events.FunctionEvent;
import com.alet.common.structure.type.programable.functions.flows.FunctionFlowControl;
import com.alet.common.structure.type.programable.functions.flows.FunctionForLoop;
import com.alet.common.structure.type.programable.functions.getters.FunctionGetter;
import com.alet.common.structure.type.programable.nodes.values.NodeFunction;
import com.alet.common.structure.type.programable.nodes.values.NodeValue;

import net.minecraft.entity.Entity;
import net.minecraft.world.WorldServer;

public class FunctionExecutor {
    
    public static void startScript(LittleProgramableStructureALET executer, FunctionActivator activator, int index) {
        WorldServer server = (WorldServer) executer.getWorld();
        HashSet<Entity> entities = executer.entities;
        if (activator.shouldRun(activator.structure.getWorld(), entities)) {
            NodeFunction node = activator.methodSender;
            List<UUID> uuids = new ArrayList<UUID>();
            for (Entity e : entities)
                uuids.add(e.getPersistentID());
            // activator.setUUIDs(uuids);
            //activator.setNodeValue(server);
            Function bp = node.receiverConnections.get(0).parent;
            nextMethod(executer, bp, server, index);
        }
    }
    
    public static void resumeScript(LittleProgramableStructureALET executer, Function startingBlueprint) {
        WorldServer server = (WorldServer) executer.getWorld();
        HashSet<Entity> entities = executer.entities;
        Function activator = findParent(startingBlueprint, FunctionActivator.class);
        List<UUID> uuids = new ArrayList<UUID>();
        for (Entity e : entities)
            uuids.add(e.getPersistentID());
        //activator.setUUIDs(uuids);
        // activator.setNodeValue(server);
        nextMethod(executer, startingBlueprint, server, -1);
        
    }
    
    private static Function findParent(Function function, Class<? extends Function> clazz) {
        if (function != null) {
            NodeFunction node = function.methodReciever;
            if (node == null || node.senderConnection == null)
                return null;
            Function blueprint = node.senderConnection.parent;
            if (blueprint != null)
                if (clazz.isAssignableFrom(blueprint.getClass()))
                    return blueprint;
                else
                    return findParent(blueprint, clazz);
        }
        return null;
    }
    
    public static void nextMethod(LittleProgramableStructureALET executer, Function blueprint, WorldServer server, int index) {
        Function bp = blueprint;
        Function last = null;
        boolean wipeEntities = true;
        while (bp != null) {
            buildValues(bp.recieverNodes, server);
            buildValues(bp.senderNodes, server);
            if (bp instanceof FunctionEvent) {
                FunctionEvent event = (FunctionEvent) bp;
                event.runEvent(server);
                bp = bp.getNextFunction();
                wipeEntities = bp == null;
            } else if (bp instanceof FunctionCondition) {
                FunctionCondition condtion = (FunctionCondition) bp;
                bp = condtion.conditionRunEvent() ? bp.getNextFunction() : null;
                wipeEntities = bp == null;
            } else if (bp instanceof FunctionFlowControl) {
                FunctionFlowControl flow = (FunctionFlowControl) bp;
                if (flow.doesTick()) {
                    // executer.startingBlueprint = flow;
                    if (flow.ticking()) {
                        bp = bp.getNextFunction();
                        //executer.startingBlueprint = null;
                    } else {
                        bp = null;
                        continue;
                    }
                } else if (flow.doesLoop()) {
                    bp = flow.looping(executer, server);
                }
            } else if (bp instanceof FunctionGetter) {
                bp = bp.getNextFunction();
                wipeEntities = bp == null;
            }
            if (bp == null && last != null) {
                FunctionFlowControl loop = (FunctionFlowControl) findParent(last, FunctionForLoop.class);
                if (loop != null && loop.doesLoop() && !loop.reachedMaxLoop())
                    bp = loop;
            } else
                last = bp;
        }
        if (executer.resumeFrom == null && wipeEntities && (executer.activators.size() - 1 <= index || index == -1))
            executer.entities = new HashSet<Entity>();
    }
    
    private static void buildValues(List<NodeValue> nodes, WorldServer server) {
        for (NodeValue n : nodes) {
            n.setNodeValue(server);
        }
    }
}
