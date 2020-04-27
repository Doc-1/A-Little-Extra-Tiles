package com.alet.gui;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.Color;

import com.alet.ALET;
import com.alet.font.FontReader;
import com.creativemd.creativecore.common.gui.container.SubGui;
import com.creativemd.creativecore.common.gui.controls.gui.GuiButton;
import com.creativemd.creativecore.common.gui.controls.gui.GuiColorPicker;
import com.creativemd.creativecore.common.gui.controls.gui.GuiComboBox;
import com.creativemd.creativecore.common.gui.controls.gui.GuiTextfield;
import com.creativemd.creativecore.common.utils.mc.ColorUtils;
import com.creativemd.littletiles.LittleTiles;
import com.creativemd.littletiles.common.item.ItemMultiTiles;
import com.creativemd.littletiles.common.util.grid.LittleGridContext;

import net.minecraft.nbt.NBTTagCompound;

public class SubGuiTypeWriter extends SubGui {
	
	public GuiTextfield input;
	public GuiTextfield search;

	public static List<String> names = ALET.fontTypeNames;
	
	public int BLACK = ColorUtils.BLACK;
	
	public SubGuiTypeWriter() {
		super(176, 190);
	}
	
	@Override
	public void createControls() {
		
		Color color = ColorUtils.IntToRGBA(BLACK);
		controls.add(new GuiColorPicker("picker", 0, 60, color, LittleTiles.CONFIG.isTransparencyEnabled(getPlayer()), LittleTiles.CONFIG.getMinimumTransparency(getPlayer())));
		
		GuiComboBox contextBox = new GuiComboBox("grid", 155, 40, 15, LittleGridContext.getNames());
		contextBox.select(ItemMultiTiles.currentContext.size + "");
		
		controls.add(contextBox);
		
		input = new GuiTextfield("input", "", 20, 40, 100, 14);
		input.setCustomTooltip("Text to Exported");
		controls.add(input);
		
		GuiComboBox fontType = new GuiComboBox("fontType", 20, 19, 150, names);
		int index = names.indexOf(fontType.caption);
		fontType.select(names.get(index));
		controls.add(fontType);
		
		search = (new GuiTextfield("search", "", 20, 0, 150, 14) {
			@Override
			public boolean onKeyPressed(char character, int key) {
				if (super.onKeyPressed(character, key)) {
					List<String> foundFonts = new ArrayList<>();
					for (int i = 0; i < names.size(); i++) {
						if (names.get(i).toLowerCase().contains(search.text.toLowerCase()))
							foundFonts.add(names.get(i));
					}
					if(!foundFonts.isEmpty()) {
						fontType.lines = foundFonts;
						int index = names.indexOf(foundFonts.get(0));
						fontType.select(names.get(index));
					}
				}
				return false;
			}
		});
		search.setCustomTooltip("Search For Font");
		controls.add(search);
		
		GuiTextfield fontSize = new GuiTextfield("fontSize", "48", 128, 40, 20, 14);
		fontSize.setCustomTooltip("Font Size");
		controls.add(fontSize);
		
		
		controls.add(new GuiButton("Paste", 142, 82) {
			
			@Override
			public void onClicked(int x, int y, int button) {
				StringSelection stringSelection = new StringSelection(input.text);
				Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
				Transferable t = clpbrd.getContents(this);
				if (t == null)
					return;
				try {
					input.text = (String) t.getTransferData(DataFlavor.stringFlavor);
				} catch (Exception e) {
					
				}
			}
		});
		
		controls.add(new GuiButton("Print ", 142, 61) {
			
			@Override
			public void onClicked(int x, int y, int button) {
				
				GuiColorPicker picker = (GuiColorPicker) get("picker");
				int color = ColorUtils.RGBAToInt(picker.color);
				
				GuiTextfield contextField = (GuiTextfield) get("fontSize");
				int fontSize = Integer.parseInt(contextField.text);
				
				GuiComboBox contextBox = (GuiComboBox) get("fontType");
				String font = contextBox.caption;
				
				GuiComboBox contextBox_2 = (GuiComboBox) get("grid");
				int grid = Integer.parseInt(contextBox_2.caption);
				
				try {
					NBTTagCompound nbt = FontReader.photoToNBT(input.text, font, grid, fontSize, color);
					sendPacketToServer(nbt);
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
		});
	}
	
}
