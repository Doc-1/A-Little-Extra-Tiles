package com.alet.items;

import java.awt.Event;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Nullable;

import com.alet.gui.SubGuiTapeMeasure;
import com.alet.tiles.Measurement;
import com.alet.tiles.SelectLittleTile;
import com.creativemd.littletiles.LittleTiles;
import com.creativemd.littletiles.client.LittleTilesClient;
import com.creativemd.littletiles.client.event.HoldLeftClick;
import com.creativemd.littletiles.client.gui.configure.SubGuiConfigure;
import com.creativemd.littletiles.common.action.LittleAction;
import com.creativemd.littletiles.common.api.ILittleTile;
import com.creativemd.littletiles.common.container.SubContainerConfigure;
import com.creativemd.littletiles.common.item.ItemMultiTiles;
import com.creativemd.littletiles.common.tile.math.vec.LittleAbsoluteVec;
import com.creativemd.littletiles.common.tile.math.vec.LittleVec;
import com.creativemd.littletiles.common.tile.preview.LittlePreviews;
import com.creativemd.littletiles.common.util.grid.LittleGridContext;
import com.creativemd.littletiles.common.util.place.PlacementPosition;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemTapeMeasure extends Item implements ILittleTile {

	public static String shape = "";

	public void clear(ItemStack stack) {
		writeNBTData(stack, new NBTTagCompound());
	}
	
	/***
	 * 
	 * @param stack 
	 * The TapeMeasure the player is using
	 * @param index 
	 * What index the player is selected in the GUI
	 */
	public void clear(ItemStack stack, int index) {
	    NBTTagCompound nbt = stack.getTagCompound();

		List<Integer> allIndexes = new ArrayList<Integer>();
		List<String> allMatches = new ArrayList<String>();
		index *= 2;
	    System.out.println(index);
	    System.out.println(nbt.toString());

	    Matcher m1 = Pattern.compile("[a-zA-Z]+"+(index+1)).matcher(nbt.toString());
	    while (m1.find()) {
	      allMatches.add(m1.group());
	    }
	    
	    Matcher m2 = Pattern.compile("[a-zA-Z]+"+(index)).matcher(nbt.toString());
	    while (m2.find()) {
	      allMatches.add(m2.group());
	    }
	  
	    for (String key : allMatches) {
	    	if(!key.contains("context"))
	    		if(!key.contains("color"))
	    			nbt.removeTag(key);
	    	
		}

	}
	
	public ItemTapeMeasure() {
		setMax(50);
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
	
	public void writeNBTData(ItemStack stack, NBTTagCompound nbt) {
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
	public boolean onRightClick(World world, EntityPlayer plr, ItemStack stack, PlacementPosition position, RayTraceResult result) {
		int index = 0;
		int contextSize = 1;
		List<String> list = LittleGridContext.getNames();
		NBTTagCompound nbt = new NBTTagCompound();
		if(stack.hasTagCompound()) {
			nbt = getNBTData(stack);
			index = nbt.getInteger("index");
			contextSize = Integer.parseInt(list.get(nbt.getInteger("context"+(index*2))));
		}
		
		LittleGridContext context = LittleGridContext.get(contextSize);
		RayTraceResult res = plr.rayTrace(6.0, (float) 0.1);
		LittleAbsoluteVec pos = new LittleAbsoluteVec(res, context);

		double[] posOffsetted = facingOffset(pos.getPosX(), pos.getPosY(), pos.getPosZ(), contextSize, position.facing);
		nbt.setString("x"+(index*2), Double.toString(posOffsetted[0]));
		nbt.setString("y"+(index*2), Double.toString(posOffsetted[1]));
		nbt.setString("z"+(index*2), Double.toString(posOffsetted[2]));
		System.out.println(nbt.getString("x"+(index*2))+ " "+ nbt.getString("y"+(index*2)) + " " + nbt.getString("z"+(index*2)));
		nbt.setString("facing"+(index*2), position.facing.getName());
		writeNBTData(stack, nbt);
		readNBTData(stack);
		return false;
	}
	
	
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean onClickBlock(World world, EntityPlayer plr, ItemStack stack, PlacementPosition position, RayTraceResult result) {
		int index = 0;
		int contextSize = 1;
		List<String> list = LittleGridContext.getNames();
		NBTTagCompound nbt = new NBTTagCompound();
		if(stack.hasTagCompound()) {
			nbt = getNBTData(stack);
			index = nbt.getInteger("index");
			contextSize = Integer.parseInt(list.get(nbt.getInteger("context"+(index*2))));
		}
		LittleGridContext context = LittleGridContext.get(contextSize);
		RayTraceResult res = plr.rayTrace(6.0, (float) 0.1);
		LittleAbsoluteVec pos = new LittleAbsoluteVec(res, context);

		double[] posOffsetted = facingOffset(pos.getPosX(), pos.getPosY(), pos.getPosZ(), contextSize, position.facing);

		nbt.setString("x"+((index*2)+1), Double.toString(posOffsetted[0]));
		nbt.setString("y"+((index*2)+1), Double.toString(posOffsetted[1]));
		nbt.setString("z"+((index*2)+1), Double.toString(posOffsetted[2]));
		nbt.setString("facing"+((index*2)+1), position.facing.getName());

		
		writeNBTData(stack, nbt);
		readNBTData(stack);
		return false;
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent 
	public boolean onClearKeyPress(RenderWorldLastEvent event) {
		if(LittleTilesClient.configureAdvanced.isPressed()) {
			
		}
		return false;
	}
	
	public static double[] facingOffset(double x, double y, double z, int contextSize, EnumFacing facing) {
		double offset = 1D/contextSize;
		switch (facing) {
		case UP:
			y -= offset;
			break;
		case EAST:
			x -= offset;
			break;
		case SOUTH:
			z -= offset;
			break;
		default:
			break;
		}
		double[] arr = {x, y, z};
		return arr;
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
	public SubGuiConfigure getConfigureGUIAdvanced(EntityPlayer player, ItemStack stack) {
		if(stack.getItem() instanceof ItemTapeMeasure && stack.hasTagCompound()) 
			clear(player.getHeldItemMainhand(), player.getHeldItemMainhand().getTagCompound().getInteger("index"));
		return null;
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
