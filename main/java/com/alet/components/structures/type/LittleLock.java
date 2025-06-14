package com.alet.components.structures.type;

import com.creativemd.littletiles.common.action.block.LittleActionActivated;
import com.creativemd.littletiles.common.structure.LittleStructure;
import com.creativemd.littletiles.common.structure.exception.CorruptedConnectionException;
import com.creativemd.littletiles.common.structure.exception.NotYetConnectedException;
import com.creativemd.littletiles.common.structure.registry.LittleStructureType;
import com.creativemd.littletiles.common.structure.signal.output.InternalSignalOutput;
import com.creativemd.littletiles.common.structure.type.door.LittleDoor;
import com.creativemd.littletiles.common.tile.LittleTile;
import com.creativemd.littletiles.common.tile.parent.IStructureTileList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class LittleLock extends LittleStructure {
    
    public int[] toLock;
    public boolean lockIfOpen = true;
    public ItemStack key = ItemStack.EMPTY;
    public boolean playSound = true;
    public boolean consumeKey = false;
    
    public LittleLock(LittleStructureType type, IStructureTileList mainBlock) {
        super(type, mainBlock);
        
    }
    
    @Override
    public boolean onBlockActivated(World worldIn, LittleTile tile, BlockPos pos, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ, LittleActionActivated action) {
        if (!worldIn.isRemote)
            if (!key.isEmpty()) {
                if (heldItem.getItem().equals(key.getItem())) {
                    getOutput(0).toggle();
                    if (consumeKey)
                        heldItem.shrink(1);
                } else
                    playerIn.sendStatusMessage(new TextComponentString("Incorrect Key"), true);
            } else
                getOutput(0).toggle();
        else if (playSound) {
            SoundEvent event = new SoundEvent(new ResourceLocation("block.wooden_trapdoor.close"));
            worldIn.playSound(playerIn, pos.getX(), pos.getY(), pos.getZ(), event, SoundCategory.BLOCKS, 1.0F, 1.6F);
        }
        return true;
    }
    
    public void lockDoor(LittleDoor door) {
        if (door.disableRightClick) {
            door.disableRightClick = false;
        } else {
            door.disableRightClick = true;
        }
        door.mainBlock.getTe().updateBlock();
        door.mainBlock.getTe().updateNeighbour();
        door.updateStructure();
    }
    
    public void changeLockState() {
        try {
            for (int c : toLock) {
                LittleStructure child = this.getChild(c).getStructure();
                if (child instanceof LittleDoor) {
                    LittleDoor door = (LittleDoor) child;
                    if (!lockIfOpen) {
                        if (!door.opened)
                            lockDoor(door);
                    } else
                        lockDoor(door);
                    
                }
                updateStructure();
            }
        } catch (CorruptedConnectionException | NotYetConnectedException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    protected void loadFromNBTExtra(NBTTagCompound nbt) {
        toLock = nbt.getIntArray("toLock");
        key = new ItemStack(nbt.getCompoundTag("key"));
        lockIfOpen = nbt.getBoolean("lockIfOpen");
        playSound = nbt.getBoolean("playSound");
        consumeKey = nbt.getBoolean("consumeKey");
    }
    
    @Override
    protected void writeToNBTExtra(NBTTagCompound nbt) {
        nbt.setIntArray("toLock", toLock);
        NBTTagCompound nbtItemStack = new NBTTagCompound();
        key.writeToNBT(nbtItemStack);
        nbt.setTag("key", nbtItemStack);
        nbt.setBoolean("lockIfOpen", lockIfOpen);
        nbt.setBoolean("playSound", playSound);
        nbt.setBoolean("consumeKey", consumeKey);
    }
    
    @Override
    public void performInternalOutputChange(InternalSignalOutput output) {
        if (output.component.is("lock")) {
            changeLockState();
        }
    }
    
}

/*
 * for (int c : fixedChildren) {
			NBTTagCompound nbt = new NBTTagCompound();
			StructureChildConnection child = this.getChild(c);
			
			Iterable<IStructureTileList> v = child.getStructure().blocksList();
			for (IStructureTileList littleTile : child.getStructure().blocksList()) {
				
				for (Pair<IParentTileList, LittleTile> tiles : littleTile.getTe().allTiles()) {
					
					tiles.value.invisible = true;
				}
				littleTile.getTe().updateBlock();
				littleTile.getTe().updateNeighbour();
				child.getStructure().updateStructure();
			}
			updateStructure();
		}
 */
