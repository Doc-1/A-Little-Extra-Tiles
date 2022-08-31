package com.alet.client.gui.controls;

import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;

import com.alet.ALET;
import com.alet.font.FontReader;
import com.creativemd.creativecore.common.gui.GuiControl;
import com.creativemd.creativecore.common.gui.GuiRenderHelper;
import com.creativemd.creativecore.common.gui.client.style.Style;

import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;

public class GuiImage extends GuiControl {
    
    public Map<TextAttribute, Object> textAttributeMap;
    public DynamicTexture texture;
    public int scale = 1;
    
    public GuiImage(String name, int x, int y, String fontType, Map<TextAttribute, Object> textAttributeMap, int fontSize, int fontColor, double rotation) {
        super(name, x, y, 0, 0);
        this.marginWidth = 0;
        this.textAttributeMap = textAttributeMap;
        this.scale = 3;
        updateFont(fontType, fontSize, fontColor, rotation);
        
    }
    
    public GuiImage(String name, String text, int x, int y, String fontType, Map<TextAttribute, Object> textAttributeMap, int fontSize, int fontColor, double rotation) {
        super(name, x, y, 0, 0);
        this.marginWidth = 0;
        this.textAttributeMap = textAttributeMap;
        this.scale = 3;
        updateFont(fontType, text, fontSize, fontColor, rotation);
    }
    
    public GuiImage(String name, String path, int x, int y, int scale) {
        super(name, x, y, 0, 0);
        this.marginWidth = 0;
        
        try {
            BufferedImage image = TextureUtil.readBufferedImage(getClass().getClassLoader().getResourceAsStream(path));
            this.width = (image.getWidth() / scale);
            this.height = (image.getHeight() / scale);
            this.scale = scale;
            texture = new DynamicTexture(image);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
    
    public BufferedImage updateFont(String fontType, int fontSize, int fontColor, double rotation) {
        return updateFont(fontType, "Preview123", fontSize, fontColor, rotation);
    }
    
    public BufferedImage updateFont(String fontType, String text, int fontSize, int fontColor, double rotation) {
        if (fontType == null || fontType.equals(""))
            fontType = "Arial";
        if (fontSize == 0)
            fontSize = 48;
        BufferedImage image = FontReader.fontToPhoto(text, fontType, textAttributeMap, fontSize, fontColor, rotation);
        this.width = (image.getWidth() / this.scale);
        this.height = (image.getHeight() / this.scale);
        texture = new DynamicTexture(image);
        return image;
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
    
    public void drawTexturedModalRect(ResourceLocation location, int x, int y, int textureX, int textureY, int width, int height, int textureW, int textureH) {
        drawTexturedModalRect(x, y, textureX, textureY, width, height, textureW, textureH);
    }
    
    public void drawTexturedModalRect(int x, int y, int textureX, int textureY, int width, int height, int textureW, int textureH) {
        
    }
    
    @Override
    public void onClosed() {
        texture.deleteGlTexture();
    }
    
    @Override
    public boolean mousePressed(int x, int y, int button) {
        playSound(SoundEvents.UI_BUTTON_CLICK);
        onClicked(x, y, button);
        return true;
    }
    
    public void onClicked(int x, int y, int button) {
        
    }
    
    @Override
    public boolean hasBorder() {
        return false;
    }
    
    @Override
    public boolean hasBackground() {
        return false;
    }
}
