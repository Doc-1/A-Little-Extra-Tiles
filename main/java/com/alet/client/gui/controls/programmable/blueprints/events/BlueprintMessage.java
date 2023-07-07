package com.alet.client.gui.controls.programmable.blueprints.events;

import com.alet.client.gui.controls.programmable.blueprints.GuiBlueprint;
import com.alet.client.gui.controls.programmable.nodes.GuiNode;
import com.alet.client.gui.controls.programmable.nodes.GuiNodeString;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class BlueprintMessage extends BlueprintEvent {
    public String value;
    
    public BlueprintMessage(int id) {
        super(id);
        // TODO Auto-generated constructor stub
    }
    
    @Override
    public void runEvent(World world, Entity entity) {
        if (world.isRemote)
            if (entity instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) entity;
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
    public void setNodeValue(Entity entity) {
        GuiNodeString msg = (GuiNodeString) this.getNode("msg");
        GuiNodeString nmsg = (GuiNodeString) msg.senderConnection;
        this.value = nmsg.getValue(entity);
        msg.setValue(this.value, true);
    }
    
    @Override
    public void setNodes() {
        this.nodes.add(new GuiNodeString("msg", "msg", GuiNode.RECEIVER));
        
    }
    
}
