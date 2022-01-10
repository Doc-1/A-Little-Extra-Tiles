package com.alet.client.gui;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.Color;

import com.alet.client.gui.controls.GuiColorablePanel;
import com.alet.client.gui.controls.GuiGIF;
import com.alet.client.gui.controls.GuiModifibleTextBox;
import com.alet.client.gui.controls.GuiModifibleTextBox.ModifierAttribute;
import com.alet.client.gui.controls.GuiScalableTextBox;
import com.alet.client.gui.controls.GuiTable;
import com.alet.client.gui.controls.GuiTree;
import com.alet.client.gui.controls.GuiTreePart;
import com.alet.client.gui.controls.GuiTreePart.EnumPartType;
import com.creativemd.creativecore.common.gui.CoreControl;
import com.creativemd.creativecore.common.gui.container.SubGui;
import com.creativemd.creativecore.common.gui.controls.gui.GuiScrollBox;
import com.creativemd.creativecore.common.gui.event.gui.GuiControlChangedEvent;
import com.creativemd.creativecore.common.utils.mc.ColorUtils;
import com.n247s.api.eventapi.eventsystem.CustomEventSubscribe;

import net.minecraft.util.text.TextFormatting;

public class SubGuiManual extends SubGui {
	
	String selected = "";
	
	GuiScrollBox scrollBoxLeft;
	GuiTreePart welcome = new GuiTreePart("Welcome to LittleTiles", EnumPartType.Title);
	
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
	
	GuiTreePart signaling = new GuiTreePart("Signaling", EnumPartType.Branch);
	GuiTreePart gates = new GuiTreePart("Logic Gates", EnumPartType.Branch);
	GuiTreePart and = new GuiTreePart("and", EnumPartType.Leaf);
	GuiTreePart nand = new GuiTreePart("nand", EnumPartType.Leaf);
	GuiTreePart or = new GuiTreePart("or", EnumPartType.Leaf);
	GuiTreePart xor = new GuiTreePart("xor", EnumPartType.Leaf);
	GuiTreePart not = new GuiTreePart("not", EnumPartType.Leaf);
	
	GuiTreePart math = new GuiTreePart("Math Operators", EnumPartType.Branch);
	GuiTreePart add = new GuiTreePart("add", EnumPartType.Leaf);
	GuiTreePart sub = new GuiTreePart("sub", EnumPartType.Leaf);
	GuiTreePart multi = new GuiTreePart("multi", EnumPartType.Leaf);
	GuiTreePart div = new GuiTreePart("div", EnumPartType.Leaf);
	
	GuiTreePart structuresSingal = new GuiTreePart("Structures", EnumPartType.Branch);
	GuiTreePart storage = new GuiTreePart("Storage", EnumPartType.Leaf);
	GuiTreePart noclip = new GuiTreePart("No-Clip", EnumPartType.Leaf);
	GuiTreePart bed = new GuiTreePart("Bed", EnumPartType.Leaf);
	GuiTreePart chair = new GuiTreePart("Chair", EnumPartType.Leaf);
	GuiTreePart door = new GuiTreePart("Doors", EnumPartType.Leaf);
	GuiTreePart message = new GuiTreePart("Message", EnumPartType.Leaf);
	GuiTreePart light = new GuiTreePart("Light", EnumPartType.Leaf);
	
	GuiTreePart example = new GuiTreePart("Signal Examples", EnumPartType.Branch);
	GuiTreePart button = new GuiTreePart("Button", EnumPartType.Leaf);
	GuiTreePart lever = new GuiTreePart("Lever", EnumPartType.Leaf);
	GuiTreePart pressure = new GuiTreePart("Pressure Plate", EnumPartType.Leaf);
	
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
		
		gates.addMenu(and).addMenu(nand).addMenu(or).addMenu(xor).addMenu(not);
		
		math.addMenu(add).addMenu(sub).addMenu(div).addMenu(multi);
		
		structuresSingal.addMenu(bed).addMenu(chair).addMenu(storage).addMenu(noclip).addMenu(light).addMenu(message).addMenu(door);
		
		signaling.addMenu(gates).addMenu(math).addMenu(structuresSingal);
		
		toolsAlet.addMenu(tapeMeasureAlet).addMenu(littleRopeAlet);
		
