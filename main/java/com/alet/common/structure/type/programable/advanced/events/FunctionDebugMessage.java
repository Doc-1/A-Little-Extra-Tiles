package com.alet.common.structure.type.programable.advanced.events;

import com.alet.common.structure.type.programable.advanced.nodes.values.NodeString;
import com.alet.common.structure.type.programable.advanced.nodes.values.NodeValue;

import net.minecraft.world.WorldServer;

public class FunctionDebugMessage extends FunctionEvent {
    
    public FunctionDebugMessage(int id) {
        super("debug_message", id, EVENT_COLOR);
        this.setAsReciever();
        this.setAsSender();
    }
    
    @Override
    public void runEvent(WorldServer server) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void setFunctionNodes() {
        this.senderNodes.add((NodeValue) new NodeString("msg_in", "msg", true).setAsReciever());
    }
    
    @Override
    public void setValues() {
        // TODO Auto-generated method stub
        
    }
    
}
