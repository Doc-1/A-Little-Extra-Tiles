package com.MasterForge;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.creativemd.creativecore.common.packet.PacketHandler;
import com.creativemd.littletiles.client.LittleTilesClient;
import com.creativemd.littletiles.common.action.LittleAction;
import com.creativemd.littletiles.common.packet.LittleActionMessagePacket;
import com.creativemd.littletiles.common.structure.registry.LittleStructureType;
import com.creativemd.littletiles.common.util.ingredient.LittleIngredients;
import com.creativemd.littletiles.common.util.ingredient.LittleInventory;
import com.creativemd.littletiles.common.util.ingredient.NotEnoughIngredientsException;
import com.creativemd.littletiles.common.util.ingredient.StackIngredient;
import com.creativemd.littletiles.common.util.ingredient.StackIngredientEntry;
import com.creativemd.littletiles.common.util.tooltip.ActionMessage;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
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
	public static void addRecipe(String id, Item ingredient, int count) {
		recipeDict.put(id, new ItemStack(ingredient,count));
	}
	
	public static void addRecipe(String id, Block ingredient, int count) {
		recipeDict.put(id, new ItemStack(ingredient,count));
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
	
	public static boolean takeIngredients(EntityPlayer playerIn, LittleStructureType type) {
		if (!playerIn.world.isRemote) {
			ItemStack ingredient = MultiTileStructureRecipe.findRecipe(type.id);
			
			StackIngredient stacks = new StackIngredient();
			stacks.add(new StackIngredientEntry(ingredient, ingredient.getCount()));
			
			LittleIngredients ingredients = new LittleIngredients(stacks);
			LittleInventory inventory = new LittleInventory(playerIn);
			
			try {
	            LittleAction.checkAndTake(playerIn, inventory, ingredients);
	        } catch (NotEnoughIngredientsException e) {
	            ActionMessage message = e.getActionMessage();
	            if (message != null) 
	            	PacketHandler.sendPacketToPlayer(new LittleActionMessagePacket(e.getActionMessage()), (EntityPlayerMP) playerIn);
	            else
	                playerIn.sendStatusMessage(new TextComponentString(e.getLocalizedMessage()), true);
	            return false;
	        }
		}
		return true;
	}
	
}




/*

		
		HashMap<ItemStack, Integer> invDict = new HashMap<>();
		HashMap<String, Integer> invTotalDict = new HashMap<>();

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
			//System.out.println(mentry.getKey()+" : "+mentry.getValue());
			
			ItemStack itemKey = (ItemStack) mentry.getKey();
			int meta = itemKey.getMetadata();
			String item = itemKey.getItem().getRegistryName().toString();
			int count = itemKey.getCount();
			
			String stackT = item+"`"+meta;

			if(invTotalDict.containsKey(stackT)) {
				System.out.println("da");
				invTotalDict.put(stackT, invTotalDict.get(stackT) + count);
			}else {
				System.out.println("fe");
				invTotalDict.put(stackT, count);

			}
		}

		set = invTotalDict.entrySet();
		iterator = set.iterator();
		
		while(iterator.hasNext()) {
			Map.Entry mentry = (Map.Entry)iterator.next();
			String[] split = mentry.getKey().toString().split("`");
			Item item = Item.getByNameOrId(split[0]);
			int meta = Integer.parseInt(split[1]);
			ItemStack stackType = new ItemStack(item, (int) mentry.getValue(), meta);
			System.out.println(stackType);
		}
*/