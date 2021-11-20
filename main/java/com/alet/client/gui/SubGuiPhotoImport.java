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
import com.creativemd.littletiles.common.entity.AnimationPreview;
import com.creativemd.littletiles.common.item.ItemMultiTiles;
import com.creativemd.littletiles.common.tile.preview.LittlePreview;
import com.creativemd.littletiles.common.tile.preview.LittlePreviews;
import com.creativemd.littletiles.common.util.grid.LittleGridContext;

public class SubGuiPhotoImport extends SubGui {
	
	private volatile Thread thread;
	
	public SubGuiPhotoImport() {
		super(313, 200);
	}
	
	@Override
	public void createControls() {
		double aspectRatio = 0;
		GuiAnimationViewerAlet viewer = new GuiAnimationViewerAlet("renderer", 171, 0, 136, 135);
		controls.add(viewer);
		viewer.moveViewPort(0, 60);
		
		controls.add(new GuiButton("refresh", "Refresh Preview", 217, 145, 90) {
			
			@Override
			public void onClicked(int x, int y, int button) {
				GuiTextfield imageWidth = (GuiTextfield) get("imageWidth");
				GuiTextfield imageHeight = (GuiTextfield) get("imageHeight");
				GuiLongTextField file = (GuiLongTextField) get("file");
				int resizeX = Integer.parseInt(imageWidth.text);
				int resizeY = Integer.parseInt(imageHeight.text);
				
				if (resizeX * resizeY > ALET.CONFIG.getMaxPixelAmount()) {
					Layer.addLayer(getGui(), new SubGuiErrorMessage(resizeX * resizeY));
				} else {
					PhotoReader.setScale(resizeX, resizeY);
					
					GuiComboBox contextBox = (GuiComboBox) get("grid");
					int grid = Integer.parseInt(contextBox.getCaption());
					try {
						LittlePreviews pre = LittlePreview.getPreview(PhotoReader.photoToStack(file.text, false, grid, getGui()));
						viewer.onLoaded(new AnimationPreview(pre));
						if (pre == null)
							Layer.addLayer(getGui(), new SubGuiNoPathMessage(".png or .jpeg"));
					} catch (NullPointerException | IOException e) {
					}
				}
				
			}
		});
		
		controls.add(new GuiTextfield("imageWidth", "0", 93, 102, 30, 14) {
			@Override
			public boolean onKeyPressed(char character, int key) {
				GuiTextfield imageWidth = (GuiTextfield) get("imageWidth");
				GuiTextfield imageHeight = (GuiTextfield) get("imageHeight");
				GuiCheckBox keepAspect = (GuiCheckBox) get("keepAspect");
				boolean result = super.onKeyPressed(character, key);
				if (!this.text.isEmpty() && keepAspect.value) {
					imageHeight.text = String.valueOf((int) (aspectRatio * Double.parseDouble(imageWidth.text)));
				}
				return result;
			}
		});
		GuiTextfield imageWidth = (GuiTextfield) get("imageWidth");
		imageWidth.enabled = false;
		imageWidth.setCustomTooltip("Width Of Image");
		imageWidth.setNumbersOnly();
		
		controls.add(new GuiTextfield("imageHeight", "0", 132, 102, 30, 14) {
			
			@Override
			public boolean onKeyPressed(char character, int key) {
				GuiTextfield imageWidth = (GuiTextfield) get("imageWidth");
				GuiTextfield imageHeight = (GuiTextfield) get("imageHeight");
				GuiCheckBox keepAspect = (GuiCheckBox) get("keepAspect");
				boolean result = super.onKeyPressed(character, key);
				if (!imageHeight.text.isEmpty() && keepAspect.value) {
					imageWidth.text = String.valueOf((int) (Double.parseDouble(imageHeight.text) / aspectRatio));
				}
				return result;
			}
		});
		GuiTextfield imageHeight = (GuiTextfield) get("imageHeight");
		imageHeight.enabled = false;
		imageHeight.setCustomTooltip("Height Of Image");
		imageHeight.setNumbersOnly();
		
		controls.add(new GuiCheckBox("ignoreAlpha", "Ignore Alpha", -3, 50, false));
		
		controls.add(new GuiCheckBox("keepAspect", translate("Keep Aspect Ratio?"), -3, 65, false));
		
		controls.add(new GuiButton("autoScale", "Auto Scale Image?", 0, 80, 90) {
			@Override
			public void onClicked(int x, int y, int button) {
				GuiTextfield imageWidth = (GuiTextfield) get("imageWidth");
				GuiTextfield imageHeight = (GuiTextfield) get("imageHeight");
				
				int x1 = (int) (ALET.CONFIG.getMaxPixelAmount() * aspectRatio);
				int x2 = (int) Math.sqrt(x1);
				int y1 = (int) (x2 / aspectRatio);
				
				int xa = Integer.parseInt(imageHeight.text);
				int ya = Integer.parseInt(imageWidth.text);
				if (xa * ya > x1) {
					if (Integer.parseInt(imageWidth.text) > Integer.parseInt(imageHeight.text)) {
						imageHeight.text = String.valueOf(x2);
						imageWidth.text = String.valueOf(y1);
					} else if (Integer.parseInt(imageWidth.text) < Integer.parseInt(imageHeight.text)) {
						imageWidth.text = String.valueOf(y1);
						imageHeight.text = String.valueOf(x2);
					} else {
						imageHeight.text = String.valueOf(x2);
						imageWidth.text = String.valueOf(x2);
					}
				}
				
			}
		});
		GuiButton autoScale = (GuiButton) get("autoScale");
		autoScale.enabled = false;
		autoScale.setCustomTooltip("Shrinks Image down to max image size.");
		
		GuiComboBox contextBox = new GuiComboBox("grid", 120, 0, 15, LittleGridContext.getNames());
		contextBox.select(ItemMultiTiles.currentContext.size + "");
		controls.add(contextBox);
		
		controls.add(new GuiLongTextField("file", "", 0, 26, 162, 14) {
			@Override
			public boolean onKeyPressed(char character, int key) {
				boolean result = super.onKeyPressed(character, key);
				updatePhotoData(aspectRatio);
				return result;
			}
		});
		
		GuiButton paste = (new GuiButton("Paste", 0, 102) {
			
			@Override
			public void onClicked(int x, int y, int button) {
				GuiLongTextField file = (GuiLongTextField) get("file");
				StringSelection stringSelection = new StringSelection(file.text);
				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				String path = CopyUtils.getCopiedFilePath(clipboard);
				if (path == null)
					return;
				file.text = path;
				
				updatePhotoData(aspectRatio);
			}
		});
		controls.add(paste);
		
		GuiButton print = (new GuiButton("Print", 38, 102) {
			
			@Override
			public void onClicked(int x, int y, int button) {
				GuiLongTextField file = (GuiLongTextField) get("file");
				int resizeX = Integer.parseInt(imageWidth.text);
				int resizeY = Integer.parseInt(imageHeight.text);
				if (PhotoReader.photoExists(file.text, false))
					if (resizeX * resizeY > ALET.CONFIG.getMaxPixelAmount()) {
						Layer.addLayer(getGui(), new SubGuiErrorMessage(resizeX * resizeY));
					} else {
						PhotoReader.setScale(resizeX, resizeY);
						
						GuiComboBox contextBox = (GuiComboBox) get("grid");
						int grid = Integer.parseInt(contextBox.getCaption());
						PhotoConverter converter = new PhotoConverter(file.text, false, grid, this.getGui());
						thread = new Thread(converter);
						thread.start();
					}
				else
					Layer.addLayer(getGui(), new SubGuiNoPathMessage(".png or .jpeg"));
			}
		});
		controls.add(print);
		
		GuiProgressBar progress = new GuiProgressBar("progress", 165, 184, 142, 10, 100, 0);
		progress.setStyle(new Style("s", new ColoredDisplayStyle(0x11111111), new ColoredDisplayStyle(0xdddddddd), DisplayStyle.emptyDisplay, new ColoredDisplayStyle(0x2d9912), DisplayStyle.emptyDisplay));
		controls.add(progress);
	}
	
	public void updatePhotoData(double aspectRatio) {
		GuiLongTextField file = (GuiLongTextField) get("file");
		GuiTextfield imageWidth = (GuiTextfield) get("imageWidth");
		GuiTextfield imageHeight = (GuiTextfield) get("imageHeight");
		GuiButton autoScale = (GuiButton) get("autoScale");
		if (PhotoReader.imageExists(file.text, false)) {
			imageHeight.text = Integer.toString(PhotoReader.getPixelLength(file.text, false));
			imageWidth.text = Integer.toString(PhotoReader.getPixelWidth(file.text, false));
			imageHeight.enabled = true;
			imageWidth.enabled = true;
			autoScale.enabled = true;
			aspectRatio = Float.parseFloat(imageHeight.text) / Float.parseFloat(imageWidth.text);
		} else {
			imageHeight.text = "0";
			imageWidth.text = "0";
			imageHeight.enabled = false;
			imageWidth.enabled = false;
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
