package com.alet.common.structure.type.programable.nodes.values;

import com.alet.client.gui.controls.programmable.nodes.GuiNode;
import com.alet.client.gui.controls.programmable.nodes.GuiNodeRegistar;
import com.alet.client.gui.controls.programmable.nodes.values.GuiNodeValue;
import com.creativemd.creativecore.common.utils.mc.ColorUtils;

import net.minecraft.world.WorldServer;

public class NodeInteger extends NodeValue<Integer> {
    
    public NodeInteger(String name, String title, boolean isSender, boolean isReciever, boolean isModifiable) {
        super(name, GuiNodeRegistar.INTEGER_NODE, title, ColorUtils.BLUE, isSender, isReciever, isModifiable);
    }
    
    public NodeInteger() {
        super("", GuiNodeRegistar.INTEGER_NODE, "", ColorUtils.BLUE);
    }
    
    @Override
    public void setNodeValue(WorldServer server) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public GuiNode getGuiNode() throws Exception {
        return new GuiNodeValue<Integer>(name, TITLE, COLOR, IS_SENDER, IS_RECIEVER, IS_MODIFIABLE);
    }
    
}
