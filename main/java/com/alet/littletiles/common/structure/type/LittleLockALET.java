package com.alet.littletiles.common.structure.type;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import com.creativemd.creativecore.common.gui.container.GuiParent;
import com.creativemd.creativecore.common.gui.controls.gui.GuiCheckBox;
import com.creativemd.creativecore.common.gui.controls.gui.GuiScrollBox;
import com.creativemd.creativecore.common.gui.event.gui.GuiControlClickEvent;
import com.creativemd.littletiles.common.action.block.LittleActionActivated;
import com.creativemd.littletiles.common.structure.LittleStructure;
import com.creativemd.littletiles.common.structure.animation.AnimationGuiHandler;
import com.creativemd.littletiles.common.structure.exception.CorruptedConnectionException;
import com.creativemd.littletiles.common.structure.exception.NotYetConnectedException;
import com.creativemd.littletiles.common.structure.registry.LittleStructureGuiParser;
import com.creativemd.littletiles.common.structure.registry.LittleStructureRegistry;
import com.creativemd.littletiles.common.structure.registry.LittleStructureType;
import com.creativemd.littletiles.common.structure.signal.output.InternalSignalOutput;
import com.creativemd.littletiles.common.structure.type.door.LittleDoor;
import com.creativemd.littletiles.common.structure.type.door.LittleDoorBase;
import com.creativemd.littletiles.common.tile.LittleTile;
import com.creativemd.littletiles.common.tile.parent.IStructureTileList;
import com.creativemd.littletiles.common.tile.preview.LittlePreviews;
import com.n247s.api.eventapi.eventsystem.CustomEventSubscribe;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class LittleLockALET extends LittleStructure {
	
	public int[] toLock;
	public String item;
	
	public LittleLockALET(LittleStructureType type, IStructureTileList mainBlock) {
		super(type, mainBlock);
		
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, LittleTile tile, BlockPos pos, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ, LittleActionActivated action) {
		try {
			for (int c : toLock) {
				NBTTagCompound nbt = new NBTTagCompound();
				LittleStructure child = this.getChild(c).getStructure();
				if (child instanceof LittleDoorBase) {
					LittleDoorBase door = (LittleDoorBase) child;
					if (door.disableRightClick) {
						door.disableRightClick = false;
					} else {
						door.disableRightClick = true;
					}
					door.updateStructure();
				}
				updateStructure();
			}
		} catch (CorruptedConnectionException | NotYetConnectedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	
	@Override
	protected void loadFromNBTExtra(NBTTagCompound nbt) {
		toLock = nbt.getIntArray("lock");
	}
	
	@Override
	protected void writeToNBTExtra(NBTTagCompound nbt) {
		nbt.setIntArray("lock", toLock);
	}
	
	@Override
	public void performInternalOutputChange(InternalSignalOutput output) {
		if (output.component.is("lock")) {
			
			System.out.println("test");
		}
		System.out.println("test");
	}
	
	public static class LittleFixedActivatorStructureParserALET extends LittleStructureGuiParser {
		
		public List<Integer> possibleChildren;
		public List<Integer> selectedChildren;
		
		public LittleFixedActivatorStructureParserALET(GuiParent parent, AnimationGuiHandler handler) {
			super(parent, handler);
		}
		
		public String getDisplayName(LittlePreviews previews, int childId) {
			String name = previews.getStructureName();
			if (name == null)
				if (previews.hasStructure())
					name = previews.getStructureId();
				else
					name = "none";
			return name + " " + childId;
		}
		
		@SideOnly(Side.CLIENT)
		@CustomEventSubscribe
		public void buttonClicked(GuiControlClickEvent event) {
			
		}
		
		@Override
		protected void createControls(LittlePreviews previews, LittleStructure structure) {
			System.out.println(previews.getChildren());
			GuiScrollBox box = new GuiScrollBox("content", 0, 0, 100, 115);
			parent.controls.add(box);
			LittleLockALET lock = structure instanceof LittleLockALET ? (LittleLockALET) structure : null;
			possibleChildren = new ArrayList<>();
			int i = 0;
			int added = 0;
			for (LittlePreviews child : previews.getChildren()) {
				Class clazz = LittleStructureRegistry.getStructureClass(child.getStructureId());
				if (clazz != null && LittleDoor.class.isAssignableFrom(clazz)) {
					box.addControl(new GuiCheckBox("" + i, getDisplayName(child, i), 0, added * 20, lock != null && ArrayUtils.contains(lock.toLock, i)));
					possibleChildren.add(i);
					added++;
				}
				i++;
			}
			
		}
		
		@Override
		protected LittleStructure parseStructure(LittlePreviews previews) {
			LittleLockALET structure = createStructure(LittleLockALET.class, null);
			GuiScrollBox box = (GuiScrollBox) parent.get("content");
			List<Integer> toLock = new ArrayList<>();
			for (Integer integer : possibleChildren) {
				GuiCheckBox checkBox = (GuiCheckBox) box.get("" + integer);
				if (checkBox != null && checkBox.value)
					toLock.add(integer);
			}
			structure.toLock = new int[toLock.size()];
			for (int i = 0; i < structure.toLock.length; i++)
				structure.toLock[i] = toLock.get(i);
			return structure;
		}
		
		@Override
		protected LittleStructureType getStructureType() {
			return LittleStructureRegistry.getStructureType(LittleLockALET.class);
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
