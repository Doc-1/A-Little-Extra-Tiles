package com.alet.client.gui.controls.menu;

public class GuiMenuItem {
    
    public boolean selected = false;
    public GuiMenu parent;
    public String name;
    public GuiMenuButton button;
    
    public GuiMenuItem(String name) {
        this.name = name;
    }
    
}
