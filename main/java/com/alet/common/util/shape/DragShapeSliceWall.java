package com.alet.common.util.shape;

import java.util.ArrayList;
import java.util.List;

import com.alet.render.tapemeasure.shape.Box;
import com.alet.tiles.SelectLittleTile;
import com.creativemd.creativecore.common.gui.GuiControl;
import com.creativemd.creativecore.common.gui.container.GuiParent;
import com.creativemd.creativecore.common.gui.controls.gui.GuiStateButton;
import com.creativemd.creativecore.common.gui.controls.gui.GuiSteppedSlider;
import com.creativemd.creativecore.common.utils.math.Rotation;
import com.creativemd.creativecore.common.utils.math.box.BoxCorner;
import com.creativemd.creativecore.common.utils.math.geo.Ray2d;
import com.creativemd.littletiles.common.tile.math.box.LittleBox;
import com.creativemd.littletiles.common.tile.math.box.LittleBoxes;
import com.creativemd.littletiles.common.tile.math.box.LittleTransformableBox;
import com.creativemd.littletiles.common.tile.math.box.slice.LittleSlice;
import com.creativemd.littletiles.common.tile.math.vec.LittleAbsoluteVec;
import com.creativemd.littletiles.common.tile.math.vec.LittleVec;
import com.creativemd.littletiles.common.tile.math.vec.LittleVecContext;
import com.creativemd.littletiles.common.util.grid.LittleGridContext;
import com.creativemd.littletiles.common.util.shape.DragShape;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class DragShapeSliceWall extends DragShape {
	
	public DragShapeSliceWall() {
		super("wall slice");
	}
	
	@Override
	public LittleBoxes getBoxes(LittleBoxes boxes, LittleVec min, LittleVec max, EntityPlayer player, NBTTagCompound nbt, boolean preview, LittleAbsoluteVec originalMin, LittleAbsoluteVec originalMax) {
		LittleBox box = new LittleBox(min, max);
		System.out.println(LittleSlice.values()[nbt.getInteger("slice")]);
		
		
		//(one, two, minOne + endOne, minTwo + endTwo, minOne + startOne, minTwo + startTwo)
		
		LittleBox.combineBoxesBlocks(boxes);
		return boxes;
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
	
	@Override
	public void addExtraInformation(NBTTagCompound nbt, List<String> list) {
		
	}
	
	@Override
	public List<GuiControl> getCustomSettings(NBTTagCompound nbt, LittleGridContext context) {
		return new ArrayList<>();
	}
	
	@Override
	public void saveCustomSettings(GuiParent gui, NBTTagCompound nbt, LittleGridContext context) {
		
	}
	
	@Override
	public void rotate(NBTTagCompound nbt, Rotation rotation) {
		LittleSlice slice = LittleSlice.values()[nbt.getInteger("slice")];
		slice = slice.rotate(rotation);
		nbt.setInteger("slice", slice.ordinal());
	}
	
	@Override
	public void flip(NBTTagCompound nbt, Axis axis) {
		LittleSlice slice = LittleSlice.values()[nbt.getInteger("slice")];
		slice = slice.flip(axis);
		nbt.setInteger("slice", slice.ordinal());
	}
	
}
