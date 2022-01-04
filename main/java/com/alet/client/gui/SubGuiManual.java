package com.alet.client.gui;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.Color;

import com.alet.client.gui.controls.GuiColorablePanel;
import com.alet.client.gui.controls.GuiModifibleTextBox;
import com.alet.client.gui.controls.GuiModifibleTextBox.ModifierAttribute;
import com.alet.client.gui.controls.GuiScalableTextBox;
import com.alet.client.gui.controls.GuiTree;
import com.alet.client.gui.controls.GuiTreePart;
import com.alet.client.gui.controls.GuiTreePart.EnumPartType;
import com.creativemd.creativecore.common.gui.CoreControl;
import com.creativemd.creativecore.common.gui.container.SubGui;
import com.creativemd.creativecore.common.gui.controls.gui.GuiScrollBox;
import com.creativemd.creativecore.common.gui.event.gui.GuiControlChangedEvent;
import com.n247s.api.eventapi.eventsystem.CustomEventSubscribe;

import net.minecraft.util.text.TextFormatting;

public class SubGuiManual extends SubGui {
	
	GuiScrollBox scrollBoxLeft;
	GuiTreePart welcome = new GuiTreePart("Welcome to Little Tiles", EnumPartType.Title);
	
	GuiTreePart drawshape = new GuiTreePart("Draw Shapes", EnumPartType.Branch);
	GuiTreePart box = new GuiTreePart("Box", EnumPartType.Leaf);
	GuiTreePart slices = new GuiTreePart("Slices", EnumPartType.Leaf);
	GuiTreePart polygon = new GuiTreePart("Polygon", EnumPartType.Leaf);
	GuiTreePart wall = new GuiTreePart("Wall", EnumPartType.Leaf);
	GuiTreePart pillar = new GuiTreePart("Pillar", EnumPartType.Leaf);
	GuiTreePart curves = new GuiTreePart("Curves", EnumPartType.Leaf);
	GuiTreePart cylinder = new GuiTreePart("Cylinder", EnumPartType.Leaf);
	GuiTreePart sphere = new GuiTreePart("Sphere", EnumPartType.Leaf);
	GuiTreePart pyramid = new GuiTreePart("Pyramid", EnumPartType.Leaf);
	GuiTreePart tile = new GuiTreePart("Tile", EnumPartType.Leaf);
	GuiTreePart type = new GuiTreePart("Type", EnumPartType.Leaf);
	GuiTreePart connected = new GuiTreePart("Connected", EnumPartType.Leaf);
	
	GuiTreePart tools = new GuiTreePart("Tools", EnumPartType.Branch);
	GuiTreePart chisel = new GuiTreePart("Little Chisel", EnumPartType.Leaf);
	GuiTreePart hammer = new GuiTreePart("Little Hammer", EnumPartType.Leaf);
	GuiTreePart glove = new GuiTreePart("Little Glove", EnumPartType.Leaf);
	GuiTreePart paint = new GuiTreePart("Little Paint Brush", EnumPartType.Leaf);
	GuiTreePart saw = new GuiTreePart("Little Saw", EnumPartType.Leaf);
	GuiTreePart screwdriver = new GuiTreePart("Little Screwdriver", EnumPartType.Leaf);
	GuiTreePart wrench = new GuiTreePart("Little Wrench", EnumPartType.Leaf);
	
	GuiTreePart control = new GuiTreePart("Controls", EnumPartType.Branch);
	GuiTreePart mark = new GuiTreePart("Mark Mode", EnumPartType.Leaf);
	GuiTreePart openConfig = new GuiTreePart("Open Configuration Menu", EnumPartType.Leaf);
	GuiTreePart openConfigAdv = new GuiTreePart("Open Mode/Grid Menu", EnumPartType.Leaf);
	GuiTreePart undo = new GuiTreePart("Undo/Redo", EnumPartType.Leaf);
	GuiTreePart movement = new GuiTreePart("Moving A Structure", EnumPartType.Leaf);
	
