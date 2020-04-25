package com.ltphoto.render.tapemeasure.shape;

import com.ltphoto.items.ItemTapeMeasure;
import com.ltphoto.render.tapemeasure.TapeRenderer;

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
		bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
		
		drawBoundingBox(bufferbuilder, minX - d0 - 0.001, minY - d1 - 0.001, minZ - d2 - 0.001, maxX + 0.001 - d0, maxY - d1 + 0.001, maxZ - d2 + 0.001, red, green, blue, alpha);
		tessellator.draw();
	}
	
	
	public static void drawBox(double centerX_1, double centerX_2, double centerY_1, double centerY_2, double centerZ_1, double centerZ_2, ItemTapeMeasure tape) {
		
		int selcMeas = tape.selectedMeasurement;
		
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		GlStateManager.glLineWidth(2.0F);
		GlStateManager.disableTexture2D();
		GlStateManager.depthMask(false);
		GlStateManager.disableDepth();
		
		
		
		if (centerX_1 < centerX_2 && centerY_1 > centerY_2 && centerZ_1 > centerZ_2) {
			drawBoundingBox(tape.select.corner_6, tape.select_2.corner_2, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
			drawBoundingBox(tape.select.corner_1, tape.select.corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
			drawBoundingBox(tape.select_2.corner_1, tape.select_2.corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
		} else if (centerX_1 > centerX_2 && centerY_1 < centerY_2 && centerZ_1 > centerZ_2) {
			drawBoundingBox(tape.select.corner_3, tape.select_2.corner_7, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
			drawBoundingBox(tape.select.corner_1, tape.select.corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
			drawBoundingBox(tape.select_2.corner_1, tape.select_2.corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
		} else if (centerX_1 > centerX_2 && centerY_1 > centerY_2 && centerZ_1 > centerZ_2) {
			drawBoundingBox(tape.select.corner_5, tape.select_2.corner_1, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
			drawBoundingBox(tape.select.corner_1, tape.select.corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
			drawBoundingBox(tape.select_2.corner_1, tape.select_2.corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
		} else if (centerX_1 < centerX_2 && centerY_1 < centerY_2 && centerZ_1 > centerZ_2) {
			drawBoundingBox(tape.select.corner_4, tape.select_2.corner_8, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
			drawBoundingBox(tape.select.corner_1, tape.select.corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
			drawBoundingBox(tape.select_2.corner_1, tape.select_2.corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
		}
		if (centerX_1 < centerX_2 && centerY_1 > centerY_2 && centerZ_1 < centerZ_2) {
			drawBoundingBox(tape.select.corner_7, tape.select_2.corner_3, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
			drawBoundingBox(tape.select.corner_1, tape.select.corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
			drawBoundingBox(tape.select_2.corner_1, tape.select_2.corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
		} else if (centerX_1 > centerX_2 && centerY_1 < centerY_2 && centerZ_1 < centerZ_2) {
			drawBoundingBox(tape.select.corner_2, tape.select_2.corner_6, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
			drawBoundingBox(tape.select.corner_1, tape.select.corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
			drawBoundingBox(tape.select_2.corner_1, tape.select_2.corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
		} else if (centerX_1 > centerX_2 && centerY_1 > centerY_2 && centerZ_1 < centerZ_2) {
			drawBoundingBox(tape.select.corner_8, tape.select_2.corner_4, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
			drawBoundingBox(tape.select.corner_1, tape.select.corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
			drawBoundingBox(tape.select_2.corner_1, tape.select_2.corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
		} else if (centerX_1 < centerX_2 && centerY_1 < centerY_2 && centerZ_1 < centerZ_2) {
			drawBoundingBox(tape.select.corner_1, tape.select_2.corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
			drawBoundingBox(tape.select.corner_1, tape.select.corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
			drawBoundingBox(tape.select_2.corner_1, tape.select_2.corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
		}
		if (centerX_1 == centerX_2 && centerY_1 == centerY_2 && centerZ_1 == centerZ_2) {
			drawBoundingBox(tape.select.corner_1, tape.select_2.corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
			drawBoundingBox(tape.select.corner_1, tape.select.corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
			drawBoundingBox(tape.select_2.corner_1, tape.select_2.corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
		} else if (centerX_1 == centerX_2 && centerY_1 > centerY_2 && centerZ_1 == centerZ_2) {
			drawBoundingBox(tape.select.corner_5, tape.select_2.corner_1, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
			drawBoundingBox(tape.select.corner_1, tape.select.corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
			drawBoundingBox(tape.select_2.corner_1, tape.select_2.corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
		} else if (centerX_1 == centerX_2 && centerY_1 < centerY_2 && centerZ_1 == centerZ_2) {
			drawBoundingBox(tape.select.corner_1, tape.select_2.corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
			drawBoundingBox(tape.select.corner_1, tape.select.corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
			drawBoundingBox(tape.select_2.corner_1, tape.select_2.corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
		} else if (centerX_1 > centerX_2 && centerY_1 == centerY_2 && centerZ_1 == centerZ_2) {
			drawBoundingBox(tape.select.corner_5, tape.select_2.corner_1, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
			drawBoundingBox(tape.select.corner_1, tape.select.corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
			drawBoundingBox(tape.select_2.corner_1, tape.select_2.corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
		} else if (centerX_1 < centerX_2 && centerY_1 == centerY_2 && centerZ_1 == centerZ_2) {
			drawBoundingBox(tape.select.corner_1, tape.select_2.corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
			drawBoundingBox(tape.select.corner_1, tape.select.corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
			drawBoundingBox(tape.select_2.corner_1, tape.select_2.corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
		} else if (centerX_1 == centerX_2 && centerY_1 == centerY_2 && centerZ_1 < centerZ_2) {
			drawBoundingBox(tape.select.corner_1, tape.select_2.corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
			drawBoundingBox(tape.select.corner_1, tape.select.corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
			drawBoundingBox(tape.select_2.corner_1, tape.select_2.corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
		} else if (centerX_1 == centerX_2 && centerY_1 == centerY_2 && centerZ_1 > centerZ_2) {
			drawBoundingBox(tape.select.corner_5, tape.select_2.corner_1, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
			drawBoundingBox(tape.select.corner_1, tape.select.corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
			drawBoundingBox(tape.select_2.corner_1, tape.select_2.corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
		}
		if (centerX_1 == centerX_2 && centerY_1 > centerY_2 && centerZ_1 > centerZ_2) {
			drawBoundingBox(tape.select.corner_5, tape.select_2.corner_1, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
			drawBoundingBox(tape.select.corner_1, tape.select.corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
			drawBoundingBox(tape.select_2.corner_1, tape.select_2.corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
		} else if (centerX_1 == centerX_2 && centerY_1 < centerY_2 && centerZ_1 > centerZ_2) {
			drawBoundingBox(tape.select.corner_4, tape.select_2.corner_8, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
			drawBoundingBox(tape.select.corner_1, tape.select.corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
			drawBoundingBox(tape.select_2.corner_1, tape.select_2.corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
		} else if (centerX_1 == centerX_2 && centerY_1 > centerY_2 && centerZ_1 < centerZ_2) {
			drawBoundingBox(tape.select.corner_8, tape.select_2.corner_4, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
			drawBoundingBox(tape.select.corner_1, tape.select.corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
			drawBoundingBox(tape.select_2.corner_1, tape.select_2.corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
		} else if (centerX_1 == centerX_2 && centerY_1 < centerY_2 && centerZ_1 < centerZ_2) {
			drawBoundingBox(tape.select.corner_1, tape.select_2.corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
			drawBoundingBox(tape.select.corner_1, tape.select.corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
			drawBoundingBox(tape.select_2.corner_1, tape.select_2.corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
		}
		if (centerX_1 > centerX_2 && centerY_1 > centerY_2 && centerZ_1 == centerZ_2) {
			drawBoundingBox(tape.select.corner_5, tape.select_2.corner_1, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
			drawBoundingBox(tape.select.corner_1, tape.select.corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
			drawBoundingBox(tape.select_2.corner_1, tape.select_2.corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
		} else if (centerX_1 > centerX_2 && centerY_1 < centerY_2 && centerZ_1 == centerZ_2) {
			drawBoundingBox(tape.select.corner_2, tape.select_2.corner_6, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
			drawBoundingBox(tape.select.corner_1, tape.select.corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
			drawBoundingBox(tape.select_2.corner_1, tape.select_2.corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
		} else if (centerX_1 < centerX_2 && centerY_1 > centerY_2 && centerZ_1 == centerZ_2) {
			drawBoundingBox(tape.select.corner_7, tape.select_2.corner_3, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
			drawBoundingBox(tape.select.corner_1, tape.select.corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
			drawBoundingBox(tape.select_2.corner_1, tape.select_2.corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
		} else if (centerX_1 < centerX_2 && centerY_1 < centerY_2 && centerZ_1 == centerZ_2) {
			drawBoundingBox(tape.select.corner_1, tape.select_2.corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
			drawBoundingBox(tape.select.corner_1, tape.select.corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
			drawBoundingBox(tape.select_2.corner_1, tape.select_2.corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
		}
		if (centerX_1 > centerX_2 && centerY_1 == centerY_2 && centerZ_1 < centerZ_2) {
			drawBoundingBox(tape.select.corner_8, tape.select_2.corner_4, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
			drawBoundingBox(tape.select.corner_1, tape.select.corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
			drawBoundingBox(tape.select_2.corner_1, tape.select_2.corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
		} else if (centerX_1 > centerX_2 && centerY_1 == centerY_2 && centerZ_1 > centerZ_2) {
			drawBoundingBox(tape.select.corner_3, tape.select_2.corner_7, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
			drawBoundingBox(tape.select.corner_1, tape.select.corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
			drawBoundingBox(tape.select_2.corner_1, tape.select_2.corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
		} else if (centerX_1 < centerX_2 && centerY_1 == centerY_2 && centerZ_1 > centerZ_2) {
			drawBoundingBox(tape.select.corner_6, tape.select_2.corner_2, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
			drawBoundingBox(tape.select.corner_1, tape.select.corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
			drawBoundingBox(tape.select_2.corner_1, tape.select_2.corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
		} else if (centerX_1 < centerX_2 && centerY_1 == centerY_2 && centerZ_1 < centerZ_2) {
			drawBoundingBox(tape.select.corner_1, tape.select_2.corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
			drawBoundingBox(tape.select.corner_1, tape.select.corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
			drawBoundingBox(tape.select_2.corner_1, tape.select_2.corner_5, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
		}

	}
}
