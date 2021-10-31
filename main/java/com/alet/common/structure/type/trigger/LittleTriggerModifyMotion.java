package com.alet.common.structure.type.trigger;

import com.creativemd.creativecore.common.gui.CoreControl;
import com.creativemd.creativecore.common.gui.container.GuiParent;
import com.creativemd.creativecore.common.gui.controls.gui.GuiAnalogeSlider;
import com.creativemd.creativecore.common.gui.controls.gui.GuiLabel;
import com.creativemd.creativecore.common.gui.controls.gui.GuiPanel;
import com.creativemd.creativecore.common.gui.event.gui.GuiControlChangedEvent;
import com.n247s.api.eventapi.eventsystem.CustomEventSubscribe;

import net.minecraft.nbt.NBTTagCompound;

public class LittleTriggerModifyMotion extends LittleTriggerEvent {
	
	public double xStrength = 0;
	public double yStrength = 0;
	public double zStrength = 0;
	public double forward = 0;
	
	public LittleTriggerModifyMotion(String id) {
		super(id);
		name = "Modify Motion";
	}
	
	public LittleTriggerModifyMotion(String id, double xStrength, double yStrength, double zStrength, double forward) {
		super(id);
		name = "Modify Motion";
		this.forward = forward;
		this.xStrength = xStrength;
		this.yStrength = yStrength;
		this.zStrength = zStrength;
	}
	
	@Override
	public NBTTagCompound createNBT() {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setString("trigger", id);
		nbt.setDouble("xStrength", xStrength);
		nbt.setDouble("yStrength", yStrength);
		nbt.setDouble("zStrength", zStrength);
		nbt.setDouble("forward", forward);
		return nbt;
	}
	
	@Override
	public void updateControls(GuiParent parent) {
		GuiPanel panel = (GuiPanel) parent.get("content");
		wipeControls(panel);
		panel.addControl(new GuiLabel("Modify Motion", 0, 0));
		panel.addControl(new GuiAnalogeSlider("forward", 50, 79, 48, 10, forward, -5, 5) {
			@CustomEventSubscribe
			public void onSlotChange(GuiControlChangedEvent event) {
				System.out.println("test");
			}
		});
		panel.addControl(new GuiAnalogeSlider("xStr", 15, 19, 48, 10, xStrength, -5, 5));
		panel.addControl(new GuiAnalogeSlider("yStr", 15, 39, 48, 10, yStrength, -5, 5));
		panel.addControl(new GuiAnalogeSlider("zStr", 15, 59, 48, 10, zStrength, -5, 5));
		panel.addControl(new GuiLabel("ford", "Forward:", 0, 79));
		panel.addControl(new GuiLabel("x", "X:", 0, 19));
		panel.addControl(new GuiLabel("y", "Y:", 0, 39));
		panel.addControl(new GuiLabel("z", "Z:", 0, 59));
	}
	
	@Override
	public void updateValues(CoreControl source) {
		if (source instanceof GuiAnalogeSlider) {
			GuiAnalogeSlider slider = (GuiAnalogeSlider) source;
			if (slider.name.equals("xStr"))
				xStrength = slider.value;
			if (slider.name.equals("yStr"))
				yStrength = slider.value;
			if (slider.name.equals("zStr"))
				zStrength = slider.value;
			if (slider.name.equals("forward"))
				forward = slider.value;
		}
	}
	
}
