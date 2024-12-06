package com.alet.common.gui.origins;

import com.creativemd.creativecore.common.gui.CoreControl;
import com.creativemd.creativecore.common.gui.container.SubGui;
import com.creativemd.creativecore.common.gui.controls.gui.GuiLabel;

import net.minecraft.client.Minecraft;

public class SubGuiLittleHopper extends SubGui {
    
    private String playerInvTitle;
    
    public SubGuiLittleHopper() {
        super(162, 126);
        playerInvTitle = Minecraft.getMinecraft().player.inventory.getDisplayName().getUnformattedText();
    }
    
    @Override
    public void createControls() {
        this.addControl(new GuiLabel(CoreControl.translate("gui.little_hopper.title"), 0, 0));
        this.addControl(new GuiLabel(playerInvTitle, 0, 36));
    }
    
}
