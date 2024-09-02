package com.alet.common.structure.type.programable.advanced.conditions;

import com.alet.common.structure.type.programable.advanced.Function;

public abstract class FunctionCondition extends Function {
    
    public FunctionCondition(String name, int id, int color) {
        super(name, id, color);
        // TODO Auto-generated constructor stub
    }
    
    public abstract boolean conditionRunEvent();
}
