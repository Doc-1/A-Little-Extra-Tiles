package com.alet.common.packet;

import com.creativemd.creativecore.common.packet.CreativeCorePacket;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketUpdateNBT extends CreativeCorePacket{
	
	private NBTTagCompound nbt;

	public PacketUpdateNBT(NBTTagCompound nbt) {
		this.nbt = nbt;
	}
	
	@Override
	public void writeBytes(ByteBuf buf) {
		writeNBT(buf, nbt);
	}

	@Override
	public void readBytes(ByteBuf buf) {
		nbt = readNBT(buf);
	}

	@Override
	public void executeClient(EntityPlayer player) {
	}

	@Override
	public void executeServer(EntityPlayer player) {
		if (nbt.hasNoTags()) {
            ItemStack mainHand = player.getHeldItemMainhand();
            mainHand.setTagCompound(nbt);
        }
	}
}


/*
public static class Handler implements IMessageHandler<PacketUpdateNBT, IMessage> {
    @Override
    public IMessage onMessage(PacketUpdateNBT message, MessageContext ctx) {
        FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
        return null;
    }

    private void handle(PacketUpdateNBT message, MessageContext ctx) {
        EntityPlayerMP playerEntity = ctx.getServerHandler().player;
        if (!message.nbt.hasNoTags()) {
            ItemStack mainHand = playerEntity.getHeldItemMainhand();
            mainHand.setTagCompound(message.nbt);
        }
    }
}
 */