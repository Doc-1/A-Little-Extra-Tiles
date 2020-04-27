package com.alet.render.tapemeasure.shape;

import com.alet.render.tapemeasure.TapeRenderer;
import com.creativemd.littletiles.common.item.ItemMultiTiles;
import com.creativemd.littletiles.common.util.grid.LittleGridContext;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;

public class Circle {
	
	/*Experiment*/
	public static void drawCircle(double minX, double minY, double minZ, double radius, float red, float green, float blue, float alpha) {
		EntityPlayer player = Minecraft.getMinecraft().player;
		LittleGridContext context = LittleGridContext.get(ItemMultiTiles.currentContext.size);
		//radius = context.toVanillaGrid(radius);
		double d0 = player.lastTickPosX + (player.posX - player.lastTickPosX) * TapeRenderer.event.getPartialTicks();
		double d1 = player.lastTickPosY + (player.posY - player.lastTickPosY) * TapeRenderer.event.getPartialTicks();
		double d2 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * TapeRenderer.event.getPartialTicks();
		
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		
		int pointsToDraw = (int) Math.max(50, radius*20);
		
		double anglePerPoint = (Math.PI/pointsToDraw)*2;
		for(int i = 0; i < pointsToDraw; i++) {
			double angle = anglePerPoint * i;
			double x = Math.sin(angle)*radius;
			double y = Math.cos(angle)*radius;
			bufferbuilder.pos((x + minX) - d0 -0.001, (y + minY) - d1, minZ - d2).color(red, green, blue, alpha).endVertex();

		}
	}
	
	private static double cleanDouble(double doub) {
		String clean = String.format("%.4f", doub);
		doub = Double.parseDouble(clean);
		return doub;
	}
}



/*
double y = 0.0;
double x = -radius;
double newX = x;

while(!Double.isNaN(y)) {
	x = cleanDouble(x);
	newX = x;
	newX = newX/10;
	newX = cleanDouble(newX);
	y = (Math.sqrt(Math.pow(radius,2)-Math.pow(x, 2)))/10;
	//System.out.println(x + " / " + newX + " " + y);
	bufferbuilder.pos((newX + minX) - d0 -0.001, (y + minY) - d1, minZ - d2).color(red, green, blue, alpha).endVertex();
	x = x + 0.125;
}

y = 0.0;
x = radius;

while(!Double.isNaN(y)) {
	x = cleanDouble(x);
	newX = x;
	newX = newX/10;
	newX = cleanDouble(newX);
	y = -(Math.sqrt(Math.pow(radius,2)-Math.pow(x, 2)))/10;
	//System.out.println(x + " / " + newX + " " + y);
	bufferbuilder.pos((newX + minX) - d0 - 0.001, (y + minY) - d1 - 0.001, minZ - d2 - 0.001).color(red, green, blue, alpha).endVertex();
	x = x - 0.125;
}
*/