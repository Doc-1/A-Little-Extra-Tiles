package com.alet.common.utils.shape.tapemeasure;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Point3d;

import com.alet.ALETConfig;
import com.alet.client.gui.overlay.controls.GuiOverlayTextList;
import com.alet.items.ItemTapeMeasure;
import com.creativemd.creativecore.common.gui.GuiControl;
import com.creativemd.littletiles.common.util.grid.LittleGridContext;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class MeasurementShapeLine extends MeasurementShape {
    
    public MeasurementShapeLine(String name) {
        super(2, name);
        // TODO Auto-generated constructor stub
    }
    
    public static void drawLine(Point3d tilePosMin, Point3d tilePosMax, float red, float green, float blue, float alpha) {
        EntityPlayer player = Minecraft.getMinecraft().player;
        
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        
        double minX = tilePosMin.x;
        double minY = tilePosMin.y;
        double minZ = tilePosMin.z;
        
        double maxX = tilePosMax.x;
        double maxY = tilePosMax.y;
        double maxZ = tilePosMax.z;
        
        bufferbuilder.pos(minX - 0.001, minY - 0.001, minZ - 0.001).color(red, green, blue, 0.0F).endVertex();
        bufferbuilder.pos(maxX - 0.001, maxY - 0.001, maxZ - 0.001).color(red, green, blue, 1.0F).endVertex();
        bufferbuilder.pos(minX - 0.001, minY - 0.001, minZ - 0.001).color(red, green, blue, 0.0F).endVertex();
    }
    
    @Override
    protected void getText(GuiOverlayTextList textList, List<String> measurementUnits, int colorInt) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    protected List<String> getMeasurementUnits(List<Point3d> points, LittleGridContext context) {
        List<String> units = new ArrayList<>();
        Point3d pos = points.get(0);
        Point3d pos2 = points.get(1);
        
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
    protected void drawShape(List<Point3d> points, LittleGridContext context, float red, float green, float blue, float alpha) {
        Point3d p1 = points.get(0);
        Point3d p2 = points.get(1);
        MeasurementShapeBox.drawCube(p1, context.size, red, green, blue, 1.0F);
        MeasurementShapeBox.drawCube(p2, context.size, red, green, blue, 1.0F);
        
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        
        double minX = p1.x;
        double minY = p1.y;
        double minZ = p1.z;
        
        double maxX = p2.x;
        double maxY = p2.y;
        double maxZ = p2.z;
        
        bufferbuilder.pos(minX - 0.001, minY - 0.001, minZ - 0.001).color(red, green, blue, 0.0F).endVertex();
        bufferbuilder.pos(maxX - 0.001, maxY - 0.001, maxZ - 0.001).color(red, green, blue, 1.0F).endVertex();
        bufferbuilder.pos(minX - 0.001, minY - 0.001, minZ - 0.001).color(red, green, blue, 0.0F).endVertex();
    }
    
}
