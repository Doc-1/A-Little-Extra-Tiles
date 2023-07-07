package com.alet.client.gui.controls.programmable.blueprints.activators;

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
    
    public BlueprintActivator(int id) {
        super(id);
    }
    
    public abstract void onCollision(World worldIn, Entity entityIn);
    
    public abstract void onRightClick(World worldIn, LittleTile tile, BlockPos pos, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ, LittleActionActivated action);
    
    public abstract void loopRules(Entity entities, boolean shouldContinue, boolean flag);
    
    public abstract boolean shouldRun(World world, Entity entities);
    
    @Override
    public void setNodes() {
        this.nodes.add(new GuiNodeMethod("run", "run", (GuiNode.SENDER)));
    }
}
