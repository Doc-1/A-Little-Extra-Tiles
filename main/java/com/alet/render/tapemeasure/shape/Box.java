package com.alet.render.tapemeasure.shape;

import com.alet.items.ItemTapeMeasure;
import com.alet.render.tapemeasure.TapeRenderer;
import com.creativemd.littletiles.common.item.ItemMultiTiles;
import com.creativemd.littletiles.common.util.grid.LittleGridContext;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;

public class Box {
	
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
		
		double d0 = player.lastTickPosX + (player.posX - player.lastTickPosX) * TapeRenderer.event.getPartialTicks();
		double d1 = player.lastTickPosY + (player.posY - player.lastTickPosY) * TapeRenderer.event.getPartialTicks();
		double d2 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * TapeRenderer.event.getPartialTicks();
		
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
	
	public static String distence(double pos_1, double pos_2) {
		LittleGridContext context = LittleGridContext.get(ItemMultiTiles.currentContext.size);
		
		double contDecimal = 1D / context.size;
		double distence = (Math.abs(pos_1 - pos_2)) + contDecimal;
		int denominator = context.size;
		String[] dis = String.valueOf(distence).split("\\.");
		double numerator = context.size * Double.parseDouble("0." + dis[1]);
		
		if((int)(numerator)==0) {
			return dis[0] + " BLOCK";
		}else if(Integer.parseInt(dis[0])==0){
			return (int) (numerator) + "/" + denominator + " TILE";
		}else {
			return dis[0] + " BLOCK " + (int) (numerator) + "/" + denominator + " TILE";
		}
	} 
	
