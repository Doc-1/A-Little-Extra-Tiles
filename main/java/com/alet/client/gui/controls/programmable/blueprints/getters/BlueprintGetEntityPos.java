package com.alet.client.gui.controls.programmable.blueprints.getters;

import com.alet.client.gui.controls.programmable.blueprints.GuiBlueprint;
import com.alet.client.gui.controls.programmable.nodes.GuiNode;
import com.alet.client.gui.controls.programmable.nodes.GuiNodeDouble;
import com.alet.client.gui.controls.programmable.nodes.GuiNodeEntityUUID;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldServer;

public class BlueprintGetEntityPos extends BlueprintGetter {
    
    public BlueprintGetEntityPos(int id) {
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
    public void setNodeValue(WorldServer server) {
        GuiNodeDouble x = (GuiNodeDouble) this.getNode("x");
        GuiNodeDouble y = (GuiNodeDouble) this.getNode("y");
        GuiNodeDouble z = (GuiNodeDouble) this.getNode("z");
        GuiNodeEntityUUID uuid = (GuiNodeEntityUUID) this.getNode("uuid");
        Entity entity = server.getEntityFromUuid(uuid.getValue(server));
        x.setValue(entity.posX, true);
        y.setValue(entity.posY, true);
        z.setValue(entity.posZ, true);
    }
    
    @Override
    public void setNodes() {
        this.nodes.add(new GuiNodeDouble("x", "x", (byte) (GuiNode.SENDER)));
        this.nodes.add(new GuiNodeDouble("y", "y", (byte) (GuiNode.SENDER)));
        this.nodes.add(new GuiNodeDouble("z", "z", (byte) (GuiNode.SENDER)));
        this.nodes.add(new GuiNodeEntityUUID("uuid", "Entity", GuiNode.RECEIVER));
        
    }
    
}
