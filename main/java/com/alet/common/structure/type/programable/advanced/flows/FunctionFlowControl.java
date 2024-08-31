package com.alet.common.structure.type.programable.advanced.flows;

import com.alet.common.structure.type.programable.LittleProgramableStructureALET;
import com.alet.common.structure.type.programable.advanced.Function;

import net.minecraft.world.WorldServer;

public abstract class FunctionFlowControl extends Function {
    
    public FunctionFlowControl(String name, int id, int color, boolean sender, boolean reciever) {
        super(name, id, color, sender, reciever);
        // TODO Auto-generated constructor stub
    }
    
    public abstract boolean doesTick();
    
    public abstract boolean ticking();
    
    public abstract boolean doesLoop();
    
    public abstract Function looping(LittleProgramableStructureALET executer, WorldServer server);
    
    public abstract boolean reachedMaxLoop();
    
}
