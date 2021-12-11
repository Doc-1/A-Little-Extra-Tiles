package com.alet.common.structure.type;

import java.util.HashMap;

import com.creativemd.creativecore.common.gui.GuiRenderHelper;
import com.creativemd.creativecore.common.gui.client.style.Style;
import com.creativemd.creativecore.common.gui.container.GuiParent;
import com.creativemd.creativecore.common.gui.controls.gui.GuiButton;
import com.creativemd.creativecore.common.gui.controls.gui.GuiLabel;
import com.creativemd.creativecore.common.gui.controls.gui.GuiScrollBox;
import com.creativemd.creativecore.common.gui.controls.gui.custom.GuiStackSelectorAll;
import com.creativemd.creativecore.common.utils.mc.BlockUtils;
import com.creativemd.littletiles.client.gui.LittleSubGuiUtils.LittleBlockSelector;
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

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class LittleStateActivatorALET extends LittleStructure {
	
	public int attributeMod = 0;
	public HashMap<Integer, IBlockState[]> replaceMaterial = new HashMap<Integer, IBlockState[]>();
	public boolean activated = false;
	
	public LittleStateActivatorALET(LittleStructureType type, IStructureTileList mainBlock) {
		super(type, mainBlock);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void loadFromNBTExtra(NBTTagCompound nbt) {
	}
	
	@Override
	protected void writeToNBTExtra(NBTTagCompound nbt) {
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
				System.out.println(tile.getBlockState());
				for (IBlockState[] b : replaceMaterial.values()) {
					
				}
				if (activated)
					tile.setBlock(Blocks.COBBLESTONE);
				else
					tile.setBlock(Blocks.GRASS);
				tile.updateBlockState();
			}
			tileList.getTe().updateBlock();
			tileList.getTe().updateNeighbour();
		}
		
		this.updateStructure();
	}
	
	public void changeCollisionState() throws CorruptedConnectionException, NotYetConnectedException {
		NBTTagCompound nbt = new NBTTagCompound();
		
		if (activated)
			attributeMod = LittleStructureAttribute.NOCOLLISION;
		else
			attributeMod = LittleStructureAttribute.NONE;
		
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
		
		public HashMap<Integer, IBlockState[]> replaceMaterial = new HashMap<Integer, IBlockState[]>();
		
		public LittleStateActivatorParserALET(GuiParent parent, AnimationGuiHandler handler) {
			super(parent, handler);
		}
		
		@Override
		protected void createControls(LittlePreviews previews, LittleStructure structure) {
			GuiScrollBox box = new GuiScrollBox("box", 0, 0, 194, 100);
			GuiButton add = new GuiButton("new", 170, 110, 20) {
				int y = 0;
				
				@Override
				public void onClicked(int x, int y, int button) {
					box.controls.add(new GuiStackSelectorAll("a" + this.y, 0, (this.y * 21), 50, null, new GuiStackSelectorAll.CreativeCollector(new LittleBlockSelector()), true) {
						
					});
					box.controls.add(new GuiStackSelectorAll("b" + this.y, 114, (this.y * 21), 50, null, new GuiStackSelectorAll.CreativeCollector(new LittleBlockSelector()), true) {
						private String oldCaption = "";
						
						@Override
						protected void renderContent(GuiRenderHelper helper, Style style, int width, int height) {
							super.renderContent(helper, style, width, height);
							if (oldCaption != this.caption) {
								int index = Integer.parseInt(this.name.charAt(1) + "");
								IBlockState keepBlock = replaceMaterial.get(index)[0];
								replaceMaterial.put(index, new IBlockState[] { keepBlock, BlockUtils.getState(this.getSelected()) });
								
								oldCaption = this.caption;
								System.out.println();
								System.out.println(Block.getStateById(Block.getStateId(BlockUtils.getState(this.getSelected()))));
							}
						}
					});
					
					box.controls.add(new GuiLabel("To", 87, (this.y * 21) + 2));
					box.refreshControls();
					GuiStackSelectorAll a = (GuiStackSelectorAll) box.get("a" + this.y);
					GuiStackSelectorAll b = (GuiStackSelectorAll) box.get("b" + this.y);
					replaceMaterial.put(this.y, new IBlockState[] { BlockUtils.getState(a.getSelected()), BlockUtils.getState(b.getSelected()) });
					
					this.y++;
				}
				
			};
			
			parent.controls.add(add);
			parent.controls.add(box);
			
			/*
			GuiScrollBox box = new GuiScrollBox("content", 0, 0, 100, 115);
			parent.controls.add(box);
			LittleStateActivatorALET activator = structure instanceof LittleStateActivatorALET ? (LittleStateActivatorALET) structure : null;
			possibleChildren = new ArrayList<>();
			int i = 0;
			int added = 0;
			for (LittlePreviews child : previews.getChildren()) {
				
				Class clazz = LittleStructureRegistry.getStructureClass(child.getStructureId());
				System.out.println(child.getStructureId());
				if ((clazz != null && LittleFixedStructure.class.isAssignableFrom(clazz)) || child.getStructureId().equals("adjustable")) {
					box.addControl(new GuiCheckBox("" + i, getDisplayName(child, i), 0, added * 20, activator != null && ArrayUtils.contains(activator.toActivate, i)));
					possibleChildren.add(i);
					added++;
				}
				i++;
			}
			*/
		}
		
		@Override
		protected LittleStructure parseStructure(LittlePreviews previews) {
			LittleStateActivatorALET structure = createStructure(LittleStateActivatorALET.class, null);
			/*
			GuiScrollBox box = (GuiScrollBox) parent.get("content");
			List<Integer> toLock = new ArrayList<>();
			List<LittlePreviews> replace = new ArrayList<LittlePreviews>();
			
			for (int i = 0; i < previews.size(); i++) {
				LittlePreviews child = previews.getChild(i);
				Class clazz = LittleStructureRegistry.getStructureClass(child.getStructureId());
				if (clazz != null && LittleFixedStructure.class.isAssignableFrom(clazz)) {
					NBTTagCompound nbt = child.structureNBT.copy();
					nbt.setString("id", "adjustable");
					LittlePreviews ch = new LittlePreviews(nbt, child);
					previews.updateChild(i, ch);
				}
			}
			
			for (Integer integer : possibleChildren) {
				GuiCheckBox checkBox = (GuiCheckBox) box.get("" + integer);
				if (checkBox != null && checkBox.value)
					toLock.add(integer);
			}
			structure.toActivate = new int[toLock.size()];
			for (int i = 0; i < structure.toActivate.length; i++)
				structure.toActivate[i] = toLock.get(i);
			*/
			return structure;
		}
		
		/*
		public String getDisplayName(LittlePreviews previews, int childId) {
			String name = previews.getStructureName();
			if (name == null)
				if (previews.hasStructure())
					name = previews.getStructureId();
				else
					name = "none";
			return name + " " + childId;
		}*/
		@Override
		protected LittleStructureType getStructureType() {
			return LittleStructureRegistry.getStructureType(LittleStateActivatorALET.class);
		}
		
	}
}
