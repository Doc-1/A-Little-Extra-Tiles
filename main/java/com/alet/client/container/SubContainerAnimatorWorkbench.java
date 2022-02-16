package com.alet.client.container;

import com.alet.common.structure.type.premade.signal.LittleCircuitMicroprocessor;
import com.creativemd.creativecore.common.gui.container.SubContainer;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.nbt.NBTTagCompound;

public class SubContainerAnimatorWorkbench extends SubContainer {
    
    public final InventoryBasic input = new InventoryBasic("input", false, 1);
    LittleCircuitMicroprocessor structure;
    
    public SubContainerAnimatorWorkbench(EntityPlayer player, LittleCircuitMicroprocessor structure) {
        super(player);
        this.structure = structure;
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
