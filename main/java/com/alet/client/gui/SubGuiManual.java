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
import com.alet.client.gui.controls.menu.GuiTree;
import com.alet.client.gui.controls.menu.GuiTreeManualPart;
import com.alet.client.gui.controls.menu.GuiTreePagePart;
import com.alet.client.gui.controls.menu.GuiTreePart;
import com.alet.client.gui.controls.menu.GuiTreePart.EnumPartType;
import com.creativemd.creativecore.common.gui.CoreControl;
import com.creativemd.creativecore.common.gui.container.SubGui;
import com.creativemd.creativecore.common.gui.controls.gui.GuiLabel;
import com.creativemd.creativecore.common.gui.controls.gui.GuiScrollBox;
import com.creativemd.creativecore.common.gui.event.gui.GuiControlChangedEvent;
import com.creativemd.creativecore.common.utils.mc.ColorUtils;
import com.n247s.api.eventapi.eventsystem.CustomEventSubscribe;

import net.minecraft.util.text.TextFormatting;

public class SubGuiManual extends SubGui {
    
    public GuiTreePart selected = new GuiTreePart("", EnumPartType.Leaf);
    //ModifierAttribute.addText(1, ColorUtils.WHITE, false, 0, "", false, false, false)
    public GuiScrollBox scrollBoxPage;
    private final int white = ColorUtils.WHITE;
    
