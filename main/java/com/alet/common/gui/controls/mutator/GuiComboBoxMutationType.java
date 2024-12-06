package com.alet.common.gui.controls.mutator;

import java.util.ArrayList;

import com.creativemd.creativecore.common.gui.controls.gui.GuiComboBox;

public class GuiComboBoxMutationType extends GuiComboBox {
	
	public GuiComboBoxMutationType(String name, int x, int y, int width) {
		super(name, x, y, width, new ArrayList<String>());
		this.lines.add("Material");
		this.lines.add("Collision");
		this.select(0);
	}
	
}
