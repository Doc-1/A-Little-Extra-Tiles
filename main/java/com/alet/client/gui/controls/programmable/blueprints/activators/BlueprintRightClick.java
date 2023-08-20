package com.alet.client.gui.controls.programmable.blueprints.activators;

import java.util.HashSet;
import java.util.List;

import com.alet.client.gui.controls.programmable.blueprints.GuiBlueprint;
import com.alet.client.gui.controls.programmable.nodes.GuiNode;
import com.alet.client.gui.controls.programmable.nodes.GuiNodeEntityUUID;
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
import net.minecraft.world.WorldServer;

public class BlueprintRightClick extends BlueprintActivator {
    
    public BlueprintRightClick(int id) {
        super(id);
        // TODO Auto-generated constructor stub
    }
    
    @Override
    public void onRightClick(World worldIn, LittleTile tile, BlockPos pos, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ, LittleActionActivated action) {
        this.structure.entities.add(playerIn);
    }
    
    @Override
    public void loopRules(Entity entities, boolean shouldContinue, boolean flag) {
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
    public void setNodeValue(WorldServer server) {
        GuiNodeEntityUUID uuid = (GuiNodeEntityUUID) this.getNode("uuid");
        uuid.setValue(uuids.get(0), false);
    }
    
    @Override
    public void setNodes() {
        super.setNodes();
        this.nodes.add(new GuiNodeEntityUUID("uuid", "Entity", GuiNode.SENDER));
    }
    
    @Override
    public boolean shouldRun(World world, HashSet<Entity> entities) {
        return entities != null && !entities.isEmpty();
    }
    
    @Override
    public void onCollision(World worldIn, List<Entity> entityIn) {
        // TODO Auto-generated method stub
        
    }
}
