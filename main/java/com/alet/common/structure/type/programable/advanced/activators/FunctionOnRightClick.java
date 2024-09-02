package com.alet.common.structure.type.programable.advanced.activators;

import java.util.HashSet;

import com.alet.common.structure.type.programable.advanced.nodes.values.NodeFunction;
import com.alet.common.structure.type.programable.advanced.nodes.values.NodeValue;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class FunctionOnRightClick extends FunctionActivator {
    
    public FunctionOnRightClick(int id) {
        super("on_right_click", id, ACTIVATOR_COLOR);
    }
    
    @Override
    public void setValues() {
        
    }
    
    @Override
    public void setFunctionNodes() {
        this.senderNodes.add((NodeValue) new NodeFunction("event_out", "event", true).setAsSender());
    }
    
    @Override
    public boolean shouldRun(World world, HashSet<Entity> entities) {
        return false;
    }
    
}
