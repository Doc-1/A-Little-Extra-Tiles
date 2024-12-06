package com.alet.components.structures.type.premade;

import com.alet.ALET;
import com.creativemd.littletiles.common.action.LittleActionException;
import com.creativemd.littletiles.common.action.block.LittleActionActivated;
import com.creativemd.littletiles.common.structure.LittleStructure;
import com.creativemd.littletiles.common.structure.registry.LittleStructureType;
import com.creativemd.littletiles.common.tile.LittleTile;
import com.creativemd.littletiles.common.tile.parent.IStructureTileList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PickupItemPremade extends InteractivePremade {
	
	public Item itemToPickup;
	
	public PickupItemPremade(LittleStructureType type, IStructureTileList mainBlock) {
		super(type, mainBlock);
	}
	
	public PickupItemPremade(LittleStructureType type, IStructureTileList mainBlock, Item itemToPickup) {
		super(type, mainBlock);
		this.itemToPickup = itemToPickup;
	}
	
	@Override
	public ItemStack getStructureDrop() {
		if (itemToPickup != null)
			return new ItemStack(this.itemToPickup);
		
		return new ItemStack(Item.getByNameOrId(ALET.MODID + ":" + this.type.id));
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, LittleTile tile, BlockPos pos, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ, LittleActionActivated action) throws LittleActionException {
		if (worldIn.isRemote)
			return true;
		ItemStack stack = null;
		
		if (itemToPickup != null)
			stack = new ItemStack(this.itemToPickup);
		else {
			stack = new ItemStack(Item.getByNameOrId(ALET.MODID + ":" + this.type.id));
		}
		System.out.println(playerIn.inventory.getCurrentItem());
		
		if (playerIn.inventory.getCurrentItem().getItem() == Items.AIR) {
			int currentSlot = playerIn.inventory.currentItem;
			playerIn.inventory.setInventorySlotContents(currentSlot, stack);
			if (getParent() != null) {
				LittleStructure parent = getParent().getStructure();
				parent.removeDynamicChild(getParent().childId);
				parent.updateStructure();
			}
			this.onLittleTileDestroy();
		}
		return true;
	}
	
	@Override
	public void onPremadeActivated(ItemStack heldItem) {
		// TODO Auto-generated method stub
		
	}
	
}
