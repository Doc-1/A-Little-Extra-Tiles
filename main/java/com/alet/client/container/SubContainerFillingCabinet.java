package com.alet.client.container;

import com.alet.common.structure.type.premade.LittleFillingCabinet;
import com.creativemd.creativecore.common.gui.container.SubContainer;
import com.creativemd.creativecore.common.utils.mc.WorldUtils;
import com.creativemd.littletiles.common.item.ItemLittleRecipe;
import com.creativemd.littletiles.common.item.ItemLittleRecipeAdvanced;
import com.creativemd.littletiles.common.util.converation.StructureStringUtils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class SubContainerFillingCabinet extends SubContainer {
	
	public final InventoryBasic input = new InventoryBasic("input", false, 1);
	public final InventoryBasic output = new InventoryBasic("output", false, 1);
	public LittleFillingCabinet LittleFillingCabinet;
	
	public SubContainerFillingCabinet(EntityPlayer player, LittleFillingCabinet LittleFillingCabinet) {
		super(player);
		this.LittleFillingCabinet = LittleFillingCabinet;
		if (!player.world.isRemote)
			this.LittleFillingCabinet.openContainer(this);
	}
	
	@Override
	public void createControls() {
		addSlotToContainer(new Slot(input, 0, 0, 245));
		addSlotToContainer(new Slot(output, 0, 221, 177));
		addPlayerSlotsToContainer(player, 220, 206);
	}
	
	@Override
	public void onClosed() {
		super.onClosed();
		if (LittleFillingCabinet != null && !player.world.isRemote)
			LittleFillingCabinet.closeContainer(this);
		WorldUtils.dropItem(getPlayer(), input.getStackInSlot(0));
	}
	
	@Override
	public void onPacketReceive(NBTTagCompound nbt) {
		ItemStack stack = output.getStackInSlot(0);
		if (stack.getItem() instanceof ItemLittleRecipe || stack.getItem() instanceof ItemLittleRecipeAdvanced
		        || (getPlayer().capabilities.isCreativeMode && stack.isEmpty())) {
			ItemStack newStack = StructureStringUtils.importStructure(nbt);
			if (stack.getItem() instanceof ItemLittleRecipe) {
				stack.setTagCompound(newStack.getTagCompound());
				newStack = stack;
			} else {
				if (getPlayer().isCreative() && stack.isEmpty())
					newStack.setCount(1);
				else
					newStack.setCount(stack.getCount());
			}
			output.setInventorySlotContents(0, newStack);
		}
	}
}
