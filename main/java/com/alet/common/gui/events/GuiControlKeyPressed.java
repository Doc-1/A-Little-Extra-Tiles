package com.alet.common.gui.events;

import org.lwjgl.input.Keyboard;

import com.creativemd.creativecore.common.gui.GuiControl;
import com.creativemd.creativecore.common.gui.event.gui.GuiControlEvent;

public class GuiControlKeyPressed extends GuiControlEvent {
    
    public String keyName;
    public int key;
    public boolean ctrl;
    public boolean alt;
    
    public GuiControlKeyPressed(GuiControl source, boolean ctrl, boolean alt, int key) {
        super(source);
        this.key = key;
        this.keyName = Keyboard.getKeyName(key);
        this.ctrl = ctrl;
        this.alt = alt;
    }
    
    @Override
    public boolean isCancelable() {
        return false;
    }
    
}
