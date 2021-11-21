package com.alet.client.gui;

import java.util.ArrayList;
import java.util.List;

import com.alet.client.gui.controls.GuiTree;
import com.alet.client.gui.controls.GuiTreePart;
import com.creativemd.creativecore.common.gui.container.SubGui;

public class SubGuiFillingCabinet extends SubGui {
	
	List<GuiTreePart> listOfRoots = new ArrayList<GuiTreePart>();
	
	public SubGuiFillingCabinet() {
		super(200, 200);
	}
	
	@Override
	public void createControls() {
		addControl(new GuiTree("list", 0, 0, 80, listOfRoots, false, 0, 0, 0));
	}
	
}
