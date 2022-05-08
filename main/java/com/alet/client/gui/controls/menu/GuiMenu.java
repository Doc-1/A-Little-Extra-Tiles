package com.alet.client.gui.controls.menu;

import java.util.ArrayList;
import java.util.List;

public class GuiMenu extends GuiMenuItem {
    
    public List<GuiMenuItem> listOfItems = new ArrayList<GuiMenuItem>();
    
    public GuiMenu(String name) {
        super(name);
    }
    
    public void add(GuiMenuItem item) {
        listOfItems.add(item);
        item.parent = this;
    }
    
}
