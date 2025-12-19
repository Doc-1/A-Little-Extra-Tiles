package com.docvin.alet.components.gui.controls.labeling;

import com.creativemd.creativecore.common.gui.GuiControl;
import com.creativemd.creativecore.common.gui.GuiRenderHelper;
import com.creativemd.creativecore.common.gui.client.style.Style;
import com.docvin.alet.Tags;
import com.docvin.alet.components.gui.controls.drawing.photo.FontReader;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;

import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.util.Map;

public class GuiCustomFontLabel extends GuiControl {

    private final int fontSize;
    private final int fontColor;
    private final String fontType;
    private final double rotation;
    public Map<TextAttribute, Object> textAttributeMap;
    public DynamicTexture texture;
    public int scale;
    private String text;

    public GuiCustomFontLabel(String name, String text, int x, int y, String fontType, Map<TextAttribute, Object> textAttributeMap, int fontSize, int fontColor, double rotation) {
        super(name, x, y, 0, 0);
        this.marginWidth = 0;
        this.textAttributeMap = textAttributeMap;
        this.scale = 3;
        this.text = text;
        this.fontType = !fontType.isEmpty() ? fontType : "Arial";
        this.fontSize = fontSize;
        this.fontColor = fontColor;
        this.rotation = rotation;
        updateFont();
    }

    public void setText(String text) {
        this.text = text;
        this.updateFont();
    }

    private void updateFont() {
        BufferedImage image = FontReader.fontToPhoto(text, fontType, textAttributeMap, fontSize, fontColor, rotation);
        this.width = (image.getWidth() / this.scale);
        this.height = (image.getHeight() / this.scale);
        texture = new DynamicTexture(image);
    }

    @Override
    protected void renderContent(GuiRenderHelper helper, Style style, int width, int height) {
        if (texture != null) {
            ResourceLocation location = mc.getTextureManager().getDynamicTextureLocation(Tags.MOD_ID, texture);

            mc.getTextureManager().bindTexture(location);
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
            GlStateManager.scale(0.5, 0.5, 0);
            GuiIngame.drawModalRectWithCustomSizedTexture(0, 0, 0, 0, this.width, this.height, this.width, this.height);
            GlStateManager.popMatrix();
        }
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
