package com.alet.common.structure.type.programable.advanced.nodes.values;

import com.alet.client.gui.controls.programmable.nodes.GuiNode;
import com.alet.client.gui.controls.programmable.nodes.values.GuiNodeValue;
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
    public GuiNode getGuiNode() throws Exception {
        return new GuiNodeValue<String>(name, TITLE, COLOR, IS_MODIFIABLE).setSender(this.isSender());
    }
    
}
