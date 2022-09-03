package com.alet.common.structure.type.trigger;

import com.alet.common.structure.type.trigger.LittleTriggerBoxStructureALET.LittleTriggerBoxStructureParser;
import com.creativemd.creativecore.common.gui.controls.gui.GuiButton;
import com.creativemd.creativecore.common.gui.controls.gui.GuiComboBox;
import com.creativemd.creativecore.common.gui.controls.gui.GuiScrollBox;

public class GuiTriggerBoxAddButton extends GuiButton {
    
    LittleTriggerBoxStructureParser parser;
    
    public GuiTriggerBoxAddButton(LittleTriggerBoxStructureParser parser, String caption, int x, int y, int width) {
        super(caption, x, y, width);
        this.parser = parser;
    }
    
    @Override
    public void onClicked(int x, int y, int button) {
        GuiScrollBox box = (GuiScrollBox) this.getGui().get("box");
        GuiComboBox list = (GuiComboBox) this.getGui().get("list");
        int i = box.controls.size();
        parser.triggers.add(LittleTriggerRegistrar.getLittleTrigger(list.getCaption(), list.getCaption() + i));
        GuiTriggerEventButton bu = new GuiTriggerEventButton(parser, list.getCaption() + i, list.getCaption(), 0, i * 17, 119, 12);
        
        box.addControl(bu);
    }
    
}
