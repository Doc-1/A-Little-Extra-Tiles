package com.alet.client.gui.controls;

import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import com.creativemd.creativecore.common.gui.controls.gui.GuiComboBoxExtension;
import com.creativemd.creativecore.common.utils.mc.ColorUtils;

public class GuiGlythSelectorExtension extends GuiComboBoxExtension {
	
	public String fontr;
	
	public GuiGlythSelectorExtension(String name, int x, int y, int width, int height, GuiGlyphSelector fontSelector, String font) {
		super(name, fontSelector, x, y, width, height, new ArrayList<String>());
		this.fontr = font;
		reloadControls();
	}
	
	public List<Character> collectGlyphs() {
		Font f = new Font(fontr, Font.PLAIN, 48);
		List<Character> glyphs = new ArrayList<Character>();
		for (int c = 0x0000; c <= Character.MAX_VALUE; c++) {
			
			if (f.canDisplay(Character.toChars(c)[0]) && Character.isValidCodePoint(c))
				glyphs.add(Character.toChars(c)[0]);
		}
		
		return glyphs;
	}
	
	@Override
	public void reloadControls() {
		
		if (fontr != null) {
			List<Character> glyphs = collectGlyphs();
			int rowMax = glyphs.size() / 5;
			int counter = 0;
			for (int r = 0; r <= rowMax; r++) {
				for (int c = 0; c <= 5; c++) {
					if (counter >= glyphs.size())
						break;
					
					controls.add(new GuiImage(((int) glyphs.get(counter)) + "", glyphs.get(counter) + "", (c * 13) + 2, (r * 13) + 2, fontr, 32, ColorUtils.BLACK, 0) {
						@Override
						public void onClicked(int x, int y, int button) {
							GuiLongTextField text = (GuiLongTextField) this.getParent().getParent().get("input");
							if (text != null) {
								int codePoint = Integer.parseInt(name);
								System.out.println((char) codePoint);
								text.text += (char) codePoint;
							}
						}
					});
					counter++;
				}
			}
		}
	}
}
