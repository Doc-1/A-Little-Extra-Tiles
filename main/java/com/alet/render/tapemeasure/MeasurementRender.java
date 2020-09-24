package com.alet.render.tapemeasure;

import java.util.List;

import org.lwjgl.opengl.GL11;

import com.alet.ALET;
import com.alet.items.ItemTapeMeasure;
import com.alet.render.tapemeasure.shape.Box;
import com.alet.render.tapemeasure.shape.Line;
import com.creativemd.littletiles.common.util.grid.LittleGridContext;
import com.creativemd.littletiles.common.util.ingredient.LittleInventory;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MeasurementRender {

	@SubscribeEvent
	public void render(RenderGameOverlayEvent.Post event) {
		
		EntityPlayer player = Minecraft.getMinecraft().player;
		
		ItemStack stack = ItemStack.EMPTY;
		ItemStack ingredient = new ItemStack(ALET.tapeMeasure, 1);
		LittleInventory inventory = new LittleInventory(player);
		
		for(int i = 0; i < inventory.size(); i++) {
			if(inventory.get(i).getItem() instanceof ItemTapeMeasure) {
				stack = inventory.get(i);
				break;
			}
		}
		if(!stack.isEmpty()) {
			NBTTagCompound nbt = stack.getTagCompound();
			Minecraft mc = Minecraft.getMinecraft();
			ScaledResolution res = new ScaledResolution(mc);
			FontRenderer fontRender = mc.fontRenderer;

			
			int width = res.getScaledWidth();
			int height = res.getScaledHeight();
			mc.entityRenderer.setupOverlayRendering();
			int x = 10;
			int y = 119;
			int color = 0xFFFFFF;
			List<String> list = LittleGridContext.getNames();

			
			GlStateManager.pushMatrix();
			
			GlStateManager.disableCull();
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glPushAttrib(GL11.GL_4_BYTES);
			GL11.glColor4d(0d, 0d, 0d, 0.12d);
			GL11.glRecti(3, 66 , 190, 300);
			GL11.glPopAttrib();
			GL11.glScalef(0.58F, 0.58F, 0.0F);
			GL11.glFlush();
			GlStateManager.enableCull();
			
			try {
				String distence1 = Box.distence(new Vec3d(Double.parseDouble(nbt.getString("x0")), Double.parseDouble(nbt.getString("y0")), 
						Double.parseDouble(nbt.getString("z0"))), new Vec3d(Double.parseDouble(nbt.getString("x1")), 
						Double.parseDouble(nbt.getString("y1")), Double.parseDouble(nbt.getString("z1"))), 
						Integer.parseInt(list.get(nbt.getInteger("context0"))));
				
				fontRender.drawStringWithShadow("Measurement 1: ", x, y, color);
				fontRender.drawStringWithShadow(distence1, x, y+10, 0xFF0000);
				
				String distence2 = Line.distence(new Vec3d(Double.parseDouble(nbt.getString("x2")), Double.parseDouble(nbt.getString("y2")), 
						Double.parseDouble(nbt.getString("z2"))), new Vec3d(Double.parseDouble(nbt.getString("x3")), 
						Double.parseDouble(nbt.getString("y3")), Double.parseDouble(nbt.getString("z3"))),
						Integer.parseInt(list.get(nbt.getInteger("context0"))));
				fontRender.drawStringWithShadow("Measurement 2: ", x, y+20, color);
				fontRender.drawStringWithShadow(distence2, x, y+30, 0x00FF00);
			} catch (Exception e) {
				// TODO: handle exception
			}
			GlStateManager.popMatrix();
		}		
	}

	public void getDrawDistence(NBTTagCompound nbt, FontRenderer font) {
		
	}
	
}
