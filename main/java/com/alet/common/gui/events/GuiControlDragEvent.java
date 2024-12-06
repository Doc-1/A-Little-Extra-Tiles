package com.alet.common.gui.events;

import com.creativemd.creativecore.common.gui.GuiControl;
import com.creativemd.creativecore.common.gui.event.gui.GuiControlEvent;

public class GuiControlDragEvent extends GuiControlEvent {
    public int mouseX;
    public int mouseY;
    public int button;
    
    public GuiControlDragEvent(GuiControl source, int mouseX, int mouseY, int button) {
        super(source);
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.button = button;
    }
    
    @Override
    public boolean isCancelable() {
        // TODO Auto-generated method stub
        return false;
    }
    
}
