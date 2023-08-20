package com.alet.client.gui.controls.programmable.blueprints.conditions;

import com.alet.client.gui.controls.programmable.blueprints.GuiBlueprint;

public abstract class BlueprintCondition extends GuiBlueprint {
    
    public BlueprintCondition(int id) {
        super(id);
    }
    
    public boolean completed = false;
    public boolean shouldLoop = false;
    
    public boolean conditionRunEvent() {
        if (tryToPass()) {
            return true;
        }
        return false;
    }
    
    public abstract boolean tryToPass();
    
}
