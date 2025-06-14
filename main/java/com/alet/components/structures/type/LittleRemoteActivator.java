package com.alet.components.structures.type;

import com.creativemd.littletiles.common.action.block.LittleActionActivated;
import com.creativemd.littletiles.common.structure.LittleStructure;
import com.creativemd.littletiles.common.structure.registry.LittleStructureType;
import com.creativemd.littletiles.common.tile.LittleTile;
import com.creativemd.littletiles.common.tile.parent.IStructureTileList;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class LittleRemoteActivator extends LittleStructure {
    
    public BlockPos linkedBlock = new BlockPos(0, 0, 0);
    public boolean useAbsolutePos = false;
    
    public LittleRemoteActivator(LittleStructureType type, IStructureTileList mainBlock) {
        super(type, mainBlock);
        // TODO Auto-generated constructor stub
    }
    
    @Override
    protected void loadFromNBTExtra(NBTTagCompound nbt) {
        if (nbt.hasKey("linkedBlock")) {
            NBTTagCompound n = nbt.getCompoundTag("linkedBlock");
            linkedBlock = NBTUtil.getPosFromTag(n);
        }
        if (nbt.hasKey("useAbsolutePos"))
            useAbsolutePos = nbt.getBoolean("useAbsolutePos");
    }
    
    @Override
    protected void writeToNBTExtra(NBTTagCompound nbt) {
        nbt.setTag("linkedBlock", NBTUtil.createPosTag(linkedBlock));
        nbt.setBoolean("useAbsolutePos", useAbsolutePos);
    }
    
    @Override
    public boolean onBlockActivated(World worldIn, LittleTile tile, BlockPos pos, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ, LittleActionActivated action) {
        if (worldIn.isRemote) {
            return true;
        }
        BlockPos newPos;
        if (useAbsolutePos)
            newPos = new BlockPos(linkedBlock.getX(), linkedBlock.getY(), linkedBlock.getZ());
        else
            newPos = new BlockPos(linkedBlock.getX() + this.getPos().getX(), linkedBlock.getY() + this.getPos()
                    .getY(), linkedBlock.getZ() + this.getPos().getZ());
        
        float f = (float) (hitX - (double) newPos.getX());
        float f1 = (float) (hitY - (double) newPos.getY());
        float f2 = (float) (hitZ - (double) newPos.getZ());
        IBlockState iblockstate = worldIn.getBlockState(newPos);
        TileEntity tileentity = worldIn.getTileEntity(newPos);
        if (tileentity != null) {
            double distance = new Vec3d(playerIn.posX, playerIn.posY, playerIn.posZ).distanceTo(new Vec3d(newPos
                    .getX(), newPos.getY(), newPos.getZ()));
            if (distance <= 8.5D) {
                if (!iblockstate.getBlock().onBlockActivated(worldIn, newPos, iblockstate, playerIn, hand, side, f, f1, f2))
                    playerIn.sendMessage(
                        new TextComponentString("Block: " + iblockstate + " at " + newPos + " failed to activate."));
            } else {
                playerIn.sendMessage(
                    new TextComponentString("Block: " + iblockstate + " at " + newPos + " is to farway. Must be within 8.5 blocks player."));
            }
        }
        if (tileentity == null)
            playerIn.sendMessage(
                new TextComponentString("Block: " + iblockstate + " at " + newPos + " doesn't have a tile entity."));
        return true;
    }
    
}
