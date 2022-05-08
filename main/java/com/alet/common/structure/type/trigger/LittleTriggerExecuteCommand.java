package com.alet.common.structure.type.trigger;

import java.util.HashSet;

import com.alet.client.gui.controls.GuiWrappedTextField;
import com.creativemd.creativecore.common.gui.CoreControl;
import com.creativemd.creativecore.common.gui.container.GuiParent;
import com.creativemd.creativecore.common.gui.controls.gui.GuiPanel;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;

public class LittleTriggerExecuteCommand extends LittleTriggerEvent {
    
    String command = "";
    
    public LittleTriggerExecuteCommand(String id) {
        super(id);
    }
    
    public LittleTriggerExecuteCommand(String id, NBTTagCompound nbt) {
        super(id);
        this.command = nbt.getString("command");
    }
    
    @Override
    public NBTTagCompound createNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setString("trigger", id);
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
    public void runEvent(HashSet<Entity> entities, Integer tick) {
        for (Entity entity : entities) {
            entity.world.getMinecraftServer().getCommandManager().executeCommand(entity.world.getMinecraftServer(), this.command);
        }
    }
    
    @Override
    public String getName() {
        return "Execute Command";
    }
    
}
