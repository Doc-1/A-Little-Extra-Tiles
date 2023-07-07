package com.alet.client.gui.controls.programmable.blueprints.activators;

import com.alet.client.gui.controls.programmable.blueprints.GuiBlueprint;
import com.creativemd.littletiles.common.action.block.LittleActionActivated;
import com.creativemd.littletiles.common.tile.LittleTile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlueprintTileCollision extends BlueprintActivator {
    
    public BlueprintTileCollision(int id) {
        super(id);
    }
    
    @Override
    public void onCollision(World worldIn, Entity entityIn) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public NBTTagCompound serializeNBT(NBTTagCompound nbt) {
        // TODO Auto-generated method stub
        return nbt;
    }
    
    @Override
    public GuiBlueprint deserializeNBT(NBTTagCompound nbt) {
        // TODO Auto-generated method stub
        return this;
    }
    
    @Override
    public void setNodeValue(Entity entity) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void onRightClick(World worldIn, LittleTile tile, BlockPos pos, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ, LittleActionActivated action) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void loopRules(Entity entities, boolean shouldContinue, boolean flag) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public boolean shouldRun(World world, Entity entities) {
        // TODO Auto-generated method stub
        return true;
    }
    
}
