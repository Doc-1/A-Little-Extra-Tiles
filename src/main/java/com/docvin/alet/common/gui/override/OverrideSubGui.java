package com.docvin.alet.common.gui.override;


import com.creativemd.creativecore.common.gui.container.SubGui;

import java.util.HashMap;

public abstract class OverrideSubGui<K extends SubGui> {

    private static final HashMap<Class<? extends SubGui>, OverrideSubGui<?>> overrideMap = new HashMap<>();
    K subGui;

    OverrideSubGui(Class<? extends SubGui> key) {
        overrideMap.put(key, this);
    }

    public static OverrideSubGui<?> getGuiOverride(SubGui gui) {
        if (overrideMap.containsKey(gui.getClass())) {
            return overrideMap.get(gui.getClass());
        }
        return null;
    }

    public abstract void onScreenResized(K gui, int width, int height);

    public abstract void overrideGui(K gui);

    public K getSubGui() {
        return subGui;
    }

}
