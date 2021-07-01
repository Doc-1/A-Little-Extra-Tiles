package com.alet.common.structure.type.premade.signal;

import com.creativemd.littletiles.common.structure.exception.CorruptedConnectionException;
import com.creativemd.littletiles.common.structure.exception.NotYetConnectedException;
import com.creativemd.littletiles.common.structure.registry.LittleStructureType;
import com.creativemd.littletiles.common.structure.type.premade.LittleStructurePremade;
import com.creativemd.littletiles.common.structure.type.premade.signal.LittleSignalInput;
import com.creativemd.littletiles.common.structure.type.premade.signal.LittleSignalOutput;
import com.creativemd.littletiles.common.tile.parent.IStructureTileList;

import net.minecraft.nbt.NBTTagCompound;

public class LittleCircuitConverterFourToSixteen extends LittleStructurePremade {
	
	boolean pulse = false;
	
	public LittleCircuitConverterFourToSixteen(LittleStructureType type, IStructureTileList mainBlock) {
		super(type, mainBlock);
		
	}
	
	@Override
	public void tick() {
		
		if (!isClient())
			try {
				LittleSignalInput in1 = (LittleSignalInput) this.children.get(0).getStructure();
				LittleSignalInput in2 = (LittleSignalInput) this.children.get(1).getStructure();
				LittleSignalInput in3 = (LittleSignalInput) this.children.get(2).getStructure();
				LittleSignalInput in4 = (LittleSignalInput) this.children.get(3).getStructure();
				
				LittleSignalOutput out = (LittleSignalOutput) this.children.get(4).getStructure();
				
				boolean[] state = { in1.getState()[0], in1.getState()[1], in1.getState()[2], in1.getState()[3], in2.getState()[0], in2.getState()[1], in2.getState()[2], in2.getState()[3], in3.getState()[0], in3.getState()[1], in3.getState()[2], in3.getState()[3], in4.getState()[0], in4.getState()[1], in4.getState()[2], in4.getState()[3] };
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
