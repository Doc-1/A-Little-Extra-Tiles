package com.alet.items;

import com.alet.client.gui.SubGuiLittleRope;
import com.creativemd.creativecore.common.utils.math.Rotation;
import com.creativemd.littletiles.LittleTiles;
import com.creativemd.littletiles.client.gui.configure.SubGuiConfigure;
import com.creativemd.littletiles.common.api.ILittleTool;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemLittleRope extends Item implements ILittleTool {
    
    public ItemLittleRope() {}
    
    public ItemLittleRope(String name) {
        setUnlocalizedName(name);
        setRegistryName(name);
        setCreativeTab(LittleTiles.littleTab);
        
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public SubGuiConfigure getConfigureGUI(EntityPlayer player, ItemStack stack) {
        return new SubGuiLittleRope(stack);
    }
    
    @Override
    public void rotate(EntityPlayer player, ItemStack stack, Rotation rotation, boolean client) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void flip(EntityPlayer player, ItemStack stack, Axis axis, boolean client) {
        // TODO Auto-generated method stub
        
    }
}
