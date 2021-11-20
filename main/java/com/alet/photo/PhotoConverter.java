package com.alet.photo;

import java.io.IOException;

import com.creativemd.creativecore.common.gui.container.SubGui;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class PhotoConverter implements Runnable {
	
	private String input;
	private boolean uploadOption;
	private int grid;
	private NBTTagCompound nbt;
	private SubGui gui;
	
	public PhotoConverter(String input, boolean uploadOption, int grid, SubGui subGui) {
		this.grid = grid;
		this.input = input;
		this.uploadOption = uploadOption;
		this.nbt = new NBTTagCompound();
		this.gui = subGui;
	}
	
	@Override
	public void run() {
		try {
			ItemStack stack = PhotoReader.photoToStack(input, uploadOption, grid, gui);
			if (!stack.equals(ItemStack.EMPTY)) {
				nbt = stack.getTagCompound();
				gui.sendPacketToServer(nbt);
			}
		} catch (IOException e) {
		}
	}
	
}
