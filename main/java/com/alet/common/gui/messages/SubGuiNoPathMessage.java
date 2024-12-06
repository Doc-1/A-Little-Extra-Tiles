package com.alet.common.gui.messages;

import com.creativemd.creativecore.common.gui.container.SubGui;
import com.creativemd.creativecore.common.gui.controls.gui.GuiButton;
import com.creativemd.creativecore.common.gui.controls.gui.GuiTextBox;

public class SubGuiNoPathMessage extends SubGui {
	
	String fileExt;
	
	public SubGuiNoPathMessage(String fileExt) {
		super(200, 70);
		this.fileExt = fileExt;
	}
	
	@Override
	public void createControls() {
		controls.add(new GuiTextBox("text", "Either no path was entered or the path is incorrect. Make sure your path ends with " + fileExt, 0, 0, 194));
		
		controls.add(new GuiButton("Okay", 0, 50, 40) {
			@Override
			public void onClicked(int x, int y, int button) {
				closeGui();
			}
		});
	}
	
}
