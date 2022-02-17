package com.alet.client.programmer.functions;

import java.util.List;

import com.alet.client.gui.controls.programmer.Function;
import com.creativemd.creativecore.common.utils.math.BooleanUtils;

public class FunctionEventPulseReceived extends Function {
    
    boolean[] state;
    boolean[] oldState;
    
    public FunctionEventPulseReceived(String nextFunction, String... values) {
        super(nextFunction, values);
        // TODO Auto-generated constructor stub
    }
    
    @Override
    public void run() {
        completed = BooleanUtils.equals(state, oldState);
        oldState = state;
    }
    
    @Override
    public Function setValues(List<Object> values) {
        this.state = (boolean[]) values.get(0);
        this.oldState = this.state;
        return this;
    }
    
    @Override
    public boolean isEvent() {
        return true;
    }
    
}
