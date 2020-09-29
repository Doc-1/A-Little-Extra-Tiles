package com.alet.littletiles.gui.controls;

import org.lwjgl.util.Color;

import com.alet.littletiles.common.utils.mc.ColorUtilsAlet;
import com.alet.littletiles.common.utils.mc.ColorUtilsAlet.ColorPart;
import com.creativemd.creativecore.common.gui.GuiRenderHelper;
import com.creativemd.creativecore.common.gui.client.style.Style;
import com.creativemd.creativecore.common.gui.controls.gui.GuiColorPicker;
import com.creativemd.creativecore.common.gui.controls.gui.GuiSteppedSlider;

public class GuiColoredSteppedSliderAlet extends GuiSteppedSlider {
	
	public GuiColorPickerAlet picker;
	public ColorPart part;
	
	public GuiColoredSteppedSliderAlet(String name, int x, int y, int width, int height, GuiColorPickerAlet guiColorPickerAlet, ColorPart alpha) {
		super(name, x, y, width, height, alpha.getColor(guiColorPickerAlet.color), 0, 255);
		this.picker = guiColorPickerAlet;
		this.part = alpha;
	}
	
	@Override
	public void setValue(double value) {
		super.setValue((int) value);
		if (part != null) {
			part.setColor(picker.color, (int) this.value);
			picker.onColorChanged();
		}
	}
	
	@Override
	protected void renderContent(GuiRenderHelper helper, Style style, int width, int height) {
		if (part == ColorPart.ALPHA) {
			Color startColor = new Color(picker.color);
			startColor.setAlpha(0);
			Color endColor = new Color(picker.color);
			endColor.setAlpha(255);
			helper.drawHorizontalGradientRect(0, 0, width, height, ColorUtilsAlet.RGBAToInt(startColor), ColorUtilsAlet.RGBAToInt(endColor));
		} else
			helper.drawHorizontalChannelMaskGradientRect(0, 0, width, height, ColorUtilsAlet.RGBAToInt(picker.color), part.getBrightest());
		
		super.renderContent(helper, style, width, height);
	}
}