	GuiTreePart block = new GuiTreePart("Blocks", EnumPartType.Branch);
	GuiTreePart importer = new GuiTreePart("Little Importer", EnumPartType.Leaf);
	GuiTreePart exporter = new GuiTreePart("Little Exporter", EnumPartType.Leaf);
	GuiTreePart workbench = new GuiTreePart("Little Workbench", EnumPartType.Leaf);
	GuiTreePart partical = new GuiTreePart("Particle Emitter", EnumPartType.Leaf);
	GuiTreePart blankMatic = new GuiTreePart("Blank-o-matic", EnumPartType.Leaf);
	GuiTreePart structureBuilder = new GuiTreePart("Structure Builder", EnumPartType.Leaf);
	
	GuiTreePart config = new GuiTreePart("Configuration", EnumPartType.Leaf);
	
	GuiTreePart welcomeAlet = new GuiTreePart("Welcome To A Little Extra Tiles", EnumPartType.Title);
	GuiTreePart drawshapeAlet = new GuiTreePart("Draw Shapes", EnumPartType.Branch);
	GuiTreePart magicWandAlet = new GuiTreePart("Magic Wand", EnumPartType.Leaf);
	GuiTreePart centeredAlet = new GuiTreePart("Centered Draw Shapes", EnumPartType.Leaf);
	
	GuiTreePart blockAlet = new GuiTreePart("Blocks", EnumPartType.Branch);
	GuiTreePart typewriterAlet = new GuiTreePart("Typewriter", EnumPartType.Leaf);
	GuiTreePart photoAlet = new GuiTreePart("Photo Importer", EnumPartType.Leaf);
	GuiTreePart fillingAlet = new GuiTreePart("Filling Cabinet", EnumPartType.Leaf);
	
	GuiTreePart toolsAlet = new GuiTreePart("Tools", EnumPartType.Branch);
	GuiTreePart tapeMeasureAlet = new GuiTreePart("Little Tape Measure", EnumPartType.Leaf);
	GuiTreePart littleRopeAlet = new GuiTreePart("Little Rope", EnumPartType.Leaf);
	
	GuiTree tree;
	
	public SubGuiManual() {
		super(600, 400);
	}
	
	@Override
	public void createControls() {
		GuiColorablePanel panelLeft = new GuiColorablePanel("tableLeft", 0, 0, 200, 394, new Color(0, 0, 0), new Color(198, 198, 198));
		GuiColorablePanel panelRight = new GuiColorablePanel("tableRight", 209, 0, 385, 394, new Color(0, 0, 0), new Color(198, 198, 198));
		scrollBoxLeft = new GuiScrollBox("scrollBoxLeft", 0, 0, 379, 388);
		scrollBoxLeft.setStyle(defaultStyle);
		panelRight.addControl(scrollBoxLeft);
		GuiScrollBox scrollBox = new GuiScrollBox("scrollBox", 0, 0, 194, 388);
		scrollBox.setStyle(defaultStyle);
		controls.add(panelLeft);
		controls.add(panelRight);
		panelLeft.controls.add(scrollBox);
		
		List<GuiTreePart> listOfMenus = new ArrayList<GuiTreePart>();
		drawshape.addMenu(box).addMenu(slices).addMenu(polygon).addMenu(wall).addMenu(pillar).addMenu(curves).addMenu(cylinder).addMenu(sphere).addMenu(pyramid).addMenu(tile).addMenu(type).addMenu(connected);
		box.setSearchKeywords("Draw Shape");
		slices.setSearchKeywords("Draw Shape");
		polygon.setSearchKeywords("Draw Shape");
		wall.setSearchKeywords("Draw Shape");
		pillar.setSearchKeywords("Draw Shape");
		curves.setSearchKeywords("Draw Shape");
		cylinder.setSearchKeywords("Draw Shape");
		sphere.setSearchKeywords("Draw Shape");
		pyramid.setSearchKeywords("Draw Shape");
		tile.setSearchKeywords("Draw Shape");
		type.setSearchKeywords("Draw Shape");
		connected.setSearchKeywords("Draw Shape");
		
		control.addMenu(mark).addMenu(openConfig).addMenu(openConfigAdv).addMenu(undo).addMenu(movement);
		
		block.addMenu(importer).addMenu(exporter).addMenu(workbench).addMenu(partical).addMenu(blankMatic).addMenu(structureBuilder);
		
		drawshapeAlet.addMenu(centeredAlet).addMenu(magicWandAlet);
		
		blockAlet.addMenu(typewriterAlet).addMenu(photoAlet).addMenu(fillingAlet);
		
		toolsAlet.addMenu(tapeMeasureAlet).addMenu(littleRopeAlet);
		
		tools.addMenu(chisel).addMenu(hammer).addMenu(glove).addMenu(paint).addMenu(saw).addMenu(screwdriver).addMenu(wrench);
		
		listOfMenus.add(welcome.addMenu(drawshape).addMenu(tools).addMenu(block).addMenu(control).addMenu(config));
		listOfMenus.add(welcomeAlet.addMenu(drawshapeAlet).addMenu(blockAlet).addMenu(toolsAlet));
		tree = new GuiTree("tree", 0, 0, 194, listOfMenus, true, 0, 0, 50);
		scrollBox.controls.add(tree);
		
	}
	
