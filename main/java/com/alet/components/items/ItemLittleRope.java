package com.alet.components.items;

import java.util.List;

import com.alet.common.gui.tool.SubGuiLittleRope;
import com.alet.components.entities.RopeData;
import com.creativemd.creativecore.common.utils.math.Rotation;
import com.creativemd.creativecore.common.utils.mc.ColorUtils;
import com.creativemd.littletiles.LittleTiles;
import com.creativemd.littletiles.client.gui.configure.SubGuiConfigure;
import com.creativemd.littletiles.common.api.ILittleTool;
import com.creativemd.littletiles.common.tile.math.location.StructureLocation;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
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
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer player, EnumHand handIn) {
        if (player.isSneaking())
            removeSelected(player.getHeldItemMainhand());
        
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, player.getHeldItem(handIn));
    }
    
    public static RopeData writeDataFromNBT(NBTTagCompound nbt) {
        System.out.println(nbt);
        return new RopeData(nbt.getInteger("color"), nbt.getDouble("thickness"), nbt.getDouble("tautness"));
    }
    
    public static boolean hasSelected(ItemStack rope) {
        NBTTagCompound nbt = rope.getTagCompound();
        if (nbt != null && (nbt.hasKey("selected")))
            return true;
        return false;
    }
    
    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if (worldIn != null && !stack.hasTagCompound()) {
            NBTTagCompound stackNBT = new NBTTagCompound();
            stackNBT.setDouble("thickness", 0.3F);
            stackNBT.setDouble("tautness", 0.5F);
            stackNBT.setInteger("color", ColorUtils.BLACK);
            stack.setTagCompound(stackNBT);
        }
    }
    
    public static void addSelected(ItemStack rope, StructureLocation location) {
        NBTTagCompound nbt = rope.getTagCompound() == null ? new NBTTagCompound() : rope.getTagCompound();
        nbt.setTag("selected", location.write());
        rope.setTagCompound(nbt);
        
    }
    
    public static void removeSelected(ItemStack rope) {
        NBTTagCompound nbt = rope.getTagCompound();
        nbt.removeTag("selected");
        rope.setTagCompound(nbt);
        
    }
    
    public static StructureLocation getSelectedLocation(ItemStack rope) {
        return new StructureLocation(rope.getTagCompound().getCompoundTag("selected"));
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
