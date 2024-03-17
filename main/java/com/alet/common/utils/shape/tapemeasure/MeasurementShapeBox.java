package com.alet.common.utils.shape.tapemeasure;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Point3d;

import com.alet.ALETConfig;
import com.alet.common.utils.text.draw.DrawString3d;
import com.alet.common.utils.text.draw.DrawString3d.DrawPosition;
import com.alet.items.ItemTapeMeasure;
import com.creativemd.creativecore.common.gui.GuiControl;
import com.creativemd.creativecore.common.utils.mc.ColorUtils;
import com.creativemd.littletiles.common.util.grid.LittleGridContext;

import net.minecraft.nbt.NBTTagCompound;

public class MeasurementShapeBox extends MeasurementShape {
    
    public MeasurementShapeBox(String name) {
        super(2, name);
    }
    
    @Override
    protected void drawShape(List<Point3d> points, LittleGridContext context, float red, float green, float blue, float alpha) {
        List<Point3d> boxPoints = drawBox(points.get(0), points.get(1), context.size, red, green, blue, alpha);
        drawCube(points.get(0), context.size, 1.0F, 0.0F, 0.0F, alpha);
        drawCube(points.get(1), context.size, 0.0F, 1.0F, 0.0F, alpha);
        drawText(boxPoints, tryGetMeasurementUnits(points, context), context.size, ColorUtils.WHITE);
    }
    
    @Override
    public List<GuiControl> getCustomSettings(NBTTagCompound nbt, LittleGridContext context) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    protected List<String> getMeasurementUnits(List<Point3d> points, LittleGridContext context) {
        Point3d pos = points.get(0);
        Point3d pos2 = points.get(1);
        List<String> measurmentUnits = new ArrayList<>();
        double xDistence = getDistence(pos.x, pos2.x, context.size);
        double yDistence = getDistence(pos.y, pos2.y, context.size);
        double zDistence = getDistence(pos.z, pos2.z, context.size);
        
        double contDecimal = 1D / context.size;
        int denominator = context.size;
        String[] xDis = String.valueOf(xDistence).split("\\.");
        double xNumerator = context.size * Double.parseDouble("0." + xDis[1]);
        
        String[] yDis = String.valueOf(yDistence).split("\\.");
        double yNumerator = context.size * Double.parseDouble("0." + yDis[1]);
        
        String[] zDis = String.valueOf(zDistence).split("\\.");
        double zNumerator = context.size * Double.parseDouble("0." + zDis[1]);
        
        String xStr = "";
        String yStr = "";
        String zStr = "";
        
        if (ItemTapeMeasure.measurementType == 0) {
            if ((int) (xNumerator) == 0)
                xStr = xDis[0] + " BLOCK";
            else if (Integer.parseInt(xDis[0]) == 0)
                xStr = (int) (xNumerator) + "/" + denominator + " TILE";
            else
                xStr = xDis[0] + " BLOCK " + (int) (xNumerator) + "/" + denominator + " TILE";
            
            if ((int) (yNumerator) == 0)
                yStr = yDis[0] + " BLOCK";
            else if (Integer.parseInt(yDis[0]) == 0)
                yStr = (int) (yNumerator) + "/" + denominator + " TILE";
            else
                yStr = yDis[0] + " BLOCK " + (int) (yNumerator) + "/" + denominator + " TILE";
            
            if ((int) (zNumerator) == 0)
                zStr = zDis[0] + " BLOCK";
            else if (Integer.parseInt(zDis[0]) == 0)
                zStr = (int) (zNumerator) + "/" + denominator + " TILE";
            else
                zStr = zDis[0] + " BLOCK " + (int) (zNumerator) + "/" + denominator + " TILE";
            
            measurmentUnits.add(xStr);
            measurmentUnits.add(yStr);
            measurmentUnits.add(zStr);
        } else {
            String measurementName = ALETConfig.tapeMeasure.measurementName.get(ItemTapeMeasure.measurementType - 1);
            measurmentUnits.add(cleanDouble(changeMesurmentType((Math.abs(
                pos.x - pos2.x) + contDecimal))) + " " + measurementName);
            measurmentUnits.add(cleanDouble(changeMesurmentType((Math.abs(
                pos.y - pos2.y) + contDecimal))) + " " + measurementName);
            measurmentUnits.add(cleanDouble(changeMesurmentType((Math.abs(
                pos.z - pos2.z) + contDecimal))) + " " + measurementName);
        }
        
        return measurmentUnits;
        
    }
    
    @Override
    protected void drawText(List<Point3d> points, List<String> measurementUnits, int contextSize, int colorInt) {
        Point3d p2 = (Point3d) points.get(0).clone();
        p2.x = points.get(1).x;
        DrawString3d.drawStringOnLine(measurementUnits.get(0), contextSize, DrawPosition.Middle, points.get(0), p2, 0, 0, 0,
            ColorUtils.WHITE, true);
        // DrawString3d.drawStringOnLine(measurementUnits.get(2), contextSize, DrawPosition.Center, points.get(0), points.get(
        //    1), 0, -90, 0, ColorUtils.WHITE, true);
        
    }
    
}
