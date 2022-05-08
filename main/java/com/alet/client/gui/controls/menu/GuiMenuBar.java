package com.alet.client.gui.controls.menu;

import java.util.ArrayList;
import java.util.List;

import com.creativemd.creativecore.common.gui.GuiRenderHelper;
import com.creativemd.creativecore.common.gui.client.style.Style;
import com.creativemd.creativecore.common.gui.container.SubGui;

public class GuiMenuBar extends GuiMenuController {
    
    public GuiMenuBar(String name) {
        super(name);
    }
    
    public void addMenuTo(SubGui sub) {
        int w = 0;
        int cur = 0;
        for (GuiMenuItem item : listOfItems) {
            GuiMenuButton button = new GuiMenuButton(item.name + " ", cur, 0, item);
            this.addControl(button);
            topButtons.add(button);
            w = font.getStringWidth(item.name);
            cur += w + 10;
        }
        this.width = cur + 6;
        sub.addControl(this);
    }
    
    @Override
    protected void renderContent(GuiRenderHelper helper, Style style, int width, int height) {
        
    }
    
    @Override
    public void updateMenu(GuiMenu menu) {
        List<GuiMenuButton> temp = new ArrayList<GuiMenuButton>();
        for (int i = 0; i < menu.listOfItems.size(); i++) {
            GuiMenuItem item = menu.listOfItems.get(i);
            GuiMenuButton button;
            if (menu.parent == null)
                button = new GuiMenuButton(item.name, menu.button.posX, (menu.button.posY + 15) + (15 * i), item);
            else
                button = new GuiMenuButton(item.name, menu.button.posX + menu.button.width, (menu.button.posY) + (15 * i), item);
            temp.add(button);
            this.addControl(button);
        }
        int max = 0;
        for (GuiMenuButton button : temp) {
            max = Math.max(max, button.width);
        }
        for (GuiMenuButton button : temp) {
            button.width = max;
        }
        
        /*
        for (GuiMenuItem item : menu.listOfItems) {
            System.out.println(item.name);
        }*/
        
    }
    
    @Override
    public void clearMenus() {
        List<String> exclude = new ArrayList<String>();
        for (GuiMenuButton b : this.topButtons) {
            exclude.add(b.name);
            b.selected = false;
        }
        this.removeControls(exclude.toArray(new String[] {}));
    }
    
}
