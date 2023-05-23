package com.alet.common.structure.type.trigger.events;

import com.alet.client.gui.controls.GuiConnectedCheckBoxes;
import com.alet.client.gui.controls.GuiWrappedTextField;
import com.creativemd.creativecore.common.gui.CoreControl;
import com.creativemd.creativecore.common.gui.container.GuiParent;
import com.creativemd.creativecore.common.gui.controls.gui.GuiPanel;
import com.creativemd.creativecore.common.world.IOrientatedWorld;
import com.creativemd.creativecore.common.world.SubWorldServer;
import com.creativemd.littletiles.common.structure.exception.CorruptedConnectionException;
import com.creativemd.littletiles.common.structure.exception.NotYetConnectedException;
import com.creativemd.littletiles.common.tile.preview.LittlePreviews;

import net.minecraft.command.CommandResultStats;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
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
    public void createGuiControls(GuiParent parent, LittlePreviews previews) {
        GuiPanel panel = (GuiPanel) parent.get("content");
        wipeControls(panel);
        panel.addControl(new GuiWrappedTextField("command", command, 0, 50, 150, 100));
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
        for (Entity entity : this.getEntities()) {
            ICommandSender sender = new StructureCommandSender(entity);
            if (this.isSenderPlayer)
                sender = new EntityCommandSender(entity);
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
    
    public class StructureCommandSender implements ICommandSender {
        Vec3d center;
        private final Entity entity;
        
        public StructureCommandSender(Entity entity) {
            this.entity = entity;
            try {
                center = structure.getSurroundingBox().getAABB().getCenter();
            } catch (CorruptedConnectionException | NotYetConnectedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        
        @Override
        public String getName() {
            return "Server";
        }
        
        @Override
        public ITextComponent getDisplayName() {
            String name = "";
            if (structure.name != null && !structure.name.isEmpty())
                name = structure.name;
            else {
                name = structure.type.id;
            }
            return new TextComponentString(name);
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
        public Entity getCommandSenderEntity() {
            return entity;
        }
        
        @Override
        public BlockPos getPosition() {
            if (structure.getWorld() instanceof IOrientatedWorld) {
                BlockPos pos = structure.getPos();
                Vec3d vec = new Vec3d(pos.getX(), pos.getY(), pos.getZ());
                vec = ((IOrientatedWorld) structure.getWorld()).getOrigin().transformPointToWorld(vec);
                System.out.println(pos + " " + vec);
                return new BlockPos(vec);
            }
            return structure.getPos();
        }
        
        @Override
        public Vec3d getPositionVector() {
            return center;
        }
        
        @Override
        public boolean sendCommandFeedback() {
            return false;
        }
        
        @Override
        public void setCommandStat(CommandResultStats.Type type, int amount) {
            getServer().setCommandStat(type, amount);
        }
        
        @Override
        public MinecraftServer getServer() {
            return structure.getWorld().getMinecraftServer();
        }
        
        @Override
        public World getEntityWorld() {
            World world = structure.getWorld();
            if (world instanceof SubWorldServer)
                world = ((SubWorldServer) world).getRealWorld();
            return world;
        }
    }
}
