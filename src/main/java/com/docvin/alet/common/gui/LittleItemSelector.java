package com.docvin.alet.common.gui;

import com.creativemd.creativecore.common.gui.controls.gui.custom.GuiStackSelectorAll;
import com.creativemd.creativecore.common.gui.controls.gui.custom.GuiStackSelectorAll.SearchSelector;
import com.creativemd.creativecore.common.gui.controls.gui.custom.GuiStackSelectorAll.StackCollector;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class LittleItemSelector extends SearchSelector {

    public static StackCollector getCollector(EntityPlayer player) {
        if (player.isCreative())
            return new GuiStackSelectorAll.CreativeCollector(new LittleItemSelector());
        return new GuiStackSelectorAll.InventoryCollector(new LittleItemSelector());
    }

    @Override
    public boolean allow(ItemStack stack) {
        if (super.allow(stack))
            return !stack.getItem().getCreatorModId(stack).equals("littletiles");
        return false;
    }

}
