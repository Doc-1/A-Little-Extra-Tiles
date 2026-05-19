package com.docvin.alet.client.eventhandler;

import com.creativemd.creativecore.common.gui.container.SubGui;
import com.creativemd.creativecore.common.gui.mc.ContainerSub;
import com.creativemd.creativecore.common.gui.mc.IVanillaGUI;
import com.docvin.alet.common.gui.override.OverrideSubGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SubGuiEventHandler {
    private static final Minecraft mc = Minecraft.getMinecraft();
    private static boolean opened = false;
    private static SubGui oldGui;
    private static ScaledResolution oldRes;
    private static int oldGuiScale;

    @SubscribeEvent(priority = EventPriority.LOWEST)
    @SideOnly(Side.CLIENT)
    public void onClientTick(TickEvent.RenderTickEvent event) {
        if (event.side == Side.CLIENT && event.phase == TickEvent.Phase.START) {
            if (mc.player != null) {
                if (mc.currentScreen instanceof IVanillaGUI) {
                    ContainerSub sub = (ContainerSub) mc.player.openContainer;
                    SubGui currentGui = ((ContainerSub) mc.player.openContainer).gui.getTopLayer();

                    if (!opened) {
                        oldGuiScale = mc.gameSettings.guiScale;
                        mc.gameSettings.guiScale = 2;
                        oldGui = ((ContainerSub) mc.player.openContainer).gui.getTopLayer();
                        MinecraftForge.EVENT_BUS.post(new SubGuiEvent.OnGuiOpenedEvent(oldGui));
                        opened = true;
                    } else if (!currentGui.equals(oldGui)) {
                        MinecraftForge.EVENT_BUS.post(new SubGuiEvent.GuiChangedToEvent(oldGui, currentGui));
                        oldGui = currentGui;
                    }

                    ScaledResolution res = new ScaledResolution(mc);

                    if (oldRes != null && (res.getScaledHeight() != oldRes.getScaledHeight() || res.getScaledWidth() != oldRes.getScaledWidth()))
                        MinecraftForge.EVENT_BUS.post(new SubGuiEvent.GuiScreenResizedEvent(currentGui));
                    oldRes = res;
                } else if (opened) {
                    MinecraftForge.EVENT_BUS.post(new SubGuiEvent.OnGuiClosedEvent(oldGui));
                    oldGui = null;
                    opened = false;

                }
            }
        }
    }

    @SubscribeEvent
    public void onGuiTick(GuiScreenEvent.InitGuiEvent.Pre event) {
    }

    @SuppressWarnings("unchecked")
    @SubscribeEvent
    public void resizedScreen(SubGuiEvent.GuiScreenResizedEvent event) {
        OverrideSubGui<SubGui> overrideSubGui = (OverrideSubGui<SubGui>) OverrideSubGui.getGuiOverride(event.getGui());
        if (overrideSubGui != null) {
            SubGui subGui = event.getGui();
            int[] dim = event.getScaledResolution();
            assert subGui.container != null;
            subGui.setDimension(dim[0] - 10, dim[1] - 10);
            subGui.container.container.gui.setGuiSize(dim[0], dim[1]);
            overrideSubGui.onScreenResized(subGui, dim[0], dim[1]);
            subGui.container.container.gui.resize();
        }

    }

    @SuppressWarnings("unchecked")
    @SubscribeEvent
    public void openedGui(SubGuiEvent.OnGuiOpenedEvent event) {
        OverrideSubGui<SubGui> overrideSubGui = (OverrideSubGui<SubGui>) OverrideSubGui.getGuiOverride(event.getGui());
        if (overrideSubGui != null) {
            SubGui subGui = event.getGui();
            int[] dim = event.getScaledResolution();
            assert subGui.container != null;
            subGui.setDimension(dim[0] - 10, dim[1] - 10);
            subGui.container.container.gui.setGuiSize(dim[0], dim[1]);
            overrideSubGui.overrideGui(subGui);
            subGui.container.container.gui.resize();
        }

    }

    @SubscribeEvent
    public void closedGui(SubGuiEvent.OnGuiClosedEvent event) {
        mc.gameSettings.guiScale = oldGuiScale;
    }

    @SubscribeEvent
    public void changedGui(SubGuiEvent.GuiChangedToEvent event) {
        //  OverrideSubGui.overrideGui(event.getChangedToGui());
    }
}
