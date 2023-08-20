package com.alet.client.gui.controls.programmable.nodes;

import java.util.UUID;

public class GuiNodeEntityUUID extends GuiNodeValue<UUID> {
    
    public GuiNodeEntityUUID(String name, String title, byte attributes) {
        super(name, title, attributes);
        // TODO Auto-generated constructor stub
    }
    
    @Override
    public void setValue(UUID v, boolean updateModifiable) {
        this.v = v;
    }
    
    @Override
    public int setNodeColor() {
        return 0xFFFFD700;
    }
    
}
