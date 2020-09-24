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
import net.minecraft.nbt.NBTTagCompound;

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
		//ItemTapeMeasure.measure.get(ItemTapeMeasure.index).setShapeType(1);
		NBTTagCompound nbt = stack.getTagCompound();
		int index = indexBox.index;
		int context = contextBox.index;
		nbt.setInteger("index", index);
		nbt.setInteger("context"+(index*2), context);
		stack.setTagCompound(nbt);
		
		//(Integer.parseInt(get(selected)))
	}

	@Override
	public void createControls() {
		NBTTagCompound nbt;
		if(stack.hasTagCompound()) 
			nbt = stack.getTagCompound();
		else
			nbt = new NBTTagCompound();
		
		int index = nbt.getInteger("index");
		int context = nbt.getInteger("context"+(index*2));
		contextBox = new GuiComboBox("grid", 120, 0, 15, LittleGridContext.getNames());
		contextBox.select(nbt.getInteger("context"+(index*2)));
		contextBox.index = context;
		controls.add(contextBox);
		
		controls.add(new GuiButton("Clear", 0, 0, 40) {
			@Override
			public void onClicked(int x, int y, int button) {
				ItemTapeMeasure thisTapeMeasure = (ItemTapeMeasure) stack.getItem();
				thisTapeMeasure.clear(stack);
			}
		});
		
		shapeBox = new GuiComboBox("shape", 0, 40, 100, new ArrayList<>(DragShape.keys()));
		shapeBox.select(4);
		shapeBox.setCustomTooltip("Does nothing yet.");
		controls.add(shapeBox);
		
		List<String> indexMax = new ArrayList<>();
		for(int i=1;i<=10;i++) {
			indexMax.add(String.valueOf(i));
		}
		
		indexBox = new GuiComboBox("selection", 0, 80, 20, indexMax);
		indexBox.select(index);
		indexBox.index = index;
		controls.add(indexBox);
	}
	
}
