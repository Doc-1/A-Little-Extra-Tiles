package com.alet.render.tapemeasure;

import java.util.ArrayList;
import java.util.List;

import com.alet.ALET;
import com.alet.items.ItemTapeMeasure;
import com.alet.render.string.StringRenderer;
import com.alet.render.string.DrawCharacter.Facing;
import com.alet.render.string.StringRenderer.Middle;
import com.alet.render.tapemeasure.shape.Box;
import com.alet.render.tapemeasure.shape.Circle;
import com.alet.render.tapemeasure.shape.Line;
import com.alet.tiles.SelectLittleTile;
import com.creativemd.littletiles.client.render.overlay.PreviewRenderer;
import com.creativemd.littletiles.common.action.LittleAction;
import com.creativemd.littletiles.common.item.ItemMultiTiles;
import com.creativemd.littletiles.common.tile.math.vec.LittleAbsoluteVec;
import com.creativemd.littletiles.common.util.grid.LittleGridContext;
import com.creativemd.littletiles.common.util.ingredient.LittleIngredients;
import com.creativemd.littletiles.common.util.ingredient.LittleInventory;
import com.creativemd.littletiles.common.util.ingredient.NotEnoughIngredientsException;
import com.creativemd.littletiles.common.util.ingredient.StackIngredient;
import com.creativemd.littletiles.common.util.ingredient.StackIngredientEntry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TapeRenderer {
	
	public static RenderWorldLastEvent event;
	public static int counter = 0;
	public static boolean inInv = false;
	
	public double radius(double pos_1, double pos_2) {
		LittleGridContext context = LittleGridContext.get(ItemMultiTiles.currentContext.size);
		
		double contDecimal = (1D / context.size);
		double distence = (((Math.abs(pos_1 - pos_2)) + contDecimal) * context.size) - 1D;
		
		return (distence/context.size);
	}
	
	public static double distence(double pos_1, double pos_2, int contextSize) {
		LittleGridContext context = LittleGridContext.get(contextSize);
		
		double contDecimal = 1D / context.size;
		double distence = (Math.abs(pos_1 - pos_2)) + contDecimal;
		
		return distence;
	} 
	
	@SubscribeEvent
	public void render(RenderWorldLastEvent event) {
		this.event = event;
		EntityPlayer player = Minecraft.getMinecraft().player;
		
		ItemStack stack = ItemStack.EMPTY;
		ItemStack ingredient = new ItemStack(ALET.tapeMeasure, 1);
		LittleInventory inventory = new LittleInventory(player);
		inInv = false;

		for(int i = 0; i < inventory.size(); i++) {
			if(inventory.get(i).getItem() instanceof ItemTapeMeasure) {
				stack = inventory.get(i);
				inInv = true;
				break;
			}
		}
		//player.sendStatusMessage(new TextComponentString(t), true);
		NBTTagCompound nbt = stack.getTagCompound();
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		try {
			if(!stack.isEmpty()) {
				if(stack.hasTagCompound()) 
					for(int i=0;i<4;i+=2) { 
						double d0 = player.lastTickPosX + (player.posX - player.lastTickPosX) * event.getPartialTicks();
						double d1 = player.lastTickPosY + (player.posY - player.lastTickPosY) * event.getPartialTicks();
						double d2 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * event.getPartialTicks();
				
						EnumFacing facingMin = EnumFacing.byName(nbt.getString("facing"+i));
						EnumFacing facingMax = EnumFacing.byName(nbt.getString("facing"+(i+1)));
						
						List<String> list = LittleGridContext.getNames();
						int contextSize = Integer.parseInt(list.get(nbt.getInteger("context"+i)));
						
						SelectLittleTile tilePosMin = new SelectLittleTile(new Vec3d(Double.parseDouble(nbt.getString("x"+i)), Double.parseDouble(nbt.getString("y"+i)),
								Double.parseDouble(nbt.getString("z"+i))),LittleGridContext.get(contextSize), facingMin);
						
						SelectLittleTile tilePosMax = new SelectLittleTile(new Vec3d(Double.parseDouble(nbt.getString("x"+(i+1))), Double.parseDouble(nbt.getString("y"+(i+1))),
								Double.parseDouble(nbt.getString("z"+(i+1)))),LittleGridContext.get(contextSize), facingMax);
						
						GlStateManager.enableBlend();
						GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
						GlStateManager.glLineWidth(2.0F);
						GlStateManager.disableTexture2D();
						GlStateManager.depthMask(false);
						GlStateManager.disableDepth();
						
						bufferbuilder.begin(2, DefaultVertexFormats.POSITION_COLOR);
				
						if(i==0) {
							Box.drawBox(tilePosMin, tilePosMax, nbt, contextSize, i);
				
						//	Box.drawBox(centerX_1, centerY_1, centerZ_1, contextSize, 1.0F, 0.0F, 0.0F, 1.0F);
						//	Box.drawBox(centerX_2, centerY_2, centerZ_2, contextSize, 1.0F, 0.0F, 0.0F, 1.0F);
						//	Line.drawLine(bufferbuilder, centerX_1, centerY_1, centerZ_1, centerX_2, centerY_2, centerZ_2, 1.0F, 0.0F, 0.0F, 1.0F);
						}
						if(i==2) {
							Box.drawBox(tilePosMin, contextSize, 0.0F, 1.0F, 0.0F, 1.0F);
							Box.drawBox(tilePosMax, contextSize, 0.0F, 1.0F, 0.0F, 1.0F);
							Line.drawLine(bufferbuilder, tilePosMin, tilePosMax, 0.0F, 1.0F, 0.0F, 1.0F);
				
							
						}
						
				
						tessellator.draw();
				
						GlStateManager.enableDepth();
						GlStateManager.depthMask(true);
						GlStateManager.enableTexture2D();
						GlStateManager.disableBlend();	
					
					}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
		/*
		for(int i=0;i<tape.measure.size();i+=2) {
			if(tape.measure.get(i) != null && tape.measure.get(i+1) != null) {
				//System.out.println(i);
				double d0 = player.lastTickPosX + (player.posX - player.lastTickPosX) * event.getPartialTicks();
				double d1 = player.lastTickPosY + (player.posY - player.lastTickPosY) * event.getPartialTicks();
				double d2 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * event.getPartialTicks();

				double centerX_1 = tape.measure.get(i).centerX;
				double centerY_1 = tape.measure.get(i).centerY;
				double centerZ_1 = tape.measure.get(i).centerZ;
				
				double centerX_2 = tape.measure.get(i+1).centerX;
				double centerY_2 = tape.measure.get(i+1).centerY;
				double centerZ_2 = tape.measure.get(i+1).centerZ;
				
				GlStateManager.enableBlend();
				GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
				GlStateManager.glLineWidth(2.0F);
				GlStateManager.disableTexture2D();
				GlStateManager.depthMask(false);
				GlStateManager.disableDepth();
				
				bufferbuilder.begin(2, DefaultVertexFormats.POSITION_COLOR);
				
				switch (tape.measure.get(i).shapeType) {
				case Box:
					player.sendStatusMessage(new TextComponentString("X: " + distence(centerX_1, centerX_2)+ " Y: " + distence(centerY_1, centerY_2) + " Z: " + distence(centerZ_1, centerZ_2)), true);
					Box.drawBox(centerX_1, centerX_2, centerY_1, centerY_2, centerZ_1, centerZ_2, tape, i);
					break;
				case Line:
					//player.sendStatusMessage(new TextComponentString(Line.distence(tape.measure.get(i).center, tape.measure.get(i+1).center)), true);
					if(i==0)
						Line.drawLine(bufferbuilder, centerX_1, centerY_1, centerZ_1, centerX_2, centerY_2, centerZ_2, 1.0F, 0.0F, 0.0F, 1.0F);
					if(i==2)
						Line.drawLine(bufferbuilder, centerX_1, centerY_1, centerZ_1, centerX_2, centerY_2, centerZ_2, 0.0F, 1.0F, 0.0F, 1.0F);
				
					//StringRenderer.drawString(Middle.T, i, tape, Line.distence(tape.measure.get(i).center, tape.measure.get(i+1).center), Facing.UP, 0.0F, 1.0F, 0F, 1.0F);

					break;
				case Circle:
					Circle.drawCircle(centerX_1, centerY_1, centerZ_1, radius(centerX_1, centerX_2), 1.0F, 1.0F, 1.0F, 1.0F);				
					break;
				default:
					break;
				}
				
				//StringRenderer.drawString(Middle.Z, i, tape, distence(centerZ_1, centerZ_2), Facing.UP ,0.0F, 1.0F, 0F, 1.0F);
				//StringRenderer.drawString(Middle.X, i, tape, distence(centerX_1, centerX_2), Facing.WEST, 0.0F, 1.0F, 0F, 1.0F);
				//StringRenderer.drawString(Middle.Y, i, tape, distence(centerY_1, centerY_2), Facing.UP, 0.0F, 1.0F, 0F, 1.0F);
				
				//StringRenderer.drawString(Middle.Y, i, tape, "Y", Facing.EAST, 0.0F, 1.0F, 0F, 1.0F);

				//System.out.println(bufferbuilder.getVertexCount());
				tessellator.draw();
				//System.out.println(bufferbuilder.getVertexCount());

				GlStateManager.enableDepth();
				GlStateManager.depthMask(true);
				GlStateManager.enableTexture2D();
				GlStateManager.disableBlend();	
			}
		}
*/
		//Box.drawBox(centerX_1, centerX_2, centerY_1, centerY_2, centerZ_1, centerZ_2, tape);
		//Box.drawBox(centerX_1, centerX_2, centerY_1, centerY_2, centerZ_1, centerZ_2, tape);

		//Circle.drawCircle(centerX_1, centerY_1, centerZ_1, radius(centerX_1, centerX_2), 1.0F, 1.0F, 1.0F, 1.0F);
		//Circle.drawCircle(centerX_1, centerY_1, centerZ_1, radius(centerX_1, centerX_2), 1.0F, 1.0F, 1.0F, 1.0F);
		
					
		
	}
}
