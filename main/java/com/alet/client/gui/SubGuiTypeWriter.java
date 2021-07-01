package com.alet.client.gui;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.Color;

import com.alet.ALET;
import com.alet.client.gui.controls.GuiGlyphSelector;
import com.alet.client.gui.controls.GuiImage;
import com.alet.client.gui.controls.GuiLongTextField;
import com.alet.client.gui.controls.GuiToolTipBox;
import com.alet.client.gui.controls.Layer;
import com.alet.client.gui.message.SubGuiNoTextInFieldMessage;
import com.alet.common.packet.PacketUpdateStructureFromClient;
import com.alet.common.structure.type.premade.LittleTypeWriter;
import com.alet.common.util.CopyUtils;
import com.alet.font.FontReader;
import com.alet.littletiles.gui.controls.GuiColorPickerAlet;
import com.creativemd.creativecore.common.gui.container.SubGui;
import com.creativemd.creativecore.common.gui.controls.gui.GuiAnalogeSlider;
import com.creativemd.creativecore.common.gui.controls.gui.GuiButton;
import com.creativemd.creativecore.common.gui.controls.gui.GuiComboBox;
import com.creativemd.creativecore.common.gui.controls.gui.GuiComboBoxExtension;
import com.creativemd.creativecore.common.gui.controls.gui.GuiTextfield;
import com.creativemd.creativecore.common.gui.event.gui.GuiControlChangedEvent;
import com.creativemd.creativecore.common.packet.PacketHandler;
import com.creativemd.creativecore.common.utils.mc.ColorUtils;
import com.creativemd.littletiles.LittleTiles;
import com.creativemd.littletiles.common.structure.LittleStructure;
import com.creativemd.littletiles.common.util.grid.LittleGridContext;
import com.n247s.api.eventapi.eventsystem.CustomEventSubscribe;

import net.minecraft.nbt.NBTTagCompound;

public class SubGuiTypeWriter extends SubGui {
	
	public static List<String> names = ALET.fontTypeNames;
	
	public LittleStructure structure;
	public int BLACK = ColorUtils.BLACK;
	public NBTTagCompound nbt = new NBTTagCompound();
	
	public SubGuiTypeWriter(LittleStructure structure) {
		super(277, 190);
		this.structure = structure;
	}
	
	public void openedGui() {
		LittleTypeWriter typeWriter = (LittleTypeWriter) structure;
		typeWriter.writeToNBT(nbt);
		if (nbt.hasKey("font")) {
			GuiComboBox fontBox = (GuiComboBox) get("fontType");
			fontBox.select(nbt.getString("font"));
		}
		if (nbt.hasKey("fontSize")) {
			GuiTextfield fontSize = (GuiTextfield) get("fontSize");
			fontSize.text = nbt.getString("fontSize");
		}
		if (nbt.hasKey("color")) {
			GuiColorPickerAlet picker = (GuiColorPickerAlet) get("picker");
			picker.setColor(ColorUtils.IntToRGBA(nbt.getInteger("color")));
		}
		if (nbt.hasKey("grid")) {
			GuiComboBox gridBox = (GuiComboBox) get("grid");
			gridBox.select(nbt.getString("grid"));
		}
		if (nbt.hasKey("rotation")) {
			GuiAnalogeSlider rotation = (GuiAnalogeSlider) get("rotation");
			rotation.value = nbt.getDouble("rotation");
		}
		GuiImage image = (GuiImage) get("image");
		GuiComboBox contextBox = (GuiComboBox) get("fontType");
		GuiTextfield fontSize = (GuiTextfield) get("fontSize");
		String font = contextBox.getCaption();
		GuiAnalogeSlider rotation = (GuiAnalogeSlider) get("rotation");
		GuiColorPickerAlet color = (GuiColorPickerAlet) get("picker");
		image.updateFont(font, Integer.parseInt(fontSize.text), ColorUtils.RGBAToInt(color.color), rotation.value);
	}
	
	@Override
	public void onClosed() {
		GuiColorPickerAlet picker = (GuiColorPickerAlet) get("picker");
		GuiTextfield fontSize = (GuiTextfield) get("fontSize");
		GuiComboBox fontBox = (GuiComboBox) get("fontType");
		GuiComboBox gridBox = (GuiComboBox) get("grid");
		GuiAnalogeSlider rotation = (GuiAnalogeSlider) get("rotation");
		
		LittleTypeWriter typeWriter = (LittleTypeWriter) structure;
		nbt.setString("font", fontBox.getCaption());
		nbt.setString("fontSize", fontSize.text);
		nbt.setInteger("color", ColorUtils.RGBAToInt(picker.color));
		nbt.setString("grid", gridBox.getCaption());
		nbt.setDouble("rotation", rotation.value);
		PacketHandler.sendPacketToServer(new PacketUpdateStructureFromClient(typeWriter.getStructureLocation(), nbt));
		
		super.onClosed();
	}
	
