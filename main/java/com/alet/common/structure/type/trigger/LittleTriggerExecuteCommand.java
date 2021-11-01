package com.alet.common.structure.type.trigger;

import com.alet.client.gui.controls.GuiWrappedTextField;
import com.creativemd.creativecore.common.gui.CoreControl;
import com.creativemd.creativecore.common.gui.container.GuiParent;
import com.creativemd.creativecore.common.gui.controls.gui.GuiPanel;
import com.creativemd.creativecore.common.gui.controls.gui.GuiTextBox;

import net.minecraft.nbt.NBTTagCompound;

public class LittleTriggerExecuteCommand extends LittleTriggerEvent {
	
	String command = "";
	
	public LittleTriggerExecuteCommand(String id) {
		super(id);
		name = "Execute Command";
	}
	
	public LittleTriggerExecuteCommand(String id, String command) {
		super(id);
		name = "Execute Command";
		this.command = command;
	}
	
	@Override
	public NBTTagCompound createNBT() {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setString("trigger", id);
		nbt.setString("command", command);
		return nbt;
	}
	
	@Override
	public void updateControls(GuiParent parent) {
		GuiPanel panel = (GuiPanel) parent.get("content");
		wipeControls(panel);
		panel.addControl(new GuiWrappedTextField("command", command, 0, 50, 100, 100));
		new GuiTextBox("", "", 0, 0, 0);
	}
	
	@Override
	public void updateValues(CoreControl source) {
		if (source instanceof GuiWrappedTextField) {
			GuiWrappedTextField text = (GuiWrappedTextField) source;
			this.command = text.text;
		}
	}
	
}
