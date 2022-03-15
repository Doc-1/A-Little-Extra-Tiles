package com.alet.common.programmer.functions;

import java.util.List;

import com.alet.client.gui.controls.programmer.Function;

public class FunctionSleep extends Function {
    
    public boolean result;
    public int delay;
    
    public FunctionSleep(String nextFunction, String... values) {
        super(nextFunction, values);
    }
    
    @Override
    public void run() {
        this.completed = true;
    }
    
    @Override
    public boolean isEvent() {
        return false;
    }
    
    @Override
    public Function setValues(List<Object> values) {
        delay = Integer.parseInt((String) values.get(0));
        return this;
    }
    
}
