package com.alet.client.gui.controls.programmable.blueprints.conditions;

import com.alet.client.gui.controls.programmable.blueprints.GuiBlueprint;

import net.minecraft.entity.Entity;

public abstract class BlueprintCondition extends GuiBlueprint {
    
    public BlueprintCondition(int id) {
        super(id);
    }
    
    public boolean completed = false;
    public boolean shouldLoop = false;
    
    public boolean conditionRunEvent(Entity entity) {
        if (tryToPass(entity)) {
            conditionPassed(entity);
            return true;
        }
        return false;
    }
    
    public abstract boolean tryToPass(Entity entity);
    
    public abstract void conditionPassed(Entity entity);
}
