package com.alet.client.gui.event.gui;

import com.creativemd.creativecore.common.gui.GuiControl;
import com.creativemd.creativecore.common.gui.event.gui.GuiControlEvent;

public class GuiControlReleaseEvent extends GuiControlEvent {
    
    public int mouseX;
    public int mouseY;
    public int button;
    
    public GuiControlReleaseEvent(GuiControl source, int mouseX, int mouseY, int button) {
        super(source);
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.button = button;
    }
    
    @Override
    public boolean isCancelable() {
        return false;
    }
    
}