    GuiTreePart welcome = (new GuiTreeManualPart("Welcome To LittleTiles & ALET", EnumPartType.Title) {
        @Override
        public void getPage() {}
    });
    GuiTreePart littleTile = (new GuiTreeManualPart("LittleTiles", EnumPartType.Leaf) {
        @Override
        public void getPage() {}
    });
    GuiTreePart alet = (new GuiTreeManualPart("A Little Extra Tiles", EnumPartType.Leaf) {
        @Override
        public void getPage() {}
    });
    GuiTreePart buildingWTile = (new GuiTreeManualPart("Building With Tiles", EnumPartType.Title) {
        @Override
        public void getPage() {}
    });
    GuiTreePart whatIsTile = (new GuiTreeManualPart("What is a Tile?", EnumPartType.Leaf) {
        @Override
        public void getPage() {}
    });
    GuiTreePart addModRemove = (new GuiTreeManualPart("Add, Modify, & Remove Tiles", EnumPartType.Branch) {
        @Override
        public void getPage() {}
    });
    GuiTreePart adding = (new GuiTreeManualPart("Adding Tiles", EnumPartType.Leaf) {
        @Override
        public void getPage() {}
    });
    GuiTreePart modifying = (new GuiTreeManualPart("Modfying Tiles", EnumPartType.Leaf) {
        @Override
        public void getPage() {}
    });
    GuiTreePart removing = (new GuiTreeManualPart("Removing Tiles", EnumPartType.Leaf) {
        @Override
        public void getPage() {}
    });
    GuiTreePart measuring = (new GuiTreeManualPart("Measuring Tiles", EnumPartType.Branch) {
        @Override
        public void getPage() {}
    });
    GuiTreePart tapeMeasure = (new GuiTreeManualPart("Little Tape Measure", EnumPartType.Leaf) {
        @Override
        public void getPage() {}
    });
    GuiTreePart measurements = (new GuiTreeManualPart("Measurement Shapes", EnumPartType.Branch) {
        @Override
        public void getPage() {}
    });
    GuiTreePart box = (new GuiTreeManualPart("Box", EnumPartType.Leaf) {
        @Override
        public void getPage() {}
    });
    GuiTreePart line = (new GuiTreeManualPart("Line", EnumPartType.Leaf) {
        @Override
        public void getPage() {}
    });
    GuiTreePart compass = (new GuiTreeManualPart("Compass", EnumPartType.Leaf) {
        @Override
        public void getPage() {}
    });
    GuiTreePart buildingTool = (new GuiTreeManualPart("Building Tool", EnumPartType.Branch) {
        @Override
        public void getPage() {}
    });
    GuiTreePart chisel = (new GuiTreeManualPart("Little Chisel", EnumPartType.Leaf) {
        @Override
        public void getPage() {}
    });
    GuiTreePart hammer = (new GuiTreeManualPart("Little Hammer", EnumPartType.Leaf) {
        @Override
        public void getPage() {}
    });
    GuiTreePart glove = (new GuiTreeManualPart("Little Glove", EnumPartType.Leaf) {
        @Override
        public void getPage() {}
    });
    GuiTreePart paintBrush = (new GuiTreeManualPart("Little Paint Brush", EnumPartType.Leaf) {
        @Override
        public void getPage() {}
    });
    GuiTreePart saw = (new GuiTreeManualPart("Little Saw", EnumPartType.Leaf) {
        @Override
        public void getPage() {}
    });
    GuiTreePart screwdriver = (new GuiTreeManualPart("Little Screwdriver", EnumPartType.Leaf) {
        @Override
        public void getPage() {}
    });
    GuiTreePart drawShape = (new GuiTreeManualPart("Draw Shapes", EnumPartType.Branch) {
        @Override
        public void getPage() {}
    });
    GuiTreePart dragBox = (new GuiTreeManualPart("Box", EnumPartType.Leaf) {
        @Override
        public void getPage() {}
    });
    GuiTreePart dragSlice = (new GuiTreeManualPart("Slice", EnumPartType.Leaf) {
        @Override
        public void getPage() {}
    });
    GuiTreePart dragPolygon = (new GuiTreeManualPart("Polygon", EnumPartType.Leaf) {
        @Override
        public void getPage() {}
    });
    GuiTreePart dragWall = (new GuiTreeManualPart("Wall", EnumPartType.Leaf) {
        @Override
        public void getPage() {}
    });
    GuiTreePart dragPillar = (new GuiTreeManualPart("Pillar", EnumPartType.Leaf) {
        @Override
        public void getPage() {}
    });
    GuiTreePart dragCurves = (new GuiTreeManualPart("Curves", EnumPartType.Leaf) {
        @Override
        public void getPage() {}
    });
    GuiTreePart dragCylinder = (new GuiTreeManualPart("Cylinder", EnumPartType.Leaf) {
        @Override
        public void getPage() {}
    });
    GuiTreePart dragSphere = (new GuiTreeManualPart("Shpere", EnumPartType.Leaf) {
        @Override
        public void getPage() {}
    });
    GuiTreePart dragPyramid = (new GuiTreeManualPart("Pyramid", EnumPartType.Leaf) {
        @Override
        public void getPage() {}
    });
    GuiTreePart dragTile = (new GuiTreeManualPart("Tile", EnumPartType.Leaf) {
        @Override
        public void getPage() {}
    });
    GuiTreePart dragType = (new GuiTreeManualPart("Type", EnumPartType.Leaf) {
        @Override
        public void getPage() {}
    });
    GuiTreePart dragConnected = (new GuiTreeManualPart("Connected", EnumPartType.Leaf) {
        @Override
        public void getPage() {}
    });
    GuiTreePart dragCetered = (new GuiTreeManualPart("Centered Drag Shapes", EnumPartType.Leaf) {
        @Override
        public void getPage() {}
    });
    GuiTreePart dragMagicWand = (new GuiTreeManualPart("Magic Wand", EnumPartType.Leaf) {
        @Override
        public void getPage() {}
    });
    GuiTreePart drawMode = (new GuiTreeManualPart("Draw Modes", EnumPartType.Branch) {
        @Override
        public void getPage() {}
    });
    GuiTreePart defaultMode = (new GuiTreeManualPart("Default", EnumPartType.Leaf) {
        @Override
        public void getPage() {}
    });
    GuiTreePart fillMode = (new GuiTreeManualPart("Fill", EnumPartType.Leaf) {
        @Override
        public void getPage() {}
    });
    GuiTreePart allMode = (new GuiTreeManualPart("All", EnumPartType.Leaf) {
        @Override
        public void getPage() {}
    });
    GuiTreePart overwriteMode = (new GuiTreeManualPart("Overwrite", EnumPartType.Leaf) {
        @Override
        public void getPage() {}
    });
    GuiTreePart overwriteAllMode = (new GuiTreeManualPart("Overwrite All", EnumPartType.Leaf) {
        @Override
        public void getPage() {}
    });
    GuiTreePart replaceMode = (new GuiTreeManualPart("Replace", EnumPartType.Leaf) {
        @Override
        public void getPage() {}
    });
    GuiTreePart stencilMode = (new GuiTreeManualPart("Stencil", EnumPartType.Leaf) {
        @Override
        public void getPage() {}
    });
    GuiTreePart colorizeMode = (new GuiTreeManualPart("Colorize", EnumPartType.Leaf) {
        @Override
        public void getPage() {}
    });
    GuiTreePart materialWhitelist = (new GuiTreeManualPart("Material Whitelist", EnumPartType.Leaf) {
        @Override
        public void getPage() {}
    });
    GuiTreePart usingStructure = (new GuiTreeManualPart("Using Structures", EnumPartType.Title) {
        @Override
        public void getPage() {}
    });
    GuiTreePart savingStructure = (new GuiTreeManualPart("Saving Structures", EnumPartType.Branch) {
        @Override
        public void getPage() {}
    });
    GuiTreePart blueprint = (new GuiTreeManualPart("Little Blueprint", EnumPartType.Leaf) {
        @Override
        public void getPage() {}
    });
    GuiTreePart staticStructure = (new GuiTreeManualPart("Static Structures", EnumPartType.Branch) {
        @Override
        public void getPage() {}
    });
    GuiTreePart fixedStructure = (new GuiTreeManualPart("Fixed", EnumPartType.Leaf) {
        @Override
        public void getPage() {}
    });
    GuiTreePart ladderStructure = (new GuiTreeManualPart("Ladder", EnumPartType.Leaf) {
        @Override
        public void getPage() {}
    });
    GuiTreePart bedStructure = (new GuiTreeManualPart("Bed", EnumPartType.Leaf) {
        @Override
        public void getPage() {}
    });
    GuiTreePart chairStructure = (new GuiTreeManualPart("Chair", EnumPartType.Leaf) {
        @Override
        public void getPage() {}
    });
    GuiTreePart storageStructure = (new GuiTreeManualPart("Chair", EnumPartType.Leaf) {
        @Override
        public void getPage() {}
    });
    GuiTreePart noclipStructure = (new GuiTreeManualPart("No-Clip", EnumPartType.Leaf) {
        @Override
        public void getPage() {}
    });
    GuiTreePart lightStructure = (new GuiTreeManualPart("Light", EnumPartType.Leaf) {
        @Override
        public void getPage() {}
    });
    GuiTreePart messageStructure = (new GuiTreeManualPart("Message", EnumPartType.Leaf) {
        @Override
        public void getPage() {}
    });
    GuiTreePart allowsOnLightStructure = (new GuiTreeManualPart("Allows On Light", EnumPartType.Leaf) {
        @Override
        public void getPage() {}
    });
    GuiTreePart ropeStructure = (new GuiTreeManualPart("Rope Connection", EnumPartType.Leaf) {
        @Override
        public void getPage() {}
    });
    GuiTreePart doorStructures = (new GuiTreeManualPart("Door Structures", EnumPartType.Branch) {
        @Override
        public void getPage() {}
    });
    GuiTreePart axisDoor = (new GuiTreeManualPart("Axis Door", EnumPartType.Leaf) {
        @Override
        public void getPage() {}
    });
    GuiTreePart slidingDoor = (new GuiTreeManualPart("Sliding Door", EnumPartType.Leaf) {
        @Override
        public void getPage() {}
    });
    GuiTreePart advancedDoor = (new GuiTreeManualPart("Advanced Door", EnumPartType.Leaf) {
        @Override
        public void getPage() {}
    });
    GuiTreePart doorActivator = (new GuiTreeManualPart("Door Activator", EnumPartType.Leaf) {
        @Override
        public void getPage() {}
    });
    GuiTreePart doorLock = (new GuiTreeManualPart("Door Lock", EnumPartType.Leaf) {
        @Override
        public void getPage() {}
    });
    GuiTreePart advancedStructures = (new GuiTreeManualPart("Advanced Structures", EnumPartType.Branch) {
        @Override
        public void getPage() {}
    });
    GuiTreePart triggerStructures = (new GuiTreeManualPart("Trigger", EnumPartType.Leaf) {
        @Override
        public void getPage() {}
    });
    GuiTreePart stateMutatorStructures = (new GuiTreeManualPart("State Mutator", EnumPartType.Leaf) {
        @Override
        public void getPage() {}
    });
    GuiTreePart camPlayerStructures = (new GuiTreeManualPart("Camera Player", EnumPartType.Leaf) {
        @Override
        public void getPage() {}
    });
    GuiTreePart usingSignaling = (new GuiTreeManualPart("Using Signaling", EnumPartType.Title) {
        @Override
        public void getPage() {}
    });
    GuiTreePart whatIsSignal = (new GuiTreeManualPart("What is Signaling?", EnumPartType.Branch) {
        @Override
        public void getPage() {}
    });
    GuiTreePart signalingGUI = (new GuiTreeManualPart("Signaling Interface", EnumPartType.Leaf) {
        @Override
        public void getPage() {}
    });
    GuiTreePart logicGates = (new GuiTreeManualPart("Logic Gates", EnumPartType.Branch) {
        @Override
        public void getPage() {}
    });
    GuiTreePart and = (new GuiTreeManualPart("AND", EnumPartType.Leaf) {
        @Override
        public void getPage() {}
    });
    GuiTreePart or = (new GuiTreeManualPart("OR", EnumPartType.Leaf) {
        @Override
        public void getPage() {}
    });
    GuiTreePart not = (new GuiTreeManualPart("NOT", EnumPartType.Leaf) {
        @Override
        public void getPage() {}
    });
    GuiTreePart nand = (new GuiTreeManualPart("NAND", EnumPartType.Leaf) {
        @Override
        public void getPage() {}
    });
    GuiTreePart nor = (new GuiTreeManualPart("NOR", EnumPartType.Leaf) {
        @Override
        public void getPage() {}
    });
    GuiTreePart xor = (new GuiTreeManualPart("XOR", EnumPartType.Leaf) {
        @Override
        public void getPage() {}
    });
    GuiTreePart xnor = (new GuiTreeManualPart("XNOR", EnumPartType.Leaf) {
        @Override
        public void getPage() {}
    });
    GuiTreePart structureSignal = (new GuiTreeManualPart("Structure's Signaling", EnumPartType.Branch) {
        @Override
        public void getPage() {}
    });
    GuiTreePart staticStructureSignal = (new GuiTreeManualPart("Static Structures", EnumPartType.Branch) {
        @Override
        public void getPage() {}
    });
    GuiTreePart bedSignal = (new GuiTreeManualPart("Bed", EnumPartType.Leaf) {
        @Override
        public void getPage() {}
    });
    GuiTreePart chairSignal = (new GuiTreeManualPart("Chair", EnumPartType.Leaf) {
        @Override
        public void getPage() {}
    });
    GuiTreePart storageSignal = (new GuiTreeManualPart("Storage", EnumPartType.Leaf) {
        @Override
        public void getPage() {}
    });
    GuiTreePart noclipSignal = (new GuiTreeManualPart("No-Clip", EnumPartType.Leaf) {
        @Override
        public void getPage() {}
    });
    GuiTreePart lightSignal = (new GuiTreeManualPart("Light", EnumPartType.Leaf) {
        @Override
        public void getPage() {}
    });
    GuiTreePart messageSignal = (new GuiTreeManualPart("Message", EnumPartType.Leaf) {
        @Override
        public void getPage() {}
    });
    GuiTreePart doorStructureSignal = (new GuiTreeManualPart("Door Structures", EnumPartType.Branch) {
        @Override
        public void getPage() {}
    });
    GuiTreePart axisDoorSignal = (new GuiTreeManualPart("Axis Door", EnumPartType.Leaf) {
        @Override
        public void getPage() {}
    });
    GuiTreePart slidingDoorSignal = (new GuiTreeManualPart("Sliding Door", EnumPartType.Leaf) {
        @Override
        public void getPage() {}
    });
    GuiTreePart advancedDoorSignal = (new GuiTreeManualPart("Advanced Door", EnumPartType.Leaf) {
        @Override
        public void getPage() {}
    });
    GuiTreePart doorActivatorSignal = (new GuiTreeManualPart("Door Activator", EnumPartType.Leaf) {
        @Override
        public void getPage() {}
    });
    GuiTreePart doorLockSignal = (new GuiTreeManualPart("Door Lock", EnumPartType.Leaf) {
        @Override
        public void getPage() {}
    });
    GuiTreePart advancedStructureSignal = (new GuiTreeManualPart("Advanced Structures", EnumPartType.Branch) {
        @Override
        public void getPage() {}
    });
    GuiTreePart triggerSignal = (new GuiTreeManualPart("Trigger", EnumPartType.Leaf) {
        @Override
        public void getPage() {}
    });
    GuiTreePart stateMutatorSignal = (new GuiTreeManualPart("State Mutator", EnumPartType.Leaf) {
        @Override
        public void getPage() {}
    });
    GuiTreePart camPlayerSignal = (new GuiTreeManualPart("Camera Player", EnumPartType.Leaf) {
        @Override
        public void getPage() {}
    });
    GuiTreePart tutorial = (new GuiTreeManualPart("Tutorial", EnumPartType.Title) {
        @Override
        public void getPage() {}
    });
    public GuiTree tree;
    
