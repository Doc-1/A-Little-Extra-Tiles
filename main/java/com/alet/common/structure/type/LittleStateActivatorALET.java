package com.alet.common.structure.type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.alet.client.gui.mutator.controls.GuiButtonAddMutationType;
import com.creativemd.creativecore.common.gui.GuiControl;
import com.creativemd.creativecore.common.gui.container.GuiParent;
import com.creativemd.creativecore.common.gui.controls.gui.GuiScrollBox;
import com.creativemd.creativecore.common.gui.controls.gui.custom.GuiStackSelectorAll;
import com.creativemd.creativecore.common.utils.mc.BlockUtils;
import com.creativemd.littletiles.common.action.LittleActionException;
import com.creativemd.littletiles.common.action.block.LittleActionActivated;
import com.creativemd.littletiles.common.structure.LittleStructure;
import com.creativemd.littletiles.common.structure.animation.AnimationGuiHandler;
import com.creativemd.littletiles.common.structure.attribute.LittleStructureAttribute;
import com.creativemd.littletiles.common.structure.exception.CorruptedConnectionException;
import com.creativemd.littletiles.common.structure.exception.NotYetConnectedException;
import com.creativemd.littletiles.common.structure.registry.LittleStructureGuiParser;
import com.creativemd.littletiles.common.structure.registry.LittleStructureRegistry;
import com.creativemd.littletiles.common.structure.registry.LittleStructureType;
import com.creativemd.littletiles.common.structure.signal.output.InternalSignalOutput;
import com.creativemd.littletiles.common.tile.LittleTile;
import com.creativemd.littletiles.common.tile.parent.IStructureTileList;
import com.creativemd.littletiles.common.tile.preview.LittlePreviews;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

public class LittleStateActivatorALET extends LittleStructure {
	
	public int attributeMod = 0;
	public HashMap<String, IBlockState> mutateMaterial = new HashMap<String, IBlockState>();
	public boolean activated = false;
	public List<IBlockState> ignoreMaterial = new ArrayList<IBlockState>();
	
	public LittleStateActivatorALET(LittleStructureType type, IStructureTileList mainBlock) {
		super(type, mainBlock);
	}
	
	@Override
	protected void loadFromNBTExtra(NBTTagCompound nbt) {
		attributeMod = nbt.getInteger("attributeMod");
		if (nbt.hasKey("mutater")) {
			NBTTagList mutateList = nbt.getTagList("mutater", Constants.NBT.TAG_LIST);
			int counter = 0;
			for (NBTBase mutater : mutateList)
				if (mutater instanceof NBTTagList) {
					NBTTagList mutate = (NBTTagList) mutater;
					mutateMaterial.put("a" + counter, NBTUtil.readBlockState((NBTTagCompound) mutate.get(0)));
					mutateMaterial.put("b" + counter, NBTUtil.readBlockState((NBTTagCompound) mutate.get(1)));
					counter++;
				}
		}
	}
	
	@Override
	protected void writeToNBTExtra(NBTTagCompound nbt) {
		nbt.setInteger("attributeMod", attributeMod);
		if (!mutateMaterial.isEmpty()) {
			NBTTagList mutate = new NBTTagList();
			
			for (int i = 0; i < mutateMaterial.size() / 2; i++) {
				NBTTagList mutateList = new NBTTagList();
				mutateList.appendTag(NBTUtil.writeBlockState(new NBTTagCompound(), mutateMaterial.get("a" + i)));
				mutateList.appendTag(NBTUtil.writeBlockState(new NBTTagCompound(), mutateMaterial.get("b" + i)));
				mutate.appendTag(mutateList);
			}
			nbt.setTag("mutater", mutate);
		}
	}
	
