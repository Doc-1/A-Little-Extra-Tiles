package com.alet.client.gui.controls;

import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.util.Map;

import com.alet.ALET;
import com.alet.font.FontReader;
import com.creativemd.creativecore.common.gui.GuiControl;
import com.creativemd.creativecore.common.gui.GuiRenderHelper;
import com.creativemd.creativecore.common.gui.client.style.Style;

import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;

public class GuiImage extends GuiControl {
    
    public Map<TextAttribute, Object> textAttributeMap;
    public BufferedImage image;
    public DynamicTexture texture;
    
    public GuiImage(String name, int x, int y, String fontType, Map<TextAttribute, Object> textAttributeMap, int fontSize, int fontColor, double rotation) {
        super(name, x, y, 0, 0);
        this.marginWidth = 0;
        this.textAttributeMap = textAttributeMap;
        updateFont(fontType, fontSize, fontColor, rotation);
        texture = new DynamicTexture(image);
        
    }
    
    public GuiImage(String name, String text, int x, int y, String fontType, Map<TextAttribute, Object> textAttributeMap, int fontSize, int fontColor, double rotation) {
        super(name, x, y, 0, 0);
        this.marginWidth = 0;
        this.textAttributeMap = textAttributeMap;
        updateFont(fontType, text, fontSize, fontColor, rotation);
        texture = new DynamicTexture(image);
    }
    
    public GuiImage(String name, BufferedImage image, int x, int y) {
        super(name, x, y, 0, 0);
        this.marginWidth = 0;
        this.width = (image.getWidth() / 3);
        this.height = (image.getHeight() / 3);
        this.image = image;
        texture = new DynamicTexture(this.image);
    }
    
    public void updateFont(String fontType, int fontSize, int fontColor, double rotation) {
        updateFont(fontType, "Preview123", fontSize, fontColor, rotation);
    }
    
    public void updateFont(String fontType, String text, int fontSize, int fontColor, double rotation) {
        if (fontType == null || fontType.equals(""))
            fontType = "Arial";
        if (fontSize == 0)
            fontSize = 48;
        image = FontReader.fontToPhoto(text, fontType, textAttributeMap, fontSize, fontColor, rotation);
        this.width = (image.getWidth() / 3);
        this.height = (image.getHeight() / 3);
        texture = new DynamicTexture(image);
    }
    
    @Override
    protected void renderContent(GuiRenderHelper helper, Style style, int width, int height) {
        if (image != null) {
            ResourceLocation location = this.mc.getTextureManager().getDynamicTextureLocation(ALET.MODID, texture);
            
            this.mc.getTextureManager().bindTexture(location);
            int i = 3;
            //helper.drawTexturedModalRect(location, 0, 0, image.getWidth() / i, image.getHeight() / i, image.getWidth() / i, image.getHeight() / i);
            helper.drawTexturedModalRect(new ResourceLocation(ALET.MODID, "textures/textures/chain.png"), 0, 1 * 16, 0, 0, 16 * 16, 16 * 16);
            
            //  GuiIngame.drawModalRectWithCustomSizedTexture(0, 0, 0, 0, image.getWidth() / i, image.getHeight() / i, image.getWidth() / i, image.getHeight() / i);
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
