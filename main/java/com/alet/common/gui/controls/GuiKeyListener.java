package com.alet.common.gui.controls;

import org.lwjgl.input.Keyboard;

import com.alet.common.gui.event.GuiControlKeyPressed;
import com.creativemd.creativecore.common.gui.GuiControl;
import com.creativemd.creativecore.common.gui.GuiRenderHelper;
import com.creativemd.creativecore.common.gui.client.style.Style;

import net.minecraft.client.gui.GuiScreen;

public class GuiKeyListener extends GuiControl {
    
    public GuiKeyListener(String name) {
        super(name, 0, 0, 0, 0);
        // TODO Auto-generated constructor stub
    }
    
    @Override
    protected void renderContent(GuiRenderHelper helper, Style style, int width, int height) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public boolean hasBackground() {
        return false;
    }
    
    @Override
    public boolean hasBorder() {
        return false;
    }
    
    @Override
    public boolean onKeyPressed(char character, int key) {
        if (key != Keyboard.KEY_LCONTROL && key != Keyboard.KEY_RCONTROL && key != Keyboard.KEY_LMENU && key != Keyboard.KEY_RMENU)
            this.raiseEvent(new GuiControlKeyPressed(this, GuiScreen.isCtrlKeyDown(), GuiScreen.isAltKeyDown(), key));
        return false;
    }
}
