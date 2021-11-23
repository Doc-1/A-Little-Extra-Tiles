package com.alet.client.gui;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.creativemd.creativecore.common.gui.container.SubGui;
import com.creativemd.creativecore.common.gui.controls.gui.GuiButton;
import com.creativemd.creativecore.common.gui.controls.gui.GuiLabel;
import com.creativemd.creativecore.common.gui.controls.gui.GuiTextfield;

import net.minecraft.util.text.TextFormatting;

public class GuiRenameFile extends SubGui {
	
	public String fileName;
	public SubGuiFillingCabinet instance;
	
	public GuiRenameFile(String fileName, SubGuiFillingCabinet instance) {
		super(200, 42);
		this.fileName = fileName;
		this.instance = instance;
	}
	
	@Override
	public void createControls() {
		addControl(new GuiTextfield("filename", this.fileName, 0, 0, 194, 14));
		addControl(new GuiButton("save", "Save", 144, 22, 50, 14) {
			
			@Override
			public void onClicked(int x, int y, int button) {
				File d = new File("./little_structures/" + fileName);
				if (d.exists()) {
					GuiTextfield fileName = (GuiTextfield) this.getGui().get("filename");
					File f = new File("./little_structures/" + fileName.text);
					try {
						FileUtils.copyFile(d, f);
					} catch (IOException e) {
						e.printStackTrace();
					}
					d.delete();
					instance.updateTree();
					
					GuiLabel lable = (GuiLabel) instance.get("viewing");
					lable.setCaption(TextFormatting.BOLD + fileName.text);
					this.getGui().closeGui();
				}
			}
		});
		addControl(new GuiButton("cancel", "Cancel", 0, 22, 50, 14) {
			
			@Override
			public void onClicked(int x, int y, int button) {
				this.getGui().closeGui();
			}
		});
	}
	
}
