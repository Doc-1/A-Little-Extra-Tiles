package com.alet.common.gui.override;

import org.lwjgl.util.Color;

import com.alet.common.gui.controls.GuiColorPickerAlet;
import com.creativemd.creativecore.common.gui.container.SubGui;
import com.creativemd.creativecore.common.utils.mc.ColorUtils;
import com.creativemd.littletiles.LittleTiles;
import com.creativemd.littletiles.client.gui.SubGuiColorTube;
import com.creativemd.littletiles.common.item.ItemLittlePaintBrush;

public class SubGuiOverrideColorTube extends SubGuiOverride {
	
	public SubGuiOverrideColorTube(boolean shouldUpdate) {
		super(shouldUpdate);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void modifyControls(SubGui gui) {
		SubGuiColorTube colorTubeGui = (SubGuiColorTube) gui;
		colorTubeGui.controls.remove(colorTubeGui.get("picker"));
		
		Color color = ColorUtils.IntToRGBA(ItemLittlePaintBrush.getColor(colorTubeGui.stack));
		
		colorTubeGui.controls.add(1, new GuiColorPickerAlet("picker", 2, 2, color, LittleTiles.CONFIG.isTransparencyEnabled(colorTubeGui.getPlayer()), LittleTiles.CONFIG.getMinimumTransparency(colorTubeGui.getPlayer())));
		colorTubeGui.get("settings").posY += 4;
		colorTubeGui.get("settings").height -= 2;
		colorTubeGui.get("shape").posY += 4;
		colorTubeGui.refreshControls();
	}
	
	@Override
	public void updateControls(SubGui gui) {
		// TODO Auto-generated method stub
		
	}
	
}
