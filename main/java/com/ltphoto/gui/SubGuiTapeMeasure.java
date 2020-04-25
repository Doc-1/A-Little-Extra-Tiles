package com.ltphoto.gui;

import java.util.ArrayList;
import java.util.List;

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
import com.ltphoto.items.ItemTapeMeasure;
import com.ltphoto.render.tapemeasure.Measurement.Shape;
import com.ltphoto.render.tapemeasure.TapeRenderer;

import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;

public class SubGuiTapeMeasure extends SubGuiConfigure{
	
	public GuiComboBox contextBox;
	public int selectedIndex = 0;
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
				ItemTapeMeasure.clear();
			}
		});
		
		GuiComboBox box = (new GuiComboBox("shape", 0, 40, 100, new ArrayList<>(DragShape.keys())) {
			@Override
			protected GuiComboBoxExtension createBox() {
				return (new GuiComboBoxExtension(name + "extension", this, posX, posY + height, width - getContentOffset() * 2, 100, lines) {
					@Override
					public boolean mousePressed(int x, int y, int button) {
						super.mousePressed(x, y, button);
						ItemTapeMeasure.measure.get(ItemTapeMeasure.index).setShapeType(selected);
						System.out.println(selected+" "+ItemTapeMeasure.measure.size()
							+ItemTapeMeasure.measure.get(ItemTapeMeasure.index).shapeType);
						return true;
					}
				});
			}
		});
		box.select(ItemLittleChisel.getShape(stack).key);
		box.setCustomTooltip("Does nothing yet.");
		controls.add(box);
		
		
		List<String> test = new ArrayList<>();
		for(int i=1;i<=10;i++) {
			test.add(String.valueOf(i));
		}
		

		GuiComboBox box2 = (new GuiComboBox("selection", 0, 80, 20, test) {
			//Allows detection of mouse pressed inside the drop down menu
			@Override
			protected GuiComboBoxExtension createBox() {
				return (new GuiComboBoxExtension(name + "extension", this, posX, posY + height, width - getContentOffset() * 2, 100, lines) {
					@Override
					public boolean mousePressed(int x, int y, int button) {
						super.mousePressed(x, y, button);
						
						ItemTapeMeasure.index2 = selected;
						//(Integer.parseInt(get(selected)))
						ItemTapeMeasure.index = Integer.parseInt(get(selected))+(selected-1);
						//System.out.println(Integer.parseInt(get(selected))+(selected-1));
						//System.out.println(selected);
						//System.out.println(selected-1);
						
						return true;
						/*
					 	1 0-1
					 	2 2-3
					 	3 4-5
					 	4 6-7
					 	5 8-9
						*/
					}
				});
			}
		});
		
		box2.select(ItemTapeMeasure.index2);
		controls.add(box2);
	}

}
