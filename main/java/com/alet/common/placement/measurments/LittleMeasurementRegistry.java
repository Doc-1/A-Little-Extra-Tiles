package com.alet.common.placement.measurments;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.LinkedHashMap;

import com.alet.common.placement.measurments.type.LittleMeasurementBox;
import com.alet.common.placement.measurments.type.LittleMeasurementCompass;
import com.alet.common.placement.measurments.type.LittleMeasurementLine;
import com.alet.common.placement.measurments.type.LittleMeasurementPattern;
import com.alet.common.placement.measurments.type.LittleMeasurementType;

import net.minecraft.nbt.NBTTagCompound;

public class LittleMeasurementRegistry {
    private static LinkedHashMap<String, Class<? extends LittleMeasurementType>> registeredShapes = new LinkedHashMap<>();
    
    static {
        try {
            registerLittleMeasurement("Box", LittleMeasurementBox.class);
            registerLittleMeasurement("Line", LittleMeasurementLine.class);
            registerLittleMeasurement("Compass", LittleMeasurementCompass.class);
            registerLittleMeasurement("Pattern", LittleMeasurementPattern.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void registerLittleMeasurement(String name, Class<? extends LittleMeasurementType> shapeClass) throws Exception {
        if (!registeredShapes.containsKey(name)) {
            registeredShapes.put(name, shapeClass);
        } else
            throw new Exception(name + " already exists");
    }
    
    public static LittleMeasurementType createMeasurementShape(NBTTagCompound dataNBT) {
        try {
            String name = dataNBT.getString("name");
            LittleMeasurementType measurementShape = registeredShapes.get(name).getDeclaredConstructor(String.class).newInstance(
                name);
            measurementShape.deserialize(dataNBT);
            return measurementShape;
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            
        }
        return null;
        
    }
    
    public static LittleMeasurementType getMeasurementShape(String measurementShapeName) {
        try {
            return registeredShapes.get(measurementShapeName).getDeclaredConstructor(String.class).newInstance(
                measurementShapeName);
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            
        }
        return null;
    }
    
    public static Collection<String> getAllMeasurmentShapeNames() {
        return registeredShapes.keySet();
    }
}
