package com.alet.common.gui.origins;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.Color;

import com.alet.ALETConfig;
import com.alet.client.shapes.measurements.MeasurementShapeRegistar;
import com.alet.common.gui.controls.GuiColorPickerAlet;
import com.alet.common.gui.controls.GuiColorablePanel;
import com.alet.common.gui.controls.GuiColoredSteppedSliderAlet;
import com.alet.common.utils.ColorUtilsAlet;
import com.alet.components.items.ItemTapeMeasure;
import com.creativemd.creativecore.common.gui.controls.gui.GuiButton;
import com.creativemd.creativecore.common.gui.controls.gui.GuiComboBox;
import com.creativemd.creativecore.common.gui.controls.gui.GuiComboBoxExtension;
import com.creativemd.creativecore.common.gui.controls.gui.GuiScrollBox;
import com.creativemd.creativecore.common.utils.mc.ColorUtils;
import com.creativemd.littletiles.client.gui.configure.SubGuiConfigure;
import com.creativemd.littletiles.common.util.grid.LittleGridContext;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants.NBT;

public class SubGuiTapeMeasure extends SubGuiConfigure {
    
    public SubGuiTapeMeasure(ItemStack stack) {
        super(141, 200, stack);
    }
    
    @Override
    public void saveConfiguration() {
        int index = ((GuiComboBox) this.get("indexSelector")).index;
        saveConfiguration(index);
    }
    
    public void saveConfiguration(int index) {
        NBTTagCompound stackNBT = stack.getTagCompound();
        int context = ((GuiComboBox) this.get("context")).index;
        String shape = ((GuiComboBox) this.get("shape")).getCaption();
        GuiColorPickerAlet colorPicker = (GuiColorPickerAlet) this.get("picker");
        GuiColoredSteppedSliderAlet sliderR = colorPicker.sliderR;
        GuiColoredSteppedSliderAlet sliderG = colorPicker.sliderG;
        GuiColoredSteppedSliderAlet sliderB = colorPicker.sliderB;
        
        ItemTapeMeasure.measurementType = ((GuiComboBox) this.get("measurmentType")).index;
        
        int r = (int) sliderR.value;
        int g = (int) sliderG.value;
        int b = (int) sliderB.value;
        
        int color = ColorUtilsAlet.RGBAToInt(r, g, b, 0);
        NBTTagCompound data = new NBTTagCompound();
        NBTTagCompound measurements = new NBTTagCompound();
        if (stackNBT.hasKey("measurements"))
            measurements = (NBTTagCompound) stackNBT.getTag("measurements");
        if (measurements.hasKey(index + ""))
            data = measurements.getCompoundTag(index + "");
        
        data.setInteger("context", context);
        data.setString("shape", shape);
        data.setInteger("color", color);
        measurements.setTag(index + "", data);
        stackNBT.setTag("measurements", measurements);
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
        if (stackNBT.hasKey("measurements")) {
            NBTTagCompound measurementList = (NBTTagCompound) stackNBT.getTag("measurements");
            nbt = (NBTTagCompound) measurementList.getTag(index + "");
        }
        LittleGridContext contextSize = ItemTapeMeasure.getContextAt(stackNBT, index);
        int colorInt = nbt.hasKey("color") ? nbt.getInteger("color") : ColorUtils.WHITE;
        String shape = nbt.hasKey("shape") ? nbt.getString("shape") : "";
        
        List<String> relativeMeasurement = new ArrayList<String>();
        relativeMeasurement.add("tile");
        relativeMeasurement.addAll(ALETConfig.tapeMeasure.measurementName);
        GuiComboBox measurmentTypeBox = new GuiComboBox("measurmentType", 85, 0, 25, relativeMeasurement);
        measurmentTypeBox.select(ItemTapeMeasure.measurementType);
        measurmentTypeBox.index = ItemTapeMeasure.measurementType;
        controls.add(measurmentTypeBox);
        
        GuiComboBox contextBox = new GuiComboBox("context", 120, 0, 15, contextNames);
        contextBox.select(contextSize.index);
        contextBox.index = contextSize.index;
        controls.add(contextBox);
        
        GuiColorablePanel colorDisp = new GuiColorablePanel("colorDisp", 120, 22, 14, 14, new Color(0, 0, 0), ColorUtils
                .IntToRGBA(colorInt));
        controls.add(colorDisp);
        
        List<String> indexMax = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            indexMax.add(String.valueOf(i));
        }
        
        GuiComboBox indexBox = (new GuiComboBox("indexSelector", 0, 80, 20, indexMax) {
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
        clearButton.setCustomTooltip("Click: Deletes all measurements",
            "Hold Shift and Click: Deletes selected measurement");
        controls.add(clearButton);
        
        List<String> shapes = new ArrayList<String>();
        shapes.addAll(MeasurementShapeRegistar.getAllMeasurmentShapeNames());
        GuiComboBox shapeBox = new GuiComboBox("shape", 0, 22, 100, shapes);
        controls.add(shapeBox);
        shapeBox.select(shape);
        GuiColorPickerAlet colorPicker = (new GuiColorPickerAlet("picker", 35, 45, ColorUtilsAlet.IntToRGBA(
            colorInt), false, 255) {
            @Override
            public void onColorChanged() {
                super.onColorChanged();
                colorDisp.setBackgroundColor(ColorUtilsAlet.RGBAToInt(color));
            }
        });
        controls.add(colorPicker);
        
        GuiScrollBox settings = new GuiScrollBox("settings", 0, 102, 135, 92);
        controls.add(settings);
        
    }
    
    private void updateControls(NBTTagCompound stackNBT, int index) {
        NBTTagCompound nbt = new NBTTagCompound();
        if (stackNBT.hasKey("measurement_" + index)) {
            NBTTagList l = stackNBT.getTagList("measurement_" + index, NBT.TAG_COMPOUND);
            nbt = l.getCompoundTagAt(0);
        }
        int contextSize = ItemTapeMeasure.getSelectedContext(nbt).size;
        int colorInt = nbt.hasKey("color") ? nbt.getInteger("color") : ColorUtils.WHITE;
        String shape = nbt.hasKey("shape") ? nbt.getString("shape") : "";
        
        ((GuiColorablePanel) this.get("colorDisp")).setBackgroundColor(colorInt);
        
        GuiColorPickerAlet colorPicker = (GuiColorPickerAlet) this.get("picker");
        GuiColoredSteppedSliderAlet sliderR = colorPicker.sliderR;
        GuiColoredSteppedSliderAlet sliderG = colorPicker.sliderG;
        GuiColoredSteppedSliderAlet sliderB = colorPicker.sliderB;
        
        colorPicker.color = ColorUtilsAlet.IntToRGBA(colorInt);
        sliderR.value = ColorUtilsAlet.IntToRGBA(colorInt).getRed();
        sliderG.value = ColorUtilsAlet.IntToRGBA(colorInt).getGreen();
        sliderB.value = ColorUtilsAlet.IntToRGBA(colorInt).getBlue();
        colorPicker.updateShadeSlider();
        
        GuiComboBox contextBox = (GuiComboBox) this.get("context");
        contextBox.select(contextSize);
        contextBox.index = contextSize;
        
        ((GuiComboBox) this.get("shape")).select(shape);
        
    }
}
