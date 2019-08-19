package com.ltphoto.items;

import java.util.List;

import javax.annotation.Nullable;

import com.creativemd.littletiles.common.tiles.vec.LittleTilePos;
import com.creativemd.littletiles.common.tiles.vec.LittleTileVec;
import com.creativemd.littletiles.common.utils.grid.LittleGridContext;
import com.ltphoto.render.SelectLittleTile;

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

public class TapeMessure extends Item {
	
	public LittleTilePos firstPos;
	public LittleTilePos secondPos;
	
	public Minecraft mc = Minecraft.getMinecraft();
	public EntityPlayer player;
	public World world;
	
	public Vec3d a;
	public Vec3d b;
	
	public SelectLittleTile select = new SelectLittleTile();
	public SelectLittleTile select_2 = new SelectLittleTile();
	
	public static double differenceX;
	public static double differenceZ;
	public static double differenceY;
	
	public TapeMessure() {
		
	}
	
	public TapeMessure(String name) {
		setUnlocalizedName(name);
		setRegistryName(name);
		setMaxStackSize(1);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add("rightclick to increase and");
		tooltip.add("shift+rightclick to decrease");
		tooltip.add("the size of a placed tile");
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		LittleGridContext context = LittleGridContext.get(16);
		RayTraceResult res = player.rayTrace(4.0, (float) 0.1);
		LittleTileVec vec = new LittleTileVec(context, res);
		
		this.player = player;
		this.world = worldIn;
		double cont = 1 / context.size;
		
		if (player.isSneaking()) {
			a = res.hitVec;
			firstPos = new LittleTilePos(res, context);
			select = new SelectLittleTile(firstPos, context, facing);
		} else {
			b = res.hitVec;
			secondPos = new LittleTilePos(res, context);
			select_2 = new SelectLittleTile(secondPos, context, facing);
		}
		
		if (firstPos != null && secondPos != null) {
			
			System.out.println();
			
			String side = facing.toString();
			
			/* if (minX < maxX && minY < maxY && minZ > maxZ && facing.toString().equals("east")) {
			 * maxY += 0.03125;
			 * } else if (minY > maxY && minX < maxX && facing.toString().equals("east")) {
			 * 
			 * } else if (minY < maxY && minX < maxX) {
			 * maxY += 0.03125;
			 * maxX += 0.03125;
			 * } else if (minY > maxY && minX < maxX) {
			 * maxX += 0.03125;
			 * System.out.println("5");
			 * } else if (minY < maxY && minZ < maxZ) {
			 * maxZ += 0.03125;
			 * } else if (minY > maxY && minZ > maxZ) {
			 * minY += 0.03125;
			 * } else if (minY == maxY && minX < maxX && minZ > maxZ) {
			 * maxY += 0.03125;
			 * } else if (minY > maxY && minX > maxX) {
			 * minY += 0.03125;
			 * minX += 0.03125;
			 * } else if (minY > maxY && minX < maxX) {
			 * maxX += 0.03125;
			 * minY += 0.03125;
			 * } else if (minY < maxY && minX > maxX) {
			 * maxY += 0.03125;
			 * minX += 0.03125;
			 * } else if (minY == maxY && minX < maxX) {
			 * maxY += 0.03125;
			 * maxX += 0.03125;
			 * } else if (minY == maxY && minX > maxX) {
			 * maxY += 0.03125;
			 * minX += 0.03125;
			 * } else if (minX == maxX && minY < maxY) {
			 * maxY += 0.03125;
			 * minX += 0.03125;
			 * } else if (minX == maxX && minY > maxY) {
			 * minY += 0.03125;
			 * minX += 0.03125;
			 * } else if (minX == maxX && minY == maxY && minZ == maxZ) {
			 * maxY += 0.03125;
			 * minX += 0.03125;
			 * } */
		}
		
		return EnumActionResult.PASS;
	}
	
	public double makePositive(double num) {
		if (num < 0) {
			num *= -1;
		}
		return num;
	}
	
	/* x = (makePositive(firstPos.getPosX())) + cont;
	 * y = (makePositive(firstPos.getPosY())) + cont;
	 * z = (makePositive(firstPos.getPosZ())) + cont;
	 * 
	 * x2 = (makePositive(secondPos.getPosX())) + cont;
	 * y2 = (makePositive(secondPos.getPosY())) + cont;
	 * z2 = (makePositive(secondPos.getPosZ())) + cont; */
	
	/* if (facing.equals(facing.UP)) {
	 * y--;
	 * y2--;
	 * if (y == 0) {
	 * y = context.size;
	 * y2 = context.size;
	 * }
	 * } else if (facing.equals(facing.EAST)) {
	 * x--;
	 * x2--;
	 * } else if (facing.equals(facing.SOUTH)) {
	 * z--;
	 * z2--;
	 * } */
	
}
