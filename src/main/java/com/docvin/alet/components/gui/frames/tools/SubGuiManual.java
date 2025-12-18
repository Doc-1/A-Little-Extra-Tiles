package com.docvin.alet.components.gui.frames.tools;

import com.creativemd.creativecore.common.gui.container.SubGui;
import com.docvin.alet.components.gui.controls.hierarchy.tree.GuiTree;
import com.docvin.alet.components.gui.controls.hierarchy.tree.GuiTreeNode;

public class SubGuiManual extends SubGui {

    @Override
    public void createControls() {
        GuiTree tree = new GuiTree("", 0, 0, 100, 100);
        this.addControl(tree);
        tree.addItem(new GuiTreeNode("", "Opening", (subGui, guiControl) -> {
        }));
        tree.addItem(new GuiTreeNode("", "Closing", (subGui, guiControl) -> {
        }).addItem(new GuiTreeNode("", "Testing", (subGui, guiControl) -> {
        })));
    }
}
