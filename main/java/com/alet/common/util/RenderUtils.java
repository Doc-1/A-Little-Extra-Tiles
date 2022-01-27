package com.alet.common.util;

import javax.vecmath.Point2i;

import org.lwjgl.opengl.GL11;

import com.creativemd.creativecore.common.utils.mc.ColorUtils;
import com.sun.istack.internal.Nullable;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

public class RenderUtils {
	
	public static void drawCubicBezier(Point2i p0, Point2i p1, Point2i p2, Point2i p3) {
		Point2i pFinal = new Point2i();
		int color = ColorUtils.BLACK;
		double t = 0;
		float f = (color >> 24 & 255) / 255.0F;
		float f1 = (color >> 16 & 255) / 255.0F;
		float f2 = (color >> 8 & 255) / 255.0F;
		float f3 = (color & 255) / 255.0F;
		
		GlStateManager.disableTexture2D();
		GlStateManager.enableBlend();
		GlStateManager.disableAlpha();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		GlStateManager.pushMatrix();
		GlStateManager.glLineWidth(6F);
		GlStateManager.scale(0.05, 0.05, 1);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		bufferbuilder.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION_COLOR);
		for (int i = 0; i <= 50; i++) {
			t = i / 50D;
			pFinal = cubicBezier(p0, p1, p2, p3, t, pFinal);
			bufferbuilder.pos(pFinal.x, pFinal.y, 0).color(f1, f2, f3, f).endVertex();
		}
		
		tessellator.draw();
		
		GlStateManager.glLineWidth(2F);
		bufferbuilder.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION_COLOR);
		for (int i = 0; i <= 50; i++) {
			t = i / 50D;
			pFinal = cubicBezier(p0, p1, p2, p3, t, pFinal);
			bufferbuilder.pos(pFinal.x, pFinal.y, 0).color(100, 100, 100, f).endVertex();
		}
		
		tessellator.draw();
		GlStateManager.popMatrix();
		GlStateManager.disableBlend();
		GlStateManager.enableAlpha();
		GlStateManager.enableTexture2D();
		/*
		float f = (color >> 24 & 255) / 255.0F;
		float f1 = (color >> 16 & 255) / 255.0F;
		float f2 = (color >> 8 & 255) / 255.0F;
		float f3 = (color & 255) / 255.0F;
		GlStateManager.disableTexture2D();
		GlStateManager.enableBlend();
		GlStateManager.disableAlpha();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		GlStateManager.shadeModel(7425);
		GlStateManager.pushMatrix();
		GlStateManager.glLineWidth(4F);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		bufferbuilder.begin(5, DefaultVertexFormats.POSITION_COLOR);
		for (int i = 0; i < 25; i++) {
			t = i / 25;
			pFinal = cubicBezier(p0, p1, p2, p3, t, pFinal);
			
		}
		bufferbuilder.pos(0, 10, 0).color(f1, f2, f3, f).endVertex();
		bufferbuilder.pos(0, 20, 0).color(f1, f2, f3, f).endVertex();
		tessellator.draw();
		GlStateManager.popMatrix();
		GlStateManager.shadeModel(7424);
		GlStateManager.disableBlend();
		GlStateManager.enableAlpha();
		GlStateManager.enableTexture2D();
		*/
	}
	
	public static Point2i cubicBezier(Point2i p0, Point2i p1, Point2i p2, Point2i p3, double t, @Nullable Point2i pFinal) {
		if (pFinal.equals(null))
			pFinal = new Point2i();
		pFinal.x = (int) (Math.pow(1 - t, 3) * p0.x + Math.pow(1 - t, 2) * 3 * t * p1.x + (1 - t) * 3 * t * t * p2.x
		        + t * t * t * p3.x);
		pFinal.y = (int) (Math.pow(1 - t, 3) * p0.y + Math.pow(1 - t, 2) * 3 * t * p1.y + (1 - t) * 3 * t * t * p2.y
		        + t * t * t * p3.y);
		return pFinal;
	}
}
