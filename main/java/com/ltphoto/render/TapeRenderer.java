package com.ltphoto.render;

import com.ltphoto.items.TapeMessure;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TapeRenderer {
	
	@SubscribeEvent
	public void render(RenderWorldLastEvent event) {
		
		EntityPlayer player = Minecraft.getMinecraft().player;
		String item = "ltphoto:tapemessure";
		String mainItem = player.getHeldItemMainhand().getItem().getRegistryName().toString();
		TapeMessure tape = new TapeMessure();
		if (item.equals(mainItem)) {
			tape = (TapeMessure) player.getHeldItemMainhand().getItem();
		}
		
		if (tape.a != null) {
			//player.sendStatusMessage(new TextComponentString(t), true);
			
			double maxY = tape.select.maxY;
			double maxX = tape.select.maxX;
			double maxZ = tape.select.maxZ;
			
			double minY = tape.select.minY;
			double minX = tape.select.minX;
			double minZ = tape.select.minZ;
			
			double maxY_2 = tape.select_2.maxY;
			double maxX_2 = tape.select_2.maxX;
			double maxZ_2 = tape.select_2.maxZ;
			
			double minY_2 = tape.select_2.minY;
			double minX_2 = tape.select_2.minX;
			double minZ_2 = tape.select_2.minZ;
			
			double lineMaxX = tape.select.centerX;
			double lineMaxY = tape.select.centerY;
			double lineMaxZ = tape.select.centerZ;
			
			double lineMinX = tape.select_2.centerX;
			double lineMinY = tape.select_2.centerY;
			double lineMinZ = tape.select_2.centerZ;
			
			double d0 = player.lastTickPosX + (player.posX - player.lastTickPosX) * event.getPartialTicks();
			double d1 = player.lastTickPosY + (player.posY - player.lastTickPosY) * event.getPartialTicks();
			double d2 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * event.getPartialTicks();
			
			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
			GlStateManager.glLineWidth(2.0F);
			GlStateManager.disableTexture2D();
			GlStateManager.depthMask(false);
			GlStateManager.disableDepth();
			
			drawBoundingBox(minX - d0 - 0.001, minY - d1 - 0.001, minZ - d2 - 0.001, maxX + 0.001 - d0, maxY - d1 + 0.001, maxZ - d2 + 0.001, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
			drawBoundingBox(minX_2 - d0 - 0.001, minY_2 - d1 - 0.001, minZ_2 - d2 - 0.001, maxX_2 + 0.001 - d0, maxY_2 - d1 + 0.001, maxZ_2 - d2 + 0.001, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
			drawLine(lineMinX - d0 - 0.001, lineMinY - d1 - 0.001, lineMinZ - d2 - 0.001, lineMaxX + 0.001 - d0, lineMaxY - d1 + 0.001, lineMaxZ - d2 + 0.001, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
			
			GlStateManager.enableDepth();
			GlStateManager.depthMask(true);
			GlStateManager.enableTexture2D();
			GlStateManager.disableBlend();
		}
	}
	
	public static void drawBoundingBox(double minX, double minY, double minZ, double maxX, double maxY, double maxZ, float red, float green, float blue, float alpha) {
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
		drawBoundingBox(bufferbuilder, minX, minY, minZ, maxX, maxY, maxZ, red, green, blue, alpha);
		tessellator.draw();
	}
	
	public static void drawLine(double minX, double minY, double minZ, double maxX, double maxY, double maxZ, float red, float green, float blue, float alpha) {
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
		bufferbuilder.pos(minX, minY, minZ).color(red, green, blue, alpha).endVertex();
		bufferbuilder.pos(maxX, maxY, maxZ).color(red, green, blue, alpha).endVertex();
		tessellator.draw();
	}
	
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
	
}
