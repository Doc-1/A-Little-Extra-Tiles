package com.alet.common.structure.type.trigger;

import java.util.ArrayList;
import java.util.List;

import com.alet.common.structure.type.trigger.LittleTriggerBoxStructureALET.LittleTriggerBoxStructureParser;
import com.creativemd.creativecore.common.gui.GuiControl;
import com.creativemd.creativecore.common.gui.controls.gui.GuiButton;
import com.creativemd.creativecore.common.gui.controls.gui.GuiComboBox;
import com.creativemd.creativecore.common.gui.controls.gui.GuiPanel;
import com.creativemd.creativecore.common.gui.controls.gui.GuiScrollBox;
import com.creativemd.creativecore.common.utils.mc.ColorUtils;

public class GuiTriggerBoxAddButton extends GuiButton {
	LittleTriggerBoxStructureParser parser;
	
	public GuiTriggerBoxAddButton(LittleTriggerBoxStructureParser parser, String name, String caption, int x, int y, int width, int height) {
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
				if (parser.triggers.get(i).id.equals(this.name)) {
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
		GuiComboBox list = (GuiComboBox) this.getGui().get("list");
		box.controls = new ArrayList<GuiControl>();
		panel.controls = new ArrayList<GuiControl>();
		
		List<LittleTriggerEvent> tempTriggers = new ArrayList<LittleTriggerEvent>();
		for (int i = 0; i < parser.triggers.size(); i++) {
			LittleTriggerEvent tempTrigger = parser.triggers.get(i);
			tempTrigger.id = tempTrigger.name + i;
			tempTriggers.add(tempTrigger);
		}
		
		parser.triggers = tempTriggers;
		for (int i = 0; i < parser.triggers.size(); i++)
			box.addControl(new GuiTriggerBoxAddButton(parser, parser.triggers.get(i).name + i, parser.triggers.get(i).name, 0, i * 17, 119, 12));
		
	}
	
	public void updateControls(String name) {
		for (LittleTriggerEvent trig : parser.triggers)
			if (trig.id.equals(name)) {
				parser.trigger = trig;
				break;
			}
		if (parser.trigger != null)
			parser.trigger.updateControls(this.getGui());
		System.out.println(parser.trigger.id);
	}
	
	@Override
	public boolean hasBorder() {
		return false;
	}
	
}
