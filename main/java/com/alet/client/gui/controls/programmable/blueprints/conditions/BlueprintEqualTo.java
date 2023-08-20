package com.alet.client.gui.controls.programmable.blueprints.conditions;

import com.alet.client.gui.controls.programmable.blueprints.GuiBlueprint;
import com.alet.client.gui.controls.programmable.nodes.GuiNode;
import com.alet.client.gui.controls.programmable.nodes.GuiNodeBoolean;
import com.alet.client.gui.controls.programmable.nodes.GuiNodeInteger;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldServer;

public class BlueprintEqualTo extends BlueprintCondition {
    
    Boolean ret;
    
    public BlueprintEqualTo(int id) {
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
        this.nodes.add(new GuiNodeBoolean("return", "return", GuiNode.SENDER));
        this.nodes.add(new GuiNodeInteger("a", "a", GuiNode.RECEIVER));
        this.nodes.add(new GuiNodeInteger("b", "b", GuiNode.RECEIVER));
    }
    
    @Override
    public boolean tryToPass() {
        return ret;
    }
    
    @Override
    public void setNodeValue(WorldServer server) {
        GuiNodeInteger a = (GuiNodeInteger) this.getNode("a");
        GuiNodeInteger b = (GuiNodeInteger) this.getNode("b");
        GuiNodeInteger ac = (GuiNodeInteger) a.senderConnection;
        GuiNodeInteger bc = (GuiNodeInteger) b.senderConnection;
        a.setValue(ac.getValue(server), true);
        b.setValue(bc.getValue(server), true);
        this.ret = a.getValue(server) == b.getValue(server);
        ((GuiNodeBoolean) this.getNode("return")).setValue(ret, true);
    }
    
}
