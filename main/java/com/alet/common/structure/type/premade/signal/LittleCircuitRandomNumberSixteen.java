package com.alet.common.structure.type.premade.signal;

import com.creativemd.littletiles.common.structure.exception.CorruptedConnectionException;
import com.creativemd.littletiles.common.structure.exception.NotYetConnectedException;
import com.creativemd.littletiles.common.structure.registry.LittleStructureType;
import com.creativemd.littletiles.common.structure.type.premade.LittleStructurePremade;
import com.creativemd.littletiles.common.structure.type.premade.signal.LittleSignalOutput;
import com.creativemd.littletiles.common.tile.parent.IStructureTileList;

import net.minecraft.nbt.NBTTagCompound;

public class LittleCircuitRandomNumberSixteen extends LittleStructurePremade {
	
	boolean pulse = false;
	
	public LittleCircuitRandomNumberSixteen(LittleStructureType type, IStructureTileList mainBlock) {
		super(type, mainBlock);
		
	}
	
	@Override
	public void tick() {
		
		if (!isClient())
			try {
				LittleSignalOutput out = (LittleSignalOutput) this.children.get(0).getStructure();
				pulse = (!pulse);
				boolean[] state = { pulse };
				out.updateState(state);
			} catch (CorruptedConnectionException | NotYetConnectedException e) {
				e.printStackTrace();
			}
	}
	
	@Override
	protected void loadFromNBTExtra(NBTTagCompound nbt) {
		if (nbt.hasKey("pulse"))
			pulse = nbt.getBoolean("pulse");
	}
	
	@Override
	protected void writeToNBTExtra(NBTTagCompound nbt) {
		nbt.setBoolean("pulse", pulse);
	}
	
}
