package com.alet.client.gui.override;

import org.lwjgl.util.Color;

import com.alet.littletiles.gui.controls.GuiColorPickerAlet;
import com.creativemd.creativecore.common.gui.container.SubGui;
import com.creativemd.creativecore.common.utils.mc.ColorUtils;
import com.creativemd.littletiles.LittleTiles;
import com.creativemd.littletiles.client.gui.SubGuiChisel;
import com.creativemd.littletiles.common.item.ItemLittleChisel;
import com.creativemd.littletiles.common.tile.preview.LittlePreview;

public class SubGuiOverrideChisel implements IOverrideSubGui {
	
	public SubGuiOverrideChisel() {
		
	}
	
	@Override
	public void modifyControls(SubGui gui) {
		SubGuiChisel chiselGui = (SubGuiChisel) gui;
		chiselGui.controls.remove(chiselGui.get("picker"));
		LittlePreview preview = ItemLittleChisel.getPreview(chiselGui.stack);
		
		Color color = ColorUtils.IntToRGBA(preview.getColor());
		
		chiselGui.controls.add(1, new GuiColorPickerAlet("picker", 2, 2, color, LittleTiles.CONFIG.isTransparencyEnabled(chiselGui.getPlayer()), LittleTiles.CONFIG.getMinimumTransparency(chiselGui.getPlayer())));
		
		chiselGui.refreshControls();
	}
	
}
