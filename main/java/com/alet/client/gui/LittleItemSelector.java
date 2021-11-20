package com.alet.client.gui;

import com.creativemd.creativecore.common.gui.controls.gui.custom.GuiStackSelectorAll;
import com.creativemd.creativecore.common.gui.controls.gui.custom.GuiStackSelectorAll.SearchSelector;
import com.creativemd.creativecore.common.gui.controls.gui.custom.GuiStackSelectorAll.StackCollector;
import com.creativemd.creativecore.common.utils.mc.BlockUtils;
import com.creativemd.littletiles.common.mod.chiselsandbits.ChiselsAndBitsManager;
import com.creativemd.littletiles.common.mod.coloredlights.ColoredLightsManager;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBreakable;
import net.minecraft.block.BlockGlass;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStainedGlass;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class LittleItemSelector extends SearchSelector {
	
	@Override
	public boolean allow(ItemStack stack) {
		if (super.allow(stack))
			return !this.isBlockValid(BlockUtils.getState(stack))
			        && !stack.getItem().getCreatorModId(stack).equals("littletiles");
		return false;
	}
	
	public static StackCollector getCollector(EntityPlayer player) {
		if (player.isCreative())
			return new GuiStackSelectorAll.CreativeCollector(new LittleItemSelector());
		return new GuiStackSelectorAll.InventoryCollector(new LittleItemSelector());
	}
	
	public static boolean isBlockValid(IBlockState state) {
		Block block = state.getBlock();
		if (ChiselsAndBitsManager.isChiselsAndBitsStructure(state))
			return true;
		if (ColoredLightsManager.isBlockFromColoredBlocks(block))
			return true;
		if (block.hasTileEntity(state) || block instanceof BlockSlab || block instanceof BlockStairs)
			return true;
		return state.isNormalCube() || state.isFullCube() || state.isFullBlock() || block instanceof BlockGlass
		        || block instanceof BlockStainedGlass || block instanceof BlockBreakable;
	}
}
