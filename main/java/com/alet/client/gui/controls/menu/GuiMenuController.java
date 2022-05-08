package com.alet.client.gui.controls.menu;

import java.util.ArrayList;
import java.util.List;

import com.creativemd.creativecore.common.gui.container.GuiParent;
import com.creativemd.creativecore.common.gui.container.SubGui;

public abstract class GuiMenuController extends GuiParent implements IGuiMenu {
    
    protected List<GuiMenuButton> topButtons = new ArrayList<GuiMenuButton>();
    public List<GuiMenuItem> listOfItems = new ArrayList<GuiMenuItem>();
    public List<GuiMenu> listOfSelectedMenus = new ArrayList<GuiMenu>();
    
    public GuiMenuController(String name) {
        super(name, 120, 0, 0, 120);
        this.style = style.emptyStyle;
    }
    
    public void add(GuiMenuItem item) {
        this.listOfItems.add(item);
    }
    
    public abstract void updateMenu(GuiMenu menu);
    
    public abstract void addMenuTo(SubGui sub);
    
    public abstract void clearMenus();
}
