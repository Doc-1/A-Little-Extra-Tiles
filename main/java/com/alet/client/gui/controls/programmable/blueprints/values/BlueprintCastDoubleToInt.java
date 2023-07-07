package com.alet.client.gui.controls.programmable.blueprints.values;

import com.alet.client.gui.controls.programmable.blueprints.GuiBlueprint;
import com.alet.client.gui.controls.programmable.nodes.GuiNode;
import com.alet.client.gui.controls.programmable.nodes.GuiNodeDouble;
import com.alet.client.gui.controls.programmable.nodes.GuiNodeInteger;
import com.creativemd.creativecore.common.gui.GuiControl;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;

public class BlueprintCastDoubleToInt extends BlueprintValue {
    
    public BlueprintCastDoubleToInt(int id) {
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
        this.nodes.add(new GuiNodeInteger("int", "int", GuiNode.SENDER));
        this.nodes.add(new GuiNodeDouble("double", "double", GuiNode.RECEIVER));
    }
    
    @Override
    public void setNodeValue(Entity entity) {
        GuiNodeDouble node = (GuiNodeDouble) this.getNode("double").senderConnection;
        if (node != null) {
            GuiNodeInteger intNode = (GuiNodeInteger) this.getNode("int");
            intNode.setValue(node.getValue(entity).intValue(), true);
        }
    }
    
    @Override
    public void controlChanged(GuiControl source) {
        // TODO Auto-generated method stub
        
    }
    
}
