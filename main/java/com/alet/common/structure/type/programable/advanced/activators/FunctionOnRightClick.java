package com.alet.common.structure.type.programable.advanced.activators;

import com.alet.common.structure.type.programable.nodes.values.NodeInteger;

public class FunctionOnRightClick extends FunctionActivator {
    
    public FunctionOnRightClick(int id) {
        super("on_right_click", id, ACTIVATOR_COLOR, true, false);
        
    }
    
    @Override
    public void setValues() {
        
    }
    
    @Override
    public void setFunctionNodes() {
        this.senderNodes.add(new NodeInteger("int_out", "int", true, false, true));
    }
    
}
