package com.alet.common.programmer.functions;

import java.util.List;

import com.alet.client.gui.controls.programmer.Function;
import com.creativemd.creativecore.common.utils.math.BooleanUtils;

public class FunctionIsInputEqual extends Function {
    
    boolean[] inputA;
    boolean[] inputB;
    
    public FunctionIsInputEqual(String nextFunction, String... values) {
        super(nextFunction, values);
    }
    
    @Override
    public void run() {
        results = BooleanUtils.equals(inputA, inputB);
        completed = true;
    }
    
    @Override
    public boolean isEvent() {
        return false;
    }
    
    @Override
    public Function setValues(List<Object> values) {
        this.inputA = (boolean[]) values.get(0);
        this.inputB = (boolean[]) values.get(1);
        return this;
    }
    
}