    public SubGuiManual() {
        super(600, 400);
        this.setStyle(defaultStyle);
    }
    
    /*
    public BufferedImage testing() {
        Parser parser = Parser.builder().build();
        org.commonmark.node.Node document = parser
                .parse("<html> <head> <style> div { font-family:fixedsys; font-size: 8px; color:rgb(256, 256, 256); width: 300px;  height: 200px;  background-color:rgb(90, 90, 90);} </style> </head> <body> <div><h1>Welcome To LittleTiles</h1>" + "LittleTiles is a mod for anyone who wants to push the limit of building in Minecraft. This mod allows you to build with what are called <a href=\"url\">Tiles.</a>" + "</div> </body> </html>");
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        org.jsoup.nodes.Document doc = Jsoup.parse(renderer.render(document));
        HtmlImageGenerator imageGenerator = new HtmlImageGenerator();
        imageGenerator.loadHtml(doc.html());
        BufferedImage image = imageGenerator.getBufferedImage();
        return image;
    }
    */
    @Override
    public void createControls() {
        
        GuiColorablePanel panelLeft = new GuiColorablePanel("tableLeft", 0, 0, 200, 394, new Color(0, 0, 0), new Color(60, 80, 100));
        GuiColorablePanel panelRight = new GuiColorablePanel("tableRight", 209, 0, 385, 394, new Color(0, 0, 0), new Color(60, 80, 100));
        scrollBoxPage = new GuiScrollBox("scrollBoxLeft", 0, 0, 379, 388);
        scrollBoxPage.setStyle(defaultStyle);
        
        panelRight.addControl(scrollBoxPage);
        GuiScrollBox scrollBox = new GuiScrollBox("scrollBox", 0, 0, 194, 388);
        scrollBox.setStyle(defaultStyle);
        scrollBox.addControl(new GuiLabel("Hello, we've been trying to reach you", 0, 1540));
        scrollBox.addControl(new GuiLabel("about your car's extended warranty.", 0, 1550));
        controls.add(panelLeft);
        controls.add(panelRight);
        //GuiImage image = new GuiImage("image", testing(), 0, 0, 1);
        //scrollBoxLeft.addControl(image);
        panelLeft.controls.add(scrollBox);
        
        List<GuiTreePart> listOfMenus = new ArrayList<GuiTreePart>();
        welcome.addMenu(littleTile).addMenu(alet);
        addModRemove.addMenu(adding).addMenu(modifying).addMenu(removing);
        measuring.addMenu(tapeMeasure).addMenu(measurements.addMenu(box).addMenu(line).addMenu(compass));
        buildingTool.addMenu(chisel).addMenu(hammer).addMenu(glove).addMenu(paintBrush).addMenu(saw).addMenu(screwdriver);
        drawShape.addMenu(dragBox).addMenu(dragSlice).addMenu(dragPolygon).addMenu(dragWall).addMenu(dragPillar).addMenu(dragCurves).addMenu(dragCylinder).addMenu(dragSphere)
                .addMenu(dragPyramid).addMenu(dragTile).addMenu(dragType).addMenu(dragConnected).addMenu(dragCetered).addMenu(dragMagicWand);
        drawMode.addMenu(defaultMode).addMenu(fillMode).addMenu(allMode).addMenu(overwriteMode).addMenu(overwriteAllMode).addMenu(replaceMode).addMenu(stencilMode)
                .addMenu(colorizeMode);
        buildingWTile.addMenu(whatIsTile).addMenu(addModRemove).addMenu(measuring).addMenu(buildingTool).addMenu(drawShape).addMenu(drawMode).addMenu(materialWhitelist);
        savingStructure.addMenu(blueprint);
        staticStructure.addMenu(fixedStructure).addMenu(ladderStructure).addMenu(bedStructure).addMenu(chairStructure).addMenu(storageStructure).addMenu(allowsOnLightStructure)
                .addMenu(ropeStructure);
        doorStructures.addMenu(axisDoor).addMenu(slidingDoor).addMenu(advancedDoor).addMenu(doorActivator).addMenu(doorLock);
        advancedStructures.addMenu(triggerStructures).addMenu(stateMutatorStructures).addMenu(camPlayerStructures);
        usingStructure.addMenu(savingStructure).addMenu(staticStructure).addMenu(doorStructures).addMenu(advancedStructures);
        whatIsSignal.addMenu(signalingGUI);
        logicGates.addMenu(and).addMenu(or).addMenu(not).addMenu(nand).addMenu(nor).addMenu(xor).addMenu(xnor);
        staticStructureSignal.addMenu(bedSignal).addMenu(chairSignal).addMenu(storageSignal).addMenu(noclipSignal).addMenu(lightSignal).addMenu(messageSignal);
        doorStructureSignal.addMenu(axisDoorSignal).addMenu(slidingDoorSignal).addMenu(advancedDoorSignal).addMenu(doorActivatorSignal).addMenu(doorLockSignal);
        advancedStructureSignal.addMenu(triggerSignal).addMenu(stateMutatorSignal).addMenu(camPlayerSignal);
        structureSignal.addMenu(staticStructureSignal).addMenu(doorStructureSignal).addMenu(advancedStructureSignal);
        usingSignaling.addMenu(whatIsSignal).addMenu(logicGates).addMenu(structureSignal);
        listOfMenus.add(welcome);
        listOfMenus.add(buildingWTile);
        listOfMenus.add(usingStructure);
        listOfMenus.add(usingSignaling);
        
        tree = new GuiTree("tree", 0, 0, 194, listOfMenus, true, 0, 0, 50);
        scrollBox.controls.add(tree);
        
    }
    
