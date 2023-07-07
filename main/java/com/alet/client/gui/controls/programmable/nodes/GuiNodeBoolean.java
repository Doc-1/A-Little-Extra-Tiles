package com.alet.client.gui.controls.programmable.nodes;

import com.creativemd.creativecore.common.gui.controls.gui.GuiCheckBox;
import com.creativemd.creativecore.common.gui.controls.gui.GuiTextfield;

public class GuiNodeBoolean extends GuiNodeValue<Boolean> {
    
    public GuiNodeBoolean(String name, String title, byte attributes) {
        super(name, title, attributes);
    }
    
    @Override
    public int setNodeColor() {
        // TODO Auto-generated method stub
        return 0xffff3333;
    }
    
    @Override
    public void createControls() {
        if (this.isModifiable()) {
            GuiCheckBox value = new GuiCheckBox("value", 7, 0, false);
            this.width = Math.max(67, this.width);
            value.posX = this.width - 67;
            this.height = 26;
            this.addControl(value);
        }
        
    }
    
    @Override
    public void setValue(Boolean v, boolean updateModifiable) {
        this.v = v;
        if (updateModifiable && this.isModifiable()) {
            GuiTextfield value = (GuiTextfield) this.get("value");
            value.text = v + "";
        }
    }
}
