package com.alet.client.programmer.functions;

import java.util.List;

import com.alet.client.gui.controls.programmer.Function;
import com.creativemd.littletiles.common.structure.type.premade.signal.LittleSignalOutput;

public class FunctionSetOutput extends Function {
    
    public LittleSignalOutput output;
    public boolean[] state;
    
    public FunctionSetOutput(String nextFunction, String... values) {
        super(nextFunction, values);
    }
    
    @Override
    public void run() {
        output.updateState(state);
        this.completed = true;
    }
    
    @Override
    public boolean isEvent() {
        
        return false;
    }
    
    @Override
    public Function setValues(List<Object> values) {
        state = (boolean[]) values.get(0);
        output = (LittleSignalOutput) values.get(1);
        return this;
    }
    
}
