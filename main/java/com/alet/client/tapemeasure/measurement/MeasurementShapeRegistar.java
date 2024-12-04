package com.alet.client.tapemeasure.measurement;

import java.util.Collection;
import java.util.LinkedHashMap;

public class MeasurementShapeRegistar {
    private static LinkedHashMap<String, MeasurementShape> registeredShapes = new LinkedHashMap<>();
    
    static {
        try {
            registerMeasurementShape("Box", MeasurementShapeBox.class);
            registerMeasurementShape("Line", MeasurementShapeLine.class);
            registerMeasurementShape("Compass", MeasurementShapeCompass.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void registerMeasurementShape(String name, Class<? extends MeasurementShape> shapeClass) throws Exception {
        if (!registeredShapes.containsKey(name)) {
            MeasurementShape shape = shapeClass.getDeclaredConstructor(String.class).newInstance(name);
            registeredShapes.put(name, shape);
        } else
            throw new Exception(name + " already exists");
    }
    
    public static MeasurementShape getMeasurementShape(String measurementShapeName) {
        return registeredShapes.get(measurementShapeName);
    }
    
    public static Collection<String> getAllMeasurmentShapeNames() {
        return registeredShapes.keySet();
    }
}
