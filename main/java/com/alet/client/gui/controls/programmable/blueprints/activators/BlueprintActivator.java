package com.alet.client.gui.controls.programmable.blueprints.activators;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import com.alet.client.gui.controls.programmable.blueprints.GuiBlueprint;
import com.alet.client.gui.controls.programmable.nodes.GuiNode;
import com.alet.client.gui.controls.programmable.nodes.GuiNodeMethod;
import com.creativemd.littletiles.common.action.block.LittleActionActivated;
import com.creativemd.littletiles.common.tile.LittleTile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class BlueprintActivator extends GuiBlueprint {
    
    protected List<UUID> uuids = new ArrayList<UUID>();
    
    public BlueprintActivator(int id) {
        super(id);
    }
    
    public abstract void onCollision(World worldIn, List<Entity> entityIn);
    
    public abstract void onRightClick(World worldIn, LittleTile tile, BlockPos pos, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ, LittleActionActivated action);
    
    public abstract void loopRules(Entity entities, boolean shouldContinue, boolean flag);
    
    public abstract boolean shouldRun(World world, HashSet<Entity> entities);
    
    public void setUUIDs(List<UUID> uuid) {
        uuids = uuid;
    }
    
    @Override
    public void setNodes() {
        this.nodes.add(new GuiNodeMethod("run", "run", (GuiNode.SENDER)));
    }
}
