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
    protected BiConsumer<SubGui, GuiControl> action;
    @Nullable
    protected GuiHierarchyBaseItem container;
    protected List<GuiHierarchyBaseItem> items = new ArrayList<>();
    boolean isSelected = false;
    boolean isOpened = false;
    private String title;
    private GuiHierarchyBaseMenu menu;

    protected GuiHierarchyBaseItem(String name, String title, int x, int y, int width, int height, @NotNull BiConsumer<SubGui, GuiControl> action) {
        super(name, x, y, width, height);
        this.action = action;
        this.setTitle(title);
    }

    protected GuiHierarchyBaseItem(String name, String title, int x, int y, int width, int height) {
        this(name, title, x, y, width, height, new BiConsumer<SubGui, GuiControl>(
        ) {
            @Override
            public void accept(SubGui subGui, GuiControl guiControl) {

            }
        });
    }

    public @NotNull BiConsumer<SubGui, GuiControl> getAction() {
        return action;
    }

    public void setAction(@NotNull BiConsumer<SubGui, GuiControl> action) {
        this.action = action;
    }

    public List<GuiHierarchyBaseItem> getItems() {
        return items;
    }

    public List<GuiHierarchyBaseItem> getNestedItems() {
        if (this.items != null)
            return getNestedItems(new ArrayList<>(), this.items);
        return new ArrayList<>();
    }

    private List<GuiHierarchyBaseItem> getNestedItems(ArrayList<GuiHierarchyBaseItem> nestedItems, List<GuiHierarchyBaseItem> items) {
        if (this.items != null) {
            nestedItems.addAll(items);
            for (GuiHierarchyBaseItem item : items) {
                this.getNestedItems(nestedItems, item.items);
            }
        }
        return nestedItems;
    }

    public HierarchyPosition getHierarchyPosition() {
        if (container != null) {
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

    public int getContainerCount() {
        return getContainerNesting().size();
    }

    private List<GuiHierarchyBaseItem> getContainerNesting() {
        if (this.container != null)
            return getContainerNesting(new ArrayList<>(), this.container);
        return new ArrayList<>();
    }

    private List<GuiHierarchyBaseItem> getContainerNesting(ArrayList<GuiHierarchyBaseItem> nestedContainers, GuiHierarchyBaseItem container) {
        if (container != null) {
            nestedContainers.add(container);
            getContainerNesting(nestedContainers, container.container);
        }
        return nestedContainers;
    }

    public GuiHierarchyBaseItem addItem(@NotNull GuiHierarchyBaseItem item) {
        item.menu = this.menu;
        this.items.add(item.setContainer(this));
        return this;
    }

    public boolean isOpened() {
        return this.isOpened;
    }

    public void setOpenedState(boolean opened) {
        isOpened = opened;
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
            if (this.isSelected())
                this.getMenu().onNodeSelected(this);
            if (this.getHierarchyPosition().isContainer())
                if (!this.isOpened) {
                    this.isOpened = true;
                    this.menu.onNodeOpened(this);
                } else {
                    this.isOpened = false;
                    this.menu.onNodeClosed(this);
                }
            action.accept(this.getGui(), this);
        }
    }

    public GuiHierarchyBaseMenu getMenu() {
        return (GuiHierarchyBaseMenu) this.getParent();
    }

    public void setMenu(GuiHierarchyBaseMenu guiHierarchyBaseMenu) {
        this.menu = guiHierarchyBaseMenu;
    }

    protected GuiHierarchyBaseItem setContainer(GuiHierarchyBaseItem container) {
        this.container = container;
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
    }
}
