package com.alet.common.event;

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
    private static int phase = 1;
    private static String guiName = "";
    private SubGui guiList;
    
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onClientTick(ClientTickEvent event) {
        if (event.phase == Phase.START) {
            Minecraft mc = Minecraft.getMinecraft();
            if (mc.player != null) {
                if (phase == 1 && mc.player.openContainer instanceof ContainerSub) {
                    guiList = ((ContainerSub) mc.player.openContainer).gui.getTopLayer();
                    HandlerSubGuiOverride.overrideGuiFrom(guiList);
                    phase = 2;
                }
                if (phase == 2 && !(mc.player.openContainer instanceof ContainerSub)) {
                    HandlerSubGuiOverride.refreshAllGuiUpdate();
                    phase = 1;
                } else if (mc.player.openContainer instanceof ContainerSub) {
                    guiList = ((ContainerSub) mc.player.openContainer).gui.getTopLayer();
                    HandlerSubGuiOverride.updateGuiFrom(guiList);
                }
            }
        }
    }
}
