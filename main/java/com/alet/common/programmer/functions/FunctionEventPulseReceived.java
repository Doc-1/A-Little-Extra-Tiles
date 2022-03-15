package com.alet.common.programmer.functions;

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
        //if (oldState == null)
        //    oldState = new boolean[] { false };
        completed = !BooleanUtils.equals(state, BooleanUtils.asArray(true));
    }
    
    @Override
    public Function setValues(List<Object> values) {
        this.state = (boolean[]) values.get(0);
        return this;
    }
    
    @Override
    public boolean isEvent() {
        return true;
    }
    
}
