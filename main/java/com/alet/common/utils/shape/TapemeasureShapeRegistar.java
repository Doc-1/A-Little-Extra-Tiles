package com.alet.common.utils.shape;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.List;

import com.alet.common.utils.shape.tapemeasure.Box;
import com.alet.common.utils.shape.tapemeasure.Compass;
import com.alet.common.utils.shape.tapemeasure.Line;
import com.alet.common.utils.shape.tapemeasure.TapeMeasureShape;
import com.creativemd.littletiles.common.util.grid.LittleGridContext;

import net.minecraft.util.math.Vec3d;

public class TapemeasureShapeRegistar {
    public static LinkedHashMap<String, Class<? extends TapeMeasureShape>> registeredShapes = new LinkedHashMap<>();
    private static LinkedHashMap<Class<? extends TapeMeasureShape>, String> registeredShapesNames = new LinkedHashMap<>();
    
    static {
        try {
            registerShape("Box", Box.class);
            registerShape("Line", Line.class);
            registerShape("Compass", Compass.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void registerShape(String name, Class<? extends TapeMeasureShape> shape) throws Exception {
        if (!registeredShapes.containsKey(name)) {
            registeredShapes.put(name, shape);
            registeredShapesNames.put(shape, name);
        } else
            throw new Exception(name + " already exists");
    }
    
    public static TapeMeasureShape createNewShape(String shapeName, List<Vec3d> listOfPoints, LittleGridContext context) {
        TapeMeasureShape shape = null;
        try {
            Class<? extends TapeMeasureShape> shapeClass = TapemeasureShapeRegistar.registeredShapes.get(shapeName);
            if (shapeClass != null)
                shape = shapeClass.getConstructor(List.class, LittleGridContext.class).newInstance(listOfPoints, context);
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            return null;
        }
        return shape;
    }
    
    public static boolean equalsType(String name, TapeMeasureShape shape) {
        return registeredShapesNames.get(shape.getClass()).equals(name);
    }
}