    @CustomEventSubscribe
    public void changed(GuiControlChangedEvent event) {
        if (event.source instanceof GuiTreeManualPart) {
            GuiTreeManualPart part = (GuiTreeManualPart) event.source;
            updateMessage(part);
        }
    }
    
    public void updateMessage(GuiTreeManualPart part) {
        scrollBoxPage.scrolled.set(0);
        if (part instanceof GuiTreePagePart) {
            GuiTreePagePart paper = (GuiTreePagePart) part;
            part.getPage();
            this.selected = paper.page;
            tree.highlightPart(paper.page);
        } else if (!this.selected.equals(part)) {
            int i = 0;
            while (i < scrollBoxPage.controls.size()) {
                if (scrollBoxPage.controls.get(i) instanceof CoreControl) {
                    if (scrollBoxPage.controls.get(i) instanceof GuiGIF)
                        ((GuiGIF) scrollBoxPage.controls.get(i)).onClosed();
                    scrollBoxPage.controls.remove(i);
                } else
                    i++;
            }
            part.getPage();
            this.selected = part;
        }
    }
    
    public void getWelcomeLTMsg() {
        
    }
    
    public void getWelcomeAletMsg() {}
    
    public void getDrawShape() {
        scrollBoxPage.addControl(new GuiModifibleTextBox("", ModifierAttribute.addText(2, white, false, 0, "Draw Shapes", false, true, false) + ModifierAttribute
                .addText(1, white, false, 2, "    A ", false, false, false) + ModifierAttribute
                        .addText(1.1, 0x00FFFF, false, 0, "Draw Shape", true, false, false) + ModifierAttribute
                                .addText(1, white, false, 0, ", is the type of shape that the little tools can use. The draw shape can remove,add or edit tiles, depending on the tool and placement mode that is used.", false, false, false), 0, 0, 350));
    }
    
