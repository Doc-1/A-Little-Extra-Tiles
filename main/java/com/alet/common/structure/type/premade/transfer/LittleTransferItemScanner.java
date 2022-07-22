package com.alet.common.structure.type.premade.transfer;

import java.util.ArrayList;
import java.util.List;

import com.creativemd.creativecore.common.utils.math.BooleanUtils;
import com.creativemd.littletiles.client.gui.handler.LittleStructureGuiHandler;
import com.creativemd.littletiles.common.action.LittleActionException;
import com.creativemd.littletiles.common.action.block.LittleActionActivated;
import com.creativemd.littletiles.common.structure.exception.CorruptedConnectionException;
import com.creativemd.littletiles.common.structure.exception.NotYetConnectedException;
import com.creativemd.littletiles.common.structure.registry.LittleStructureType;
import com.creativemd.littletiles.common.structure.type.LittleNoClipStructure;
import com.creativemd.littletiles.common.structure.type.premade.LittleStructurePremade;
import com.creativemd.littletiles.common.structure.type.premade.signal.LittleSignalOutput;
import com.creativemd.littletiles.common.tile.LittleTile;
import com.creativemd.littletiles.common.tile.parent.IStructureTileList;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class LittleTransferItemScanner extends LittleStructurePremade {
    
    private ItemStack stack;
    private boolean matched;
    private boolean itemExists;
    private boolean hasMultiply;
    
    public LittleTransferItemScanner(LittleStructureType type, IStructureTileList mainBlock) {
        super(type, mainBlock);
        
    }
    
    @Override
    public void tick() {
        if (!isClient()) {
            try {
                LittleSignalOutput itemStack = (LittleSignalOutput) this.children.get(0).getStructure();
                LittleSignalOutput multiplyItems = (LittleSignalOutput) this.children.get(3).getStructure();
                LittleNoClipStructure noClip = (LittleNoClipStructure) this.children.get(2).getStructure();
                List<EntityItem> items = new ArrayList<EntityItem>();
                
                if (!noClip.entities.isEmpty()) {
                    for (Entity entity : noClip.entities) {
                        if (entity instanceof EntityItem)
                            items.add((EntityItem) entity);
                    }
                }
                if (!items.isEmpty()) {
                    if (items.size() == 1) {
                        stack = items.get(0).getItem();
                        multiplyItems.updateState(BooleanUtils.SINGLE_TRUE);
                        itemStack.updateState(BooleanUtils.toBits(stack.getCount(), 16));
                    } else {
                        multiplyItems.updateState(BooleanUtils.SINGLE_FALSE);
                        itemStack.updateState(BooleanUtils.toBits(0, 16));
                    }
                } else {
                    multiplyItems.updateState(BooleanUtils.SINGLE_FALSE);
                    itemStack.updateState(BooleanUtils.toBits(0, 16));
                }
                
            } catch (CorruptedConnectionException | NotYetConnectedException e) {}
        }
    }
    
    @Override
    public boolean onBlockActivated(World worldIn, LittleTile tile, BlockPos pos, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ, LittleActionActivated action) throws LittleActionException {
        if (!worldIn.isRemote)
            LittleStructureGuiHandler.openGui("item_scanner", new NBTTagCompound(), playerIn, this);
        return true;
    }
    
    @Override
    protected void loadFromNBTExtra(NBTTagCompound nbt) {
        if (nbt.hasKey("matched"))
            matched = nbt.getBoolean("matched");
        if (nbt.hasKey("itemExists"))
            itemExists = nbt.getBoolean("itemExists");
        if (nbt.hasKey("hasMultiply"))
            hasMultiply = nbt.getBoolean("hasMultiply");
    }
    
    @Override
    protected void writeToNBTExtra(NBTTagCompound nbt) {
        nbt.setBoolean("matched", matched);
        nbt.setBoolean("itemExists", itemExists);
        nbt.setBoolean("hasMultiply", hasMultiply);
    }
    
}
