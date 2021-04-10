package com.alet.client.gui;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;

import com.alet.ALET;
import com.alet.client.gui.controls.GuiLongTextField;
import com.alet.client.gui.controls.Layer;
import com.alet.common.util.CopyUtils;
import com.alet.photo.PhotoReader;
import com.creativemd.creativecore.common.gui.container.SubGui;
import com.creativemd.creativecore.common.gui.controls.gui.GuiButton;
import com.creativemd.creativecore.common.gui.controls.gui.GuiCheckBox;
import com.creativemd.creativecore.common.gui.controls.gui.GuiComboBox;
import com.creativemd.creativecore.common.gui.controls.gui.GuiTextfield;
import com.creativemd.creativecore.common.gui.event.gui.GuiControlChangedEvent;
import com.creativemd.creativecore.common.gui.opener.GuiHandler;
import com.creativemd.littletiles.common.item.ItemMultiTiles;
import com.creativemd.littletiles.common.util.grid.LittleGridContext;

import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;

public class SubGuiPhotoImport extends SubGui {
	
	public GuiLongTextField file;
	public GuiCheckBox useFile = null;
	public GuiCheckBox useURL = null;
	public GuiCheckBox keepAspect = null;
	public GuiTextfield imgHeight = null;
	public GuiTextfield imgWidth = null;
	public GuiButton autoScale = null;
	
	public double aspectRatio = 0;
	
	public SubGuiPhotoImport() {
		super(176, 190);
	}
	
