package com.alet.items;

import java.awt.Event;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Nullable;

import org.lwjgl.util.Color;

import com.alet.common.packet.PacketUpdateNBT;
import com.alet.common.util.TapeMeasureKeyEventHandler;
import com.alet.gui.SubGuiTapeMeasure;
import com.alet.render.tapemeasure.TapeRenderer;
import com.alet.render.tapemeasure.shape.Box;
import com.alet.render.tapemeasure.shape.Line;
import com.alet.tiles.Measurement;
import com.alet.tiles.SelectLittleTile;
import com.creativemd.creativecore.client.key.ExtendedKeyBinding;
import com.creativemd.creativecore.common.packet.PacketHandler;
import com.creativemd.creativecore.common.utils.math.Rotation;
import com.creativemd.creativecore.common.utils.mc.ColorUtils;
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

import io.netty.buffer.ByteBuf;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SPacketEntityEquipment;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemTapeMeasure extends Item implements ILittleTile {
	
	public static PosData data;
	
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
	    	if(!key.contains("context") && !key.contains("color") && !key.contains("shape"))
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
		nbt.setString("facing"+(index*2), position.facing.getName());
		
		writeNBTData(stack, nbt);
		PacketHandler.sendPacketToServer(new PacketUpdateNBT(nbt));
		
		return false;
	}
	
	@Override
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
		
		PacketHandler.sendPacketToServer(new PacketUpdateNBT(nbt));
		
		return false;
	}
	
	@Override
	public void onDeselect(EntityPlayer player, ItemStack stack) {
		
	}
	
	public class PosData {
		public SelectLittleTile tilePosMin;
		public SelectLittleTile tilePosMax;
		public SelectLittleTile tilePosCursor;
		public RayTraceResult result;
		
		public PosData(SelectLittleTile posMin, SelectLittleTile posMax, SelectLittleTile posCursor, RayTraceResult res) {
			tilePosMin = posMin;
			tilePosMax = posMax;
			tilePosCursor = posCursor;
			result = res;
		}
	}
	
	@Override
	public void tickPreview(EntityPlayer player, ItemStack stack, PlacementPosition position, RayTraceResult result) {
		NBTTagCompound nbt = stack.getTagCompound();
		List<String> list = LittleGridContext.getNames();

		if(nbt.hasKey("index")) {
			int index = nbt.getInteger("index")*2;
			int index1 = index;
			int index2 = index+1;
			int contextSize = (nbt.hasKey("context"+index1)) ? Integer.parseInt(list.get(nbt.getInteger("context"+index1))) : Integer.parseInt(list.get(0));
			
			LittleAbsoluteVec pos = new LittleAbsoluteVec(result, LittleGridContext.get(contextSize));
			double[] posEdit = facingOffset(pos.getPosX(), pos.getPosY(), pos.getPosZ(), contextSize, result.sideHit);
			
			SelectLittleTile tilePosMin = new SelectLittleTile(new Vec3d(posEdit[0], posEdit[1], posEdit[2]), LittleGridContext.get(contextSize));
			SelectLittleTile tilePosMax = new SelectLittleTile(new Vec3d(posEdit[0], posEdit[1], posEdit[2]), LittleGridContext.get(contextSize));
			SelectLittleTile tilePosCursor = new SelectLittleTile(new Vec3d(posEdit[0], posEdit[1], posEdit[2]), LittleGridContext.get(contextSize));
			data = new PosData(tilePosMin, tilePosMax, tilePosCursor, result);
			
			TapeRenderer.renderCursor(nbt, index1, contextSize, tilePosCursor);
		}
	}

	
	public void onKeyPress(int pressedKey, EntityPlayer player, ItemStack stack) {
		if(pressedKey == TapeMeasureKeyEventHandler.CLEAR) {
			clear(stack, stack.getTagCompound().getInteger("index"));
		}
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
	public void rotateLittlePreview(EntityPlayer player, ItemStack stack, Rotation rotation) {
		NBTTagCompound nbt = stack.hasTagCompound() ? stack.getTagCompound() : new NBTTagCompound();
		if(nbt.hasKey("index")) {
			int index = nbt.getInteger("index");
	
			if(rotation == Rotation.Z_CLOCKWISE) 
				index--;
			if(rotation == Rotation.Z_COUNTER_CLOCKWISE) 
				index++;
			
			if(index > 9) 
				index = 0;
			else if(index < 0) 
				index = 9;
			
			nbt.setInteger("index", index);
			
			stack.setTagCompound(nbt);
			
		}
	}
	
	@Override
	public void flipLittlePreview(EntityPlayer player, ItemStack stack, Axis axis) { 
		
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
		
		return null;
	}
	
	@Override
	public SubContainerConfigure getConfigureContainer(EntityPlayer player, ItemStack stack) {
		return new SubContainerConfigure(player, stack);
	}
	
	@Override
	public boolean hasLittlePreview(ItemStack stack) {
		return true;
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
