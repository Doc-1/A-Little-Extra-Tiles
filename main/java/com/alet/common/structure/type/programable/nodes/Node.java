package com.alet.common.structure.type.programable.nodes;

import java.util.ArrayList;
import java.util.List;

import com.alet.common.structure.type.programable.advanced.Function;

public class Node {
    
    //GUI Fields
    public final String TYPE;
    public final String TITLE;
    public final int COLOR;
    
    //Connection Fields
    public String name;
    public Node senderConnection;
    public List<Node> receiverConnections = new ArrayList<Node>();
    public Function parent;
    public final boolean IS_SENDER, IS_RECIEVER, IS_MODIFIABLE;
    
    public Node(String name, String type, String title, int color, boolean isSender, boolean isReciever, boolean isModifiable) {
        this.COLOR = color;
        this.TITLE = title;
        this.TYPE = type;
        this.name = name;
        this.IS_SENDER = isSender;
        this.IS_RECIEVER = isReciever;
        this.IS_MODIFIABLE = isModifiable;
    }
    
}
