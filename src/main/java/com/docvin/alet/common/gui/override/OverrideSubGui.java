package com.docvin.alet.common.gui.override;

import com.creativemd.creativecore.common.gui.container.SubGui;

import java.util.HashMap;
import java.util.function.Consumer;

public class OverrideSubGui<K extends SubGui> {

    private static final HashMap<Class<? extends SubGui>, Consumer<SubGui>> overrideMap = new HashMap<>();

    OverrideSubGui(Class<? extends SubGui> key, Consumer<K> override) {
        overrideMap.put(key, (Consumer<SubGui>) override);
    }

    public static void overrideGui(SubGui gui) {
        if (overrideMap.containsKey(gui.getClass()))
            overrideMap.get(gui.getClass()).accept(gui);

    }

}
