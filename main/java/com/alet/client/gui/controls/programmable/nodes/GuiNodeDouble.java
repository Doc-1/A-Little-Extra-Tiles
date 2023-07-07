package com.alet.client.gui.controls.programmable.nodes;

import com.creativemd.creativecore.common.gui.controls.gui.GuiTextfield;

public class GuiNodeDouble extends GuiNodeValue<Double> {
    
    public GuiNodeDouble(String name, String title, byte attributes) {
        super(name, title, attributes);
    }
    
    @Override
    public int setNodeColor() {
        // TODO Auto-generated method stub
        return 0xffD43FC0;
    }
    
    @Override
    public void createControls() {
        if (this.isModifiable()) {
            GuiTextfield value = new GuiTextfield("value", "0", 0, 7, 55, 7).setFloatOnly();
            this.width = Math.max(67, this.width);
            value.posX = this.width - 67;
            this.height = 26;
            this.addControl(value);
        }
        
    }
    
    @Override
    public void setValue(Double v, boolean updateModifiable) {
        if (updateModifiable && this.isModifiable()) {
            GuiTextfield value = (GuiTextfield) this.get("value");
            value.text = v + "";
        }
    }
}
