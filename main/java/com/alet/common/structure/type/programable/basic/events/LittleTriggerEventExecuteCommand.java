package com.alet.common.structure.type.programable.basic.events;

import com.alet.client.gui.controls.GuiConnectedCheckBoxes;
import com.alet.client.gui.controls.GuiWrappedTextField;
import com.alet.common.command.sender.EntityCommandSender;
import com.alet.common.command.sender.StructureCommandSender;
import com.creativemd.creativecore.common.gui.CoreControl;
import com.creativemd.creativecore.common.gui.controls.gui.GuiLabel;
import com.creativemd.creativecore.common.gui.controls.gui.GuiPanel;
import com.creativemd.littletiles.common.tile.preview.LittlePreviews;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class LittleTriggerEventExecuteCommand extends LittleTriggerEvent {
    
    String command = "";
    boolean isSenderPlayer = false;
    
    public LittleTriggerEventExecuteCommand(int id) {
        super(id);
    }
    
    @Override
    public NBTTagCompound serializeNBT(NBTTagCompound nbt) {
        nbt.setString("command", command);
        nbt.setBoolean("isSenderPlayer", isSenderPlayer);
        return nbt;
    }
    
    @Override
    public LittleTriggerEvent deserializeNBT(NBTTagCompound nbt) {
        this.command = nbt.getString("command");
        this.isSenderPlayer = nbt.getBoolean("isSenderPlayer");
        return this;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void createGuiControls(GuiPanel panel, LittlePreviews previews) {
        panel.addControl(new GuiLabel("Command", 0, 37));
        panel.addControl(new GuiWrappedTextField("command", command, 0, 50, 153, 123));
        GuiConnectedCheckBoxes option = new GuiConnectedCheckBoxes("", 0, 0);
        option.addCheckBox("entityIsSender", "Entity is Sender");
        option.addCheckBox("structureIsSender", "Structure is Sender");
        option.setSelected(isSenderPlayer ? "entityIsSender" : "structureIsSender");
        panel.addControl(option);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void guiChangedEvent(CoreControl source) {
        if (source instanceof GuiWrappedTextField) {
            GuiWrappedTextField text = (GuiWrappedTextField) source;
            this.command = text.text;
        } else if (source instanceof GuiConnectedCheckBoxes) {
            GuiConnectedCheckBoxes checkBox = (GuiConnectedCheckBoxes) source;
            String selected = checkBox.getSelected().name;
            if (selected.equals("structureIsSender"))
                this.isSenderPlayer = false;
            else if (selected.equals("entityIsSender")) {
                this.isSenderPlayer = true;
            }
        }
    }
    
    @Override
    public boolean runEvent() {
        if (this.isSenderPlayer)
            for (Entity entity : this.getEntities()) {
                ICommandSender sender = new EntityCommandSender(entity);
                entity.world.getMinecraftServer().getCommandManager().executeCommand(sender, this.command);
            }
        else {
            ICommandSender sender = new StructureCommandSender(null, structure);
            structure.mainBlock.getWorld().getMinecraftServer().getCommandManager().executeCommand(sender, this.command);
        }
        return true;
    }
    
}
