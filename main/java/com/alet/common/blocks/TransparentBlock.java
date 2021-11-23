package com.alet.common.blocks;

import java.util.Random;

import net.minecraft.block.BlockBreakable;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TransparentBlock extends BlockBreakable {
	
	public TransparentBlock(String name) {
		super(Material.GLASS, false);
		setUnlocalizedName(name);
		setRegistryName(name);
	}
	
	public int quantityDropped(Random random) {
		return 0;
	}
	
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}
	
	public boolean isFullCube(IBlockState state) {
		return false;
	}
}