	@Override
	public boolean mousePressed(int x, int y, int button) {
		boolean result = super.mousePressed(x, y, button);
		if (result) {
			GuiImage image = (GuiImage) get("image");
			GuiComboBox contextBox = (GuiComboBox) get("fontType");
			GuiTextfield fontSize = (GuiTextfield) get("fontSize");
			String font = contextBox.getCaption();
			GuiAnalogeSlider rotation = (GuiAnalogeSlider) get("rotation");
			GuiColorPickerAlet color = (GuiColorPickerAlet) get("picker");
			image.updateFont(font, Integer.parseInt(fontSize.text), ColorUtils.RGBAToInt(color.color), rotation.value);
		}
		return result;
	}
	
	@Override
	public void mouseReleased(int x, int y, int button) {
		GuiImage image = (GuiImage) get("image");
		GuiComboBox contextBox = (GuiComboBox) get("fontType");
		GuiTextfield fontSize = (GuiTextfield) get("fontSize");
		String font = contextBox.getCaption();
		GuiAnalogeSlider rotation = (GuiAnalogeSlider) get("rotation");
		GuiColorPickerAlet color = (GuiColorPickerAlet) get("picker");
		image.updateFont(font, Integer.parseInt(fontSize.text), ColorUtils.RGBAToInt(color.color), rotation.value);
		
		super.mouseReleased(x, y, button);
	}
	
