package com.ltphoto.gui.controls;

import com.creativemd.creativecore.common.gui.container.SubGui;
import com.creativemd.creativecore.common.gui.controls.gui.GuiButton;
import com.creativemd.creativecore.common.gui.mc.ContainerSub;
import com.creativemd.creativecore.common.gui.mc.GuiContainerSub;
import com.creativemd.creativecore.common.gui.premade.SubContainerEmpty;
import com.creativemd.creativecore.common.packet.PacketHandler;
import com.creativemd.creativecore.common.packet.gui.GuiLayerPacket;
import com.creativemd.littletiles.client.gui.dialogs.SubGuiDialogAxis;
import com.ltphoto.gui.SubGuiErrorMessage;

import net.minecraft.nbt.NBTTagCompound;

public class Layer {

	public static void addLayer(SubGui parentGui, SubGui dialog) {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setBoolean("dialog", true);
		dialog.gui = parentGui.gui;
		PacketHandler.sendPacketToServer(new GuiLayerPacket(nbt, dialog.gui.getLayers().size() - 1, false));
		dialog.gui.addLayer(dialog);
		dialog.container = new SubContainerEmpty(parentGui.getPlayer());
		((ContainerSub) dialog.gui.inventorySlots).layers.add(dialog.container);
		dialog.onOpened();
		dialog.gui.resize();
	}
}

