package com.alet.client.gui;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.Color;

import com.alet.ALETConfig;
import com.alet.client.gui.controls.GuiColorablePanel;
import com.alet.items.ItemTapeMeasure;
import com.alet.littletiles.common.utils.mc.ColorUtilsAlet;
import com.alet.littletiles.gui.controls.GuiColorPickerAlet;
import com.alet.littletiles.gui.controls.GuiColoredSteppedSliderAlet;
import com.creativemd.creativecore.common.gui.controls.gui.GuiButton;
import com.creativemd.creativecore.common.gui.controls.gui.GuiComboBox;
import com.creativemd.creativecore.common.gui.controls.gui.GuiComboBoxExtension;
import com.creativemd.creativecore.common.utils.mc.ColorUtils;
import com.creativemd.littletiles.client.gui.configure.SubGuiConfigure;
import com.creativemd.littletiles.common.util.grid.LittleGridContext;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants.NBT;

public class SubGuiTapeMeasure extends SubGuiConfigure {
    
    public GuiComboBox measurmentTypeBox;
    public GuiComboBox contextBox;
    public GuiComboBox shapeBox;
    public GuiComboBox indexBox;
    public GuiColorPickerAlet colorPicker;
    public GuiColorablePanel colorDisp;
    
    public int selectedIndex = 0;
    
    public SubGuiTapeMeasure(ItemStack stack) {
        super(141, 100, stack);
    }
    
    @Override
    public void saveConfiguration() {
        int index = indexBox.index;
        saveConfiguration(index);
    }
    
    public void saveConfiguration(int index) {
        NBTTagCompound stackNBT = stack.getTagCompound();
        int context = contextBox.index;
        int shape = shapeBox.index;
        System.out.println(context + " save");
        GuiColoredSteppedSliderAlet sliderR = colorPicker.sliderR;
        GuiColoredSteppedSliderAlet sliderG = colorPicker.sliderG;
        GuiColoredSteppedSliderAlet sliderB = colorPicker.sliderB;
        
        ItemTapeMeasure.measurementType = measurmentTypeBox.index;
        
        int r = (int) sliderR.value;
        int g = (int) sliderG.value;
        int b = (int) sliderB.value;
        
        int color = ColorUtilsAlet.RGBAToInt(r, g, b, 0);
        NBTTagList list = new NBTTagList();
        
        NBTTagCompound nbt = new NBTTagCompound();
        if (stackNBT.hasKey("measurement_" + index)) {
            NBTTagList l = stackNBT.getTagList("measurement_" + index, NBT.TAG_COMPOUND);
            nbt = l.getCompoundTagAt(0);
        }
        nbt.setInteger("context", context);
        nbt.setInteger("shape", shape);
        nbt.setInteger("color", color);
        list.appendTag(nbt);
        stackNBT.setTag("measurement_" + index, list);
        stackNBT.setInteger("index", index);
        stack.setTagCompound(stackNBT);
    }
    
