package com.alet.client.gui.controls.programmable.blueprints.values;

import com.alet.client.gui.controls.programmable.blueprints.GuiBlueprint;
import com.alet.client.gui.controls.programmable.nodes.GuiNode;
import com.alet.client.gui.controls.programmable.nodes.GuiNodeString;
import com.creativemd.creativecore.common.gui.GuiControl;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;

public class BlueprintJoinString extends BlueprintValue {
    
    public BlueprintJoinString(int id) {
        super(id);
        // TODO Auto-generated constructor stub
    }
    
    @Override
    public void controlChanged(GuiControl event) {
        // TODO Auto-generated method stub
        
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
    public void setNodeValue(Entity entity) {
        GuiNodeString a = (GuiNodeString) this.getNode("a").senderConnection;
        GuiNodeString b = (GuiNodeString) this.getNode("b").senderConnection;
        if (a != null && b != null) {
            GuiNodeString joined = (GuiNodeString) this.getNode("joined");
            joined.setValue(a.getValue(entity).concat(b.getValue(entity)), true);
        }
    }
    
    @Override
    public void setNodes() {
        this.nodes.add(new GuiNodeString("joined", "joined", GuiNode.SENDER));
        this.nodes.add(new GuiNodeString("a", "a", GuiNode.RECEIVER));
        this.nodes.add(new GuiNodeString("b", "b", GuiNode.RECEIVER));
        
    }
    
}
