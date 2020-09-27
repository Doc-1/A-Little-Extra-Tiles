package com.alet.render.tapemeasure;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.lwjgl.util.Color;

import com.alet.ALET;
import com.alet.items.ItemTapeMeasure;
import com.alet.render.string.StringRenderer;
import com.alet.render.string.DrawCharacter.Facing;
import com.alet.render.string.StringRenderer.Middle;
import com.alet.render.tapemeasure.shape.Box;
import com.alet.render.tapemeasure.shape.Circle;
import com.alet.render.tapemeasure.shape.Line;
import com.alet.tiles.SelectLittleTile;
import com.creativemd.creativecore.common.utils.mc.ColorUtils;
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

		if(!stack.isEmpty())
			if(stack.hasTagCompound()) {
				double d0 = player.lastTickPosX + (player.posX - player.lastTickPosX) * event.getPartialTicks();
				double d1 = player.lastTickPosY + (player.posY - player.lastTickPosY) * event.getPartialTicks();
				double d2 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * event.getPartialTicks();
				List<String> list = LittleGridContext.getNames();

				List<Integer> allIndexes = new ArrayList<Integer>();
				String nbtStr = nbt.toString();

			    List<String> allMatches = new ArrayList<String>();
			    Matcher m = Pattern.compile("x\\d+").matcher(nbtStr);
			    while (m.find()) {
			      allMatches.add(m.group());
			    }
			    for (String string : allMatches) {
			    	int x = Integer.parseInt(string.split("x")[1]);
			    	if(x % 2 != 0) {
				    	allIndexes.add(x);
			    	}
				}
			    GlStateManager.enableBlend();
				GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
				GlStateManager.glLineWidth(2.0F);
				GlStateManager.disableTexture2D();
				GlStateManager.depthMask(false);
				GlStateManager.disableDepth();
				
				bufferbuilder.begin(2, DefaultVertexFormats.POSITION_COLOR);
			    
			    for(int index : allIndexes) {
			    	if(nbt.hasKey("x"+(index-1)) && nbt.hasKey("x"+index)) {
			    		
			    		int index1 = index-1;
			    		int index2 = index;
			    		
			    		EnumFacing facingMin = EnumFacing.byName(nbt.getString("facing"+index1));
						EnumFacing facingMax = EnumFacing.byName(nbt.getString("facing"+index2));
						
						int contextSize = (nbt.hasKey("context"+index1)) ? Integer.parseInt(list.get(nbt.getInteger("context"+index1))) : Integer.parseInt(list.get(0));
						
						SelectLittleTile tilePosMin = new SelectLittleTile(new Vec3d(Double.parseDouble(nbt.getString("x"+index1)), Double.parseDouble(nbt.getString("y"+index1)),
								Double.parseDouble(nbt.getString("z"+index1))),LittleGridContext.get(contextSize), facingMin);
						
						SelectLittleTile tilePosMax = new SelectLittleTile(new Vec3d(Double.parseDouble(nbt.getString("x"+index2)), Double.parseDouble(nbt.getString("y"+index2)),
								Double.parseDouble(nbt.getString("z"+index2))),LittleGridContext.get(contextSize), facingMax);
						
			    		String xKey1 = "x"+index1;
			    		String yKey1 = "y"+index1;
			    		String zKey1 = "z"+index1;
			    		
			    		String xKey2 = "x"+index2;
			    		String yKey2 = "y"+index2;
			    		String zKey2 = "z"+index2;
			    		
			    		double x1 = Double.parseDouble(nbt.getString(xKey1));
			    		double y1 = Double.parseDouble(nbt.getString(yKey1));
			    		double z1 = Double.parseDouble(nbt.getString(zKey1));

			    		double x2 = Double.parseDouble(nbt.getString(xKey2));
			    		double y2 = Double.parseDouble(nbt.getString(yKey2));
			    		double z2 = Double.parseDouble(nbt.getString(zKey2));

			    		int color = (nbt.hasKey("color"+index1)) ? nbt.getInteger("color"+index1) : ColorUtils.WHITE;
			    		Color colour = ColorUtils.IntToRGBA(color);
			    		float r = colour.getRed()/255F;
			    		float g = colour.getGreen()/255F;
			    		float b = colour.getBlue()/255F;
			    		//System.out.println(nbt.getDouble("x1") + ", " + nbt.getDouble(y1) + ", " + nbt.getDouble(z1) + ", " + nbt.getDouble(x2) + ", " 
			    		//		+ nbt.getDouble(y2) + ", " + nbt.getDouble(z2));
			    		if(nbt.getInteger("shape"+index1) == 0) {
							Box.drawBox(tilePosMin, tilePosMax, r, g, b, 1.0F);
			    		}else if(nbt.getInteger("shape"+index1) == 1) {
							Line.drawLine(tilePosMin, tilePosMax, r, g, b, 1.0F);
							Box.drawBox(tilePosMin, contextSize, r, g, b, 1.0F);
							Box.drawBox(tilePosMax, contextSize,r, g, b, 1.0F);
			    		}
			    	}
			    }
				
				tessellator.draw();
		
				GlStateManager.enableDepth();
				GlStateManager.depthMask(true);
				GlStateManager.enableTexture2D();
				GlStateManager.disableBlend();	
			}
			
		}

	/*
	 * 
					if(i==0) {
						Box.drawBox(tilePosMin, tilePosMax, 1.0F, 0.0F, 0.0F, 1.0F);
					//	Box.drawBox(centerX_1, centerY_1, centerZ_1, contextSize, 1.0F, 0.0F, 0.0F, 1.0F);
					//	Box.drawBox(centerX_2, centerY_2, centerZ_2, contextSize, 1.0F, 0.0F, 0.0F, 1.0F);
					//	Line.drawLine(bufferbuilder, centerX_1, centerY_1, centerZ_1, centerX_2, centerY_2, centerZ_2, 1.0F, 0.0F, 0.0F, 1.0F);
					}
					if(i==2) {
						Box.drawBox(tilePosMin, contextSize, 0.0F, 1.0F, 0.0F, 1.0F);
						Box.drawBox(tilePosMax, contextSize, 0.0F, 1.0F, 0.0F, 1.0F);
						Line.drawLine(bufferbuilder, tilePosMin, tilePosMax, 0.0F, 1.0F, 0.0F, 1.0F);
					}
					if(i==4) {
						Box.drawBox(tilePosMin, tilePosMax, 0.0F, 0.0F, 1.0F, 1.0F);
					}
	 */
	
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
