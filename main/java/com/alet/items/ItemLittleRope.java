package com.alet.items;

import com.alet.client.gui.SubGuiLittleRope;
import com.creativemd.creativecore.common.utils.math.Rotation;
import com.creativemd.littletiles.LittleTiles;
import com.creativemd.littletiles.client.gui.configure.SubGuiConfigure;
import com.creativemd.littletiles.common.api.ILittleTool;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
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
    
    public boolean hasSelected(ItemStack rope) {
        NBTTagCompound nbt = rope.getTagCompound();
        if (nbt != null && nbt.hasKey("selected"))
            return true;
        return false;
    }
    
    public void addSelected(ItemStack rope, double x, double y, double z, double ax, double ay, double az) {
        NBTTagCompound n = new NBTTagCompound();
        n.setDouble("x", x);
        n.setDouble("y", y);
        n.setDouble("z", z);
        n.setDouble("ax", ax);
        n.setDouble("ay", ay);
        n.setDouble("az", az);
        
        NBTTagCompound nbt = rope.getTagCompound() == null ? new NBTTagCompound() : rope.getTagCompound();
        nbt.setTag("selected", n);
        rope.setTagCompound(nbt);
    }
    
    public void removeSelected(ItemStack rope) {
        NBTTagCompound nbt = rope.getTagCompound();
        if (this.hasSelected(rope)) {
            nbt.removeTag("selected");
            rope.setTagCompound(nbt);
        }
    }
    
    public Vec3d getSelected(ItemStack rope) {
        if (!this.hasSelected(rope))
            return null;
        else {
            NBTTagCompound sel = (NBTTagCompound) rope.getTagCompound().getTag("selected");
            
            return new Vec3d(sel.getDouble("x") + sel.getDouble("ax"), sel.getDouble("y") + sel.getDouble("ay"), sel.getDouble("z") + sel.getDouble("az"));
        }
    }
    
    public boolean hasSameSelected(ItemStack rope, double x, double y, double z) {
        if (!this.hasSelected(rope))
            return false;
        else {
            Vec3d vec1 = getSelected(rope);
            Vec3d vec2 = new Vec3d(x, y, z);
            return vec1.equals(vec2);
        }
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        if (playerIn.isSneaking()) {
            ItemStack stack = playerIn.getHeldItem(handIn);
            if (this.hasSelected(stack))
                this.removeSelected(stack);
            
        }
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
