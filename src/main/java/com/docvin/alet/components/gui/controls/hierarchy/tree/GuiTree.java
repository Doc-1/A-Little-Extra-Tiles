package com.docvin.alet.components.gui.controls.hierarchy.tree;

import com.creativemd.creativecore.common.gui.GuiControl;
import com.docvin.alet.components.gui.controls.hierarchy.GuiHierarchyBaseItem;
import com.docvin.alet.components.gui.controls.hierarchy.GuiHierarchyBaseMenu;

import java.util.ArrayList;
import java.util.List;

public class GuiTree extends GuiHierarchyBaseMenu {

    public GuiTree(String name, int x, int y) {
        super(name, x, y);

    }

    @Override
    public void addItem(GuiHierarchyBaseItem baseItem) {
        int count = this.getControls().size();
        int containerCount = baseItem.getContainerCount();
        int offset = 14;
        if (count > 0)
            baseItem.posY = offset * count;
        if (containerCount > 0)
            baseItem.posX = offset * containerCount;

        super.addItem(baseItem);
    }

    private void refreshItemsOrder() {
        List<GuiControl> clone = (List<GuiControl>) this.controls.clone();
        this.controls = new ArrayList<>();

        for (GuiControl item : clone)
            this.addControl(item);
    }

    @Override
    public void onNodeClosed(GuiHierarchyBaseItem baseItem) {
        for (GuiHierarchyBaseItem item : baseItem.getNestedItems()) {
            item.setOpenedState(false);
            this.controls.remove(item);
        }
        this.refreshItemsOrder();
    }


    @Override
    public void onNodeOpened(GuiHierarchyBaseItem baseItem) {
        int id = baseItem.getID() + 1;
        this.controls.addAll(id, baseItem.getItems());
        this.refreshItemsOrder();
    }

    @Override
    public void onNodeSelected(GuiHierarchyBaseItem baseItem) {
        deselectAllItems(baseItem);
    }
}
