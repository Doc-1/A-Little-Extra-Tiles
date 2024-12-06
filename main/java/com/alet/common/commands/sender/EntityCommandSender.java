package com.alet.common.commands.sender;

import net.minecraft.command.CommandResultStats;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

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
