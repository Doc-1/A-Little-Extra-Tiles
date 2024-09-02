package com.alet.client.gui.controls.programmable.nodes.values;

import java.util.ArrayList;

public class GuiNodeValueList<V> extends GuiNodeValue<ArrayList<V>> {
    
    V v;
    
    public GuiNodeValueList(String name, String title, int color, boolean isSender, boolean isReciever, boolean isModifiable) throws Exception {
        super(name, title, color, isModifiable);
    }
    
}
