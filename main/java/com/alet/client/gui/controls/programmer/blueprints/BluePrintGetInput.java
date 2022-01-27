package com.alet.client.gui.controls.programmer.blueprints;

import java.util.ArrayList;
import java.util.List;

import com.alet.client.gui.controls.programmer.BluePrintNode;
import com.creativemd.creativecore.common.gui.controls.gui.GuiComboBox;
import com.creativemd.creativecore.common.gui.controls.gui.GuiComboBoxExtension;

public class BluePrintGetInput extends GuiBluePrint {
	
	private BluePrintNode<Boolean[]> input = new BluePrintNode<Boolean[]>("input", "Input", 1, BluePrintNode.RETURN_NODE);
	
	public BluePrintGetInput(String name, int x, int y) {
		super(name, "Get Input", x, y);
		this.addNode(input);
		input.setValue(new Boolean[] { false });
		this.height = 41;
	}
	
	@Override
	public void createControls() {
		List<String> list = new ArrayList<String>();
		list.add("i0");
		list.add("i1");
		list.add("i2");
		GuiComboBox box = new GuiComboBox("", 49, 24, 32, list) {
			@Override
			public boolean canOverlap() {
				return true;
			}
			
			@Override
			protected GuiComboBoxExtension createBox() {
				return new GuiComboBoxExtension(name + "extension", this, posX, posY + height, 50
				        - getContentOffset() * 2, 100, lines);
			}
		};
		box.height = 12;
		this.addControl(box);
	}
	
}
