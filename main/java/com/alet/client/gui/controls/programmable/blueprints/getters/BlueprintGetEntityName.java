package com.alet.client.gui.controls.programmable.blueprints.getters;

import com.alet.client.gui.controls.programmable.blueprints.GuiBlueprint;
import com.alet.client.gui.controls.programmable.nodes.GuiNode;
import com.alet.client.gui.controls.programmable.nodes.GuiNodeString;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;

public class BlueprintGetEntityName extends BlueprintGetter {
    
    public BlueprintGetEntityName(int id) {
        super(id);
        // TODO Auto-generated constructor stub
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
        GuiNodeString node = (GuiNodeString) this.getNode("entity_name");
        node.setValue(entity.getName(), true);
    }
    
    @Override
    public void setNodes() {
        this.nodes.add(new GuiNodeString("entity_name", "name", (byte) (GuiNode.SENDER)));
    }
    
}
