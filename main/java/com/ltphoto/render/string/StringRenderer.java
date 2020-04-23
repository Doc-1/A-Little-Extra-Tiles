package com.ltphoto.render.string;

import com.ltphoto.items.ItemTapeMeasure;
import com.ltphoto.render.string.DrawCharacter.Facing;
import com.ltphoto.render.string.alphabet.Alphabet;
import com.ltphoto.render.string.numbers.Numbers;
import com.ltphoto.render.string.specialcharacters.SpecialChar;
import com.ltphoto.render.tapemeasure.TapeRenderer;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;

public class StringRenderer {
	
	public enum Middle {
		X,
		Y,
		Z
	}
	
	
	public static void drawString(Middle middle, ItemTapeMeasure tape, String str, Facing facing, float red, float green, float blue, float alpha) {
		Vec3d start = new Vec3d(0,0,0);
		
		switch (middle) {
		case X:
			start = new Vec3d(tape.select.boxCorner_1.x, 
					((tape.select.centerY + tape.select_2.centerY)/2)-(1/2D)*0.0655, 
					tape.select.boxCorner_2.z);
			break;
		case Y:
			start = new Vec3d(((tape.select.centerX + tape.select_2.centerX)/2)-(1/2D)*0.0655,
					tape.select.boxCorner_1.y, 
					tape.select.boxCorner_1.z-0.05);
			break;
		case Z:
			start = new Vec3d(tape.select.boxCorner_1.x, 
					tape.select.boxCorner_1.y, 
					((tape.select.centerZ + tape.select_2.centerZ)/2)-(1/2D*0.0655));
			break;
		default:
			break;
		}
		drawString(start, str, facing, red, green, blue, alpha);
	}
		
		
	public static void drawString(Vec3d start, String str, Facing facing, float red, float green, float blue, float alpha) {
		
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
			
			
			switch (facing) {
			case NORTH:
				
				break;
			case EAST:
				
				break;
			case SOUTH:
				
				break;
			case WEST:
				if (i != 0) {
					x = start.x + 0.065;
				}
				break;
			case UP:
				if (i != 0) {
					z = start.z + 0.065;
				}
				break;
			case DOWN:
				
				break;
			default:
				break;
			}
			
			start = new Vec3d(x, y, z);
			
			if (c[i] >= 'A' && c[i] <= 'Z') {
				Alphabet alphabet = new Alphabet(bufferbuilder, TapeRenderer.event, facing);
				alphabet.character(c[i], start, red, green, blue, alpha);
			} else if (c[i] >= '0' && c[i] <= '9') {
				Numbers number = new Numbers(bufferbuilder, TapeRenderer.event, facing);
				number.character(c[i], start, red, green, blue, alpha);
			} else {
				SpecialChar specialChar = new SpecialChar(bufferbuilder, TapeRenderer.event, facing);
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
