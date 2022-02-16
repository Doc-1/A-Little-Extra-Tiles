package com.alet.client.gui.controls.programmer.functions;

import com.alet.client.gui.controls.programmer.IFunction;

public class FunctionEventPulseReceived implements IFunction {
    
    boolean oldState = false;
    boolean[] state;
    boolean completed = false;
    
    public FunctionEventPulseReceived() {}
    
    @Override
    public void run() {
        System.out.println("" + state[0]);
        if (oldState != state[0]) {
            completed = true;
        }
        completed = false;
        oldState = state[0];
    }
    
    @Override
    public IFunction setValues(Object... values) {
        for (Object o : values) {
            if (o instanceof boolean[]) {
                this.state = (boolean[]) o;
            }
        }
        return this;
    }
    
    @Override
    public boolean completedRun() {
        return completed;
    }
    
    @Override
    public boolean isEvent() {
        return true;
    }
    
}
