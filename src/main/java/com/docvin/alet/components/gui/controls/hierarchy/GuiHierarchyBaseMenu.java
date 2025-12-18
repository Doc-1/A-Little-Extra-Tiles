package com.docvin.alet.components.gui.controls.hierarchy;

import com.creativemd.creativecore.common.gui.GuiControl;
import com.creativemd.creativecore.common.gui.controls.gui.GuiScrollBox;

public class GuiHierarchyBaseMenu extends GuiScrollBox {


    public GuiHierarchyBaseMenu(String name, int x, int y) {
        super(name, x, y, 100, 100);
    }


    public void addItem(GuiHierarchyBaseItem baseItem) {
        int count = this.getControls().size();
        int offset = 14;
        if (count > 0)
            baseItem.posY += offset;
        baseItem.setMenu(this);
        this.addControl(baseItem);
    }

    @Override
    public void addControl(GuiControl control) {
        if (control instanceof GuiHierarchyBaseItem) {
            super.addControl(control);
        }
    }

    private void deselectAllItems(GuiHierarchyBaseItem excludedItem) {
        for (Object control : this.getControls())
            if (control instanceof GuiHierarchyBaseItem && control != excludedItem) {
                GuiHierarchyBaseItem item = (GuiHierarchyBaseItem) control;
                item.setSelected(false);
                if (item.getHierarchyPosition().isContainer())
                    deselectAllItems(excludedItem, item);
            }


    }

    private void deselectAllItems(GuiHierarchyBaseItem excludedItem, GuiHierarchyBaseItem item) {
        for (GuiHierarchyBaseItem internalItem : item.items) {
            if (internalItem != excludedItem) {
                internalItem.setSelected(false);
                if (internalItem.getHierarchyPosition().isContainer())
                    deselectAllItems(excludedItem, internalItem);
            }
        }
    }

    public void onNodeOpened(GuiHierarchyBaseItem baseItem) {
        for (GuiHierarchyBaseItem item : baseItem.items) {
            System.out.println(item);
        }
    }

    public void onNodeSelected(GuiHierarchyBaseItem baseItem) {
        deselectAllItems(baseItem);
        System.out.println(baseItem);
    }
}
