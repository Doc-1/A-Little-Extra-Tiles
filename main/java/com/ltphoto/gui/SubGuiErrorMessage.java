package com.ltphoto.gui;

import java.awt.Color;

import javax.annotation.Nullable;

import com.creativemd.creativecore.common.gui.container.SubGui;
import com.creativemd.creativecore.common.gui.controls.gui.GuiButton;
import com.creativemd.creativecore.common.gui.controls.gui.GuiLabel;
import com.creativemd.creativecore.common.gui.controls.gui.GuiTextBox;
import com.creativemd.creativecore.common.gui.mc.ContainerSub;
import com.creativemd.creativecore.common.gui.mc.GuiContainerSub;
import com.creativemd.creativecore.common.gui.premade.SubContainerEmpty;
import com.creativemd.creativecore.common.packet.PacketHandler;
import com.creativemd.creativecore.common.packet.gui.GuiLayerPacket;
import com.creativemd.littletiles.client.gui.controls.SubGuiSoundSelector;
import com.creativemd.littletiles.common.util.animation.event.PlaySoundEvent;
import com.ltphoto.config.Config;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundEvent;

public class SubGuiErrorMessage extends SubGui{

	private int pixelSize;
	
	public SubGuiErrorMessage(int pixelSize) {
		super(300,100);
		this.pixelSize = pixelSize;
	}
	
	@Override
	public void createControls() {
		controls.add(new GuiTextBox("text", "Your Image is " + pixelSize + " pixels large. It must be at most "
	+ Config.getMaxPixelAmount() + " pixels. You can click on \"Auto Scale Image?\" to scale it down to a valid"
			+ " size.\n\nTo change the limit open IGCM and select LTPhoto then increase the Pixel Limit. Be Warned,"
			+ " as the image size increases so does the time it takes. Large photos can even freeze your game for "
			+ "tens of minutes.",0,0,294));
		
		controls.add(new GuiButton("Okay", 0, 77, 40) {
			@Override
			public void onClicked(int x, int y, int button) {
				closeGui();
			}
		});
	}
		
}

