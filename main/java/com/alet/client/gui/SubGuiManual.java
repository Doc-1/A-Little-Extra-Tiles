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

import net.minecraft.util.text.TextFormatting;

public class SubGuiManual extends SubGui {
	
	public SubGuiManual() {
		super(600, 400);
	}
	
	@Override
	public void createControls() {
		GuiColorablePanel panelLeft = new GuiColorablePanel("tableLeft", 0, 0, 200, 394, new Color(0, 0, 0), new Color(198, 198, 198));
		GuiColorablePanel panelRight = new GuiColorablePanel("tableRight", 209, 0, 385, 394, new Color(0, 0, 0), new Color(198, 198, 198));
		GuiScrollBox scrollBoxLeft = new GuiScrollBox("scrollBoxLeft", 0, 0, 379, 388);
		GuiConfigurableTextBox textBox = new GuiConfigurableTextBox("wiki", getWelcomeLTMsg(), 0, 0, 379);
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
		GuiTreePart centeredAlet = new GuiTreePart("Centered Draw Shapes");
		GuiTreePart centeredBox = new GuiTreePart("Centered Box");
		GuiTreePart centeredCylinder = new GuiTreePart("Centered Cylinder");
		GuiTreePart centeredSphere = new GuiTreePart("Centered Sphere");
		centeredAlet.addMenu(centeredBox).addMenu(centeredCylinder).addMenu(centeredSphere);
		drawshapeAlet.addMenu(centeredAlet).addMenu(magicWandAlet);
		
		GuiTreePart blockAlet = new GuiTreePart("Blocks");
		GuiTreePart typewriterAlet = new GuiTreePart("Typewriter");
		GuiTreePart photoAlet = new GuiTreePart("Photo Importer");
		GuiTreePart fillingAlet = new GuiTreePart("Filling Cabinet");
		blockAlet.addMenu(typewriterAlet).addMenu(photoAlet).addMenu(fillingAlet);
		
		GuiTreePart toolsAlet = new GuiTreePart("Tools");
		GuiTreePart tapeMeasureAlet = new GuiTreePart("Little Tape Measure");
		GuiTreePart littleRopeAlet = new GuiTreePart("Little Rope");
		toolsAlet.addMenu(tapeMeasureAlet).addMenu(littleRopeAlet);
		
		listOfMenus.add(welcome);
		listOfMenus.add(drawshape.addMenu(box).addMenu(slices).addMenu(polygon).addMenu(wall).addMenu(pillar).addMenu(curves).addMenu(cylinder).addMenu(pyramid).addMenu(tile).addMenu(type).addMenu(connected));
		listOfMenus.add(tools.addMenu(chisel).addMenu(hammer).addMenu(glove).addMenu(paint).addMenu(saw).addMenu(screwdriver).addMenu(wrench));
		listOfMenus.add(block);
		listOfMenus.add(control);
		listOfMenus.add(welcomeAlet);
		listOfMenus.add(drawshapeAlet);
		listOfMenus.add(blockAlet);
		listOfMenus.add(toolsAlet);
		scrollBox.controls.add(new GuiTree("tree", 0, 0, 194, listOfMenus, true, 0, 0, 50));
		
	}
	
	@CustomEventSubscribe
	public void changed(GuiControlChangedEvent event) {
		if (event.source instanceof GuiTreePart) {
			GuiColorablePanel panel = (GuiColorablePanel) this.get("tableRight");
			GuiTextBox textBox = (GuiTextBox) panel.get("wiki");
			GuiTreePart part = (GuiTreePart) event.source;
			
			if (part.CAPTION.equals("Welcome to Little Tiles"))
				textBox.text = getWelcomeLTMsg();
			if (part.CAPTION.equals("Welcome To A Little Extra Tiles"))
				textBox.text = getWelcomeAletMsg();
			if (part.CAPTION.equals("Magic Wand"))
				textBox.text = getMagicWandMsg();
			if (part.CAPTION.equals("Centered Box"))
				textBox.text = getCenteredBoxMsg();
			if (part.CAPTION.equals("Centered Cylinder"))
				textBox.text = getCenteredCylinderMsg();
			if (part.CAPTION.equals("Centered Sphere"))
				textBox.text = getCenteredSphereMsg();
			if (part.CAPTION.equals("Typewriter"))
				textBox.text = getTypewriterMsg();
			if (part.CAPTION.equals("Photo Importer"))
				textBox.text = getPhotoImporterMsg();
			if (part.CAPTION.equals("Filling Cabinet"))
				textBox.text = getFillingCabinetMsg();
			if (part.CAPTION.equals("Little Tape Measure"))
				textBox.text = getTapeMeasureMsg();
			if (part.CAPTION.equals("Little Rope"))
				textBox.text = getLittleRopeMsg();
		}
	}
	
