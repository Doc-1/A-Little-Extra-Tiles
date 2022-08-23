package com.alet.client.gui;

import com.alet.common.structure.type.premade.transfer.LittleTransferLittleHopper;
import com.creativemd.creativecore.common.gui.container.SubGui;

public class SubGuiLittleHopper extends SubGui {
    
    private LittleTransferLittleHopper hopper;
    
    public SubGuiLittleHopper(LittleTransferLittleHopper hopper) {
        super(250, 250);
        this.hopper = hopper;
    }
    
    @Override
    public void createControls() {
        
    }
    
}
