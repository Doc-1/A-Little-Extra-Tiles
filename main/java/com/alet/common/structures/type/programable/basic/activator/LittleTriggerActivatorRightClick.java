package com.alet.common.structures.type.programable.basic.activator;

import java.util.Collection;
import java.util.HashSet;

import com.alet.common.structures.type.programable.basic.LittleTriggerObject;
import com.creativemd.creativecore.common.gui.CoreControl;
import com.creativemd.creativecore.common.gui.controls.gui.GuiPanel;
import com.creativemd.littletiles.common.action.block.LittleActionActivated;
import com.creativemd.littletiles.common.tile.LittleTile;
import com.creativemd.littletiles.common.tile.preview.LittlePreviews;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class LittleTriggerActivatorRightClick extends LittleTriggerActivator {
    
    @Override
    public boolean shouldRun(World world, HashSet<Entity> entities) {
        if (entities != null && !entities.isEmpty())
            return true;
        return false;
    }
    
    @Override
    public LittleTriggerObject deserializeNBT(NBTTagCompound nbt) {
        // TODO Auto-generated method stub
        return this;
    }
    
    @Override
    public NBTTagCompound serializeNBT(NBTTagCompound nbt) {
        // TODO Auto-generated method stub
        return nbt;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void createGuiControls(GuiPanel panel, LittlePreviews previews) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void guiChangedEvent(CoreControl source) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void loopRules(HashSet<Entity> entities, boolean shouldContinue, boolean flag) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void onCollision(World worldIn, Entity entityIn) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void onCollision(World worldIn, Collection<Entity> entities) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void onRightClick(World worldIn, LittleTile tile, BlockPos pos, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ, LittleActionActivated action) {
        structure.entities.clear();
        structure.entities.add(playerIn);
    }
    
}
