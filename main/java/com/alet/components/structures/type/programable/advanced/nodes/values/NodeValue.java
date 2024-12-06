package com.alet.components.structures.type.programable.advanced.nodes.values;

import com.alet.common.gui.controls.programmable.GuiNodeValue;
import com.alet.components.structures.type.programable.advanced.nodes.Node;

import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class NodeValue<V> extends Node {
    
    V v;
    
    public NodeValue(String name, NodeType type, String title, int color, boolean isModifiable) {
        super(name, type, title, color, isModifiable);
    }
    
    public NodeValue(String name, NodeType type, String title, int color) {
        super(name, type, title, color, false);
    }
    
    @SideOnly(Side.SERVER)
    public abstract void setNodeValue(WorldServer server);
    
    @SideOnly(Side.CLIENT)
    public abstract GuiNodeValue getGuiNode() throws Exception;
    
    @SideOnly(Side.SERVER)
    public V getNodeValue() {
        return this.v;
    }
}
