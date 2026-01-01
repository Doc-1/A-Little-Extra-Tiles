package com.docvin.alet.components.gui.controls.hierarchy.tree;

import com.creativemd.creativecore.common.gui.GuiControl;
import com.creativemd.creativecore.common.gui.GuiRenderHelper;
import com.creativemd.creativecore.common.gui.client.style.ColoredDisplayStyle;
import com.creativemd.creativecore.common.gui.client.style.Style;
import com.creativemd.creativecore.common.gui.container.GuiParent;
import com.creativemd.creativecore.common.gui.container.SubGui;
import com.creativemd.creativecore.common.utils.mc.ColorUtils;
import com.docvin.alet.components.gui.controls.hierarchy.GuiHierarchyBaseItem;
import net.minecraft.client.renderer.GlStateManager;

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

    public GuiTreeNode(String name, String title) {
        this(name, title, (subGui, guiControl) -> {

        });
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

    @Override
    protected void renderContent(GuiRenderHelper helper, Style style, int x, int y) {
        super.renderContent(helper, style, x, y);
        int color = ColorUtils.WHITE;
        if (this.getHierarchyPosition().isContainer()) {
            GlStateManager.pushMatrix();
            color = (color & 16579836) >> 2 | color & -16777216;
            GlStateManager.translate(4, 2, 0);
            if (this.isOpened()) {
                GlStateManager.translate(6, 3, 0);
                GlStateManager.rotate(90, 0, 0, 1);
            }
            for (int f = 0; f < 4; f++) {
                helper.drawRect(f, f + 1, f + 1, f + 2, color);
                helper.drawRect(f, 7 - f, f + 1, 8 - f, color);
            }
            if (this.isOpened()) {
                GlStateManager.translate(-1, 1, 0);
            }
            color = ColorUtils.WHITE;
            for (int f = 0; f < 4; f++) {
                helper.drawRect(f, f, f + 1, f + 1, color);
                helper.drawRect(f, 6 - f, f + 1, 7 - f, color);
            }
            GlStateManager.popMatrix();
        }
    }
}
