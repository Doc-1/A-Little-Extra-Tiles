package com.alet.render.tapemeasure.shape;

import com.alet.items.ItemTapeMeasure;
import com.alet.render.tapemeasure.TapeRenderer;
import com.alet.tiles.Measurement;
import com.alet.tiles.SelectLittleTile;
import com.creativemd.littletiles.common.item.ItemMultiTiles;
import com.creativemd.littletiles.common.tile.math.vec.LittleAbsoluteVec;
import com.creativemd.littletiles.common.util.grid.LittleGridContext;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;

public class Box extends Shapes{
	
	public Box(double x1, double y1, double z1, double x2, double y2, double z2, int contextSize) {
		super(x1, y1, z1, x2, y2, z2, contextSize);
		calculateDistance();
		//double modifier = 1D/contextSize;
		//xString = (Math.abs(pos.x - pos2.x)+modifier) + " mm";
		//yString = (Math.abs(pos.y - pos2.y)+modifier) + " mm";
		//zString = (Math.abs(pos.z - pos2.z)+modifier) + " mm";
	}

	public static String xString = "";
	public static String yString = "";
	public static String zString = "";
	
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
	
	public static void drawBoundingBox(Vec3d vec_1, Vec3d vec_2, float red, float green, float blue, float alpha) {
		EntityPlayer player = Minecraft.getMinecraft().player;
		
		double d0 = player.lastTickPosX + (player.posX - player.lastTickPosX) * mc.getRenderPartialTicks();
		double d1 = player.lastTickPosY + (player.posY - player.lastTickPosY) * mc.getRenderPartialTicks();
		double d2 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * mc.getRenderPartialTicks();
		
		double minX = vec_1.x;
		double minY = vec_1.y;
		double minZ = vec_1.z;
		
		double maxX = vec_2.x;
		double maxY = vec_2.y;
		double maxZ = vec_2.z;
		
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		
		drawBoundingBox(bufferbuilder, minX - d0 - 0.001, minY - d1 - 0.001, minZ - d2 - 0.001, maxX + 0.001 - d0, maxY - d1 + 0.001, maxZ - d2 + 0.001, red, green, blue, alpha);
	}
	
	public static void drawBox(SelectLittleTile tilePos, int contextSize, float red, float green, float blue, float alpha) {
		EntityPlayer player = Minecraft.getMinecraft().player;
		
		double d0 = player.lastTickPosX + (player.posX - player.lastTickPosX) * mc.getRenderPartialTicks();
		double d1 = player.lastTickPosY + (player.posY - player.lastTickPosY) * mc.getRenderPartialTicks();
		double d2 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * mc.getRenderPartialTicks();
		double conDiv = 0.5/contextSize;
		
		double minX = tilePos.centerX-conDiv;
		double minY = tilePos.centerY-conDiv;
		double minZ = tilePos.centerZ-conDiv;
		
		double maxX = tilePos.centerX+conDiv;
		double maxY = tilePos.centerY+conDiv;
		double maxZ = tilePos.centerZ+conDiv;
		
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		
		drawBoundingBox(bufferbuilder, minX - d0 - 0.001, minY - d1 - 0.001, minZ - d2 - 0.001, maxX + 0.001 - d0, maxY - d1 + 0.001, maxZ - d2 + 0.001, red, green, blue, alpha);
	}
	
