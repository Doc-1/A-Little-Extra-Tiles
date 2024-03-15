package com.alet.common.utils.shape.tapemeasure;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Point3d;

import com.alet.client.gui.overlay.controls.GuiOverlayTextList;
import com.creativemd.creativecore.common.gui.GuiControl;
import com.creativemd.littletiles.common.util.grid.LittleGridContext;

import net.minecraft.nbt.NBTTagCompound;

public class MeasurementShapeCompass extends MeasurementShape {
    
    public MeasurementShapeCompass(String name) {
        super(3, name);
        // TODO Auto-generated constructor stub
    }
    
    @Override
    protected void getText(GuiOverlayTextList textList, List<String> measurementUnits, int colorInt) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    protected void drawShape(List<Point3d> points, LittleGridContext context, float red, float green, float blue, float alpha) {
        Point3d p1 = points.get(0);
        Point3d p2 = points.get(1);
        Point3d p3 = points.get(2);
        MeasurementShapeLine.drawLine(p1, p2, red, green, blue, alpha);
        MeasurementShapeLine.drawLine(p2, p3, red, green, blue, alpha);
        MeasurementShapeBox.drawCube(p1, context.size, 1.0F, 0.0F, 0.0F, alpha);
        MeasurementShapeBox.drawCube(p2, context.size, 0.0F, 1.0F, 0.0F, alpha);
        MeasurementShapeBox.drawCube(p3, context.size, 0.0F, 0.0F, 1.0F, alpha);
    }
    
    @Override
    protected List<String> getMeasurementUnits(List<Point3d> points, LittleGridContext context) {
        List<String> units = new ArrayList<>();
        Point3d C = points.get(0); //is angle C
        Point3d A = points.get(1); //is angle A
        Point3d B = points.get(2); //is angle B
        
        double a = C.distance(B);
        double b = A.distance(C);
        double c = A.distance(B);
        
        double a2 = Math.pow(a, 2);
        double b2 = Math.pow(b, 2);
        double c2 = Math.pow(c, 2);
        
        double val = (b2 + c2 - a2) / (2 * b * c);
        
        units.add("Degrees: " + cleanDouble(Math.toDegrees(Math.acos(val))) + "�");
        return units;
    }
    
    @Override
    public List<GuiControl> getCustomSettings(NBTTagCompound nbt, LittleGridContext context) {
        // TODO Auto-generated method stub
        return null;
    }
    
}
