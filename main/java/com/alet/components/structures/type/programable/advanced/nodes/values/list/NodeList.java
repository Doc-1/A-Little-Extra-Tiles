package com.alet.components.structures.type.programable.advanced.nodes.values.list;

import java.util.ArrayList;

import com.alet.components.structures.type.programable.advanced.nodes.values.NodeValue;

public abstract class NodeList<V> extends NodeValue<ArrayList<V>> {
    
    public NodeList(String name, int color, boolean isSender, boolean isReciever, boolean isModifiable) {
        super(name, NodeType.INTEGER, name, color, isModifiable);
        // TODO Auto-generated constructor stub
    }
    
}
