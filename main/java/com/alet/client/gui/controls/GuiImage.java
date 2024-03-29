package com.alet.client.gui.controls;

import java.awt.image.BufferedImage;
import java.io.IOException;

import com.alet.ALET;
import com.creativemd.creativecore.common.gui.GuiControl;
import com.creativemd.creativecore.common.gui.GuiRenderHelper;
import com.creativemd.creativecore.common.gui.client.style.Style;

import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.util.ResourceLocation;

public class GuiImage extends GuiControl {
    public DynamicTexture texture;
    public double scale;
    
    public GuiImage(String name, String path, int x, int y, double scale) {
        super(name, x, y, 0, 0);
        this.marginWidth = 0;
        try {
            BufferedImage image = TextureUtil.readBufferedImage(getClass().getClassLoader().getResourceAsStream(path));
            this.width = (int) (image.getWidth() * scale);
            this.height = (int) (image.getHeight() * scale);
            this.scale = scale;
            texture = new DynamicTexture(image);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
    
    @Override
    public boolean hasBorder() {
        return false;
    }
    
    @Override
    public boolean hasBackground() {
        return false;
    }
    
    @Override
    protected void renderContent(GuiRenderHelper helper, Style style, int width, int height) {
        if (texture != null) {
            ResourceLocation location = this.mc.getTextureManager().getDynamicTextureLocation(ALET.MODID, texture);
            
            this.mc.getTextureManager().bindTexture(location);
            int i = 3;
            //helper.drawTexturedModalRect(location, 0, 0, image.getWidth() / i, image.getHeight() / i, image.getWidth() / i, image.getHeight() / i);
            // helper.drawTexturedModalRect(new ResourceLocation(LittleForge.MODID, "textures/gui/arrow.png"), 187 * 16, 1 * 16, 0, 0, 16 * 16, 16 * 16);
            //TextureStretchDisplayStyle s = new TextureStretchDisplayStyle(location, 0, 50, image.getWidth() / 3, image.getHeight() / 3);
            //TextureDisplayStyle s = new TextureDisplayStyle(location, image.getWidth() / i, image.getHeight() / i);
            // s.renderStyle(helper, width, height);
            
            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager
                    .tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            GlStateManager.enableAlpha();
            // GlStateManager.disableLighting();
            GlStateManager.enableTexture2D();
            GuiIngame.drawModalRectWithCustomSizedTexture(0, 0, 0, 0, this.width, this.height, this.width, this.height);
            GlStateManager.popMatrix();
        }
    }
    
    @Override
    public void onClosed() {
        texture.deleteGlTexture();
    }
    
}
