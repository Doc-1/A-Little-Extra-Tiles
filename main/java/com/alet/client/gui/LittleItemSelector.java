package com.alet.client.gui;

import com.creativemd.creativecore.common.gui.controls.gui.custom.GuiStackSelectorAll.SearchSelector;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public class LittleItemSelector extends SearchSelector {
	
	@Override
	public boolean allow(ItemStack stack) {
		if (super.allow(stack))
			return Block.getBlockFromItem(stack.getItem()) == Blocks.AIR ? true : false;
		return false;
	}
	
}
