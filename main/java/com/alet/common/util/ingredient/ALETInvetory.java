package com.alet.common.util.ingredient;

import com.creativemd.littletiles.common.util.ingredient.LittleInventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.items.IItemHandler;

public class ALETInvetory extends LittleInventory {
    
    public ALETInvetory(EntityPlayer player) {
        super(player);
    }
    
    public ALETInvetory(EntityPlayer player, IItemHandler inventory) {
        super(player, inventory);
    }
}