		tools.addMenu(chisel).addMenu(hammer).addMenu(glove).addMenu(paint).addMenu(saw).addMenu(screwdriver).addMenu(wrench);
		
		listOfMenus.add(welcome.addMenu(drawshape).addMenu(tools).addMenu(block).addMenu(control).addMenu(signaling).addMenu(config));
		listOfMenus.add(welcomeAlet.addMenu(drawshapeAlet).addMenu(blockAlet).addMenu(toolsAlet));
		tree = new GuiTree("tree", 0, 0, 194, listOfMenus, true, 0, 0, 50);
		scrollBox.controls.add(tree);
		
	}
	
	@CustomEventSubscribe
	public void changed(GuiControlChangedEvent event) {
		if (event.source instanceof GuiTreePart) {
			GuiTreePart part = (GuiTreePart) event.source;
			updateMessage(part.CAPTION, part.getBranchIDThisIsIn());
		}
	}
	
	public void updateMessage(String caption, int id) {
		if (!this.selected.equals(caption)) {
			int i = 0;
			while (i < scrollBoxLeft.controls.size()) {
				if (scrollBoxLeft.controls.get(i) instanceof CoreControl) {
					if (scrollBoxLeft.controls.get(i) instanceof GuiGIF)
						((GuiGIF) scrollBoxLeft.controls.get(i)).onClosed();
					scrollBoxLeft.controls.remove(i);
				} else
					i++;
			}
			System.out.println(caption + " " + id);
			if (caption.equals("Welcome to LittleTiles"))
				getWelcomeLTMsg();
			if (caption.equals("Draw Shapes"))
				getDrawShape();
			if (caption.equals("Box"))
				getDrawShapeBox();
			if (caption.equals("Slices"))
				getDrawShapeSlice();
			if (caption.equals("and"))
				getAndMsg();
			if (caption.equals("nand"))
				getNandMsg();
			if (caption.equals("or"))
				getOrMsg();
			if (caption.equals("xor"))
				getXorMsg();
			if (caption.equals("not"))
				getNotMsg();
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
		this.selected = caption;
	}
	
	public void getWelcomeLTMsg() {
		scrollBoxLeft.addControl(new GuiModifibleTextBox("t", ModifierAttribute.scale(2) + ModifierAttribute.bold()
		        + "Welcome To LittleTiles" + ModifierAttribute.end() + ModifierAttribute.scale(1)
		        + ModifierAttribute.newLines(2)
		        + "    LittleTiles is a mod for anyone who wants to push the limit of building in Minecraft. This mod allows you to build with what are called "
		        + ModifierAttribute.end() + ModifierAttribute.scale(1.1) + ModifierAttribute.italic()
		        + ModifierAttribute.color(0x00FFFF) + "  Tiles" + ModifierAttribute.end() + ModifierAttribute.scale(1)
		        + ". Tiles are smaller than a vanilia Minecraft block, with the default minimal size being 1/32nd the size of a vanilia Minecraft block. You can place, remove, and modify tiles in several different sizes called "
		        + ModifierAttribute.end() + ModifierAttribute.scale(1.1) + ModifierAttribute.italic()
		        + ModifierAttribute.color(0x00FFFF) + "Grid Size" + ModifierAttribute.end() + ModifierAttribute.scale(1)
		        + ". By default the grid sizes avaliable for you to use are as follows: 1, 2, 4, 8, 16,"
		        + " and 32. You can increase the smallest tile you can use in the configuration file. To learn how go to "
		        + ModifierAttribute.end() + ModifierAttribute.italic() + ModifierAttribute.underline()
		        + ModifierAttribute.scale(1) + ModifierAttribute.clickable() + "Configuration" + ModifierAttribute.end()
		        + ModifierAttribute.scale(1) + ", under LittleTiles." + ModifierAttribute.end(), 0, 0, 350) {
			
			@Override
			public void clickedOn(String text) {
				if (text.contains("Configuration")) {
					raiseEvent(new GuiControlChangedEvent(config));
					tree.highlightPart(config);
				}
			}
		});
	}
	
	public void getWelcomeAletMsg() {
	}
	
	public void getDrawShape() {
		scrollBoxLeft.addControl(new GuiModifibleTextBox("", ModifierAttribute.addText(2, ColorUtils.WHITE, false, 0, "Draw Shapes", false, true, false)
		        + ModifierAttribute.addText(1, ColorUtils.WHITE, false, 2, "    A ", false, false, false)
		        + ModifierAttribute.addText(1.1, 0x00FFFF, false, 0, "Draw Shape", true, false, false)
		        + ModifierAttribute.addText(1, ColorUtils.WHITE, false, 0, ", is the type of shape that the little tools can use. The draw shape can remove,add or edit tiles, depending on the tool and placement mode that is used.", false, false, false), 0, 0, 350));
	}
	
	public void getDrawShapeBox() {
		scrollBoxLeft.addControl(new GuiModifibleTextBox("", ModifierAttribute.addText(2, ColorUtils.WHITE, false, 0, "Draw Shape: Box", false, true, false)
		        + ModifierAttribute.addText(1, ColorUtils.WHITE, false, 2, "    The Box draw shape is a simple shape. As the name implies this draw shape allows you draw a squares, rectangles, or cubes. To draw with this shape right click to set point A then right click again to set point B. This will then place the cube.", false, false, false)
		        + ModifierAttribute.addText(1, ColorUtils.WHITE, false, 1, "    There are two settings with the box draw shape, Hallow and Thinkness. Hallow lets you place a cube with a hallow center and thickness is how thick the walls will be. The thickness is based off of the grid size. If you are using grid size 32 than thickness of 16 would mean the wall is 16/32 or 1/2 the size of block.", false, false, false), 0, 0, 350));
		scrollBoxLeft.addControl(new GuiGIF("", "assets/alet/gif/box.gif", 0, 110, 1));
	}
	
	public void getDrawShapeSlice() {
		scrollBoxLeft.addControl(new GuiModifibleTextBox("", ModifierAttribute.addText(2, ColorUtils.WHITE, false, 0, "Draw Shape: Slice", false, true, false)
		        + ModifierAttribute.addText(1, ColorUtils.WHITE, false, 2, "    The Slice draw shape allows you to draw a slice or slope."
		                + " To draw with this shape right click to set point A then right click again to set point B. This will then place the slice. You can use the arrow keys to change the"
		                + " facing of the slope.", false, false, false), 0, 0, 350));
		scrollBoxLeft.addControl(new GuiGIF("", "assets/alet/gif/slice.gif", 0, 65, 1));
		scrollBoxLeft.addControl(new GuiModifibleTextBox("", ModifierAttribute.addText(2, ColorUtils.WHITE, false, 0, "Draw Shape: Slice Corner", false, true, false)
		        + ModifierAttribute.addText(1, ColorUtils.WHITE, false, 2, "    The Corner Slices draw shape allows you to draw a slice or sloped corner."
		                + " To draw with this shape right click to set point A then right click again to set point B. This will then place the corner slice. You can use the arrow keys to change the"
		                + " facing of the slope.", false, false, false)
		        + ModifierAttribute.addText(1, ColorUtils.WHITE, false, 1, "    There is a setting for both Inner and Outer Corner Slice called second-type that will switch it to an alternate "
		                + "corner slope. The type of corner you want to use will depend on the shape you are going for.", false, false, false), 0, 255, 350));
		scrollBoxLeft.addControl(new GuiGIF("", "assets/alet/gif/sliceIn.gif", 0, 348, 1));
		scrollBoxLeft.addControl(new GuiGIF("", "assets/alet/gif/sliceOut.gif", 0, 535, 1));
	}
	
	public void getAndMsg() {
		scrollBoxLeft.addControl(new GuiModifibleTextBox("", ModifierAttribute.addText(2, ColorUtils.WHITE, false, 0, "Logic Gate: And", false, true, false)
		        + ModifierAttribute.addText(1, ColorUtils.WHITE, false, 2, "    The logic gate, And, compares two values to see if they are equal. For example, if"
		                + " you have one signal with the value of 1 and another with the value of 1 then the And gate will return with true. However, if one of those"
		                + " signals is 0 and the other is 1 then the And gate will return false.", false, false, false), 0, 0, 350));
		scrollBoxLeft.addControl(new GuiTable("", "", 0, 100, 50, 15, 5, 3, false));
	}
	
	public void getNandMsg() {
	}
	
	public void getOrMsg() {
	}
	
	public void getXorMsg() {
	}
	
	public void getNotMsg() {
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
