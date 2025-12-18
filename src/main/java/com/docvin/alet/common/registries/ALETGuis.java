package com.docvin.alet.common.registries;

import com.creativemd.creativecore.common.gui.container.SubContainer;
import com.creativemd.creativecore.common.gui.container.SubGui;
import com.creativemd.creativecore.common.gui.opener.CustomGuiHandler;
import com.creativemd.creativecore.common.gui.opener.GuiHandler;
import com.creativemd.creativecore.common.gui.premade.SubContainerEmpty;
import com.docvin.alet.components.gui.frames.tools.SubGuiManual;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class ALETGuis {

    public static void registerGuis() {
        for (ALET_GUI guiHandler : ALET_GUI.values()) {
            register(guiHandler.getName(), guiHandler.getGuiHandler());
        }
    }

    private static void register(String guiName, CustomGuiHandler guiHandler) {
        GuiHandler.registerGuiHandler(guiName, guiHandler);
    }

    public enum ALET_GUI {
        MANUAL("manual", new CustomGuiHandler() {
            @Override
            public SubContainer getContainer(EntityPlayer entityPlayer, NBTTagCompound nbtTagCompound) {
                return new SubContainerEmpty(entityPlayer);
            }

            @Override
            public SubGui getGui(EntityPlayer entityPlayer, NBTTagCompound nbtTagCompound) {
                return new SubGuiManual();
            }
        });

        private final String name;
        private final CustomGuiHandler guiHandler;

        ALET_GUI(String name, CustomGuiHandler guiHandler) {
            this.name = name;
            this.guiHandler = guiHandler;
        }

        public String getName() {
            return name;
        }

        public CustomGuiHandler getGuiHandler() {
            return guiHandler;
        }
    }
}