	@CustomEventSubscribe
	public void changed(GuiControlChangedEvent event) {
		if (event.source instanceof GuiTreePart) {
			GuiTreePart part = (GuiTreePart) event.source;
			if (part.type.equals(EnumPartType.Leaf) || part.type.equals(EnumPartType.Searched)
			        || part.type.equals(EnumPartType.Title) || part.type.equals(EnumPartType.Root))
				updateMessage(part.CAPTION);
		}
	}
	
	public void updateMessage(String caption) {
		int i = 0;
		while (i < scrollBoxLeft.controls.size()) {
			if (scrollBoxLeft.controls.get(i) instanceof CoreControl)
				scrollBoxLeft.controls.remove(i);
			else
				i++;
		}
		if (caption.equals("Welcome to Little Tiles"))
			getWelcomeLTMsg();
		if (caption.equals("Welcome To A Little Extra Tiles"))
			getWelcomeAletMsg();
		if (caption.equals("Magic Wand"))
			getMagicWandMsg();
		if (caption.equals("Centered Box"))
			getCenteredBoxMsg();
		if (caption.equals("Centered Cylinder"))
			getCenteredCylinderMsg();
		if (caption.equals("Centered Sphere"))
			getCenteredSphereMsg();
		if (caption.equals("Typewriter"))
			getTypewriterMsg();
		if (caption.equals("Photo Importer"))
			getPhotoImporterMsg();
		if (caption.equals("Filling Cabinet"))
			getFillingCabinetMsg();
		if (caption.equals("Little Tape Measure"))
			getTapeMeasureMsg();
		if (caption.equals("Little Rope"))
			getLittleRopeMsg();
		if (caption.equals("Configuration"))
			getGridSizeMsg();
	}
	
