
package com.MasterForge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import com.creativemd.creativecore.common.utils.type.HashMapList;
import com.creativemd.littletiles.common.action.block.LittleActionActivated;
import com.creativemd.littletiles.common.action.block.LittleActionPlaceStack;
import com.creativemd.littletiles.common.structure.connection.IStructureChildConnector;
import com.creativemd.littletiles.common.structure.exception.MissingTileEntity;
import com.creativemd.littletiles.common.structure.premade.LittleStructurePremade;
import com.creativemd.littletiles.common.structure.registry.LittleStructureType;
import com.creativemd.littletiles.common.tile.LittleTile;
import com.creativemd.littletiles.common.tile.math.vec.LittleVec;
import com.creativemd.littletiles.common.tile.place.PlacePreview;
import com.creativemd.littletiles.common.tile.place.PlacePreviews;
import com.creativemd.littletiles.common.tile.preview.LittlePreview;
import com.creativemd.littletiles.common.tile.preview.LittlePreviews;
import com.creativemd.littletiles.common.tileentity.TileEntityLittleTiles;
import com.creativemd.littletiles.common.tileentity.TileEntityLittleTilesTicking;
import com.creativemd.littletiles.common.util.grid.LittleGridContext;
import com.creativemd.littletiles.common.util.place.PlacementMode;
import com.creativemd.littletiles.common.util.vec.SurroundingBox;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ServerTickEvent;
import scala.reflect.internal.Trees.This;

public class MultiTileStructure extends LittleStructurePremade {
	
	private int counter = 0;
	private int seriesIndex = 6;
	private String seriesName = type.id.toString().split("_")[0];

	public MultiTileStructure(LittleStructureType type) {
		super(type);
	}
	
	
	
	@Override
	protected void loadFromNBTExtra(NBTTagCompound nbt) {}
	
	@Override
	protected void writeToNBTExtra(NBTTagCompound nbt) {}
	
	private String nextSeries() {
		int seriesAt = Integer.parseInt(type.id.toString().split("_")[1]);
		if(seriesIndex > seriesAt) {
			return seriesName + "_" + (seriesAt+1);
		}
		return "";
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, LittleTile tile, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ, LittleActionActivated action) {
		
		if(MultiTileStructureRecipe.takeIngredients(playerIn, type) && !playerIn.world.isRemote) {
			String next = nextSeries();
			if(!next.isEmpty()) {
				SurroundingBox box = new SurroundingBox(false).add(tiles.entrySet());
				long minX = box.getMinX();
				long minY = box.getMinY();
				long minZ = box.getMinZ();
				LittleGridContext context = box.getContext();
				BlockPos min = new BlockPos(context.toBlockOffset(minX), context.toBlockOffset(minY), context.toBlockOffset(minZ));
				LittleVec minVec = new LittleVec((int) (minX - (long) min.getX() * (long) context.size), (int) (minY - (long) min.getY() * (long) context.size), (int) (minZ - (long) min.getZ() * (long) context.size));
	
				ItemStack stack = getPremadeStack(next); // Change this line to support different states
				LittlePreviews previews = LittlePreviews.getPreview(stack, true);
				LittleVec previewMinVec = previews.getMinVec();
				
				for (LittlePreview preview : previews) {
					preview.box.sub(previewMinVec);
					preview.box.add(minVec);
				}
				
				previews.convertToSmallest();
				
				List<PlacePreview> placePreviews = new ArrayList<>();
				previews.getPlacePreviews(placePreviews, null, true, LittleVec.ZERO);
				
				HashMap<BlockPos, PlacePreviews> splitted = LittleActionPlaceStack.getSplittedTiles(previews.context, placePreviews, min);
				//Test if the structure can be placed.
				if (LittleActionPlaceStack.canPlaceTiles(null, worldIn, splitted, PlacementMode.overwrite.getCoordsToCheck(splitted, min), PlacementMode.overwrite, (LittleTile x) -> !x.isChildOfStructure(this), false)) {
					// Remove existing structure
					this.removeStructure();
					// Places new structure
					LittleActionPlaceStack.placeTilesWithoutPlayer(worldIn, previews.context, splitted, previews.getStructure(), PlacementMode.normal, min, null, null, null, null);
				} else {
					playerIn.sendStatusMessage(new TextComponentString("Not enough space!"), true);
				}
			}
		}
		return true;
	}
}