	public static void drawBox(double centerX_1, double centerX_2, double centerY_1, double centerY_2, double centerZ_1, double centerZ_2, ItemTapeMeasure tape, int index) {
		
		int selcMeas = tape.index;
		
		if (centerX_1 < centerX_2 && centerY_1 > centerY_2 && centerZ_1 > centerZ_2) {
			drawBoundingBox(tape.measure.get(index).corner_6, tape.measure.get(index+1).corner_2, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
			drawBoundingBox(tape.measure.get(index).corner_1, tape.measure.get(index).corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
			drawBoundingBox(tape.measure.get(index+1).corner_1, tape.measure.get(index+1).corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
		} else if (centerX_1 > centerX_2 && centerY_1 < centerY_2 && centerZ_1 > centerZ_2) {
			drawBoundingBox(tape.measure.get(index).corner_3, tape.measure.get(index+1).corner_7, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
			drawBoundingBox(tape.measure.get(index).corner_1, tape.measure.get(index).corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
			drawBoundingBox(tape.measure.get(index+1).corner_1, tape.measure.get(index+1).corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
		} else if (centerX_1 > centerX_2 && centerY_1 > centerY_2 && centerZ_1 > centerZ_2) {
			drawBoundingBox(tape.measure.get(index).corner_5, tape.measure.get(index+1).corner_1, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
			drawBoundingBox(tape.measure.get(index).corner_1, tape.measure.get(index).corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
			drawBoundingBox(tape.measure.get(index+1).corner_1, tape.measure.get(index+1).corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
		} else if (centerX_1 < centerX_2 && centerY_1 < centerY_2 && centerZ_1 > centerZ_2) {
			drawBoundingBox(tape.measure.get(index).corner_4, tape.measure.get(index+1).corner_8, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
			drawBoundingBox(tape.measure.get(index).corner_1, tape.measure.get(index).corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
			drawBoundingBox(tape.measure.get(index+1).corner_1, tape.measure.get(index+1).corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
		}
		if (centerX_1 < centerX_2 && centerY_1 > centerY_2 && centerZ_1 < centerZ_2) {
			drawBoundingBox(tape.measure.get(index).corner_7, tape.measure.get(index+1).corner_3, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
			drawBoundingBox(tape.measure.get(index).corner_1, tape.measure.get(index).corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
			drawBoundingBox(tape.measure.get(index+1).corner_1, tape.measure.get(index+1).corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
		} else if (centerX_1 > centerX_2 && centerY_1 < centerY_2 && centerZ_1 < centerZ_2) {
			drawBoundingBox(tape.measure.get(index).corner_2, tape.measure.get(index+1).corner_6, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
			drawBoundingBox(tape.measure.get(index).corner_1, tape.measure.get(index).corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
			drawBoundingBox(tape.measure.get(index+1).corner_1, tape.measure.get(index+1).corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
		} else if (centerX_1 > centerX_2 && centerY_1 > centerY_2 && centerZ_1 < centerZ_2) {
			drawBoundingBox(tape.measure.get(index).corner_8, tape.measure.get(index+1).corner_4, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
			drawBoundingBox(tape.measure.get(index).corner_1, tape.measure.get(index).corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
			drawBoundingBox(tape.measure.get(index+1).corner_1, tape.measure.get(index+1).corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
		} else if (centerX_1 < centerX_2 && centerY_1 < centerY_2 && centerZ_1 < centerZ_2) {
			drawBoundingBox(tape.measure.get(index).corner_1, tape.measure.get(index+1).corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
			drawBoundingBox(tape.measure.get(index).corner_1, tape.measure.get(index).corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
			drawBoundingBox(tape.measure.get(index+1).corner_1, tape.measure.get(index+1).corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
		}
		if (centerX_1 == centerX_2 && centerY_1 == centerY_2 && centerZ_1 == centerZ_2) {
			drawBoundingBox(tape.measure.get(index).corner_1, tape.measure.get(index+1).corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
			drawBoundingBox(tape.measure.get(index).corner_1, tape.measure.get(index).corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
			drawBoundingBox(tape.measure.get(index+1).corner_1, tape.measure.get(index+1).corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
		} else if (centerX_1 == centerX_2 && centerY_1 > centerY_2 && centerZ_1 == centerZ_2) {
			drawBoundingBox(tape.measure.get(index).corner_5, tape.measure.get(index+1).corner_1, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
			drawBoundingBox(tape.measure.get(index).corner_1, tape.measure.get(index).corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
			drawBoundingBox(tape.measure.get(index+1).corner_1, tape.measure.get(index+1).corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
		} else if (centerX_1 == centerX_2 && centerY_1 < centerY_2 && centerZ_1 == centerZ_2) {
			drawBoundingBox(tape.measure.get(index).corner_1, tape.measure.get(index+1).corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
			drawBoundingBox(tape.measure.get(index).corner_1, tape.measure.get(index).corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
			drawBoundingBox(tape.measure.get(index+1).corner_1, tape.measure.get(index+1).corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
		} else if (centerX_1 > centerX_2 && centerY_1 == centerY_2 && centerZ_1 == centerZ_2) {
			drawBoundingBox(tape.measure.get(index).corner_5, tape.measure.get(index+1).corner_1, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
			drawBoundingBox(tape.measure.get(index).corner_1, tape.measure.get(index).corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
			drawBoundingBox(tape.measure.get(index+1).corner_1, tape.measure.get(index+1).corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
		} else if (centerX_1 < centerX_2 && centerY_1 == centerY_2 && centerZ_1 == centerZ_2) {
			drawBoundingBox(tape.measure.get(index).corner_1, tape.measure.get(index+1).corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
			drawBoundingBox(tape.measure.get(index).corner_1, tape.measure.get(index).corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
			drawBoundingBox(tape.measure.get(index+1).corner_1, tape.measure.get(index+1).corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
		} else if (centerX_1 == centerX_2 && centerY_1 == centerY_2 && centerZ_1 < centerZ_2) {
			drawBoundingBox(tape.measure.get(index).corner_1, tape.measure.get(index+1).corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
			drawBoundingBox(tape.measure.get(index).corner_1, tape.measure.get(index).corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
			drawBoundingBox(tape.measure.get(index+1).corner_1, tape.measure.get(index+1).corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
		} else if (centerX_1 == centerX_2 && centerY_1 == centerY_2 && centerZ_1 > centerZ_2) {
			drawBoundingBox(tape.measure.get(index).corner_5, tape.measure.get(index+1).corner_1, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
			drawBoundingBox(tape.measure.get(index).corner_1, tape.measure.get(index).corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
			drawBoundingBox(tape.measure.get(index+1).corner_1, tape.measure.get(index+1).corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
		}
		if (centerX_1 == centerX_2 && centerY_1 > centerY_2 && centerZ_1 > centerZ_2) {
			drawBoundingBox(tape.measure.get(index).corner_5, tape.measure.get(index+1).corner_1, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
			drawBoundingBox(tape.measure.get(index).corner_1, tape.measure.get(index).corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
			drawBoundingBox(tape.measure.get(index+1).corner_1, tape.measure.get(index+1).corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
		} else if (centerX_1 == centerX_2 && centerY_1 < centerY_2 && centerZ_1 > centerZ_2) {
			drawBoundingBox(tape.measure.get(index).corner_4, tape.measure.get(index+1).corner_8, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
			drawBoundingBox(tape.measure.get(index).corner_1, tape.measure.get(index).corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
			drawBoundingBox(tape.measure.get(index+1).corner_1, tape.measure.get(index+1).corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
		} else if (centerX_1 == centerX_2 && centerY_1 > centerY_2 && centerZ_1 < centerZ_2) {
			drawBoundingBox(tape.measure.get(index).corner_8, tape.measure.get(index+1).corner_4, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
			drawBoundingBox(tape.measure.get(index).corner_1, tape.measure.get(index).corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
			drawBoundingBox(tape.measure.get(index+1).corner_1, tape.measure.get(index+1).corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
		} else if (centerX_1 == centerX_2 && centerY_1 < centerY_2 && centerZ_1 < centerZ_2) {
			drawBoundingBox(tape.measure.get(index).corner_1, tape.measure.get(index+1).corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
			drawBoundingBox(tape.measure.get(index).corner_1, tape.measure.get(index).corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
			drawBoundingBox(tape.measure.get(index+1).corner_1, tape.measure.get(index+1).corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
		}
		if (centerX_1 > centerX_2 && centerY_1 > centerY_2 && centerZ_1 == centerZ_2) {
			drawBoundingBox(tape.measure.get(index).corner_5, tape.measure.get(index+1).corner_1, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
			drawBoundingBox(tape.measure.get(index).corner_1, tape.measure.get(index).corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
			drawBoundingBox(tape.measure.get(index+1).corner_1, tape.measure.get(index+1).corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
		} else if (centerX_1 > centerX_2 && centerY_1 < centerY_2 && centerZ_1 == centerZ_2) {
			drawBoundingBox(tape.measure.get(index).corner_2, tape.measure.get(index+1).corner_6, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
			drawBoundingBox(tape.measure.get(index).corner_1, tape.measure.get(index).corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
			drawBoundingBox(tape.measure.get(index+1).corner_1, tape.measure.get(index+1).corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
		} else if (centerX_1 < centerX_2 && centerY_1 > centerY_2 && centerZ_1 == centerZ_2) {
			drawBoundingBox(tape.measure.get(index).corner_7, tape.measure.get(index+1).corner_3, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
			drawBoundingBox(tape.measure.get(index).corner_1, tape.measure.get(index).corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
			drawBoundingBox(tape.measure.get(index+1).corner_1, tape.measure.get(index+1).corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
		} else if (centerX_1 < centerX_2 && centerY_1 < centerY_2 && centerZ_1 == centerZ_2) {
			drawBoundingBox(tape.measure.get(index).corner_1, tape.measure.get(index+1).corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
			drawBoundingBox(tape.measure.get(index).corner_1, tape.measure.get(index).corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
			drawBoundingBox(tape.measure.get(index+1).corner_1, tape.measure.get(index+1).corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
		}
		if (centerX_1 > centerX_2 && centerY_1 == centerY_2 && centerZ_1 < centerZ_2) {
			drawBoundingBox(tape.measure.get(index).corner_8, tape.measure.get(index+1).corner_4, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
			drawBoundingBox(tape.measure.get(index).corner_1, tape.measure.get(index).corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
			drawBoundingBox(tape.measure.get(index+1).corner_1, tape.measure.get(index+1).corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
		} else if (centerX_1 > centerX_2 && centerY_1 == centerY_2 && centerZ_1 > centerZ_2) {
			drawBoundingBox(tape.measure.get(index).corner_3, tape.measure.get(index+1).corner_7, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
			drawBoundingBox(tape.measure.get(index).corner_1, tape.measure.get(index).corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
			drawBoundingBox(tape.measure.get(index+1).corner_1, tape.measure.get(index+1).corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
		} else if (centerX_1 < centerX_2 && centerY_1 == centerY_2 && centerZ_1 > centerZ_2) {
			drawBoundingBox(tape.measure.get(index).corner_6, tape.measure.get(index+1).corner_2, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
			drawBoundingBox(tape.measure.get(index).corner_1, tape.measure.get(index).corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
			drawBoundingBox(tape.measure.get(index+1).corner_1, tape.measure.get(index+1).corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
		} else if (centerX_1 < centerX_2 && centerY_1 == centerY_2 && centerZ_1 < centerZ_2) {
			drawBoundingBox(tape.measure.get(index).corner_1, tape.measure.get(index+1).corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
			drawBoundingBox(tape.measure.get(index).corner_1, tape.measure.get(index).corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
			drawBoundingBox(tape.measure.get(index+1).corner_1, tape.measure.get(index+1).corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
		}

	}
}
