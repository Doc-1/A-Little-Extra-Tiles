package com.alet.common.structure.type.programable.advanced.nodes;

import com.alet.client.gui.controls.programmable.nodes.GuiNodeValue;
import com.alet.common.structure.type.programable.advanced.nodes.values.NodeFunction;
import com.alet.common.structure.type.programable.advanced.nodes.values.NodeInteger;
import com.alet.common.structure.type.programable.advanced.nodes.values.NodeValue;
import com.creativemd.creativecore.common.utils.type.PairList;

public class NodeRegistar {
    
    public static PairList<String, GuiNodeValue> guiNodes = new PairList<>();
    public static final String FUNCTION_NODE = "function";
    public static final String INTEGER_NODE = "integer";
    
    public static void registerNodes() {
        registerNode(FUNCTION_NODE, new NodeFunction());
        registerNode(INTEGER_NODE, new NodeInteger());
    }
    
    public static void registerNode(String type, NodeValue node) {
        try {
            guiNodes.add(type, node.getGuiNode());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static boolean matchType(String type, GuiNodeValue compare) {
        return compare.name.equals(type);
    }
    
    public static GuiNodeValue createNode(String type, String name, String title, boolean isModifiable) {
        return guiNodes.getValue(type).clone(name, title, isModifiable);
    }
}
