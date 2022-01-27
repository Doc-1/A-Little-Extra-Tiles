package com.alet.client.gui.controls.programmer;

import com.creativemd.creativecore.common.gui.GuiControl;
import com.creativemd.creativecore.common.gui.GuiRenderHelper;
import com.creativemd.creativecore.common.gui.client.style.Style;
import com.creativemd.creativecore.common.gui.event.gui.GuiControlClickEvent;

public class GuiDragBluePrint extends GuiControl {
	
	public GuiDragBluePrint(String name, int x, int y) {
		super(name, x, y, 50, 10);
		
	}
	
	@Override
	protected void renderContent(GuiRenderHelper helper, Style style, int width, int height) {
		
	}
	
	@Override
	public boolean mousePressed(int x, int y, int button) {
		if (this.isMouseOver())
			this.raiseEvent(new GuiControlClickEvent(this, x, y, button));
		return false;
	}
	
}
