package com.alet.render.tapemeasure.shape;

import com.alet.render.tapemeasure.TapeRenderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;

public class Line {

	public static void drawLine(BufferBuilder bufferbuilder, double minX, double minY, double minZ, double maxX, double maxY, double maxZ, float red, float green, float blue, float alpha) {
		EntityPlayer player = Minecraft.getMinecraft().player;

		double d0 = player.lastTickPosX + (player.posX - player.lastTickPosX) * TapeRenderer.event.getPartialTicks();
		double d1 = player.lastTickPosY + (player.posY - player.lastTickPosY) * TapeRenderer.event.getPartialTicks();
		double d2 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * TapeRenderer.event.getPartialTicks();
		
		//System.out.println("da");
		
		bufferbuilder.pos(minX - d0 - 0.001, minY - d1 - 0.001, minZ - d2 - 0.001).color(red, green, blue, 0.0F).endVertex();
		bufferbuilder.pos(maxX - d0 - 0.001, maxY - d1 - 0.001, maxZ - d2 - 0.001).color(red, green, blue, 1.0F).endVertex();
		bufferbuilder.pos(minX - d0 - 0.001, minY - d1 - 0.001, minZ - d2 - 0.001).color(red, green, blue, 0.0F).endVertex();
	}
	
	public static void distence(Vec3d pos, Vec3d pos2) {
		
		double d = Math.sqrt( Math.pow((pos2.x-pos.x),2) 
				+ Math.pow((pos2.y-pos.y),2) + Math.pow((pos2.z-pos.z),2));
	}
}