    public void getDrawShapeBox() {
        scrollBoxPage.addControl(new GuiModifibleTextBox("", ModifierAttribute.addText(2, white, false, 0, "Draw Shape: Box", false, true, false) + ModifierAttribute
                .addText(1, white, false, 2, "    The Box draw shape is a simple shape. As the name implies this draw shape allows you draw a squares, rectangles, or cubes. To draw with this shape right click to set point A then right click again to set point B. This will then place the cube.", false, false, false) + ModifierAttribute
                        .addText(1, white, false, 1, "    There are two settings with the box draw shape, Hallow and Thinkness. Hallow lets you place a cube with a hallow center and thickness is how thick the walls will be. The thickness is based off of the grid size. If you are using grid size 32 than thickness of 16 would mean the wall is 16/32 or 1/2 the size of block.", false, false, false), 0, 0, 350));
        scrollBoxPage.addControl(new GuiGIF("", "assets/alet/gif/box.gif", 0, 110, 1));
    }
    
    public void getDrawShapeSlice() {
        scrollBoxPage.addControl(new GuiModifibleTextBox("", ModifierAttribute.addText(2, white, false, 0, "Draw Shape: Slice", false, true, false) + ModifierAttribute
                .addText(1, white, false, 2, "    The Slice draw shape allows you to draw a slice or slope." + " To draw with this shape right click to set point A then right click again to set point B. This will then place the slice. You can use the arrow keys to change the" + " facing of the slope.", false, false, false), 0, 0, 350));
        scrollBoxPage.addControl(new GuiGIF("", "assets/alet/gif/slice.gif", 0, 65, 1));
        scrollBoxPage.addControl(new GuiModifibleTextBox("", ModifierAttribute.addText(2, white, false, 0, "Draw Shape: Slice Corner", false, true, false) + ModifierAttribute
                .addText(1, ColorUtils.WHITE, false, 2, "    The Corner Slices draw shape allows you to draw a slice or sloped corner." + " To draw with this shape right click to set point A then right click again to set point B. This will then place the corner slice. You can use the arrow keys to change the" + " facing of the slope.", false, false, false) + ModifierAttribute
                        .addText(1, ColorUtils.WHITE, false, 1, "    There is a setting for both Inner and Outer Corner Slice called second-type that will switch it to an alternate " + "corner slope. The type of corner you want to use will depend on the shape you are going for.", false, false, false), 0, 255, 350));
        scrollBoxPage.addControl(new GuiGIF("", "assets/alet/gif/inner_slice.gif", 0, 360, 1));
        scrollBoxPage.addControl(new GuiGIF("", "assets/alet/gif/inner_slice_2.gif", 0, 535, 1));
    }
    
