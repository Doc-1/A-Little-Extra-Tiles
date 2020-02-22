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
import com.creativemd.littletiles.common.util.grid.LittleGridContext;
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
	
	public GuiTextfield file;
	public GuiCheckBox useFile = null;
	public GuiCheckBox useURL = null;
	public GuiCheckBox isRescale = null;
	public GuiTextfield imgHeight = null;
	public GuiTextfield imgWidth = null;
	
	public float aspectRatio = 0;
	
	@Override
	public void createControls() {
		imgWidth = (new GuiTextfield("imgWidth", "0", 115, 60, 20, 14) {
			@Override
			public boolean onKeyPressed(char character, int key) {
				
				if(super.onKeyPressed(character, key) && !imgWidth.text.isEmpty()) {
					imgHeight.text =String.valueOf((int)(aspectRatio*Float.parseFloat(imgWidth.text)));
				}
				return false;
			}
		});
		imgWidth.enabled = false;
		imgWidth.setCustomTooltip("X Scale Of Image");
		imgWidth.setNumbersOnly();
		controls.add(imgWidth);
		
		imgHeight = (new GuiTextfield("imgHeight", "0", 145, 60, 20, 14) {
			@Override
			public boolean onKeyPressed(char character, int key) {
				
				if(super.onKeyPressed(character, key) && !imgHeight.text.isEmpty()) {
					System.out.println(aspectRatio);
					imgWidth.text = String.valueOf((int)(Float.parseFloat(imgHeight.text)/aspectRatio));
				}
				return false;
			}
		});
		imgHeight.enabled = false;
		imgHeight.setCustomTooltip("Y Scale Of Image");
		imgHeight.setNumbersOnly();
		controls.add(imgHeight);
		
		isRescale = new GuiCheckBox("isRescale", translate("Lock Dimensions?     X      Y"), 8, 45, false);
		controls.add(isRescale);
		
		GuiComboBox contextBox = new GuiComboBox("grid", 120, 0, 15, LittleGridContext.getNames());
		contextBox.select(ItemMultiTiles.currentContext.size + "");
		controls.add(contextBox);
		
		controls.add(file = new GuiTextfield("file", "", 10, 26, 150, 14) {
			@Override
			public boolean onKeyPressed(char character, int key) {
				if(super.onKeyPressed(character, key)) {
					//C:\Users\keven\Desktop\med.png
					if(PhotoReader.imageExists(file.text, useURL.value)) {
						imgHeight.text = Integer.toString(PhotoReader.getPixelWidth(file.text, useURL.value));
						imgWidth.text = Integer.toString(PhotoReader.getPixelLength(file.text, useURL.value));
						imgHeight.enabled = true;
						imgWidth.enabled = true;
						aspectRatio = Float.parseFloat(imgHeight.text)/Float.parseFloat(imgWidth.text);
					}else {
						imgHeight.text = "0";
						imgWidth.text = "0";
						imgHeight.enabled = false;
						imgWidth.enabled = false;
					}
				}
				return true;
			}
		});
		
		controls.add(useFile = new GuiCheckBox("useFile", translate("Use file path"), 40, -1, true) {
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
				StringSelection stringSelection = new StringSelection(file.text);
				Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
				Transferable t = clpbrd.getContents(this);
				if (t == null)
					return;
				try {
					file.text = (String) t.getTransferData(DataFlavor.stringFlavor);
				} catch (Exception e) {}
				
				if(PhotoReader.imageExists(file.text, useURL.value)) {
					imgHeight.text = Integer.toString(PhotoReader.getPixelWidth(file.text, useURL.value));
					imgWidth.text = Integer.toString(PhotoReader.getPixelLength(file.text, useURL.value));
					imgHeight.enabled = true;
					imgWidth.enabled = true;
					aspectRatio = Float.parseFloat(imgHeight.text)/Float.parseFloat(imgWidth.text);
				}else {
					imgHeight.text = "0";
					imgWidth.text = "0";
					imgHeight.enabled = false;
					imgWidth.enabled = false;
				}
			}
		});
		
		controls.add(new GuiButton("Print", 50, 60) {
			
			@Override
			public void onClicked(int x, int y, int button) {
				if(isRescale.value) {
					int resizeX = Integer.parseInt(imgWidth.text);
					int resizeY = Integer.parseInt(imgHeight.text);
					PhotoReader.setScale(resizeX, resizeY);
				}
				
				GuiComboBox contextBox = (GuiComboBox) get("grid");
				int grid = Integer.parseInt(contextBox.caption);
				try {
					NBTTagCompound nbt = PhotoReader.photoToNBT(file.text, useURL.value, grid);
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
