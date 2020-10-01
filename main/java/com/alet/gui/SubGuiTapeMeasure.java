package com.alet.gui;

import java.util.ArrayList;
import java.util.List;

import com.alet.gui.controls.GuiPanelWithBackground;
import com.alet.items.ItemTapeMeasure;
import com.alet.littletiles.common.utils.mc.ColorUtilsAlet;
import com.alet.littletiles.gui.controls.GuiColorPickerAlet;
import com.alet.littletiles.gui.controls.GuiColoredSteppedSliderAlet;
import com.creativemd.creativecore.common.gui.controls.gui.GuiButton;
import com.creativemd.creativecore.common.gui.controls.gui.GuiComboBox;
import com.creativemd.creativecore.common.gui.controls.gui.GuiComboBoxExtension;
import com.creativemd.littletiles.client.gui.configure.SubGuiConfigure;
import com.creativemd.littletiles.common.util.grid.LittleGridContext;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class SubGuiTapeMeasure extends SubGuiConfigure{
	
	public GuiComboBox contextBox;
	public GuiComboBox shapeBox;
	public GuiComboBox indexBox;
	public GuiColorPickerAlet colorPicker;
	public GuiPanelWithBackground colorDisp;
	
	public int selectedIndex = 0;
	public SubGuiTapeMeasure(ItemStack stack) {
		super(141, 100, stack);
	}
	
	@Override
	public void saveConfiguration() {
		NBTTagCompound nbt = stack.getTagCompound();
		int index = indexBox.index;
		int context = contextBox.index;
		int shape = shapeBox.index;
		
		GuiColoredSteppedSliderAlet sliderR = (GuiColoredSteppedSliderAlet) colorPicker.get("r");
		GuiColoredSteppedSliderAlet sliderG = (GuiColoredSteppedSliderAlet) colorPicker.get("g");
		GuiColoredSteppedSliderAlet sliderB = (GuiColoredSteppedSliderAlet) colorPicker.get("b");

		int r = (int) sliderR.value;
		int g = (int) sliderG.value;
		int b = (int) sliderB.value;

		int color = ColorUtilsAlet.RGBAToInt(r, g, b, 0);
		nbt.setInteger("index", index);
		nbt.setInteger("context"+(index*2), context);
		nbt.setInteger("shape"+(index*2), shape);
		nbt.setInteger("color"+(index*2), color);
		stack.setTagCompound(nbt);
	}
	
	public void saveConfiguration(int index) {
		NBTTagCompound nbt = stack.getTagCompound();
		int context = contextBox.index;
		int shape = shapeBox.index;

		GuiColoredSteppedSliderAlet sliderR = (GuiColoredSteppedSliderAlet) colorPicker.get("r");
		GuiColoredSteppedSliderAlet sliderG = (GuiColoredSteppedSliderAlet) colorPicker.get("g");
		GuiColoredSteppedSliderAlet sliderB = (GuiColoredSteppedSliderAlet) colorPicker.get("b");
		
		int r = (int) sliderR.value;
		int g = (int) sliderG.value;
		int b = (int) sliderB.value;

		int color = ColorUtilsAlet.RGBAToInt(r, g, b, 0);
		nbt.setInteger("index", index);
		nbt.setInteger("context"+(index*2), context);
		nbt.setInteger("shape"+(index*2), shape);
		nbt.setInteger("color"+(index*2), color);
		stack.setTagCompound(nbt);
	}

	@Override
	public void createControls() {
		NBTTagCompound nbt;
		if(stack.hasTagCompound()) 
			nbt = stack.getTagCompound();
		else
			nbt = new NBTTagCompound();
		
		int measurementIndex = nbt.getInteger("index");
		
		int context = (nbt.hasKey("context"+(measurementIndex*2))) ? nbt.getInteger("context"+(measurementIndex*2)) : 0;
		int color = (nbt.hasKey("color"+(measurementIndex*2))) ? nbt.getInteger("color"+(measurementIndex*2)) : ColorUtilsAlet.WHITE;
		
		contextBox = new GuiComboBox("grid", 120, 0, 15, LittleGridContext.getNames());
		contextBox.select(nbt.getInteger("context"+(measurementIndex*2)));
		contextBox.index = context;
		controls.add(contextBox);
		
		colorDisp = new GuiPanelWithBackground("colorDisp", 120, 22, 14, 14);
		colorDisp.setColor(color);
		controls.add(colorDisp);
		
		GuiButton clearButton = (new GuiButton("Clear", 0, 0, 40) {
			@Override
			public void onClicked(int x, int y, int button) {
				ItemTapeMeasure thisTapeMeasure = (ItemTapeMeasure) stack.getItem();
				if(GuiScreen.isShiftKeyDown()) 
					thisTapeMeasure.clear(stack, indexBox.index);
				else
					thisTapeMeasure.clear(stack);

				
			}
		});
		clearButton.setCustomTooltip("Click: Deletes all measurements", "Hold Shift and Click: Deletes selected measurement");
		controls.add(clearButton);
		
		List<String> shape = new ArrayList<String>();
		shape.add("box");
		shape.add("line");
		shapeBox = new GuiComboBox("shape", 0, 22, 100, shape);
		shapeBox.select(nbt.getInteger("shape"+(measurementIndex*2)));
		shapeBox.index = nbt.getInteger("shape"+(measurementIndex*2));
		controls.add(shapeBox);
		
		colorPicker = (new GuiColorPickerAlet("picker", 35, 45, ColorUtilsAlet.IntToRGBA(color), false, 255) {
			@Override
			public void onColorChanged() {
				super.onColorChanged();
				colorDisp.setColor(ColorUtilsAlet.RGBAToInt(color));
			}
		});
		controls.add(colorPicker);
				
		List<String> indexMax = new ArrayList<>();
		for(int i=1;i<=10;i++) {
			indexMax.add(String.valueOf(i));
		}
		
		GuiComboBoxExtension indexExtension = null;
		indexBox = (new GuiComboBox("indexSelector", 0, 80, 20, indexMax) {
			@Override
			protected GuiComboBoxExtension createBox() {
				return (new GuiComboBoxExtension(name + "extension", this, posX, posY + height, width - getContentOffset() * 2, 100, lines) {
					@Override
					public void onSelectionChange() {
						saveConfiguration(index);
						super.onSelectionChange();
						updateControls(nbt, index);
					}
				});
			}
		});
		indexBox.select(measurementIndex);
		indexBox.index = measurementIndex;
		controls.add(indexBox);
	}
	
	private void updateControls(NBTTagCompound nbt, int index) {
		int shape = (nbt.hasKey("shape"+(index*2))) ? nbt.getInteger("shape"+(index*2)) : 0;
		int context = (nbt.hasKey("context"+(index*2))) ? nbt.getInteger("context"+(index*2)) : 0;
		int color = (nbt.hasKey("color"+(index*2))) ? nbt.getInteger("color"+(index*2)) : ColorUtilsAlet.WHITE;

		colorDisp.setColor(color);

		GuiColoredSteppedSliderAlet sliderR = (GuiColoredSteppedSliderAlet) colorPicker.get("r");
		GuiColoredSteppedSliderAlet sliderG = (GuiColoredSteppedSliderAlet) colorPicker.get("g");
		GuiColoredSteppedSliderAlet sliderB = (GuiColoredSteppedSliderAlet) colorPicker.get("b");
		
		colorPicker.color = ColorUtilsAlet.IntToRGBA(color);
		sliderR.value = ColorUtilsAlet.IntToRGBA(color).getRed();
		sliderG.value = ColorUtilsAlet.IntToRGBA(color).getGreen();
		sliderB.value = ColorUtilsAlet.IntToRGBA(color).getBlue();
		colorPicker.updateShadeSlider();

		contextBox.select(context);
		contextBox.index = context;
		
		shapeBox.select(shape);
		shapeBox.index = shape;
		
	}
}
