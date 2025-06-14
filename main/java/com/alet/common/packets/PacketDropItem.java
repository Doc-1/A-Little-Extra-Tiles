package com.alet.common.packets;

import javax.vecmath.Vector3d;

import com.creativemd.creativecore.common.packet.CreativeCorePacket;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

public class PacketDropItem extends CreativeCorePacket {
    
    double x;
    double y;
    double z;
    BlockPos structureLocation;
    ItemStack stack;
    
    public PacketDropItem() {
        
    }
    
    public PacketDropItem(Vector3d vector3d, BlockPos structureLocation, ItemStack stack) {
        x = vector3d.x;
        y = vector3d.y - 0.5;
        z = vector3d.z;
        this.structureLocation = structureLocation;
        this.stack = stack;
    }
    
    @Override
    public void writeBytes(ByteBuf buf) {
        buf.writeDouble(x);
        buf.writeDouble(y);
        buf.writeDouble(z);
        
        writePos(buf, structureLocation);
        writeItemStack(buf, stack);
    }
    
    @Override
    public void readBytes(ByteBuf buf) {
        x = buf.readDouble();
        y = buf.readDouble();
        z = buf.readDouble();
        
        structureLocation = readPos(buf);
        stack = readItemStack(buf);
    }
    
    @Override
    public void executeClient(EntityPlayer player) {}
    
    @Override
    public void executeServer(EntityPlayer player) {
        EntityItem item = new EntityItem(player.world, structureLocation.getX() + x, structureLocation
                .getY() + y, structureLocation.getZ() + z, stack);
        item.motionX = 0;
        item.motionY = 0;
        item.motionZ = 0;
        player.world.spawnEntity(item);
    }
    
}
