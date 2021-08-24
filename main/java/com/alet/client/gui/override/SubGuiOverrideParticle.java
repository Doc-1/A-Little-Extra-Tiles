package com.alet.client.gui.override;

import org.lwjgl.util.Color;

import com.alet.littletiles.gui.controls.GuiColorPickerAlet;
import com.creativemd.creativecore.common.gui.container.SubGui;
import com.creativemd.creativecore.common.utils.mc.ColorUtils;
import com.creativemd.littletiles.client.gui.SubGuiParticle;

public class SubGuiOverrideParticle extends SubGuiOverride {
	
	public SubGuiOverrideParticle(boolean shouldUpdate) {
		super(shouldUpdate);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void modifyControls(SubGui gui) {
		SubGuiParticle particleGui = (SubGuiParticle) gui;
		particleGui.controls.remove(particleGui.get("color"));
		Color color = ColorUtils.IntToRGBA(particleGui.particle.settings.color);
		particleGui.controls.add(1, new GuiColorPickerAlet("color", 0, 65, color, true, 1));
		particleGui.height += 6;
		particleGui.get("color").posY -= 1;
		particleGui.get("randomColor").posY += 7;
		particleGui.get("ageLabel").posY += 6;
		particleGui.get("age").posY += 6;
		particleGui.get("ageDeviationLabel").posY += 6;
		particleGui.get("agedeviation").posY += 6;
		particleGui.get("countLabel").posY += 6;
		particleGui.get("count").posY += 6;
		particleGui.get("perLabel").posY += 6;
		particleGui.get("delay").posY += 6;
		particleGui.get("gravityLabel").posY += 6;
		particleGui.get("gravity").posY += 6;
		particleGui.get("tickLabel").posY += 6;
		particleGui.get("spread").posY += 6;
		particleGui.get("spreadpanel").posY += 6;
		particleGui.get("save").posY += 6;
		particleGui.refreshControls();
	}
	
	@Override
	public void updateControls(SubGui gui) {
		// TODO Auto-generated method stub
		
	}
	
}
