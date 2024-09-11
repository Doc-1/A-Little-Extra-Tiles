package com.alet.client.tapemeasure.shape.draw;

import java.util.ArrayList;
import java.util.List;

import com.creativemd.creativecore.common.gui.GuiControl;
import com.creativemd.creativecore.common.gui.container.GuiParent;
import com.creativemd.creativecore.common.gui.controls.gui.GuiLabel;
import com.creativemd.creativecore.common.gui.controls.gui.GuiSteppedSlider;
import com.creativemd.creativecore.common.utils.math.Rotation;
import com.creativemd.littletiles.common.tile.LittleTile;
import com.creativemd.littletiles.common.tile.math.box.LittleBoxes;
import com.creativemd.littletiles.common.util.grid.LittleGridContext;
import com.creativemd.littletiles.common.util.shape.ShapeSelection;
import com.creativemd.littletiles.common.util.shape.ShapeSelection.ShapeSelectPos;
import com.creativemd.littletiles.common.util.shape.type.LittleShapeSelectable;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class LittleShapeMagicWand extends LittleShapeSelectable {
	
	int tolerance;
	
	public LittleShapeMagicWand() {
		super(1);
	}
	
	@Override
	protected void addBoxes(LittleBoxes boxes, ShapeSelection selection, boolean lowResolution) {
		
		for (ShapeSelectPos pos : selection) {
			if (pos.result.isComplete()) {
				if (pos.result.parent.isStructure())
					continue;
				
				LittleTile tile = pos.result.tile;
				int color = tile.getPreviewTile().getColor();
				int r = (color >> 16) & 0x000000FF;
				int g = (color >> 8) & 0x000000FF;
				int b = (color) & 0x000000FF;
				int minR = Math.max(r - tolerance, 0);
				int minG = Math.max(g - tolerance, 0);
				int minB = Math.max(b - tolerance, 0);
				int maxR = Math.min(r + tolerance, 255);
				int maxG = Math.min(g + tolerance, 255);
				int maxB = Math.min(b + tolerance, 255);
				for (LittleTile toDestroy : pos.result.te.noneStructureTiles()) {
					
					color = toDestroy.getPreviewTile().getColor();
					int red = (color >> 16) & 0x000000FF;
					int green = (color >> 8) & 0x000000FF;
					int blue = (color) & 0x000000FF;
					
					if ((red >= minR && red <= maxR) && (green >= minG && green <= maxG) && (blue >= minB && blue <= maxB))
						addBox(boxes, selection.inside, selection.getContext(), pos.result.te.noneStructureTiles(), toDestroy.getBox(), pos.pos.facing);
					
					//if (tile.canBeCombined(toDestroy) && toDestroy.canBeCombined(tile))
				}
			} else
				addBox(boxes, selection.inside, selection.getContext(), pos.ray.getBlockPos(), pos.pos.facing);
		}
	}
	
	@Override
	public void addExtraInformation(NBTTagCompound nbt, List<String> list) {
		list.add("tolerance: " + nbt.getInteger("tolerance"));
		
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public List<GuiControl> getCustomSettings(NBTTagCompound nbt, LittleGridContext context) {
		List<GuiControl> controls = new ArrayList<>();
		controls.add(new GuiLabel("tolerance: ", 5, 5));
		controls.add(new GuiSteppedSlider("tolerance", 5, 20, 100, 14, nbt.getInteger("tolerance"), 1, 255));
		return controls;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void saveCustomSettings(GuiParent gui, NBTTagCompound nbt, LittleGridContext context) {
		GuiSteppedSlider slider = (GuiSteppedSlider) gui.get("tolerance");
		nbt.setInteger("tolerance", (int) slider.value);
		tolerance = (int) slider.value;
	}
	
	@Override
	public void rotate(NBTTagCompound nbt, Rotation rotation) {
		
	}
	
	@Override
	public void flip(NBTTagCompound nbt, Axis axis) {
		
	}
	
}
