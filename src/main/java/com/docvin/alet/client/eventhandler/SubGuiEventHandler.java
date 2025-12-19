package com.docvin.alet.client.eventhandler;

import com.creativemd.creativecore.common.gui.container.SubGui;
import com.creativemd.creativecore.common.gui.mc.ContainerSub;
import com.docvin.alet.common.gui.override.OverrideSubGui;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SubGuiEventHandler {
    private static boolean opened = false;
    private static SubGui oldGui;

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onClientTick(TickEvent.RenderTickEvent event) {
        if (event.side == Side.CLIENT && event.phase == TickEvent.Phase.START) {
            Minecraft mc = Minecraft.getMinecraft();
            if (mc.player != null) {
                if (mc.player.openContainer instanceof ContainerSub) {
                    ContainerSub sub = (ContainerSub) mc.player.openContainer;
                    SubGui currentGui = ((ContainerSub) mc.player.openContainer).gui.getTopLayer();

                    if (!opened) {
                        oldGui = ((ContainerSub) mc.player.openContainer).gui.getTopLayer();
                        MinecraftForge.EVENT_BUS.post(new SubGuiEvent.OnGuiOpenedEvent(oldGui));
                        opened = true;
                    } else if (!currentGui.equals(oldGui)) {
                        MinecraftForge.EVENT_BUS.post(new SubGuiEvent.GuiChangedToEvent(oldGui, currentGui));
                        oldGui = currentGui;
                    }
                } else if (opened) {
                    MinecraftForge.EVENT_BUS.post(new SubGuiEvent.OnGuiClosedEvent(oldGui));
                    oldGui = null;
                    opened = false;

                }
            }
        }
    }

    @SubscribeEvent
    public void openedGui(SubGuiEvent.OnGuiOpenedEvent event) {
        OverrideSubGui.overrideGui(event.getGui());
    }

    @SubscribeEvent
    public void closedGui(SubGuiEvent.OnGuiClosedEvent event) {
    }

    @SubscribeEvent
    public void changedGui(SubGuiEvent.GuiChangedToEvent event) {
        OverrideSubGui.overrideGui(event.getChangedToGui());
    }
}
