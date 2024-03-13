package com.alet.common.utils.shape.tapemeasure;

import java.util.List;

import com.alet.ALETConfig;
import com.alet.client.gui.overlay.controls.GuiOverlayTextList;
import com.alet.items.ItemTapeMeasure;
import com.alet.tiles.SelectLittleTile;
import com.creativemd.littletiles.common.util.grid.LittleGridContext;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;

public class Line extends TapeMeasureShape {
    
    public Line(List<Vec3d> listOfPoints, LittleGridContext context) {
        super(listOfPoints, context);
        this.pointsNeeded = 2;
    }
    
    public static String distance = "";
    
    public static void drawLine(SelectLittleTile tilePosMin, SelectLittleTile tilePosMax, float red, float green, float blue, float alpha) {
        EntityPlayer player = Minecraft.getMinecraft().player;
        
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        
        double d0 = player.lastTickPosX + (player.posX - player.lastTickPosX) * mc.getRenderPartialTicks();
        double d1 = player.lastTickPosY + (player.posY - player.lastTickPosY) * mc.getRenderPartialTicks();
        double d2 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * mc.getRenderPartialTicks();
        
        double minX = tilePosMin.centerX;
        double minY = tilePosMin.centerY;
        double minZ = tilePosMin.centerZ;
        
        double maxX = tilePosMax.centerX;
        double maxY = tilePosMax.centerY;
        double maxZ = tilePosMax.centerZ;
        
        bufferbuilder.pos(minX - d0 - 0.001, minY - d1 - 0.001, minZ - d2 - 0.001).color(red, green, blue, 0.0F).endVertex();
        bufferbuilder.pos(maxX - d0 - 0.001, maxY - d1 - 0.001, maxZ - d2 - 0.001).color(red, green, blue, 1.0F).endVertex();
        bufferbuilder.pos(minX - d0 - 0.001, minY - d1 - 0.001, minZ - d2 - 0.001).color(red, green, blue, 0.0F).endVertex();
    }
    
    @Override
    public void calculateDistance() {
        Vec3d pos = listOfPoints.get(0);
        Vec3d pos2 = listOfPoints.get(1);
        
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
                distance = distArr[0] + " BLOCK";
            else if (Integer.parseInt(distArr[0]) == 0)
                distance = (int) (numerator) + "/" + denominator + " TILE";
            else
                distance = distArr[0] + " BLOCK " + (int) (numerator) + "/" + denominator + " TILE";
            
        } else {
            String measurementName = ALETConfig.tapeMeasure.measurementName.get(ItemTapeMeasure.measurementType - 1);
            double modifier = 1D / context.size;
            distance = cleanDouble(changeMesurmentType((Math.floor((pos.distanceTo(
                pos2) + modifier) * context.size)) / context.size)) + " " + measurementName;
        }
        
    }
    
    @Override
    protected void getText(GuiOverlayTextList textList, int colorInt) {
        textList.addText("Line: " + distance, colorInt);
    }
    
    @Override
    protected void drawShape(float red, float green, float blue, float alpha, List<SelectLittleTile> listOfTilePos) {
        SelectLittleTile tilePosMin = listOfTilePos.get(0);
        SelectLittleTile tilePosMax = listOfTilePos.get(1);
        Box.drawBox(tilePosMin, context.size, red, green, blue, 1.0F);
        Box.drawBox(tilePosMax, context.size, red, green, blue, 1.0F);
        EntityPlayer player = Minecraft.getMinecraft().player;
        
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        
        double d0 = player.lastTickPosX + (player.posX - player.lastTickPosX) * mc.getRenderPartialTicks();
        double d1 = player.lastTickPosY + (player.posY - player.lastTickPosY) * mc.getRenderPartialTicks();
        double d2 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * mc.getRenderPartialTicks();
        
        double minX = tilePosMin.centerX;
        double minY = tilePosMin.centerY;
        double minZ = tilePosMin.centerZ;
        
        double maxX = tilePosMax.centerX;
        double maxY = tilePosMax.centerY;
        double maxZ = tilePosMax.centerZ;
        
        bufferbuilder.pos(minX - d0 - 0.001, minY - d1 - 0.001, minZ - d2 - 0.001).color(red, green, blue, 0.0F).endVertex();
        bufferbuilder.pos(maxX - d0 - 0.001, maxY - d1 - 0.001, maxZ - d2 - 0.001).color(red, green, blue, 1.0F).endVertex();
        bufferbuilder.pos(minX - d0 - 0.001, minY - d1 - 0.001, minZ - d2 - 0.001).color(red, green, blue, 0.0F).endVertex();
        
    }
    
}
