package com.alet.components.structures.type.programable.advanced.flows;

import com.alet.components.structures.type.programable.advanced.Function;
import com.alet.components.structures.type.programable.advanced.LittleAdvancedProgramableStructure;

import net.minecraft.world.WorldServer;

public abstract class FunctionFlowControl extends Function {
    
    public FunctionFlowControl(String name, int id, int color) {
        super(name, id, color);
        // TODO Auto-generated constructor stub
    }
    
    public abstract boolean doesTick();
    
    public abstract boolean ticking();
    
    public abstract boolean doesLoop();
    
    public abstract Function looping(LittleAdvancedProgramableStructure executer, WorldServer server);
    
    public abstract boolean reachedMaxLoop();
    
}
