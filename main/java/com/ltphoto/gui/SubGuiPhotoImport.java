package com.ltphoto.gui;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.io.IOException;

import com.creativemd.creativecore.common.gui.GuiControl;
import com.creativemd.creativecore.common.gui.container.SubGui;
import com.creativemd.creativecore.common.gui.controls.gui.GuiButton;
import com.creativemd.creativecore.common.gui.controls.gui.GuiCheckBox;
import com.creativemd.creativecore.common.gui.controls.gui.GuiComboBox;
import com.creativemd.creativecore.common.gui.controls.gui.GuiTextBox;
import com.creativemd.creativecore.common.gui.controls.gui.GuiTextfield;
import com.creativemd.creativecore.common.gui.event.gui.GuiControlChangedEvent;
import com.creativemd.creativecore.common.gui.opener.GuiHandler;
import com.creativemd.littletiles.LittleTiles;
import com.creativemd.littletiles.client.gui.handler.LittleGuiHandler;
import com.creativemd.littletiles.common.item.ItemMultiTiles;
import com.creativemd.littletiles.common.tile.LittleTile;
import com.creativemd.littletiles.common.util.grid.LittleGridContext;
import com.creativemd.littletiles.common.util.selection.mode.SelectionMode.SelectionResult;
import com.ltphoto.config.Config;
import com.ltphoto.container.SubContainerPhotoImport;
import com.ltphoto.photo.PhotoReader;
import com.ltphoto.structure.premade.LittlePhotoImporter;
import com.n247s.api.eventapi.eventsystem.CustomEventSubscribe;

import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

public class SubGuiPhotoImport extends SubGui {
	
	public GuiTextfield textfield;
	public GuiCheckBox useFile = null;
	public GuiCheckBox useURL = null;
	public GuiCheckBox isRescale = null;
	
	@Override
	public void createControls() {
		textfield = new GuiTextfield("file", "", 10, 26, 150, 14);
		controls.add(textfield);
		
		GuiTextfield xScale = new GuiTextfield("xScale", "64", 115, 60, 20, 14);
		controls.add(xScale);
		
		GuiTextfield yScale = new GuiTextfield("yScale", "64", 145, 60, 20, 14);
		controls.add(yScale);

		isRescale = new GuiCheckBox("isRescale", translate("Resize Image?         X      Y"), 8, 43, false);
		controls.add(isRescale);
		
		GuiComboBox contextBox = new GuiComboBox("grid", 120, 0, 15, LittleGridContext.getNames());
		contextBox.select(ItemMultiTiles.currentContext.size + "");
		controls.add(contextBox);
		
		controls.add(useFile = new GuiCheckBox("useFile", translate("Use file path"), 40, -5, true) {
		
			@Override
			public boolean mousePressed(int posX, int posY, int button) {
				playSound(SoundEvents.UI_BUTTON_CLICK);
				useFile.value = true;
				useURL.value = false;
				raiseEvent(new GuiControlChangedEvent(this));
				return true;
			}
		});
		
		controls.add(useURL = new GuiCheckBox("useURL", translate("Use a URL"), 40, 10, false) {
			
			@Override
			public boolean mousePressed(int posX, int posY, int button) {
				playSound(SoundEvents.UI_BUTTON_CLICK);
				useFile.value = false;
				useURL.value = true;
				raiseEvent(new GuiControlChangedEvent(this));
				return true;
			}
		});
		if(!Config.isAllowURL()) {
			useURL.enabled = false;
		}
		
		controls.add(new GuiButton("Paste", 10, 60) {
			
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
		
		controls.add(new GuiButton("Print", 50, 60) {
			
			@Override
			public void onClicked(int x, int y, int button) {
				if(isRescale.value) {
					GuiTextfield yScale = (GuiTextfield) get("yScale");
					int resizeY = Integer.parseInt(yScale.text);
					GuiTextfield xScale = (GuiTextfield) get("xScale");
					int resizeX = Integer.parseInt(xScale.text);
					PhotoReader.setScale(resizeX, resizeY);
				}
				
				GuiComboBox contextBox = (GuiComboBox) get("grid");
				int grid = Integer.parseInt(contextBox.caption);
				try {
					NBTTagCompound nbt = PhotoReader.photoToNBT(textfield.text, useURL.value, grid);
					sendPacketToServer(nbt);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		
		controls.add(new GuiButton("-->", 145, 0) {
			
			@Override
			public void onClicked(int x, int y, int button) {
				closeGui();
				GuiHandler.openGui("block",  new NBTTagCompound(), getPlayer());
			}

		});

	}
}
