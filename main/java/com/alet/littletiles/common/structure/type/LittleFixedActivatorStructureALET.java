package com.alet.littletiles.common.structure.type;

import java.util.ArrayList;
import java.util.List;

import com.creativemd.creativecore.common.gui.container.GuiParent;
import com.creativemd.creativecore.common.gui.controls.gui.GuiCheckBox;
import com.creativemd.creativecore.common.gui.controls.gui.GuiLabel;
import com.creativemd.littletiles.common.block.BlockStorageTile;
import com.creativemd.littletiles.common.structure.LittleStructure;
import com.creativemd.littletiles.common.structure.animation.AnimationGuiHandler;
import com.creativemd.littletiles.common.structure.connection.StructureChildConnection;
import com.creativemd.littletiles.common.structure.exception.CorruptedConnectionException;
import com.creativemd.littletiles.common.structure.exception.NotYetConnectedException;
import com.creativemd.littletiles.common.structure.registry.LittleStructureGuiParser;
import com.creativemd.littletiles.common.structure.registry.LittleStructureRegistry;
import com.creativemd.littletiles.common.structure.registry.LittleStructureType;
import com.creativemd.littletiles.common.structure.type.LittleStorage;
import com.creativemd.littletiles.common.tile.parent.IStructureTileList;
import com.creativemd.littletiles.common.tile.preview.LittlePreviews;

import net.minecraft.inventory.InventoryBasic;
import net.minecraft.nbt.NBTTagCompound;

public class LittleFixedActivatorStructureALET extends LittleStorage {
	
	public List<Integer> fixedChildren = new ArrayList<Integer>();
	public String item;
	
	public LittleFixedActivatorStructureALET(LittleStructureType type, IStructureTileList mainBlock) {
		super(type, mainBlock);
		
	}
	
	@Override
	protected void afterPlaced() {
		if (!children.isEmpty()) {
			for (StructureChildConnection child : children) {
				try {
					updateChildConnection(child.childId, child.getStructure(), true);
					child.getStructure().updateParentConnection(child.childId, this, true);
					
				} catch (CorruptedConnectionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NotYetConnectedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	@Override
	protected void loadFromNBTExtra(NBTTagCompound nbt) {
		for (int c : nbt.getIntArray("fixedChild")) {
			fixedChildren.add(c);
		}
	}
	
	@Override
	protected void writeToNBTExtra(NBTTagCompound nbt) {
		int[] c = new int[fixedChildren.size()];
		for (int i = 0; i < fixedChildren.size(); i++) {
			c[i] = fixedChildren.get(i);
		}
		nbt.setIntArray("fixedChild", c);
	}
	
	public static class LittleFixedActivatorStructureParserALET extends LittleStructureGuiParser {
		
		private List<Integer> fixedChildren = new ArrayList<Integer>();
		
		public LittleFixedActivatorStructureParserALET(GuiParent parent, AnimationGuiHandler handler) {
			super(parent, handler);
			
		}
		
		@Override
		protected void createControls(LittlePreviews previews, LittleStructure structure) {
			parent.controls.add(new GuiLabel("space: " + getSizeOfInventory(previews), 5, 0));
			boolean invisible = false;
			if (structure instanceof LittleStorage)
				invisible = ((LittleStorage) structure).invisibleStorageTiles;
			parent.controls.add(new GuiCheckBox("invisible", "invisible storage tiles", 5, 18, invisible));
			
			System.out.println(previews.getChildren());
			int i = 0;
			for (LittlePreviews child : previews.getChildren()) {
				child.structureNBT.setBoolean("dynamic", true);
				fixedChildren.add(i);
				i++;
			}
			
		}
		
		@Override
		protected LittleStructure parseStructure(LittlePreviews previews) {
			LittleFixedActivatorStructureALET storage = createStructure(LittleFixedActivatorStructureALET.class, null);
			storage.invisibleStorageTiles = ((GuiCheckBox) parent.get("invisible")).value;
			
			if (parent.getPlayer().isCreative()) {
				storage.fixedChildren = this.fixedChildren;
			}
			
			for (int i = 0; i < previews.size(); i++) {
				if (previews.get(i).getBlock() instanceof BlockStorageTile)
					previews.get(i).setInvisibile(storage.invisibleStorageTiles);
			}
			storage.inventorySize = getSizeOfInventory(previews);
			storage.stackSizeLimit = maxSlotStackSize;
			storage.updateNumberOfSlots();
			storage.inventory = new InventoryBasic("basic", false, storage.numberOfSlots);
			
			return storage;
		}
		
		@Override
		protected LittleStructureType getStructureType() {
			return LittleStructureRegistry.getStructureType(LittleFixedActivatorStructureALET.class);
		}
		
	}
}

/*
 * for (int c : fixedChildren) {
			NBTTagCompound nbt = new NBTTagCompound();
			StructureChildConnection child = this.getChild(c);
			
			Iterable<IStructureTileList> v = child.getStructure().blocksList();
			for (IStructureTileList littleTile : child.getStructure().blocksList()) {
				
				for (Pair<IParentTileList, LittleTile> tiles : littleTile.getTe().allTiles()) {
					
					tiles.value.invisible = true;
				}
				littleTile.getTe().updateBlock();
				littleTile.getTe().updateNeighbour();
				child.getStructure().updateStructure();
			}
			updateStructure();
		}
 */
