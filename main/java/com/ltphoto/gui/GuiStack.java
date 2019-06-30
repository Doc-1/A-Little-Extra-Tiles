package com.ltphoto.gui;

import java.awt.image.BufferedImage;

import com.creativemd.creativecore.common.gui.controls.gui.custom.GuiStackSelectorAll;
import com.creativemd.creativecore.common.gui.controls.gui.custom.GuiStackSelectorAll.StackCollector;
import com.creativemd.creativecore.common.gui.event.gui.GuiControlChangedEvent;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStone;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelBlockDefinition;
import net.minecraft.client.renderer.block.model.ModelManager;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.model.SimpleBakedModel;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public class GuiStack extends GuiStackSelectorAll{

	public String texture;
	private EnumFacing facing;
	private ItemStack itemStack;
	
	public GuiStack(String name, int x, int y, int width, EntityPlayer player, StackCollector collector,
			boolean searchBar) {
		super(name, x, y, width, player, collector, searchBar);
	}
	
	public void setEnumFacing(String face) {
		switch(face) {
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
		
		Block block = Block.getBlockFromItem(itemStack.getItem());
		Item item = itemStack.getItem();
		int damage = item.getDamage(itemStack);
		int meta = item.getMetadata(damage);
		IBlockState state = block.getStateFromMeta(meta);
		
		ResourceLocation reg = block.getRegistryName();
		ResourceLocation location = new ResourceLocation(reg.toString());
		Minecraft minecraft = Minecraft.getMinecraft();
		BlockRendererDispatcher ren = minecraft.getBlockRendererDispatcher();
		System.out.println(facing);
		texture = ren.getModelForState(state).getQuads(state, facing, 0).get(0).getSprite().toString();
		
	}
	
	public EnumFacing getEnumFacing() {
		return facing;
	}
	
	@Override
	public boolean setSelected(ItemStack stack) {
		if (stacks.contains(stack)) {
			itemStack = stack;
			if(facing == null) {
				facing = EnumFacing.NORTH;
			}
			Block block = Block.getBlockFromItem(itemStack.getItem());
			Item item = itemStack.getItem();
			int damage = item.getDamage(itemStack);
			int meta = item.getMetadata(damage);
			IBlockState state = block.getStateFromMeta(meta);
			
			ResourceLocation reg = block.getRegistryName();
			ResourceLocation location = new ResourceLocation(reg.toString());
			Minecraft minecraft = Minecraft.getMinecraft();
			BlockRendererDispatcher ren = minecraft.getBlockRendererDispatcher();
			System.out.println(facing);
			texture = ren.getModelForState(state).getQuads(state, facing, 0).get(0).getSprite().toString();
			
			caption = stack.getDisplayName();
			this.selected = stack;
			raiseEvent(new GuiControlChangedEvent(this));
			return true;
		}
		return false;
	}
}
