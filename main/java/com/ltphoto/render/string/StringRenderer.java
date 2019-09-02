package com.ltphoto.render.string;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;

public class StringRenderer {
	
	public static void drawString(Vec3d start, RenderWorldLastEvent event, float red, float green, float blue, float alpha) {
		EntityPlayer player = Minecraft.getMinecraft().player;
		
		Alphabet alphabet = new Alphabet('A');
		
		double d0 = player.lastTickPosX + (player.posX - player.lastTickPosX) * event.getPartialTicks();
		double d1 = player.lastTickPosY + (player.posY - player.lastTickPosY) * event.getPartialTicks();
		double d2 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * event.getPartialTicks();
		
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		GlStateManager.glLineWidth(2.0F);
		GlStateManager.disableTexture2D();
		GlStateManager.depthMask(false);
		GlStateManager.disableDepth();
		
		for (int y = 0; y < 5; y++) {
			
			double minX = start.x + alphabet.charaters().get(y).x;
			double minY = start.y + alphabet.charaters().get(y).y;
			double minZ = start.z + alphabet.charaters().get(y).z;
			
			System.out.println(minX + " " + minY + " " + minZ);
			bufferbuilder.pos(minX - d0 - 0.001, minY - d1 - 0.001, minZ - d2 - 0.001).color(red, green, blue, alpha).endVertex();
			//bufferbuilder.pos(maxX + 0.001 - d0, maxY - d1 + 0.001, maxZ - d2 + 0.001).color(red, green, blue, alpha).endVertex();
		}
		tessellator.draw();
		GlStateManager.depthMask(true);
		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();
		GlStateManager.enableDepth();
	}
}
