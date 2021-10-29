package com.alet.client.gui.mutator.controls;

import com.alet.common.structure.type.LittleStateActivatorALET;
import com.alet.littletiles.gui.controls.GuiColorPickerAlet;
import com.creativemd.creativecore.common.gui.controls.gui.GuiButton;
import com.creativemd.creativecore.common.gui.controls.gui.GuiLabel;
import com.creativemd.creativecore.common.gui.controls.gui.GuiScrollBox;
import com.creativemd.creativecore.common.gui.controls.gui.custom.GuiStackSelectorAll;
import com.creativemd.creativecore.common.utils.mc.ColorUtils;
import com.creativemd.littletiles.client.gui.LittleSubGuiUtils.LittleBlockSelector;

import net.minecraft.item.ItemStack;

public class GuiButtonAddMutationType extends GuiButton {
	
	public int depth = 0;
	public GuiScrollBox box;
	
	public GuiButtonAddMutationType(String caption, int x, int y, int width, GuiScrollBox box) {
		super(caption, x, y, width);
		this.box = box;
	}
	
	@Override
	public void onClicked(int x, int y, int button) {
		addMaterialMutation();
	}
	
	public void addMaterialMutation() {
		
		box.controls.add(new GuiStackSelectorAll("a" + depth, 0, (depth), 80, null, new GuiStackSelectorAll.CreativeCollector(new LittleBlockSelector()), true));
		box.controls.add(new GuiStackSelectorAll("b" + depth, 134, (depth), 80, null, new GuiStackSelectorAll.CreativeCollector(new LittleBlockSelector()), true));
		
		box.controls.add(new GuiLabel("To", 111, (depth) + 2));
		box.refreshControls();
		
		depth += 21;
	}
	
	public void addColorMutation() {
		System.out.println(depth);
		box.controls.add(new GuiColorPickerAlet("a", 0, depth, ColorUtils.IntToRGBA(ColorUtils.WHITE), true, 0));
		System.out.println(depth);
		box.controls.add(new GuiColorPickerAlet("b", 157, depth, ColorUtils.IntToRGBA(ColorUtils.WHITE), true, 0));
		box.controls.add(new GuiLabel("To", 137, (depth) + 18));
		box.refreshControls();
		depth += 55;
	}
	
	public void addCollisionMutation() {
		
	}
	
	public void addMaterialColorMutation() {
		
	}
	
	public void addMaterialMutation(LittleStateActivatorALET mutator) {
		if (mutator != null && !mutator.mutateMaterial.isEmpty()) {
			for (int i = 0; i < mutator.mutateMaterial.size() / 2; i++) {
				GuiStackSelectorAll boxA = new GuiStackSelectorAll("a" + i, 0, (depth), 80, null, new GuiStackSelectorAll.CreativeCollector(new LittleBlockSelector()), true);
				GuiStackSelectorAll boxB = new GuiStackSelectorAll("b" + i, 134, (depth), 80, null, new GuiStackSelectorAll.CreativeCollector(new LittleBlockSelector()), true);
				
				ItemStack itemA = new ItemStack(mutator.mutateMaterial.get("a" + i).getBlock(), 1, mutator.mutateMaterial.get("a" + i).getBlock().getMetaFromState(mutator.mutateMaterial.get("a" + i)));
				ItemStack itemB = new ItemStack(mutator.mutateMaterial.get("b" + i).getBlock(), 1, mutator.mutateMaterial.get("b" + i).getBlock().getMetaFromState(mutator.mutateMaterial.get("b" + i)));
				
				boxA.setSelectedForce(itemA);
				boxB.setSelectedForce(itemB);
				box.controls.add(boxA);
				box.controls.add(boxB);
				box.controls.add(new GuiLabel("To", 111, (i * 21) + 2));
				
				box.refreshControls();
				depth += 21;
			}
		}
	}
}
