package com.alet.client.gui;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.Color;

import com.alet.ALET;
import com.alet.client.gui.controls.GuiLongTextField;
import com.alet.client.gui.controls.Layer;
import com.alet.client.gui.message.SubGuiNoTextInFieldMessage;
import com.alet.common.packet.PacketUpdateStructureFromClient;
import com.alet.common.structure.type.premade.LittleTypeWriter;
import com.alet.common.util.CopyUtils;
import com.alet.font.FontReader;
import com.alet.littletiles.gui.controls.GuiColorPickerAlet;
import com.creativemd.creativecore.common.gui.GuiControl;
import com.creativemd.creativecore.common.gui.container.SubGui;
import com.creativemd.creativecore.common.gui.controls.gui.GuiAnalogeSlider;
import com.creativemd.creativecore.common.gui.controls.gui.GuiButton;
import com.creativemd.creativecore.common.gui.controls.gui.GuiComboBox;
import com.creativemd.creativecore.common.gui.controls.gui.GuiTextfield;
import com.creativemd.creativecore.common.packet.PacketHandler;
import com.creativemd.creativecore.common.utils.mc.ColorUtils;
import com.creativemd.littletiles.LittleTiles;
import com.creativemd.littletiles.common.structure.LittleStructure;
import com.creativemd.littletiles.common.util.grid.LittleGridContext;

import net.minecraft.nbt.NBTTagCompound;

public class SubGuiTypeWriter extends SubGui {
	
	public static List<String> names = ALET.fontTypeNames;
	
	public LittleStructure structure;
	public int BLACK = ColorUtils.BLACK;
	public NBTTagCompound nbt = new NBTTagCompound();
	
	public SubGuiTypeWriter(LittleStructure structure) {
		super(250, 190);
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
	public void createControls() {
		LittleTypeWriter typeWriter = (LittleTypeWriter) structure;
		typeWriter.writeToNBT(nbt);
		//System.out.println(nbt);
		Color color = ColorUtils.IntToRGBA(BLACK);
		if (nbt.hasKey("color"))
			color = ColorUtils.IntToRGBA(nbt.getInteger("color"));
		
		controls.add(new GuiColorPickerAlet("picker", 0, 60, color, LittleTiles.CONFIG.isTransparencyEnabled(getPlayer()), LittleTiles.CONFIG.getMinimumTransparency(getPlayer())));
		
		controls.add(new GuiComboBox("grid", 155, 40, 15, LittleGridContext.getNames()).setCustomTooltip("Grid"));
		
		controls.add(new GuiLongTextField("input", "", 20, 40, 100, 14).setCustomTooltip("Text to Exported"));
		
		controls.add(new GuiComboBox("fontType", 20, 19, 150, ALET.fontTypeNames));
		
		controls.add(new GuiTextfield("search", "", 20, 0, 150, 14) {
			
			@Override
			public GuiControl setCustomTooltip(String... lines) {
				return super.setCustomTooltip("Search For Font");
			}
			
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
		
		controls.add(new GuiAnalogeSlider("rotation", 177, 0, 64, 20, 0, 0, 360).setCustomTooltip("Rotate Text"));
		
		GuiTextfield fontSize = new GuiTextfield("fontSize", "48", 128, 40, 20, 14);
		fontSize.setCustomTooltip("Font Size");
		controls.add(fontSize);
		
		controls.add(new GuiButton("Paste", 142, 82) {
			
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
		
		controls.add(new GuiButton("Print ", 142, 61) {
			
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
	
}
