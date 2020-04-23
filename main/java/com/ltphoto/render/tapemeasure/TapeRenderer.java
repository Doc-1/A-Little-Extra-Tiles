package com.ltphoto.render.tapemeasure;

import com.creativemd.littletiles.common.action.LittleAction;
import com.creativemd.littletiles.common.item.ItemMultiTiles;
import com.creativemd.littletiles.common.util.grid.LittleGridContext;
import com.creativemd.littletiles.common.util.ingredient.LittleIngredients;
import com.creativemd.littletiles.common.util.ingredient.LittleInventory;
import com.creativemd.littletiles.common.util.ingredient.NotEnoughIngredientsException;
import com.creativemd.littletiles.common.util.ingredient.StackIngredient;
import com.creativemd.littletiles.common.util.ingredient.StackIngredientEntry;
import com.ltphoto.LTPhoto;
import com.ltphoto.items.ItemTapeMeasure;
import com.ltphoto.render.string.DrawCharacter.Facing;
import com.ltphoto.render.string.StringRenderer;
import com.ltphoto.render.string.StringRenderer.Middle;
import com.ltphoto.render.tapemeasure.shape.Box;
import com.ltphoto.render.tapemeasure.shape.Circle;
import com.ltphoto.render.tapemeasure.shape.Line;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TapeRenderer {
	
	public static RenderWorldLastEvent event;
	public static ItemTapeMeasure tape = new ItemTapeMeasure();

	public static void clear() {
		tape.firstPos = null;
		tape.secondPos = null;
	}
	
	public double radius(double pos_1, double pos_2) {
		LittleGridContext context = LittleGridContext.get(16);
		
		double contDecimal = (1D / context.size);
		double distence = (((Math.abs(pos_1 - pos_2)) + contDecimal) * context.size) - 1D;
		
		return (distence/context.size) * 10D;
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
	
	@SuppressWarnings("unused")
	@SubscribeEvent
	public void render(RenderWorldLastEvent event) {
		this.event = event;
		EntityPlayer player = Minecraft.getMinecraft().player;
		
		ItemStack stack = ItemStack.EMPTY;
		ItemStack ingredient = new ItemStack(LTPhoto.tapeMeasure, 1);
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
		
		if (tape.firstPos != null && tape.secondPos != null) {
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
			
			player.sendStatusMessage(new TextComponentString("X: " + distence(centerX_1, centerX_2)+ " Y: " + distence(centerY_1, centerY_2) + " Z: " + distence(centerZ_1, centerZ_2)), true);
			
			StringRenderer.drawString(Middle.Z, tape, "Z", Facing.UP ,0.0F, 1.0F, 0F, 1.0F);
			StringRenderer.drawString(Middle.X, tape, "X", Facing.WEST, 0.0F, 1.0F, 0F, 1.0F);
			StringRenderer.drawString(Middle.Y, tape, "Y", Facing.EAST, 0.0F, 1.0F, 0F, 1.0F);
			
			//Line.drawLine(centerX_1, centerY_1, centerZ_1, centerX_2, centerY_2, centerZ_2, 1.0F, 1.0F, 1.0F, 1.0F);
			//Box.drawBox(centerX_1, centerX_2, centerY_1, centerY_2, centerZ_1, centerZ_2, tape);
			//Box.drawBox(centerX_1, centerX_2, centerY_1, centerY_2, centerZ_1, centerZ_2, tape);

			//Circle.drawCircle(centerX_1, centerY_1, centerZ_1, radius(centerX_1, centerX_2), 1.0F, 1.0F, 1.0F, 1.0F);
			//Circle.drawCircle(centerX_1, centerY_1, centerZ_1, radius(centerX_1, centerX_2), 1.0F, 1.0F, 1.0F, 1.0F);

			GlStateManager.enableDepth();
			GlStateManager.depthMask(true);
			GlStateManager.enableTexture2D();
			GlStateManager.disableBlend();
		}else if(!stack.isEmpty()){
			tape = (ItemTapeMeasure) stack.getItem();
		}
	}
}
//drawLine(tape.select.corner_1.x, tape.select.corner_1.y, tape.select.corner_1.z, tape.select.corner_4.x, tape.select.corner_1.y, tape.select_2.corner_4.z, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
//drawLine(tape.select_2.corner_6.x, tape.select_2.corner_5.y, tape.select.corner_6.z, tape.select_2.corner_5.x, tape.select_2.corner_5.y, tape.select_2.corner_5.z, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
