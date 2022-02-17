package com.alet.client.gui.controls.programmer;

import java.util.List;

public interface IFunction {
    
    public void run();
    
    public boolean completedRun();
    
    public boolean isEvent();
    
    public Function setValues(List<Object> values);
}
