package com.alet.common.structure.type.programable.advanced.nodes.values;

import com.alet.client.gui.controls.programmable.nodes.GuiNode;
import com.alet.client.gui.controls.programmable.nodes.values.GuiNodeValue;
import com.creativemd.creativecore.common.utils.mc.ColorUtils;

import net.minecraft.world.WorldServer;

public class NodeInteger extends NodeValue<Integer> {
    
    public NodeInteger(String name, String title, boolean isModifiable) {
        super(name, NodeType.INTEGER, title, ColorUtils.BLUE, isModifiable);
    }
    
    public NodeInteger() {
        super("", NodeType.INTEGER, "", ColorUtils.BLUE);
    }
    
    @Override
    public void setNodeValue(WorldServer server) {
        
    }
    
    @Override
    public GuiNode getGuiNode() throws Exception {
        return new GuiNodeValue<Integer>(name, TITLE, COLOR, IS_MODIFIABLE).setSender(this.isSender());
    }
    
}
