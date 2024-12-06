package com.alet.common.gui.container;

import com.alet.components.structures.type.premade.transfer.LittleTransferLittleHopper;
import com.creativemd.creativecore.common.gui.container.SubContainer;
import com.creativemd.creativecore.common.utils.mc.InventoryUtils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class SubContainerLittleHopper extends SubContainer {
    LittleTransferLittleHopper hopper;
    
    public SubContainerLittleHopper(EntityPlayer player, LittleTransferLittleHopper hopper) {
        super(player);
        this.hopper = hopper;
        if (!player.world.isRemote)
            this.hopper.openContainer(this);
    }
    
    @Override
    public void createControls() {
        for (int i = 0; i < 5; i++) {
            addSlotToContainer(new Slot(hopper.inventory, i, (i * 18) + 35, 15));
        }
        addPlayerSlotsToContainer(player, 0, 50);
    }
    
    @Override
    public void writeOpeningNBT(NBTTagCompound nbt) {
        nbt.setTag("inventory", InventoryUtils.saveInventoryBasic(hopper.inventory));
    }
    
    @Override
    public void onPacketReceive(NBTTagCompound nbt) {
        if (isRemote()) {
            if (nbt.hasKey("inventory")) {
                ItemStack[] stacks = InventoryUtils.loadInventory(nbt.getCompoundTag("inventory"));
                for (int i = 0; i < stacks.length; i++)
                    hopper.inventory.setInventorySlotContents(i, stacks[i]);
            }
        }
    }
    
    @Override
    public void onClosed() {
        super.onClosed();
        if (hopper != null && !player.world.isRemote)
            hopper.closeContainer(this);
    }
}
