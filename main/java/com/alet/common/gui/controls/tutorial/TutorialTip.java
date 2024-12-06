package com.alet.common.gui.controls.tutorial;

import com.creativemd.creativecore.common.gui.GuiControl;

public class TutorialTip {
    
    public int tutBoxPosX;
    public int tutBoxPosY;
    public int x;
    public int y;
    public int offX;
    public int offY;
    public static int guiWidth;
    public static int guiHeight;
    public GuiControl control;
    public String tutorial;
    public String side;
    
    public TutorialTip(GuiControl control, String side, String tutorial) {
        this.control = control;
        this.tutorial = tutorial;
        this.side = side;
        this.x = 0;
        this.y = 0;
        this.offX = 0;
        this.offY = 0;
    }
    
    public TutorialTip(int x, int y, GuiControl control, String tutorial) {
        this.control = control;
        this.tutorial = tutorial;
        this.x = 0;
        this.y = 0;
        this.offX = x;
        this.offY = y;
    }
    
    public void setPos(int boxPosX, int boxPosY) {
        int posX = control.posX;
        int width = control.width;
        if ((guiWidth / 2) <= posX + width) {
            x = posX + width + 5 + offX;
        } else if ((guiWidth / 2) >= posX + width) {
            x = (posX) - (100 + 12) + offX;
        }
        if (side.equalsIgnoreCase("right"))
            this.tutBoxPosX = guiWidth - 109;
        else if (side.equalsIgnoreCase("left"))
            this.tutBoxPosX = -3;
        else if (side.equalsIgnoreCase("leftout"))
            this.tutBoxPosX = -110;
        y = offY;
    }
    
    public static void setGuiDimensions(int gWidth, int gHeight) {
        guiWidth = gWidth;
        guiHeight = gHeight;
    }
}
