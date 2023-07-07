package com.alet.client.gui.controls.menu;

public class GuiTreePartHolder<K> extends GuiTreePart {
    
    public K key;
    
    public GuiTreePartHolder(String name, String caption, EnumPartType type, K k) {
        super(name, caption, type);
        this.key = k;
    }
    
}
