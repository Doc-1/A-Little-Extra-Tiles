package com.docvin.alet.components.gui.controls.drawing;

import com.creativemd.creativecore.common.gui.GuiControl;
import com.creativemd.creativecore.common.gui.GuiRenderHelper;
import com.creativemd.creativecore.common.gui.client.style.Style;
import com.docvin.alet.Tags;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.util.ResourceLocation;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class GuiImage extends GuiControl {
    public DynamicTexture texture;
    public double scale;

    public GuiImage(String name, BufferedImage image, int x, int y, double scale) {
        super(name, x, y, 0, 0);
        this.marginWidth = 0;
        this.width = (int) (image.getWidth() * scale);
        this.height = (int) (image.getHeight() * scale);
        this.scale = scale;
        texture = new DynamicTexture(image);

    }

    public GuiImage(String name, String path, int x, int y, double scale) {
        super(name, x, y, 0, 0);
        this.marginWidth = 0;
        try {
            BufferedImage image = TextureUtil.readBufferedImage(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(path)));
            this.width = (int) (image.getWidth() * scale);
            this.height = (int) (image.getHeight() * scale);
            this.scale = scale;
            texture = new DynamicTexture(image);
        } catch (IOException e) {
            throw new NullPointerException("No image found at " + path);
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
            ResourceLocation location = mc.getTextureManager().getDynamicTextureLocation(Tags.MOD_ID, texture);

            mc.getTextureManager().bindTexture(location);

            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
                    GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
                    GlStateManager.DestFactor.ZERO);
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
