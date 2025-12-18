package com.docvin.alet.components.gui.controls.hierarchy;

import com.creativemd.creativecore.common.gui.GuiControl;
import com.creativemd.creativecore.common.gui.GuiRenderHelper;
import com.creativemd.creativecore.common.gui.client.style.Style;
import com.creativemd.creativecore.common.gui.container.SubGui;
import com.creativemd.creativecore.common.utils.mc.ColorUtils;
import net.minecraft.client.renderer.GlStateManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public abstract class GuiHierarchyBaseItem extends GuiControl {

    @NotNull
    private final BiConsumer<SubGui, GuiControl> action;
    @Nullable
    protected GuiHierarchyBaseItem parent;
    protected List<GuiHierarchyBaseItem> items = new ArrayList<>();
    boolean isSelected = false;
    private String title;
    private GuiHierarchyBaseMenu menu;

    protected GuiHierarchyBaseItem(String name, String title, int x, int y, int width, int height, @NotNull BiConsumer<SubGui, GuiControl> action) {
        super(name, x, y, width, height);
        this.action = action;
        this.setTitle(title);
    }

    public HierarchyPosition getHierarchyPosition() {
        if (parent != null) {
            if (this.items.isEmpty())
                return HierarchyPosition.ITEM;
            else
                return HierarchyPosition.CONTAINER;
        } else {
            if (this.items.isEmpty())
                return HierarchyPosition.ROOT_ITEM;
            else
                return HierarchyPosition.ROOT_CONTAINER;
        }
    }

    public GuiHierarchyBaseItem addItem(@NotNull GuiHierarchyBaseItem item) {
        item.menu = this.menu;
        this.items.add(item.setParent(this));
        return this;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean flag) {
        this.isSelected = flag;
    }

    @Override
    public void mouseReleased(int x, int y, int button) {
        if (button == 0 && this.isMouseOver(x, y)) {
            setSelected(!this.isSelected);
            this.getMenu().onNodeSelected(this);
            if (this.getHierarchyPosition().isContainer())
                this.menu.onNodeOpened(this);
            action.accept(this.getGui(), this);
        }
    }

    public GuiHierarchyBaseMenu getMenu() {
        return (GuiHierarchyBaseMenu) this.getParent();
    }

    public void setMenu(GuiHierarchyBaseMenu guiHierarchyBaseMenu) {
        this.menu = guiHierarchyBaseMenu;
    }

    protected GuiHierarchyBaseItem setParent(GuiHierarchyBaseItem parent) {
        this.parent = parent;
        return this;
    }

    public String getTitle() {
        return title;
    }

    private void setTitle(@NotNull String title) {
        if (title.isEmpty())
            return;
        this.title = title;
    }

    @Override
    protected void renderContent(GuiRenderHelper helper, Style style, int x, int y) {
        GlStateManager.translate(-3, -3, 0);
        int color = ColorUtils.WHITE;

        GlStateManager.pushMatrix();

        GlStateManager.translate(10, 0, 0);
        helper.drawStringWithShadow(this.getTitle(), helper.getStringWidth(this.getTitle()), this.height, color);
        GlStateManager.popMatrix();

        if (this.getHierarchyPosition().isContainer()) {
            GlStateManager.pushMatrix();
            color = (color & 16579836) >> 2 | color & -16777216;
            GlStateManager.translate(4, 2, 0);
            if (this.isSelected) {
                GlStateManager.translate(6, 3, 0);
                GlStateManager.rotate(90, 0, 0, 1);
            }
            for (int f = 0; f < 4; f++) {
                helper.drawRect(f, f + 1, f + 1, f + 2, color);
                helper.drawRect(f, 7 - f, f + 1, 8 - f, color);
            }
            if (this.isSelected) {
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
