package com.ltphoto.container;

import com.creativemd.creativecore.common.gui.container.SubContainer;
import com.creativemd.creativecore.common.utils.mc.WorldUtils;
import com.creativemd.littletiles.LittleTiles;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class SubContainerPhotoImport extends SubContainer {
	
	public final static InventoryBasic slot = new InventoryBasic("slot", false, 1);
	public static ItemStack itemStack;
	public SubContainerPhotoImport(EntityPlayer player) {
		super(player);
	}
	
	@Override
	public void createControls() {
		addSlotToContainer(new Slot(slot, 0, 10, 10));
		addPlayerSlotsToContainer(player);
	}
	
	@Override
	public void onPacketReceive(NBTTagCompound nbt) {
		itemStack.setCount(1);
		slot.setInventorySlotContents(0, itemStack);
	}
	
	public static void setItemStack(ItemStack stack) {
		itemStack = stack;
	}
	
	@Override
	public void onClosed() {
		super.onClosed();
		WorldUtils.dropItem(getPlayer(), slot.getStackInSlot(0));
	}
}
