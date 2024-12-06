package com.alet.common.gui.container;

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

public class SubContainerBlock extends SubContainer {
    
    public final InventoryBasic slot = new InventoryBasic("slot", false, 1);
    
    public SubContainerBlock(EntityPlayer player) {
        super(player);
        
    }
    
    @Override
    public void createControls() {
        addSlotToContainer(new Slot(slot, 0, 0, 0));
        addPlayerSlotsToContainer(player);
    }
    
    @Override
    public void onPacketReceive(NBTTagCompound nbt) {
        ItemStack stack = slot.getStackInSlot(0);
        if (stack.getItem() instanceof ItemLittleRecipe || stack
                .getItem() instanceof ItemLittleRecipeAdvanced || (getPlayer().capabilities.isCreativeMode && stack
                        .isEmpty())) {
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
            slot.setInventorySlotContents(0, newStack);
        }
    }
    
    @Override
    public void onClosed() {
        super.onClosed();
        WorldUtils.dropItem(getPlayer(), slot.getStackInSlot(0));
    }
}
