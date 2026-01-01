package com.docvin.alet.components.gui.controls.hierarchy.tree;

public class GuiTreeDataNode<V> extends GuiTreeNode {

    V value;

    public GuiTreeDataNode(String name, String title) {
        super(name, title);
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }
}
