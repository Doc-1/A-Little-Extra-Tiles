package com.ltphoto.render;

import com.creativemd.littletiles.common.item.ItemMultiTiles;
import com.creativemd.littletiles.common.util.grid.LittleGridContext;
import com.ltphoto.items.ItemTapeMeasure;
import com.ltphoto.render.string.DrawCharacter.Facing;
import com.ltphoto.render.string.StringRenderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TapeRenderer {
	
	public static RenderWorldLastEvent event;
	public static ItemTapeMeasure tape = new ItemTapeMeasure();

	public double radius(double pos_1, double pos_2) {
		LittleGridContext context = LittleGridContext.get(16);
		
		double contDecimal = (1D / context.size);
		double distence = (((Math.abs(pos_1 - pos_2)) + contDecimal) * context.size) - 1D;
		
		return (distence/context.size) * 10D;
	}
	
	public static void clear() {
		tape.a = null;
		tape.b = null;
	}
	
	public String distence(double pos_1, double pos_2) {
		LittleGridContext context = LittleGridContext.get(ItemMultiTiles.currentContext.size);
		
		double contDecimal = 1D / context.size;
		double distence = (Math.abs(pos_1 - pos_2)) + contDecimal;
		int denominator = context.size;
		String[] dis = String.valueOf(distence).split("\\.");
		double numerator = context.size * Double.parseDouble("0." + dis[1]);
		
		if((int)(numerator)==0) {
			return dis[0] + " BLOCK";
		}else if(Integer.parseInt(dis[0])==0){
			return (int) (numerator) + "/" + denominator + " TILE";
		}else {
			return dis[0] + " BLOCK " + (int) (numerator) + "/" + denominator + " TILE";
		}
	} 
	
	@SubscribeEvent
	public void render(RenderWorldLastEvent event) {
		this.event = event;
		EntityPlayer player = Minecraft.getMinecraft().player;
		String item = "ltphoto:tapemeasure";
		
		String mainItem = player.getHeldItemMainhand().getItem().getRegistryName().toString();
		
		
		if (tape.a != null && tape.b != null) {
			//player.sendStatusMessage(new TextComponentString(t), true);
			
			double d0 = player.lastTickPosX + (player.posX - player.lastTickPosX) * event.getPartialTicks();
			double d1 = player.lastTickPosY + (player.posY - player.lastTickPosY) * event.getPartialTicks();
			double d2 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * event.getPartialTicks();
			
			double centerX_1 = tape.select.centerX;
			double centerY_1 = tape.select.centerY;
			double centerZ_1 = tape.select.centerZ;
			
			double centerX_2 = tape.select_2.centerX;
			double centerY_2 = tape.select_2.centerY;
			double centerZ_2 = tape.select_2.centerZ;
			
			
			Vec3d middleZ = new Vec3d(tape.select.boxCorner_1.x, 
					tape.select.boxCorner_1.y, 
					((centerZ_1 + centerZ_2)/2)-(1/2D*0.0655));
			
			Vec3d middleX = new Vec3d(((centerX_1 + centerX_2)/2)-(1/2D)*0.0655,
					tape.select.boxCorner_1.y, 
					tape.select.boxCorner_1.z-0.05);
			
			Vec3d middleY = new Vec3d(tape.select.boxCorner_1.x, 
					((centerY_1 + centerY_2)/2)-(1/2D)*0.0655, 
					tape.select.boxCorner_2.z);
			
			StringRenderer.drawString(middleZ, "Z", Facing.UP ,event, 0.0F, 1.0F, 0F, 1.0F);
			//StringRenderer.drawString(tape.select_2.boxCorner_1, distence(centerZ_1, centerZ_2), event, 0.0F, 1.0F, 0F, 1.0F);
			StringRenderer.drawString(middleX, "X", Facing.WEST, event, 0.0F, 1.0F, 0F, 1.0F);
			StringRenderer.drawString(middleY, "Y", Facing.EAST, event, 0.0F, 1.0F, 0F, 1.0F);

			//String r = Double.toString(distence(centerZ_1, centerZ_2).length()/100D);
			//String t = Double.toString(radius(centerX_1, centerX_2));
			//player.sendStatusMessage(new TextComponentString(r), true);

			player.sendStatusMessage(new TextComponentString("X: " + distence(centerX_1, centerX_2)+ " Y: " + distence(centerY_1, centerY_2) + " Z: " + distence(centerZ_1, centerZ_2)), true);
			
			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
			GlStateManager.glLineWidth(2.0F);
			GlStateManager.disableTexture2D();
			GlStateManager.depthMask(false);
			GlStateManager.disableDepth();
			
			/* 0 0 - x<x2 y>y2
			 * 1 -1 /
			 * 
			 * 0 0 - x>x2 y<y2
			 * -1 1 /
			 * 
			 * 0 0 - x>x2 y>y2
			 * -1 -1 /
			 * 
			 * 0 0 - x<x2 y<y2
			 * 1 1 / */
			
			
			//drawLine(centerX_1, centerY_1, centerZ_1, centerX_2, centerY_2, centerZ_2, 1.0F, 1.0F, 1.0F, 1.0F);
			
			
			//drawCircle(centerX_1, centerY_1, centerZ_1, radius(centerX_1, centerX_2), 1.0F, 1.0F, 1.0F, 1.0F);

			
			
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
			
			
			//drawCircle(centerX_1, centerY_1, centerZ_1, centerX_2, centerY_2, centerZ_2, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
			GlStateManager.enableDepth();
			GlStateManager.depthMask(true);
			GlStateManager.enableTexture2D();
			GlStateManager.disableBlend();
		}else if (item.equals(mainItem)) {
			tape = (ItemTapeMeasure) player.getHeldItemMainhand().getItem();
		}
	}
	
	public static void drawBoundingBox(Vec3d vec_1, Vec3d vec_2, float red, float green, float blue, float alpha) {
		EntityPlayer player = Minecraft.getMinecraft().player;
		
		double d0 = player.lastTickPosX + (player.posX - player.lastTickPosX) * event.getPartialTicks();
		double d1 = player.lastTickPosY + (player.posY - player.lastTickPosY) * event.getPartialTicks();
		double d2 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * event.getPartialTicks();
		
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
	
	public static void drawLine(double minX, double minY, double minZ, double maxX, double maxY, double maxZ, float red, float green, float blue, float alpha) {
		EntityPlayer player = Minecraft.getMinecraft().player;
		
		double d0 = player.lastTickPosX + (player.posX - player.lastTickPosX) * event.getPartialTicks();
		double d1 = player.lastTickPosY + (player.posY - player.lastTickPosY) * event.getPartialTicks();
		double d2 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * event.getPartialTicks();
		
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
		bufferbuilder.pos(minX - d0 - 0.001, minY - d1 - 0.001, minZ - d2 - 0.001).color(red, green, blue, alpha).endVertex();
		bufferbuilder.pos(maxX - d0 - 0.001, maxY - d1 - 0.001, maxZ - d2 - 0.001).color(red, green, blue, alpha).endVertex();
		tessellator.draw();
	}
	
	/*Experiment*/
	public static void drawCircle(double minX, double minY, double minZ, double radius, float red, float green, float blue, float alpha) {
		EntityPlayer player = Minecraft.getMinecraft().player;
		
		double d0 = player.lastTickPosX + (player.posX - player.lastTickPosX) * event.getPartialTicks();
		double d1 = player.lastTickPosY + (player.posY - player.lastTickPosY) * event.getPartialTicks();
		double d2 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * event.getPartialTicks();
		
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
	
	private static double cleanDouble(double doub) {
		String clean = String.format("%.4f", doub);
		doub = Double.parseDouble(clean);
		return doub;
	}
	
}
//drawLine(tape.select.corner_1.x, tape.select.corner_1.y, tape.select.corner_1.z, tape.select.corner_4.x, tape.select.corner_1.y, tape.select_2.corner_4.z, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
//drawLine(tape.select_2.corner_6.x, tape.select_2.corner_5.y, tape.select.corner_6.z, tape.select_2.corner_5.x, tape.select_2.corner_5.y, tape.select_2.corner_5.z, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
