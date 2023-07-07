package com.alet.client.gui.controls.programmable.nodes;

import com.creativemd.creativecore.common.gui.controls.gui.GuiTextfield;

public class GuiNodeLong extends GuiNodeValue<Long> {
    
    public GuiNodeLong(String name, String title, byte attributes) {
        super(name, title, attributes);
        // TODO Auto-generated constructor stub
    }
    
    @Override
    public int setNodeColor() {
        return 0;
    }
    
    @Override
    public void setValue(Long v, boolean updateModifiable) {
        this.v = v;
        if (updateModifiable && this.isModifiable()) {
            GuiTextfield value = (GuiTextfield) this.get("value");
            value.text = v + "";
        }
    }
}
