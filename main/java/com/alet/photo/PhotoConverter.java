package com.alet.photo;

import java.io.IOException;

import com.creativemd.creativecore.common.gui.container.SubGui;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class PhotoConverter implements Runnable {
	
	private String input;
	private boolean uploadOption;
	private int grid;
	private NBTTagCompound nbt;
	private SubGui gui;
	
	public PhotoConverter(String input, boolean uploadOption, int grid, SubGui subGui) {
		this.grid = grid;
		this.input = input;
		this.uploadOption = uploadOption;
		this.nbt = new NBTTagCompound();
		this.gui = subGui;
	}
	
	@Override
	public void run() {
		try {
			ItemStack stack = PhotoReader.photoToStack(input, uploadOption, grid, gui);
			if (!stack.equals(ItemStack.EMPTY)) {
				nbt = stack.getTagCompound();
				gui.sendPacketToServer(nbt);
			}
		} catch (IOException e) {
		}
	}
	
	/** Orignal Author: Timardo
	 * Modified by: _Doc
	 * <b>MUCH</b> faster equivalent of {@link LittlePreview#savePreview(LittlePreviews, ItemStack)} which is tweaked a bit to fit the needs of this mod
	 * 
	 * @param model
	 *            - the model */
	/*
	private static ItemStack fastSavePreview(LittlePreviews previews, List<LittlePreview> previewsList) {
		NBTTagCompound ret = new NBTTagCompound();
		LinkedHashMap<Integer, List<LittlePreview>> colorMap = new LinkedHashMap<Integer, List<LittlePreview>>();
		
		List<LittlePreview> ls = new ArrayList<LittlePreview>();
		for (LittlePreview pre : previewsList) {
			if (colorMap.containsKey(pre.getColor())) {
				colorMap.get(pre.getColor()).add(pre);
			} else {
				ls.add(pre);
				colorMap.put(pre.getColor(), ls);
				ls.clear();
			}
			
		}
		int minX = Integer.MAX_VALUE;
		int minY = Integer.MAX_VALUE;
		int minZ = Integer.MAX_VALUE;
		int maxX = Integer.MIN_VALUE;
		int maxY = Integer.MIN_VALUE;
		int maxZ = Integer.MIN_VALUE;
		
		for (LittlePreview preview : previews.allPreviews()) {
			minX = Math.min(minX, preview.box.minX);
			minY = Math.min(minY, preview.box.minY);
			minZ = Math.min(minZ, preview.box.minZ);
			maxX = Math.max(maxX, preview.box.maxX);
			maxY = Math.max(maxY, preview.box.maxY);
			maxZ = Math.max(maxZ, preview.box.maxZ);
		}
		
		new LittleVec(maxX - minX, maxY - minY, maxZ - minZ).writeToNBT("size", ret);
		new LittleVec(minX, minY, minZ).writeToNBT("min", ret); // TODO set base point to center of the object with min Y
		
		if (previews.totalSize() >= LittlePreview.lowResolutionMode) {
			NBTTagList list = new NBTTagList();
			// BlockPos lastPos = null;
			
			HashSet<BlockPos> positions = new HashSet<>();
			
			for (int i = 0; i < previews.size(); i++) { // Will not be sorted after rotating
				BlockPos pos = previews.get(i).box.getMinVec().getBlockPos(previews.getContext());
				if (!positions.contains(pos)) {
					positions.add(pos);
					list.appendTag(new NBTTagIntArray(new int[] { pos.getX(), pos.getY(), pos.getZ() }));
				}
			}
			ret.setTag("pos", list);
		} else {
			ret.removeTag("pos");
		}
		
		NBTTagList list = new NBTTagList();
		
		for (Integer color : colorMap.keySet()) {
			List<LittlePreview> pList = colorMap.get(color);
			ArrayList<LittlePreview> tileList = new ArrayList<LittlePreview>(pList);
			LittlePreview grouping = tileList.remove(0);
			NBTTagCompound groupNBT = null;
			
			for (Iterator<LittlePreview> iterator2 = tileList.iterator(); iterator2.hasNext();) {
				LittlePreview preview = (LittlePreview) iterator2.next();
				
				if (groupNBT == null) {
					groupNBT = grouping.startNBTGrouping();
				}
				
				grouping.groupNBTTile(groupNBT, preview);
				iterator2.remove();
			}
			
			if (groupNBT == null) {
				NBTTagCompound nbt = new NBTTagCompound();
				grouping.writeToNBT(nbt);
				list.appendTag(nbt);
			} else {
				list.appendTag(groupNBT);
			}
		}
		
		ret.setTag("tiles", list);
		ret.setInteger("count", previews.size());
		
		ItemStack recipe = new ItemStack(LittleTiles.recipeAdvanced);
		recipe.setTagCompound(ret);
		
		return recipe;
	}
	*/
}
