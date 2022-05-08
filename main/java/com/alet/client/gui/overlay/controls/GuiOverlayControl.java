package com.alet.client.gui.overlay.controls;

import com.creativemd.creativecore.common.gui.GuiControl;
import com.creativemd.creativecore.common.gui.GuiRenderHelper;
import com.creativemd.creativecore.common.gui.client.style.Style;
import com.creativemd.creativecore.common.gui.container.GuiParent;

public abstract class GuiOverlayControl extends GuiControl {
    
    private GuiParent parent;
    
    public GuiOverlayControl(String name, int x, int y, GuiParent par) {
        super(name, x, y, 0, 0);
        parent = par;
    }
    
    public GuiOverlayControl(String name, int x, int y, int width, int height) {
        super(name, x, y, width, height);
    }
    
    @Override
    protected abstract void renderContent(GuiRenderHelper helper, Style style, int width, int height);
    
    public void setParent(GuiParent par) {
        parent = par;
    }
    
    @Override
    public GuiParent getParent() {
        return parent;
    }
    
}
