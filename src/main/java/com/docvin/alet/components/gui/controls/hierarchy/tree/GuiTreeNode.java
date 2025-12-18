package com.docvin.alet.components.gui.controls.hierarchy.tree;

import com.creativemd.creativecore.common.gui.GuiControl;
import com.creativemd.creativecore.common.gui.GuiRenderHelper;
import com.creativemd.creativecore.common.gui.client.style.ColoredDisplayStyle;
import com.creativemd.creativecore.common.gui.client.style.Style;
import com.creativemd.creativecore.common.gui.container.GuiParent;
import com.creativemd.creativecore.common.gui.container.SubGui;
import com.docvin.alet.components.gui.controls.hierarchy.GuiHierarchyBaseItem;

import java.util.function.BiConsumer;

public class GuiTreeNode extends GuiHierarchyBaseItem {
    public static final Style SELECTED_DISPLAY = new Style("SELECTED", new ColoredDisplayStyle(50, 50, 50), new ColoredDisplayStyle(145, 201, 247), new ColoredDisplayStyle(145, 201, 247), new ColoredDisplayStyle(198, 198, 198), new ColoredDisplayStyle(0, 0, 0, 100));
    public static final Style DISPLAY = new Style("DISPLAY", new ColoredDisplayStyle(240, 240, 240, 0), new ColoredDisplayStyle(240, 240, 240, 0), new ColoredDisplayStyle(145, 201, 247), new ColoredDisplayStyle(198, 198, 198), new ColoredDisplayStyle(0, 0, 0, 100));


    public GuiTreeNode(String name, String title, BiConsumer<SubGui, GuiControl> action) {
        super(name, title, 0, 0, 0, 0, action);
        GuiRenderHelper guiRenderHelper = GuiRenderHelper.instance;
        this.height = guiRenderHelper.getFontHeight() + 4;
        this.width = guiRenderHelper.getStringWidth(this.getTitle()) + 15;
        this.style = DISPLAY;
    }


    @Override
    public GuiParent getParent() {
        GuiParent guiTree = super.getParent();
        if (guiTree instanceof GuiTree)
            return guiTree;
        else
            throw new IllegalArgumentException("GuiTreeNode is to be used in conjunction with a GuiTree Object");
    }

    @Override
    public boolean hasBorder() {
        return false;
    }


    @Override
    public void setSelected(boolean flag) {
        super.setSelected(flag);
        if (this.isSelected())
            this.style = SELECTED_DISPLAY;
        else
            this.style = DISPLAY;
    }

}
