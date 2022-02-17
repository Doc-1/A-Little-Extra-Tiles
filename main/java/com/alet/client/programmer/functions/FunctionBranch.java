package com.alet.client.programmer.functions;

import java.util.List;

import com.alet.client.gui.controls.programmer.Function;

public class FunctionBranch extends Function {
    
    public boolean result;
    public boolean condition;
    public String trueFunction;
    public String falseFunction;
    
    public FunctionBranch(String nextFunction, String... values) {
        super(nextFunction, values);
    }
    
    @Override
    public void run() {
        result = condition;
        this.nextFunction = condition ? trueFunction : falseFunction;
        this.completed = true;
    }
    
    @Override
    public boolean isEvent() {
        return false;
    }
    
    @Override
    public Function setValues(List<Object> values) {
        Function function = functions.get((String) values.get(0));
        condition = function.results;
        trueFunction = (String) values.get(1);
        falseFunction = (String) values.get(2);
        return this;
    }
    
}
