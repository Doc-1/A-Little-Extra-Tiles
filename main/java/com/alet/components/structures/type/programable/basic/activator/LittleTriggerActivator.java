package com.alet.components.structures.type.programable.basic.activator;

import java.util.Collection;
import java.util.HashSet;

import com.alet.components.structures.type.programable.basic.LittleTriggerObject;
import com.creativemd.creativecore.common.gui.container.GuiParent;
import com.creativemd.creativecore.common.gui.controls.gui.GuiPanel;
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class LittleTriggerActivator extends LittleTriggerObject {
    
    public LittleTriggerActivator() {
        super(0);
    }
    
    @SideOnly(Side.CLIENT)
    public GuiPanel getPanel(GuiParent parent) {
        return (GuiPanel) parent.get("trigger");
    }
    
    @Override
    public NBTTagCompound createNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setString("activator", getName() + id);
        return serializeNBT(nbt);
    }
    
    public abstract void onCollision(World worldIn, Entity entityIn);
    
    public abstract void onCollision(World worldIn, Collection<Entity> entities);
    
    public abstract void onRightClick(World worldIn, LittleTile tile, BlockPos pos, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ, LittleActionActivated action);
    
    public abstract void loopRules(HashSet<Entity> entities, boolean shouldContinue, boolean flag);
    
    public abstract boolean shouldRun(World world, HashSet<Entity> entities);
}
