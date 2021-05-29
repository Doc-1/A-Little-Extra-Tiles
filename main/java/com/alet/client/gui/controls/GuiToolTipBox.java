package com.alet.client.gui.controls;

import java.util.HashMap;

import com.creativemd.creativecore.common.gui.GuiControl;
import com.creativemd.creativecore.common.gui.GuiRenderHelper;
import com.creativemd.creativecore.common.gui.client.style.Style;
import com.creativemd.creativecore.common.gui.controls.container.client.GuiSlotControl;
import com.creativemd.creativecore.common.gui.controls.gui.GuiColorPicker;
import com.creativemd.creativecore.common.gui.controls.gui.GuiTextBox;
import com.creativemd.creativecore.common.utils.mc.ColorUtils;

public class GuiToolTipBox extends GuiTextBox {
	
	private HashMap<String, String> tips = new HashMap<String, String>();
	
	public GuiToolTipBox(String name, int width, int height) {
		super(name, "", -130, -3, width, ColorUtils.WHITE);
		this.height = height;
	}
	
	public GuiToolTipBox addAdditionalTips(String name, String context) {
		tips.put(name, context);
		return this;
	}
	
	public String getAdditionalTips(String name) {
		return (tips.containsKey(name)) ? tips.get(name) : "";
	}
	
	public String defaultTips(GuiControl control) {
		if (control instanceof GuiColorPicker) {
			return "Color Slider:\nSets the color value.\n\n1st Slider: Red\n2nd Slider: Green\n3rd Slider: Blue\n4th Slider: Alpha\n5th Slider: Tone" + "\n\nControls:\nClick on arrows to adjust color\nClick and drag on a slider to adjust color\nMouse wheel adjusts color by 1.\nCTRL+Mouse wheel adjusts color by 10";
		}
		return "";
	}
	
	@Override
	protected void renderContent(GuiRenderHelper helper, Style style, int width, int height) {
		GuiControl control = null;
		for (GuiControl cont : this.getGui().controls) {
			control = cont;
			if (cont.isMouseOver())
				break;
		}
		
		if (control != null && !(control instanceof GuiSlotControl)) {
			text = defaultTips(control) + "\n" + getAdditionalTips(control.name);
		} else {
			text = "";
		}
		
		int y = 0;
		for (String s : font.listFormattedStringToWidth(text, width)) {
			font.drawString(s, 0, y, color, true);
			y += font.FONT_HEIGHT;
		}
		//System.out.println(this.getGui().width);
	}
	
	@Override
	public boolean canOverlap() {
		return true;
	}
}
