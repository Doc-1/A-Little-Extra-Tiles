package com.alet.client.gui.controls.programmable.blueprints.events;

import com.alet.client.gui.controls.programmable.blueprints.GuiBlueprint;
import com.alet.client.gui.controls.programmable.nodes.GuiNode;
import com.alet.client.gui.controls.programmable.nodes.GuiNodeEntityUUID;
import com.alet.client.gui.controls.programmable.nodes.GuiNodeString;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.WorldServer;

public class BlueprintMessage extends BlueprintEvent {
    
    public String value;
    
    public BlueprintMessage(int id) {
        super(id);
    }
    
    @Override
    public void runEvent(WorldServer server) {
        GuiNodeEntityUUID uuid = (GuiNodeEntityUUID) this.getNode("uuid");
        Entity entity = server.getEntityFromUuid(uuid.getValue(server));
        if (entity instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) entity;
            player.sendMessage(new TextComponentString(value));
        }
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
        GuiNodeString msg = (GuiNodeString) this.getNode("msg");
        GuiNodeString nmsg = (GuiNodeString) msg.senderConnection;
        value = nmsg.getValue(server);
        msg.setValue(value, false);
        GuiNodeEntityUUID uuid = (GuiNodeEntityUUID) this.getNode("uuid");
        GuiNodeEntityUUID nuuid = (GuiNodeEntityUUID) uuid.senderConnection;
        uuid.setValue(nuuid.getValue(server), false);
    }
    
    @Override
    public void setNodes() {
        this.nodes.add(new GuiNodeString("msg", "msg", GuiNode.RECEIVER));
        this.nodes.add(new GuiNodeEntityUUID("uuid", "Entity", GuiNode.RECEIVER));
        
    }
    
}
