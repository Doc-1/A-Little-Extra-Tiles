package com.alet.client.gui.controls;

import java.util.ArrayList;

import com.creativemd.creativecore.common.gui.controls.gui.GuiComboBox;
import com.creativemd.creativecore.common.gui.controls.gui.GuiComboBoxExtension;

public class GuiGlyphSelector extends GuiComboBox {
	
	public char character;
	public String fontr;
	
	public GuiGlyphSelector(String name, String font, int x, int y, int width) {
		super(name, x, y, width, new ArrayList<String>());
		caption = "Glyphs";
	}
	
	@Override
	protected GuiComboBoxExtension createBox() {
		return new GuiGlythSelectorExtension(name + "extension", posX, posY + height, (width - getContentOffset() * 2), 50, this, fontr);
	}
	
	@Override
	public boolean canOverlap() {
		return true;
	}
}
