package com.alet.common.structure.type.premade.signal;

import com.creativemd.littletiles.common.structure.exception.CorruptedConnectionException;
import com.creativemd.littletiles.common.structure.exception.NotYetConnectedException;
import com.creativemd.littletiles.common.structure.registry.LittleStructureType;
import com.creativemd.littletiles.common.structure.type.premade.LittleStructurePremade;
import com.creativemd.littletiles.common.structure.type.premade.signal.LittleSignalInput;
import com.creativemd.littletiles.common.structure.type.premade.signal.LittleSignalOutput;
import com.creativemd.littletiles.common.tile.parent.IStructureTileList;

import net.minecraft.nbt.NBTTagCompound;

public class LittleCircuitRandomNumberOne extends LittleStructurePremade {
	
	boolean update = false;
	
	public LittleCircuitRandomNumberOne(LittleStructureType type, IStructureTileList mainBlock) {
		super(type, mainBlock);
		
	}
	
	@Override
	public void tick() {
		
		if (!isClient())
			try {
				boolean[] state = { false };
				LittleSignalOutput out = (LittleSignalOutput) this.children.get(0).getStructure();
				LittleSignalInput in = (LittleSignalInput) this.children.get(1).getStructure();
				
				if (in.getState()[0]) {
					int value = (int) Math.random();
					if (value == 1)
						state[0] = true;
					out.updateState(state);
					update = true;
				} else
					out.updateState(state);
			} catch (CorruptedConnectionException | NotYetConnectedException e) {
				e.printStackTrace();
			}
	}
	
	@Override
	protected void loadFromNBTExtra(NBTTagCompound nbt) {
		if (nbt.hasKey("pulse"))
			update = nbt.getBoolean("pulse");
	}
	
	@Override
	protected void writeToNBTExtra(NBTTagCompound nbt) {
		nbt.setBoolean("pulse", update);
	}
	
}
