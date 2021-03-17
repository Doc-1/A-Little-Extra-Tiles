package com.alet.items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.vecmath.Vector3d;

import com.creativemd.creativecore.client.rendering.RenderBox;
import com.creativemd.creativecore.client.rendering.model.ICreativeRendered;
import com.creativemd.creativecore.common.utils.mc.ColorUtils;
import com.creativemd.littletiles.client.render.cache.ItemModelCache;
import com.creativemd.littletiles.common.particle.LittleParticle;
import com.creativemd.littletiles.common.particle.LittleParticleTexture;
import com.creativemd.littletiles.common.structure.registry.LittleStructureRegistry;
import com.creativemd.littletiles.common.structure.type.premade.LittleParticleEmitter.ParticleSettings;
import com.creativemd.littletiles.common.structure.type.premade.LittleStructurePremade;
import com.creativemd.littletiles.common.structure.type.premade.LittleStructurePremade.LittleStructurePremadeEntry;
import com.creativemd.littletiles.common.structure.type.premade.LittleStructurePremade.LittleStructureTypePremade;
import com.creativemd.littletiles.common.tile.preview.LittlePreview;
import com.creativemd.littletiles.common.tile.preview.LittlePreviews;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemJumpTool extends Item implements ICreativeRendered {
	
	public String premadeToPlace = "";
	public String premadeToRender = "";
	public boolean isShifting = false;
	
	public ItemJumpTool(String registryName) {
		
		this.premadeToPlace = "jump_rod";
		this.premadeToRender = "jump_rod";
		setUnlocalizedName(registryName);
		setRegistryName(registryName);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void applyCustomOpenGLHackery(ItemStack stack, TransformType cameraTransformType) {
		Minecraft mc = Minecraft.getMinecraft();
		
		if (cameraTransformType == TransformType.FIRST_PERSON_RIGHT_HAND) {
			GlStateManager.scale(2.0D, 2.0D, 2.0D);
			GlStateManager.translate(0.05D, .1D, -0.02D);
			
			GlStateManager.rotate(34.0F, 0.0F, 1.0F, 0.0F);
			GlStateManager.rotate(45.0F, 1.0F, 0.0F, 0.0F);
			GlStateManager.rotate(35.0F, 0.0F, 0.0F, 1.0F);
			
			//GlStateManager.scale(1.0D, 1.0D, 1.0D);
		}
		
		if (cameraTransformType == TransformType.GUI)
			GlStateManager.scale(1.6D, 1.6D, 1.6D);
		
		if (cameraTransformType == cameraTransformType.THIRD_PERSON_RIGHT_HAND) {
			GlStateManager.scale(2.2D, 2.2D, 2.2D);
			GlStateManager.translate(0.1D, -.02D, 0.10D);
			GlStateManager.rotate(3.0F, 0.0F, 1.0F, 0.0F);
			GlStateManager.rotate(5.0F, 1.0F, 0.0F, 0.0F);
			GlStateManager.rotate(30.0F, 0.0F, 0.0F, 1.0F);
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public List<RenderBox> getRenderingCubes(IBlockState state, TileEntity te, ItemStack stack) {
		LittleStructureTypePremade premade = (LittleStructureTypePremade) LittleStructureRegistry.getStructureType(premadeToRender);
		LittlePreviews previews = LittleStructurePremade.getPreviews(premade.id).copy();
		List<RenderBox> cubes = premade.getRenderingCubes(previews);
		if (cubes == null) {
			cubes = new ArrayList<>();
			
			for (LittlePreview preview : previews.allPreviews())
				cubes.add(preview.getCubeBlock(previews.getContext()));
			
			LittlePreview.shrinkCubesToOneBlock(cubes);
		}
		return cubes;
	}
	
	@SideOnly(Side.CLIENT)
	public static IBakedModel model;
	
	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		isShifting = entityIn.isSneaking();
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer player, EnumHand handIn) {
		EnumActionResult result = EnumActionResult.PASS;
		player.addExhaustion(0.1f);
		player.getCooldownTracker().setCooldown(player.getHeldItemMainhand().getItem(), 50);
		player.motionY = 0.6D;
		result = EnumActionResult.SUCCESS;
		
		if (!worldIn.isRemote)
			renderParticles(worldIn, player);
		
		return ActionResult.newResult(result, player.getHeldItemMainhand());
	}
	
	@SideOnly(Side.CLIENT)
	public void renderParticles(World worldIn, EntityPlayer player) {
		Minecraft mc = Minecraft.getMinecraft();
		ParticleSettings settings = new ParticleSettings(0.0F, ColorUtils.RGBAToInt(127, 20, 239, 255), 40, 0, 1, 1, 0, LittleParticleTexture.dust_fade_out, false);
		double rand = ((Math.random() * (0.5 - 0)) + 0);
		double rand2 = ((Math.random() * (0.5 - 0)) + 0);
		mc.effectRenderer.addEffect(new LittleParticle(worldIn, new Vector3d(player.posX, player.posY + 1, player.posZ + rand), new Vector3d(0D, -0.2D, 0D), settings));
		mc.effectRenderer.addEffect(new LittleParticle(worldIn, new Vector3d(player.posX - rand, player.posY + 2 - rand2, player.posZ), new Vector3d(0D, -0.2D, 0D), settings));
		mc.effectRenderer.addEffect(new LittleParticle(worldIn, new Vector3d(player.posX + rand2, player.posY + 2, player.posZ + rand), new Vector3d(0D, -0.2D, 0D), settings));
		mc.effectRenderer.addEffect(new LittleParticle(worldIn, new Vector3d(player.posX - rand, player.posY + 2 + rand, player.posZ + rand2), new Vector3d(0D, -0.2D, 0D), settings));
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void saveCachedModel(EnumFacing facing, BlockRenderLayer layer, List<BakedQuad> cachedQuads, IBlockState state, TileEntity te, ItemStack stack, boolean threaded) {
		stack = LittleStructurePremade.getPremadeStack(premadeToRender);
		if (stack != null)
			ItemModelCache.cacheModel(getPremade(stack).stack, facing, cachedQuads);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public List<BakedQuad> getCachedModel(EnumFacing facing, BlockRenderLayer layer, IBlockState state, TileEntity te, ItemStack stack, boolean threaded) {
		stack = LittleStructurePremade.getPremadeStack(premadeToRender);
		if (stack == null)
			return null;
		LittleStructurePremadeEntry entry = getPremade(stack);
		if (entry == null)
			return null;
		return ItemModelCache.requestCache(entry.stack, facing);
		
	}
	
	public void removeUnnecessaryData(ItemStack stack) {
		if (stack.hasTagCompound()) {
			stack.getTagCompound().removeTag("tiles");
			stack.getTagCompound().removeTag("size");
			stack.getTagCompound().removeTag("min");
		}
	}
	
	private static HashMap<String, LittlePreviews> cachedPreviews = new HashMap<>();
	
	public static void clearCache() {
		cachedPreviews.clear();
	}
	
	public static String getPremadeId(ItemStack stack) {
		if (stack.hasTagCompound())
			return stack.getTagCompound().getCompoundTag("structure").getString("id");
		return null;
	}
	
	public static LittleStructurePremadeEntry getPremade(ItemStack stack) {
		if (stack.hasTagCompound())
			return LittleStructurePremade.getStructurePremadeEntry(stack.getTagCompound().getCompoundTag("structure").getString("id"));
		return null;
	}
}
