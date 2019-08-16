package com.ltphoto.render;

import com.ltphoto.items.TapeMessure;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TapeRenderer {
	
	@SubscribeEvent
	public void render(RenderWorldLastEvent event) {
		
		EntityPlayer player = Minecraft.getMinecraft().player;
		String item = "ltphoto:tapemessure";
		String mainItem = player.getHeldItemMainhand().getItem().getRegistryName().toString();
		TapeMessure tape = new TapeMessure();
		//Gets the data from the Tape Measure the player is holding
		if (item.equals(mainItem)) {
			tape = (TapeMessure) player.getHeldItemMainhand().getItem();
		}
		
		if (tape.a != null && tape.b != null) {
			String t = String.valueOf(tape.minX + " " + tape.minY + " " + tape.minZ + " " + tape.maxX + " " + tape.maxY + " " + tape.maxZ);
			player.sendStatusMessage(new TextComponentString(t), true);
			double minY = tape.minY;
			double minX = tape.minX;
			double minZ = tape.minZ;
			
			double maxY = tape.maxY;
			double maxX = tape.maxX;
			double maxZ = tape.maxZ;
			
			double d0 = player.lastTickPosX + (player.posX - player.lastTickPosX) * event.getPartialTicks();
			double d1 = player.lastTickPosY + (player.posY - player.lastTickPosY) * event.getPartialTicks();
			double d2 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * event.getPartialTicks();
			
			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
			GlStateManager.glLineWidth(2.0F);
			GlStateManager.disableTexture2D();
			GlStateManager.depthMask(false);
			GlStateManager.disableDepth();
			
			RenderGlobal.drawBoundingBox(minX - d0 - 0.001, minY - d1 - 0.001, minZ - d2 - 0.001, maxX + 0.001 - d0, maxY - d1 + 0.001, maxZ - d2 + 0.001, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
			
			GlStateManager.enableDepth();
			GlStateManager.depthMask(true);
			GlStateManager.enableTexture2D();
			GlStateManager.disableBlend();
		}
	}
	
}
