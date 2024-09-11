package com.alet.client.tapemeasure.shape.measurement;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Point3f;

import com.alet.ALETConfig;
import com.alet.common.utils.text.draw.DrawString3d;
import com.alet.common.utils.text.draw.DrawString3d.DrawPosition;
import com.alet.items.ItemTapeMeasure;
import com.creativemd.creativecore.common.gui.GuiControl;
import com.creativemd.creativecore.common.utils.mc.ColorUtils;
import com.creativemd.littletiles.common.util.grid.LittleGridContext;

import net.minecraft.nbt.NBTTagCompound;

public class MeasurementShapeLine extends MeasurementShape {
    
    public MeasurementShapeLine(String name) {
        super(2, name);
        // TODO Auto-generated constructor stub
    }
    
    @Override
    protected List<String> getMeasurementUnits(List<Point3f> points, LittleGridContext context) {
        List<String> units = new ArrayList<>();
        Point3f pos = points.get(0);
        Point3f pos2 = points.get(1);
        
        double xDist = getDistence(pos.x, pos2.x, context.size);
        double yDist = getDistence(pos.y, pos2.y, context.size);
        double zDist = getDistence(pos.z, pos2.z, context.size);
        
        double dist = 0.0;
        if (xDist >= yDist && xDist >= zDist)
            dist = xDist;
        else if (yDist >= xDist && yDist >= zDist)
            dist = yDist;
        else if (zDist >= xDist && zDist >= yDist)
            dist = zDist;
        int denominator = context.size;
        String[] distArr = String.valueOf(dist).split("\\.");
        double numerator = context.size * Double.parseDouble("0." + distArr[1]);
        
        if (ItemTapeMeasure.measurementType == 0) {
            if ((int) (numerator) == 0)
                units.add(distArr[0] + " BLOCK");
            else if (Integer.parseInt(distArr[0]) == 0)
                units.add((int) (numerator) + "/" + denominator + " TILE");
            else
                units.add(distArr[0] + " BLOCK " + (int) (numerator) + "/" + denominator + " TILE");
            
        } else {
            String measurementName = ALETConfig.tapeMeasure.measurementName.get(ItemTapeMeasure.measurementType - 1);
            double modifier = 1D / context.size;
            units.add(cleanDouble(changeMesurmentType((Math.floor((pos.distance(
                pos2) + modifier) * context.size)) / context.size)) + " " + measurementName);
        }
        return units;
        
    }
    
    @Override
    public List<GuiControl> getCustomSettings(NBTTagCompound nbt, LittleGridContext context) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    protected void drawShape(List<Point3f> points, LittleGridContext context, float red, float green, float blue, float alpha) {
        Point3f p1 = points.get(0);
        Point3f p2 = points.get(1);
        MeasurementShapeBox.drawCube(p1, context.size, 1F, 0F, 0F, 1.0F);
        MeasurementShapeBox.drawCube(p2, context.size, 0F, 1F, 0F, 1.0F);
        List<Point3f> linePoints = drawLine(p1, p2, context.size, red, green, blue, alpha);
        drawText(linePoints, getMeasurementUnits(linePoints, context), context.size, pointsNeeded);
    }
    
    @Override
    protected void drawText(List<Point3f> points, List<String> measurementUnits, int contextSize, int colorInt) {
        float[] angles = getLineAngle(points.get(0), points.get(1));
        DrawString3d.drawStringOnLine(measurementUnits.get(0), contextSize, DrawPosition.Middle, points.get(0), points.get(
            1), ColorUtils.WHITE, true, 0);
    }
    
}
