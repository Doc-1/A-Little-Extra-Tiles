package com.alet.common.util.shape;

import java.util.ArrayList;
import java.util.List;

import com.creativemd.creativecore.common.gui.GuiControl;
import com.creativemd.creativecore.common.gui.container.GuiParent;
import com.creativemd.creativecore.common.gui.controls.gui.GuiCheckBox;
import com.creativemd.creativecore.common.gui.controls.gui.GuiStateButton;
import com.creativemd.creativecore.common.gui.controls.gui.GuiSteppedSlider;
import com.creativemd.creativecore.common.utils.math.Rotation;
import com.creativemd.littletiles.common.tile.combine.BasicCombiner;
import com.creativemd.littletiles.common.tile.math.box.LittleAbsoluteBox;
import com.creativemd.littletiles.common.tile.math.box.LittleBox;
import com.creativemd.littletiles.common.tile.math.box.LittleBoxes;
import com.creativemd.littletiles.common.tile.math.vec.LittleAbsoluteVec;
import com.creativemd.littletiles.common.tile.math.vec.LittleVec;
import com.creativemd.littletiles.common.util.grid.LittleGridContext;
import com.creativemd.littletiles.common.util.shape.DragShape;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class DragShapeTriangle extends DragShape {
	
	public DragShapeTriangle() {
		super("triangle");
	}
	
	@Override
	public LittleBoxes getBoxes(LittleBoxes boxes, LittleVec min, LittleVec max, EntityPlayer player, NBTTagCompound nbt, boolean preview, LittleAbsoluteVec originalMin, LittleAbsoluteVec originalMax) {
		LittleBox box = new LittleBox(min, max);
		
		int direction = nbt.getInteger("direction");
		int thickness = nbt.getInteger("thickness")-1;
		boolean hollow = nbt.getBoolean("hollow");
		
		boolean flip = false;
		
		LittleAbsoluteVec absolute = new LittleAbsoluteVec(boxes.pos, boxes.context);

		LittleVec originalMinVec = originalMin.getRelative(absolute).getVec(boxes.context);
		LittleVec originalMaxVec = originalMax.getRelative(absolute).getVec(boxes.context);
		
		LittleBox box2 = new LittleBox(originalMinVec, originalMaxVec);

		int w = (originalMaxVec.x - originalMinVec.x)/2;
		int wm = originalMaxVec.x - originalMinVec.x;
		int h = originalMaxVec.z - originalMinVec.z;
		
		int x = originalMinVec.x;
		int y = originalMinVec.z;
		int xm = x;
		int ym = y;
		
		if (direction == 1 ^ direction == 3) {
			w = (originalMaxVec.z - originalMinVec.z)/2;
			wm = originalMaxVec.z - originalMinVec.z;
			h = originalMaxVec.x - originalMinVec.x;
			
			x = originalMinVec.z;
			y = originalMinVec.x;
			xm = x;
			ym = y;
		} else if (direction == 4 ^ direction == 6) {
			w = (originalMaxVec.y - originalMinVec.y)/2;
			wm = originalMaxVec.y - originalMinVec.y;
			h = originalMaxVec.z - originalMinVec.z;
			
			x = originalMinVec.y;
			y = originalMinVec.z;
			xm = x;
			ym = y;
		}else if (direction == 5 ^ direction == 7) {
			w = (originalMaxVec.z - originalMinVec.z)/2;
			wm = originalMaxVec.z - originalMinVec.z;
			h = originalMaxVec.y - originalMinVec.y;
			
			x = originalMinVec.z;
			y = originalMinVec.y;
			xm = x;
			ym = y;
		}else if (direction == 8 ^ direction == 10) {
			w = (originalMaxVec.x - originalMinVec.x)/2;
			wm = originalMaxVec.x - originalMinVec.x;
			h = originalMaxVec.y - originalMinVec.y;
			
			x = originalMinVec.x;
			y = originalMinVec.y;
			xm = x;
			ym = y;
		}else if (direction == 9 ^ direction == 11) {
			w = (originalMaxVec.y - originalMinVec.y)/2;
			wm = originalMaxVec.y - originalMinVec.y;
			h = originalMaxVec.x - originalMinVec.x;
			
			x = originalMinVec.y;
			y = originalMinVec.x;
			xm = x;
			ym = y;
		}

		int dx1 = 0, dy1 = 0, dx2 = 0, dy2 = 0;
		if (w < 0)
			dx1 = -1;
		else if (w > 0)
			dx1 = 1;
		if (h < 0)
			dy1 = -1;
		else if (h > 0)
			dy1 = 1;
		if (w < 0)
			dx2 = -1;
		else if (w > 0)
			dx2 = 1;
		int longest = Math.abs(w);
		int shortest = Math.abs(h);
		if (!(longest > shortest)) {
			longest = Math.abs(h);
			shortest = Math.abs(w);
			if (h < 0)
				dy2 = -1;
			else if (h > 0)
				dy2 = 1;
			dx2 = 0;
		}
		int numerator = longest >> 1;
		
		LittleBox oldLine_1 = null;
		LittleBox oldLine_2 = null;
		
		for (int i = 0; i <= longest; i++) {
			
			LittleBox line_1 = null;
			LittleBox line_2 = null;
			

			switch (direction) {
			/**************************************************************************************************************************************************************/
			case 0:
			case 2:
				line_1 = new LittleBox(x, box.minY, y , x + 1, box.maxY, y  + 1);
				line_2 = new LittleBox(xm+(w*2),box.minY,ym ,xm +1+(w*2),box.maxY,ym+1);
				//Moves line_2 over if the box is even
				if(Math.abs(wm) % 2 == 1)
					if(wm<0) 
						line_2.add(-1, 0, 0);
					else if(wm>0) 
						line_2.add(1, 0, 0);
				
				//Placed inside a try catch, cause I am lazy and don't want oldLine_1 and oldLine_2 being null crash the game
				try {
					//Distance between the two sloped lines.
					int dis = Math.abs(oldLine_1.maxX-oldLine_2.maxX)-1;
					int index = boxes.size()-2;
					
					//Checks if the previous box does not have the same relative Y value.
					if(line_1.maxZ != oldLine_1.maxZ) {
						//Changes the which corner you want to modify. Min or Max
						if(wm<0) { //if min > max
							if(i > thickness+1 && (dis >= (thickness)*2) && hollow) { //Prevents overlap as well as whether to extend by thickness or distance between the two sloped lines.
								boxes.set(index, editBox(oldLine_1, -thickness, 0, 0, 0, 0, 0));
								boxes.set(index+1, editBox(oldLine_2, 0, 0, 0, thickness, 0, 0));
							}else {
								boxes.set(index, editBox(oldLine_1, -dis, 0, 0, 0, 0, 0));
							}
						}else { //if min < max
							if(i > thickness+1 && (dis >= (thickness)*2) && hollow) {
								boxes.set(index, editBox(oldLine_1, 0, 0, 0, thickness, 0, 0));
								boxes.set(index+1, editBox(oldLine_2, -thickness, 0, 0, 0, 0, 0));
							}else {
								boxes.set(index, editBox(oldLine_1, 0, 0, 0, dis, 0, 0));
							}
						}
					}
				} catch (NullPointerException e) {}
				if(direction == 0)
					flip = (originalMin.getPosZ()-originalMax.getPosZ())*16 < 0;
				else
					flip = (originalMin.getPosZ()-originalMax.getPosZ())*16 > 0;
				break;
			/**************************************************************************************************************************************************************/
			case 1:
			case 3:
				line_1 = new LittleBox(y, box.minY, x, y + 1, box.maxY, x  + 1);
				line_2 = new LittleBox(ym, box.minY, xm+(w*2), ym + 1, box.maxY, xm  + 1+(w*2));
				//Moves line_2 over if the box is even
				if(Math.abs(wm) % 2 == 1)
					if(wm<0) 
						line_2.add(0, 0, -1);
					else if(wm>0) 
						line_2.add(0, 0, 1);
				
				//Placed inside a try catch, cause I am lazy and don't want oldLine_1 and oldLine_2 being null crash the game
				try {
					//Distance between the two sloped lines.
					int dis = Math.abs(oldLine_1.maxZ-oldLine_2.maxZ)-1;
					int index = boxes.size()-2;
					
					//Checks if the previous box does not have the same relative Y value.
					if(line_1.maxX != oldLine_1.maxX) {
						//Changes the which corner you want to modify. Min or Max
						if(wm<0) { //if min > max
							if(i > thickness+1 && (dis >= (thickness)*2) && hollow) { //Prevents overlap as well as whether to extend by thickness or distance between the two sloped lines.
								boxes.set(index, editBox(oldLine_1, 0, 0, -thickness, 0, 0, 0));
								boxes.set(index+1, editBox(oldLine_2, 0, 0, 0, 0, 0, thickness));
							}else {
								boxes.set(index, editBox(oldLine_1, 0, 0, -dis, 0, 0, 0));
							}
						}else { //if min < max
							if(i > thickness+1 && (dis >= (thickness)*2) && hollow) {
								boxes.set(index, editBox(oldLine_1, 0, 0, 0, 0, 0, thickness));
								boxes.set(index+1, editBox(oldLine_2, 0, 0, -thickness, 0, 0, 0));
							}else {
								boxes.set(index, editBox(oldLine_1, 0, 0, 0, 0, 0, dis));
							}
						}
					}
				} catch (NullPointerException e) {}
				if(direction == 1)
					flip = (originalMin.getPosX()-originalMax.getPosX())*16 > 0;
				else 
					flip = (originalMin.getPosX()-originalMax.getPosX())*16 < 0;
				
				break;
			/**************************************************************************************************************************************************************/
			case 4:
			case 6:
				line_1 = new LittleBox(box.minX, x , y , box.maxX, x + 1, y  + 1);
				line_2 = new LittleBox(box.minX,xm+(w*2),ym ,box.maxX,xm+(w*2)+1,ym+1);

				//Moves line_2 over if the box is even
				if(Math.abs(wm) % 2 == 1)
					if(wm<0) 
						line_2.add(0, -1, 0);
					else if(wm>0) 
						line_2.add(0, 1, 0);
				
				//Placed inside a try catch, cause I am lazy and don't want oldLine_1 and oldLine_2 being null crash the game
				try {
					//Distance between the two sloped lines.
					int dis = Math.abs(oldLine_1.maxY-oldLine_2.maxY)-1;
					int index = boxes.size()-2;
					
					//Checks if the previous box does not have the same relative Y value.
					if(line_1.maxZ != oldLine_1.maxZ) {
						//Changes the which corner you want to modify. Min or Max
						if(wm<0) { //if min > max
							if(i > thickness+1 &&  (dis >= (thickness)*2) && hollow) { //Prevents overlap as well as whether to extend by thickness or distance between the two sloped lines.
								boxes.set(index, editBox(oldLine_1, 0, -thickness, 0, 0, 0, 0));
								boxes.set(index+1, editBox(oldLine_2, 0, 0, 0, 0, thickness, 0));
							}else {
								boxes.set(index, editBox(oldLine_1, 0, -dis, 0, 0, 0, 0));
							}
						}else { //if min < max
							if(i > thickness+1 &&  (dis >= (thickness)*2) && hollow) { //Prevents overlap as well as whether to extend by thickness or distance between the two sloped lines.
								boxes.set(index, editBox(oldLine_1, 0, 0, 0, 0, thickness, 0));
								boxes.set(index+1, editBox(oldLine_2, 0, -thickness, 0, 0, 0, 0));
							}else {
								boxes.set(index, editBox(oldLine_1, 0, 0, 0, 0, dis, 0));
							}
						}
					}
				} catch (NullPointerException e) {}
				if(direction == 4)
					flip = (originalMin.getPosZ()-originalMax.getPosZ())*16 < 0;
				else 
					flip = (originalMin.getPosZ()-originalMax.getPosZ())*16 > 0;
				break;
			/**************************************************************************************************************************************************************/
			case 5:
			case 7:
				line_1 = new LittleBox(box.minX, y, x,  box.maxX, y + 1, x + 1);
				line_2 = new LittleBox(box.minX, ym, xm+(w*2), box.maxX, ym + 1 , xm+(w*2) + 1);

				//Moves line_2 over if the box is even
				if(Math.abs(wm) % 2 == 1)
					if(wm<0) 
						line_2.add(0, 0, -1);
					else if(wm>0) 
						line_2.add(0, 0, 1);
				
				//Placed inside a try catch, cause I am lazy and don't want oldLine_1 and oldLine_2 being null crash the game
				try {
					//Distance between the two sloped lines.
					int dis = Math.abs(oldLine_1.maxZ-oldLine_2.maxZ)-1;
					int index = boxes.size()-2;
					
					//Checks if the previous box does not have the same relative Y value.
					if(line_1.maxY != oldLine_1.maxY) {
						//Changes the which corner you want to modify. Min or Max
						if(wm<0) { //if min > max
							if(i > thickness+1 && (dis >= (thickness)*2)  && hollow) { //Prevents overlap as well as whether to extend by thickness or distance between the two sloped lines.
								boxes.set(index, editBox(oldLine_1, 0, 0, -thickness, 0, 0, 0));
								boxes.set(index+1, editBox(oldLine_2, 0, 0, 0, 0, 0, thickness));
							}else {
								boxes.set(index, editBox(oldLine_1, 0, 0, -dis, 0, 0, 0));
							}
						}else { //if min < max
							if(i > thickness+1 && (dis >= (thickness)*2)  && hollow) { //Prevents overlap as well as whether to extend by thickness or distance between the two sloped lines.
								boxes.set(index, editBox(oldLine_1, 0, 0, thickness, 0, 0, 0));
								boxes.set(index+1, editBox(oldLine_2, 0, 0, 0, 0, 0, -thickness));
							}else {
								boxes.set(index, editBox(oldLine_1, 0, 0, 0, 0, 0, dis));
							}
						}
					}
				} catch (NullPointerException e) {}
				if(direction == 5)
					flip = (originalMin.getPosY()-originalMax.getPosY())*16 < 0;
				else 
					flip = (originalMin.getPosY()-originalMax.getPosY())*16 > 0;
				break;
			/**************************************************************************************************************************************************************/
			case 8:
			case 10:
				line_1 = new LittleBox(x, y, box.minZ, x + 1, y + 1, box.maxZ);
				line_2 = new LittleBox(xm+(w*2), ym, box.minZ, xm +(w*2)+ 1, y + 1 ,box.maxZ);

				//Moves line_2 over if the box is even
				if(Math.abs(wm) % 2 == 1)
					if(wm<0) 
						line_2.add(-1, 0, 0);
					else if(wm>0) 
						line_2.add(1, 0, 0);
				
				//Placed inside a try catch, cause I am lazy and don't want oldLine_1 and oldLine_2 being null crash the game
				try {
					//Distance between the two sloped lines.
					int dis = Math.abs(oldLine_1.maxX-oldLine_2.maxX)-1;
					int index = boxes.size()-2;
					
					//Checks if the previous box does not have the same relative Y value.
					if(line_1.maxY != oldLine_1.maxY) {
						//Changes the which corner you want to modify. Min or Max
						if(wm<0) { //if min > max
							if(i > thickness+1 &&  (dis >= (thickness)*2) && hollow) { //Prevents overlap as well as whether to extend by thickness or distance between the two sloped lines.
								boxes.set(index, editBox(oldLine_1, -thickness, 0, 0, 0, 0, 0));
								boxes.set(index+1, editBox(oldLine_2, 0, 0, 0, thickness, 0, 0));
							}else {
								boxes.set(index, editBox(oldLine_1, -dis, 0, 0, 0, 0, 0));
							}
						}else { //if min < max
							if(i > thickness+1 &&  (dis >= (thickness)*2) && hollow) { //Prevents overlap as well as whether to extend by thickness or distance between the two sloped lines.
								boxes.set(index, editBox(oldLine_1, 0, 0, 0, thickness, 0, 0));
								boxes.set(index+1, editBox(oldLine_2, -thickness, 0, 0, 0, 0, 0));
							}else {
								boxes.set(index, editBox(oldLine_1, 0, 0, 0, dis, 0, 0));
							}
						}
					}
				} catch (NullPointerException e) {}
				if(direction == 8)
					flip = (originalMin.getPosY()-originalMax.getPosY())*16 > 0;
				else 
					flip = (originalMin.getPosY()-originalMax.getPosY())*16 < 0;
				break;
			/**************************************************************************************************************************************************************/
			case 9:
			case 11:
				line_1 = new LittleBox(y, x, box.minZ, y + 1, x + 1, box.maxZ);
				line_2 = new LittleBox(ym, xm+(w*2), box.minZ, ym + 1, xm+(w*2) + 1, box.maxZ);

				//Moves line_2 over if the box is even
				if(Math.abs(wm) % 2 == 1)
					if(wm<0) 
						line_2.add(0, -1, 0);
					else if(wm>0) 
						line_2.add(0, 1, 0);
				
				//Placed inside a try catch, cause I am lazy and don't want oldLine_1 and oldLine_2 being null crash the game
				try {
					//Distance between the two sloped lines.
					int dis = Math.abs(oldLine_1.maxZ-oldLine_2.maxZ)-1;
					int index = boxes.size()-2;
					
					//Checks if the previous box does not have the same relative Y value.
					if(line_1.maxZ != oldLine_1.maxZ) {
						//Changes the which corner you want to modify. Min or Max
						if(wm<0) { //if min > max
							if(i > thickness+1 &&  (dis >= (thickness)*2) && hollow) { //Prevents overlap as well as whether to extend by thickness or distance between the two sloped lines.
								boxes.set(index, editBox(oldLine_1, 0, -thickness, 0, 0, 0, 0));
								boxes.set(index+1, editBox(oldLine_2, 0, 0, 0, 0, thickness, 0));
							}else {
								boxes.set(index, editBox(oldLine_1, 0, -dis, 0, 0, 0, 0));
							}
						}else { //if min < max
							if(i > thickness+1 &&  (dis >= (thickness)*2) && hollow) { //Prevents overlap as well as whether to extend by thickness or distance between the two sloped lines.
								boxes.set(index, editBox(oldLine_1, 0, 0, 0, 0, thickness, 0));
								boxes.set(index+1, editBox(oldLine_2, 0, -thickness, 0, 0, 0, 0));
							}else {
								boxes.set(index, editBox(oldLine_1, 0, 0, 0, 0, dis, 0));
							}
						}
					}
				} catch (NullPointerException e) {}
				if(direction == 9)
					flip = (originalMin.getPosX()-originalMax.getPosX())*16 > 0;
				else 
					flip = (originalMin.getPosX()-originalMax.getPosX())*16 < 0;
			break;
			/**************************************************************************************************************************************************************/
			default:
				line_1 = new LittleBox(box.minX, y, x,  box.maxX, y + 1, x + 1);
				line_2 = new LittleBox(box.minX, ym, xm+(w*2), box.maxX, ym + 1 , xm+(w*2) + 1);
				break;
			}
				
			boxes.add(line_1);
			if(i!=longest || i==longest && Math.abs(wm) % 2 == 1)
				boxes.add(line_2);
			
			oldLine_1 = line_1;
			oldLine_2 = line_2;
			numerator += shortest;
			if (!(numerator < longest)) {
				numerator -= longest;
				x += dx1;
				y += dy1;
				xm -= dx1;
				ym += dy1;
			} else {
				x += dx2;
				y += dy2;
				xm -= dx2;
				ym += dy2;
			}
		}
		//LittleBox line = new LittleBox(box.minX,box.minY,min.z- thicknessYInv,box.maxX,box.maxY,min.z+ thicknessY+1);
		//boxes.add(line);
		

		LittleBox.combineBoxesBlocks(boxes);
		if (flip) {
			switch (direction) {
			case 0:
			case 2:
			case 4:
			case 6:
				boxes.flip(Axis.Z, new LittleAbsoluteBox(boxes.pos, box, boxes.context));
				break;
			case 1:
			case 3:
			case 9:
			case 11:
				boxes.flip(Axis.X, new LittleAbsoluteBox(boxes.pos, box, boxes.context));
				break;
			case 5:
			case 7:
			case 8:
			case 10:
				boxes.flip(Axis.Y, new LittleAbsoluteBox(boxes.pos, box, boxes.context));
				break;
			default:
				break;
			}
		}
		//boxes.getSurroundingBox().flipBox(Axis.Z, box.getCenter());
		
		return boxes;
	}
	
	public LittleBox editBox(LittleBox box, int x1, int y1, int z1, int x2, int y2, int z2) {
		x1 += box.minX;
		y1 += box.minY;
		z1 += box.minZ;
		
		x2 += box.maxX;
		y2 += box.maxY;
		z2 += box.maxZ;
		return new LittleBox(x1,y1,z1,x2,y2,z2);
	}
	
	@Override
	public void addExtraInformation(NBTTagCompound nbt, List<String> list) {
		if (nbt.getBoolean("hollow")) {
			list.add("type: hollow");
			list.add("thickness: " + nbt.getInteger("thickness") + " tiles");
		} else
			list.add("type: solid");
		
		int facing = nbt.getInteger("direction");
		String text = "facing: ";
		switch (facing) {
		case 0:
			text += "y";
			break;
		case 1:
			text += "x";
			break;
		case 2:
			text += "z";
			break;
		}
		list.add(text);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public List<GuiControl> getCustomSettings(NBTTagCompound nbt, LittleGridContext context) {
		List<GuiControl> controls = new ArrayList<>();
		controls.add(new GuiCheckBox("hollow", 5, 0, nbt.getBoolean("hollow")));

		controls.add(new GuiSteppedSlider("thickness", 5, 13, 100, 14, nbt.getInteger("thickness"), 1, context.size));
		controls.add(new GuiStateButton("direction", nbt.getInteger("direction"), 5, 35, 
				"facing: y north", "facing: y east", "facing: y south", "facing: y west", 
				"facing: x north", "facing: x down", "facing: x south", "facing: x up", 
				"facing: z up", "facing: z east", "facing: z down", "facing: z west"));
		return controls;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void saveCustomSettings(GuiParent gui, NBTTagCompound nbt, LittleGridContext context) {
		
		GuiCheckBox box = (GuiCheckBox) gui.get("hollow");
		nbt.setBoolean("hollow", box.value);
		
		GuiSteppedSlider slider = (GuiSteppedSlider) gui.get("thickness");
		nbt.setInteger("thickness", (int) slider.value);
		
		GuiStateButton state = (GuiStateButton) gui.get("direction");
		nbt.setInteger("direction", state.getState());
		
	}
	
	@Override
	public void rotate(NBTTagCompound nbt, Rotation rotation) {
		int direction = nbt.getInteger("direction");
		if (rotation == Rotation.Y_CLOCKWISE) {
			if(direction == 0)
				direction = 11;
			else
				direction--;

		}else if(rotation == Rotation.Y_COUNTER_CLOCKWISE){
			if(direction == 11)
				direction = 0;
			else
				direction++;

		}
		
		nbt.setInteger("direction", direction);
	}
	
	@Override
	public void flip(NBTTagCompound nbt, Axis axis) {
		
	}
	
	
}

/*
 * case 33:
				
 */


