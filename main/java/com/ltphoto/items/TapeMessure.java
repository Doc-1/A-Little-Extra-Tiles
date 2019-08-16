package com.ltphoto.items;

import java.util.List;

import javax.annotation.Nullable;

import com.creativemd.littletiles.common.tiles.vec.LittleTilePos;
import com.creativemd.littletiles.common.tiles.vec.LittleTileVec;
import com.creativemd.littletiles.common.utils.grid.LittleGridContext;

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
	
	public static int x;
	public static int y;
	public static int z;
	public static int x2;
	public static int y2;
	public static int z2;
	
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
	
	public int getPermX() {
		return makePositive(x - x2);
	}
	
	public int getPermY() {
		return makePositive(y - y2);
	}
	
	public int getPermZ() {
		return makePositive(z - z2);
	}
	
	public Vec3d getFirstPos() {
		return a;
	}
	
	public Vec3d getSecondPos() {
		return b;
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		LittleGridContext context = LittleGridContext.get(32);
		RayTraceResult res = player.rayTrace(4.0, (float) 0.1);
		Vec3d results = res.hitVec;
		this.player = player;
		this.world = worldIn;
		
		LittleTileVec vec = new LittleTileVec(context, res);
		if (player.isSneaking()) {
			a = res.hitVec;
			firstPos = new LittleTilePos(res, context);
		} else {
			b = res.hitVec;
			secondPos = new LittleTilePos(res, context);
		}
		
		if (firstPos != null && secondPos != null) {
			x = (makePositive(firstPos.contextVec.vec.x)) + 1;
			y = (makePositive(firstPos.contextVec.vec.y)) + 1;
			z = (makePositive(firstPos.contextVec.vec.z)) + 1;
			
			x2 = (makePositive(secondPos.contextVec.vec.x)) + 1;
			y2 = (makePositive(secondPos.contextVec.vec.y)) + 1;
			z2 = (makePositive(secondPos.contextVec.vec.z)) + 1;
			
			if (facing.equals(facing.UP)) {
				y--;
				y2--;
				if (y == 0) {
					y = context.size;
					y2 = context.size;
				}
			} else if (facing.equals(facing.EAST)) {
				x--;
				x2--;
			} else if (facing.equals(facing.SOUTH)) {
				z--;
				z2--;
			}
			
			System.out.println(x + " " + y + " " + z + " " + facing.toString());
		}
		
		return EnumActionResult.PASS;
	}
	
	public int makePositive(int num) {
		if (num < 0) {
			num *= -1;
		}
		return num;
	}
	
}
