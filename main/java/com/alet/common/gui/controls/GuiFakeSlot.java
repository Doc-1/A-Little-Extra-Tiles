package com.alet.common.gui.controls;

import com.creativemd.creativecore.common.gui.client.style.DisplayStyle;
import com.creativemd.creativecore.common.gui.client.style.Style;
import com.creativemd.creativecore.common.gui.container.GuiParent;
import com.creativemd.creativecore.common.gui.controls.container.SlotControlNoSync;
import com.creativemd.creativecore.common.gui.event.gui.GuiControlChangedEvent;
import com.creativemd.creativecore.common.slots.SlotPreview;

import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;

public class GuiFakeSlot extends GuiParent {
    
    public InventoryBasic basic = new InventoryBasic("", false, 1);
    
    public GuiFakeSlot(String name, int x, int y, int width, int height) {
        super(name, x, y, width, height);
        ItemStack itemStack = ItemStack.EMPTY;
        addControl(new SlotControlNoSync(new SlotPreview(basic, 0, 0, 0)).getGuiControl());
        basic.setInventorySlotContents(0, itemStack);
    }
    
    @Override
    public boolean mousePressed(int x, int y, int button) {
        if (button == 1 || button == 0) {
            updateItemStack(ItemStack.EMPTY);
        }
        return super.mousePressed(x, y, button);
    }
    
    @Override
    public Style getStyle() {
        return new Style("empty", DisplayStyle.emptyDisplay, DisplayStyle.emptyDisplay, DisplayStyle.emptyDisplay, DisplayStyle.emptyDisplay, DisplayStyle.emptyDisplay);
    }
    
    public void updateItemStack(ItemStack itemStack) {
        basic.setInventorySlotContents(0, itemStack);
        this.raiseEvent(new GuiControlChangedEvent(this));
    }
}