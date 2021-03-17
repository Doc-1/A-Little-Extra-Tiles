package com.alet.littletiles.gui;

import com.alet.littletiles.items.ItemLittleGrabberAlet;
import com.creativemd.creativecore.common.gui.opener.GuiHandler;
import com.creativemd.littletiles.client.gui.SubGuiGrabber;
import com.creativemd.littletiles.common.item.ItemLittleGrabber.GrabberMode;
import com.creativemd.littletiles.common.util.grid.LittleGridContext;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public abstract class SubGuiGrabberAlet extends SubGuiGrabber {
	
	public SubGuiGrabberAlet(GrabberMode mode, ItemStack stack, int width, int height, LittleGridContext context) {
		super(mode, stack, width, height, context);
	}
	
	public void openNewGui(GrabberMode mode) {
		ItemLittleGrabberAlet.setMode(stack, mode);
		GuiHandler.openGui("grabberAlet", new NBTTagCompound(), getPlayer());
	}
	
}
