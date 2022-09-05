package com.alet.common.structure.type.trigger;

import com.alet.common.structure.type.trigger.LittleTriggerBoxStructureALET.LittleTriggerBoxStructureParser;
import com.creativemd.creativecore.common.gui.controls.gui.GuiButton;
import com.creativemd.creativecore.common.gui.controls.gui.GuiComboBoxCategory;
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
        GuiComboBoxCategory<Class<? extends LittleTriggerObject>> list = (GuiComboBoxCategory<Class<? extends LittleTriggerObject>>) this.getGui().get("list");
        int i = box.controls.size();
        parser.triggers.add(LittleTriggerRegistrar.getTriggerObject(list.getSelected().value, i));
        
        GuiTriggerEventButton bu = new GuiTriggerEventButton(parser, list.getSelected().key + i, list.getCaption(), 0, i * 17, 119, 12);
        
        box.addControl(bu);
    }
    
}
