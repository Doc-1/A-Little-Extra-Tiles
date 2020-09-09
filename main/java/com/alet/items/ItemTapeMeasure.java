package com.alet.items;

import java.awt.Event;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import com.alet.gui.SubGuiTapeMeasure;
import com.alet.tiles.Measurement;
import com.alet.tiles.SelectLittleTile;
import com.creativemd.littletiles.LittleTiles;
import com.creativemd.littletiles.client.event.HoldLeftClick;
import com.creativemd.littletiles.client.gui.configure.SubGuiConfigure;
import com.creativemd.littletiles.common.api.ILittleTile;
import com.creativemd.littletiles.common.container.SubContainerConfigure;
import com.creativemd.littletiles.common.item.ItemMultiTiles;
import com.creativemd.littletiles.common.tile.math.vec.LittleAbsoluteVec;
import com.creativemd.littletiles.common.tile.math.vec.LittleVec;
import com.creativemd.littletiles.common.tile.preview.LittlePreviews;
import com.creativemd.littletiles.common.util.grid.LittleGridContext;
import com.creativemd.littletiles.common.util.place.PlacementPosition;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemTapeMeasure extends Item implements ILittleTile {

	public static String shape = "";
	
	public static void clear(ItemStack stack) {
		writeNBTData(stack, new NBTTagCompound());
	}
	
	public ItemTapeMeasure() {
		setMax(50);
	}
	
	public ItemTapeMeasure(int maxMeasurements) {
		
	}
	
	public ItemTapeMeasure(String name) {
		setUnlocalizedName(name);
		setRegistryName(name);
		setMaxStackSize(1);
		setCreativeTab(LittleTiles.littleTab);
	}
	
	public void readNBTData(ItemStack stack) {
		NBTTagCompound nbt = stack.getTagCompound();
	}
	
	public NBTTagCompound getNBTData(ItemStack stack) {
		return stack.getTagCompound();
	}
	
	public static void writeNBTData(ItemStack stack, NBTTagCompound nbt) {
		stack.setTagCompound(nbt);
	}
	
	public static void setMax(int maxMeasurements) {
		
	}
	
	public static int getMax() {
		return 50;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add("THIS IS IN ALPHA!\n" + "Design Will Change.\n" + "Press C to change  \n" + "grid size.");
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean onRightClick(World world, EntityPlayer player, ItemStack stack, PlacementPosition position, RayTraceResult result) {
		int index = 0;
		int contextSize = 16;
		List<String> list = LittleGridContext.getNames();
		NBTTagCompound nbt = new NBTTagCompound();
		if(stack.hasTagCompound()) {
			nbt = getNBTData(stack);
			index = nbt.getInteger("index");
			contextSize = Integer.parseInt(list.get(nbt.getInteger("context"+(index*2))));
		}
		
		LittleGridContext context = LittleGridContext.get(contextSize);
		RayTraceResult res = player.rayTrace(6.0, (float) 0.1);
		LittleAbsoluteVec pos = new LittleAbsoluteVec(res, context);
		
		nbt.setString("x"+(index*2), Double.toString(pos.getPosX()));
		nbt.setString("y"+(index*2), Double.toString(pos.getPosY()));
		nbt.setString("z"+(index*2), Double.toString(pos.getPosZ()));
		System.out.println(nbt.getString("x"+(index*2))+ " "+ nbt.getString("y"+(index*2)) + " " + nbt.getString("z"+(index*2)));
		nbt.setString("facing"+(index*2), position.facing.getName());
		writeNBTData(stack, nbt);
		readNBTData(stack);
		return false;
	}
	
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean onClickBlock(World world, EntityPlayer player, ItemStack stack, PlacementPosition position, RayTraceResult result) {
		int index = 0;
		int contextSize = 16;
		List<String> list = LittleGridContext.getNames();
		NBTTagCompound nbt = new NBTTagCompound();
		if(stack.hasTagCompound()) {
			nbt = getNBTData(stack);
			index = nbt.getInteger("index");
			contextSize = Integer.parseInt(list.get(nbt.getInteger("context"+(index*2))));
		}
		LittleGridContext context = LittleGridContext.get(contextSize);
		RayTraceResult res = player.rayTrace(6.0, (float) 0.1);
		LittleAbsoluteVec pos = new LittleAbsoluteVec(res, context);


		nbt.setString("x"+((index*2)+1), Double.toString(pos.getPosX()));
		nbt.setString("y"+((index*2)+1), Double.toString(pos.getPosY()));
		nbt.setString("z"+((index*2)+1), Double.toString(pos.getPosZ()));
		nbt.setString("facing"+((index*2)+1), position.facing.getName());

		
		writeNBTData(stack, nbt);
		readNBTData(stack);
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
		return new SubGuiTapeMeasure(stack);
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
