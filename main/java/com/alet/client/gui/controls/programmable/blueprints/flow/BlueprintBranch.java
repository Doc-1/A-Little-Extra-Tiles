package com.alet.client.gui.controls.programmable.blueprints.flow;

import com.alet.client.gui.controls.programmable.blueprints.GuiBlueprint;
import com.alet.client.gui.controls.programmable.nodes.GuiNode;
import com.alet.client.gui.controls.programmable.nodes.GuiNodeBoolean;
import com.alet.client.gui.controls.programmable.nodes.GuiNodeMethod;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;

public class BlueprintBranch extends BlueprintFlowControl {
    
    Boolean b;
    
    public BlueprintBranch(int id) {
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
        this.nodes.add(new GuiNodeBoolean("b", "b", GuiNode.RECEIVER));
        this.nodes.add(new GuiNodeMethod("true", "true", (GuiNode.SENDER)));
        this.nodes.add(new GuiNodeMethod("false", "false", (GuiNode.SENDER)));
        
    }
    
    @Override
    public void setNodeValue(Entity entity) {
        GuiNodeBoolean b = (GuiNodeBoolean) this.getNode("b");
        GuiNodeBoolean node = (GuiNodeBoolean) b.senderConnection;
        this.b = node.getValue(entity);
        b.setValue(this.b, true);
    }
    
    @Override
    public GuiBlueprint getNextBlueprint() {
        GuiNodeMethod t = (GuiNodeMethod) this.getNode("true");
        GuiNodeMethod f = (GuiNodeMethod) this.getNode("false");
        if (this.b) {
            if (t.receiverConnection != null)
                return t.receiverConnection.getBlueprint();
        } else if (f.receiverConnection != null)
            return f.receiverConnection.getBlueprint();
        return null;
    }
    
}
