package com.alet.common.structure.type.premade.signal;

import javax.annotation.Nullable;

import com.creativemd.littletiles.client.gui.handler.LittleStructureGuiHandler;
import com.creativemd.littletiles.common.action.LittleActionException;
import com.creativemd.littletiles.common.action.block.LittleActionActivated;
import com.creativemd.littletiles.common.structure.exception.CorruptedConnectionException;
import com.creativemd.littletiles.common.structure.exception.NotYetConnectedException;
import com.creativemd.littletiles.common.structure.registry.LittleStructureType;
import com.creativemd.littletiles.common.structure.type.premade.LittleStructurePremade;
import com.creativemd.littletiles.common.structure.type.premade.signal.LittleSignalInput;
import com.creativemd.littletiles.common.structure.type.premade.signal.LittleSignalOutput;
import com.creativemd.littletiles.common.tile.LittleTile;
import com.creativemd.littletiles.common.tile.parent.IStructureTileList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class LittleMagnitudeComparator extends LittleStructurePremade {
	
	private String output = "1111";
	private String logic = "==";
	
	public LittleMagnitudeComparator(LittleStructureType type, IStructureTileList mainBlock) {
		super(type, mainBlock);
	}
	
	@Override
	public void tick() {
		if (!isClient()) {
			
			try {
				
				LittleSignalOutput out = (LittleSignalOutput) this.children.get(0).getStructure();
				LittleSignalInput red = (LittleSignalInput) this.children.get(1).getStructure();
				LittleSignalInput blue = (LittleSignalInput) this.children.get(2).getStructure();
				int rBits = 0;
				for (int i = 0; i < red.getState().length; i++)
					if (red.getState()[i])
						rBits |= 1 << i;
				int bBits = 0;
				for (int i = 0; i < blue.getState().length; i++)
					if (blue.getState()[i])
						bBits |= 1 << i;
					
				boolean result = false;
				
				switch (logic) {
				case "<":
					if (bBits < rBits)
						result = true;
					break;
				case ">":
					if (bBits > rBits)
						result = true;
					break;
				case "<=":
					if (bBits <= rBits)
						result = true;
					break;
				case ">=":
					if (bBits >= rBits)
						result = true;
					break;
				case "==":
					if (bBits == rBits)
						result = true;
					break;
				default:
					break;
				}
				
				if (result) {
					boolean[] state = { false, false, false, false };
					for (int i = 0; i < output.toCharArray().length; i++) {
						if (output.toCharArray()[i] == '1')
							state[i] = true;
						else
							state[i] = false;
					}
					out.updateState(state);
				} else {
					boolean[] state = { false, false, false, false };
					out.updateState(state);
				}
			} catch (CorruptedConnectionException | NotYetConnectedException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	@Override
	protected void loadFromNBTExtra(NBTTagCompound nbt) {
		if (nbt.hasKey("logic"))
			logic = nbt.getString("logic");
		if (nbt.hasKey("output"))
			output = nbt.getString("output");
		
	}
	
	@Override
	protected void writeToNBTExtra(NBTTagCompound nbt) {
		nbt.setString("logic", logic);
		nbt.setString("output", output);
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, LittleTile tile, BlockPos pos, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ, LittleActionActivated action) throws LittleActionException {
		if (!worldIn.isRemote)
			LittleStructureGuiHandler.openGui("magnitude-comparator", new NBTTagCompound(), playerIn, this);
		return true;
	}
}