	public static void drawBox(SelectLittleTile tilePosMin, SelectLittleTile tilePosMax, float red, float green, float blue, float alpha) {
		
		double centerX_1 = tilePosMin.centerX;
		double centerY_1 = tilePosMin.centerY;
		double centerZ_1 = tilePosMin.centerZ;
		
		double centerX_2 = tilePosMax.centerX;
		double centerY_2 = tilePosMax.centerY;
		double centerZ_2 = tilePosMax.centerZ;
		
		//System.out.println(new LittleAbsoluteVec(new BlockPos(centerX_1, centerY_1, centerZ_1), LittleGridContext.get(contextSize)));
		if (centerX_1 < centerX_2 && centerY_1 > centerY_2 && centerZ_1 > centerZ_2) {
			drawBoundingBox(tilePosMin.corner_6, tilePosMax.corner_2, red, green, blue, alpha);
			drawBoundingBox(tilePosMin.corner_1, tilePosMin.corner_5, red, green, blue, alpha);
			drawBoundingBox(tilePosMax.corner_1, tilePosMax.corner_5, red, green, blue, alpha);
		} else if (centerX_1 > centerX_2 && centerY_1 < centerY_2 && centerZ_1 > centerZ_2) {
			drawBoundingBox(tilePosMin.corner_3, tilePosMax.corner_7, red, green, blue, alpha);
			drawBoundingBox(tilePosMin.corner_1, tilePosMin.corner_5, red, green, blue, alpha);
			drawBoundingBox(tilePosMax.corner_1, tilePosMax.corner_5, red, green, blue, alpha);
		} else if (centerX_1 > centerX_2 && centerY_1 > centerY_2 && centerZ_1 > centerZ_2) {
			drawBoundingBox(tilePosMin.corner_5, tilePosMax.corner_1, red, green, blue, alpha);
			drawBoundingBox(tilePosMin.corner_1, tilePosMin.corner_5, red, green, blue, alpha);
			drawBoundingBox(tilePosMax.corner_1, tilePosMax.corner_5, red, green, blue, alpha);
		} else if (centerX_1 < centerX_2 && centerY_1 < centerY_2 && centerZ_1 > centerZ_2) {
			drawBoundingBox(tilePosMin.corner_4, tilePosMax.corner_8, red, green, blue, alpha);
			drawBoundingBox(tilePosMin.corner_1, tilePosMin.corner_5, red, green, blue, alpha);
			drawBoundingBox(tilePosMax.corner_1, tilePosMax.corner_5, red, green, blue, alpha);
		}
		if (centerX_1 < centerX_2 && centerY_1 > centerY_2 && centerZ_1 < centerZ_2) {
			drawBoundingBox(tilePosMin.corner_7, tilePosMax.corner_3, red, green, blue, alpha);
			drawBoundingBox(tilePosMin.corner_1, tilePosMin.corner_5, red, green, blue, alpha);
			drawBoundingBox(tilePosMax.corner_1, tilePosMax.corner_5, red, green, blue, alpha);
		} else if (centerX_1 > centerX_2 && centerY_1 < centerY_2 && centerZ_1 < centerZ_2) {
			drawBoundingBox(tilePosMin.corner_2, tilePosMax.corner_6, red, green, blue, alpha);
			drawBoundingBox(tilePosMin.corner_1, tilePosMin.corner_5, red, green, blue, alpha);
			drawBoundingBox(tilePosMax.corner_1, tilePosMax.corner_5, red, green, blue, alpha);
		} else if (centerX_1 > centerX_2 && centerY_1 > centerY_2 && centerZ_1 < centerZ_2) {
			drawBoundingBox(tilePosMin.corner_8, tilePosMax.corner_4, red, green, blue, alpha);
			drawBoundingBox(tilePosMin.corner_1, tilePosMin.corner_5, red, green, blue, alpha);
			drawBoundingBox(tilePosMax.corner_1, tilePosMax.corner_5, red, green, blue, alpha);
		} else if (centerX_1 < centerX_2 && centerY_1 < centerY_2 && centerZ_1 < centerZ_2) {
			drawBoundingBox(tilePosMin.corner_1, tilePosMax.corner_5, red, green, blue, alpha);
			drawBoundingBox(tilePosMin.corner_1, tilePosMin.corner_5, red, green, blue, alpha);
			drawBoundingBox(tilePosMax.corner_1, tilePosMax.corner_5, red, green, blue, alpha);
		}
		if (centerX_1 == centerX_2 && centerY_1 == centerY_2 && centerZ_1 == centerZ_2) {
			drawBoundingBox(tilePosMin.corner_1, tilePosMax.corner_5, red, green, blue, alpha);
			drawBoundingBox(tilePosMin.corner_1, tilePosMin.corner_5, red, green, blue, alpha);
			drawBoundingBox(tilePosMax.corner_1, tilePosMax.corner_5, red, green, blue, alpha);
		} else if (centerX_1 == centerX_2 && centerY_1 > centerY_2 && centerZ_1 == centerZ_2) {
			drawBoundingBox(tilePosMin.corner_5, tilePosMax.corner_1, red, green, blue, alpha);
			drawBoundingBox(tilePosMin.corner_1, tilePosMin.corner_5, red, green, blue, alpha);
			drawBoundingBox(tilePosMax.corner_1, tilePosMax.corner_5, red, green, blue, alpha);
		} else if (centerX_1 == centerX_2 && centerY_1 < centerY_2 && centerZ_1 == centerZ_2) {
			drawBoundingBox(tilePosMin.corner_1, tilePosMax.corner_5, red, green, blue, alpha);
			drawBoundingBox(tilePosMin.corner_1, tilePosMin.corner_5, red, green, blue, alpha);
			drawBoundingBox(tilePosMax.corner_1, tilePosMax.corner_5, red, green, blue, alpha);
		} else if (centerX_1 > centerX_2 && centerY_1 == centerY_2 && centerZ_1 == centerZ_2) {
			drawBoundingBox(tilePosMin.corner_5, tilePosMax.corner_1, red, green, blue, alpha);
			drawBoundingBox(tilePosMin.corner_1, tilePosMin.corner_5, red, green, blue, alpha);
			drawBoundingBox(tilePosMax.corner_1, tilePosMax.corner_5, red, green, blue, alpha);
		} else if (centerX_1 < centerX_2 && centerY_1 == centerY_2 && centerZ_1 == centerZ_2) {
			drawBoundingBox(tilePosMin.corner_1, tilePosMax.corner_5, red, green, blue, alpha);
			drawBoundingBox(tilePosMin.corner_1, tilePosMin.corner_5, red, green, blue, alpha);
			drawBoundingBox(tilePosMax.corner_1, tilePosMax.corner_5, red, green, blue, alpha);
		} else if (centerX_1 == centerX_2 && centerY_1 == centerY_2 && centerZ_1 < centerZ_2) {
			drawBoundingBox(tilePosMin.corner_1, tilePosMax.corner_5, red, green, blue, alpha);
			drawBoundingBox(tilePosMin.corner_1, tilePosMin.corner_5, red, green, blue, alpha);
			drawBoundingBox(tilePosMax.corner_1, tilePosMax.corner_5, red, green, blue, alpha);
		} else if (centerX_1 == centerX_2 && centerY_1 == centerY_2 && centerZ_1 > centerZ_2) {
			drawBoundingBox(tilePosMin.corner_5, tilePosMax.corner_1, red, green, blue, alpha);
			drawBoundingBox(tilePosMin.corner_1, tilePosMin.corner_5, red, green, blue, alpha);
			drawBoundingBox(tilePosMax.corner_1, tilePosMax.corner_5, red, green, blue, alpha);
		}
		if (centerX_1 == centerX_2 && centerY_1 > centerY_2 && centerZ_1 > centerZ_2) {
			drawBoundingBox(tilePosMin.corner_5, tilePosMax.corner_1, red, green, blue, alpha);
			drawBoundingBox(tilePosMin.corner_1, tilePosMin.corner_5, red, green, blue, alpha);
			drawBoundingBox(tilePosMax.corner_1, tilePosMax.corner_5, red, green, blue, alpha);
		} else if (centerX_1 == centerX_2 && centerY_1 < centerY_2 && centerZ_1 > centerZ_2) {
			drawBoundingBox(tilePosMin.corner_4, tilePosMax.corner_8, red, green, blue, alpha);
			drawBoundingBox(tilePosMin.corner_1, tilePosMin.corner_5, red, green, blue, alpha);
			drawBoundingBox(tilePosMax.corner_1, tilePosMax.corner_5, red, green, blue, alpha);
		} else if (centerX_1 == centerX_2 && centerY_1 > centerY_2 && centerZ_1 < centerZ_2) {
			drawBoundingBox(tilePosMin.corner_8, tilePosMax.corner_4, red, green, blue, alpha);
			drawBoundingBox(tilePosMin.corner_1, tilePosMin.corner_5, red, green, blue, alpha);
			drawBoundingBox(tilePosMax.corner_1, tilePosMax.corner_5, red, green, blue, alpha);
		} else if (centerX_1 == centerX_2 && centerY_1 < centerY_2 && centerZ_1 < centerZ_2) {
			drawBoundingBox(tilePosMin.corner_1, tilePosMax.corner_5, red, green, blue, alpha);
			drawBoundingBox(tilePosMin.corner_1, tilePosMin.corner_5, red, green, blue, alpha);
			drawBoundingBox(tilePosMax.corner_1, tilePosMax.corner_5, red, green, blue, alpha);
		}
		if (centerX_1 > centerX_2 && centerY_1 > centerY_2 && centerZ_1 == centerZ_2) {
			drawBoundingBox(tilePosMin.corner_5, tilePosMax.corner_1, red, green, blue, alpha);
			drawBoundingBox(tilePosMin.corner_1, tilePosMin.corner_5, red, green, blue, alpha);
			drawBoundingBox(tilePosMax.corner_1, tilePosMax.corner_5, red, green, blue, alpha);
		} else if (centerX_1 > centerX_2 && centerY_1 < centerY_2 && centerZ_1 == centerZ_2) {
			drawBoundingBox(tilePosMin.corner_2, tilePosMax.corner_6, red, green, blue, alpha);
			drawBoundingBox(tilePosMin.corner_1, tilePosMin.corner_5, red, green, blue, alpha);
			drawBoundingBox(tilePosMax.corner_1, tilePosMax.corner_5, red, green, blue, alpha);
		} else if (centerX_1 < centerX_2 && centerY_1 > centerY_2 && centerZ_1 == centerZ_2) {
			drawBoundingBox(tilePosMin.corner_7, tilePosMax.corner_3, red, green, blue, alpha);
			drawBoundingBox(tilePosMin.corner_1, tilePosMin.corner_5, red, green, blue, alpha);
			drawBoundingBox(tilePosMax.corner_1, tilePosMax.corner_5, red, green, blue, alpha);
		} else if (centerX_1 < centerX_2 && centerY_1 < centerY_2 && centerZ_1 == centerZ_2) {
			drawBoundingBox(tilePosMin.corner_1, tilePosMax.corner_5, red, green, blue, alpha);
			drawBoundingBox(tilePosMin.corner_1, tilePosMin.corner_5, red, green, blue, alpha);
			drawBoundingBox(tilePosMax.corner_1, tilePosMax.corner_5, red, green, blue, alpha);
		}
		if (centerX_1 > centerX_2 && centerY_1 == centerY_2 && centerZ_1 < centerZ_2) {
			drawBoundingBox(tilePosMin.corner_8, tilePosMax.corner_4, red, green, blue, alpha);
			drawBoundingBox(tilePosMin.corner_1, tilePosMin.corner_5, red, green, blue, alpha);
			drawBoundingBox(tilePosMax.corner_1, tilePosMax.corner_5, red, green, blue, alpha);
		} else if (centerX_1 > centerX_2 && centerY_1 == centerY_2 && centerZ_1 > centerZ_2) {
			drawBoundingBox(tilePosMin.corner_3, tilePosMax.corner_7, red, green, blue, alpha);
			drawBoundingBox(tilePosMin.corner_1, tilePosMin.corner_5, red, green, blue, alpha);
			drawBoundingBox(tilePosMax.corner_1, tilePosMax.corner_5, red, green, blue, alpha);
		} else if (centerX_1 < centerX_2 && centerY_1 == centerY_2 && centerZ_1 > centerZ_2) {
			drawBoundingBox(tilePosMin.corner_6, tilePosMax.corner_2, red, green, blue, alpha);
			drawBoundingBox(tilePosMin.corner_1, tilePosMin.corner_5, red, green, blue, alpha);
			drawBoundingBox(tilePosMax.corner_1, tilePosMax.corner_5, red, green, blue, alpha);
		} else if (centerX_1 < centerX_2 && centerY_1 == centerY_2 && centerZ_1 < centerZ_2) {
			drawBoundingBox(tilePosMin.corner_1, tilePosMax.corner_5, red, green, blue, alpha);
			drawBoundingBox(tilePosMin.corner_1, tilePosMin.corner_5, red, green, blue, alpha);
			drawBoundingBox(tilePosMax.corner_1, tilePosMax.corner_5, red, green, blue, alpha);
		}
	}

