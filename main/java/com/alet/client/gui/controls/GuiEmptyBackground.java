package com.alet.client.gui.controls;

import com.creativemd.creativecore.common.gui.GuiControl;
import com.creativemd.creativecore.common.gui.GuiRenderHelper;
import com.creativemd.creativecore.common.gui.client.style.ColoredDisplayStyle;
import com.creativemd.creativecore.common.gui.client.style.Style;

public class GuiEmptyBackground extends GuiControl {
    static Style background = new Style("background", new ColoredDisplayStyle(185, 185, 185, 0), new ColoredDisplayStyle(185, 185, 185), new ColoredDisplayStyle(185, 185, 185), new ColoredDisplayStyle(198, 198, 198), new ColoredDisplayStyle(0, 0, 0, 100));
    
    public GuiEmptyBackground(String name, int x, int y, int width, int height) {
        super(name, x, y, width, height);
        
    }
    
    @Override
    public Style getStyle() {
        return background;
    }
    
    @Override
    protected void renderContent(GuiRenderHelper helper, Style style, int width, int height) {
        
    }
    
}
