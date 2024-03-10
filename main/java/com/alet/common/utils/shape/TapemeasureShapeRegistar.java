package com.alet.common.utils.shape;

import java.util.LinkedHashMap;

import com.alet.common.utils.shape.tapemeasure.Box;
import com.alet.common.utils.shape.tapemeasure.Compass;
import com.alet.common.utils.shape.tapemeasure.Line;
import com.alet.common.utils.shape.tapemeasure.TapeMeasureShape;

public class TapemeasureShapeRegistar {
    public static LinkedHashMap<String, Class<? extends TapeMeasureShape>> registeredShapes = new LinkedHashMap<String, Class<? extends TapeMeasureShape>>();
    
    static {
        registeredShapes.put("Box", Box.class);
        registeredShapes.put("Line", Line.class);
        registeredShapes.put("Compass", Compass.class);
    }
    
    public static void registerShape(String name, Class<? extends TapeMeasureShape> shape) throws Exception {
        if (!registeredShapes.containsKey(name))
            registeredShapes.put(name, shape);
        else
            throw new Exception(name + " already exists");
    }
}
