package com.alet.common.packets.tool.tapemeasure;

import java.util.UUID;

import com.alet.ALET;
import com.creativemd.creativecore.common.packet.CreativeCorePacket;
import com.creativemd.creativecore.common.packet.PacketHandler;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class PacketGenAndSyncUUID extends CreativeCorePacket {
    
    private UUID uuid;
    
    public PacketGenAndSyncUUID() {
        uuid = null;
    }
    
    public PacketGenAndSyncUUID(UUID uuid) {
        this.uuid = uuid;
    }
    
    @Override
    public void writeBytes(ByteBuf buf) {
        if (uuid != null) {
            buf.writeLong(uuid.getMostSignificantBits());
            buf.writeLong(uuid.getLeastSignificantBits());
        }
    }
    
    @Override
    public void readBytes(ByteBuf buf) {
        if (buf.isReadable())
            uuid = new UUID(buf.readLong(), buf.readLong());
        
    }
    
    @Override
    public void executeClient(EntityPlayer player) {
        ItemStack stack = player.getHeldItemMainhand();
        if (stack.getItem().equals(ALET.tapeMeasure)) {
            NBTTagCompound nbt = stack.getTagCompound() != null ? stack.getTagCompound() : new NBTTagCompound();
            if (!nbt.hasUniqueId("UUID")) {
                nbt.setUniqueId("UUID", uuid);
                stack.setTagCompound(nbt);
            }
        }
        
    }
    
    @Override
    public void executeServer(EntityPlayer player) {
        ItemStack stack = player.getHeldItemMainhand();
        if (stack.getItem().equals(ALET.tapeMeasure)) {
            UUID uuid = UUID.randomUUID();
            NBTTagCompound nbt = stack.getTagCompound() != null ? stack.getTagCompound() : new NBTTagCompound();
            if (!nbt.hasUniqueId("UUID")) {
                nbt.setUniqueId("UUID", uuid);
                stack.setTagCompound(nbt);
                PacketHandler.sendPacketToPlayer(new PacketGenAndSyncUUID(uuid), (EntityPlayerMP) player);
            }
        }
    }
}
