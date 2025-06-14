package com.alet.client.tool.shaper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.alet.common.placement.measurments.LittleMeasurementRegistry;
import com.alet.common.placement.measurments.type.LittleMeasurementType;
import com.alet.components.items.ItemTapeMeasure;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class MeasurementHandler {
    private static HashMap<ItemStack, Measurements> tapeMeasures;
    
    public MeasurementHandler() {
        tapeMeasures = new HashMap<>();
    }
    
    public static NBTTagCompound serialize(ItemStack tapeMeasureStack) {
        NBTTagCompound nbtStack = new NBTTagCompound();
        NBTTagCompound measurements = new NBTTagCompound();
        Measurements tapeMeasure = tapeMeasures.get(tapeMeasureStack);
        if (tapeMeasure == null)
            return new NBTTagCompound();
        nbtStack.setInteger("index", tapeMeasure.index);
        tapeMeasure.listOfMeasurements.forEach((x, measurement) -> {
            measurements.setTag("" + x, measurement.serialize());
        });
        nbtStack.setTag("measurements", measurements);
        return nbtStack;
    }
    
    public static void deserialize(ItemStack tapeMeasureStack) {
        if (tapeMeasureStack.hasTagCompound()) {}
    }
    
    public static List<Measurements> getTapeMeasures() {
        List<Measurements> measurements = new ArrayList<>();
        tapeMeasures.entrySet().forEach(x -> {
            measurements.add(x.getValue());
        });
        return measurements;
    }
    
    public static void tryRemoveTapeMeasure(ItemStack tapeMeasureStack) {
        if (tapeMeasures.containsKey(tapeMeasureStack))
            tapeMeasures.remove(tapeMeasureStack);
    }
    
    public static Measurements getOrCreateTapeMeasure(ItemStack tapeMeasureStack) {
        if (tapeMeasures.containsKey(tapeMeasureStack))
            return tapeMeasures.get(tapeMeasureStack);
        else {
            Measurements tapeMeasure = new Measurements(tapeMeasureStack);
            tapeMeasures.put(tapeMeasureStack, tapeMeasure);
            return tapeMeasure;
        }
    }
    
    public static void searchPlayerInv(EntityPlayer player) {
        for (int x = 0; x <= player.inventory.getSizeInventory(); x++) {
            ItemStack stack = player.inventory.getStackInSlot(x);
            if (stack.getItem() instanceof ItemTapeMeasure)
                deserialize(stack);
        }
        
    }
    
    public static class Measurements {
        public HashMap<Integer, LittleMeasurementType> listOfMeasurements;
        public int index;
        private ItemStack tapeMeasureStack;
        
        protected Measurements(ItemStack tapeMeasureStack) {
            this.tapeMeasureStack = tapeMeasureStack;
            listOfMeasurements = new HashMap<>();
            index = 0;
        }
        
        public LittleMeasurementType getOrCreateMeasurement(int index, String shapeName) {
            if (listOfMeasurements.containsKey(index)) {
                final LittleMeasurementType s = listOfMeasurements.get(index);
                if (!s.getKey().equals(shapeName)) {
                    final LittleMeasurementType shape = LittleMeasurementRegistry.getMeasurementShape(shapeName);
                    this.setMeasurement(index, shape);
                    return shape;
                }
                return s;
            } else {
                final LittleMeasurementType shape = LittleMeasurementRegistry.getMeasurementShape(shapeName);
                this.setMeasurement(index, shape);
                return shape;
            }
        }
        
        public LittleMeasurementType getOrCreateMeasurement(int index) {
            if (listOfMeasurements.containsKey(index))
                return listOfMeasurements.get(index);
            else {
                final LittleMeasurementType shape = LittleMeasurementRegistry.getMeasurementShape("Box");
                this.setMeasurement(index, shape);
                return shape;
            }
        }
        
        public void setMeasurement(int index, LittleMeasurementType measurement) {
            if (listOfMeasurements.containsKey(index))
                listOfMeasurements.replace(index, measurement);
            else
                listOfMeasurements.put(index, measurement);
            MeasurementHandler.serialize(tapeMeasureStack);
        }
    }
    
}