	public void getWelcomeLTMsg() {/*
	                               scrollBoxLeft.addControl(new GuiScalableTextBox("", TextFormatting.BOLD
	                               + "Welcome To LittleTiles", 0, 0, 371, 1.5));
	                               scrollBoxLeft.addControl(new GuiScalableTextBox("", "    LittleTiles is a mod for anyone who wants to push the limit of building in Minecraft."
	                               + " This mod allows you to build with what are called " + TextFormatting.AQUA + TextFormatting.ITALIC
	                               + "Tiles" + TextFormatting.RESET
	                               + ". Tiles are smaller than a vanilia Minecraft block, with the default minimal"
	                               + " size being 1/32nd the size of a vanilia Minecraft block. You can place, remove, and modify tiles in several different sizes called "
	                               + TextFormatting.AQUA + TextFormatting.ITALIC + "Grid Sizes" + TextFormatting.RESET
	                               + ". By default the grid sizes avaliable for you to use are as follows: 1, 2, 4, 8, 16, and 32."
	                               + " You can increase the smallest tile you can use in the configuration file. To learn how go to "
	                               + TextFormatting.ITALIC + TextFormatting.UNDERLINE + "Configuration" + TextFormatting.RESET
	                               + ", under LittleTiles.", 0, 25, 371, 1));
	                               GuiButton button = (new GuiButton("", 193, 103, 63, 7) {
	                               @Override
	                               public boolean hasBorder() {
	                               return false;
	                               }
	                               
	                               @Override
	                               public void onClicked(int x, int y, int button) {
	                               updateMessage("Configuration");
	                               tree.openTo(config);
	                               tree.highlightPart(config);
	                               }
	                               
	                               });
	                               
	                               button.setStyle(new Style("default", new ColoredDisplayStyle(0, 0, 0), new ColoredDisplayStyle(90, 90, 90), new ColoredDisplayStyle(140, 140, 140), new ColoredDisplayStyle(198, 198, 198), new ColoredDisplayStyle(0, 0, 0, 100)));
	                               scrollBoxLeft.addControl(button);*/
		scrollBoxLeft.addControl(new GuiModifibleTextBox("t", ModifierAttribute.scale(2) + "Welcome To LittleTiles"
		        + ModifierAttribute.end() + ModifierAttribute.newLines(1) + "" + ModifierAttribute.end()
		        + ModifierAttribute.clickable() + ModifierAttribute.scale(1) + ModifierAttribute.newLines(1)
		        + "    LittleTiles is a mod for anyone who wants to push the limit of building in Minecraft. This mod allows you to build with what are called "
		        + ModifierAttribute.end() + TextFormatting.ITALIC + ModifierAttribute.scale(1.1)
		        + ModifierAttribute.color(0x00FFFF) + "  Tiles" + ModifierAttribute.end() + ModifierAttribute.scale(1)
		        + ". Tiles are smaller than a vanilia Minecraft block, with the default minimal size being 1/32nd the size of a vanilia Minecraft block. You can place, remove, and modify tiles in several different sizes called "
		        + ModifierAttribute.end() + TextFormatting.ITALIC + ModifierAttribute.scale(1.1)
		        + ModifierAttribute.color(0x00FFFF) + "Grid Size" + ModifierAttribute.end() + ModifierAttribute.scale(1)
		        + ". By default the grid sizes avaliable for you to use are as follows: 1, 2, 4, 8, 16,"
		        + " and 32. You can increase the smallest tile you can use in the configuration file. To learn how go to "
		        + ModifierAttribute.end() + TextFormatting.ITALIC + TextFormatting.UNDERLINE
		        + ModifierAttribute.scale(1) + "Configuration" + ModifierAttribute.end() + ModifierAttribute.scale(1)
		        + ", under LittleTiles." + ModifierAttribute.end(), 0, 0, 350));
	}
	
	public void getWelcomeAletMsg() {
	}
	
	public void getMagicWandMsg() {
	}
	
	public void getCenteredBoxMsg() {
	}
	
	public void getCenteredCylinderMsg() {
	}
	
	public void getCenteredSphereMsg() {
	}
	
	public void getTypewriterMsg() {
	}
	
	public void getPhotoImporterMsg() {
	}
	
	public void getFillingCabinetMsg() {
	}
	
	public void getTapeMeasureMsg() {
	}
	
	public void getLittleRopeMsg() {
	}
	
	public void getGridSizeMsg() {
		scrollBoxLeft.addControl(new GuiScalableTextBox("", TextFormatting.BOLD + "Configuration", 0, 0, 371, 1.5));
		scrollBoxLeft.addControl(new GuiScalableTextBox("", "   ", 0, 25, 371, 1));
	}
}
