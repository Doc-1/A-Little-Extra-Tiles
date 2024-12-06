package com.alet.common.gui.message;

import com.creativemd.creativecore.common.gui.container.SubGui;
import com.creativemd.creativecore.common.gui.controls.gui.GuiButton;
import com.creativemd.creativecore.common.gui.controls.gui.GuiTextBox;

public class SubGuiNoTextInFieldMessage extends SubGui {
	
	String textFieldName;
	String valueType;
	
	public SubGuiNoTextInFieldMessage(String textFieldName, String valueType) {
		super(200, 70);
		this.textFieldName = textFieldName;
		this.valueType = valueType;
	}
	
	@Override
	public void createControls() {
		controls.add(new GuiTextBox("text", "The text field " + textFieldName + " is empty. Please enter a " + valueType, 0, 0, 194));
		
		controls.add(new GuiButton("Okay", 0, 50, 40) {
			@Override
			public void onClicked(int x, int y, int button) {
				closeGui();
			}
		});
	}
	
}