	@Override
	public void createControls() {
		imgWidth = (new GuiTextfield("imgWidth", "0", 93, 85, 30, 14) {
			@Override
			public void onTextChange() {
				
				super.onTextChange();
			}
			
			@Override
			public boolean onKeyPressed(char character, int key) {
				boolean result = super.onKeyPressed(character, key);
				if (!imgWidth.text.isEmpty() && keepAspect.value) {
					imgHeight.text = String.valueOf((int) (aspectRatio * Double.parseDouble(imgWidth.text)));
				}
				return result;
			}
		});
		imgWidth.enabled = false;
		imgWidth.setCustomTooltip("Width Of Image");
		imgWidth.setNumbersOnly();
		controls.add(imgWidth);
		
		imgHeight = (new GuiTextfield("imgHeight", "0", 135, 85, 30, 14) {
			@Override
			public void onTextChange() {
				
				super.onTextChange();
			}
			
			@Override
			public boolean onKeyPressed(char character, int key) {
				boolean result = super.onKeyPressed(character, key);
				if (!imgHeight.text.isEmpty() && keepAspect.value) {
					imgWidth.text = String.valueOf((int) (Double.parseDouble(imgHeight.text) / aspectRatio));
				}
				return result;
			}
		});
		imgHeight.enabled = false;
		imgHeight.setCustomTooltip("Height Of Image");
		imgHeight.setNumbersOnly();
		controls.add(imgHeight);
		
		keepAspect = new GuiCheckBox("keepAspect", translate("Keep Aspect Ratio?"), 8, 47, false);
		controls.add(keepAspect);
		
		autoScale = (new GuiButton("autoScale", "Auto Scale Image?", 10, 61, 90) {
			@Override
			public void onClicked(int x, int y, int button) {
				
				int x1 = (int) (ALET.CONFIG.getMaxPixelAmount() * aspectRatio);
				int x2 = (int) Math.sqrt(x1);
				int y1 = (int) (x2 / aspectRatio);
				
				int xa = Integer.parseInt(imgHeight.text);
				int ya = Integer.parseInt(imgWidth.text);
				System.out.println((xa * ya) + " " + x1);
				if (xa * ya > x1) {
					if (Integer.parseInt(imgWidth.text) > Integer.parseInt(imgHeight.text)) {
						imgHeight.text = String.valueOf(x2);
						imgWidth.text = String.valueOf(y1);
					} else if (Integer.parseInt(imgWidth.text) < Integer.parseInt(imgHeight.text)) {
						imgWidth.text = String.valueOf(y1);
						imgHeight.text = String.valueOf(x2);
					} else {
						imgHeight.text = String.valueOf(x2);
						imgWidth.text = String.valueOf(x2);
					}
				}
				
			}
		});
		autoScale.enabled = false;
		autoScale.setCustomTooltip("Shrinks Image down to max image size.");
		controls.add(autoScale);
		
		GuiComboBox contextBox = new GuiComboBox("grid", 120, 0, 15, LittleGridContext.getNames());
		contextBox.select(ItemMultiTiles.currentContext.size + "");
		controls.add(contextBox);
		
		controls.add(file = new GuiLongTextField("file", "", 10, 26, 150, 14) {
			@Override
			public boolean onKeyPressed(char character, int key) {
				boolean result = super.onKeyPressed(character, key);
				if (PhotoReader.imageExists(file.text, useURL.value)) {
					imgHeight.text = Integer.toString(PhotoReader.getPixelLength(file.text, useURL.value));
					imgWidth.text = Integer.toString(PhotoReader.getPixelWidth(file.text, useURL.value));
					imgHeight.enabled = true;
					imgWidth.enabled = true;
					autoScale.enabled = true;
					aspectRatio = Float.parseFloat(imgHeight.text) / Float.parseFloat(imgWidth.text);
				} else {
					imgHeight.text = "0";
					imgWidth.text = "0";
					imgHeight.enabled = false;
					imgWidth.enabled = false;
					autoScale.enabled = false;
				}
				return result;
			}
		});
		
		controls.add(useFile = new GuiCheckBox("useFile", translate("Use file path"), 40, -1, true) {
			@Override
			public boolean mousePressed(int posX, int posY, int button) {
				playSound(SoundEvents.UI_BUTTON_CLICK);
				useFile.value = true;
				useURL.value = false;
				raiseEvent(new GuiControlChangedEvent(this));
				if (PhotoReader.imageExists(file.text, useURL.value)) {
					imgHeight.text = Integer.toString(PhotoReader.getPixelLength(file.text, useURL.value));
					imgWidth.text = Integer.toString(PhotoReader.getPixelWidth(file.text, useURL.value));
					imgHeight.enabled = true;
					imgWidth.enabled = true;
					autoScale.enabled = true;
					aspectRatio = Float.parseFloat(imgHeight.text) / Float.parseFloat(imgWidth.text);
				} else {
					imgHeight.text = "0";
					imgWidth.text = "0";
					imgHeight.enabled = false;
					imgWidth.enabled = false;
					autoScale.enabled = false;
				}
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
				if (PhotoReader.imageExists(file.text, useURL.value)) {
					imgHeight.text = Integer.toString(PhotoReader.getPixelLength(file.text, useURL.value));
					imgWidth.text = Integer.toString(PhotoReader.getPixelWidth(file.text, useURL.value));
					imgHeight.enabled = true;
					imgWidth.enabled = true;
					autoScale.enabled = true;
					aspectRatio = Float.parseFloat(imgHeight.text) / Float.parseFloat(imgWidth.text);
				} else {
					imgHeight.text = "0";
					imgWidth.text = "0";
					imgHeight.enabled = false;
					imgWidth.enabled = false;
					autoScale.enabled = false;
				}
				return true;
			}
		});
		
		if (!ALET.CONFIG.isAllowURL()) {
			useURL.enabled = false;
		}
		
		GuiButton paste = (new GuiButton("Paste", 10, 85) {
			
			@Override
			public void onClicked(int x, int y, int button) {
				StringSelection stringSelection = new StringSelection(file.text);
				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				String path = CopyUtils.getCopiedFilePath(clipboard);
				if (path == null)
					return;
				file.text = path;
				
				if (PhotoReader.imageExists(file.text, useURL.value)) {
					imgHeight.text = Integer.toString(PhotoReader.getPixelLength(file.text, useURL.value));
					imgWidth.text = Integer.toString(PhotoReader.getPixelWidth(file.text, useURL.value));
					imgHeight.enabled = true;
					imgWidth.enabled = true;
					autoScale.enabled = true;
					aspectRatio = Float.parseFloat(imgHeight.text) / Float.parseFloat(imgWidth.text);
				} else {
					imgHeight.text = "0";
					imgWidth.text = "0";
					autoScale.enabled = false;
					imgHeight.enabled = false;
					imgWidth.enabled = false;
				}
			}
		});
		controls.add(paste);
		
		GuiButton print = (new GuiButton("Print", 50, 85) {
			
			@Override
			public void onClicked(int x, int y, int button) {
				int resizeX = Integer.parseInt(imgWidth.text);
				int resizeY = Integer.parseInt(imgHeight.text);
				
				if (resizeX * resizeY > ALET.CONFIG.getMaxPixelAmount()) {
					Layer.addLayer(getGui(), new SubGuiErrorMessage(resizeX * resizeY));
				} else {
					PhotoReader.setScale(resizeX, resizeY);
					
					GuiComboBox contextBox = (GuiComboBox) get("grid");
					int grid = Integer.parseInt(contextBox.getCaption());
					try {
						NBTTagCompound nbt = PhotoReader.photoToNBT(file.text, useURL.value, grid);
						sendPacketToServer(nbt);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
		controls.add(print);
		
		controls.add(new GuiButton("-->", 145, 0) {
			
			@Override
			public void onClicked(int x, int y, int button) {
				closeGui();
				GuiHandler.openGui("block", new NBTTagCompound(), getPlayer());
			}
		});
	}
}
