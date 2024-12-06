package com.alet.components.structures.type.programable.advanced.activators;

import java.util.HashSet;

import com.alet.components.structures.type.programable.advanced.Function;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public abstract class FunctionActivator extends Function {
    
    public FunctionActivator(String name, int id, int color) {
        super(name, id, color);
        // TODO Auto-generated constructor stub
    }
    
    public abstract boolean shouldRun(World world, HashSet<Entity> entities);
    
}
