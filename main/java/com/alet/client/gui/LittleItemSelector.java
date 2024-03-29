package com.alet.client.gui;

import com.creativemd.creativecore.common.gui.controls.gui.custom.GuiStackSelectorAll;
import com.creativemd.creativecore.common.gui.controls.gui.custom.GuiStackSelectorAll.SearchSelector;
import com.creativemd.creativecore.common.gui.controls.gui.custom.GuiStackSelectorAll.StackCollector;
import com.creativemd.creativecore.common.utils.mc.BlockUtils;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class LittleItemSelector extends SearchSelector {
    
    @Override
    public boolean allow(ItemStack stack) {
        if (super.allow(stack))
            return !this.isBlockValid(BlockUtils.getState(stack)) && !stack.getItem().getCreatorModId(stack).equals("littletiles");
        return false;
    }
    
    public static StackCollector getCollector(EntityPlayer player) {
        if (player.isCreative())
            return new GuiStackSelectorAll.CreativeCollector(new LittleItemSelector());
        return new GuiStackSelectorAll.InventoryCollector(new LittleItemSelector());
    }
    
    public static boolean isBlockValid(IBlockState state) {
        
        return false;
    }
}
