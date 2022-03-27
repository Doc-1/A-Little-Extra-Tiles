package com.alet.client.gui.controls.programmer;

import java.util.ArrayList;
import java.util.List;

public abstract class Function implements IFunction {
    
    protected boolean completed = false;
    public boolean results;
    public final List<String> values;
    public String nextFunction;
    public BlueprintExecutor executor;
    
    public Function(String nextFunction, String... values) {
        List<String> vs = new ArrayList<String>();
        for (String v : values) {
            vs.add(v);
        }
        this.values = vs;
        this.nextFunction = nextFunction;
    }
    
    @Override
    public boolean completedRun() {
        return completed;
    }
}
