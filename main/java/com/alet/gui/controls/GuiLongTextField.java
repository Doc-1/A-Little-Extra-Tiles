package com.alet.gui.controls;

import com.creativemd.creativecore.common.gui.controls.gui.GuiTextfield;

public class GuiLongTextField extends GuiTextfield{

	public GuiLongTextField(String name, String text, int x, int y, int width, int height) {
		super(name, text, x, y, width, height);
		this.maxLength = 2048;
	}

}
