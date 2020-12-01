package com.alet.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.lwjgl.opengl.GL11;

import com.alet.ALET;
import com.alet.gui.controls.GuiOverlayTextList;
import com.alet.items.ItemTapeMeasure;
import com.alet.items.ItemTapeMeasure.PosData;
import com.alet.render.tapemeasure.shape.Box;
import com.alet.render.tapemeasure.shape.Circle;
import com.alet.render.tapemeasure.shape.Line;
import com.alet.tiles.SelectLittleTile;
import com.creativemd.creativecore.common.gui.GuiControl;
import com.creativemd.creativecore.common.gui.GuiRenderHelper;
import com.creativemd.creativecore.common.gui.Rect;
import com.creativemd.creativecore.common.gui.client.style.ColoredDisplayStyle;
import com.creativemd.creativecore.common.gui.client.style.Style;
import com.creativemd.creativecore.common.gui.container.GuiParent;
import com.creativemd.creativecore.common.gui.controls.gui.GuiTextBox;
import com.creativemd.creativecore.common.utils.mc.ColorUtils;
import com.creativemd.littletiles.common.tile.math.vec.LittleAbsoluteVec;
import com.creativemd.littletiles.common.tile.math.vec.LittleVec;
import com.creativemd.littletiles.common.tile.math.vec.LittleVecContext;
import com.creativemd.littletiles.common.util.grid.LittleGridContext;
import com.creativemd.littletiles.common.util.ingredient.LittleInventory;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GuiDisplayMeasurements extends GuiControl{
	
	public static Style transparentStyle = new Style("Transparent", new ColoredDisplayStyle(0, 0, 0, 40), new ColoredDisplayStyle(90, 90, 90, 60), new ColoredDisplayStyle(90, 90, 90, 50), new ColoredDisplayStyle(198, 198, 198), new ColoredDisplayStyle(0, 0, 0, 100));
	public static EntityPlayer player;
	protected static ScaledResolution scaledResolution;

	public GuiDisplayMeasurements(String name) {
		super(name, 0, 0, 0, 0);
	}
	
	@Override
	protected void renderContent(GuiRenderHelper helper, Style style, int width, int height) {
		player = Minecraft.getMinecraft().player;
		scaledResolution = new ScaledResolution(mc);
		
		posX = 0;
		posY = 0;
		
		ItemStack stack = ItemStack.EMPTY;
		ItemStack ingredient = new ItemStack(ALET.tapeMeasure, 1);
		LittleInventory inventory = new LittleInventory(player);
		
		for(int i = 0; i < inventory.size(); i++) {
			if(inventory.get(i).getItem() instanceof ItemTapeMeasure) {
				stack = inventory.get(i);
				break;
			}
		}
		if(!stack.isEmpty()  && !Minecraft.getMinecraft().gameSettings.showDebugInfo) {
			NBTTagCompound nbt = stack.getTagCompound();
			FontRenderer fontRender = mc.fontRenderer;
			List<String> list = LittleGridContext.getNames();
			
			GuiOverlayTextList textList = new GuiOverlayTextList("measurement", 0, 90, 140, getParent());
			textList.setStyle(transparentStyle);
			
			GlStateManager.pushMatrix();
			GlStateManager.translate(0, 0, 50);
			GlStateManager.disableCull();
			GlStateManager.scale(0.9F, 0.9F, 0.0F);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glFlush();
			GlStateManager.enableCull();
			int i = 0;
			
		    List<Integer> allIndexes = new ArrayList<Integer>();
			
			if(stack.hasTagCompound()) {
				String nbtStr = (stack.hasTagCompound()) ? nbt.toString() : "";

				List<String> allMatches = new ArrayList<String>();
			    Matcher m = Pattern.compile("context\\d+").matcher(nbtStr);
			    while (m.find()) {
			      allMatches.add(m.group());
			    }
			    for (String string : allMatches) {
			    	int x = Integer.parseInt(string.split("context")[1]);
				    allIndexes.add(x);
				}
			    Collections.sort(allIndexes);
			    
				PosData data = ItemTapeMeasure.data;
				if(data != null) {
					List<AxisAlignedBB> boxes = new ArrayList<AxisAlignedBB>();
				    for(int index : allIndexes) {
			    		int index1 = index;
			    		int index2 = index+1;
	
			    		if((nbt.hasKey("x"+index1) || nbt.hasKey("x"+index2)) || (nbt.hasKey("x"+index1) && nbt.hasKey("x"+index2))) { 
				    		
				    		String contextSize = "context"+index1;
				    		int color = (nbt.hasKey("color"+index1)) ? nbt.getInteger("color"+index1) : ColorUtils.WHITE;
				    		
							LittleAbsoluteVec pos = new LittleAbsoluteVec(data.result, LittleGridContext.get(Integer.parseInt(list.get(nbt.getInteger(contextSize)))));
							double[] posEdit = ItemTapeMeasure.facingOffset(pos.getPosX(), pos.getPosY(), pos.getPosZ(), Integer.parseInt(list.get(nbt.getInteger(contextSize))), data.result.sideHit);
				    		
				    		String xKey1 = "x"+index1;
				    		String yKey1 = "y"+index1;
				    		String zKey1 = "z"+index1;
				    		
				    		String xKey2 = "x"+index2;
				    		String yKey2 = "y"+index2;
				    		String zKey2 = "z"+index2;
				    		
							double x1 = posEdit[0];
							double y1 = posEdit[1];
							double z1 = posEdit[2];
							
							double x2 = posEdit[0];
							double y2 = posEdit[1];
							double z2 = posEdit[2];
	
							if(nbt.hasKey("x"+index1)) {
								x1 = Double.parseDouble(nbt.getString(xKey1));
					    		y1 = Double.parseDouble(nbt.getString(yKey1));
					    		z1 = Double.parseDouble(nbt.getString(zKey1));
							}
								
							if(nbt.hasKey("x"+index2)) {
					    		x2 = Double.parseDouble(nbt.getString(xKey2));
					    		y2 = Double.parseDouble(nbt.getString(yKey2));
					    		z2 = Double.parseDouble(nbt.getString(zKey2));
							}
							
							boxes.add(new AxisAlignedBB(x1, y1, z1, x2, y2, z2));

				    		if(nbt.getInteger("shape"+index1) == 0) {
					    		Box box = new Box(x1, y1, z1, x2, y2, z2, Integer.parseInt(list.get(nbt.getInteger(contextSize))));
					    		if(((index/2)) == nbt.getInteger("index")) 
						    		textList.addText(TextFormatting.UNDERLINE+"Measurment "+((index/2)+1), ColorUtils.WHITE);
					    		else
						    		textList.addText("Measurment "+((index/2)+1), ColorUtils.WHITE);

					    		textList.addText("X: " + box.xString, color);
					    		textList.addText("Y: " + box.yString, color);
					    		textList.addText("Z: " + box.zString, color);
				    		}else if(nbt.getInteger("shape"+index1) == 1) {
				    			Line line = new Line(x1, y1, z1, x2, y2, z2, Integer.parseInt(list.get(nbt.getInteger(contextSize))));
				    			if(((index/2)) == nbt.getInteger("index")) 
						    		textList.addText(TextFormatting.UNDERLINE+"Measurment "+((index/2)+1), ColorUtils.WHITE);
					    		else
						    		textList.addText("Measurment "+((index/2)+1), ColorUtils.WHITE);
					    		textList.addText("Line: " +line.distance, color);
				    		}		
			    		}
				    }
				    
				    /*AxisAlignedBB box = getBox(player.getPositionEyes(1F), player.getLookVec(), boxes, 5);
				    

				    if(box != null) {
					    mc.fontRenderer.drawStringWithShadow("da "+ this.getGui().getScreenRect().maxX, 0, 0, 0xFFFFFFFF);
				    }*/
					textList.renderControl(helper, 0, getRect());
					
			    }
			}
			GlStateManager.popMatrix();
		}				
	}

	/***
	 * 
	 * @param playerPos
	 * @param playerLookVector
	 * @param boxes
	 * Your list of things that player hovers over.
	 * @param maxDistance
	 * @return
	 */
	public static AxisAlignedBB getBox(Vec3d playerPos, Vec3d playerLookVector, List<AxisAlignedBB> boxes, double maxDistance) {
	    double resultDistance = maxDistance;
	    AxisAlignedBB result = null;
	    AxisAlignedBB box = null;
	    Vec3d startPos = null;
	    Vec3d endPos = null;
	    double distance = 0, x3, y3, z3 = 0;
	    for(int i = 0,m=boxes.size();i<m;i++) {
	    	box = boxes.get(i);
        	endPos = playerPos.addVector(playerLookVector.x * maxDistance, playerLookVector.y * maxDistance, playerLookVector.z * maxDistance);
        	endPos = new Vec3d(endPos.x, endPos.y, endPos.z);
        	startPos = new Vec3d(playerPos.x, playerPos.y, playerPos.z);
	        distance = startPos.distanceTo(endPos);
	    }
	    for(double j = 0; j < distance; j += 0.25) {
        	x3 = startPos.x + (j/distance) * (endPos.x - startPos.x); 
        	y3 = startPos.y + (j/distance) * (endPos.y - startPos.y);
        	z3 = startPos.z + (j/distance) * (endPos.z - startPos.z); 
        	Vec3d pointPos = getAccurateVec(x3, y3, z3);
	        
	        if(contains(box, pointPos)) {
	            result = box;
	            break;
	        }
	    }
        	//box[-1555.25, 4.0, 81.0 -> -1553.75, 4.5, 81.75]
	        //(-1555.5, 4.5, 81.5)
	    return result;
	}
	
	public static Vec3d getAccurateVec(double x, double y, double z) {
		LittleGridContext context = LittleGridContext.get(4);
		
		x = context.toGridAccurate(x);
		y = context.toGridAccurate(y);
		z = context.toGridAccurate(z);
		
		BlockPos pos = new BlockPos((int) Math.floor(context.toVanillaGrid(x)), (int) Math.floor(context.toVanillaGrid(y)), (int) Math.floor(context.toVanillaGrid(z)));
		LittleVecContext contextVec = new LittleVecContext(new LittleVec((int) (x - context.toGridAccurate(pos.getX())), (int) (y - context.toGridAccurate(pos.getY())), (int) (z - context.toGridAccurate(pos.getZ()))), context);
		
		return new Vec3d(pos.getX() + contextVec.getPosX(), pos.getY() + contextVec.getPosY(), pos.getZ() + contextVec.getPosZ());
	}
	
	/**
     * Returns if the supplied Vec3D is completely inside the bounding box
     */
    public static boolean contains(AxisAlignedBB box, Vec3d vec) {
        if (vec.x >= box.minX && vec.x <= box.maxX) {
            if (vec.y >= box.minY && vec.y <= box.maxY) {
                return vec.z >= box.minZ && vec.z <= box.maxZ;
            }
            else {
                return false;
            }
        }
        else {
            return false;
        }
    }
}




