package com.alet.common.structure.type.premade.transfer;

import javax.vecmath.Vector3f;

import com.creativemd.creativecore.common.utils.mc.ColorUtils;
import com.creativemd.littletiles.common.structure.directional.StructureDirectional;
import com.creativemd.littletiles.common.structure.exception.CorruptedConnectionException;
import com.creativemd.littletiles.common.structure.exception.NotYetConnectedException;
import com.creativemd.littletiles.common.structure.registry.LittleStructureType;
import com.creativemd.littletiles.common.structure.relative.StructureRelative;
import com.creativemd.littletiles.common.structure.signal.output.InternalSignalOutput;
import com.creativemd.littletiles.common.structure.type.LittleNoClipStructure;
import com.creativemd.littletiles.common.structure.type.LittleStorage;
import com.creativemd.littletiles.common.structure.type.premade.LittleStructurePremade;
import com.creativemd.littletiles.common.tile.parent.IStructureTileList;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

public class LittleTransferItemImport extends LittleStructurePremade {
	
	@StructureDirectional(color = ColorUtils.CYAN)
	public StructureRelative frame;
	
	@StructureDirectional
	public EnumFacing facing;
	
	@StructureDirectional
	public Vector3f topRight;
	
	public boolean added = false;
	
	int counter = 0;
	
	public LittleTransferItemImport(LittleStructureType type, IStructureTileList mainBlock) {
		super(type, mainBlock);
	}
	
	@Override
	protected void loadFromNBTExtra(NBTTagCompound nbt) {
	}
	
	@Override
	protected void writeToNBTExtra(NBTTagCompound nbt) {
	}
	
	public void addItemToStorage(EntityItem itemToAdd) {
		try {
			if (this.getParent() != null)
				if (this.getParent().getStructure() instanceof LittleStorage) {
					LittleStorage storage = (LittleStorage) this.getParent().getStructure();
					for (int i = 0; i < storage.inventorySize; i++) {
						if (storage.inventory.getStackInSlot(i).getItem().equals(Items.AIR) || storage.inventory.getStackInSlot(i).getItem().equals(itemToAdd.getItem().getItem())) {
							ItemStack stack = storage.inventory.addItem(itemToAdd.getItem());
							if (stack.getItem().equals(Items.AIR)) {
								itemToAdd.setDead();
							} else if (stack.getCount() < 64) {
								itemToAdd.getItem().setCount(64 - stack.getCount());
							}
							break;
						}
					}
				}
		} catch (CorruptedConnectionException | NotYetConnectedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	@Override
	public void performInternalOutputChange(InternalSignalOutput output) {
		if (output.component.is("drop")) {
			if (!added) {
				added = true;
				counter = 0;
			}
		}
	}
	
	@Override
	public void tick() {
		if (!this.isClient()) {
			try {
				LittleNoClipStructure noclip = (LittleNoClipStructure) this.children.get(0).getStructure();
				for (Entity entity : noclip.entities) {
					if (entity instanceof EntityItem) {
						EntityItem eItem = (EntityItem) entity;
						addItemToStorage(eItem);
					}
				}
			} catch (CorruptedConnectionException | NotYetConnectedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
}
