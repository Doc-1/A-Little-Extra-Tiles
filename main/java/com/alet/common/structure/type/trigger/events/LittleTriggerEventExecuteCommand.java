package com.alet.common.structure.type.trigger.events;

import com.alet.client.gui.controls.GuiWrappedTextField;
import com.creativemd.creativecore.common.gui.CoreControl;
import com.creativemd.creativecore.common.gui.container.GuiParent;
import com.creativemd.creativecore.common.gui.controls.gui.GuiPanel;
import com.creativemd.littletiles.common.tile.preview.LittlePreviews;

import net.minecraft.command.CommandResultStats;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
    @SideOnly(Side.CLIENT)
    public void createGuiControls(GuiParent parent, LittlePreviews previews) {
        GuiPanel panel = (GuiPanel) parent.get("content");
        wipeControls(panel);
        panel.addControl(new GuiWrappedTextField("command", command, 0, 50, 150, 100));
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void guiChangedEvent(CoreControl source) {
        if (source instanceof GuiWrappedTextField) {
            GuiWrappedTextField text = (GuiWrappedTextField) source;
            this.command = text.text;
        }
    }
    
    @Override
    public boolean runEvent() {
        for (Entity entity : this.getEntities()) {
            ICommandSender sender = new EntityCommandSender(entity);
            entity.world.getMinecraftServer().getCommandManager().executeCommand(sender, this.command);
        }
        return true;
    }
    
    public class EntityCommandSender implements ICommandSender {
        private final Entity entity;
        
        public EntityCommandSender(Entity entity) {
            this.entity = entity;
        }
        
        @Override
        public String getName() {
            return entity.getName();
        }
        
        @Override
        public ITextComponent getDisplayName() {
            return entity.getDisplayName();
        }
        
        @Override
        public void sendMessage(ITextComponent component) {
            // Handle this as you need
        }
        
        @Override
        public boolean canUseCommand(int permLevel, String commandName) {
            // Implement your permission logic here
            return true;
        }
        
        @Override
        public BlockPos getPosition() {
            return entity.getPosition();
        }
        
        @Override
        public Vec3d getPositionVector() {
            return entity.getPositionVector();
        }
        
        @Override
        public World getEntityWorld() {
            return entity.world;
        }
        
        @Override
        public Entity getCommandSenderEntity() {
            return entity;
        }
        
        @Override
        public boolean sendCommandFeedback() {
            return false;
        }
        
        @Override
        public void setCommandStat(CommandResultStats.Type type, int amount) {
            entity.setCommandStat(type, amount);
        }
        
        @Override
        public MinecraftServer getServer() {
            return entity.getServer();
        }
    }
}
