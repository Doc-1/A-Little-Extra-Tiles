package com.alet.client.gui.controls.tutorial;

import javax.annotation.Nullable;

import com.creativemd.creativecore.common.gui.GuiControl;
import com.creativemd.creativecore.common.gui.GuiRenderHelper;
import com.creativemd.creativecore.common.gui.client.style.Style;
import com.creativemd.creativecore.common.utils.mc.ColorUtils;

public class GuiControlHighlighter extends GuiControl {
	
	public int boxPosX;
	public int boxPosY;
	public GuiControl control;
	private boolean empty = true;
	
	public GuiControlHighlighter(String name, @Nullable GuiControl control, int boxPosX, int boxPosY) {
		super(name, 0, 0, 0, 0);
		this.boxPosX = boxPosX;
		this.boxPosY = boxPosY;
		if (control != null) {
			this.posX = control.posX - 9 - boxPosX;
			this.posY = control.posY - 9 - boxPosY;
			this.width = control.width + 7;
			this.height = control.height + 1 + 7;
			this.control = control;
			empty = false;
		}
		setStyle(Style.emptyStyle);
	}
	
	@Override
	protected void renderContent(GuiRenderHelper helper, Style style, int width, int height) {
		if (!empty) {
			helper.drawRect(0, 0, 0 + 1, 0 + (this.height - 2) + 1, ColorUtils.RED);
			helper.drawRect(0, 0, 0 + this.width, 0 + 1, ColorUtils.RED);
			helper.drawRect(0 + this.width - 1, 0 + 1, 0 + this.width, 0 + (this.height - 2) + 1, ColorUtils.RED);
			helper.drawRect(1, (this.height - 2), 0 + this.width, 0 + (this.height - 2) + 1, ColorUtils.RED);
		}
	}
	
	public void setControl(GuiControl control) {
		if (control != null) {
			this.posX = control.posX - 9 - this.boxPosX;
			this.posY = control.posY - 9 - this.boxPosY;
			this.width = control.width + 7;
			this.height = control.height + 1 + 7;
			this.control = control;
			empty = false;
		}
	}
	
	@Override
	public boolean canOverlap() {
		return true;
	}
	
}