	public String getWelcomeLTMsg() {
		return "{size:3}LittleTiles{end}{size:2}\nAbout The Mod:\n\n\n {end}"
		        + "     What this mod allows you to do is to create detailed builds that'd you never be able to do in vanilia Minecraft. "
		        + "By default you are able to place tiles that are 1/32nd the size of a block. That is a quarter of the size of pixel on a "
		        + "Minecraft block. Not only area able to build with such detail, but the structures you build can be made to more than a"
		        + "static object. You can make turn them into a door, ladder, bed, chair, a light and multiply other options.";
	}
	
	public String getWelcomeAletMsg() {
		return "{size:3}A Little Extra Tiles{end}{size:2}\nAbout The Mod:\n\n\n {end}"
		        + "    This is a addon for LittleTiles. It adds new structures, draw shapes, and several Quality of Life improvements."
		        + "";
	}
	
	public String getMagicWandMsg() {
		return "{size:2}Magic Wand{end}\n\n\n    This draw shape selects tiles that are of a similiar color as the one you are looking at."
		        + " On this draw shape there is a setting called " + TextFormatting.ITALIC + TextFormatting.BOLD
		        + "Tolerance" + TextFormatting.RESET + ". This setting changes how similiar a color has to be for"
		        + " it to select a tile. At 0 it will only select colors that are the same as the one you are looking at. At 255 it will select all colors.";
	}
	
	public String getCenteredBoxMsg() {
		return "{size:2}Centered Box{end}\n\n\n    This draw shape is functions the same as the default box shape. However, it remains centered on the first"
		        + "possition you have selected. The setting " + TextFormatting.ITALIC + TextFormatting.BOLD
		        + "Double Centered" + TextFormatting.RESET + " will"
		        + " make the center be two tiles wide. Rather than one by default.";
	}
	
	public String getCenteredCylinderMsg() {
		return "{size:2}Centered Cylinder{end}\n\n\n    This draw shape is functions the same as the default cylinder shape. However, it remains centered on the first"
		        + "possition you have selected. The setting " + TextFormatting.ITALIC + TextFormatting.BOLD
		        + "Double Centered" + TextFormatting.RESET + " will"
		        + " make the center be two tiles wide. Rather than one by default.";
	}
	
	public String getCenteredSphereMsg() {
		return "{size:2}Centered Sphere{end}\n\n\n    This draw shape is functions the same as the default sphere shape. However, it remains centered on the first"
		        + "possition you have selected. The setting " + TextFormatting.ITALIC + TextFormatting.BOLD
		        + "Double Centered" + TextFormatting.RESET + " will"
		        + " make the center be two tiles wide. Rather than one by default.";
	}
	
	public String getTypewriterMsg() {
		return "{size:2}Typewriter{end}\n\n\n    The typewriter allows you to enter text to be printed out as tiles on a blueprint. You can change"
		        + " the text's color, font type, font size, grid size, and rotation. It also has a view to see a preview of what it will look like"
		        + " when printed.";
	}
	
	public String getPhotoImporterMsg() {
		return "{size:2}Photo Importer{end}{size:1}    The photo importer allows you to print out an image as tiles on a blueprint. You can print out"
		        + " a image from your computer, a website, a block, or an item.{end}{size:2}Settings{end}  -Keep Aspect Ratio: ";
	}
	
	public String getFillingCabinetMsg() {
		return "{size:2}Filling Cabinet{end}\n\n\n    ";
	}
	
	public String getTapeMeasureMsg() {
		return "{size:3}";
	}
	
	public String getLittleRopeMsg() {
		return "{size:3}";
	}
	
}
