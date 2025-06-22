package com.alet.common.gui.tool;

import com.alet.common.gui.controls.GuiColorPickerAlet;
import com.alet.common.gui.controls.GuiColorablePanel;
import com.alet.common.gui.controls.GuiColoredSteppedSliderAlet;
import com.alet.common.measurment.shape.LittleMeasurementRegistry;
import com.alet.common.measurment.shape.type.LittleMeasurement;
import com.alet.common.utils.ColorUtilsAlet;
import com.creativemd.creativecore.common.gui.controls.gui.GuiComboBox;
import com.creativemd.creativecore.common.gui.event.gui.GuiControlChangedEvent;
import com.creativemd.creativecore.common.utils.mc.ColorUtils;
import com.creativemd.littletiles.client.gui.configure.SubGuiConfigure;
import com.creativemd.littletiles.common.item.ItemMultiTiles;
import com.n247s.api.eventapi.eventsystem.CustomEventSubscribe;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

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
        //Measurements tapeMeasure = MeasurementRecord.getOrCreateTapeMeasure(stack);
        /*
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
        
        LittleMeasurement measurement = tapeMeasure.getOrCreateMeasurement(index, shape);
        measurement.setColor(color);
        measurement.setGrid(context);
        */
    }
    
    @Override
    public void createControls() {
        /*
        List<String> contextNames = LittleGridContext.getNames();
        
        Measurements tapeMeasure = MeasurementRecord.getOrCreateTapeMeasure(stack);
        LittleMeasurement measurement = tapeMeasure.getOrCreateMeasurement(tapeMeasure.index);
        LittleGridContext contextSize = measurement.getGrid();
        int colorInt = measurement.getColor();
        String shape = measurement.getKey();
        
        List<String> relativeMeasurement = new ArrayList<String>();
        relativeMeasurement.add("tile");
        relativeMeasurement.add("pixel");
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
        for (int i = 1; i <= 20; i++) {
            indexMax.add(String.valueOf(i));
        }
        
        GuiComboBox indexBox = (new GuiComboBox("indexSelector", 0, 80, 20, indexMax) {
            @Override
            protected GuiComboBoxExtension createBox() {
                return (new GuiComboBoxExtension(name + "extension", this, posX, posY + height, width - getContentOffset() * 2, 100, lines) {
                    @Override
                    public void onSelectionChange() {
                        GuiComboBox shapeBox = (GuiComboBox) SubGuiTapeMeasure.this.get("shape");
                        String oldShape = shapeBox.getCaption();
                        saveConfiguration(index);
                        super.onSelectionChange();
                        updateControls(stack.getTagCompound(), index);
                        
                        GuiScrollBox settings = (GuiScrollBox) SubGuiTapeMeasure.this.get("settings");
                        boolean flag = false;
                        if (!shapeBox.getCaption().equals(oldShape)) {
                            settings.controls = new ArrayList<>();
                            flag = true;
                        }
                        LittleMeasurement shape = LittleMeasurementRegistry.getMeasurementShape(shapeBox.getCaption());
                        shape.customSettingsUpdateControl(stack, flag).forEach(x -> {
                            settings.addControl(x);
                        });
                    }
                });
            }
        });
        indexBox.select(tapeMeasure.index);
        indexBox.index = tapeMeasure.index;
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
        shapes.addAll(LittleMeasurementRegistry.getAllMeasurmentShapeNames());
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
        LittleMeasurement s = LittleMeasurementRegistry.getMeasurementShape(shapeBox.getCaption());
        s.getCustomSettings(stack).forEach(x -> {
            settings.addControl(x);
        });
        */
    }
    
    @CustomEventSubscribe
    public void onControlChanged(GuiControlChangedEvent event) {
        GuiComboBox shapeBox = (GuiComboBox) this.get("shape");
        LittleMeasurement shape = LittleMeasurementRegistry.getMeasurementShape(shapeBox.getCaption());
        if (shape.customSettingsChangedEvent(event, stack))
            saveConfiguration();
    }
    
    public void updateControls(NBTTagCompound stackNBT, int index) {
        NBTTagCompound measurements = stackNBT.hasKey("measurements") ? (NBTTagCompound) stackNBT.getTag(
            "measurements") : new NBTTagCompound();
        
        NBTTagCompound data = measurements.hasKey(index + "") ? measurements.getCompoundTag(
            index + "") : new NBTTagCompound();
        int contextSize = data.hasKey("context") ? data.getInteger("context") : ItemMultiTiles.currentContext.size;
        int colorInt = data.hasKey("color") ? data.getInteger("color") : ColorUtils.WHITE;
        String shape = data.hasKey("shape") ? data.getString("shape") : "Box";
        
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
        GuiComboBox shapeBox = (GuiComboBox) this.get("shape");
        shapeBox.select(shape);
        
    }
}
