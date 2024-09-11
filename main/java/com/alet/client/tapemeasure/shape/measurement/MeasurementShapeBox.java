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
import com.creativemd.creativecore.common.utils.mc.TickUtils;
import com.creativemd.littletiles.common.util.grid.LittleGridContext;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

public class MeasurementShapeBox extends MeasurementShape {
    
    public MeasurementShapeBox(String name) {
        super(2, name);
    }
    
    @Override
    protected void drawShape(List<Point3f> points, LittleGridContext context, float red, float green, float blue, float alpha) {
        List<Point3f> boxPoints = drawBox(points.get(0), points.get(1), context.size, red, green, blue, alpha);
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
    protected List<String> getMeasurementUnits(List<Point3f> points, LittleGridContext context) {
        Point3f pos = points.get(0);
        Point3f pos2 = points.get(1);
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
    protected void drawText(List<Point3f> points, List<String> measurementUnits, int contextSize, int colorInt) {
        AxisAlignedBB[] boxes = new AxisAlignedBB[6];
        AxisAlignedBB xFrontFace = new AxisAlignedBB(points.get(1).x, points.get(1).y, points.get(1).z, points.get(
            4).x, points.get(4).y, points.get(4).z);
        AxisAlignedBB xBackFace = new AxisAlignedBB(points.get(0).x, points.get(0).y, points.get(0).z, points.get(
            5).x, points.get(5).y, points.get(5).z);
        
        AxisAlignedBB yTopFace = new AxisAlignedBB(points.get(0).x, points.get(0).y, points.get(0).z, points.get(3).x, points
                .get(3).y, points.get(3).z);
        AxisAlignedBB yBottomFace = new AxisAlignedBB(points.get(4).x, points.get(4).y, points.get(4).z, points.get(
            7).x, points.get(7).y, points.get(7).z);
        
        AxisAlignedBB zFrontFace = new AxisAlignedBB(points.get(4).x, points.get(4).y, points.get(4).z, points.get(
            2).x, points.get(2).y, points.get(2).z);
        AxisAlignedBB zBackFace = new AxisAlignedBB(points.get(0).x, points.get(0).y, points.get(0).z, points.get(
            6).x, points.get(6).y, points.get(6).z);
        
        boxes[0] = xFrontFace;
        boxes[1] = xBackFace;
        boxes[2] = yTopFace;
        boxes[3] = yBottomFace;
        boxes[4] = zFrontFace;
        boxes[5] = zBackFace;
        
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        float partialTickTime = TickUtils.getPartialTickTime();
        Vec3d pos = player.getPositionEyes(partialTickTime);
        double d0 = player.capabilities.isCreativeMode ? 5.0F : 4.5F;
        Vec3d look = player.getLook(partialTickTime);
        Vec3d vec32 = pos.addVector(look.x * d0, look.y * d0, look.z * d0);
        RayTraceResult ray = yTopFace.calculateIntercept(pos, vec32);
        double distance = Double.MAX_VALUE;
        int index = -1;
        for (int i = 0; i < boxes.length; i++) {
            RayTraceResult result = boxes[i].calculateIntercept(pos, vec32);
            if (result != null) {
                double tempDistance = pos.squareDistanceTo(result.hitVec);
                if (tempDistance < distance) {
                    index = i;
                    distance = tempDistance;
                }
            }
        }
        if (index != -1) {
            
            if (index == 2 || index == 5)
                DrawString3d.drawStringOnLine(measurementUnits.get(0), contextSize, DrawPosition.Middle, points.get(0),
                    points.get(1), ColorUtils.WHITE, true, 0);
            if (index == 2 || index == 4)
                DrawString3d.drawStringOnLine(measurementUnits.get(0), contextSize, DrawPosition.Middle, points.get(2),
                    points.get(3), ColorUtils.WHITE, true, 0);
            if (index == 2 || index == 0)
                DrawString3d.drawStringOnLine(measurementUnits.get(2), contextSize, DrawPosition.Middle, points.get(1),
                    points.get(3), ColorUtils.WHITE, true, 0);
            if (index == 2 || index == 1)
                DrawString3d.drawStringOnLine(measurementUnits.get(2), contextSize, DrawPosition.Middle, points.get(0),
                    points.get(2), ColorUtils.WHITE, true, 0);
            
            if (index == 4 || index == 3)
                DrawString3d.drawStringOnLine(measurementUnits.get(0), contextSize, DrawPosition.Middle, points.get(4),
                    points.get(5), ColorUtils.WHITE, true, 0);
            if (index == 5 || index == 3)
                DrawString3d.drawStringOnLine(measurementUnits.get(0), contextSize, DrawPosition.Middle, points.get(6),
                    points.get(7), ColorUtils.WHITE, true, 0);
            if (index == 1 || index == 3)
                DrawString3d.drawStringOnLine(measurementUnits.get(2), contextSize, DrawPosition.Middle, points.get(5),
                    points.get(7), ColorUtils.WHITE, true, 0);
            if (index == 0 || index == 3)
                DrawString3d.drawStringOnLine(measurementUnits.get(2), contextSize, DrawPosition.Middle, points.get(4),
                    points.get(6), ColorUtils.WHITE, true, 0);
            
            if (index == 5 || index == 1)
                DrawString3d.drawStringOnLine(measurementUnits.get(1), contextSize, DrawPosition.Middle, points.get(0),
                    points.get(7), ColorUtils.WHITE, true, 0);
            if (index == 0 || index == 5)
                DrawString3d.drawStringOnLine(measurementUnits.get(1), contextSize, DrawPosition.Middle, points.get(1),
                    points.get(6), ColorUtils.WHITE, true, 0);
            if (index == 4 || index == 1)
                DrawString3d.drawStringOnLine(measurementUnits.get(1), contextSize, DrawPosition.Middle, points.get(2),
                    points.get(5), ColorUtils.WHITE, true, 0);
            if (index == 0 || index == 4)
                DrawString3d.drawStringOnLine(measurementUnits.get(1), contextSize, DrawPosition.Middle, points.get(3),
                    points.get(4), ColorUtils.WHITE, true, 0);
            
        }
        /*
        */
    }
    
}
