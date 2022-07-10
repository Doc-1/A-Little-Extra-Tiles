package com.alet.render.tapemeasure.shape;

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
    public Line() {}
    
    public Line(List<Vec3d> listOfPoints, int contextSize) {
        super(listOfPoints, contextSize);
        calculateDistance();
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
    protected void calculateDistance(List<Vec3d> listOfPoints, int contextSize) {
        Vec3d pos = listOfPoints.get(0);
        Vec3d pos2 = listOfPoints.get(1);
        LittleGridContext context = LittleGridContext.get(contextSize);
        
        double xDist = getDistence(pos.x, pos2.x, contextSize);
        double yDist = getDistence(pos.y, pos2.y, contextSize);
        double zDist = getDistence(pos.z, pos2.z, contextSize);
        
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
            distance = dist + "";
        } else {
            String measurementName = ALETConfig.tapeMeasure.measurementName.get(ItemTapeMeasure.measurementType - 1);
            double modifier = 1D / contextSize;
            distance = cleanDouble(changeMesurmentType((Math.floor((pos.distanceTo(pos2) + modifier) * contextSize)) / contextSize)) + " " + measurementName;
        }
        
    }
    
    @Override
    public void getText(GuiOverlayTextList textList, int colorInt) {
        textList.addText("Line: " + distance, colorInt);
    }
    
    @Override
    public void drawShape(List<SelectLittleTile> listOfTilePos, int contextSize, float red, float green, float blue, float alpha) {
        SelectLittleTile tilePosMin = listOfTilePos.get(0);
        SelectLittleTile tilePosMax = listOfTilePos.get(1);
        Box.drawBox(tilePosMin, contextSize, red, green, blue, 1.0F);
        Box.drawBox(tilePosMax, contextSize, red, green, blue, 1.0F);
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
    
    /*
     * if((int)(numerator)==0) {
    		return dis[0] + " BLOCK";
    	}else if(Integer.parseInt(dis[0])==0){
    		return (int) (numerator) + "/" + denominator + " TILE";
    	}else {
    		return dis[0] + " BLOCK " + (int) (numerator) + "/" + denominator + " TILE";
    	}
     */
    
    /*
     *LittleGridContext context = LittleGridContext.get(ItemMultiTiles.currentContext.size);
    	
    	SelectLittleTile select = new SelectLittleTile();
    	/*
    	double contDecimal = 1D / context.size;
    	double distence = (Math.abs(pos_1 - pos_2)) + contDecimal;
    	int denominator = context.size;
    	String[] dis = String.valueOf(distence).split("\\.");
    	double numerator = context.size * Double.parseDouble("0." + dis[1]);
    	
    	double ax = pos.x;
    	double ay = pos.y;
    	double az = pos.z;
    	double bx = pos2.x;
    	double by = pos2.y;
    	double bz = pos2.z;
    	
    	double newX = 0;
    	double newY = 0;
    	double newZ = 0;
    	double t = 0;
    	
    	List<Double> xList = new ArrayList<Double>();
    	List<Double> yList = new ArrayList<Double>();
    	List<Double> zList = new ArrayList<Double>();
    
    	LinkedHashSet<Double> xHashSet = null;
    	LinkedHashSet<Double> yHashSet = null;
    	LinkedHashSet<Double> zHashSet = null;
    	
    	BlockPos point = new BlockPos(0,0,0);
    	
    	for(Double i=0.0;i<=1;i=i+0.01) {
    		i = cleanDouble(i);
    		double distenceX = (Math.abs(ax - bx));
    		double distenceY = (Math.abs(ay - by));
    		double distenceZ = (Math.abs(az - bz));
    		//System.out.println("x: "+ distenceX + " y: " + distenceY + " z: " + distenceZ);
    
    		newX = ax+(distenceX*i);
    		newY = ay+(distenceY*i);
    		newZ = az+(distenceZ*i);
    		
    		newX = (Math.round(newX*16));
    		newY = (Math.round(newY*16));
    		newZ = (Math.round(newZ*16));
    
    		xList.add(newX/16);
    		yList.add(newY/16);
    		zList.add(newZ/16);
    
    		xHashSet = new LinkedHashSet(xList);
    		yHashSet = new LinkedHashSet(yList);
    		zHashSet = new LinkedHashSet(zList);
    		
    	}
    
    	Double[] xCount = xHashSet.toArray(new Double[] {});
    	Double[] yCount = yHashSet.toArray(new Double[] {});
    	Double[] zCount = zHashSet.toArray(new Double[] {});
    
    	System.out.println("X: " + xCount.length + " Y: " + yCount.length + " Z: " + zCount.length);
    	
    	double dis = Math.sqrt( Math.pow((pos2.x-pos.x),2) 
    			+ Math.pow((pos2.y-pos.y),2) + Math.pow((pos2.z-pos.z),2));
    	
    	dis = (Math.round(dis*16));
    	dis++;
    	//dis = dis/16;
    		
     */
    
}