/*
 String distence1 = Box.distence(new Vec3d(Double.parseDouble(nbt.getString("x0")), Double.parseDouble(nbt.getString("y0")), 
						Double.parseDouble(nbt.getString("z0"))), new Vec3d(Double.parseDouble(nbt.getString("x1")), 
						Double.parseDouble(nbt.getString("y1")), Double.parseDouble(nbt.getString("z1"))), 
						Integer.parseInt(list.get(nbt.getInteger("context0"))));
				
				textList.addText("Measurement 1:");
				textList.addText("X: "+Box.xString, ColorUtils.RED);
				textList.addText("Y: "+Box.yString, ColorUtils.RED);
				textList.addText("Z: "+Box.zString, ColorUtils.RED);
				
				String distence2 = Line.distence(new Vec3d(Double.parseDouble(nbt.getString("x2")), Double.parseDouble(nbt.getString("y2")), 
						Double.parseDouble(nbt.getString("z2"))), new Vec3d(Double.parseDouble(nbt.getString("x3")), 
						Double.parseDouble(nbt.getString("y3")), Double.parseDouble(nbt.getString("z3"))),
						Integer.parseInt(list.get(nbt.getInteger("context0"))));

				textList.addText("Measurement 2:");
				textList.addText("Distance: "+distence2, ColorUtils.GREEN);

				String distence3 = Box.distence(new Vec3d(Double.parseDouble(nbt.getString("x4")), Double.parseDouble(nbt.getString("y4")), 
						Double.parseDouble(nbt.getString("z4"))), new Vec3d(Double.parseDouble(nbt.getString("x5")), 
						Double.parseDouble(nbt.getString("y5")), Double.parseDouble(nbt.getString("z5"))),
						Integer.parseInt(list.get(nbt.getInteger("context4"))));
				
				textList.addText("Measurement 3:");
				textList.addText("X: "+Box.xString, ColorUtils.BLUE);
				textList.addText("Y: "+Box.yString, ColorUtils.BLUE);
				textList.addText("Z: "+Box.zString, ColorUtils.BLUE);

*/