	@Override
	public void createControls() {
		LittleTypeWriter typeWriter = (LittleTypeWriter) structure;
		typeWriter.writeToNBT(nbt);
		//System.out.println(nbt);
		Color color = ColorUtils.IntToRGBA(BLACK);
		if (nbt.hasKey("color"))
			color = ColorUtils.IntToRGBA(nbt.getInteger("color"));
		
		controls.add(new GuiColorPickerAlet("picker", -2, 42, color, LittleTiles.CONFIG.isTransparencyEnabled(getPlayer()), LittleTiles.CONFIG.getMinimumTransparency(getPlayer())));
		
		controls.add(new GuiComboBox("grid", 256, 0, 15, LittleGridContext.getNames()) {
			
			@Override
			protected GuiComboBoxExtension createBox() {
				return new GuiComboBoxExtension(name + "extension", this, posX, posY + height, 30 - getContentOffset() * 2, 100, lines);
			}
		});
		
		GuiTextfield fontSize = new GuiTextfield("fontSize", "48", 229, 0, 20, 14);
		controls.add(fontSize);
		
		controls.add(new GuiLongTextField("input", "", 0, 21, 271, 15));
		
		controls.add(new GuiToolTipBox("tips").addAdditionalTips("picker", "\n\nThis sets the color of the text.").addAdditionalTips("grid", "Grid Size:\nSets the size the tiles will be.\n\nSizes:\nTo change the max grid size open your littletiles.json in your config folder within your Minecraft folder. \n\nChange the Scale line to:\nScale = 7 (64 grid)\nScale = 8 (128 grid)\nScale = 9 (256 grid)\nAnd so on."));
		GuiToolTipBox tips = (GuiToolTipBox) get("tips");
		tips.addAdditionalTips("input", "Input:\nThis is the text that will be exported as a structure.\n\nClick on it and you can type or press ctrl+v to paste text you have copied.");
		tips.addAdditionalTips("search", "Search Font:\nSearch for a font.\n\nClick and type in a name or part of a name of the font you are looking for.");
		tips.addAdditionalTips("fontSize", "Font Size:\nSet the size of the font.\n\nClick and type in a positive number for the size of the font.");
		tips.addAdditionalTips("fontType", "Font:\nSelect the Font to use.\n\nClick and select a font. You can click and drag the scroll bar or use the mouse wheel to move the menu.");
		tips.addAdditionalTips("rotation", "Rotation:\nUsed to change the rotation of the text.\n\nControls:\nClick and drag on the slider to adjust rotation.\nRight Click to enter a value");
		tips.addAdditionalTips("Paste", "Paste:\nPastes any text you have copied into the text field above.");
		tips.addAdditionalTips("Print", "Print:\nPrints out the text you have entered into the text field above.");
		tips.addAdditionalTips("MainGui", "Typewriter:\nWill create a structure of the text you entered with the font, font size, color, and grid size you have selected.");
		controls.add(new GuiTextfield("search", "", 0, 0, 115, 14) {
			
			@Override
			public boolean onKeyPressed(char character, int key) {
				boolean result = super.onKeyPressed(character, key);
				if (!result)
					return result;
				
				GuiComboBox fontType = (GuiComboBox) get("fontType");
				
				List<String> foundFonts = new ArrayList<>();
				for (int i = 0; i < ALET.fontTypeNames.size(); i++) {
					if (ALET.fontTypeNames.get(i).toLowerCase().contains(this.text.toLowerCase()))
						foundFonts.add(ALET.fontTypeNames.get(i));
				}
				if (!foundFonts.isEmpty()) {
					fontType.lines = foundFonts;
					int index = ALET.fontTypeNames.indexOf(foundFonts.get(0));
					fontType.select(ALET.fontTypeNames.get(index));
				}
				
				return result;
			}
		});
		
		GuiTextfield fontSearch = (GuiTextfield) get("search");
		fontSearch.setCustomTooltip("Search for Font");
		
		controls.add(new GuiComboBox("fontType", 122, 0, 100, ALET.fontTypeNames) {
			
			@Override
			protected GuiComboBoxExtension createBox() {
				GuiComboBoxExtension extend = new GuiComboBoxExtension(name + "extension", this, posX, posY + height, 200 - getContentOffset() * 2, 100, lines);
				extend.scrolled.set(15 * this.index);
				return extend;
			}
		});
		GuiComboBox fontCombo = (GuiComboBox) get("fontType");
		fontCombo.setCustomTooltip("Font");
		
		controls.add(new GuiAnalogeSlider("rotation", 0, 96, 150, 10, 0, 0, 360));
		
		controls.add(new GuiButton("Paste", "Paste", 221, 43, 50) {
			
			@Override
			public void onClicked(int x, int y, int button) {
				
				GuiLongTextField input = (GuiLongTextField) get("input");
				
				StringSelection stringSelection = new StringSelection(input.text);
				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				String path = CopyUtils.getCopiedFilePath(clipboard);
				if (path == null)
					return;
				try {
					input.text = path;
				} catch (Exception e) {
					
				}
			}
		});
		
		GuiComboBox contextBox = (GuiComboBox) get("fontType");
		String font = contextBox.getCaption();
		GuiAnalogeSlider rotation = (GuiAnalogeSlider) get("rotation");
		controls.add(new GuiImage("image", 180, 130, font, Integer.parseInt(fontSize.text), ColorUtils.RGBAToInt(color), rotation.value));
		
		controls.add(new GuiGlyphSelector("na", font, 180, 85, 80));
		controls.add(new GuiButton("Print", 243, 64) {
			
			@Override
			public void onClicked(int x, int y, int button) {
				GuiLongTextField input = (GuiLongTextField) get("input");
				if (input.text.equals(""))
					Layer.addLayer(getGui(), new SubGuiNoTextInFieldMessage("for the text that will be exported", "digit(s) and or character(s)"));
				else {
					GuiColorPickerAlet picker = (GuiColorPickerAlet) get("picker");
					int color = ColorUtils.RGBAToInt(picker.color);
					
					GuiTextfield contextField = (GuiTextfield) get("fontSize");
					int fontSize = Integer.parseInt(contextField.text);
					
					GuiComboBox contextBox = (GuiComboBox) get("fontType");
					String font = contextBox.getCaption();
					
					GuiComboBox contextBox_2 = (GuiComboBox) get("grid");
					int grid = Integer.parseInt(contextBox_2.getCaption());
					
					GuiAnalogeSlider rotation = (GuiAnalogeSlider) get("rotation");
					
					try {
						NBTTagCompound nbt = FontReader.photoToNBT(input.text, font, grid, fontSize, color, rotation.value);
						if (nbt != null)
							sendPacketToServer(nbt);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
		
		openedGui();
	}
	
	@CustomEventSubscribe
	public void onChanged(GuiControlChangedEvent event) {
		System.out.println(event.source.name);
		GuiComboBox contextBox = (GuiComboBox) get("fontType");
		String font = contextBox.getCaption();
		System.out.println(font);
		GuiGlyphSelector na = (GuiGlyphSelector) get("na");
		na.fontr = font;
		if (event.source.is("searchBar")) {
			
			refreshControls();
		}
	}
}
