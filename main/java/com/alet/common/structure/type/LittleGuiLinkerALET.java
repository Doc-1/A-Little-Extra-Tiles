package com.alet.common.structure.type;

import com.creativemd.creativecore.common.gui.container.GuiParent;
import com.creativemd.creativecore.common.gui.controls.gui.GuiTextfield;
import com.creativemd.littletiles.common.action.block.LittleActionActivated;
import com.creativemd.littletiles.common.structure.LittleStructure;
import com.creativemd.littletiles.common.structure.animation.AnimationGuiHandler;
import com.creativemd.littletiles.common.structure.registry.LittleStructureGuiParser;
import com.creativemd.littletiles.common.structure.registry.LittleStructureRegistry;
import com.creativemd.littletiles.common.structure.registry.LittleStructureType;
import com.creativemd.littletiles.common.tile.LittleTile;
import com.creativemd.littletiles.common.tile.parent.IStructureTileList;
import com.creativemd.littletiles.common.tile.preview.LittlePreviews;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class LittleGuiLinkerALET extends LittleStructure {
    
    public BlockPos linkedBlock = new BlockPos(0, 0, 0);
    
    public LittleGuiLinkerALET(LittleStructureType type, IStructureTileList mainBlock) {
        super(type, mainBlock);
        // TODO Auto-generated constructor stub
    }
    
    @Override
    protected void loadFromNBTExtra(NBTTagCompound nbt) {
        NBTTagCompound n = nbt.getCompoundTag("linked_block");
        linkedBlock = NBTUtil.getPosFromTag(n);
    }
    
    @Override
    protected void writeToNBTExtra(NBTTagCompound nbt) {
        nbt.setTag("linked_block", NBTUtil.createPosTag(linkedBlock));
    }
    
    @Override
    public boolean onBlockActivated(World worldIn, LittleTile tile, BlockPos pos, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ, LittleActionActivated action) {
        if (!worldIn.isRemote) {
            BlockPos newPos = new BlockPos(linkedBlock.getX() + this.getPos().getX(), linkedBlock.getY() + this.getPos().getY(), linkedBlock.getZ() + this.getPos().getZ());
            float f = (float) (hitX - (double) newPos.getX());
            float f1 = (float) (hitY - (double) newPos.getY());
            float f2 = (float) (hitZ - (double) newPos.getZ());
            IBlockState iblockstate = worldIn.getBlockState(newPos);
            if (worldIn.getTileEntity(newPos) instanceof IInventory)
                System.out.println(((IInventory) worldIn.getTileEntity(newPos)));
            boolean result = iblockstate.getBlock().onBlockActivated(worldIn, newPos, iblockstate, playerIn, hand, side, f, f1, f2);
            if (result == false)
                playerIn.sendMessage(new TextComponentString("Block: " + iblockstate + " at " + newPos + " failed to open."));
        }
        return true;
    }
    
    public static class LittleGuiLinkerParserALET extends LittleStructureGuiParser {
        
        public LittleGuiLinkerParserALET(GuiParent parent, AnimationGuiHandler handler) {
            super(parent, handler);
            // TODO Auto-generated constructor stub
        }
        
        @Override
        protected void createControls(LittlePreviews previews, LittleStructure structure) {
            LittleGuiLinkerALET guiLinker = structure instanceof LittleGuiLinkerALET ? (LittleGuiLinkerALET) structure : null;
            GuiTextfield x = new GuiTextfield("x", "0", 0, 0, 25, 15);
            GuiTextfield y = new GuiTextfield("y", "0", 33, 0, 25, 15);
            GuiTextfield z = new GuiTextfield("z", "0", 100, 0, 25, 15);
            if (guiLinker != null) {
                x.text = guiLinker.linkedBlock.getX() + "";
                y.text = guiLinker.linkedBlock.getY() + "";
                z.text = guiLinker.linkedBlock.getZ() + "";
            }
            parent.controls.add(x);
            parent.controls.add(y);
            parent.controls.add(z);
            
        }
        
        @Override
        protected LittleStructure parseStructure(LittlePreviews previews) {
            LittleGuiLinkerALET structure = createStructure(LittleGuiLinkerALET.class, null);
            GuiTextfield x = (GuiTextfield) parent.get("x");
            GuiTextfield y = (GuiTextfield) parent.get("y");
            GuiTextfield z = (GuiTextfield) parent.get("z");
            structure.linkedBlock = new BlockPos(Integer.parseInt(x.text), Integer.parseInt(y.text), Integer.parseInt(z.text));
            return structure;
        }
        
        @Override
        protected LittleStructureType getStructureType() {
            return LittleStructureRegistry.getStructureType(LittleGuiLinkerALET.class);
        }
        
    }
}
