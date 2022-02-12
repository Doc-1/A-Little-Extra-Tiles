package com.alet.client.container;

import com.creativemd.creativecore.common.gui.container.SubContainer;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.nbt.NBTTagCompound;

public class SubContainerAnimatorWorkbench extends SubContainer {
    
    public final InventoryBasic input = new InventoryBasic("input", false, 1);
    
    public SubContainerAnimatorWorkbench(EntityPlayer player) {
        super(player);
        
    }
    
    @Override
    public void createControls() {
        addSlotToContainer(new Slot(input, 0, 0, 0));
        addPlayerSlotsToContainer(player, 0, 124);
        
    }
    
    @Override
    public void onPacketReceive(NBTTagCompound nbt) {
        
    }
    
}
