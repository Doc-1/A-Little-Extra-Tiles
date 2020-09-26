package com.alet.render.tapemeasure.shape;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

import com.alet.items.ItemTapeMeasure;
import com.alet.render.tapemeasure.TapeRenderer;
import com.alet.tiles.SelectLittleTile;
import com.creativemd.littletiles.common.item.ItemMultiTiles;
import com.creativemd.littletiles.common.tile.math.box.LittleBox;
import com.creativemd.littletiles.common.tile.math.vec.LittleAbsoluteVec;
import com.creativemd.littletiles.common.util.grid.LittleGridContext;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.client.event.RenderWorldLastEvent;

public class Line extends Shapes{

	public Line(Vec3d p, Vec3d p2, int contextSz) {
		super(p, p2, contextSz);
	}

	public static void drawLine(BufferBuilder bufferbuilder, SelectLittleTile tilePosMin, SelectLittleTile tilePosMax, float red, float green, float blue, float alpha) {
		EntityPlayer player = Minecraft.getMinecraft().player;

		double d0 = player.lastTickPosX + (player.posX - player.lastTickPosX) * TapeRenderer.event.getPartialTicks();
		double d1 = player.lastTickPosY + (player.posY - player.lastTickPosY) * TapeRenderer.event.getPartialTicks();
		double d2 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * TapeRenderer.event.getPartialTicks();
		
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
	protected String writeDistance(Vec3d pos, Vec3d pos2, int contextSize) {
		LittleGridContext context = LittleGridContext.get(contextSize);

		double xDist = getDistence(pos.x, pos2.x, contextSize);
		double yDist = getDistence(pos.y, pos2.y, contextSize);
		double zDist = getDistence(pos.z, pos2.z, contextSize);
		
		double distence = 0.0;
		if(xDist>=yDist&&xDist>=zDist) 
			distence = xDist;
		else if(yDist>=xDist&&yDist>=zDist)
			distence = yDist;
		else if(zDist>=xDist&&zDist>=yDist) 
			distence = zDist;
		int denominator = context.size;
		String[] dis = String.valueOf(distence).split("\\.");
		double numerator = context.size * Double.parseDouble("0." + dis[1]);
		
		if((int)(numerator)==0) 
			return dis[0] + " BLOCK";
		else if(Integer.parseInt(dis[0])==0)
			return (int) (numerator) + "/" + denominator + " TILE";
		else 
			return dis[0] + " BLOCK " + (int) (numerator) + "/" + denominator + " TILE";	
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
