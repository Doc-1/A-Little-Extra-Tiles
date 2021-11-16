package com.alet.client.gui.controls;

import com.creativemd.creativecore.common.gui.GuiRenderHelper;
import com.creativemd.creativecore.common.gui.client.style.ColoredDisplayStyle;
import com.creativemd.creativecore.common.gui.client.style.Style;
import com.creativemd.creativecore.common.gui.controls.gui.GuiCheckBox;
import com.creativemd.creativecore.common.utils.mc.ColorUtils;

public class GuiDepressedCheckBox extends GuiCheckBox {
	
	public String textFormat;
	
	public GuiDepressedCheckBox(String name, String title, int x, int y, int width, int height, String textFormat, boolean value) {
		super(name, title, x, y, value);
		this.width = width;
		this.height = height;
		this.textFormat = textFormat;
	}
	
	@Override
	protected void renderContent(GuiRenderHelper helper, Style style, int width, int height) {
		int yoffset = 0;
		Style selectedStyle = new Style("selected", new ColoredDisplayStyle(0, 0, 0), new ColoredDisplayStyle(100, 100, 100), new ColoredDisplayStyle(90, 90, 90), new ColoredDisplayStyle(198, 198, 198), new ColoredDisplayStyle(0, 0, 0, 100));
		Style notSelectedStyle = new Style("selected", new ColoredDisplayStyle(50, 50, 50), new ColoredDisplayStyle(185, 185, 185), new ColoredDisplayStyle(100, 100, 100), new ColoredDisplayStyle(198, 198, 198), new ColoredDisplayStyle(0, 0, 0, 100));
		
		if (!enabled)
			style.getDisableEffect(this).renderStyle(0, yoffset, helper, checkBoxWidth, checkBoxWidth);
		
		if (value) {
			helper.drawRect(0 - 3, 0 - 3, 0 + 1 - 3, 0 + (this.height - 2) + 1 - 3, 0xff373737);
			helper.drawRect(0 - 3, 0 - 3, 0 + this.width - 3, 0 + 1 - 3, 0xff373737);
			helper.drawRect(0 + this.width - 1 - 3, 0 - 3, 0 + this.width - 3, 0 + 1 - 3, 0xff8b8b8b);
			helper.drawRect(0 + this.width - 1 - 3, 0 + 1 - 3, 0 + this.width - 3, 0 + (this.height - 2) + 1
			        - 3, 0xffffffff);
			helper.drawRect(0 + 1 - 3, 0 + (this.height - 1) - 1 - 3, 0 + this.width - 3, 0 + (this.height - 2) + 1
			        - 3, 0xffffffff);
			helper.drawRect(0 - 3, 0 + (this.height - 2) - 3, 0 + 1 - 3, 0 + (this.height - 2) + 1 - 3, 0xff8b8b8b);
			setStyle(selectedStyle);
			style.getBorder(this).renderStyle(-2, -2, helper, this.width - 2, this.height - 3);
			style.getBackground(this).renderStyle(-1, -1, helper, this.width - 4, this.height - 5);
		} else {
			setStyle(notSelectedStyle);
			style.getBorder(this).renderStyle(-3, -3, helper, this.width, this.height - 1);
			style.getBackground(this).renderStyle(-2, -2, helper, this.width - 2, this.height - 3);
		}
		
		helper.font.drawString(this.textFormat + title, (width / 2) - 2, (height / 2)
		        - 3, value ? ColorUtils.WHITE : ColorUtils.BLACK);
	}
	
	@Override
	public boolean isMouseOver(int posX, int posY) {
		return super.isMouseOver(posX, posY);
	}
}
