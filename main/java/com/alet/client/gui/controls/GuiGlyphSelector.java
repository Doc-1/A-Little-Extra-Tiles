package com.alet.client.gui.controls;

import java.awt.font.TextAttribute;
import java.util.ArrayList;
import java.util.Map;

import com.creativemd.creativecore.common.gui.controls.gui.GuiComboBox;
import com.creativemd.creativecore.common.gui.controls.gui.GuiComboBoxExtension;

public class GuiGlyphSelector extends GuiComboBox {
	
	public char character;
	public String fontr;
	public Map<TextAttribute, Object> textAttributeMap;
	
	public GuiGlyphSelector(String name, String font, Map<TextAttribute, Object> textAttributeMap, int x, int y, int width) {
		super(name, x, y, width, new ArrayList<String>());
		caption = "Glyphs";
		this.fontr = font;
		this.textAttributeMap = textAttributeMap;
	}
	
	@Override
	protected GuiComboBoxExtension createBox() {
		return new GuiGlythSelectorExtension(name + "extension", posX, posY
		        + height, (width - getContentOffset() * 2), 80, this, fontr, textAttributeMap);
	}
	
	@Override
	public boolean canOverlap() {
		return true;
	}
}
