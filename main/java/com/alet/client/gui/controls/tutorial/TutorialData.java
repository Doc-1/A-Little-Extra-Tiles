package com.alet.client.gui.controls.tutorial;

import com.creativemd.creativecore.common.gui.GuiControl;

public class TutorialData {
	
	public int tutBoxPosX;
	public int tutBoxPosY;
	public int x;
	public int y;
	public int modX;
	public int modY;
	public static int guiWidth;
	public static int guiHeight;
	public GuiControl control;
	public String tutorial;
	public String side;
	
	public TutorialData(GuiControl control, String side, String tutorial) {
		this.control = control;
		this.tutorial = tutorial;
		this.side = side;
		this.x = 0;
		this.y = 0;
		this.modX = 0;
		this.modY = 0;
	}
	
	public TutorialData(int x, int y, GuiControl control, String tutorial) {
		this.control = control;
		this.tutorial = tutorial;
		this.x = 0;
		this.y = 0;
		this.modX = x;
		this.modY = y;
	}
	
	public void setPos(int boxPosX, int boxPosY) {
		int posX = control.posX;
		int posY = control.posY;
		int width = control.width;
		int height = control.height;
		if ((guiWidth / 2) <= posX + width) {
			x = posX + width + 5 + modX;
		} else if ((guiWidth / 2) >= posX + width) {
			x = (posX) - (100 + 12) + modX;
		}
		if (side.equalsIgnoreCase("right"))
			this.tutBoxPosX = guiWidth - 109;
		else if (side.equalsIgnoreCase("left"))
			this.tutBoxPosX = -3;
		else if (side.equalsIgnoreCase("leftout"))
			this.tutBoxPosX = -110;
		y = modY;
	}
	
	public static void setGuiDimensions(int gWidth, int gHeight) {
		guiWidth = gWidth;
		guiHeight = gHeight;
	}
}
