package com.alet.client.gui.controls.menu;

import java.util.ArrayList;

import com.alet.client.gui.controls.menu.GuiTreePart.EnumPartType;
import com.creativemd.creativecore.common.gui.GuiControl;
import com.creativemd.creativecore.common.gui.container.GuiParent;
import com.creativemd.creativecore.common.gui.event.gui.GuiControlClickEvent;

public class GuiPopupMenu extends GuiParent {
    
    private boolean pressed = false;
    public GuiMenu menu;
    public GuiControl selected;
    public Class<? extends GuiControl> listenFor;
    
    public GuiPopupMenu(String name, GuiMenu menu, int x, int y, int width, int height) {
        super(name, x, y, width, height);
        this.menu = menu;
    }
    
    /** Specify the control it should look for when you press right click.
     * 
     * @param controlClass
     */
    public void listenFor(Class<? extends GuiControl> controlClass) {
        this.listenFor = controlClass;
    }
    
    @Override
    public boolean mousePressed(int x, int y, int button) {
        return true;
    }
    
    /** Gets the control that the cursor is over
     * 
     * @param x
     *            Cursor x pos
     * @param y
     *            Cursor y pos
     * @param button
     *            Mouse button pressed
     * @param controls
     *            List of controls to look through
     * @return
     *         Found control cursor is over. */
    public GuiControl controlOver(int x, int y, int button, ArrayList<GuiControl> controls) {
        for (GuiControl control : controls) {
            if (!(control instanceof GuiMenuPart) && control.isMouseOver((x), (y))) {
                selected = control;
                this.raiseEvent(new GuiControlClickEvent(this, x, y, button));
            }
            if (control instanceof GuiParent) {
                GuiControl found = controlOver(x, y, button, ((GuiParent) control).controls);
                if (found != null) {
                    selected = found;
                    this.raiseEvent(new GuiControlClickEvent(this, x, y, button));
                }
            }
        }
        return null;
    }
    
    /** Verifies that a part of the menu has been clicked on. Used to auto-close the menu when clicked out of the menu.
     * 
     * @param x
     *            Cursor x pos
     * @param y
     *            Cursor y pos
     * @param button
     *            Mouse button pressed
     * @param controls
     *            List of controls to look through
     * @return
     *         Found menu part under the cursor. */
    public GuiControl menuOver(int x, int y, int button, ArrayList<GuiControl> controls) {
        for (GuiControl control : controls) {
            if (control instanceof GuiMenuPart && control.isMouseOver((x - menu.posX), (y - menu.posY - 3))) {
                return control;
            }
            if (control instanceof GuiParent) {
                GuiControl found = menuOver(x, y, button, ((GuiParent) control).controls);
                if (found != null)
                    return found;
            }
        }
        return null;
    }
    
    public boolean mouseOver(int x, int y, int button, GuiControl found) {
        for (GuiTreePart part : menu.listOfParts) {
            if (found != null && found.equals(part)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public void mouseReleased(int x, int y, int button) {
        
        GuiMenuPart found = (GuiMenuPart) menuOver(x, y, button, this.getParent().controls);
        if (button == 1) {
            this.raiseEvent(new GuiControlClickEvent(this, x, y, button));
            if (!pressed) {
                menu.posX = x - 2;
                menu.posY = y - 23;
                menu.closeAllMenus();
                this.getParent().controls.add(1, menu);
                this.getParent().refreshControls();
            } else {
                menu.posX = x - 2;
                menu.posY = y - 23;
                menu.closeAllMenus();
            }
            pressed = true;
        } else if (button != 2 && (button == 0 && found == null || found.type.equals(EnumPartType.Leaf))) {
            menu.closeAllMenus();
            this.getParent().removeControl(menu);
            pressed = false;
        }
    }
}
