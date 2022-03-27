package com.alet.common.programmer.functions;

import java.util.List;

import com.alet.client.gui.controls.programmer.Function;
import com.creativemd.creativecore.common.utils.math.BooleanUtils;
import com.creativemd.littletiles.common.structure.type.premade.signal.LittleSignalOutput;

public class FunctionSetInteger extends Function {
    
    public LittleSignalOutput output;
    public int integer;
    
    public FunctionSetInteger(String nextFunction, String... values) {
        super(nextFunction, values);
    }
    
    @Override
    public void run() {
        boolean[] state = new boolean[] {};
        BooleanUtils.intToBool(integer, state);
        output.updateState(state);
        this.completed = true;
    }
    
    @Override
    public boolean isEvent() {
        
        return false;
    }
    
    @Override
    public Function setValues(List<Object> values) {
        integer = (int) values.get(0);
        output = (LittleSignalOutput) values.get(1);
        return this;
    }
    
}
