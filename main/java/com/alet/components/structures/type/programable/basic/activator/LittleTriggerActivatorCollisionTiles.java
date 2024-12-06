package com.alet.components.structures.type.programable.basic.activator;

import java.util.Collection;
import java.util.HashSet;

import com.alet.components.structures.type.programable.basic.LittleTriggerObject;
import com.creativemd.creativecore.common.gui.CoreControl;
import com.creativemd.creativecore.common.gui.controls.gui.GuiCheckBox;
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

public class LittleTriggerActivatorCollisionTiles extends LittleTriggerActivator {
    
    private boolean runWhileCollided = false;
    
    @Override
    public void loopRules(HashSet<Entity> entities, boolean shouldContinue, boolean flag) {
        if (runWhileCollided) {
            flag = entities.isEmpty();
            entities.clear();
        }
    }
    
    @Override
    public boolean shouldRun(World world, HashSet<Entity> entities) {
        if (entities != null && !entities.isEmpty())
            return true;
        return false;
    }
    
    @Override
    public LittleTriggerObject deserializeNBT(NBTTagCompound nbt) {
        //this.runWhileCollided = nbt.getBoolean("while_collided");
        return this;
    }
    
    @Override
    public NBTTagCompound serializeNBT(NBTTagCompound nbt) {
        //nbt.setBoolean("while_collided", runWhileCollided);
        return nbt;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void createGuiControls(GuiPanel panel, LittlePreviews previews) {
        panel.addControl(new GuiCheckBox("while_collided", "Run Only While Collided", 0, 0, this.runWhileCollided));
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void guiChangedEvent(CoreControl source) {
        
        if (source.is("while_collided")) {
            GuiCheckBox checkbox = (GuiCheckBox) source;
            runWhileCollided = checkbox.value;
        }
    }
    
    @Override
    public void onCollision(World worldIn, Entity entityIn) {
        structure.entities.clear();
        structure.entities.add(entityIn);
    }
    
    @Override
    public void onCollision(World worldIn, Collection<Entity> entities) {
        structure.entities.clear();
        structure.entities.addAll(entities);
    }
    
    @Override
    public void onRightClick(World worldIn, LittleTile tile, BlockPos pos, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ, LittleActionActivated action) {
        // TODO Auto-generated method stub
        
    }
    
}
