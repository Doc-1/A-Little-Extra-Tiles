package com.alet.client.gui.controls.programmer;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public abstract class Function implements IFunction {
    
    public boolean completed = false;
    public boolean results;
    public final List<String> values;
    public String nextFunction;
    public Map<String, Function> functions = new LinkedHashMap<String, Function>();
    
    public Function(String nextFunction, String... values) {
        List<String> vs = new ArrayList<String>();
        for (String v : values) {
            vs.add(v);
        }
        this.values = vs;
        this.nextFunction = nextFunction;
    }
    
    public Function setFunctionList(Map<String, Function> functions) {
        this.functions = functions;
        return this;
    }
    
    @Override
    public boolean completedRun() {
        return completed;
    }
}
