package com.alet.common.gui.controls.hierarchy.items;

import com.creativemd.creativecore.common.gui.GuiControl;
import com.creativemd.creativecore.common.gui.event.gui.GuiControlClickEvent;

import net.minecraft.init.SoundEvents;

public abstract class GuiHierarchyItem extends GuiControl {
    
    public String title;
    
    public GuiHierarchyItem(String name, String title, int x, int y, int width, int height) {
        super(name, x, y, width, height);
        this.title = title;
    }
    
    @Override
    public boolean mousePressed(int x, int y, int button) {
        if (this.isMouseOver(x, y)) {
            this.getParent().raiseEvent(new GuiControlClickEvent(this, x, y, button));
            playSound(SoundEvents.UI_BUTTON_CLICK);
            return true;
        }
        return false;
    }
}
