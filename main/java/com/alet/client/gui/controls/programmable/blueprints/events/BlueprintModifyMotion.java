package com.alet.client.gui.controls.programmable.blueprints.events;

import com.alet.client.gui.controls.programmable.blueprints.GuiBlueprint;
import com.alet.client.gui.controls.programmable.nodes.GuiNode;
import com.alet.client.gui.controls.programmable.nodes.GuiNodeBoolean;
import com.alet.client.gui.controls.programmable.nodes.GuiNodeDouble;
import com.alet.client.gui.controls.programmable.nodes.GuiNodeEntityUUID;
import com.alet.client.gui.controls.programmable.nodes.GuiNodeFloat;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldServer;

public class BlueprintModifyMotion extends BlueprintEvent {
    Double x = 0D;
    Double y = 0D;
    Double z = 0D;
    Float forward = 0F;
    Float strafe = 0F;
    Boolean add = false;
    
    public BlueprintModifyMotion(int id) {
        super(id);
    }
    
    @Override
    public NBTTagCompound serializeNBT(NBTTagCompound nbt) {
        nbt.setDouble("mot_x", x);
        nbt.setDouble("mot_y", y);
        nbt.setDouble("mot_z", z);
        nbt.setFloat("mot_f", forward);
        nbt.setFloat("mot_s", strafe);
        nbt.setBoolean("is_add", add);
        return nbt;
    }
    
    @Override
    public GuiBlueprint deserializeNBT(NBTTagCompound nbt) {
        x = nbt.getDouble("mot_x");
        y = nbt.getDouble("mot_y");
        z = nbt.getDouble("mot_z");
        forward = nbt.getFloat("mot_f");
        strafe = nbt.getFloat("mot_s");
        add = nbt.getBoolean("is_add");
        return this;
    }
    
    @Override
    public void setNodes() {
        this.nodes.add(new GuiNodeDouble("x", "x", GuiNode.RECEIVER));
        this.nodes.add(new GuiNodeDouble("y", "y", GuiNode.RECEIVER));
        this.nodes.add(new GuiNodeDouble("z", "z", GuiNode.RECEIVER));
        this.nodes.add(new GuiNodeFloat("forward", "forward", GuiNode.RECEIVER));
        this.nodes.add(new GuiNodeFloat("strafe", "strafe", GuiNode.RECEIVER));
        this.nodes.add(new GuiNodeBoolean("add", "add to velocity", GuiNode.RECEIVER));
    }
    
    @Override
    public void runEvent(WorldServer server) {
        GuiNodeEntityUUID uuid = (GuiNodeEntityUUID) this.getNode("uuid");
        Entity entity = server.getEntityFromUuid(uuid.getValue(server));
        if (this.add) {
            entity.addVelocity(x, y, z);
        } else {
            entity.setVelocity(x, y, z);
        }
        entity.moveRelative(strafe != 0F ? 1F : 0F, 0F, 0F, strafe);
        entity.moveRelative(0F, 0F, forward != 0F ? 1F : 0F, forward);
    }
    
    @Override
    public void setNodeValue(WorldServer server) {
        GuiNodeDouble x = (GuiNodeDouble) this.getNode("x");
        GuiNodeDouble y = (GuiNodeDouble) this.getNode("y");
        GuiNodeDouble z = (GuiNodeDouble) this.getNode("z");
        GuiNodeFloat forward = (GuiNodeFloat) this.getNode("forward");
        GuiNodeFloat strafe = (GuiNodeFloat) this.getNode("strafe");
        GuiNodeBoolean add = (GuiNodeBoolean) this.getNode("add");
        
        GuiNodeDouble nx = (GuiNodeDouble) x.senderConnection;
        GuiNodeDouble ny = (GuiNodeDouble) y.senderConnection;
        GuiNodeDouble nz = (GuiNodeDouble) z.senderConnection;
        GuiNodeFloat nforward = (GuiNodeFloat) forward.senderConnection;
        GuiNodeFloat nstrafe = (GuiNodeFloat) strafe.senderConnection;
        GuiNodeBoolean nadd = (GuiNodeBoolean) add.senderConnection;
        this.x = nx.getValue(server);
        this.y = ny.getValue(server);
        this.z = nz.getValue(server);
        this.forward = nforward.getValue(server);
        this.strafe = nstrafe.getValue(server);
        this.add = nadd.getValue(server);
        x.setValue(this.x, true);
        y.setValue(this.y, true);
        z.setValue(this.z, true);
        forward.setValue(this.forward, true);
        strafe.setValue(this.strafe, true);
        add.setValue(this.add, true);
    }
    
}
