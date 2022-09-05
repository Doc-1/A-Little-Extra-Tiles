package com.alet.common.structure.type.trigger;

import java.util.ArrayList;

import com.alet.common.structure.type.trigger.LittleTriggerBoxStructureALET.LittleTriggerBoxStructureParser;
import com.creativemd.creativecore.common.gui.GuiControl;
import com.creativemd.creativecore.common.gui.controls.gui.GuiButton;
import com.creativemd.creativecore.common.gui.controls.gui.GuiPanel;
import com.creativemd.creativecore.common.gui.controls.gui.GuiScrollBox;
import com.creativemd.creativecore.common.utils.mc.ColorUtils;

import net.minecraft.util.text.translation.I18n;

public class GuiTriggerEventButton extends GuiButton {
    LittleTriggerBoxStructureParser parser;
    
    public GuiTriggerEventButton(LittleTriggerBoxStructureParser parser, String name, String caption, int x, int y, int width, int height) {
        super(name, caption, x, y, width, height);
        this.parser = parser;
    }
    
    @Override
    public void onClicked(int x, int y, int button) {
        GuiScrollBox box = (GuiScrollBox) this.getGui().get("box");
        if (button == 0) {
            for (GuiControl gui : box.controls) {
                GuiButton b = (GuiButton) gui;
                b.color = ColorUtils.WHITE;
            }
            this.color = ColorUtils.YELLOW;
            updateControls(this.name);
        } else if (button == 1) {
            
            int z = 0;
            for (int i = 0; i < parser.triggers.size(); i++) {
                LittleTriggerObject triggerObj = parser.triggers.get(i);
                String triggerName = triggerObj.getName() + triggerObj.id;
                
                if (triggerName.equals(this.name)) {
                    z = i;
                    break;
                }
            }
            parser.triggers.remove(z);
            updateList();
        }
        
    }
    
    public void updateList() {
        GuiScrollBox box = (GuiScrollBox) this.getGui().get("box");
        GuiPanel panel = (GuiPanel) this.getGui().get("content");
        box.controls = new ArrayList<GuiControl>();
        panel.controls = new ArrayList<GuiControl>();
        
        for (int i = 0; i < parser.triggers.size(); i++) {
            LittleTriggerObject triggerObj = parser.triggers.get(i);
            triggerObj.id = i;
            box.addControl(new GuiTriggerEventButton(parser, triggerObj.getName() + i, I18n.translateToLocal(triggerObj.getName()), 0, i * 17, 119, 12));
        }
    }
    
    public void updateControls(String name) {
        for (LittleTriggerObject trig : parser.triggers) {
            /*
            if (trig.id.equals(name)) {
                parser.trigger = trig;
                break;
            }*/}
        if (parser.trigger != null)
            parser.trigger.updateControls(this.getGui());
    }
    
    @Override
    public boolean hasBorder() {
        return false;
    }
    
}
