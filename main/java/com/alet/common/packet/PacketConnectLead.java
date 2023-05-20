package com.alet.common.packet;

import java.util.HashSet;
import java.util.Set;

import com.alet.common.entity.EntityRopeConnection;
import com.alet.common.entity.LeadConnectionData;
import com.creativemd.creativecore.common.packet.CreativeCorePacket;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;

public class PacketConnectLead extends CreativeCorePacket {
    
    private int color;
    private double thickness;
    private double tautness;
    private float lightLevel;
    private int connectionID;
    private Set<Integer> connectors = new HashSet<Integer>();
    private int size;
    private int index;
    
    public PacketConnectLead() {
        
    }
    
    public PacketConnectLead(EntityRopeConnection connection, LeadConnectionData data, int index) {
        this.connectionID = connection.getEntityId();
        //this.size = data.idsConnected.size();
        //this.connectors = data.idsConnected;
        this.color = data.color;
        this.thickness = data.thickness;
        this.tautness = data.tautness;
        this.lightLevel = data.lightLevel;
    }
    
    @Override
    public void writeBytes(ByteBuf buf) {
        buf.writeInt(this.connectionID);
        buf.writeInt(this.size);
        for (Integer connector : this.connectors)
            buf.writeInt(connector);
        buf.writeInt(this.color);
        buf.writeDouble(this.thickness);
        buf.writeDouble(this.tautness);
        buf.writeFloat(this.lightLevel);
    }
    
    @Override
    public void readBytes(ByteBuf buf) {
        this.connectionID = buf.readInt();
        this.size = buf.readInt();
        for (int i = 0; i < this.size; i++)
            this.connectors.add(buf.readInt());
        this.color = buf.readInt();
        this.thickness = buf.readDouble();
        this.tautness = buf.readDouble();
        this.lightLevel = buf.readFloat();
    }
    
    @Override
    public void executeClient(EntityPlayer player) {
        WorldClient world = (WorldClient) player.getEntityWorld();
        EntityRopeConnection connection = (EntityRopeConnection) world.getEntityByID(this.connectionID);
        LeadConnectionData data = new LeadConnectionData(color, thickness, tautness, lightLevel);
        /*
        System.out.println("cli " + data.lightLevel);
        for (int id : connectors)
            data.idsConnected.add(id);
        if (connection.connectionsMap.isEmpty())
            connection.connectionsMap.add(data);
        else {
            LeadConnectionData d = null;
            for (LeadConnectionData data2 : connection.connectionsMap)
                if (data.equals(data2)) {
                    d = data2;
                    break;
                }
            
            if (d != null) {
                connection.connectionsMap.remove(d);
                connection.connectionsMap.add(data);
            } else {
                connection.connectionsMap.add(data);
            }
            
        }
        */
    }
    
    @Override
    public void executeServer(EntityPlayer player) {
        // TODO Auto-generated method stub
        
    }
    
}
