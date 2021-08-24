package com.alet.common.blocks;

import java.util.List;

import com.alet.ALET;
import com.creativemd.littletiles.LittleTiles;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class BasicBlock extends Block {
	
	public BasicBlock(String name) {
		super(Material.ROCK);
		setUnlocalizedName(name);
		setRegistryName(name);
		setHardness(1.5F);
		setCreativeTab(LittleTiles.littleTab);
	}
	
	@Override
	public void addInformation(ItemStack stack, World player, List<String> tooltip, ITooltipFlag advanced) {
		if (Block.getBlockFromItem(stack.getItem()).equals(ALET.smoothOakPlank) || Block.getBlockFromItem(stack.getItem()).equals(ALET.smoothBrick)) {
			tooltip.add("Requires Optifine");
			super.addInformation(stack, player, tooltip, advanced);
		}
	}
	
}
