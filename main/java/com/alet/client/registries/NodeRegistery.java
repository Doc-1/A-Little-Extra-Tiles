package com.alet.client.registries;

import com.alet.common.gui.controls.programmable.GuiNodeValue;
import com.alet.components.structures.type.programable.advanced.nodes.values.NodeFunction;
import com.alet.components.structures.type.programable.advanced.nodes.values.NodeInteger;
import com.alet.components.structures.type.programable.advanced.nodes.values.NodeValue;
import com.creativemd.creativecore.common.utils.type.PairList;

public class NodeRegistery {
    
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
