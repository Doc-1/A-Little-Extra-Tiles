package com.alet.common.gui.messages;

import com.creativemd.creativecore.common.gui.container.SubGui;
import com.creativemd.creativecore.common.gui.controls.gui.GuiButton;
import com.creativemd.creativecore.common.gui.controls.gui.GuiTextBox;

public class SubGuiTooManyChannels extends SubGui {
    
    String fileName;
    
    public SubGuiTooManyChannels(String fileName) {
        super(200, 70);
        this.fileName = fileName;
    }
    
    @Override
    public void createControls() {
        controls.add(
            new GuiTextBox("text", "The midi file " + fileName + " takes more than 18 channels to play all notes. It can only import a max of 18.", 0, 0, 194));
        
        controls.add(new GuiButton("Okay", 0, 50, 40) {
            @Override
            public void onClicked(int x, int y, int button) {
                closeGui();
            }
        });
    }
    
}
