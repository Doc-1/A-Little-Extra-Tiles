package com.ltphoto.render.tapemeasure.shape;

import com.ltphoto.render.tapemeasure.TapeRenderer;

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
		
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		GlStateManager.glLineWidth(2.0F);
		GlStateManager.disableTexture2D();
		GlStateManager.depthMask(false);
		GlStateManager.disableDepth();
		double d0 = player.lastTickPosX + (player.posX - player.lastTickPosX) * TapeRenderer.event.getPartialTicks();
		double d1 = player.lastTickPosY + (player.posY - player.lastTickPosY) * TapeRenderer.event.getPartialTicks();
		double d2 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * TapeRenderer.event.getPartialTicks();
		
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
		
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

		tessellator.draw();
	}
	
	private static double cleanDouble(double doub) {
		String clean = String.format("%.4f", doub);
		doub = Double.parseDouble(clean);
		return doub;
	}
}
