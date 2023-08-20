package com.alet.client.gui.controls.programmable.blueprints.values;

import com.alet.client.gui.controls.programmable.blueprints.GuiBlueprint;
import com.alet.client.gui.controls.programmable.nodes.GuiNode;
import com.alet.client.gui.controls.programmable.nodes.GuiNodeDouble;
import com.alet.client.gui.controls.programmable.nodes.GuiNodeString;
import com.creativemd.creativecore.common.gui.GuiControl;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldServer;

public class BlueprintCastDoubleToString extends BlueprintValue {
    
    public BlueprintCastDoubleToString(int id) {
        super(id);
    }
    
    @Override
    public NBTTagCompound serializeNBT(NBTTagCompound nbt) {
        // TODO Auto-generated method stub
        return nbt;
    }
    
    @Override
    public GuiBlueprint deserializeNBT(NBTTagCompound nbt) {
        // TODO Auto-generated method stub
        return this;
    }
    
    @Override
    public void setNodes() {
        this.nodes.add(new GuiNodeString("String", "string", GuiNode.SENDER));
        this.nodes.add(new GuiNodeDouble("double", "double", GuiNode.RECEIVER));
    }
    
    @Override
    public void setNodeValue(WorldServer server) {
        GuiNodeDouble node = (GuiNodeDouble) this.getNode("double").senderConnection;
        if (node != null) {
            GuiNodeString strNode = (GuiNodeString) this.getNode("string");
            strNode.setValue(node.getValue(server) + "", true);
        }
    }
    
    @Override
    public void controlChanged(GuiControl source) {
        // TODO Auto-generated method stub
        
    }
    
}
