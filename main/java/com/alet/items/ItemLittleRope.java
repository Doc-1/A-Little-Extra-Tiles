package com.alet.items;

import com.creativemd.littletiles.LittleTiles;

import net.minecraft.item.Item;

public class ItemLittleRope extends Item {
	
	public ItemLittleRope() {
		
	}
	
	public ItemLittleRope(String name) {
		setUnlocalizedName(name);
		setRegistryName(name);
		setCreativeTab(LittleTiles.littleTab);
	}
	
}
