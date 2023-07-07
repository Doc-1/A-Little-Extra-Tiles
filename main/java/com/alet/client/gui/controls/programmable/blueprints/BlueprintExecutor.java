package com.alet.client.gui.controls.programmable.blueprints;

import java.util.List;

import com.alet.client.gui.controls.programmable.blueprints.activators.BlueprintActivator;
import com.alet.client.gui.controls.programmable.blueprints.conditions.BlueprintCondition;
import com.alet.client.gui.controls.programmable.blueprints.events.BlueprintEvent;
import com.alet.client.gui.controls.programmable.blueprints.flow.BlueprintFlowControl;
import com.alet.client.gui.controls.programmable.blueprints.getters.BlueprintGetter;
import com.alet.client.gui.controls.programmable.nodes.GuiNode;
import com.alet.client.gui.controls.programmable.nodes.GuiNodeValue;

import net.minecraft.entity.Entity;

public class BlueprintExecutor {
    
    public static void startScript(BlueprintActivator activator, Entity entity) {
        if (activator.shouldRun(activator.structure.getWorld(), entity)) {
            GuiNode node = (GuiNode) activator.get("run");
            GuiBlueprint bp = node.receiverConnection.getBlueprint();
            nextMethod(bp, entity);
        }
    }
    
    private static void nextMethod(GuiBlueprint blueprint, Entity entity) {
        if (blueprint != null) {
            System.out.println(blueprint);
            buildValues(blueprint.nodes, entity);
            if (blueprint instanceof BlueprintEvent) {
                BlueprintEvent event = (BlueprintEvent) blueprint;
                event.runEvent(entity.world, entity);
                nextMethod(event.getNextBlueprint(), entity);
            } else if (blueprint instanceof BlueprintCondition) {
                BlueprintCondition condtion = (BlueprintCondition) blueprint;
                if (condtion.conditionRunEvent(entity))
                    nextMethod(condtion.getNextBlueprint(), entity);
            } else if (blueprint instanceof BlueprintFlowControl) {
                BlueprintFlowControl flow = (BlueprintFlowControl) blueprint;
                nextMethod(flow.getNextBlueprint(), entity);
            } else if (blueprint instanceof BlueprintGetter) {
                BlueprintGetter getter = (BlueprintGetter) blueprint;
                nextMethod(getter.getNextBlueprint(), entity);
            }
        }
    }
    
    private static void buildValues(List<GuiNode> nodes, Entity entity) {
        for (GuiNode n : nodes) {
            if (n instanceof GuiNodeValue)
                System.out.println(((GuiNodeValue) n).getValue(entity));
        }
    }
}
