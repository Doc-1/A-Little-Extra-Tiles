package com.alet.common.event;

import java.util.ArrayList;
import java.util.List;

import com.alet.client.gui.override.HandlerSubGuiOverride;
import com.creativemd.creativecore.common.gui.container.SubGui;
import com.creativemd.creativecore.common.gui.mc.ContainerSub;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ALETEventHandler {
	
	private static boolean guiOpened = false;
	private static int phase = 0;
	private static String guiName = "";
	private List<SubGui> prevGuiList = new ArrayList<SubGui>();
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onClientTick(ClientTickEvent event) {
		if (event.phase == Phase.START) {
			Minecraft mc = Minecraft.getMinecraft();
			if (mc.player != null) {
				if (phase == 2 && !(mc.player.openContainer instanceof ContainerSub)) {
					phase = 0;
				}
				if (phase == 2 && mc.player.openContainer instanceof ContainerSub) {
					List<SubGui> guiList = ((ContainerSub) mc.player.openContainer).gui.getLayers();
					if (prevGuiList != null && !guiList.equals(prevGuiList)) {
						HandlerSubGuiOverride.overrideGuiFrom(guiList);
						prevGuiList.clear();
						for (SubGui g : guiList)
							prevGuiList.add(g);
					}
				}
				if (phase == 0 && mc.player.openContainer instanceof ContainerSub) {
					guiOpened = true;
					phase = 1;
				}
				if (phase == 1 && guiOpened && mc.player.openContainer instanceof ContainerSub) {
					List<SubGui> guiList = ((ContainerSub) mc.player.openContainer).gui.getLayers();
					HandlerSubGuiOverride.overrideGuiFrom(guiList);
					guiOpened = false;
					phase = 2;
				}
				
			}
		}
	}
}
