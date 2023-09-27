package com.alet.common.structure.type.programable.nodes.values.list;

import java.util.ArrayList;

import com.alet.common.structure.type.programable.nodes.values.NodeValue;

public abstract class NodeList<V> extends NodeValue<ArrayList<V>> {
    
    public NodeList(String name, int color, boolean isSender, boolean isReciever, boolean isModifiable) {
        super(name, name, name, color, isSender, isReciever, isModifiable);
        // TODO Auto-generated constructor stub
    }
    
}
