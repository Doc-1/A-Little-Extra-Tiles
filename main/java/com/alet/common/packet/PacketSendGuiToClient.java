package com.alet.common.packet;

import java.util.List;

import com.alet.client.gui.SubGuiPhotoImport;
import com.alet.client.gui.SubGuiTypeWriter;
import com.alet.client.gui.controls.Layer;
import com.alet.client.gui.message.SubGuiNoBluePrintMessage;
import com.alet.container.SubContainerPhotoImport;
import com.alet.container.SubContainerTypeWriter;
import com.creativemd.creativecore.common.gui.container.SubGui;
import com.creativemd.creativecore.common.gui.mc.ContainerSub;
import com.creativemd.creativecore.common.packet.CreativeCorePacket;
import com.creativemd.littletiles.common.item.ItemLittleRecipe;
import com.creativemd.littletiles.common.item.ItemLittleRecipeAdvanced;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PacketSendGuiToClient extends CreativeCorePacket {
	
	public PacketSendGuiToClient() {
		
	}
	
	@Override
	public void writeBytes(ByteBuf buf) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void readBytes(ByteBuf buf) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void executeClient(EntityPlayer player) {
		List<SubGui> guiList = null;
		if (player.openContainer instanceof ContainerSub)
			guiList = ((ContainerSub) player.openContainer).gui.getLayers();
		if (guiList != null)
			for (SubGui g : guiList) {
				if (g instanceof SubGuiTypeWriter) {
					SubGuiTypeWriter gui = (SubGuiTypeWriter) g;
					typewriterNoBluePrint(player, gui);
					break;
				} else if (g instanceof SubGuiPhotoImport) {
					SubGuiPhotoImport gui = (SubGuiPhotoImport) g;
					photoImporterNoBluePrint(player, gui);
					break;
				}
			}
	}
	
	@Override
	public void executeServer(EntityPlayer player) {
		
	}
	
	@SideOnly(Side.CLIENT)
	private void photoImporterNoBluePrint(EntityPlayer player, SubGuiPhotoImport gui) {
		ItemStack stack = ((SubContainerPhotoImport) gui.container).slot.getStackInSlot(0);
		if (!(stack.getItem() instanceof ItemLittleRecipe || stack.getItem() instanceof ItemLittleRecipeAdvanced) && !player.isCreative()) {
			if (gui != null)
				Layer.addLayer(gui, new SubGuiNoBluePrintMessage());
		}
	}
	
	@SideOnly(Side.CLIENT)
	private void typewriterNoBluePrint(EntityPlayer player, SubGuiTypeWriter gui) {
		ItemStack stack = ((SubContainerTypeWriter) gui.container).slot.getStackInSlot(0);
		if (!(stack.getItem() instanceof ItemLittleRecipe || stack.getItem() instanceof ItemLittleRecipeAdvanced) && !player.isCreative()) {
			if (gui != null)
				Layer.addLayer(gui, new SubGuiNoBluePrintMessage());
		}
	}
}
