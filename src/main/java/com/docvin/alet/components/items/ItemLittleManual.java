package com.docvin.alet.components.items;

import com.creativemd.creativecore.common.gui.opener.GuiHandler;
import com.creativemd.littletiles.LittleTiles;
import com.docvin.alet.common.registries.ALETGuis;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemLittleManual extends Item {

    public ItemLittleManual(String name) {
        setRegistryName(name);
        setCreativeTab(LittleTiles.littleTab);
    }

    @Override
    @MethodsReturnNonnullByDefault
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        if (worldIn.isRemote)
            GuiHandler.openGui(ALETGuis.ALET_GUI.MANUAL.getName(), new NBTTagCompound(), playerIn);
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }
}
