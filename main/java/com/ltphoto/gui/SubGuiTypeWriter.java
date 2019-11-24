package com.ltphoto.gui;

import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.Color;

import com.creativemd.creativecore.common.gui.GuiControl;
import com.creativemd.creativecore.common.gui.container.SubGui;
import com.creativemd.creativecore.common.gui.controls.gui.GuiButton;
import com.creativemd.creativecore.common.gui.controls.gui.GuiCheckBox;
import com.creativemd.creativecore.common.gui.controls.gui.GuiColorPicker;
import com.creativemd.creativecore.common.gui.controls.gui.GuiComboBox;
import com.creativemd.creativecore.common.gui.controls.gui.GuiScrollBox;
import com.creativemd.creativecore.common.gui.controls.gui.GuiTextBox;
import com.creativemd.creativecore.common.gui.controls.gui.GuiTextfield;
import com.creativemd.creativecore.common.gui.event.gui.GuiControlChangedEvent;
import com.creativemd.creativecore.common.gui.opener.GuiHandler;
import com.creativemd.creativecore.common.utils.mc.ColorUtils;
import com.creativemd.littletiles.LittleTiles;
import com.creativemd.littletiles.common.config.SpecialServerConfig;
import com.creativemd.littletiles.client.gui.configure.SubGuiConfigure;
import com.creativemd.littletiles.client.gui.handler.LittleGuiHandler;
import com.creativemd.littletiles.common.items.ItemLittleChisel;
import com.creativemd.littletiles.common.items.ItemMultiTiles;
import com.creativemd.littletiles.common.tiles.LittleTile;
import com.creativemd.littletiles.common.tiles.preview.LittleTilePreview;
import com.creativemd.littletiles.common.utils.grid.LittleGridContext;
import com.creativemd.littletiles.common.utils.selection.mode.SelectionMode.SelectionResult;
import com.creativemd.littletiles.common.utils.shape.DragShape;
import com.ltphoto.config.Config;
import com.ltphoto.container.SubContainerPhotoImport;
import com.ltphoto.font.FontReader;
import com.ltphoto.photo.PhotoReader;
import com.ltphoto.structure.premade.LittlePhotoImporter;
import com.n247s.api.eventapi.eventsystem.CustomEventSubscribe;

import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import scala.actors.threadpool.Arrays;

public class SubGuiTypeWriter extends SubGui {
	
	public GuiTextfield textfield;
	public List<String> names;

	public int BLACK = ColorUtils.BLACK;
	
	public List<String> getFonts() {
		names = new ArrayList<>();
		String fonts[] = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
		for (int i = 0; i < fonts.length; i++) {
			names.add(fonts[i]);
		}
		return names;
	}
	
	@Override
	public void createControls() {
		
		Color color = ColorUtils.IntToRGBA(BLACK);
		controls.add(new GuiColorPicker("picker", 0, 40, color, SpecialServerConfig.isTransparencyEnabled(getPlayer()), SpecialServerConfig.getMinimumTransparency(getPlayer())));
		
		GuiComboBox contextBox = new GuiComboBox("grid", 155, 20, 15, LittleGridContext.getNames());
		contextBox.select(ItemMultiTiles.currentContext.size + "");
		controls.add(contextBox);
		
		textfield = new GuiTextfield("input", "", 20, 20, 100, 14);
		controls.add(textfield);
		
		GuiTextfield fontSize = new GuiTextfield("fontSize", "48", 128, 20, 20, 14);
		controls.add(fontSize);
		
		getFonts();
		GuiComboBox fontType = new GuiComboBox("fontType", 20, 0, 150, names);
		int index = names.indexOf(fontType.caption);
		fontType.select(names.get(index));
		controls.add(fontType);
		
		controls.add(new GuiButton("Paste", 142, 62) {
			
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
		
		controls.add(new GuiButton("Print ", 142, 41) {
			
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
					NBTTagCompound nbt = FontReader.photoToNBT(textfield.text, font, grid, fontSize, color);
					sendPacketToServer(nbt);
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
		});
	}
	
}
