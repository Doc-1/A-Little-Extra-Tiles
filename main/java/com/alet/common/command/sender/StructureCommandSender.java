package com.alet.common.command.sender;

import com.creativemd.creativecore.common.world.IOrientatedWorld;
import com.creativemd.creativecore.common.world.SubWorldServer;
import com.creativemd.littletiles.common.structure.LittleStructure;
import com.creativemd.littletiles.common.structure.exception.CorruptedConnectionException;
import com.creativemd.littletiles.common.structure.exception.NotYetConnectedException;

import net.minecraft.command.CommandResultStats;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class StructureCommandSender implements ICommandSender {
    Vec3d center;
    private final Entity entity;
    private final LittleStructure structure;
    
    public StructureCommandSender(Entity entity, LittleStructure structure) {
        this.entity = entity;
        this.structure = structure;
        try {
            center = structure.getSurroundingBox().getHighestCenterVec();
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
            return new BlockPos(vec);
        }
        return structure.getPos();
    }
    
    @Override
    public Vec3d getPositionVector() {
        if (structure.getWorld() instanceof IOrientatedWorld)
            return ((IOrientatedWorld) structure.getWorld()).getOrigin().transformPointToWorld(center);
        
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
