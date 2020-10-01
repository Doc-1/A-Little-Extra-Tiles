package com.alet.littletiles.gui;

import com.alet.littletiles.items.ItemLittleGrabberAlet;
import com.alet.littletiles.items.ItemLittleGrabberAlet.GrabberMode;
import com.creativemd.creativecore.common.gui.controls.gui.GuiButton;
import com.creativemd.creativecore.common.gui.controls.gui.GuiLabel;
import com.creativemd.creativecore.common.gui.opener.GuiHandler;
import com.creativemd.littletiles.client.gui.configure.SubGuiConfigure;
import com.creativemd.littletiles.common.util.grid.LittleGridContext;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public abstract class SubGuiGrabberAlet extends SubGuiConfigure {
	
	public final GrabberMode mode;
	public final int index;
	public final GrabberMode[] modes;
	public LittleGridContext context;
	
	public SubGuiGrabberAlet(GrabberMode mode, ItemStack stack, int width, int height, LittleGridContext context) {
		super(width, height, stack);
		this.mode = mode;
		this.modes = ItemLittleGrabberAlet.getModes();
		this.index = ItemLittleGrabberAlet.indexOf(mode);
		this.context = context;
	}
	
	@Override
	public void onClosed() {
		super.onClosed();
		ItemLittleGrabberAlet.setMode(stack, mode);
		saveConfiguration();
		sendPacketToServer(stack.getTagCompound());
	}
	
	public void openNewGui(GrabberMode mode) {
		ItemLittleGrabberAlet.setMode(stack, mode);
		GuiHandler.openGui("grabberAlet", new NBTTagCompound(), getPlayer());
	}
	
	@Override
	public void createControls() {
		controls.add(new GuiButton("<<", 0, 0, 10) {
			
			@Override
			public void onClicked(int x, int y, int button) {
				int newIndex = index - 1;
				if (newIndex < 0)
					newIndex = modes.length - 1;
				openNewGui(modes[newIndex]);
			}
			
		});
		
		controls.add(new GuiButton(">>", 124, 0, 10) {
			
			@Override
			public void onClicked(int x, int y, int button) {
				int newIndex = index + 1;
				if (newIndex >= modes.length)
					newIndex = 0;
				openNewGui(modes[newIndex]);
			}
			
		});
		
		controls.add(new GuiLabel(mode.getLocalizedName(), 20, 3));
	}
	
}
