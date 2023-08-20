package com.alet.client.gui.controls.programmable.blueprints.events;

import com.alet.client.gui.controls.programmable.blueprints.GuiBlueprint;

import net.minecraft.world.WorldServer;

public abstract class BlueprintEvent extends GuiBlueprint {
    
    public BlueprintEvent(int id) {
        super(id);
    }
    
    public abstract void runEvent(WorldServer server);
}
