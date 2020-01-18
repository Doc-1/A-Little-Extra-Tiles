package com.MasterForge;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class MultiTileStructureRecipe {
	
	public static HashMap<Object, ItemStack> recipeDict = new HashMap<>();
	
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
	
	public static String findRecipe(String id) {
		Set set = recipeDict.entrySet();
		Iterator iterator = set.iterator();
		while(iterator.hasNext()) {

			Map.Entry mentry = (Map.Entry)iterator.next();
			if(mentry.getKey().equals(id)) {
		         ItemStack ingredient = (ItemStack) mentry.getValue();
		         return "Structure is: "+ mentry.getKey() + " & it takes " + ingredient + " ";
			}
		}
		return "";
	}
	
	
	
}
