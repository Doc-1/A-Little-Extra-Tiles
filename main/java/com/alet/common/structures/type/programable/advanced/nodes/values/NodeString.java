package com.alet.common.structures.type.programable.advanced.nodes.values;

import com.alet.client.gui.controls.programmable.nodes.GuiNodeValue;
import com.creativemd.creativecore.common.utils.mc.ColorUtils;

import net.minecraft.world.WorldServer;

public class NodeString extends NodeValue<String> {
    
    public NodeString(String name, String title, boolean isModifiable) {
        super(name, NodeType.STRING, title, ColorUtils.GREEN, isModifiable);
    }
    
    public NodeString() {
        super("", NodeType.STRING, "", ColorUtils.GREEN);
    }
    
    @Override
    public void setNodeValue(WorldServer server) {
        
    }
    
    @Override
    public GuiNodeValue getGuiNode() throws Exception {
        return new GuiNodeValue<String>(name, NODE_TYPE, TITLE, COLOR, IS_MODIFIABLE).setSender(this.isSender());
    }
    
}
