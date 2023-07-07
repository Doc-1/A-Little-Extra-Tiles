package com.alet.client.gui.controls.programmable.blueprints.events;

import com.alet.client.gui.controls.programmable.blueprints.GuiBlueprint;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public abstract class BlueprintEvent extends GuiBlueprint {
    
    public BlueprintEvent(int id) {
        super(id);
    }
    
    public abstract void runEvent(World world, Entity entity);
}
