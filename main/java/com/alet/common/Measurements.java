package com.alet.common;

import java.util.HashMap;

import com.alet.common.measurment.shape.LittleMeasurementRegistry;
import com.alet.common.measurment.shape.type.LittleMeasurement;

import net.minecraft.item.ItemStack;

public class Measurements {
    private HashMap<Integer, LittleMeasurement> indexedMeasurements;
    
    protected Measurements(ItemStack tapeMeasureStack) {
        indexedMeasurements = new HashMap<>();
    }
    
    public LittleMeasurement getOrCreateMeasurement(int index, String shapeName) {
        if (indexedMeasurements.containsKey(index)) {
            final LittleMeasurement s = indexedMeasurements.get(index);
            if (!s.getKey().equals(shapeName)) {
                final LittleMeasurement shape = LittleMeasurementRegistry.getMeasurementShape(shapeName);
                this.setMeasurement(index, shape);
                return shape;
            }
            return s;
        } else {
            final LittleMeasurement shape = LittleMeasurementRegistry.getMeasurementShape(shapeName);
            this.setMeasurement(index, shape);
            return shape;
        }
    }
    
    public LittleMeasurement getOrCreateMeasurement(int index) {
        if (indexedMeasurements.containsKey(index))
            return indexedMeasurements.get(index);
        else {
            final LittleMeasurement shape = LittleMeasurementRegistry.getMeasurementShape("Box");
            this.setMeasurement(index, shape);
            return shape;
        }
    }
    
    public void setMeasurement(int index, LittleMeasurement measurement) {
        if (indexedMeasurements.containsKey(index))
            indexedMeasurements.replace(index, measurement);
        else
            indexedMeasurements.put(index, measurement);
    }
}
