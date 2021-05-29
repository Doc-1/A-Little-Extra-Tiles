package com.alet.client.gui.controls;

import java.awt.image.BufferedImage;

import com.alet.ALET;
import com.alet.font.FontReader;
import com.creativemd.creativecore.common.gui.GuiControl;
import com.creativemd.creativecore.common.gui.GuiRenderHelper;
import com.creativemd.creativecore.common.gui.client.style.Style;

import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;

public class GuiImage extends GuiControl {
	
	public BufferedImage image;
	public DynamicTexture texture;
	
	public GuiImage(String name, int x, int y) {
		super(name, x, y, 0, 0);
		
		this.marginWidth = 0;
	}
	
	public void updateFont(String fontType, int fontSize, int fontColor, double rotation) {
		if (fontType == null || fontType.equals(""))
			fontType = "Arial";
		if (fontSize == 0)
			fontSize = 48;
		
		image = FontReader.fontToPhoto("Preview123", fontType, fontSize, fontColor, rotation);
	}
	
	@Override
	protected void renderContent(GuiRenderHelper helper, Style style, int width, int height) {
		//this.mc.getTextureManager().getDynamicTextureLocation(ALET.MODID, texture)
		if (image != null) {
			//GlStateManager.scale(0.0F, 1.0F, 0.0F);
			texture = new DynamicTexture(image);
			ResourceLocation location = this.mc.getTextureManager().getDynamicTextureLocation(ALET.MODID, texture);
			//TextureStretchDisplayStyle backgroundPlate = new TextureStretchDisplayStyle(this.mc.getTextureManager().getDynamicTextureLocation(ALET.MODID, texture), 200, 200, image.getWidth(), image.getHeight());
			//backgroundPlate.renderStyle(helper, width, height);
			helper.drawTexturedModalRect(location, 0, 0, 0, 0, image.getWidth(), image.getHeight(), image.getWidth(), image.getHeight());
			texture.deleteGlTexture();
		}
	}
	
	@Override
	public boolean canOverlap() {
		return true;
	}
	
	@Override
	public boolean hasBorder() {
		return true;
	}
	
	@Override
	public boolean hasBackground() {
		return false;
	}
}
