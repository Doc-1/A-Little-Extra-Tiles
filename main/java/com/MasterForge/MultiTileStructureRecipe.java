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

public class MultiTileStructureRecipe {
	
	public static HashMap<Object, Integer> recipeDict = new HashMap<>();
	
	/**
	 * @param id
	 * ID of the premade structure
	 * @param ingredients
	 * List of Items/Blocks/Structures then followed by an integer indicating the number of that item needed.
	 * The ingredients with be required in the order that is inputed
	 */
	public static void addRecipe(String id, Object... ingredients) {
		int amount = 0;
		Object ingredient = null;
		for(int x=0; x<ingredients.length; x++) {
			if(ingredients[x] instanceof Integer) {
				amount = (int) ingredients[x];
				System.out.println(ingredients[x] + " Amount");
			}else if(ingredients[x] instanceof Item || ingredients[x] instanceof Block) {
				ingredient = ingredients[x];
				System.out.println(ingredients[x] + " Ingredient");
			}else {
				System.out.println(ingredients[x] + " Error " + ingredients[x].getClass());
			}
		}
		recipeDict.put(ingredient, amount);
	}
	
	public static void findRecipe() {
		Set set = recipeDict.entrySet();
		Iterator iterator = set.iterator();
		while(iterator.hasNext()) {
	         Map.Entry mentry = (Map.Entry)iterator.next();
	         System.out.println("key is: "+ mentry.getKey() + " & Value is: " + mentry.getValue());
		}
	}
}
