package com.alet.client.gui;

import org.lwjgl.util.Color;

import com.alet.littletiles.gui.controls.GuiColorPickerAlet;
import com.creativemd.creativecore.common.gui.controls.gui.GuiAnalogeSlider;
import com.creativemd.creativecore.common.gui.controls.gui.GuiLabel;
import com.creativemd.creativecore.common.utils.mc.ColorUtils;
import com.creativemd.littletiles.client.gui.configure.SubGuiConfigure;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class SubGuiLittleRope extends SubGuiConfigure {
	
	public SubGuiLittleRope(ItemStack stack) {
		super(200, 200, stack);
	}
	
	@Override
	public void createControls() {
		NBTTagCompound nbt = new NBTTagCompound();
		if (this.stack.hasTagCompound())
			nbt = this.stack.getTagCompound();
		
		int color = nbt.hasKey("color") ? nbt.getInteger("color") : ColorUtils.BLACK;
		double thickness = nbt.hasKey("thickness") ? nbt.getDouble("thickness") : 0.3F;
		double tautness = nbt.hasKey("tautness") ? nbt.getDouble("tautness") : 0.5F;
		
		controls.add(new GuiLabel("Rope's Thickness:", 0, 0));
		controls.add(new GuiLabel("Rope's Tautness:", 0, 20));
		
		controls.add(new GuiAnalogeSlider("thickness", 94, 0, 100, 10, thickness, 0, 0.5));
		controls.add(new GuiAnalogeSlider("tautness", 94, 20, 100, 10, tautness, 0, 10));
		controls.add(new GuiColorPickerAlet("color", 0, 40, ColorUtils.IntToRGBA(color), true, 0));
	}
	
	@Override
	public void saveConfiguration() {
		NBTTagCompound nbt = stack.getTagCompound();
		double thickness = ((GuiAnalogeSlider) this.get("thickness")).value;
		double tautness = ((GuiAnalogeSlider) this.get("tautness")).value;
		Color color = ((GuiColorPickerAlet) this.get("color")).color;
		
		nbt.setDouble("thickness", thickness);
		nbt.setDouble("tautness", tautness);
		nbt.setInteger("color", ColorUtils.RGBAToInt(color));
	}
	
}
