package com.alet.gui;

import java.util.ArrayList;
import java.util.List;

import com.alet.items.ItemTapeMeasure;
import com.alet.render.tapemeasure.TapeRenderer;
import com.alet.tiles.Measurement.Shape;
import com.creativemd.creativecore.common.gui.GuiControl;
import com.creativemd.creativecore.common.gui.controls.gui.GuiButton;
import com.creativemd.creativecore.common.gui.controls.gui.GuiCheckBox;
import com.creativemd.creativecore.common.gui.controls.gui.GuiComboBox;
import com.creativemd.creativecore.common.gui.controls.gui.GuiComboBoxExtension;
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

import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;

public class SubGuiTapeMeasure extends SubGuiConfigure{
	
	public GuiComboBox contextBox;
	public GuiComboBox shapeBox;
	public GuiComboBox indexBox;
	public int selectedIndex = 0;
	public SubGuiTapeMeasure(ItemStack stack) {
		super(141, 100, stack);
	}

	@Override
	public void saveConfiguration() {
		ItemMultiTiles.currentContext =  LittleGridContext.get(Integer.parseInt(contextBox.caption));
		System.out.println(shapeBox.index+" "+ItemTapeMeasure.measure.size());
		//ItemTapeMeasure.measure.get(ItemTapeMeasure.index).setShapeType(1);

		ItemTapeMeasure.index2 = indexBox.index;
		//(Integer.parseInt(get(selected)))
		ItemTapeMeasure.index = (indexBox.index)+(indexBox.index);
		
		System.out.println(ItemTapeMeasure.index); 
	}

	@Override
	public void createControls() {

		contextBox = new GuiComboBox("grid", 120, 0, 15, LittleGridContext.getNames());
		contextBox.select(ItemMultiTiles.currentContext.size + "");
		controls.add(contextBox);
		
		controls.add(new GuiButton("Clear", 0, 0, 40) {
			@Override
			public void onClicked(int x, int y, int button) {
				ItemTapeMeasure.clear(stack);
			}
		});
		
		shapeBox = new GuiComboBox("shape", 0, 40, 100, new ArrayList<>(DragShape.keys()));
		System.out.println();
		shapeBox.select(0);
		shapeBox.setCustomTooltip("Does nothing yet.");
		controls.add(shapeBox);
		
		
		List<String> test = new ArrayList<>();
		for(int i=1;i<=10;i++) {
			test.add(String.valueOf(i));
		}
		

		indexBox = new GuiComboBox("selection", 0, 80, 20, test);
		indexBox.select(ItemTapeMeasure.index2);
		controls.add(indexBox);
	}
	
}
