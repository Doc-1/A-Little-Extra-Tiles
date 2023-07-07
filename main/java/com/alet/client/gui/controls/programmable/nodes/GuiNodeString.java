package com.alet.client.gui.controls.programmable.nodes;

import com.creativemd.creativecore.common.gui.controls.gui.GuiTextfield;

public class GuiNodeString extends GuiNodeValue<String> {
    
    public GuiNodeString(String name, String title, byte attributes) {
        super(name, title, attributes);
        // TODO Auto-generated constructor stub
    }
    
    @Override
    public int setNodeColor() {
        // TODO Auto-generated method stub
        return 0xffFF9933;
    }
    
    @Override
    public void createControls() {
        if (this.isModifiable()) {
            GuiTextfield value = new GuiTextfield("value", this.v == null ? "" : this.v, 0, 7, 55, 7);
            this.width = Math.max(67, this.width);
            value.posX = this.width - 67;
            this.height = 26;
            this.addControl(value);
        }
    }
    
    @Override
    public void setValue(String v, boolean updateModifiable) {
        this.v = v;
        if (updateModifiable && this.isModifiable()) {
            GuiTextfield value = (GuiTextfield) this.get("value");
            value.text = (String) v;
        }
    }
}
