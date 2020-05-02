package com.alet.render.tapemeasure;

import com.alet.ALET;
import com.alet.render.tapemeasure.shape.Line;
import com.creativemd.littletiles.common.util.ingredient.LittleInventory;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MeasurementRender {
	
	@SuppressWarnings("unused")
	@SubscribeEvent
	public void render(RenderGameOverlayEvent.Post event) {
		
		EntityPlayer player = Minecraft.getMinecraft().player;
		
		ItemStack stack = ItemStack.EMPTY;
		ItemStack ingredient = new ItemStack(ALET.tapeMeasure, 1);
		LittleInventory inventory = new LittleInventory(player);
		
		for(int i = 0; i < inventory.size(); i++) {
			stack = inventory.get(i);
			if(stack.toString().equals(ingredient.toString())) 
				break;
			else {
				stack = ItemStack.EMPTY;
				break;
			}
		}
		if(stack.hasTagCompound())
			if(!stack.getTagCompound().hasNoTags()) {
				NBTTagCompound nbt = stack.getTagCompound();
				Minecraft mc = Minecraft.getMinecraft();
				ScaledResolution res = new ScaledResolution(mc);
				FontRenderer fontRender = mc.fontRenderer;
				fontRender.FONT_HEIGHT = 1;
				int width = res.getScaledWidth();
				int height = res.getScaledHeight();
				mc.entityRenderer.setupOverlayRendering();
				int x = width-150;
				int y = height/12;
				int color = 0xFFFFFF;
				String distence1 = Line.distence(new Vec3d(nbt.getDouble("x0"),nbt.getDouble("y0"),nbt.getDouble("z0")), 
						new Vec3d(nbt.getDouble("x1"),nbt.getDouble("y1"),nbt.getDouble("z1")));
				fontRender.drawStringWithShadow("Measurement 1: ", x, y, color);
				fontRender.drawStringWithShadow(distence1, x, y+10, 0xFF0000);
				
				String distence2 = Line.distence(new Vec3d(nbt.getDouble("x2"),nbt.getDouble("y2"),nbt.getDouble("z2")), 
						new Vec3d(nbt.getDouble("x3"),nbt.getDouble("y3"),nbt.getDouble("z3")));
				fontRender.drawStringWithShadow("Measurement 2: ", x, y+20, color);
				fontRender.drawStringWithShadow(distence2, x, y+30, 0x00FF00);
			}		
	}

}
