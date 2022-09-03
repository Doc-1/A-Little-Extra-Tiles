package com.alet.common.structure.type.trigger.events;

import java.util.HashSet;

import com.alet.client.gui.controls.GuiWrappedTextField;
import com.creativemd.creativecore.common.gui.CoreControl;
import com.creativemd.creativecore.common.gui.container.GuiParent;
import com.creativemd.creativecore.common.gui.controls.gui.GuiPanel;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;

public class LittleTriggerEventExecuteCommand extends LittleTriggerEvent {
    
    String command = "";
    
    public LittleTriggerEventExecuteCommand(String id) {
        super(id);
    }
    
    @Override
    public NBTTagCompound createNBT(NBTTagCompound nbt) {
        nbt.setString("command", command);
        return nbt;
    }
    
    @Override
    public LittleTriggerEvent createFromNBT(NBTTagCompound nbt) {
        this.command = nbt.getString("command");
        return this;
    }
    
    @Override
    public void updateControls(GuiParent parent) {
        GuiPanel panel = (GuiPanel) parent.get("content");
        wipeControls(panel);
        panel.addControl(new GuiWrappedTextField("command", command, 0, 50, 150, 100));
    }
    
    @Override
    public void updateValues(CoreControl source) {
        if (source instanceof GuiWrappedTextField) {
            GuiWrappedTextField text = (GuiWrappedTextField) source;
            this.command = text.text;
        }
    }
    
    @Override
    public boolean runEvent(HashSet<Entity> entities) {
        for (Entity entity : entities) {
            entity.world.getMinecraftServer().getCommandManager().executeCommand(entity.world.getMinecraftServer(), this.command);
        }
        return true;
    }
    
    @Override
    public String getName() {
        return "Execute Command";
    }
    
}
