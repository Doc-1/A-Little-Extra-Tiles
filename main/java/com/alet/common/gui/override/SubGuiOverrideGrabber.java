package com.alet.common.gui.override;

import org.lwjgl.util.Color;

import com.alet.common.gui.controls.GuiColorPickerAlet;
import com.creativemd.creativecore.common.gui.container.SubGui;
import com.creativemd.creativecore.common.utils.mc.ColorUtils;
import com.creativemd.littletiles.LittleTiles;
import com.creativemd.littletiles.client.gui.SubGuiGrabber;
import com.creativemd.littletiles.common.item.ItemLittleChisel;
import com.creativemd.littletiles.common.item.ItemLittleGrabber.GrabberMode;
import com.creativemd.littletiles.common.tile.preview.LittlePreview;

public class SubGuiOverrideGrabber extends SubGuiOverride {
	
	public SubGuiOverrideGrabber(boolean shouldUpdate) {
		super(shouldUpdate);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void modifyControls(SubGui gui) {
		SubGuiGrabber grabberGui = (SubGuiGrabber) gui;
		
		if (grabberGui.mode.equals(GrabberMode.pixelMode)) {
			grabberGui.controls.remove(grabberGui.get("picker"));
			LittlePreview preview = ItemLittleChisel.getPreview(grabberGui.stack);
			
			Color color = ColorUtils.IntToRGBA(preview.getColor());
			
			grabberGui.controls.add(1, new GuiColorPickerAlet("picker", 0, 66, color, LittleTiles.CONFIG.isTransparencyEnabled(grabberGui.getPlayer()), LittleTiles.CONFIG.getMinimumTransparency(grabberGui.getPlayer())));
			
			grabberGui.refreshControls();
		}
		
		if (grabberGui.mode.equals(GrabberMode.replaceMode)) {
			grabberGui.controls.remove(grabberGui.get("picker"));
			LittlePreview preview = ItemLittleChisel.getPreview(grabberGui.stack);
			
			Color color = ColorUtils.IntToRGBA(preview.getColor());
			
			grabberGui.controls.add(1, new GuiColorPickerAlet("picker", 0, 66, color, LittleTiles.CONFIG.isTransparencyEnabled(grabberGui.getPlayer()), LittleTiles.CONFIG.getMinimumTransparency(grabberGui.getPlayer())));
			
			grabberGui.refreshControls();
		}
	}
	
	@Override
	public void updateControls(SubGui gui) {
		// TODO Auto-generated method stub
		
	}
	
}
