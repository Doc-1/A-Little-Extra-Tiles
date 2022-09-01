package com.alet.client.gui.controls;

import java.util.HashMap;
import java.util.Random;

import com.creativemd.creativecore.common.gui.GuiControl;
import com.creativemd.creativecore.common.gui.GuiRenderHelper;
import com.creativemd.creativecore.common.gui.client.style.Style;
import com.creativemd.creativecore.common.gui.controls.container.client.GuiSlotControl;
import com.creativemd.creativecore.common.gui.controls.gui.GuiColorPicker;
import com.creativemd.creativecore.common.gui.controls.gui.GuiTextBox;
import com.creativemd.creativecore.common.utils.mc.ColorUtils;

public class GuiToolTipBox extends GuiTextBox {
	
	private HashMap<String, String> tips = new HashMap<String, String>();
	private GuiControl dControl;
	private boolean hidden = true;
	
	public GuiToolTipBox(String name) {
		super(name, "", 0, -3, 12, ColorUtils.WHITE);
		this.height = 15;
	}
	
	public GuiToolTipBox addAdditionalTips(String name, String context) {
		tips.put(name, context);
		return this;
	}
	
	public String getAdditionalTips(String name) {
		return (tips.containsKey(name)) ? tips.get(name) : "";
	}
	
	public String defaultTips(GuiControl control) {
		if (control instanceof GuiFontImage) {
			Random rand = new Random();
			int r = rand.nextInt(((10 - 1) + 1) + 1);
			switch (r) {
			case 1:
				return "I am Tips";
			case 2:
				return "There is a creeper behind you!";
			case 3:
				return "T\nI\nP\nS";
			case 4:
				return "Is this a reference or something?";
			case 5:
				return "Your build is coming along well!";
			case 6:
				return "Hello, [Player's Name].";
			case 7:
				return "You should try out Kiro's Basic Block mod.";
			case 8:
				return "Want to import 3D models? Check out LittleTiles 3D Importer by Timardo";
			case 9:
				return "Who am I?";
			case 10:
				return "Wake up.";
			default:
				return "Error: Message not found. Do Not report this.";
			}
		}
		if (control instanceof GuiToolTipBox) {
			return "Click on this to close Tool Tips. There will be a button with a question mark that you can click to open this back up.";
		}
		if (control instanceof GuiColorPicker) {
			return "Color Slider:\nSets the color value.\n\n1st Slider: Red\n2nd Slider: Green\n3rd Slider: Blue\n4th Slider: Alpha\n5th Slider: Tone" + "\n\nControls:\nRight Click to enter value a value\nClick and drag to adjust color\nMouse wheel adjusts color by 1.\nCTRL+Mouse wheel adjusts color by 10";
		}
		return "";
	}
	
	@Override
	protected void renderContent(GuiRenderHelper helper, Style style, int width, int height) {
		
		if (!hidden) {
			this.posX = -124;
			this.height = 200;
			this.width = 120;
			GuiControl control = null;
			for (GuiControl cont : this.getGui().controls) {
				if (cont.isMouseOver()) {
					control = cont;
					break;
				}
				
			}
			
			if (control != null && !(control instanceof GuiSlotControl)) {
				if (dControl == null || !dControl.getClass().isInstance(control))
					text = defaultTips(control) + getAdditionalTips(control.name);
				dControl = control;
			} else {
				text = getAdditionalTips("MainGui") + "\n\nHover over something to get an explanation of it.";
				dControl = null;
			}
			
		} else {
			this.posX = -16;
			this.height = 15;
			this.width = 12;
			text = "?";
		}
		int y = 0;
		for (String s : font.listFormattedStringToWidth(text, width)) {
			font.drawString(s, 0, y, color, true);
			y += font.FONT_HEIGHT;
		}
		//System.out.println(this.getGui().width);
	}
	
	@Override
	public boolean mousePressed(int x, int y, int button) {
		hidden = !hidden;
		dControl = null;
		return true;
	}
	
	@Override
	public boolean canOverlap() {
		return true;
	}
}
