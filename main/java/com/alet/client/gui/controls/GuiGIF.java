package com.alet.client.gui.controls;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import com.alet.ALET;
import com.alet.client.gui.controls.thread.ThreadCollectFrames;
import com.creativemd.creativecore.common.gui.GuiControl;
import com.creativemd.creativecore.common.gui.GuiRenderHelper;
import com.creativemd.creativecore.common.gui.client.style.ColoredDisplayStyle;
import com.creativemd.creativecore.common.gui.client.style.Style;
import com.creativemd.creativecore.common.utils.mc.ColorUtils;

import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;

public class GuiGIF extends GuiControl {
    
    public String gif;
    ThreadCollectFrames collectFrames;
    public List<Integer> delay = new ArrayList<Integer>();
    public List<ResourceLocation> locations = new ArrayList<ResourceLocation>();
    public int tick = 0;
    public int collectTick = 0;
    public int count = 0;
    
    public GuiGIF(String name, String gif, int x, int y, double scale) {
        super(name, x, y, 0, 0);
        this.gif = gif;
        collectFrames = new ThreadCollectFrames(gif);
        
    }
    
    @Override
    public Style getStyle() {
        return new Style("", new ColoredDisplayStyle(ColorUtils.WHITE), new ColoredDisplayStyle(ColorUtils.WHITE), new ColoredDisplayStyle(ColorUtils.WHITE), new ColoredDisplayStyle(ColorUtils.WHITE), new ColoredDisplayStyle(ColorUtils.WHITE));
    }
    
    @Override
    public boolean hasBackground() {
        return true;
    }
    
    @Override
    public boolean hasBorder() {
        return false;
    }
    
    @Override
    protected void renderContent(GuiRenderHelper helper, Style style, int width, int height) {
        
        if (collectFrames != null && collectFrames.finished) {
            
            BufferedImage f = collectFrames.textures.get(collectTick);
            DynamicTexture frame = new DynamicTexture(f);
            ResourceLocation location = mc.getTextureManager().getDynamicTextureLocation(ALET.MODID, frame);
            locations.add(location);
            delay.add(collectFrames.delay.get(collectTick));
            collectTick++;
            
            this.width = f.getWidth() / 3;
            this.height = f.getHeight() / 3;
            
            if (collectTick >= collectFrames.textures.size())
                collectFrames = null;
        }
        if (!locations.isEmpty()) {
            if (count >= delay.size())
                count = 0;
            GlStateManager.pushMatrix();
            GlStateManager.translate(-1, -1, 0);
            GlStateManager.scale(0.9859, 0.98, 1);
            this.mc.getTextureManager().bindTexture(locations.get(count));
            GuiIngame.drawModalRectWithCustomSizedTexture(0, 0, 0, 0, this.width, this.height - 1, this.width, this.height - 1);
            GlStateManager.popMatrix();
            if (tick >= delay.get(count)) {
                tick = 0;
                count++;
            }
            
            tick++;
        }
        
    }
    
    @Override
    public void onClosed() {
        for (ResourceLocation frame : this.locations) {
            this.mc.getTextureManager().deleteTexture(frame);
        }
        super.onClosed();
    }
}
