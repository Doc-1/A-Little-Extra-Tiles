package com.alet.components.structures.type.programable.advanced.nodes;

import java.util.ArrayList;
import java.util.List;

import com.alet.components.structures.type.programable.advanced.Function;

public class Node {
    
    //GUI Fields
    public final NodeType NODE_TYPE;
    public final String TITLE;
    public final int COLOR;
    
    //Connection Fields
    public String name;
    public Node senderConnection;
    public List<Node> receiverConnections = new ArrayList<Node>();
    public Function parent;
    public final boolean IS_MODIFIABLE;
    private boolean isSender;
    
    public Node(String name, NodeType type, String title, int color, boolean isModifiable) {
        this.COLOR = color;
        this.TITLE = title;
        this.NODE_TYPE = type;
        this.name = name;
        this.IS_MODIFIABLE = isModifiable;
    }
    
    public Node setAsSender() {
        this.isSender = true;
        return this;
    }
    
    public Node setAsReciever() {
        this.isSender = false;
        return this;
    }
    
    public boolean isSender() {
        return isSender;
    }
    
    public boolean isReciver() {
        return !isSender;
    }
    
    public enum NodeType {
        INTEGER,
        STRING,
        FUNCTION
    }
}
