package com.alet.common.util.shape;

import java.util.ArrayList;
import java.util.List;

import com.creativemd.creativecore.common.gui.GuiControl;
import com.creativemd.creativecore.common.gui.container.GuiParent;
import com.creativemd.creativecore.common.gui.controls.gui.GuiCheckBox;
import com.creativemd.creativecore.common.gui.controls.gui.GuiSteppedSlider;
import com.creativemd.creativecore.common.utils.math.Rotation;
import com.creativemd.littletiles.common.tile.math.box.LittleBox;
import com.creativemd.littletiles.common.tile.math.box.LittleBoxes;
import com.creativemd.littletiles.common.tile.math.vec.LittleVec;
import com.creativemd.littletiles.common.tile.preview.LittlePreview;
import com.creativemd.littletiles.common.util.grid.LittleGridContext;
import com.creativemd.littletiles.common.util.shape.LittleShape;
import com.creativemd.littletiles.common.util.shape.ShapeSelection;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class DragShapeCenteredSphere extends LittleShape {
	
	public DragShapeCenteredSphere() {
		super(2);
	}
	
	@Override
	protected void addBoxes(LittleBoxes boxes, ShapeSelection selection, boolean lowResolution) {
		LittleVec vec1 = selection.getFirst().pos.getRelative(selection.getPos());
		LittleVec vec2 = selection.getLast().pos.getRelative(selection.getPos());
		LittleBox box1 = new LittleBox(vec1);
		LittleBox box2 = new LittleBox(vec2);
		int pX = box1.maxX - box2.maxX;
		int pY = box1.maxY - box2.maxY;
		int pZ = box1.maxZ - box2.maxZ;
		LittleBox box3 = new LittleBox(box2);
		System.out.println(pX + " " + pY + " " + pZ);
		int i = 0;
		if (selection.getNBT().getBoolean("center"))
			i++;
		box3.add((pX * 2) + i, (pY * 2) + i, (pZ * 2) + i);
		LittleBox box = new LittleBox(box2, box3);
		
		boolean hollow = selection.getNBT().getBoolean("hollow");
		LittleVec size = box.getSize();
		if (lowResolution && size.getPercentVolume(boxes.context) > 4) {
			boxes.add(box);
			return;
		}
		
		LittleVec center = size.calculateCenter();
		LittleVec invCenter = size.calculateInvertedCenter();
		invCenter.invert();
		
		double a = Math.pow(Math.max(1, size.x / 2), 2);
		double b = Math.pow(Math.max(1, size.y / 2), 2);
		double c = Math.pow(Math.max(1, size.z / 2), 2);
		
		double a2 = 1;
		double b2 = 1;
		double c2 = 1;
		
		int thickness = selection.getNBT().getInteger("thickness");
		
		if (hollow && size.x > thickness * 2 && size.y > thickness * 2 && size.z > thickness * 2) {
			int all = size.x + size.y + size.z;
			
			double sizeXValue = (double) size.x / all;
			double sizeYValue = (double) size.y / all;
			double sizeZValue = (double) size.z / all;
			
			if (sizeXValue > 0.5)
				sizeXValue = 0.5;
			if (sizeYValue > 0.5)
				sizeYValue = 0.5;
			if (sizeZValue > 0.5)
				sizeZValue = 0.5;
			
			a2 = Math.pow(Math.max(1, (sizeXValue * all - thickness * 2) / 2), 2);
			b2 = Math.pow(Math.max(1, (sizeYValue * all - thickness * 2) / 2), 2);
			c2 = Math.pow(Math.max(1, (sizeZValue * all - thickness * 2) / 2), 2);
		} else
			hollow = false;
		
		boolean stretchedX = size.x % 2 == 0;
		boolean stretchedY = size.y % 2 == 0;
		boolean stretchedZ = size.z % 2 == 0;
		
		double centerX = size.x / 2;
		double centerY = size.y / 2;
		double centerZ = size.z / 2;
		
		LittleVec min = box.getMinVec();
		
		for (int x = 0; x < size.x; x++) {
			for (int y = 0; y < size.y; y++) {
				for (int z = 0; z < size.z; z++) {
					
					double posX = x - centerX + (stretchedX ? 0.5 : 0);
					double posY = y - centerY + (stretchedY ? 0.5 : 0);
					double posZ = z - centerZ + (stretchedZ ? 0.5 : 0);
					
					double valueA = Math.pow(posX, 2) / a;
					double valueB = Math.pow(posY, 2) / b;
					double valueC = Math.pow(posZ, 2) / c;
					
					if (valueA + valueB + valueC <= 1) {
						double valueA2 = Math.pow(posX, 2) / a2;
						double valueB2 = Math.pow(posY, 2) / b2;
						double valueC2 = Math.pow(posZ, 2) / c2;
						if (!hollow || valueA2 + valueB2 + valueC2 > 1)
							boxes.add(new LittleBox(new LittleVec(min.x + x, min.y + y, min.z + z)));
					}
				}
			}
		}
		
		boxes.combineBoxesBlocks();
		
		if (lowResolution && boxes.size() > LittlePreview.lowResolutionMode) {
			boxes.clear();
			boxes.add(box);
		}
	}
	
	@Override
	public void addExtraInformation(NBTTagCompound nbt, List<String> list) {
		if (nbt.getBoolean("hollow")) {
			list.add("type: hollow");
			list.add("thickness: " + nbt.getInteger("thickness") + " tiles");
		} else
			list.add("type: solid");
		if (nbt.getBoolean("center")) {
			list.add("center: double");
		} else
			list.add("center: single");
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public List<GuiControl> getCustomSettings(NBTTagCompound nbt, LittleGridContext context) {
		List<GuiControl> controls = new ArrayList<>();
		
		controls.add(new GuiCheckBox("hollow", 5, 0, nbt.getBoolean("hollow")));
		controls.add(new GuiCheckBox("center", "double center", 5, 13, nbt.getBoolean("center")));
		controls.add(new GuiSteppedSlider("thickness", 5, 30, 100, 14, nbt.getInteger("thickness"), 1, context.size));
		
		return controls;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void saveCustomSettings(GuiParent gui, NBTTagCompound nbt, LittleGridContext context) {
		
		GuiCheckBox box = (GuiCheckBox) gui.get("hollow");
		nbt.setBoolean("hollow", box.value);
		GuiCheckBox center = (GuiCheckBox) gui.get("center");
		nbt.setBoolean("center", center.value);
		GuiSteppedSlider slider = (GuiSteppedSlider) gui.get("thickness");
		nbt.setInteger("thickness", (int) slider.value);
	}
	
	@Override
	public void rotate(NBTTagCompound nbt, Rotation rotation) {
		
	}
	
	@Override
	public void flip(NBTTagCompound nbt, Axis axis) {
		
	}
}
