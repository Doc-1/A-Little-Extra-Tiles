package com.docvin.alet.components.gui.controls.labeling;

import com.creativemd.creativecore.common.gui.controls.gui.GuiTextfield;
import com.docvin.alet.client.utils.CopyUtils;
import net.minecraft.client.gui.GuiScreen;

import java.awt.*;
import java.awt.datatransfer.Clipboard;

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
                if (clipboard == null)
                    return false;
                this.writeText(path);
            }
            return true;
        }
        return super.onKeyPressed(character, key);
    }

}
