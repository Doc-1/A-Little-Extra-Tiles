package com.alet.client.gui.controls;

import com.creativemd.creativecore.common.gui.container.SubGui;
import com.creativemd.creativecore.common.gui.premade.SubContainerEmpty;
import com.creativemd.creativecore.common.packet.PacketHandler;
import com.creativemd.creativecore.common.packet.gui.GuiLayerPacket;

import net.minecraft.nbt.NBTTagCompound;

public class Layer {
	
	public static void addLayer(SubGui parentGui, SubGui dialog) {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setBoolean("dialog", true);
		dialog.gui = parentGui.gui;
		PacketHandler.sendPacketToServer(new GuiLayerPacket(nbt, dialog.gui.getLayers().size() - 1, false));
		dialog.container = new SubContainerEmpty(parentGui.getPlayer());
		dialog.gui.addLayer(dialog);
		dialog.onOpened();
	}
}
