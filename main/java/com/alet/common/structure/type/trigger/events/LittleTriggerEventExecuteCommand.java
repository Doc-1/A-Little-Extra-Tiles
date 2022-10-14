package com.alet.common.structure.type.trigger.events;

import com.alet.client.gui.controls.GuiWrappedTextField;
import com.creativemd.creativecore.common.gui.CoreControl;
import com.creativemd.creativecore.common.gui.container.GuiParent;
import com.creativemd.creativecore.common.gui.controls.gui.GuiPanel;
import com.creativemd.littletiles.common.tile.preview.LittlePreviews;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;

public class LittleTriggerEventExecuteCommand extends LittleTriggerEvent {
    
    String command = "";
    
    public LittleTriggerEventExecuteCommand(int id) {
        super(id);
    }
    
    @Override
    public NBTTagCompound serializeNBT(NBTTagCompound nbt) {
        nbt.setString("command", command);
        return nbt;
    }
    
    @Override
    public LittleTriggerEvent deserializeNBT(NBTTagCompound nbt) {
        this.command = nbt.getString("command");
        return this;
    }
    
    @Override
    public void createGuiControls(GuiParent parent, LittlePreviews previews) {
        GuiPanel panel = (GuiPanel) parent.get("content");
        wipeControls(panel);
        panel.addControl(new GuiWrappedTextField("command", command, 0, 50, 150, 100));
    }
    
    @Override
    public void guiChangedEvent(CoreControl source) {
        if (source instanceof GuiWrappedTextField) {
            GuiWrappedTextField text = (GuiWrappedTextField) source;
            this.command = text.text;
        }
    }
    
    @Override
    public boolean runEvent() {
        for (Entity entity : this.getEntities()) {
            entity.world.getMinecraftServer().getCommandManager().executeCommand(entity.world.getMinecraftServer(), this.command);
        }
        return true;
    }
    
}
