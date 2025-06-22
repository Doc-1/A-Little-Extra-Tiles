package com.alet.common.measurment.shape.type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Nullable;
import javax.vecmath.Point3f;

import com.alet.ALETConfig;
import com.alet.components.items.ItemTapeMeasure;
import com.creativemd.creativecore.common.gui.GuiControl;
import com.creativemd.creativecore.common.gui.event.gui.GuiControlChangedEvent;
import com.creativemd.creativecore.common.utils.mc.ColorUtils;
import com.creativemd.creativecore.common.utils.mc.TickUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

public class LittleMeasurementBox extends LittleMeasurement {
    
    public LittleMeasurementBox(String name) {
        super(2, name);
    }
    
    @Override
    protected void drawShape() {
        HashMap<Integer, Point3f> boxPoints = drawBox(this.getPoint(0), this.getPoint(1), this.getGrid().size, this.getRed(),
            this.getGreen(), this.getBlue(), this.getAlpha());
        drawCube(points.get(0), this.getGrid().size, 1.0F, 0.0F, 0.0F, this.getAlpha());
        drawCube(points.get(1), this.getGrid().size, 0.0F, 1.0F, 0.0F, this.getAlpha());
        drawText(boxPoints, tryGetMeasurementUnits(null));
    }
    
    @Override
    public List<GuiControl> getCustomSettings(ItemStack tapeMeasure) {
        List<GuiControl> controls = new ArrayList<>();
        return controls;
    }
    
    @Override
    protected List<String> getMeasurementUnits(@Nullable HashMap<Integer, Point3f> points) {
        Point3f pos = points.get(0);
        Point3f pos2 = points.get(1);
        List<String> measurmentUnits = new ArrayList<>();
        double xDistence = getDistence(pos.x, pos2.x, this.getGrid().size);
        double yDistence = getDistence(pos.y, pos2.y, this.getGrid().size);
        double zDistence = getDistence(pos.z, pos2.z, this.getGrid().size);
        
        double contDecimal = 1D / this.getGrid().size;
        int denominator = this.getGrid().size;
        String[] xDis = String.valueOf(xDistence).split("\\.");
        double xNumerator = this.getGrid().size * Double.parseDouble("0." + xDis[1]);
        
        String[] yDis = String.valueOf(yDistence).split("\\.");
        double yNumerator = this.getGrid().size * Double.parseDouble("0." + yDis[1]);
        
        String[] zDis = String.valueOf(zDistence).split("\\.");
        double zNumerator = this.getGrid().size * Double.parseDouble("0." + zDis[1]);
        
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
        } else if (ItemTapeMeasure.measurementType == 1) {
            measurmentUnits.add((int) (xDistence * denominator) + " Pixel");
            measurmentUnits.add((int) (yDistence * denominator) + " Pixel");
            measurmentUnits.add((int) (zDistence * denominator) + " Pixel");
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
    protected void drawText(HashMap<Integer, Point3f> points, List<String> measurementUnits) {
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
        //RayTraceResult ray = yTopFace.calculateIntercept(pos, vec32);
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
                drawStringOnLine(measurementUnits.get(0), this.getGrid().size, DrawPosition.Middle, points.get(0), points
                        .get(1), ColorUtils.WHITE, true, 0);
            if (index == 2 || index == 4)
                drawStringOnLine(measurementUnits.get(0), this.getGrid().size, DrawPosition.Middle, points.get(2), points
                        .get(3), ColorUtils.WHITE, true, 0);
            if (index == 2 || index == 0)
                drawStringOnLine(measurementUnits.get(2), this.getGrid().size, DrawPosition.Middle, points.get(1), points
                        .get(3), ColorUtils.WHITE, true, 0);
            if (index == 2 || index == 1)
                drawStringOnLine(measurementUnits.get(2), this.getGrid().size, DrawPosition.Middle, points.get(0), points
                        .get(2), ColorUtils.WHITE, true, 0);
            
            if (index == 4 || index == 3)
                drawStringOnLine(measurementUnits.get(0), this.getGrid().size, DrawPosition.Middle, points.get(4), points
                        .get(5), ColorUtils.WHITE, true, 0);
            if (index == 5 || index == 3)
                drawStringOnLine(measurementUnits.get(0), this.getGrid().size, DrawPosition.Middle, points.get(6), points
                        .get(7), ColorUtils.WHITE, true, 0);
            if (index == 1 || index == 3)
                drawStringOnLine(measurementUnits.get(2), this.getGrid().size, DrawPosition.Middle, points.get(5), points
                        .get(7), ColorUtils.WHITE, true, 0);
            if (index == 0 || index == 3)
                drawStringOnLine(measurementUnits.get(2), this.getGrid().size, DrawPosition.Middle, points.get(4), points
                        .get(6), ColorUtils.WHITE, true, 0);
            
            if (index == 5 || index == 1)
                drawStringOnLine(measurementUnits.get(1), this.getGrid().size, DrawPosition.Middle, points.get(0), points
                        .get(7), ColorUtils.WHITE, true, 0);
            if (index == 0 || index == 5)
                drawStringOnLine(measurementUnits.get(1), this.getGrid().size, DrawPosition.Middle, points.get(1), points
                        .get(6), ColorUtils.WHITE, true, 0);
            if (index == 4 || index == 1)
                drawStringOnLine(measurementUnits.get(1), this.getGrid().size, DrawPosition.Middle, points.get(2), points
                        .get(5), ColorUtils.WHITE, true, 0);
            if (index == 0 || index == 4)
                drawStringOnLine(measurementUnits.get(1), this.getGrid().size, DrawPosition.Middle, points.get(3), points
                        .get(4), ColorUtils.WHITE, true, 0);
            
        }
    }
    
    @Override
    public boolean customSettingsChangedEvent(GuiControlChangedEvent event, ItemStack tapeMeasure) {
        // TODO Auto-generated method stub
        return false;
    }
    
    @Override
    public List<GuiControl> customSettingsUpdateControl(ItemStack tapeMeasure, boolean createControls) {
        return new ArrayList<>();
        
    }
}
