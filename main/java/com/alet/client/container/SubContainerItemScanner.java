package com.alet.client.container;

import com.alet.inventory.InventoryCopier;
import com.creativemd.creativecore.common.gui.container.SubContainer;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.nbt.NBTTagCompound;

public class SubContainerItemScanner extends SubContainer {
    
    public final InventoryCopier slot = new InventoryCopier("whitelist", false, 16);
    
    public SubContainerItemScanner(EntityPlayer player) {
        super(player);
        // TODO Auto-generated constructor stub
    }
    
    @Override
    public void createControls() {
        slot.addInventoryChangeListener((x) -> onInventoryChanged());
        int slotPos = 0;
        for (int x = 0; x < 16; x++) {
            slotPos = 18 * x;
            addSlotToContainer(new Slot(slot, x, slotPos, 0));
        }
        addPlayerSlotsToContainer(player, 0, 114);
        
    }
    
    private void onInventoryChanged() {
        System.out.println("");
    }
    
    @Override
    public void onPacketReceive(NBTTagCompound nbt) {
        System.out.println("da");
    }
    
}
