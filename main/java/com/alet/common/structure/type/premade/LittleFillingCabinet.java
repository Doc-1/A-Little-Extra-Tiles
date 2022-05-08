package com.alet.common.structure.type.premade;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.alet.client.container.SubContainerFillingCabinet;
import com.creativemd.littletiles.client.gui.handler.LittleStructureGuiHandler;
import com.creativemd.littletiles.common.action.LittleActionException;
import com.creativemd.littletiles.common.action.block.LittleActionActivated;
import com.creativemd.littletiles.common.structure.exception.CorruptedConnectionException;
import com.creativemd.littletiles.common.structure.exception.NotYetConnectedException;
import com.creativemd.littletiles.common.structure.registry.LittleStructureType;
import com.creativemd.littletiles.common.structure.type.door.LittleDoorBase;
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

public class LittleFillingCabinet extends LittleStructurePremade {
    
    private List<SubContainerFillingCabinet> openContainers = new ArrayList<SubContainerFillingCabinet>();
    
    public LittleFillingCabinet(LittleStructureType type, IStructureTileList mainBlock) {
        super(type, mainBlock);
    }
    
    @Override
    protected void loadFromNBTExtra(NBTTagCompound nbt) {
        
    }
    
    @Override
    protected void writeToNBTExtra(NBTTagCompound nbt) {
        
    }
    
    @Override
    public void tick() {
        if (!this.getWorld().isRemote) {
            try {
                LittleDoorBase door = (LittleDoorBase) this.getChild(0).getStructure();
                door.getOutput(0).updateState(getInput(0).getState());
            } catch (CorruptedConnectionException | NotYetConnectedException e) {}
        }
    }
    
    public void openContainer(SubContainerFillingCabinet container) {
        openContainers.add(container);
        updateInput();
    }
    
    public void closeContainer(SubContainerFillingCabinet container) {
        openContainers.remove(container);
        updateInput();
    }
    
    public boolean hasPlayerOpened(EntityPlayer player) {
        for (SubContainerFillingCabinet container : openContainers)
            if (container.getPlayer() == player)
                return true;
        return false;
    }
    
    protected void updateInput() {
        getInput(0).updateState(new boolean[] { !openContainers.isEmpty() });
    }
    
    @Override
    public boolean onBlockActivated(World worldIn, LittleTile tile, BlockPos pos, EntityPlayer player, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ, LittleActionActivated action) throws LittleActionException {
        if (!worldIn.isRemote && !hasPlayerOpened(player))
            LittleStructureGuiHandler.openGui("filling_cabinet", new NBTTagCompound(), player, this);
        return true;
    }
}
