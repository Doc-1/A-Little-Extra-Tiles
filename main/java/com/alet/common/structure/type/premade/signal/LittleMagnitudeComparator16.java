package com.alet.common.structure.type.premade.signal;

import javax.annotation.Nullable;

import com.creativemd.creativecore.common.utils.math.BooleanUtils;
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

public class LittleMagnitudeComparator16 extends LittleStructurePremade {
	
	private String output = "1111";
	private String logic = "equals";
	
	public LittleMagnitudeComparator16(LittleStructureType type, IStructureTileList mainBlock) {
		super(type, mainBlock);
	}
	
	@Override
	public void tick() {
		if (!isClient()) {
			
			try {
				
				LittleSignalOutput out = (LittleSignalOutput) this.children.get(0).getStructure();
				LittleSignalInput k1 = (LittleSignalInput) this.children.get(1).getStructure();
				LittleSignalInput k2 = (LittleSignalInput) this.children.get(2).getStructure();
				int rBits = 0;
				int bBits = 0;
				rBits = BooleanUtils.toNumber(k1.getState());
				bBits = BooleanUtils.toNumber(k2.getState());
				
				boolean result = false;
				
				switch (logic) {
				case "less than":
					if (bBits < rBits)
						result = true;
					break;
				case "greater than":
					if (bBits > rBits)
						result = true;
					break;
				case "less than or equal to":
					if (bBits <= rBits)
						result = true;
					break;
				case "greater than or equal to":
					if (bBits >= rBits)
						result = true;
					break;
				case "equals":
					if (bBits == rBits)
						result = true;
					break;
				default:
					break;
				}
				if (result)
					out.updateState(BooleanUtils.toBits(0, k1.getBandwidth()));
				/*
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
				}*/
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
