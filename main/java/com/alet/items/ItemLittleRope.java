package com.alet.items;

import com.alet.client.gui.SubGuiLittleRope;
import com.alet.common.entity.RopeConnectionData;
import com.creativemd.creativecore.common.utils.math.Rotation;
import com.creativemd.littletiles.LittleTiles;
import com.creativemd.littletiles.client.gui.configure.SubGuiConfigure;
import com.creativemd.littletiles.common.api.ILittleTool;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
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
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer player, EnumHand handIn) {
        if (player.isSneaking()) {
            NBTTagCompound nbt = player.getHeldItemMainhand().getTagCompound();
            nbt.removeTag("selected1");
            nbt.removeTag("selected2");
            player.getHeldItemMainhand().setTagCompound(nbt);
        }
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, player.getHeldItem(handIn));
    }
    
    public static RopeConnectionData writeDataFromNBT(NBTTagCompound nbt) {
        return new RopeConnectionData(nbt.getInteger("color"), nbt.getDouble("thickness"), nbt.getDouble("tautness"));
    }
    
    public static boolean hasSelected(int i, ItemStack rope) {
        NBTTagCompound nbt = rope.getTagCompound();
        if (nbt != null && nbt.hasKey("selected" + i))
            return true;
        return false;
    }
    
    public static boolean hasBothSelected(ItemStack rope) {
        NBTTagCompound nbt = rope.getTagCompound();
        if (nbt != null && (nbt.hasKey("selected1") && nbt.hasKey("selected2")))
            return true;
        return false;
    }
    
    public static void addSelected(int i, ItemStack rope, int index, BlockPos pos) {
        
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("index", index);
        tag.setTag("pos", NBTUtil.createPosTag(pos));
        
        NBTTagCompound nbt = rope.getTagCompound() == null ? new NBTTagCompound() : rope.getTagCompound();
        nbt.setTag("selected" + i, tag);
        rope.setTagCompound(nbt);
        
    }
    
    public static void removeSelected(ItemStack rope) {
        NBTTagCompound nbt = rope.getTagCompound();
        if (hasBothSelected(rope)) {
            nbt.removeTag("selected1");
            nbt.removeTag("selected2");
            rope.setTagCompound(nbt);
        }
    }
    
    public static void removeSelected(int i, ItemStack rope) {
        NBTTagCompound nbt = rope.getTagCompound();
        nbt.removeTag("selected" + i);
        rope.setTagCompound(nbt);
        
    }
    
    public static BlockPos getSelectedPosition(int i, ItemStack rope) {
        if (!hasBothSelected(rope))
            return null;
        else {
            NBTTagCompound nbt = (NBTTagCompound) rope.getTagCompound().getTag("selected" + i);
            nbt = (NBTTagCompound) nbt.getTag("pos");
            return NBTUtil.getPosFromTag(nbt);
        }
    }
    
    public static int getSelectedIndex(int i, ItemStack rope) {
        if (!hasBothSelected(rope))
            return -1;
        else {
            NBTTagCompound nbt = (NBTTagCompound) rope.getTagCompound().getTag("selected" + i);
            
            return nbt.getInteger("index");
        }
    }
    
    public static boolean hasSameSelected(ItemStack rope, double x, double y, double z) {
        if (!hasBothSelected(rope))
            return false;
        else {
            return false;
        }
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
