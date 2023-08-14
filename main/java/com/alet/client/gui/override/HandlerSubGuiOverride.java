package com.alet.client.gui.override;

import java.util.HashMap;

import com.creativemd.creativecore.common.gui.container.SubGui;
import com.creativemd.littletiles.client.gui.SubGuiChisel;
import com.creativemd.littletiles.client.gui.SubGuiColorTube;
import com.creativemd.littletiles.client.gui.SubGuiGrabber;
import com.creativemd.littletiles.client.gui.SubGuiParticle;
import com.creativemd.littletiles.client.gui.SubGuiRecipe;
import com.creativemd.littletiles.client.gui.configure.SubGuiGridSelector;
import com.creativemd.littletiles.client.gui.configure.SubGuiModeSelector;

public class HandlerSubGuiOverride {
    public static HashMap<Class<? extends SubGui>, SubGuiOverride> guiToMod = new HashMap<Class<? extends SubGui>, SubGuiOverride>();
    
    static {
        guiToMod.put(SubGuiChisel.class, new SubGuiOverrideChisel(false));
        guiToMod.put(SubGuiColorTube.class, new SubGuiOverrideColorTube(false));
        guiToMod.put(SubGuiGrabber.class, new SubGuiOverrideGrabber(false));
        guiToMod.put(SubGuiParticle.class, new SubGuiOverrideParticle(false));
        guiToMod.put(SubGuiColorTube.class, new SubGuiOverrideColorTube(false));
        guiToMod.put(SubGuiModeSelector.class, new SubGuiOverrideConfigure(false));
        guiToMod.put(SubGuiGridSelector.class, new SubGuiOverrideGridSelector(false));
        guiToMod.put(SubGuiRecipe.class, new SubGuiOverrideRecipe(true));
    }
    
    public static HashMap<Class<? extends SubGui>, SubGuiOverride> getGuiToMod() {
        return guiToMod;
    }
    
    public static void refreshAllGuiUpdate() {
        for (SubGuiOverride modGui : guiToMod.values()) {
            if (modGui.shouldUpdate)
                modGui.hasUpdated = false;
        }
    }
    
    private static SubGuiOverride getOverrideManager(SubGui gui) {
        for (Class<? extends SubGui> clazz : guiToMod.keySet()) {
            try {
                if (clazz.isInstance(gui)) {
                    return guiToMod.get(clazz);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            
        }
        return null;
    }
    
    public static void closeGui(SubGui gui) {
        SubGuiOverride override = getOverrideManager(gui);
        if (override != null)
            override.onClose(gui);
    }
    
    public static void updateGui(SubGui gui) {
        SubGuiOverride override = getOverrideManager(gui);
        if (override != null && override.shouldUpdate)
            override.updateControls(gui);
    }
    
    public static void overrideGuiFrom(SubGui gui) {
        SubGuiOverride override = getOverrideManager(gui);
        if (override != null)
            override.modifyControls(gui);
    }
    
}