	@Override
	public void performInternalOutputChange(InternalSignalOutput output) {
		if (output.component.is("activate")) {
			//changeActiveState();
			try {
				activated = !activated;
				//changeCollisionState();
				changeMaterialState();
			} catch (CorruptedConnectionException | NotYetConnectedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void changeVisibleState() throws CorruptedConnectionException, NotYetConnectedException {
		
		for (IStructureTileList tileList : this.blocksList()) {
			for (LittleTile tile : tileList)
				tile.invisible = !tile.invisible;
			
			tileList.getTe().updateBlock();
			tileList.getTe().updateNeighbour();
		}
		
		this.updateStructure();
	}
	
	public void changeMaterialState() throws CorruptedConnectionException, NotYetConnectedException {
		for (IStructureTileList tileList : this.blocksList()) {
			for (LittleTile tile : tileList) {
				
				for (int i = 0; i < mutateMaterial.size() / 2; i++) {
					IBlockState stateA = mutateMaterial.get("a" + i);
					IBlockState stateB = mutateMaterial.get("b" + i);
					if (tile.getBlockState().equals(stateA)) {
						tile.setBlock(stateB.getBlock(), stateB.getBlock().getMetaFromState(stateB));
						break;
					}
					
				}
				tile.updateBlockState();
			}
			tileList.getTe().updateBlock();
			tileList.getTe().updateNeighbour();
		}
		
		this.updateStructure();
	}
	
	public void changeCollisionState() throws CorruptedConnectionException, NotYetConnectedException {
		NBTTagCompound nbt = new NBTTagCompound();
		
		if (attributeMod == LittleStructureAttribute.NOCOLLISION)
			attributeMod = LittleStructureAttribute.NONE;
		else if (attributeMod == LittleStructureAttribute.NONE)
			attributeMod = LittleStructureAttribute.NOCOLLISION;
		
		this.tryAttributeChangeForBlocks();
		
		World world = getWorld();
		for (IStructureTileList list : this.blocksList()) {
			IBlockState state = world.getBlockState(list.getPos());
			world.notifyBlockUpdate(list.getPos(), state, state, 2);
		}
		
		this.mainBlock.getTe().updateBlock();
		this.mainBlock.getTe().updateNeighbour();
		this.updateStructure();
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, LittleTile tile, BlockPos pos, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ, LittleActionActivated action) throws LittleActionException {
		if (!worldIn.isRemote)
			getOutput(0).toggle();
		return true;
	}
	
	@Override
	public int getAttribute() {
		return super.getAttribute() | attributeMod;
	}
	
	public static class LittleStateActivatorParserALET extends LittleStructureGuiParser {
		
		public LittleStateActivatorParserALET(GuiParent parent, AnimationGuiHandler handler) {
			super(parent, handler);
		}
		
		@Override
		protected void createControls(LittlePreviews previews, LittleStructure structure) {
			LittleStateActivatorALET mutator = (LittleStateActivatorALET) structure;
			
			GuiScrollBox box = new GuiScrollBox("box", 0, 0, 294, 100);
			GuiButtonAddMutationType add = new GuiButtonAddMutationType("new", 224, 110, 20, box);
			if (mutator != null && !mutator.mutateMaterial.isEmpty()) {
				for (int i = 0; i < mutator.mutateMaterial.size() / 2; i++) {
					IBlockState a = mutator.mutateMaterial.get("a" + i);
					IBlockState b = mutator.mutateMaterial.get("b" + i);
					add.addMaterialMutation(a, b);
				}
			}
			
			parent.controls.add(add);
			parent.controls.add(box);
		}
		
		@Override
		protected LittleStructure parseStructure(LittlePreviews previews) {
			LittleStateActivatorALET structure = createStructure(LittleStateActivatorALET.class, null);
			HashMap<String, IBlockState> mutateMaterial = new HashMap<String, IBlockState>();
			
			for (GuiControl cont : ((GuiScrollBox) parent.get("box")).controls)
				if (cont instanceof GuiStackSelectorAll) {
					GuiStackSelectorAll control = (GuiStackSelectorAll) cont;
					mutateMaterial.put(control.name, BlockUtils.getState(control.getSelected()));
				}
			
			structure.mutateMaterial = mutateMaterial;
			return structure;
		}
		
		@Override
		protected LittleStructureType getStructureType() {
			return LittleStructureRegistry.getStructureType(LittleStateActivatorALET.class);
		}
		
	}
	
}
