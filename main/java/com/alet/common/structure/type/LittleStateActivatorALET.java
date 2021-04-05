package com.alet.common.structure.type;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import com.creativemd.creativecore.common.gui.container.GuiParent;
import com.creativemd.creativecore.common.gui.controls.gui.GuiCheckBox;
import com.creativemd.creativecore.common.gui.controls.gui.GuiScrollBox;
import com.creativemd.creativecore.common.utils.type.Pair;
import com.creativemd.littletiles.common.action.LittleActionException;
import com.creativemd.littletiles.common.action.block.LittleActionActivated;
import com.creativemd.littletiles.common.structure.LittleStructure;
import com.creativemd.littletiles.common.structure.animation.AnimationGuiHandler;
import com.creativemd.littletiles.common.structure.connection.StructureChildConnection;
import com.creativemd.littletiles.common.structure.exception.CorruptedConnectionException;
import com.creativemd.littletiles.common.structure.exception.NotYetConnectedException;
import com.creativemd.littletiles.common.structure.registry.LittleStructureGuiParser;
import com.creativemd.littletiles.common.structure.registry.LittleStructureRegistry;
import com.creativemd.littletiles.common.structure.registry.LittleStructureType;
import com.creativemd.littletiles.common.structure.type.LittleFixedStructure;
import com.creativemd.littletiles.common.tile.LittleTile;
import com.creativemd.littletiles.common.tile.parent.IStructureTileList;
import com.creativemd.littletiles.common.tile.preview.LittlePreviews;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class LittleStateActivatorALET extends LittleStructure {
	
	public int[] toActivate;
	
	public LittleStateActivatorALET(LittleStructureType type, IStructureTileList mainBlock) {
		super(type, mainBlock);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void loadFromNBTExtra(NBTTagCompound nbt) {
		toActivate = nbt.getIntArray("toActivate");
	}
	
	@Override
	protected void writeToNBTExtra(NBTTagCompound nbt) {
		nbt.setIntArray("toActivate", toActivate);
	}
	
	public void changeActiveState() {
		try {
			for (int c : toActivate) {
				NBTTagCompound nbt = new NBTTagCompound();
				StructureChildConnection child = this.getChild(c);
				
				for (Pair<IStructureTileList, LittleTile> tiles : child.getStructure().tiles()) {
					if (tiles.value.invisible) {
						tiles.value.invisible = false;
					} else {
						tiles.value.invisible = true;
					}
				}
				child.getStructure().mainBlock.getTe().updateBlock();
				child.getStructure().mainBlock.getTe().updateNeighbour();
				child.getStructure().updateStructure();
				
				updateStructure();
			}
		} catch (CorruptedConnectionException | NotYetConnectedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, LittleTile tile, BlockPos pos, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ, LittleActionActivated action) throws LittleActionException {
		if (!worldIn.isRemote)
			getOutput(0).toggle();
		return true;
	}
	
	public static class LittleStateActivatorParserALET extends LittleStructureGuiParser {
		
		public List<Integer> possibleChildren;
		public List<Integer> selectedChildren;
		
		public LittleStateActivatorParserALET(GuiParent parent, AnimationGuiHandler handler) {
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
		
		@Override
		protected void createControls(LittlePreviews previews, LittleStructure structure) {
			System.out.println(previews.getChildren());
			GuiScrollBox box = new GuiScrollBox("content", 0, 0, 100, 115);
			parent.controls.add(box);
			LittleStateActivatorALET activator = structure instanceof LittleStateActivatorALET ? (LittleStateActivatorALET) structure : null;
			possibleChildren = new ArrayList<>();
			int i = 0;
			int added = 0;
			for (LittlePreviews child : previews.getChildren()) {
				Class clazz = LittleStructureRegistry.getStructureClass(child.getStructureId());
				if (clazz != null && LittleFixedStructure.class.isAssignableFrom(clazz)) {
					box.addControl(new GuiCheckBox("" + i, getDisplayName(child, i), 0, added * 20, activator != null && ArrayUtils.contains(activator.toActivate, i)));
					possibleChildren.add(i);
					added++;
				}
				i++;
			}
		}
		
		@Override
		protected LittleStructure parseStructure(LittlePreviews previews) {
			LittleStateActivatorALET structure = createStructure(LittleStateActivatorALET.class, null);
			GuiScrollBox box = (GuiScrollBox) parent.get("content");
			List<Integer> toLock = new ArrayList<>();
			for (Integer integer : possibleChildren) {
				GuiCheckBox checkBox = (GuiCheckBox) box.get("" + integer);
				if (checkBox != null && checkBox.value)
					toLock.add(integer);
			}
			structure.toActivate = new int[toLock.size()];
			for (int i = 0; i < structure.toActivate.length; i++)
				structure.toActivate[i] = toLock.get(i);
			return structure;
		}
		
		@Override
		protected LittleStructureType getStructureType() {
			return LittleStructureRegistry.getStructureType(LittleStateActivatorALET.class);
		}
		
	}
}
