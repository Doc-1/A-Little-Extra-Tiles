package com.ltphoto.render.string;

import com.ltphoto.render.string.alphabet.Alphabet;
import com.ltphoto.render.string.numbers.Numbers;
import com.ltphoto.render.string.specialcharacters.SpecialChar;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;

public class StringRenderer {
	
	public static void drawString(Vec3d start, String str, RenderWorldLastEvent event, float red, float green, float blue, float alpha) {
		
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
		
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		GlStateManager.glLineWidth(2.0F);
		GlStateManager.disableTexture2D();
		GlStateManager.depthMask(false);
		GlStateManager.disableDepth();
		
		char[] c = str.toCharArray();
		for (int i = 0; i < c.length; i++) {
			
			double x = start.x;
			double y = start.y;
			double z = start.z;
			
			if (i != 0) {
				z = start.z + 0.065;
			}
			
			start = new Vec3d(x, y, z);
			
			if (c[i] >= 'A' && c[i] <= 'Z') {
				Alphabet alphabet = new Alphabet(bufferbuilder, event);
				alphabet.character(c[i], start, red, green, blue, alpha);
			} else if (c[i] >= '0' && c[i] <= '9') {
				Numbers number = new Numbers(bufferbuilder, event);
				number.character(c[i], start, red, green, blue, alpha);
			} else {
				SpecialChar specialChar = new SpecialChar(bufferbuilder, event);
				specialChar.character(c[i], start, red, green, blue, alpha);
			}
			
		}
		
		tessellator.draw();
		GlStateManager.depthMask(true);
		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();
		GlStateManager.enableDepth();
	}
}
