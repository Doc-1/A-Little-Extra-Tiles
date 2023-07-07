package com.alet.client.gui.controls.programmable.blueprints.values;

import com.alet.client.gui.controls.programmable.blueprints.GuiBlueprint;
import com.creativemd.creativecore.common.gui.GuiControl;
import com.creativemd.creativecore.common.gui.controls.gui.GuiTextfield;
import com.creativemd.creativecore.common.gui.event.ControlEvent;
import com.creativemd.creativecore.common.gui.event.gui.GuiControlChangedEvent;

public abstract class BlueprintValue extends GuiBlueprint {
    
    public BlueprintValue(int id) {
        super(id);
    }
    
    public abstract void controlChanged(GuiControl event);
    
    @Override
    public boolean raiseEvent(ControlEvent event) {
        if (event instanceof GuiControlChangedEvent && event.source instanceof GuiTextfield) {
            controlChanged((GuiControl) event.source);
        }
        return super.raiseEvent(event);
    }
}
