package com.alet.common.utils.shape.tapemeasure;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Point3d;

import com.alet.ALETConfig;
import com.alet.client.gui.overlay.controls.GuiOverlayTextList;
import com.alet.items.ItemTapeMeasure;
import com.creativemd.creativecore.common.gui.GuiControl;
import com.creativemd.littletiles.common.util.grid.LittleGridContext;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.nbt.NBTTagCompound;

public class MeasurementShapeBox extends MeasurementShape {
    
    public MeasurementShapeBox(String name) {
        super(2, name);
    }
    
    public static void drawBoundingBox(BufferBuilder buffer, double minX, double minY, double minZ, double maxX, double maxY, double maxZ, float red, float green, float blue, float alpha) {
        buffer.pos(minX, minY, minZ).color(red, green, blue, 0.0F).endVertex();
        buffer.pos(minX, minY, minZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(maxX, minY, minZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(maxX, minY, maxZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(minX, minY, maxZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(minX, minY, minZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(minX, maxY, minZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(maxX, maxY, minZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(maxX, maxY, maxZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(minX, maxY, maxZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(minX, maxY, minZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(minX, maxY, maxZ).color(red, green, blue, 0.0F).endVertex();
        buffer.pos(minX, minY, maxZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(maxX, maxY, maxZ).color(red, green, blue, 0.0F).endVertex();
        buffer.pos(maxX, minY, maxZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(maxX, maxY, minZ).color(red, green, blue, 0.0F).endVertex();
        buffer.pos(maxX, minY, minZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(maxX, minY, minZ).color(red, green, blue, 0.0F).endVertex();
    }
    
    public static void drawBoundingBox(Point3d point1, Point3d point2, int contextSize, float red, float green, float blue, float alpha) {
        
        double conDiv = 1D / contextSize;
        double minX = point1.x;
        double minY = point1.y;
        double minZ = point1.z;
        
        double maxX = point2.x + conDiv;
        double maxY = point2.y + conDiv;
        double maxZ = point2.z + conDiv;
        
        if (minX >= maxX) {
            minX += conDiv;
            maxX -= conDiv;
        }
        if (minZ >= maxZ) {
            minZ += conDiv;
            maxZ -= conDiv;
        }
        if (minY >= maxY) {
            minY += conDiv;
            maxY -= conDiv;
        }
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        
        drawBoundingBox(bufferbuilder, minX - 0.001, minY - 0.001, minZ - 0.001, maxX + 0.001, maxY + 0.001, maxZ + 0.001,
            red, green, blue, alpha);
    }
    
    public static void drawCube(Point3d point, int contextSize, float red, float green, float blue, float alpha) {
        double conDiv = 1D / contextSize;
        double minX = (point.x) + 0.005;
        double minY = (point.y) + 0.005;
        double minZ = (point.z) + 0.005;
        
        double maxX = (point.x + conDiv) - 0.005;
        double maxY = (point.y + conDiv) - 0.005;
        double maxZ = (point.z + conDiv) - 0.005;
        
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        
        drawBoundingBox(bufferbuilder, minX - 0.001, minY - 0.001, minZ - 0.001, maxX + 0.001, maxY + 0.001, maxZ + 0.001,
            red, green, blue, alpha);
    }
    
    protected void drawShape(float red, float green, float blue, float alpha) {
        /*
        SelectLittleTile tilePosMin = listOfTilePos.get(0);
        SelectLittleTile tilePosMax = listOfTilePos.get(1);
        double centerX_1 = tilePosMin.centerX;
        double centerY_1 = tilePosMin.centerY;
        double centerZ_1 = tilePosMin.centerZ;
        
        double centerX_2 = tilePosMax.centerX;
        double centerY_2 = tilePosMax.centerY;
        double centerZ_2 = tilePosMax.centerZ;
        drawBox(tilePosMin, context.size, 1.0F, 0.0F, 0.0F, alpha);
        drawBox(tilePosMax, context.size, 0.0F, 1.0F, 0.0F, alpha);
        //  drawBoundingBox(tilePosMin, tilePosMax, red, green, blue, alpha);
        //System.out.println(new LittleAbsoluteVec(new BlockPos(centerX_1, centerY_1, centerZ_1), LittleGridContext.get(contextSize)));
        
        if (centerX_1 < centerX_2 && centerY_1 > centerY_2 && centerZ_1 > centerZ_2) {
            drawBoundingBox(tilePosMin.corner_6, tilePosMax.corner_2, red, green, blue, alpha);
        } else if (centerX_1 > centerX_2 && centerY_1 < centerY_2 && centerZ_1 > centerZ_2) {
            drawBoundingBox(tilePosMin.corner_3, tilePosMax.corner_7, red, green, blue, alpha);
        } else if (centerX_1 > centerX_2 && centerY_1 > centerY_2 && centerZ_1 > centerZ_2) {
            drawBoundingBox(tilePosMin.corner_5, tilePosMax.corner_1, red, green, blue, alpha);
        } else if (centerX_1 < centerX_2 && centerY_1 < centerY_2 && centerZ_1 > centerZ_2) {
            drawBoundingBox(tilePosMin.corner_4, tilePosMax.corner_8, red, green, blue, alpha);
        }
        if (centerX_1 < centerX_2 && centerY_1 > centerY_2 && centerZ_1 < centerZ_2) {
            drawBoundingBox(tilePosMin.corner_7, tilePosMax.corner_3, red, green, blue, alpha);
        } else if (centerX_1 > centerX_2 && centerY_1 < centerY_2 && centerZ_1 < centerZ_2) {
            drawBoundingBox(tilePosMin.corner_2, tilePosMax.corner_6, red, green, blue, alpha);
        } else if (centerX_1 > centerX_2 && centerY_1 > centerY_2 && centerZ_1 < centerZ_2) {
            drawBoundingBox(tilePosMin.corner_8, tilePosMax.corner_4, red, green, blue, alpha);
        } else if (centerX_1 < centerX_2 && centerY_1 < centerY_2 && centerZ_1 < centerZ_2) {
            drawBoundingBox(tilePosMin.corner_1, tilePosMax.corner_5, red, green, blue, alpha);
        }
        if (centerX_1 == centerX_2 && centerY_1 == centerY_2 && centerZ_1 == centerZ_2) {
            drawBoundingBox(tilePosMin.corner_1, tilePosMax.corner_5, red, green, blue, alpha);
        } else if (centerX_1 == centerX_2 && centerY_1 > centerY_2 && centerZ_1 == centerZ_2) {
            drawBoundingBox(tilePosMin.corner_5, tilePosMax.corner_1, red, green, blue, alpha);
        } else if (centerX_1 == centerX_2 && centerY_1 < centerY_2 && centerZ_1 == centerZ_2) {
            drawBoundingBox(tilePosMin.corner_1, tilePosMax.corner_5, red, green, blue, alpha);
        } else if (centerX_1 > centerX_2 && centerY_1 == centerY_2 && centerZ_1 == centerZ_2) {
            drawBoundingBox(tilePosMin.corner_5, tilePosMax.corner_1, red, green, blue, alpha);
        } else if (centerX_1 < centerX_2 && centerY_1 == centerY_2 && centerZ_1 == centerZ_2) {
            drawBoundingBox(tilePosMin.corner_1, tilePosMax.corner_5, red, green, blue, alpha);
        } else if (centerX_1 == centerX_2 && centerY_1 == centerY_2 && centerZ_1 < centerZ_2) {
            drawBoundingBox(tilePosMin.corner_1, tilePosMax.corner_5, red, green, blue, alpha);
        } else if (centerX_1 == centerX_2 && centerY_1 == centerY_2 && centerZ_1 > centerZ_2) {
            drawBoundingBox(tilePosMin.corner_5, tilePosMax.corner_1, red, green, blue, alpha);
        }
        if (centerX_1 == centerX_2 && centerY_1 > centerY_2 && centerZ_1 > centerZ_2) {
            drawBoundingBox(tilePosMin.corner_5, tilePosMax.corner_1, red, green, blue, alpha);
        } else if (centerX_1 == centerX_2 && centerY_1 < centerY_2 && centerZ_1 > centerZ_2) {
            drawBoundingBox(tilePosMin.corner_4, tilePosMax.corner_8, red, green, blue, alpha);
        } else if (centerX_1 == centerX_2 && centerY_1 > centerY_2 && centerZ_1 < centerZ_2) {
            drawBoundingBox(tilePosMin.corner_8, tilePosMax.corner_4, red, green, blue, alpha);
        } else if (centerX_1 == centerX_2 && centerY_1 < centerY_2 && centerZ_1 < centerZ_2) {
            drawBoundingBox(tilePosMin.corner_1, tilePosMax.corner_5, red, green, blue, alpha);
        }
        if (centerX_1 > centerX_2 && centerY_1 > centerY_2 && centerZ_1 == centerZ_2) {
            drawBoundingBox(tilePosMin.corner_5, tilePosMax.corner_1, red, green, blue, alpha);
        } else if (centerX_1 > centerX_2 && centerY_1 < centerY_2 && centerZ_1 == centerZ_2) {
            drawBoundingBox(tilePosMin.corner_2, tilePosMax.corner_6, red, green, blue, alpha);
        } else if (centerX_1 < centerX_2 && centerY_1 > centerY_2 && centerZ_1 == centerZ_2) {
            drawBoundingBox(tilePosMin.corner_7, tilePosMax.corner_3, red, green, blue, alpha);
        } else if (centerX_1 < centerX_2 && centerY_1 < centerY_2 && centerZ_1 == centerZ_2) {
            drawBoundingBox(tilePosMin.corner_1, tilePosMax.corner_5, red, green, blue, alpha);
        }
        if (centerX_1 > centerX_2 && centerY_1 == centerY_2 && centerZ_1 < centerZ_2) {
            drawBoundingBox(tilePosMin.corner_8, tilePosMax.corner_4, red, green, blue, alpha);
        } else if (centerX_1 > centerX_2 && centerY_1 == centerY_2 && centerZ_1 > centerZ_2) {
            drawBoundingBox(tilePosMin.corner_3, tilePosMax.corner_7, red, green, blue, alpha);
        } else if (centerX_1 < centerX_2 && centerY_1 == centerY_2 && centerZ_1 > centerZ_2) {
            drawBoundingBox(tilePosMin.corner_6, tilePosMax.corner_2, red, green, blue, alpha);
        } else if (centerX_1 < centerX_2 && centerY_1 == centerY_2 && centerZ_1 < centerZ_2) {
            drawBoundingBox(tilePosMin.corner_1, tilePosMax.corner_5, red, green, blue, alpha);
        }*/
        
    }
    
    @Override
    protected void drawShape(List<Point3d> points, LittleGridContext context, float red, float green, float blue, float alpha) {
        drawBoundingBox(points.get(0), points.get(1), context.size, red, green, blue, alpha);
        drawCube(points.get(0), context.size, 1.0F, 0.0F, 0.0F, alpha);
        drawCube(points.get(1), context.size, 0.0F, 1.0F, 0.0F, alpha);
    }
    
    @Override
    public List<GuiControl> getCustomSettings(NBTTagCompound nbt, LittleGridContext context) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    protected void getText(GuiOverlayTextList textList, List<String> measurementUnits, int colorInt) {
        textList.addText("X: " + measurementUnits.get(0), colorInt);
        textList.addText("Y: " + measurementUnits.get(1), colorInt);
        textList.addText("Z: " + measurementUnits.get(2), colorInt);
        
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
    
}
