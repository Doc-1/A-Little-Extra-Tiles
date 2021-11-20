package com.alet.client.gui;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.Color;

import com.alet.client.gui.controls.GuiColorablePanel;
import com.alet.client.gui.controls.GuiConfigurableTextBox;
import com.alet.client.gui.controls.GuiTree;
import com.alet.client.gui.controls.GuiTreePart;
import com.creativemd.creativecore.common.gui.client.style.Style;
import com.creativemd.creativecore.common.gui.container.SubGui;
import com.creativemd.creativecore.common.gui.controls.gui.GuiScrollBox;
import com.creativemd.creativecore.common.gui.controls.gui.GuiTextBox;
import com.creativemd.creativecore.common.gui.event.gui.GuiControlChangedEvent;
import com.n247s.api.eventapi.eventsystem.CustomEventSubscribe;

public class SubGuiManual extends SubGui {
	
	public SubGuiManual() {
		super(600, 400);
	}
	
	@Override
	public void createControls() {
		GuiColorablePanel panelLeft = new GuiColorablePanel("tableLeft", 0, 0, 200, 394, new Color(0, 0, 0), new Color(198, 198, 198));
		GuiColorablePanel panelRight = new GuiColorablePanel("tableRight", 209, 0, 385, 394, new Color(0, 0, 0), new Color(198, 198, 198));
		GuiScrollBox scrollBoxLeft = new GuiScrollBox("scrollBoxLeft", 0, 0, 379, 388);
		GuiConfigurableTextBox textBox = new GuiConfigurableTextBox("wiki", "", 0, 0, 379);
		textBox.setStyle(Style.emptyStyle);
		scrollBoxLeft.addControl(textBox);
		scrollBoxLeft.setStyle(defaultStyle);
		panelRight.addControl(scrollBoxLeft);
		GuiScrollBox scrollBox = new GuiScrollBox("scrollBox", 0, 0, 194, 388);
		scrollBox.setStyle(defaultStyle);
		controls.add(panelLeft);
		controls.add(panelRight);
		panelLeft.controls.add(scrollBox);
		
		List<GuiTreePart> listOfMenus = new ArrayList<GuiTreePart>();
		GuiTreePart welcome = new GuiTreePart("Welcome to Little Tiles");
		
		GuiTreePart drawshape = new GuiTreePart("Draw Shapes");
		GuiTreePart box = new GuiTreePart("Box");
		GuiTreePart slices = new GuiTreePart("Slices");
		GuiTreePart slice = new GuiTreePart("Slice");
		GuiTreePart sliceInner = new GuiTreePart("Inner Corner Slice");
		GuiTreePart sliceOuter = new GuiTreePart("Outer Corner Slice");
		slices.addMenu(slice).addMenu(sliceInner).addMenu(sliceOuter);
		
		GuiTreePart polygon = new GuiTreePart("Polygon");
		GuiTreePart wall = new GuiTreePart("Wall");
		GuiTreePart pillar = new GuiTreePart("Pillar");
		GuiTreePart curves = new GuiTreePart("Curves");
		GuiTreePart curvedLine = new GuiTreePart("Curve");
		GuiTreePart curvedWall = new GuiTreePart("Curved Wall");
		curves.addMenu(curvedLine).addMenu(curvedWall);
		
		GuiTreePart cylinder = new GuiTreePart("Cylinder");
		GuiTreePart pyramid = new GuiTreePart("Pyramid");
		GuiTreePart tile = new GuiTreePart("Tile");
		GuiTreePart type = new GuiTreePart("Type");
		GuiTreePart connected = new GuiTreePart("Connected");
		
		GuiTreePart tools = new GuiTreePart("Tools");
		GuiTreePart chisel = new GuiTreePart("Little Chisel");
		GuiTreePart hammer = new GuiTreePart("Little Hammer");
		GuiTreePart glove = new GuiTreePart("Little Glove");
		GuiTreePart paint = new GuiTreePart("Little Paint Brush");
		GuiTreePart saw = new GuiTreePart("Little Saw");
		GuiTreePart screwdriver = new GuiTreePart("Little Screwdriver");
		GuiTreePart wrench = new GuiTreePart("Little Wrench");
		
		GuiTreePart control = new GuiTreePart("Controls");
		GuiTreePart mark = new GuiTreePart("Mark Mode");
		GuiTreePart openConfig = new GuiTreePart("Open Configuration Menu");
		GuiTreePart openConfigAdv = new GuiTreePart("Open Mode/Grid Menu");
		GuiTreePart undo = new GuiTreePart("Undo/Redo");
		GuiTreePart movement = new GuiTreePart("Moving A Structure");
		control.addMenu(mark).addMenu(openConfig).addMenu(openConfigAdv).addMenu(undo).addMenu(movement);
		
		GuiTreePart block = new GuiTreePart("Blocks");
		GuiTreePart importer = new GuiTreePart("Little Importer");
		GuiTreePart exporter = new GuiTreePart("Little Exporter");
		GuiTreePart workbench = new GuiTreePart("Little Workbench");
		GuiTreePart partical = new GuiTreePart("Particle Emitter");
		GuiTreePart blankMatic = new GuiTreePart("Blank-o-matic");
		GuiTreePart structureBuilder = new GuiTreePart("Structure Builder");
		block.addMenu(importer).addMenu(exporter).addMenu(workbench).addMenu(partical).addMenu(blankMatic).addMenu(structureBuilder);
		
		GuiTreePart welcomeAlet = new GuiTreePart("Welcome To A Little Extra Tiles");
		GuiTreePart drawshapeAlet = new GuiTreePart("Draw Shapes");
		GuiTreePart magicWandAlet = new GuiTreePart("Magic Wand");
		drawshapeAlet.addMenu(magicWandAlet);
		
		GuiTreePart blockAlet = new GuiTreePart("Blocks");
		GuiTreePart typewriterAlet = new GuiTreePart("Typewriter");
		GuiTreePart photoAlet = new GuiTreePart("Photo Importer");
		blockAlet.addMenu(typewriterAlet).addMenu(photoAlet);
		
		GuiTreePart toolsAlet = new GuiTreePart("Tools");
		GuiTreePart tapeMeasure = new GuiTreePart("Little Tape Measure");
		toolsAlet.addMenu(tapeMeasure);
		
		listOfMenus.add(welcome);
		listOfMenus.add(drawshape.addMenu(box).addMenu(slices).addMenu(polygon).addMenu(wall).addMenu(pillar).addMenu(curves).addMenu(cylinder).addMenu(pyramid).addMenu(tile).addMenu(type).addMenu(connected));
		listOfMenus.add(tools.addMenu(chisel).addMenu(hammer).addMenu(glove).addMenu(paint).addMenu(saw).addMenu(screwdriver).addMenu(wrench));
		listOfMenus.add(block);
		listOfMenus.add(control);
		listOfMenus.add(welcomeAlet);
		listOfMenus.add(drawshapeAlet);
		listOfMenus.add(blockAlet);
		listOfMenus.add(toolsAlet);
		scrollBox.controls.add(new GuiTree("tree", 0, 0, 194, listOfMenus));
		
	}
	
	@CustomEventSubscribe
	public void changed(GuiControlChangedEvent event) {
		if (event.source instanceof GuiTreePart) {
			GuiColorablePanel panel = (GuiColorablePanel) this.get("tableRight");
			GuiTextBox textBox = (GuiTextBox) panel.get("wiki");
			GuiTreePart part = (GuiTreePart) event.source;
			
			if (part.CAPTION.equals("Welcome to Little Tiles")) {
				textBox.text = "{size:3}Welcome to LittleTiles!{end}{size:2}\nAbout The Mod:\n\n\n {end}"
				        + "     What this mod allows you to do is to create detailed builds that'd you never be able to do in vanilia Minecraft. "
				        + "By default you are able to place tiles that are 1/32nd the size of a block. That is a quarter of the size of pixel on a "
				        + "Minecraft block. Not only area able to build with such detail, but the structures you build can be made to more than a"
				        + "static object. You can make turn them into a door, ladder, bed, chair, a light and multiply other options.";
			}
		}
		
	}
	
}
