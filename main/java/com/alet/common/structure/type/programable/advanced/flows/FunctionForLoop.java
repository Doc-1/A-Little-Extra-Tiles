package com.alet.common.structure.type.programable.advanced.flows;

import com.alet.common.structure.type.programable.LittleProgramableStructureALET;
import com.alet.common.structure.type.programable.advanced.Function;

import net.minecraft.world.WorldServer;

public class FunctionForLoop extends FunctionFlowControl {
    
    public FunctionForLoop(int id) {
        super("for_loop", id, FLOW_COLOR);
        // TODO Auto-generated constructor stub
    }
    
    @Override
    public boolean doesTick() {
        // TODO Auto-generated method stub
        return false;
    }
    
    @Override
    public boolean ticking() {
        // TODO Auto-generated method stub
        return false;
    }
    
    @Override
    public boolean doesLoop() {
        // TODO Auto-generated method stub
        return false;
    }
    
    @Override
    public Function looping(LittleProgramableStructureALET executer, WorldServer server) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public boolean reachedMaxLoop() {
        // TODO Auto-generated method stub
        return false;
    }
    
    @Override
    public void setValues() {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void setFunctionNodes() {
        //  this.senderNodes.add(new NodeInteger("int_out", "int", false, true, false));
    }
    
}