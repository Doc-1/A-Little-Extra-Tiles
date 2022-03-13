package com.alet.common.structure.type.premade.signal;

import javax.annotation.Nullable;

import com.alet.client.gui.controls.programmer.BlueprintCompiler;
import com.alet.client.gui.controls.programmer.BlueprintExecutor;
import com.creativemd.littletiles.client.gui.handler.LittleStructureGuiHandler;
import com.creativemd.littletiles.common.action.LittleActionException;
import com.creativemd.littletiles.common.action.block.LittleActionActivated;
import com.creativemd.littletiles.common.structure.registry.LittleStructureType;
import com.creativemd.littletiles.common.structure.type.premade.LittleStructurePremade;
import com.creativemd.littletiles.common.tile.LittleTile;
import com.creativemd.littletiles.common.tile.parent.IStructureTileList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class LittleCircuitMicroprocessor extends LittleStructurePremade {
    
    NBTTagCompound scriptNBT = new NBTTagCompound();
    BlueprintExecutor executor;
    
    public LittleCircuitMicroprocessor(LittleStructureType type, IStructureTileList mainBlock) {
        super(type, mainBlock);
    }
    
    @Override
    public boolean queueTick() {
        if (!executor.equals(null)) {
            executor.run();
            queueForNextTick();
        }
        return !this.executor.equals(null);
    }
    
    @Override
    protected void loadFromNBTExtra(NBTTagCompound nbt) {
        if (nbt.hasKey("script"))
            scriptNBT = nbt.getCompoundTag("script");
        this.executor = new BlueprintExecutor(this, BlueprintCompiler.readScript(scriptNBT));
        queueForNextTick();
    }
    
    @Override
    protected void writeToNBTExtra(NBTTagCompound nbt) {
        nbt.setTag("script", scriptNBT);
    }
    
    @Override
    public boolean onBlockActivated(World worldIn, LittleTile tile, BlockPos pos, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ, LittleActionActivated action) throws LittleActionException {
        if (!worldIn.isRemote)
            LittleStructureGuiHandler.openGui("programmer", new NBTTagCompound(), playerIn, this);
        return true;
    }
    
}
