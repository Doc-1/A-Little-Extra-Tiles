package com.docvin.alet.components.gui.controls.hierarchy;

import com.creativemd.creativecore.common.gui.GuiControl;
import com.creativemd.creativecore.common.gui.controls.gui.GuiScrollBox;

public class GuiHierarchyBaseMenu extends GuiScrollBox {


    public GuiHierarchyBaseMenu(String name, int x, int y) {
        super(name, x, y, 100, 100);
    }


    public void addItem(GuiHierarchyBaseItem baseItem) {
        baseItem.setMenu(this);
        updateControl(baseItem, controls.size());
        controls.add(baseItem);
    }

    @Override
    public void addControl(GuiControl control) {
        if (control instanceof GuiHierarchyBaseItem)
            addItem((GuiHierarchyBaseItem) control);
    }

    public void deselectAllItems(GuiHierarchyBaseItem excludedItem) {
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


    public void onNodeClosed(GuiHierarchyBaseItem baseItem) {
    }

    public void onNodeOpened(GuiHierarchyBaseItem baseItem) {
    }

    public void onNodeSelected(GuiHierarchyBaseItem baseItem) {
    }
}
