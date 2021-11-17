package com.alet.client.gui.controls;

import org.lwjgl.util.Color;

import com.creativemd.creativecore.common.gui.client.style.ColoredDisplayStyle;
import com.creativemd.creativecore.common.gui.client.style.Style;
import com.creativemd.creativecore.common.gui.container.GuiParent;
import com.creativemd.creativecore.common.utils.mc.ColorUtils;

public class GuiColorablePanel extends GuiParent {
	
	public static Style panelStyle;
	public Color borderColor;
	public Color backColor;
	
	public GuiColorablePanel(String name, int x, int y, int width, int height, int borderRed, int borderGreen, int borderBlue, int backRed, int backGreen, int backBlue) {
		super(name, x, y, width, height);
		panelStyle = new Style("panel", new ColoredDisplayStyle(borderRed, borderGreen, borderBlue), new ColoredDisplayStyle(backRed, backGreen, backBlue), new ColoredDisplayStyle(0, 0, 0), new ColoredDisplayStyle(0, 0, 0), new ColoredDisplayStyle(0, 0, 0));
		setStyle(panelStyle);
		this.borderColor = new Color(borderRed, borderGreen, borderBlue);
		this.backColor = new Color(backRed, backGreen, backBlue);
	}
	
	public GuiColorablePanel(String name, int x, int y, int width, int height, Color border, Color background) {
		super(name, x, y, width, height);
		panelStyle = new Style("panel", new ColoredDisplayStyle(border.getRed(), border.getGreen(), border.getBlue()), new ColoredDisplayStyle(background.getRed(), background.getGreen(), background.getBlue()), new ColoredDisplayStyle(0, 0, 0), new ColoredDisplayStyle(0, 0, 0), new ColoredDisplayStyle(0, 0, 0));
		setStyle(panelStyle);
		this.borderColor = border;
		this.backColor = background;
	}
	
	/** String name, DisplayStyle border, DisplayStyle background, DisplayStyle mouseOverBackground, DisplayStyle face, DisplayStyle disableEffect) */
	public void setBackgroundColor(int color) {
		Color colour = ColorUtils.IntToRGBA(color);
		int r = colour.getRed();
		int g = colour.getGreen();
		int b = colour.getBlue();
		this.backColor = new Color(r, g, b);
		panelStyle = new Style("panel", new ColoredDisplayStyle(borderColor.getRed(), borderColor.getGreen(), borderColor.getBlue()), new ColoredDisplayStyle(r, g, b), new ColoredDisplayStyle(r, g, b), new ColoredDisplayStyle(0, 0, 0, 0), new ColoredDisplayStyle(0, 0, 0, 0));
		setStyle(panelStyle);
	}
	
	public void setBorderColor(int color) {
		Color colour = ColorUtils.IntToRGBA(color);
		int r = colour.getRed();
		int g = colour.getGreen();
		int b = colour.getBlue();
		this.borderColor = new Color(r, g, b);
		panelStyle = new Style("panel", new ColoredDisplayStyle(r, g, b), new ColoredDisplayStyle(backColor.getRed(), backColor.getGreen(), backColor.getBlue()), new ColoredDisplayStyle(r, g, b), new ColoredDisplayStyle(0, 0, 0, 0), new ColoredDisplayStyle(0, 0, 0, 0));
		setStyle(panelStyle);
	}
	
	@Override
	public boolean hasMouseOverEffect() {
		return false;
	}
}
