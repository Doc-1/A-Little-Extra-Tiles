package com.alet.client.gui;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;

import com.alet.ALET;
import com.alet.client.gui.controls.GuiLongTextField;
import com.alet.client.gui.controls.Layer;
import com.alet.client.gui.message.SubGuiErrorMessage;
import com.alet.client.gui.message.SubGuiNoPathMessage;
import com.alet.common.util.CopyUtils;
import com.alet.littletiles.gui.controls.GuiAnimationViewerAlet;
import com.alet.photo.PhotoConverter;
import com.alet.photo.PhotoReader;
import com.creativemd.creativecore.common.gui.client.style.ColoredDisplayStyle;
import com.creativemd.creativecore.common.gui.client.style.DisplayStyle;
import com.creativemd.creativecore.common.gui.client.style.Style;
import com.creativemd.creativecore.common.gui.container.SubGui;
import com.creativemd.creativecore.common.gui.controls.gui.GuiButton;
import com.creativemd.creativecore.common.gui.controls.gui.GuiCheckBox;
import com.creativemd.creativecore.common.gui.controls.gui.GuiComboBox;
import com.creativemd.creativecore.common.gui.controls.gui.GuiProgressBar;
import com.creativemd.creativecore.common.gui.controls.gui.GuiTextfield;
import com.creativemd.creativecore.common.gui.opener.GuiHandler;
import com.creativemd.littletiles.common.entity.AnimationPreview;
import com.creativemd.littletiles.common.item.ItemMultiTiles;
import com.creativemd.littletiles.common.tile.preview.LittlePreview;
import com.creativemd.littletiles.common.tile.preview.LittlePreviews;
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
	
	private volatile Thread thread;
	
	public double aspectRatio = 0;
	
	public SubGuiPhotoImport() {
		super(313, 200);
	}
	
	@Override
	public void createControls() {
		GuiAnimationViewerAlet viewer = new GuiAnimationViewerAlet("renderer", 171, 0, 136, 135);
		controls.add(viewer);
		viewer.moveViewPort(0, 60);
		
		controls.add(new GuiButton("refresh", "Refresh Preview", 217, 145, 90) {
			
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
						LittlePreviews pre = LittlePreview.getPreview(PhotoReader.photoToStack(file.text, useURL.value, grid, getGui()));
						viewer.onLoaded(new AnimationPreview(pre));
						if (pre == null)
							Layer.addLayer(getGui(), new SubGuiNoPathMessage(".png or .jpeg"));
					} catch (NullPointerException | IOException e) {
					}
				}
				
			}
		});
		
		imgWidth = (new GuiTextfield("imgWidth", "0", 93, 102, 30, 14) {
			
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
		
		imgHeight = (new GuiTextfield("imgHeight", "0", 132, 102, 30, 14) {
			
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
		
		controls.add(new GuiCheckBox("ignoreAlpha", "Ignore Alpha", -3, 50, false));
		
		keepAspect = new GuiCheckBox("keepAspect", translate("Keep Aspect Ratio?"), -3, 65, false);
		controls.add(keepAspect);
		
		autoScale = (new GuiButton("autoScale", "Auto Scale Image?", 0, 80, 90) {
			@Override
			public void onClicked(int x, int y, int button) {
				
				int x1 = (int) (ALET.CONFIG.getMaxPixelAmount() * aspectRatio);
				int x2 = (int) Math.sqrt(x1);
				int y1 = (int) (x2 / aspectRatio);
				
				int xa = Integer.parseInt(imgHeight.text);
				int ya = Integer.parseInt(imgWidth.text);
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
		
		controls.add(file = new GuiLongTextField("file", "", 0, 26, 162, 14) {
			@Override
			public boolean onKeyPressed(char character, int key) {
				boolean result = super.onKeyPressed(character, key);
				updatePhotoData();
				return result;
			}
		});
		
		controls.add(useFile = new GuiCheckBox("useFile", translate("Use file path"), 40, -1, true) {
			@Override
			public boolean mousePressed(int posX, int posY, int button) {
				playSound(SoundEvents.UI_BUTTON_CLICK);
				useFile.value = true;
				useURL.value = false;
				updatePhotoData();
				return true;
			}
		});
		
		controls.add(useURL = new GuiCheckBox("useURL", translate("Use a URL"), 40, 10, false) {
			
			@Override
			public boolean mousePressed(int posX, int posY, int button) {
				playSound(SoundEvents.UI_BUTTON_CLICK);
				useFile.value = false;
				useURL.value = true;
				updatePhotoData();
				return true;
			}
		});
		
		if (!ALET.CONFIG.isAllowURL()) {
			useURL.enabled = false;
		}
		
		GuiButton paste = (new GuiButton("Paste", 0, 102) {
			
			@Override
			public void onClicked(int x, int y, int button) {
				StringSelection stringSelection = new StringSelection(file.text);
				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				String path = CopyUtils.getCopiedFilePath(clipboard);
				if (path == null)
					return;
				file.text = path;
				
				updatePhotoData();
			}
		});
		controls.add(paste);
		
		GuiButton print = (new GuiButton("Print", 38, 102) {
			
			@Override
			public void onClicked(int x, int y, int button) {
				int resizeX = Integer.parseInt(imgWidth.text);
				int resizeY = Integer.parseInt(imgHeight.text);
				if (PhotoReader.photoExists(file.text, useURL.value))
					if (resizeX * resizeY > ALET.CONFIG.getMaxPixelAmount()) {
						Layer.addLayer(getGui(), new SubGuiErrorMessage(resizeX * resizeY));
					} else {
						PhotoReader.setScale(resizeX, resizeY);
						
						GuiComboBox contextBox = (GuiComboBox) get("grid");
						int grid = Integer.parseInt(contextBox.getCaption());
						PhotoConverter converter = new PhotoConverter(file.text, useURL.value, grid, this.getGui());
						thread = new Thread(converter);
						thread.start();
					}
				else
					Layer.addLayer(getGui(), new SubGuiNoPathMessage(".png or .jpeg"));
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
		GuiProgressBar progress = new GuiProgressBar("progress", 165, 184, 142, 10, 100, 0);
		progress.setStyle(new Style("s", new ColoredDisplayStyle(0x11111111), new ColoredDisplayStyle(0xdddddddd), DisplayStyle.emptyDisplay, new ColoredDisplayStyle(0x2d9912), DisplayStyle.emptyDisplay));
		controls.add(progress);
	}
	
	public void updatePhotoData() {
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
	}
	
	@Override
	public void onClosed() {
		if (thread != null) {
			thread.currentThread().interrupt();
			thread = null;
		}
		super.onClosed();
	}
}
