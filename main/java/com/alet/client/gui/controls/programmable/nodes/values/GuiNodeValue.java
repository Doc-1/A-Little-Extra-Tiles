package com.alet.client.gui.controls.programmable.nodes.values;

import com.alet.client.gui.controls.programmable.nodes.GuiNode;

public class GuiNodeValue<V> extends GuiNode {
    
    V v;
    
    public GuiNodeValue(String name, String title, int color, boolean isSender, boolean isReciever, boolean isModifiable) throws Exception {
        super(name, title, color, isSender, isReciever, isModifiable);
    }
    
    public void setValue(V v) {
        
    }
    
}
