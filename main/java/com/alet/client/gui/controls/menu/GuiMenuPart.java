package com.alet.client.gui.controls.menu;

import java.util.ArrayList;
import java.util.List;

import com.creativemd.creativecore.common.gui.GuiRenderHelper;
import com.creativemd.creativecore.common.gui.client.style.Style;
import com.creativemd.creativecore.common.gui.event.gui.GuiControlChangedEvent;
import com.creativemd.creativecore.common.utils.mc.ColorUtils;

import net.minecraft.client.renderer.GlStateManager;

public class GuiMenuPart extends GuiTreePart {
    
    public GuiMenuPart(GuiTreePart part) {
        super(part);
        
    }
    
    public GuiMenuPart(GuiTreePart part, EnumPartType type) {
        super(part, type);
    }
    
    public GuiMenuPart(String caption, EnumPartType type) {
        super(caption, type);
    }
    
    @Override
    protected void renderContent(GuiRenderHelper helper, Style style, int width, int height) {
        GlStateManager.pushMatrix();
        font.drawString(caption, 0, 0, ColorUtils.BLACK);
        if (this.type.canHold()) {
            font.drawString(">", this.width - 10, 0, ColorUtils.BLACK);
        }
        GlStateManager.popMatrix();
    }
    
    @Override
    public boolean mousePressed(int x, int y, int button) {
        System.out.println(isMouseOver());
        return super.mousePressed(x, y, button);
    }
    
    @Override
    public void onClicked(int x, int y, int mouseButton) {
        if (this.listOfParts != null && !this.listOfParts.isEmpty()) {
            if (this.type.isOpenable()) {
                closeBackMenus();
                if (!this.isOpened) {
                    this.openMenus();
                } else {
                    this.closeMenus();
                }
            }
        }
        
        tree.highlightPart(this);
        raiseEvent(new GuiControlChangedEvent(this));
    }
    
    public void closeBackMenus() {
        List<GuiTreePart> toClose = new ArrayList<GuiTreePart>();
        for (GuiTreePart branch : tree.listOfParts) {
            if (branch.type.canHold() && branch.isOpened)
                toClose.add(branch);
        }
        toClose.remove(this);
        for (GuiTreePart part : this.getListOfBranchesDown()) {
            toClose.remove(part);
        }
        for (GuiTreePart close : toClose) {
            close.closeMenus();
        }
    }
    
    @Override
    public void openMenus() {
        this.isOpened = true;
        for (int i = 0; i < this.listOfParts.size(); i++) {
            GuiTreePart button = this.listOfParts.get(i);
            if (!button.type.isOpenable())
                button.width = GuiRenderHelper.instance.getStringWidth(button.caption) + 5;
            else
                button.width = GuiRenderHelper.instance.getStringWidth(button.caption) + 15;
            if (!button.isRoot) {
                tree.addControl(button);
            }
        }
    }
    
    @Override
    public void closeMenus() {
        List controls = tree.getControls();
        this.isOpened = false;
        for (GuiTreePart button : this.listOfParts) {
            if (button.isBranch()) {
                button.closeMenus();
            }
            controls.remove(button);
        }
    }
    
    @Override
    public boolean isMouseOver(int posX, int posY) {
        return posX >= this.posX && posX < this.posX + this.width && posY >= this.posY && posY < this.posY + this.height;
    }
    
    @Override
    public boolean hasBorder() {
        return true;
    }
    
    @Override
    public Style getStyle() {
        return GuiTreePart.DISPLAY;
    }
}
