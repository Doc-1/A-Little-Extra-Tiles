package com.alet.common.structure.type.programable.advanced.nodes.values;

import com.alet.client.gui.controls.programmable.functions.GuiFunction;
import com.alet.client.gui.controls.programmable.nodes.GuiNode;
import com.alet.client.gui.controls.programmable.nodes.values.GuiNodeValue;
import com.alet.common.structure.type.programable.advanced.Function;
import com.creativemd.creativecore.common.utils.mc.ColorUtils;

import net.minecraft.world.WorldServer;

public class NodeFunction extends NodeValue<Function> {
    
    public NodeFunction(String name, String title, boolean isModifiable) {
        super(name, NodeType.FUNCTION, title, ColorUtils.WHITE, isModifiable);
    }
    
    public NodeFunction() {
        super("", NodeType.FUNCTION, "", ColorUtils.WHITE);
    }
    
    @Override
    public void setNodeValue(WorldServer server) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public GuiNode getGuiNode() throws Exception {
        return new GuiNodeValue<GuiFunction>(name, TITLE, COLOR, IS_MODIFIABLE).setSender(this.isSender());
    }
    
}
