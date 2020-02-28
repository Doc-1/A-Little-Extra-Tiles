package com.ltphoto.gui;

import javax.annotation.Nullable;

import com.creativemd.creativecore.common.gui.container.SubGui;
import com.creativemd.creativecore.common.gui.controls.gui.GuiButton;
import com.creativemd.creativecore.common.gui.controls.gui.GuiTextBox;
import com.creativemd.creativecore.common.gui.mc.ContainerSub;
import com.creativemd.creativecore.common.gui.mc.GuiContainerSub;
import com.creativemd.creativecore.common.gui.premade.SubContainerEmpty;
import com.creativemd.creativecore.common.packet.PacketHandler;
import com.creativemd.creativecore.common.packet.gui.GuiLayerPacket;
import com.creativemd.littletiles.client.gui.controls.SubGuiSoundSelector;
import com.creativemd.littletiles.common.util.animation.event.PlaySoundEvent;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundEvent;

public class SubGuiErrorMessage extends SubGui{

	
	public SubGuiErrorMessage() {
		super(100,50);
	}
	
	@Override
	public void createControls() {
		controls.add(new GuiTextBox("t", "Image to Large", 0, 0, 90));
		
		controls.add(new GuiButton("Okay", 20, 20, 40) {
			@Override
			public void onClicked(int x, int y, int button) {
				closeGui();
			}
		});
	}
		
}

