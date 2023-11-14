package com.alet.common.structure.type.trigger.conditions;

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

public class LittleTriggerConditionSuccessfulCommand extends LittleTriggerCondition {
    
    String command = "";
    boolean isSenderPlayer = false;
    
    public LittleTriggerConditionSuccessfulCommand(int id) {
        super(id);
        // TODO Auto-generated constructor stub
    }
    
    @Override
    public boolean conditionPassed() {
        for (Entity entity : this.getEntities()) {
            ICommandSender sender = new StructureCommandSender(entity, structure);
            if (this.isSenderPlayer)
                sender = new EntityCommandSender(entity);
            int i = entity.world.getMinecraftServer().getCommandManager().executeCommand(sender, this.command);
            if (i == 1)
                return true;
        }
        return false;
    }
    
    @Override
    public NBTTagCompound serializeNBT(NBTTagCompound nbt) {
        nbt.setString("command", command);
        nbt.setBoolean("isSenderPlayer", isSenderPlayer);
        return nbt;
    }
    
    @Override
    public LittleTriggerCondition deserializeNBT(NBTTagCompound nbt) {
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
}
