package com.alet.client.gui.controls.menu;

import java.util.ArrayList;

import com.creativemd.creativecore.common.gui.GuiControl;
import com.creativemd.creativecore.common.gui.container.GuiParent;

public class GuiPopupMenu extends GuiParent {
    
    private boolean pressed = false;
    private GuiMenu menu;
    
    public GuiPopupMenu(String name, GuiMenu menu, int x, int y, int width, int height) {
        super(name, x, y, width, height);
        this.menu = menu;
    }
    
    @Override
    public boolean mousePressed(int x, int y, int button) {
        return true;
    }
    
    public GuiControl controlOver(int x, int y, int button, ArrayList<GuiControl> controls) {
        for (GuiControl control : controls) {
            if (control instanceof GuiMenuPart && control.isMouseOver((x - menu.posX), (y - menu.posY))) {
                return control;
            }
            if (control instanceof GuiParent) {
                GuiControl found = controlOver(x, y, button, ((GuiParent) control).controls);
                if (found != null)
                    return found;
            }
        }
        return null;
    }
    
    public boolean mouseOver(int x, int y, int button) {
        for (GuiTreePart part : menu.listOfParts) {
            GuiControl found = controlOver(x, y, button, this.getParent().controls);
            if (found != null && found.equals(part)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public void mouseReleased(int x, int y, int button) {
        if (button == 1) {
            if (!pressed) {
                menu.posX = x - 2;
                menu.posY = y - 23;
                menu.closeAllMenus();
                this.getParent().controls.add(1, menu);
                this.getParent().refreshControls();
            } else {
                menu.posX = x - 2;
                menu.posY = y - 23;
            }
            pressed = true;
        } else if (button == 0 && !mouseOver(x, y, button)) {
            menu.closeAllMenus();
            this.getParent().removeControl(menu);
            pressed = false;
        }
    }
}
