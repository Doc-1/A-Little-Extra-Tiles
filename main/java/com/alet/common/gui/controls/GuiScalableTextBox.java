package com.alet.common.gui.controls;

import com.creativemd.creativecore.common.gui.GuiRenderHelper;
import com.creativemd.creativecore.common.gui.client.style.Style;
import com.creativemd.creativecore.common.gui.controls.gui.GuiTextBox;

import net.minecraft.client.renderer.GlStateManager;

public class GuiScalableTextBox extends GuiTextBox {
	public double scale = 1;
	
	public GuiScalableTextBox(String name, String text, int x, int y, int width, double scale) {
		super(name, text, x, y, width);
		this.scale = scale;
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
		int y = 0;
		GlStateManager.pushMatrix();
		GlStateManager.scale(scale, scale, 0);
		for (String s : font.listFormattedStringToWidth(text, width * (int) scale)) {
			font.drawString(s, 0, y, color, true);
			y += (font.FONT_HEIGHT * scale) + 2;
		}
		GlStateManager.popMatrix();
		this.height = y + getContentOffset() * 2;
	}
}
