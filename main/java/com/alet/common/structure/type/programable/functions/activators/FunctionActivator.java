package com.alet.common.structure.type.programable.functions.activators;

import java.util.HashSet;

import com.alet.common.structure.type.programable.functions.Function;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public abstract class FunctionActivator extends Function {
    
    public FunctionActivator(String name, int color, boolean sender, boolean reciever) {
        super(name, color, sender, reciever);
        // TODO Auto-generated constructor stub
    }
    
    public boolean shouldRun(World world, HashSet<Entity> entities) {
        // TODO Auto-generated method stub
        return false;
    }
    
}