	@Override
	protected void calculateDistance(Vec3d pos, Vec3d pos2, int contextSize) {
		double xDistence = getDistence(pos.x, pos2.x, contextSize);
		double yDistence = getDistence(pos.y, pos2.y, contextSize);
		double zDistence = getDistence(pos.z, pos2.z, contextSize);
		
		double contDecimal = 1D / contextSize;
		int denominator = contextSize;
		String[] xDis = String.valueOf(xDistence).split("\\.");
		double xNumerator = contextSize * Double.parseDouble("0." + xDis[1]);
		
		String[] yDis = String.valueOf(yDistence).split("\\.");
		double yNumerator = contextSize * Double.parseDouble("0." + yDis[1]);
		
		String[] zDis = String.valueOf(zDistence).split("\\.");
		double zNumerator = contextSize * Double.parseDouble("0." + zDis[1]);
		
		String xStr = "";
		String yStr = "";
		String zStr = "";

		if((int)(xNumerator)==0) 
			xStr = xDis[0] + " BLOCK";
		else if(Integer.parseInt(xDis[0])==0)
			xStr = (int) (xNumerator) + "/" + denominator + " TILE";
		else 
			xStr = xDis[0] + " BLOCK " + (int) (xNumerator) + "/" + denominator + " TILE";
		
		if((int)(yNumerator)==0) 
			yStr = yDis[0] + " BLOCK";
		else if(Integer.parseInt(yDis[0])==0)
			yStr = (int) (yNumerator) + "/" + denominator + " TILE";
		else 
			yStr = yDis[0] + " BLOCK " + (int) (yNumerator) + "/" + denominator + " TILE";
		
		if((int)(zNumerator)==0) 
			zStr = zDis[0] + " BLOCK";
		else if(Integer.parseInt(zDis[0])==0)
			zStr = (int) (zNumerator) + "/" + denominator + " TILE";
		else 
			zStr = zDis[0] + " BLOCK " + (int) (zNumerator) + "/" + denominator + " TILE";
		
		xString = xStr;
		yString = yStr;
		zString = zStr;
	}
	
}
