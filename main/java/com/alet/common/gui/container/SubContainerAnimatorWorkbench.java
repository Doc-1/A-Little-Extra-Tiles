package com.alet.common.gui.container;

import com.alet.components.structures.type.premade.LittleAnimatorBench;
import com.creativemd.creativecore.common.gui.container.SubContainer;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.nbt.NBTTagCompound;

public class SubContainerAnimatorWorkbench extends SubContainer {
    
    public final InventoryBasic input = new InventoryBasic("input", false, 1);
    LittleAnimatorBench structure;
    
    public SubContainerAnimatorWorkbench(EntityPlayer player, LittleAnimatorBench structure) {
        super(player);
        this.structure = structure;
    }
    
    @Override
    public void createControls() {
        addSlotToContainer(new Slot(input, 0, 144, 80));
        addPlayerSlotsToContainer(player, 144, 0);
    }
    
    @Override
    public void onPacketReceive(NBTTagCompound nbt) {
        
    }
    
}
