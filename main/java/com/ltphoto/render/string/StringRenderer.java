package com.ltphoto.render.string;

import com.ltphoto.render.string.alphabet.Alphabet;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;

public class StringRenderer {
	
	public static void drawString(Vec3d start, RenderWorldLastEvent event, float red, float green, float blue, float alpha) {
		
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
		
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		GlStateManager.glLineWidth(2.0F);
		GlStateManager.disableTexture2D();
		GlStateManager.depthMask(false);
		GlStateManager.disableDepth();
		
		for (int i = 0; i < 6; i++) {
			
			char[] c = { 'B', 'L', 'O', 'C', 'K', ' ' };
			
			double x = start.x;
			double y = start.y;
			double z = start.z;
			if (i != 0) {
				z = start.z + 0.065;
			}
			start = new Vec3d(x, y, z);
			Alphabet alphabet = new Alphabet(bufferbuilder, event);
			alphabet.character(c[i], start, red, green, blue, alpha);
		}
		
		tessellator.draw();
		GlStateManager.depthMask(true);
		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();
		GlStateManager.enableDepth();
	}
}
