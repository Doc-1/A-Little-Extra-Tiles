package com.alet.client.gui.controls.programmable.blueprints;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import com.alet.client.gui.controls.programmable.blueprints.activators.BlueprintActivator;
import com.alet.client.gui.controls.programmable.blueprints.conditions.BlueprintCondition;
import com.alet.client.gui.controls.programmable.blueprints.events.BlueprintEvent;
import com.alet.client.gui.controls.programmable.blueprints.flow.BlueprintFlowControl;
import com.alet.client.gui.controls.programmable.blueprints.getters.BlueprintGetter;
import com.alet.client.gui.controls.programmable.nodes.GuiNode;
import com.alet.client.gui.controls.programmable.nodes.GuiNodeValue;

import net.minecraft.entity.Entity;
import net.minecraft.world.WorldServer;

public class BlueprintExecutor {
    
    public static void startScript(BlueprintActivator activator, HashSet<Entity> entities, WorldServer server) {
        if (activator.shouldRun(activator.structure.getWorld(), entities)) {
            GuiNode node = (GuiNode) activator.get("run");
            List<UUID> uuids = new ArrayList<UUID>();
            for (Entity e : entities)
                uuids.add(e.getPersistentID());
            activator.setUUIDs(uuids);
            activator.setNodeValue(server);
            GuiBlueprint bp = node.receiverConnections.get(0).getBlueprint();
            nextMethod(bp, server);
        }
    }
    
    private static void nextMethod(GuiBlueprint blueprint, WorldServer server) {
        
        if (blueprint != null) {
            buildValues(blueprint.nodes, server);
            if (blueprint instanceof BlueprintEvent) {
                BlueprintEvent event = (BlueprintEvent) blueprint;
                event.runEvent(server);
                nextMethod(event.getNextBlueprint(), server);
            } else if (blueprint instanceof BlueprintCondition) {
                BlueprintCondition condtion = (BlueprintCondition) blueprint;
                if (condtion.conditionRunEvent())
                    nextMethod(condtion.getNextBlueprint(), server);
            } else if (blueprint instanceof BlueprintFlowControl) {
                BlueprintFlowControl flow = (BlueprintFlowControl) blueprint;
                nextMethod(flow.getNextBlueprint(), server);
            } else if (blueprint instanceof BlueprintGetter) {
                BlueprintGetter getter = (BlueprintGetter) blueprint;
                nextMethod(getter.getNextBlueprint(), server);
            }
        }
    }
    
    private static void buildValues(List<GuiNode> nodes, WorldServer server) {
        for (GuiNode n : nodes) {
            if (n instanceof GuiNodeValue)
                ((GuiNodeValue) n).getValue(server);
        }
    }
}
