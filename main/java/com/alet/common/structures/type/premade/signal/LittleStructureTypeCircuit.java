package com.alet.common.structures.type.premade.signal;

import java.util.ArrayList;
import java.util.List;

import com.alet.ALET;
import com.creativemd.littletiles.common.structure.LittleStructure;
import com.creativemd.littletiles.common.structure.type.premade.LittleStructurePremade.LittleStructureTypePremade;

import net.minecraft.creativetab.CreativeTabs;

public class LittleStructureTypeCircuit extends LittleStructureTypePremade {
	
	public List<String> toolTip = new ArrayList<String>();
	
	public LittleStructureTypeCircuit(String id, String category, Class<? extends LittleStructure> structureClass, int attribute, String modid) {
		super(id, category, structureClass, attribute, modid);
	}
	
	public LittleStructureTypeCircuit addToolTip(List<String> toolTip) {
		this.toolTip = toolTip;
		return this;
	}
	
	@Override
	public CreativeTabs getCustomTab() {
		return ALET.littleCircuitTab;
	}
	
}
