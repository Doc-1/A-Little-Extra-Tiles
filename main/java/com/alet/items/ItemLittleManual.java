package com.alet.items;

import com.creativemd.creativecore.common.gui.opener.GuiHandler;
import com.creativemd.littletiles.LittleTiles;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemLittleManual extends Item {
	
	public ItemLittleManual(String name) {
		setUnlocalizedName(name);
		setRegistryName(name);
		setCreativeTab(LittleTiles.littleTab);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		if (worldIn.isRemote)
			GuiHandler.openGui("manual", new NBTTagCompound(), playerIn);
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}
}
