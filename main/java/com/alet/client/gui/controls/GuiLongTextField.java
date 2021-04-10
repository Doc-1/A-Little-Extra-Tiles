package com.alet.client.gui.controls;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;

import com.alet.common.util.CopyUtils;
import com.creativemd.creativecore.common.gui.controls.gui.GuiTextfield;

import net.minecraft.client.gui.GuiScreen;

public class GuiLongTextField extends GuiTextfield {
	
	public GuiLongTextField(String name, String text, int x, int y, int width, int height) {
		super(name, text, x, y, width, height);
		this.maxLength = 2048;
	}
	
	@Override
	public boolean onKeyPressed(char character, int key) {
		if (!focused)
			return false;
		if (GuiScreen.isKeyComboCtrlV(key)) {
			if (this.enabled) {
				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				String path = CopyUtils.getCopiedFilePath(clipboard);
				this.writeText(path);
			}
			return true;
		}
		return super.onKeyPressed(character, key);
	}
	
}
