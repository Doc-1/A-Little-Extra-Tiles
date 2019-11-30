package com.ltphoto.gui;

import com.creativemd.creativecore.common.gui.controls.gui.custom.GuiStackSelectorAll;
import com.creativemd.creativecore.common.gui.event.gui.GuiControlChangedEvent;
import com.creativemd.creativecore.common.utils.mc.BlockUtils;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public class GuiStack extends GuiStackSelectorAll {
	
	public String texture;
	private EnumFacing facing;
	private ItemStack itemStack;
	
	public GuiStack(String name, int x, int y, int width, EntityPlayer player, StackCollector collector, boolean searchBar) {
		super(name, x, y, width, player, collector, searchBar);
	}
	
	public void setEnumFacing(String face) {
		switch (face) {
		case "up":
			facing = EnumFacing.UP;
			break;
		case "down":
			facing = EnumFacing.DOWN;
			break;
		case "north":
			facing = EnumFacing.NORTH;
			break;
		case "east":
			facing = EnumFacing.EAST;
			break;
		case "south":
			facing = EnumFacing.SOUTH;
			break;
		case "west":
			facing = EnumFacing.WEST;
			break;
		default:
			facing = EnumFacing.NORTH;
			break;
		}
		getTexture(itemStack);
	}
	
	public EnumFacing getEnumFacing() {
		return facing;
	}
	
	@Override
	public boolean setSelected(ItemStack stack) {
		if (stacks.contains(stack)) {
			itemStack = stack;
			if (facing == null) {
				facing = EnumFacing.NORTH;
			}
			getTexture(itemStack);
			caption = stack.getDisplayName();
			this.selected = stack;
			raiseEvent(new GuiControlChangedEvent(this));
			return true;
		}
		return false;
	}
	
	public String getTexture(ItemStack stack) {
		Block block = Block.getBlockFromItem(stack.getItem());
		Item item = stack.getItem();
		int damage = item.getDamage(stack);
		int meta = item.getMetadata(damage);
		IBlockState state = BlockUtils.getState(block, meta);
		
		ResourceLocation reg = block.getRegistryName();
		ResourceLocation location = new ResourceLocation(reg.toString());
		Minecraft minecraft = Minecraft.getMinecraft();
		BlockRendererDispatcher ren = minecraft.getBlockRendererDispatcher();
		System.out.println(facing);
		texture = ren.getModelForState(state).getQuads(state, facing, 0).get(0).getSprite().toString();
		return texture;
	}
}
