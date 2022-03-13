package com.alet.client.gui.controls;

import com.creativemd.creativecore.common.gui.controls.gui.GuiComboBoxExtension;
import com.creativemd.creativecore.common.gui.controls.gui.custom.GuiStackSelectorAll;
import com.creativemd.creativecore.common.utils.mc.ColorUtils;

import net.minecraft.entity.player.EntityPlayer;

public class GuiStackSelectorAllMutator extends GuiStackSelectorAll {
    public int color = ColorUtils.WHITE;
    public boolean noclip = false;
    
    public GuiStackSelectorAllMutator(String name, int x, int y, int width, EntityPlayer player, StackCollector collector) {
        super(name, x, y, width, player, collector, false);
    }
    
    public GuiStackSelectorAllMutator(String name, int x, int y, int width, EntityPlayer player, StackCollector collector, boolean searchBar) {
        super(name, x, y, width, player, collector, searchBar);
    }
    
    @Override
    protected GuiComboBoxExtension createBox() {
        return new GuiStackSelectorExtensionMutator(name + "extension", getPlayer(), posX, posY + height, (width + 40) - getContentOffset() * 2, 80, this);
    }
}