    @Override
    public void createControls() {
        NBTTagCompound stackNBT;
        List<String> contextNames = LittleGridContext.getNames();
        if (stack.hasTagCompound())
            stackNBT = stack.getTagCompound();
        else
            stackNBT = new NBTTagCompound();
        
        int index = stackNBT.getInteger("index");
        
        NBTTagCompound nbt = new NBTTagCompound();
        if (stackNBT.hasKey("measurement_" + index)) {
            NBTTagList l = stackNBT.getTagList("measurement_" + index, NBT.TAG_COMPOUND);
            nbt = l.getCompoundTagAt(0);
        }
        int contextSize = ItemTapeMeasure.getContext(nbt);
        int colorInt = nbt.hasKey("color") ? nbt.getInteger("color") : ColorUtils.WHITE;
        int shape = nbt.hasKey("shape") ? stackNBT.getInteger("shape") : 0;
        
        List<String> relativeMeasurement = new ArrayList<String>();
        relativeMeasurement.add("tile");
        relativeMeasurement.addAll(ALETConfig.tapeMeasure.measurementName);
        measurmentTypeBox = new GuiComboBox("measurmenttype", 85, 0, 25, relativeMeasurement);
        measurmentTypeBox.select(ItemTapeMeasure.measurementType);
        measurmentTypeBox.index = ItemTapeMeasure.measurementType;
        controls.add(measurmentTypeBox);
        
        contextBox = new GuiComboBox("grid", 120, 0, 15, LittleGridContext.getNames());
        System.out.println(contextSize);
        contextBox.select(contextNames.indexOf(contextSize + ""));
        contextBox.index = contextNames.indexOf(contextSize + "");
        controls.add(contextBox);
        
        colorDisp = new GuiColorablePanel("colorDisp", 120, 22, 14, 14, new Color(0, 0, 0), ColorUtils.IntToRGBA(colorInt));
        controls.add(colorDisp);
        
        GuiButton clearButton = (new GuiButton("Clear", 0, 0, 40) {
            @Override
            public void onClicked(int x, int y, int button) {
                ItemTapeMeasure thisTapeMeasure = (ItemTapeMeasure) stack.getItem();
                if (GuiScreen.isShiftKeyDown())
                    thisTapeMeasure.clear(stack, indexBox.index, this.getPlayer());
                else
                    thisTapeMeasure.clear(stack);
                
            }
        });
        clearButton.setCustomTooltip("Click: Deletes all measurements", "Hold Shift and Click: Deletes selected measurement");
        controls.add(clearButton);
        
        List<String> shapes = new ArrayList<String>();
        shapes.add("box");
        shapes.add("line");
        shapeBox = new GuiComboBox("shape", 0, 22, 100, shapes);
        shapeBox.select(shape);
        shapeBox.index = shape;
        controls.add(shapeBox);
        
        colorPicker = (new GuiColorPickerAlet("picker", 35, 45, ColorUtilsAlet.IntToRGBA(colorInt), false, 255) {
            @Override
            public void onColorChanged() {
                super.onColorChanged();
                colorDisp.setBackgroundColor(ColorUtilsAlet.RGBAToInt(color));
            }
        });
        controls.add(colorPicker);
        
        List<String> indexMax = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
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
                        updateControls(stackNBT, index);
                    }
                });
            }
        });
        indexBox.select(index);
        indexBox.index = index;
        controls.add(indexBox);
    }
    
    private void updateControls(NBTTagCompound stackNBT, int index) {
        NBTTagCompound nbt = new NBTTagCompound();
        if (stackNBT.hasKey("measurement_" + index)) {
            NBTTagList l = stackNBT.getTagList("measurement_" + index, NBT.TAG_COMPOUND);
            nbt = l.getCompoundTagAt(0);
        }
        List<String> contextNames = LittleGridContext.getNames();
        
        int contextSize = ItemTapeMeasure.getContext(nbt);
        int colorInt = nbt.hasKey("color") ? nbt.getInteger("color") : ColorUtils.WHITE;
        int shape = nbt.hasKey("shape") ? stackNBT.getInteger("shape") : 0;
        
        colorDisp.setBackgroundColor(colorInt);
        
        GuiColoredSteppedSliderAlet sliderR = colorPicker.sliderR;
        GuiColoredSteppedSliderAlet sliderG = colorPicker.sliderG;
        GuiColoredSteppedSliderAlet sliderB = colorPicker.sliderB;
        
        colorPicker.color = ColorUtilsAlet.IntToRGBA(colorInt);
        sliderR.value = ColorUtilsAlet.IntToRGBA(colorInt).getRed();
        sliderG.value = ColorUtilsAlet.IntToRGBA(colorInt).getGreen();
        sliderB.value = ColorUtilsAlet.IntToRGBA(colorInt).getBlue();
        colorPicker.updateShadeSlider();
        
        contextBox.select(contextNames.indexOf(contextSize + ""));
        contextBox.index = contextNames.indexOf(contextSize + "");
        
        shapeBox.select(shape);
        shapeBox.index = shape;
        
    }
}
