package com.alet.client.gui.override;

import org.lwjgl.util.Color;

import com.alet.client.gui.controls.GuiToolTipBox;
import com.alet.littletiles.gui.controls.GuiColorPickerAlet;
import com.creativemd.creativecore.common.gui.container.SubGui;
import com.creativemd.creativecore.common.utils.mc.ColorUtils;
import com.creativemd.littletiles.LittleTiles;
import com.creativemd.littletiles.client.gui.SubGuiChisel;
import com.creativemd.littletiles.common.item.ItemLittleChisel;
import com.creativemd.littletiles.common.tile.preview.LittlePreview;

public class SubGuiOverrideChisel extends SubGuiOverride {
	
	public SubGuiOverrideChisel(boolean shouldUpdate) {
		super(shouldUpdate);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void modifyControls(SubGui gui) {
		SubGuiChisel chiselGui = (SubGuiChisel) gui;
		chiselGui.controls.remove(chiselGui.get("picker"));
		LittlePreview preview = ItemLittleChisel.getPreview(chiselGui.stack);
		
		Color color = ColorUtils.IntToRGBA(preview.getColor());
		GuiToolTipBox tips = new GuiToolTipBox("tips");
		tips.addAdditionalTips("MainGui", "Chisel:\nUsed to place down tiles.\nTo change grid size or placement mode, exit then press ctrl+c. If no GUI appears make sure keybindings for c and ctrl+c is only to set to LittleTiles.");
		tips.addAdditionalTips("shape", "Shape:\nSets the type of shape the chisel will be using.\n\nClick and select a option.");
		tips.addAdditionalTips("shapeextension", "Shape:\nSets the type of shape the chisel will be using.\n\nClick and select a option.");
		
		tips.addAdditionalTips("settings", "Settings:\nEach shape has its own set of settings. Such as:\nHollow-Makes the shape hallow.\n\nThinkness-How thick the wall will be.\n\nFacing-The direction the shape will be placed.");
		tips.addAdditionalTips("preview", "Material:\nSelect the type of block the chisel will place.\n\nClick and select an option. If in survival mode you will only see blocks you have in a Little Bag or inventory.\nYou can also seach for blocks at the top of the drop down menu.");
		tips.addAdditionalTips("previewextension", "Material:\nSelect the type of block the chisel will place.\n\nClick and select an option. If in survival mode you will only see blocks you have in a Little Bag or inventory.\nYou can also seach for blocks at the top of the drop down menu.");
		
		chiselGui.controls.add(tips);
		chiselGui.controls.add(1, new GuiColorPickerAlet("picker", 2, 2, color, LittleTiles.CONFIG.isTransparencyEnabled(chiselGui.getPlayer()), LittleTiles.CONFIG.getMinimumTransparency(chiselGui.getPlayer())));
		
		chiselGui.refreshControls();
	}
	
	@Override
	public void updateControls(SubGui gui) {
		// TODO Auto-generated method stub
		
	}
	
}
