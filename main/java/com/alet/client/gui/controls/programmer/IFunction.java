package com.alet.client.gui.controls.programmer;

public interface IFunction {
    
    public void run();
    
    public boolean completedRun();
    
    public boolean isEvent();
    
    public IFunction setValues(Object... values);
}
