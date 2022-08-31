package com.alet.common.structure.type;

import com.creativemd.creativecore.common.gui.container.GuiParent;
import com.creativemd.creativecore.common.gui.controls.gui.GuiCheckBox;
import com.creativemd.creativecore.common.gui.controls.gui.GuiLabel;
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
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class LittleRemoteActivatorALET extends LittleStructure {
    
    public BlockPos linkedBlock = new BlockPos(0, 0, 0);
    public boolean useAbsolutePos = false;
    
    public LittleRemoteActivatorALET(LittleStructureType type, IStructureTileList mainBlock) {
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
        if (!worldIn.isRemote) {
            
            BlockPos newPos;
            if (useAbsolutePos)
                newPos = new BlockPos(linkedBlock.getX(), linkedBlock.getY(), linkedBlock.getZ());
            else
                newPos = new BlockPos(linkedBlock.getX() + this.getPos().getX(), linkedBlock.getY() + this.getPos().getY(), linkedBlock.getZ() + this.getPos().getZ());
            
            float f = (float) (hitX - (double) newPos.getX());
            float f1 = (float) (hitY - (double) newPos.getY());
            float f2 = (float) (hitZ - (double) newPos.getZ());
            IBlockState iblockstate = worldIn.getBlockState(newPos);
            boolean result = iblockstate.getBlock().onBlockActivated(worldIn, newPos, iblockstate, playerIn, hand, side, f, f1, f2);
            if (result == false)
                playerIn.sendMessage(new TextComponentString("Block: " + iblockstate + " at " + newPos + " failed to activate."));
        }
        return true;
    }
    
    public static class LittleRemoteActivatorParserALET extends LittleStructureGuiParser {
        
        public LittleRemoteActivatorParserALET(GuiParent parent, AnimationGuiHandler handler) {
            super(parent, handler);
            // TODO Auto-generated constructor stub
        }
        
        @Override
        protected void createControls(LittlePreviews previews, LittleStructure structure) {
            LittleRemoteActivatorALET guiLinker = structure instanceof LittleRemoteActivatorALET ? (LittleRemoteActivatorALET) structure : null;
            parent.addControl(new GuiLabel("Pos X:", 0, 0));
            parent.addControl(new GuiLabel("Pos Y:", 0, 18));
            parent.addControl(new GuiLabel("Pos Z:", 0, 36));
            GuiTextfield x = new GuiTextfield("x", "0", 35, 0, 25, 10);
            GuiTextfield y = new GuiTextfield("y", "0", 35, 18, 25, 10);
            GuiTextfield z = new GuiTextfield("z", "0", 35, 36, 25, 10);
            if (guiLinker != null) {
                x.text = guiLinker.linkedBlock.getX() + "";
                y.text = guiLinker.linkedBlock.getY() + "";
                z.text = guiLinker.linkedBlock.getZ() + "";
            }
            parent.addControl(x);
            parent.addControl(y);
            parent.addControl(z);
            GuiCheckBox box = new GuiCheckBox("absolute", "Use Absolute Coordinates", 0, 55, guiLinker != null ? guiLinker.useAbsolutePos : true);
            parent.addControl(box);
        }
        
        @Override
        protected LittleStructure parseStructure(LittlePreviews previews) {
            LittleRemoteActivatorALET structure = createStructure(LittleRemoteActivatorALET.class, null);
            GuiTextfield x = (GuiTextfield) parent.get("x");
            GuiTextfield y = (GuiTextfield) parent.get("y");
            GuiTextfield z = (GuiTextfield) parent.get("z");
            structure.linkedBlock = new BlockPos(Integer.parseInt(x.text), Integer.parseInt(y.text), Integer.parseInt(z.text));
            structure.useAbsolutePos = ((GuiCheckBox) parent.get("absolute")).value;
            return structure;
        }
        
        @Override
        protected LittleStructureType getStructureType() {
            return LittleStructureRegistry.getStructureType(LittleRemoteActivatorALET.class);
        }
        
    }
}
