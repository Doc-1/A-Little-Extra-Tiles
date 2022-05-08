package com.alet.client.gui.controls.menu;

import com.creativemd.creativecore.common.gui.client.style.Style;
import com.creativemd.creativecore.common.gui.controls.gui.GuiButton;

public class GuiMenuButton extends GuiButton implements IGuiMenu {
    
    private boolean isItem;
    public boolean selected;
    private GuiMenuItem item;
    
    public GuiMenuButton(String name, int x, int y, GuiMenuItem item) {
        super(name, x, y);
        this.item = item;
        this.item.button = this;
        this.isItem = !(item instanceof GuiMenu);
        this.height = 15;
    }
    
    @Override
    public Style getStyle() {
        if (this.isMouseOver() || selected)
            return highlighted;
        else
            return background;
    }
    
    @Override
    public void onClicked(int x, int y, int button) {
        selected = !selected;
        GuiMenuController controller = (GuiMenuController) this.getParent();
        if (!isItem) {
            if (selected)
                controller.listOfSelectedMenus.add((GuiMenu) this.item);
            else
                controller.listOfSelectedMenus.remove((GuiMenu) this.item);
            controller.updateMenu((GuiMenu) this.item);
        } else {
            controller.clearMenus();
        }
    }
}