    public void getLogicGateMsg() {
        scrollBoxPage.addControl(new GuiModifibleTextBox("", ModifierAttribute.addText(2, ColorUtils.WHITE, false, 0, "Logic Gates", false, true, false) + ModifierAttribute
                .addText(1, ColorUtils.WHITE, false, 2, "    Logic gates are fundemental to digital circuits. They are used in majority of electronic devices in some way." + " Within a circuit the gate will determine the output by either recieving two or one signal input(s). With the results being created by boolean algebra." + " A boolean is either two values, true being 1 or false being 0. Each gate will result in a differet boolean value.", false, false, false) + ModifierAttribute
                        .addText(1, ColorUtils.WHITE, false, 2, "    With LittleTile's signaling logic gates too are fundemental to any opperation you wish preform." + " Signals in LittleTiles can be thought of as redstone. If you are familiar with redstone then you'd know of RS-Latch, T Flip-Flop, Counters, Radomizers, and more." + " All of these can be done with signaling however, it is completely different from redstone. As with signaling you are directly working with the logic gates" + " that are used to accomplish these circuits. Whereas, with redstone you can place some dust and pistons and you have a T Flip-Flop done. But, for those that" + " may be discuraged by this. There are premade circuits for these common redstone circuits.", false, false, false), 0, 0, 350));
        
    }
    
