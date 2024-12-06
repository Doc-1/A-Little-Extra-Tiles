package com.alet.client.tapemeasure.measurement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.vecmath.Point3f;

import com.creativemd.creativecore.common.gui.GuiControl;
import com.creativemd.creativecore.common.utils.mc.ColorUtils;
import com.creativemd.littletiles.common.util.grid.LittleGridContext;

import net.minecraft.nbt.NBTTagCompound;

public class MeasurementShapeCompass extends MeasurementShape {
    
    public MeasurementShapeCompass(String name) {
        super(3, name);
        // TODO Auto-generated constructor stub
    }
    
    @Override
    protected void drawShape(HashMap<Integer, Point3f> points, LittleGridContext context, float red, float green, float blue, float alpha) {
        Point3f p1 = points.get(0);
        Point3f p2 = points.get(1);
        Point3f p3 = points.get(2);
        MeasurementShapeLine.drawLine(p1, p2, context.size, red, green, blue, alpha);
        MeasurementShapeLine.drawLine(p2, p3, context.size, red, green, blue, alpha);
        MeasurementShapeBox.drawCube(p1, context.size, 1.0F, 0.0F, 0.0F, alpha);
        MeasurementShapeBox.drawCube(p2, context.size, 0.0F, 1.0F, 0.0F, alpha);
        MeasurementShapeBox.drawCube(p3, context.size, 0.0F, 0.0F, 1.0F, alpha);
        drawText(points, getMeasurementUnits(points, context), context.size, 1);
    }
    
    @Override
    protected List<String> getMeasurementUnits(HashMap<Integer, Point3f> points, LittleGridContext context) {
        List<String> units = new ArrayList<>();
        Point3f C = points.get(0); //is angle C
        Point3f A = points.get(1); //is angle A
        Point3f B = points.get(2); //is angle B
        
        double a = C.distance(B);
        double b = A.distance(C);
        double c = A.distance(B);
        
        double a2 = Math.pow(a, 2);
        double b2 = Math.pow(b, 2);
        double c2 = Math.pow(c, 2);
        
        double val = (b2 + c2 - a2) / (2 * b * c);
        HashMap<Integer, Point3f> CA = new HashMap<>();
        CA.put(0, C);
        CA.put(1, A);
        HashMap<Integer, Point3f> AB = new HashMap<>();
        AB.put(0, A);
        AB.put(1, B);
        units.add("Degrees: " + cleanDouble(Math.toDegrees(Math.acos(val))));
        units.addAll(MeasurementShapeRegistar.getMeasurementShape("Line").getMeasurementUnits(CA, context));
        units.addAll(MeasurementShapeRegistar.getMeasurementShape("Line").getMeasurementUnits(AB, context));
        
        return units;
    }
    
    @Override
    public List<GuiControl> getCustomSettings(NBTTagCompound nbt, LittleGridContext context) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    protected void drawText(HashMap<Integer, Point3f> points, List<String> measurementUnits, int contextSize, int colorInt) {
        drawStringOnLine(measurementUnits.get(0), contextSize, DrawPosition.Middle, points.get(1), points.get(1),
            ColorUtils.WHITE, true, 0);
        drawStringOnLine(measurementUnits.get(1), contextSize, DrawPosition.Middle, points.get(0), points.get(1),
            ColorUtils.WHITE, true, 0);
        drawStringOnLine(measurementUnits.get(2), contextSize, DrawPosition.Middle, points.get(1), points.get(2),
            ColorUtils.WHITE, true, 0);
        
    }
    
}
