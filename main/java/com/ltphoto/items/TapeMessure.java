package com.ltphoto.items;

import java.awt.Color;
import java.util.List;

import javax.annotation.Nullable;

import org.lwjgl.opengl.GL11;

import com.creativemd.littletiles.LittleTiles;
import com.creativemd.littletiles.common.action.tool.LittleActionSaw;
import com.creativemd.littletiles.common.api.ILittleTile;
import com.creativemd.littletiles.common.tileentity.TileEntityLittleTiles;
import com.creativemd.littletiles.common.tiles.LittleTile;
import com.creativemd.littletiles.common.tiles.preview.LittlePreviews;
import com.creativemd.littletiles.common.tiles.vec.LittleTileBox;
import com.creativemd.littletiles.common.tiles.vec.LittleTilePos;
import com.creativemd.littletiles.common.tiles.vec.LittleTileVec;
import com.creativemd.littletiles.common.tiles.vec.LittleTileVecContext;
import com.creativemd.littletiles.common.utils.grid.LittleGridContext;
import com.creativemd.littletiles.common.utils.placing.PlacementHelper.PositionResult;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
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
	
	public double differenceX;
	public double differenceZ;
	public double differenceY;
	
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
	
	public get
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		LittleGridContext context = LittleGridContext.get(32);
		RayTraceResult res = player.rayTrace(4.0, (float) 0.1);
		Vec3d results = res.hitVec;
		this.player = player;
		this.world = worldIn;
		
		LittleTileVec vec = new LittleTileVec(context, res);
		if(player.isSneaking()) {
			a = res.hitVec;
			firstPos = new LittleTilePos(res, context);
		}else {
			b = res.hitVec;
			secondPos = new LittleTilePos(res, context);
		}
		
		if(firstPos != null && secondPos != null) {
			int x = (makePositive(firstPos.contextVec.vec.x)) + 1;
			int y = (makePositive(firstPos.contextVec.vec.y)) + 1;
			int z = (makePositive(firstPos.contextVec.vec.z)) + 1;
			
			int x2 = (makePositive(secondPos.contextVec.vec.x)) + 1;
			int y2 = (makePositive(secondPos.contextVec.vec.y)) + 1;
			int z2 = (makePositive(secondPos.contextVec.vec.z)) + 1;
			
			if(facing.equals(facing.UP)) {
				y--;
				y2--;
				if(y == 0) {
					y = context.size;
					y2 = context.size;
				}
			}else if(facing.equals(facing.EAST)) {
				x--;
				x2--;
			}else if(facing.equals(facing.SOUTH)) {
				z--;
				z2--;
			}
			
			System.out.println(x+" "+y+" "+z+" "+facing.toString());
		}
		
		
		return EnumActionResult.PASS;
    }

	public void draw(RenderWorldLastEvent event) {
		
		double minY = player.posY;
		double minX = player.posX;
		double minZ = player.posZ;
				
		double maxY = player.posY + 5;
		double maxX = player.posX + 5;
		double maxZ = player.posZ + 5;
		
		if(world.isRemote) {
			double d0 = player.lastTickPosX + (player.posX - player.lastTickPosX) * event.getPartialTicks();
			double d1 = player.lastTickPosY + (player.posY - player.lastTickPosY) * event.getPartialTicks();
			double d2 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * event.getPartialTicks();
			
			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, 
					GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
			GlStateManager.glLineWidth(2.0F);
			GlStateManager.disableTexture2D();
			GlStateManager.depthMask(false);
			
			RenderGlobal.drawBoundingBox(minX - d0 - 0.001, minY - d1 - 0.001, minZ - d2 - 0.001, maxX + 0.001 - d0, maxY - d1 + 0.001, 
					maxZ - d2 + 0.001, (float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0);
			
			GlStateManager.depthMask(true);
			GlStateManager.enableTexture2D();
			GlStateManager.disableBlend();

		}
	}
	
	public int makePositive(int num) {
		if(num<0) {
			num *= -1;
		}
		return num;
	}
	

	
}
