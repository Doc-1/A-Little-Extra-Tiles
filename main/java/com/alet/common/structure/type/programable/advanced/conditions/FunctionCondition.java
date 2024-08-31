package com.alet.common.structure.type.programable.advanced.conditions;

import com.alet.common.structure.type.programable.advanced.Function;

public abstract class FunctionCondition extends Function {
    
    public FunctionCondition(String name, int id, int color, boolean sender, boolean reciever) {
        super(name, id, color, sender, reciever);
        // TODO Auto-generated constructor stub
    }
    
    public abstract boolean conditionRunEvent();
}
