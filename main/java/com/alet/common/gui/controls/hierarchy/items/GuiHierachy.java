package com.alet.common.gui.controls.hierarchy.items;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.creativemd.creativecore.common.gui.CoreControl;
import com.creativemd.creativecore.common.gui.GuiControl;
import com.creativemd.creativecore.common.gui.GuiRenderHelper;
import com.creativemd.creativecore.common.gui.Rect;
import com.creativemd.creativecore.common.gui.client.style.Style;
import com.creativemd.creativecore.common.gui.container.IControlParent;

import net.minecraft.client.renderer.GlStateManager;

public class GuiHierachy<T extends GuiHierarchyItem> extends GuiHierarchyItem implements IControlParent {
    
    private List<T> items;
    
    public GuiHierachy(String name, String title, int x, int y, int width, int height) {
        super(name, title, x, y, width, height);
        items = new ArrayList<>();
    }
    
    @Override
    protected void renderContent(GuiRenderHelper helper, Style style, int width, int height) {}
    
    protected int lastRenderedHeight = 0;
    
    public float getScaleFactor() {
        return 1F;
    }
    
    public double getOffsetY() {
        return 0;
    }
    
    public double getOffsetX() {
        return 0;
    }
    
    public boolean shouldRenderControl(GuiControl control, int index) {
        return true;
    }
    
    @Override
    protected void renderContent(GuiRenderHelper helper, Style style, int width, int height, Rect relativeMaximumRect) {
        GlStateManager.clear(GL11.GL_DEPTH_BUFFER_BIT);
        GlStateManager.disableDepth();
        
        float scale = getScaleFactor();
        double xOffset = getOffsetX();
        double yOffset = getOffsetY();
        
        Rect newRect = relativeMaximumRect.mergeRects(getRect());
        Rect scaledRect = relativeMaximumRect.mergeRects(getRect());
        lastRenderedHeight = 0;
        
        scaledRect.scale(1 / scale);
        
        for (int i = items.size() - 1; i >= 0; i--) {
            GuiControl control = items.get(i);
            
            if (!shouldRenderControl(control, i))
                continue;
            
            if (control.visible && (control.canOverlap() || control.isVisibleInsideRect((int) -xOffset, (int) -yOffset,
                width, height, scale))) {
                if (control.canOverlap())
                    GL11.glDisable(GL11.GL_STENCIL_TEST);
                else {
                    GL11.glEnable(GL11.GL_STENCIL_TEST);
                    prepareContentStencil(helper, newRect);
                    
                    GL11.glStencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_KEEP);
                    GL11.glStencilFunc(GL11.GL_EQUAL, 0x1, 0x1);
                }
                
                GlStateManager.pushMatrix();
                GlStateManager.scale(scale, scale, 1);
                GlStateManager.translate(xOffset, yOffset, 0);
                control.renderControl(helper, 1, control.canOverlap() ? getScreenRect() : scaledRect.getOffsetRect(
                    (int) -xOffset - control.posX - control.getContentOffset(), (int) -yOffset - control.posY - control
                            .getContentOffset()));
                GlStateManager.popMatrix();
                
                GL11.glDisable(GL11.GL_STENCIL_TEST);
            }
            
            lastRenderedHeight = (int) Math.max(lastRenderedHeight, (control.posY + control.height) * scale);
            
        }
        
        GL11.glEnable(GL11.GL_STENCIL_TEST);
        
        prepareContentStencil(helper, newRect);
        
        GL11.glStencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_KEEP);
        GL11.glStencilFunc(GL11.GL_EQUAL, 0x1, 0x1);
        
        renderContent(helper, style, width, height);
        
        GL11.glDisable(GL11.GL_STENCIL_TEST);
    }
    
    public void addItem(T item) {
        items.add(item);
    }
    
    public void insertItem(int index, T item) {
        items.add(index, item);
    }
    
    @Override
    public List getControls() {
        return this.items;
    }
    
    @Override
    public void refreshControls() {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public CoreControl get(String name) {
        for (int i = 0; i < items.size(); i++) {
            GuiControl control = items.get(i);
            if (control.is(name))
                return control;
            if (control instanceof IControlParent) {
                CoreControl tempcontrol = ((IControlParent) control).get(name);
                if (tempcontrol != null)
                    return tempcontrol;
            }
        }
        return null;
    }
    
    @Override
    public boolean has(String name) {
        // TODO Auto-generated method stub
        return false;
    }
}
