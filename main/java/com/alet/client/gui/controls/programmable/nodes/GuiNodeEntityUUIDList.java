package com.alet.client.gui.controls.programmable.nodes;

import java.util.List;
import java.util.UUID;

public class GuiNodeEntityUUIDList extends GuiNodeValue<List<UUID>> {
    
    public GuiNodeEntityUUIDList(String name, String title, byte attributes) {
        super(name, title, attributes);
        // TODO Auto-generated constructor stub
    }
    
    @Override
    public int setNodeColor() {
        return 0;
    }
    
    @Override
    public void setValue(List<UUID> v, boolean updateModifiable) {
        
    }
    
}
