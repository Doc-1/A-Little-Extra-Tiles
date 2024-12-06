package com.alet.common.gui.message;

import com.creativemd.creativecore.common.gui.container.SubGui;
import com.creativemd.creativecore.common.gui.controls.gui.GuiButton;
import com.creativemd.creativecore.common.gui.controls.gui.GuiTextBox;

public class SubGuiNoBluePrintMessage extends SubGui {
	
	public SubGuiNoBluePrintMessage() {
		super(100, 55);
	}
	
	@Override
	public void createControls() {
		controls.add(new GuiTextBox("text", "Please add a Blue Print to the top left slot.", 0, 0, 94));
		
		controls.add(new GuiButton("Okay", 0, 35, 40) {
			@Override
			public void onClicked(int x, int y, int button) {
				closeGui();
			}
		});
	}
	
}
