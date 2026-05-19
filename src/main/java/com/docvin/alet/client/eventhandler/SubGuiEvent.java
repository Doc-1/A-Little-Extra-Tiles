package com.docvin.alet.client.eventhandler;

import com.creativemd.creativecore.common.gui.container.SubGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.fml.common.eventhandler.Event;

public class SubGuiEvent extends Event {

    private static final Minecraft mc = Minecraft.getMinecraft();
    private final SubGui gui;

    private SubGuiEvent(SubGui gui) {
        this.gui = gui;
    }

    public int[] getScaledResolution() {
        ScaledResolution s = new ScaledResolution(mc);
        return new int[]{s.getScaledWidth(), s.getScaledHeight()};
    }

    public SubGui getGui() {
        return gui;
    }

    public static class OnGuiOpenedEvent extends SubGuiEvent {
        public OnGuiOpenedEvent(SubGui gui) {
            super(gui);
        }
    }

    public static class GuiScreenResizedEvent extends SubGuiEvent {


        public GuiScreenResizedEvent(SubGui gui) {
            super(gui);
        }


    }

    public static class OnGuiClosedEvent extends SubGuiEvent {
        public OnGuiClosedEvent(SubGui gui) {
            super(gui);
        }
    }

    public static class GuiChangedToEvent extends SubGuiEvent {
        private final SubGui changedToGui;

        public GuiChangedToEvent(SubGui gui, SubGui changedToGui) {
            super(gui);
            this.changedToGui = changedToGui;
        }

        public SubGui getChangedToGui() {
            return changedToGui;
        }
    }

}