    public void getAndMsg() {
        scrollBoxPage.addControl(new GuiModifibleTextBox("", ModifierAttribute.addText(2, ColorUtils.WHITE, false, 0, "Logic Gate: And", false, true, false) + ModifierAttribute
                .addText(1, ColorUtils.WHITE, false, 2, "    The logic gate, And, compares two values to see if they are true.", false, false, false), 0, 0, 350));
        scrollBoxPage.addControl(new GuiTable("", "", 0, 100, 50, 15, 5, 3, false));
    }
    
    public void getNandMsg() {}
    
    public void getOrMsg() {}
    
    public void getXorMsg() {}
    
    public void getNotMsg() {}
    
    public void getMagicWandMsg() {}
    
    public void getCenteredBoxMsg() {}
    
    public void getCenteredCylinderMsg() {}
    
    public void getCenteredSphereMsg() {}
    
    public void getTypewriterMsg() {}
    
    public void getPhotoImporterMsg() {}
    
    public void getFillingCabinetMsg() {}
    
    public void getTapeMeasureMsg() {}
    
    public void getLittleRopeMsg() {}
    
    public void getGridSizeMsg() {
        scrollBoxPage.addControl(new GuiScalableTextBox("", TextFormatting.BOLD + "Configuration", 0, 0, 371, 1.5));
        scrollBoxPage.addControl(new GuiScalableTextBox("", "   ", 0, 25, 371, 1));
    }
}
