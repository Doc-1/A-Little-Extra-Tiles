package com.alet.common.measurment.shape;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.LinkedHashMap;

import com.alet.common.measurment.shape.type.LittleMeasurement;
import com.alet.common.measurment.shape.type.LittleMeasurementBox;
import com.alet.common.measurment.shape.type.LittleMeasurementCompass;
import com.alet.common.measurment.shape.type.LittleMeasurementLine;
import com.alet.common.measurment.shape.type.LittleMeasurementPattern;

import net.minecraft.nbt.NBTTagCompound;

public class LittleMeasurementRegistry {
    private static LinkedHashMap<String, Class<? extends LittleMeasurement>> registeredShapes = new LinkedHashMap<>();
    
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
    
    public static void registerLittleMeasurement(String name, Class<? extends LittleMeasurement> shapeClass) throws Exception {
        if (!registeredShapes.containsKey(name)) {
            registeredShapes.put(name, shapeClass);
        } else
            throw new Exception(name + " already exists");
    }
    
    public static LittleMeasurement createMeasurementShape(NBTTagCompound dataNBT) {
        try {
            String name = dataNBT.getString("name");
            LittleMeasurement measurementShape = registeredShapes.get(name).getDeclaredConstructor(String.class).newInstance(
                name);
            measurementShape.deserialize(dataNBT);
            return measurementShape;
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            
        }
        return null;
        
    }
    
    public static LittleMeasurement getMeasurementShape(String measurementShapeName) {
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
