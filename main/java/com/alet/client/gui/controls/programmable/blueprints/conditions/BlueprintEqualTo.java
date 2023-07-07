package com.alet.client.gui.controls.programmable.blueprints.conditions;

import com.alet.client.gui.controls.programmable.blueprints.GuiBlueprint;
import com.alet.client.gui.controls.programmable.nodes.GuiNode;
import com.alet.client.gui.controls.programmable.nodes.GuiNodeBoolean;
import com.alet.client.gui.controls.programmable.nodes.GuiNodeInteger;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;

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
    public boolean tryToPass(Entity entity) {
        return ret;
    }
    
    @Override
    public void conditionPassed(Entity entity) {}
    
    @Override
    public void setNodeValue(Entity entity) {
        GuiNodeInteger a = (GuiNodeInteger) this.getNode("a");
        GuiNodeInteger b = (GuiNodeInteger) this.getNode("b");
        GuiNodeInteger ac = (GuiNodeInteger) a.senderConnection;
        GuiNodeInteger bc = (GuiNodeInteger) b.senderConnection;
        a.setValue(ac.getValue(entity), true);
        b.setValue(bc.getValue(entity), true);
        this.ret = a.getValue(entity) == b.getValue(entity);
        ((GuiNodeBoolean) this.getNode("return")).setValue(ret, true);
    }
    
}
