package com.alet.common.structure.type.programable.nodes.values;

import com.alet.client.gui.controls.programmable.functions.GuiFunction;
import com.alet.client.gui.controls.programmable.nodes.GuiNode;
import com.alet.client.gui.controls.programmable.nodes.GuiNodeRegistar;
import com.alet.client.gui.controls.programmable.nodes.values.GuiNodeValue;
import com.alet.common.structure.type.programable.functions.Function;
import com.creativemd.creativecore.common.utils.mc.ColorUtils;

import net.minecraft.world.WorldServer;

public class NodeFunction extends NodeValue<Function> {
    
    public NodeFunction(String name, String title, boolean isSender, boolean isReciever, boolean isModifiable) {
        super(name, GuiNodeRegistar.FUNCTION_NODE, title, ColorUtils.WHITE, isSender, isReciever, isModifiable);
    }
    
    public NodeFunction() {
        super("", GuiNodeRegistar.FUNCTION_NODE, "", ColorUtils.WHITE);
    }
    
    @Override
    public void setNodeValue(WorldServer server) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public GuiNode getGuiNode() throws Exception {
        return new GuiNodeValue<GuiFunction>(name, TITLE, COLOR, IS_SENDER, IS_RECIEVER, IS_MODIFIABLE);
    }
    
}
