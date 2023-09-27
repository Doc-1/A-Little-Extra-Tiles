package com.alet.common.structure.type.programable.nodes.values;

import com.alet.client.gui.controls.programmable.nodes.GuiNode;
import com.alet.common.structure.type.programable.nodes.Node;

import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class NodeValue<V> extends Node {
    
    V v;
    
    public NodeValue(String name, String type, String title, int color, boolean isSender, boolean isReciever, boolean isModifiable) {
        super(name, type, title, color, isSender, isReciever, isModifiable);
    }
    
    public NodeValue(String name, String type, String title, int color) {
        super(name, type, title, color, false, false, false);
    }
    
    @SideOnly(Side.SERVER)
    public abstract void setNodeValue(WorldServer server);
    
    @SideOnly(Side.CLIENT)
    public abstract GuiNode getGuiNode() throws Exception;
    
    @SideOnly(Side.SERVER)
    public V getNodeValue() {
        return this.v;
    }
}
