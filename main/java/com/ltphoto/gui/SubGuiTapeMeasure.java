package com.ltphoto.gui;

import java.util.ArrayList;

import com.creativemd.creativecore.common.gui.controls.gui.GuiButton;
import com.creativemd.creativecore.common.gui.controls.gui.GuiCheckBox;
import com.creativemd.creativecore.common.gui.controls.gui.GuiComboBox;
import com.creativemd.creativecore.common.gui.controls.gui.GuiScrollBox;
import com.creativemd.creativecore.common.gui.controls.gui.GuiTextfield;
import com.creativemd.creativecore.common.gui.controls.gui.custom.GuiStackSelectorAll;
import com.creativemd.littletiles.client.gui.LittleSubGuiUtils;
import com.creativemd.littletiles.client.gui.configure.SubGuiConfigure;
import com.creativemd.littletiles.common.item.ItemLittleChisel;
import com.creativemd.littletiles.common.item.ItemMultiTiles;
import com.creativemd.littletiles.common.tile.preview.LittlePreview;
import com.creativemd.littletiles.common.util.grid.LittleGridContext;
import com.creativemd.littletiles.common.util.shape.DragShape;
import com.ltphoto.render.tapemeasure.TapeRenderer;

import net.minecraft.item.ItemStack;

public class SubGuiTapeMeasure extends SubGuiConfigure{
	
	public GuiComboBox contextBox;
	
	public SubGuiTapeMeasure(ItemStack stack) {
		super(141, 100, stack);
	}

	@Override
	public void saveConfiguration() {
		ItemMultiTiles.currentContext =  LittleGridContext.get(Integer.parseInt(contextBox.caption));
	}

	@Override
	public void createControls() {

		contextBox = new GuiComboBox("grid", 120, 0, 15, LittleGridContext.getNames());
		contextBox.select(ItemMultiTiles.currentContext.size + "");
		controls.add(contextBox);
		
		controls.add(new GuiButton("Clear", 0, 0, 40) {
			@Override
			public void onClicked(int x, int y, int button) {
				TapeRenderer.clear();
			}
		});
		
		GuiComboBox box = new GuiComboBox("shape", 0, 40, 100, new ArrayList<>(DragShape.keys()));
		box.select(ItemLittleChisel.getShape(stack).key);
		box.setCustomTooltip("Does nothing yet.");
		controls.add(box);
		
		
	}

}
