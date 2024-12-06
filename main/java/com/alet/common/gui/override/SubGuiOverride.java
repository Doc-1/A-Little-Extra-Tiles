package com.alet.common.gui.override;

import com.creativemd.creativecore.common.gui.container.SubGui;

public class SubGuiOverride {
    
    public boolean shouldUpdate = false;
    public boolean hasUpdated = false;
    
    public SubGuiOverride(boolean shouldUpdate) {
        this.shouldUpdate = shouldUpdate;
    }
    
    public void modifyControls(SubGui modGui) {
        
    }
    
    public void updateControls(SubGui gui) {
        // TODO Auto-generated method stub
        
    }
    
    public void onClose(SubGui gui) {
        
    }
}
