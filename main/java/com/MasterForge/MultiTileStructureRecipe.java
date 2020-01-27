package com.MasterForge;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.creativemd.littletiles.common.structure.registry.LittleStructureType;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentString;

public class MultiTileStructureRecipe {
	
	private static HashMap<String, ItemStack> recipeDict = new HashMap<>();
	private static HashMap<ItemStack, Integer> inventoryDict = new HashMap<>();
	
	/**
	 * @param id
	 * ID of the premade structure
	 * @param ingredient
	 * 
	 */
	public static void addRecipe(String id, Object ingredient, int count) {
		if(ingredient instanceof Item) {
			recipeDict.put(id, new ItemStack((Item) ingredient,count));
		}else if(ingredient instanceof Block) {
			recipeDict.put(id, new ItemStack((Block) ingredient,count));
		}
	}
	
	public static ItemStack findRecipe(String id) {
		Set set = recipeDict.entrySet();
		Iterator iterator = set.iterator();
		while(iterator.hasNext()) {
			Map.Entry mentry = (Map.Entry)iterator.next();
			if(mentry.getKey().equals(id)) {
				return (ItemStack) mentry.getValue();
			}
		}
		return null;
	}
	
	public static boolean hasIngredient(EntityPlayer playerIn, LittleStructureType type) {
		HashMap<ItemStack, Integer> invDict = new HashMap<>();
		ItemStack ingredient = MultiTileStructureRecipe.findRecipe(type.id);
		ItemStack test = new ItemStack(Items.APPLE, 128);
		playerIn.sendStatusMessage(new TextComponentString(ingredient.toString()), true);
		
		ItemStack heldItem = playerIn.getHeldItemMainhand();
		Iterator iterator = playerIn.inventoryContainer.inventoryItemStacks.iterator();
		System.out.println(playerIn.inventory.getStackInSlot(0));

		int counter = 0;
		while(iterator.hasNext()) {
			ItemStack itemStack = (ItemStack) iterator.next();
			invDict.put(itemStack, counter);
			counter++;
		}
		
		Set set = invDict.entrySet();
		iterator = set.iterator();
		while(iterator.hasNext()) {
			Map.Entry mentry = (Map.Entry)iterator.next();
			System.out.println(mentry.getKey()+" : "+mentry.getValue());
		}

		return false;
	}
	
	
}
