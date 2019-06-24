package com.ltphoto.gui;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.io.IOException;

import com.creativemd.creativecore.common.gui.container.SubGui;
import com.creativemd.creativecore.common.gui.controls.gui.GuiButton;
import com.creativemd.creativecore.common.gui.controls.gui.GuiTextfield;
import com.creativemd.creativecore.common.gui.event.container.SlotChangeEvent;
import com.creativemd.littletiles.LittleTiles;
import com.ltphoto.container.SubContainerPhotoImport;
import com.ltphoto.photo.PhotoReader;
import com.n247s.api.eventapi.eventsystem.CustomEventSubscribe;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;


public class SubGuiPhotoImport extends SubGui {
	
	public GuiTextfield textfield;
	
	@Override
	public void createControls() {
		textfield = new GuiTextfield("file", "", 10, 30, 150, 14);
		controls.add(textfield);
		controls.add(new GuiButton("Paste", 10, 52) {
			
			@Override
			public void onClicked(int x, int y, int button) {
				StringSelection stringSelection = new StringSelection(textfield.text);
				Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
				Transferable t = clpbrd.getContents(this);
				if (t == null)
					return;
				try {
					textfield.text = (String) t.getTransferData(DataFlavor.stringFlavor);
				} catch (Exception e) {
					
				}
			}
		});
		
		controls.add(new GuiButton("Scan", 128, 52) {
			
			@Override
			public void onClicked(int x, int y, int button) {
				try {
					NBTTagCompound nbt = PhotoReader.printPhoto(textfield.text);
					sendPacketToServer(JsonToNBT.getTagFromJson(nbt.toString()));
				} catch (IOException e) {
					e.printStackTrace();
				}
				catch (NBTException e) {
					e.printStackTrace();
				}
				
			}
		});
	}
	
	@CustomEventSubscribe
	public void onSlotChange(SlotChangeEvent event) {
		
	}
	
}
