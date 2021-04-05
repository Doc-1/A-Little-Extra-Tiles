package com.alet.client.overlay.gui.controls;

import java.util.ArrayList;

import com.creativemd.creativecore.common.gui.GuiRenderHelper;
import com.creativemd.creativecore.common.gui.client.style.Style;
import com.creativemd.creativecore.common.gui.container.GuiParent;
import com.creativemd.creativecore.common.utils.mc.ColorUtils;

public class GuiOverlayTextList extends GuiOverlayControl {
	
	public ArrayList<String> text = new ArrayList<String>();
	public ArrayList<Integer> color = new ArrayList<Integer>();
	
	public GuiOverlayTextList(String name, int x, int y, int width, GuiParent parent) {
		super(name, x, y, width, font.FONT_HEIGHT);
		setStyle(Style.liteStyleNoHighlight);
		setParent(parent);
	}
	
	public void addText(String txt) {
		text.add(txt);
		color.add(ColorUtils.RGBAToInt(255, 255, 255, 0));
		updateHeight();
	}
	
	public void addText(String txt, int colour) {
		text.add(txt);
		color.add(colour);
		updateHeight();
	}
	
	private void updateHeight() {
		int y = 0;
		for (int i = 0; i < text.size(); i++) {
			y += font.FONT_HEIGHT;
		}
		height = y + getContentOffset() * 2;
	}
	
	@Override
	protected void renderContent(GuiRenderHelper helper, Style style, int width, int height) {
		int y = 0;
		for (int i = 0; i < text.size(); i++) {
			font.drawString(text.get(i), 0, y, color.get(i), true);
			y += font.FONT_HEIGHT;
		}
	}
	
}
