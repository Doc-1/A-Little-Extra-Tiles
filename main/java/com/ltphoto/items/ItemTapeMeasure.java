package com.ltphoto.items;

import java.util.List;

import javax.annotation.Nullable;

import com.creativemd.creativecore.common.gui.container.SubContainer;
import com.creativemd.creativecore.common.gui.container.SubGui;
import com.creativemd.creativecore.common.gui.opener.IGuiCreator;
import com.creativemd.littletiles.client.gui.SubGuiChisel;
import com.creativemd.littletiles.client.gui.SubGuiScrewdriver;
import com.creativemd.littletiles.client.gui.configure.SubGuiConfigure;
import com.creativemd.littletiles.common.api.ILittleTile;
import com.creativemd.littletiles.common.container.SubContainerConfigure;
import com.creativemd.littletiles.common.container.SubContainerScrewdriver;
import com.creativemd.littletiles.common.item.ItemMultiTiles;
import com.creativemd.littletiles.common.tile.math.vec.LittleAbsoluteVec;
import com.creativemd.littletiles.common.tile.math.vec.LittleVec;
import com.creativemd.littletiles.common.tile.preview.LittlePreviews;
import com.creativemd.littletiles.common.util.grid.LittleGridContext;
import com.creativemd.littletiles.common.util.place.PlacementHelper.PositionResult;
import com.ltphoto.container.SubContainerTypeWriter;
import com.ltphoto.gui.SubGuiTapeMeasure;
import com.ltphoto.gui.SubGuiTypeWriter;
import com.ltphoto.tiles.SelectLittleTile;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemTapeMeasure extends Item implements ILittleTile{
	
	public LittleAbsoluteVec firstPos;
	public LittleAbsoluteVec secondPos;
	
	public Vec3d a;
	public Vec3d b;
	
	public SelectLittleTile select = new SelectLittleTile();
	public SelectLittleTile select_2 = new SelectLittleTile();
	
	public double differenceX;
	public double differenceZ;
	public double differenceY;
	
	public ItemTapeMeasure() {
		
	}
	
	public ItemTapeMeasure(String name) {
		setUnlocalizedName(name);
		setRegistryName(name);
		setMaxStackSize(1);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add("THIS IS IN ALPHA Design May Change");
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean onRightClick(World world, EntityPlayer player, ItemStack stack, PositionResult position, RayTraceResult result) {
		LittleGridContext context = LittleGridContext.get(ItemMultiTiles.currentContext.size);
		RayTraceResult res = player.rayTrace(6.0, (float) 0.1);
		LittleVec vec = new LittleVec(context, res);
		
		double cont = 1 / context.size;
		
		a = res.hitVec;
		
		firstPos = new LittleAbsoluteVec(res, context);
		select = new SelectLittleTile(firstPos, context, position.facing);
		
		return false;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean onClickBlock(World world, EntityPlayer player, ItemStack stack, PositionResult position, RayTraceResult result) {
		LittleGridContext context = LittleGridContext.get(ItemMultiTiles.currentContext.size);
		RayTraceResult res = player.rayTrace(6.0, (float) 0.1);
		LittleVec vec = new LittleVec(context, res);
		
		double cont = 1 / context.size;
		
		b = res.hitVec;
		
		secondPos = new LittleAbsoluteVec(res, context);
		select_2 = new SelectLittleTile(secondPos, context, position.facing);

		return false;
	}
	
	@Override
	public float getDestroySpeed(ItemStack stack, IBlockState state) {
		return 0F;
	}
	
	@Override
	public boolean canDestroyBlockInCreative(World world, BlockPos pos, ItemStack stack, EntityPlayer player) {
		return false;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public SubGuiConfigure getConfigureGUI(EntityPlayer player, ItemStack stack) {
		return new SubGuiTapeMeasure(150, 200, stack);
	}
	
	@Override
	public SubContainerConfigure getConfigureContainer(EntityPlayer player, ItemStack stack) {
		return new SubContainerConfigure(player, stack);
	}

	@Override
	public boolean hasLittlePreview(ItemStack stack) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public LittlePreviews getLittlePreview(ItemStack stack) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveLittlePreview(ItemStack stack, LittlePreviews previews) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean containsIngredients(ItemStack stack) {
		// TODO Auto-generated method stub
		return false;
	}
	
	
}
