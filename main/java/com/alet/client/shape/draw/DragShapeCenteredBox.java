package com.alet.client.shape.draw;

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
import com.creativemd.littletiles.common.util.grid.LittleGridContext;
import com.creativemd.littletiles.common.util.shape.LittleShape;
import com.creativemd.littletiles.common.util.shape.ShapeSelection;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class DragShapeCenteredBox extends LittleShape {
	
	public DragShapeCenteredBox() {
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
		if (selection.getNBT().getBoolean("hollow")) {
			int thickness = selection.getNBT().getInteger("thickness");
			LittleVec size = box.getSize();
			if (thickness * 2 >= size.x || thickness * 2 >= size.y || thickness * 2 >= size.z)
				boxes.add(box);
			else {
				boxes.add(new LittleBox(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.minZ + thickness));
				boxes.add(new LittleBox(box.minX, box.minY + thickness, box.minZ + thickness, box.minX
				        + thickness, box.maxY - thickness, box.maxZ - thickness));
				boxes.add(new LittleBox(box.maxX - thickness, box.minY + thickness, box.minZ
				        + thickness, box.maxX, box.maxY - thickness, box.maxZ - thickness));
				boxes.add(new LittleBox(box.minX, box.minY, box.minZ + thickness, box.maxX, box.minY
				        + thickness, box.maxZ - thickness));
				boxes.add(new LittleBox(box.minX, box.maxY - thickness, box.minZ
				        + thickness, box.maxX, box.maxY, box.maxZ - thickness));
				boxes.add(new LittleBox(box.minX, box.minY, box.maxZ - thickness, box.maxX, box.maxY, box.maxZ));
			}
		} else
			boxes.add(box);
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
