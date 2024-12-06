package com.alet.events;

import com.alet.ALETConfig;
import com.alet.common.gui.override.HandlerSubGuiOverride;
import com.creativemd.creativecore.common.gui.container.SubGui;
import com.creativemd.creativecore.common.gui.mc.ContainerSub;
import com.creativemd.creativecore.common.gui.opener.GuiHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ALETEventHandler {
    private static boolean shown = false;
    private static int phase = 1;
    private SubGui gui;
    
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onClientTick(PlayerTickEvent event) {
        if (event.side == Side.CLIENT && event.phase == Phase.START) {
            Minecraft mc = Minecraft.getMinecraft();
            if (mc.player != null) {
                if (phase == 2 && mc.player.openContainer instanceof ContainerSub) {
                    SubGui g = ((ContainerSub) mc.player.openContainer).gui.getTopLayer();
                    if (gui != null && g != gui) {
                        gui = ((ContainerSub) mc.player.openContainer).gui.getTopLayer();
                        HandlerSubGuiOverride.overrideGuiFrom(gui);
                        phase = 2;
                    }
                }
                if (phase == 1 && mc.player.openContainer instanceof ContainerSub) {
                    gui = ((ContainerSub) mc.player.openContainer).gui.getTopLayer();
                    HandlerSubGuiOverride.overrideGuiFrom(gui);
                    phase = 2;
                }
                if (phase == 2 && !(mc.player.openContainer instanceof ContainerSub)) {
                    HandlerSubGuiOverride.refreshAllGuiUpdate();
                    HandlerSubGuiOverride.closeGui(gui);
                    gui = null;
                    phase = 1;
                } else if (mc.player.openContainer instanceof ContainerSub) {
                    gui = ((ContainerSub) mc.player.openContainer).gui.getTopLayer();
                    HandlerSubGuiOverride.updateGui(gui);
                }
                if (!shown && ALETConfig.client.showAgain) {
                    GuiHandler.openGui("notice", new NBTTagCompound(), mc.player);
                    shown = true;
                }
            }
        }
    }
    
}
