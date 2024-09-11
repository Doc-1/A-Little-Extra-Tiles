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
import com.alet.client.gui.controls.menu.GuiTreePartManual;
import com.alet.client.gui.controls.menu.GuiTreePart;
import com.alet.client.gui.controls.menu.GuiTreePart.EnumPartType;
import com.creativemd.creativecore.common.gui.container.SubGui;
import com.creativemd.creativecore.common.gui.controls.gui.GuiLabel;
import com.creativemd.creativecore.common.gui.controls.gui.GuiScrollBox;
import com.creativemd.creativecore.common.gui.event.gui.GuiControlChangedEvent;
import com.creativemd.creativecore.common.utils.mc.ColorUtils;
import com.n247s.api.eventapi.eventsystem.CustomEventSubscribe;

import net.minecraft.util.text.TextFormatting;

public class SubGuiManual extends SubGui {
    
    public GuiTreePart selected = new GuiTreePart("", EnumPartType.Branch);
    //ModifierAttribute.addText(1, ColorUtils.WHITE, false, 0, "", false, false, false)
    public GuiScrollBox scrollBoxPage;
    private final int white = ColorUtils.WHITE;
    
    GuiTreePart welcome = (new GuiTreePartManual("Welcome To LittleTiles & ALET", EnumPartType.Title, "welcomeTitle"));
    GuiTreePart littleTile = (new GuiTreePartManual("LittleTiles", EnumPartType.Leaf, "welcomeLT"));
    GuiTreePart alet = (new GuiTreePartManual("A Little Extra Tiles", EnumPartType.Leaf, "welcomeALET"));
    GuiTreePart buildingWTile = (new GuiTreePartManual("Building With Tiles", EnumPartType.Title, "buildingTilesLT"));
    GuiTreePart whatIsTile = (new GuiTreePartManual("What is a Tile?", EnumPartType.Leaf, "whatIsTileLT"));
    GuiTreePart addModRemove = (new GuiTreePartManual("Add, Modify, & Remove Tiles", EnumPartType.Branch, "addModRemoveLT"));
    GuiTreePart adding = (new GuiTreePartManual("Adding Tiles", EnumPartType.Leaf, "addingLT"));
    GuiTreePart modifying = (new GuiTreePartManual("Modfying Tiles", EnumPartType.Leaf, "modifyingLT"));
    GuiTreePart removing = (new GuiTreePartManual("Removing Tiles", EnumPartType.Leaf, "removingLT"));
    GuiTreePart measuring = (new GuiTreePartManual("Measuring Tiles", EnumPartType.Branch, "measuringALET"));
    GuiTreePart tapeMeasure = (new GuiTreePartManual("Little Tape Measure", EnumPartType.Leaf, "tapeMeasureALET"));
    GuiTreePart measurements = (new GuiTreePartManual("Measurement Shapes", EnumPartType.Branch, "measurementsALET"));
    GuiTreePart box = (new GuiTreePartManual("Box", EnumPartType.Leaf, "boxMeasurmentALET"));
    GuiTreePart line = (new GuiTreePartManual("Line", EnumPartType.Leaf, "lineMeasurmentALET"));
    GuiTreePart compass = (new GuiTreePartManual("Compass", EnumPartType.Leaf, "compassMeasurmentALET"));
    GuiTreePart buildingTool = (new GuiTreePartManual("Building Tool", EnumPartType.Branch, "buildingToolsLT"));
    GuiTreePart chisel = (new GuiTreePartManual("Little Chisel", EnumPartType.Leaf, "chiselToolLT"));
    GuiTreePart hammer = (new GuiTreePartManual("Little Hammer", EnumPartType.Leaf, "hammerToolLT"));
    GuiTreePart glove = (new GuiTreePartManual("Little Glove", EnumPartType.Leaf, "gloveToolLT"));
    GuiTreePart paintBrush = (new GuiTreePartManual("Little Paint Brush", EnumPartType.Leaf, "paintBrushToolLT"));
    GuiTreePart saw = (new GuiTreePartManual("Little Saw", EnumPartType.Leaf, "sawToolLT"));
    GuiTreePart screwdriver = (new GuiTreePartManual("Little Screwdriver", EnumPartType.Leaf, "screwdriverToolLT"));
    GuiTreePart drawShape = (new GuiTreePartManual("Draw Shapes", EnumPartType.Branch, "drawShapeLT"));
    GuiTreePart dragBox = (new GuiTreePartManual("Box", EnumPartType.Leaf, "drawShapeBoxLT"));
    GuiTreePart dragSlice = (new GuiTreePartManual("Slice", EnumPartType.Leaf, "drawShapeSliceLT"));
    GuiTreePart dragPolygon = (new GuiTreePartManual("Polygon", EnumPartType.Leaf, "drawShapePolygonLT"));
    GuiTreePart dragWall = (new GuiTreePartManual("Wall", EnumPartType.Leaf, "drawShapeWallLT"));
    GuiTreePart dragPillar = (new GuiTreePartManual("Pillar", EnumPartType.Leaf, "drawShapePillarLT"));
    GuiTreePart dragCurves = (new GuiTreePartManual("Curves", EnumPartType.Leaf, "drawShapeCurvesLT"));
    GuiTreePart dragCylinder = (new GuiTreePartManual("Cylinder", EnumPartType.Leaf, "drawShapeCylinder"));
    GuiTreePart dragSphere = (new GuiTreePartManual("Shpere", EnumPartType.Leaf, "drawShapeSphereLT"));
    GuiTreePart dragPyramid = (new GuiTreePartManual("Pyramid", EnumPartType.Leaf, "drawShapePyramid"));
    GuiTreePart dragTile = (new GuiTreePartManual("Tile", EnumPartType.Leaf, "drawShapeTileLT"));
    GuiTreePart dragType = (new GuiTreePartManual("Type", EnumPartType.Leaf, "drawShapeTypeLT"));
    GuiTreePart dragConnected = (new GuiTreePartManual("Connected", EnumPartType.Leaf, "drawShapeConnectedLT"));
    GuiTreePart dragCetered = (new GuiTreePartManual("Centered Drag Shapes", EnumPartType.Leaf, "drawShapeCenteredALET"));
    GuiTreePart dragMagicWand = (new GuiTreePartManual("Magic Wand", EnumPartType.Leaf, "drawShapeMagicWandALET"));
    GuiTreePart drawMode = (new GuiTreePartManual("Draw Modes", EnumPartType.Branch, "drawModeLT"));
    GuiTreePart defaultMode = (new GuiTreePartManual("Default", EnumPartType.Leaf, "modeDefaultLT"));
    GuiTreePart fillMode = (new GuiTreePartManual("Fill", EnumPartType.Leaf, "modeFillLT"));
    GuiTreePart allMode = (new GuiTreePartManual("All", EnumPartType.Leaf, "modeAllLT"));
    GuiTreePart overwriteMode = (new GuiTreePartManual("Overwrite", EnumPartType.Leaf, "modeOverwriteLT"));
    GuiTreePart overwriteAllMode = (new GuiTreePartManual("Overwrite All", EnumPartType.Leaf, "modeOverwriteALllLT"));
    GuiTreePart replaceMode = (new GuiTreePartManual("Replace", EnumPartType.Leaf, "modeReplaceLT"));
    GuiTreePart stencilMode = (new GuiTreePartManual("Stencil", EnumPartType.Leaf, "modeStencilLT"));
    GuiTreePart colorizeMode = (new GuiTreePartManual("Colorize", EnumPartType.Leaf, "modeColorizeLT"));
    GuiTreePart materialWhitelist = (new GuiTreePartManual("Material Whitelist", EnumPartType.Leaf, "materialWhitelistLT"));
    GuiTreePart usingStructure = (new GuiTreePartManual("Using Structures", EnumPartType.Title, "usingStructuresLT"));
    GuiTreePart savingStructure = (new GuiTreePartManual("Saving Structures", EnumPartType.Branch, "savingStructuresLT"));
    GuiTreePart blueprint = (new GuiTreePartManual("Little Blueprint", EnumPartType.Leaf, "blueprintLT"));
    GuiTreePart staticStructure = (new GuiTreePartManual("Static Structures", EnumPartType.Branch, "staticStructureLT"));
    GuiTreePart fixedStructure = (new GuiTreePartManual("Fixed", EnumPartType.Leaf, "fixedStructureLT"));
    GuiTreePart ladderStructure = (new GuiTreePartManual("Ladder", EnumPartType.Leaf, "ladderStructureLT"));
    GuiTreePart bedStructure = (new GuiTreePartManual("Bed", EnumPartType.Leaf, "bedStructureLT"));
    GuiTreePart chairStructure = (new GuiTreePartManual("Chair", EnumPartType.Leaf, "chairStructureLT"));
    GuiTreePart storageStructure = (new GuiTreePartManual("Chair", EnumPartType.Leaf, "storageStructureLT"));
    GuiTreePart noclipStructure = (new GuiTreePartManual("No-Clip", EnumPartType.Leaf, "noclipStructureLT"));
    GuiTreePart lightStructure = (new GuiTreePartManual("Light", EnumPartType.Leaf, "lightStructureLT"));
    GuiTreePart messageStructure = (new GuiTreePartManual("Message", EnumPartType.Leaf, "messageStructureLT"));
    GuiTreePart alwaysOnLightStructure = (new GuiTreePartManual("Always On Light", EnumPartType.Leaf, "alwaysOnLightStructureALET"));
    GuiTreePart ropeStructure = (new GuiTreePartManual("Rope Connection", EnumPartType.Leaf, "ropeStructureALET"));
    GuiTreePart doorStructures = (new GuiTreePartManual("Door Structures", EnumPartType.Branch, "doorStructureLT"));
    GuiTreePart axisDoor = (new GuiTreePartManual("Axis Door", EnumPartType.Leaf, "axisDoorLT"));
    GuiTreePart slidingDoor = (new GuiTreePartManual("Sliding Door", EnumPartType.Leaf, "slidingDoorLT"));
    GuiTreePart advancedDoor = (new GuiTreePartManual("Advanced Door", EnumPartType.Leaf, "advancedDoorLT"));
    GuiTreePart doorActivator = (new GuiTreePartManual("Door Activator", EnumPartType.Leaf, "doorActivatorDoorLT"));
    GuiTreePart doorLock = (new GuiTreePartManual("Door Lock", EnumPartType.Leaf, "doorLockDoorALET"));
    GuiTreePart advancedStructures = (new GuiTreePartManual("Advanced Structures", EnumPartType.Branch, "advancedStructuresALET"));
    GuiTreePart triggerStructures = (new GuiTreePartManual("Trigger", EnumPartType.Leaf, "triggerStructureALET"));
    GuiTreePart stateMutatorStructures = (new GuiTreePartManual("State Mutator", EnumPartType.Leaf, "stateMutatorStructureALET"));
    GuiTreePart camPlayerStructures = (new GuiTreePartManual("Camera Player", EnumPartType.Leaf, "camPlayerStructureALET"));
    GuiTreePart audioStructures = (new GuiTreePartManual("Audio Structures", EnumPartType.Branch, "audioStructureLT"));
    GuiTreePart musicComposerStructure = (new GuiTreePartManual("Music Composer", EnumPartType.Leaf, "musicComposerStructureALET"));
    GuiTreePart usingSignaling = (new GuiTreePartManual("Using Signaling", EnumPartType.Title, "usingSignalingLT"));
    GuiTreePart whatIsSignal = (new GuiTreePartManual("What is Signaling?", EnumPartType.Branch, "whatIsSignalingLT"));
    GuiTreePart signalingGUI = (new GuiTreePartManual("Signaling Interface", EnumPartType.Leaf, "signalingInterfaceLT"));
    GuiTreePart logicGates = (new GuiTreePartManual("Logic Gates", EnumPartType.Branch, "logicGatesLT"));
    GuiTreePart and = (new GuiTreePartManual("AND", EnumPartType.Leaf, "andLogicGateLT"));
    GuiTreePart or = (new GuiTreePartManual("OR", EnumPartType.Leaf, "orLogicGateLT"));
    GuiTreePart not = (new GuiTreePartManual("NOT", EnumPartType.Leaf, "notLogicGateLT"));
    GuiTreePart nand = (new GuiTreePartManual("NAND", EnumPartType.Leaf, "nandLogicGateLT"));
    GuiTreePart nor = (new GuiTreePartManual("NOR", EnumPartType.Leaf, "norLogicGateLT"));
    GuiTreePart xor = (new GuiTreePartManual("XOR", EnumPartType.Leaf, "xorLogicGateLT"));
    GuiTreePart xnor = (new GuiTreePartManual("XNOR", EnumPartType.Leaf, "xnorLogicGateLT"));
    GuiTreePart bitwiseGates = (new GuiTreePartManual("Logic Bitwise-Gates", EnumPartType.Branch, "logicBitwiseGatesLT"));
    GuiTreePart andB = (new GuiTreePartManual("B-AND", EnumPartType.Leaf, "bAndLogicGateLT"));
    GuiTreePart orB = (new GuiTreePartManual("B-OR", EnumPartType.Leaf, "bOrLogicGateLT"));
    GuiTreePart notB = (new GuiTreePartManual("B-NOT", EnumPartType.Leaf, "bNotLogicGateLT"));
    GuiTreePart xorB = (new GuiTreePartManual("B-XOR", EnumPartType.Leaf, "bXorLogicGateLT"));
    GuiTreePart math = (new GuiTreePartManual("Mathematical Operators", EnumPartType.Branch, "mathematicalOperatorsLT"));
    GuiTreePart add = (new GuiTreePartManual("Add", EnumPartType.Leaf, "mathAddLT"));
    GuiTreePart sub = (new GuiTreePartManual("Sub", EnumPartType.Leaf, "mathSubLT"));
    GuiTreePart multi = (new GuiTreePartManual("Multi", EnumPartType.Leaf, "mathMultiLT"));
    GuiTreePart div = (new GuiTreePartManual("Div", EnumPartType.Leaf, "mathDivLT"));
    GuiTreePart structureSignal = (new GuiTreePartManual("Structure's Signaling", EnumPartType.Branch, "structureSignalingLT"));
    GuiTreePart staticStructureSignal = (new GuiTreePartManual("Static Structures", EnumPartType.Branch, "staticStructureSignalingLT"));
    GuiTreePart bedSignal = (new GuiTreePartManual("Bed", EnumPartType.Leaf, "bedStructureSignalingLT"));
    GuiTreePart chairSignal = (new GuiTreePartManual("Chair", EnumPartType.Leaf, "chairStructureSignalingLT"));
    GuiTreePart storageSignal = (new GuiTreePartManual("Storage", EnumPartType.Leaf, "storageStructureSignalingLT"));
    GuiTreePart noclipSignal = (new GuiTreePartManual("No-Clip", EnumPartType.Leaf, "noclipStructureSignalingLT"));
    GuiTreePart lightSignal = (new GuiTreePartManual("Light", EnumPartType.Leaf, "lightStructureSignalingLT"));
    GuiTreePart messageSignal = (new GuiTreePartManual("Message", EnumPartType.Leaf, "messageStructureSignalingLT"));
    GuiTreePart doorStructureSignal = (new GuiTreePartManual("Door Structures", EnumPartType.Branch, "doorStructureSignalingLT"));
    GuiTreePart axisDoorSignal = (new GuiTreePartManual("Axis Door", EnumPartType.Leaf, "axisDoorStructureSignalingLT"));
    GuiTreePart slidingDoorSignal = (new GuiTreePartManual("Sliding Door", EnumPartType.Leaf, "slidingDoorStructureSignalingLT"));
    GuiTreePart advancedDoorSignal = (new GuiTreePartManual("Advanced Door", EnumPartType.Leaf, "advancedDoorStructureSignalingLT"));
    GuiTreePart doorActivatorSignal = (new GuiTreePartManual("Door Activator", EnumPartType.Leaf, "doorActivatorStructureSignalingLT"));
    GuiTreePart doorLockSignal = (new GuiTreePartManual("Door Lock", EnumPartType.Leaf, "doorLockStructureSignalingLT"));
    GuiTreePart advancedStructureSignal = (new GuiTreePartManual("Advanced Structures", EnumPartType.Branch, "advancedStructureSignalingALET"));
    GuiTreePart triggerSignal = (new GuiTreePartManual("Trigger", EnumPartType.Leaf, "triggerStructureSignalingALET"));
    GuiTreePart stateMutatorSignal = (new GuiTreePartManual("State Mutator", EnumPartType.Leaf, "stateMutatorStructureSignalingALET"));
    GuiTreePart audioStructureSignal = (new GuiTreePartManual("Audio Structures", EnumPartType.Branch, "audioStructureSignalingLT"));
    GuiTreePart musicComposerSignal = (new GuiTreePartManual("Music Composer", EnumPartType.Leaf, "musicComposerStructureSignalingALET"));
    GuiTreePart camPlayerSignal = (new GuiTreePartManual("Camera Player", EnumPartType.Leaf, "camPlayerStructureSignalingALET"));
    GuiTreePart tutorial = (new GuiTreePartManual("Tutorial", EnumPartType.Title, "tutorialsLT"));
    GuiTreePart littleCircuit = new GuiTreePartManual("Little Circuits", EnumPartType.Title, "circuitALET");
    GuiTreePart circuitClock = new GuiTreePartManual("Clock", EnumPartType.Leaf, "circuitClockALET");
    GuiTreePart circuitColorMon = new GuiTreePartManual("Colored ASCII Monitor", EnumPartType.Leaf, "circuitColorMonALET");
    GuiTreePart circuitMath = new GuiTreePartManual("Algebraic Operation", EnumPartType.Leaf, "circuitMathALET");
    GuiTreePart circuitMemory = new GuiTreePartManual("Memory", EnumPartType.Leaf, "circuitMemoryALET");
    GuiTreePart circuitPulser = new GuiTreePartManual("Pulser", EnumPartType.Leaf, "circuitPulserALET");
    GuiTreePart circuitRand = new GuiTreePartManual("Random Number Generator", EnumPartType.Leaf, "circuitRandALET");
    GuiTreePart circuitSwitch = new GuiTreePartManual("Switch", EnumPartType.Leaf, "circuitSwitchALET");
    GuiTreePart circuitMagnitdue = new GuiTreePartManual("Magnitude Comparator", EnumPartType.Leaf, "circuitComparatorALET");
    GuiTreePart circuitSplitter = new GuiTreePartManual("Splitter", EnumPartType.Leaf, "circuitSplitterALET");
    GuiTreePart circuitCombiner = new GuiTreePartManual("Combiner", EnumPartType.Leaf, "circuitCombinerALET");
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
        drawShape.addMenu(dragBox).addMenu(dragSlice).addMenu(dragPolygon).addMenu(dragWall).addMenu(dragPillar).addMenu(
            dragCurves).addMenu(dragCylinder).addMenu(dragSphere).addMenu(dragPyramid).addMenu(dragTile).addMenu(dragType)
                .addMenu(dragConnected).addMenu(dragCetered).addMenu(dragMagicWand);
        drawMode.addMenu(defaultMode).addMenu(fillMode).addMenu(allMode).addMenu(overwriteMode).addMenu(overwriteAllMode)
                .addMenu(replaceMode).addMenu(stencilMode).addMenu(colorizeMode);
        buildingWTile.addMenu(whatIsTile).addMenu(addModRemove).addMenu(measuring).addMenu(buildingTool).addMenu(drawShape)
                .addMenu(drawMode).addMenu(materialWhitelist);
        savingStructure.addMenu(blueprint);
        staticStructure.addMenu(fixedStructure).addMenu(ladderStructure).addMenu(bedStructure).addMenu(chairStructure)
                .addMenu(storageStructure).addMenu(alwaysOnLightStructure).addMenu(ropeStructure);
        doorStructures.addMenu(axisDoor).addMenu(slidingDoor).addMenu(advancedDoor).addMenu(doorActivator).addMenu(doorLock);
        advancedStructures.addMenu(triggerStructures).addMenu(stateMutatorStructures).addMenu(camPlayerStructures);
        audioStructures.addMenu(musicComposerStructure);
        usingStructure.addMenu(savingStructure).addMenu(staticStructure).addMenu(doorStructures).addMenu(advancedStructures)
                .addMenu(audioStructures);
        whatIsSignal.addMenu(signalingGUI);
        logicGates.addMenu(and).addMenu(or).addMenu(not).addMenu(nand).addMenu(nor).addMenu(xor).addMenu(xnor);
        bitwiseGates.addMenu(andB).addMenu(orB).addMenu(notB).addMenu(xorB);
        math.addMenu(add).addMenu(sub).addMenu(multi).addMenu(div);
        staticStructureSignal.addMenu(bedSignal).addMenu(chairSignal).addMenu(storageSignal).addMenu(noclipSignal).addMenu(
            lightSignal).addMenu(messageSignal);
        doorStructureSignal.addMenu(axisDoorSignal).addMenu(slidingDoorSignal).addMenu(advancedDoorSignal).addMenu(
            doorActivatorSignal).addMenu(doorLockSignal);
        audioStructureSignal.addMenu(musicComposerSignal);
        advancedStructureSignal.addMenu(triggerSignal).addMenu(stateMutatorSignal).addMenu(camPlayerSignal);
        structureSignal.addMenu(staticStructureSignal).addMenu(doorStructureSignal).addMenu(advancedStructureSignal).addMenu(
            audioStructureSignal);
        usingSignaling.addMenu(whatIsSignal).addMenu(logicGates).addMenu(bitwiseGates).addMenu(math).addMenu(
            structureSignal);
        littleCircuit.addMenu(circuitClock).addMenu(circuitPulser).addMenu(circuitColorMon).addMenu(circuitMagnitdue)
                .addMenu(circuitMath).addMenu(circuitMemory).addMenu(circuitRand).addMenu(circuitSwitch).addMenu(
                    circuitSplitter).addMenu(circuitCombiner);
        listOfMenus.add(welcome);
        listOfMenus.add(buildingWTile);
        listOfMenus.add(usingStructure);
        listOfMenus.add(usingSignaling);
        listOfMenus.add(littleCircuit);
        tree = new GuiTree("tree", 0, 0, 194, listOfMenus, true, 0, 0, 50);
        scrollBox.controls.add(tree);
    }
    
    @CustomEventSubscribe
    public void changed(GuiControlChangedEvent event) {
        if (event.source instanceof GuiTreePartManual) {
            GuiTreePartManual part = (GuiTreePartManual) event.source;
            updateMessage(part);
        }
    }
    
    public void updateMessage(GuiTreePartManual part) {
        scrollBoxPage.scrolled.set(0);
        if (!part.equals(this.selected)) {
            scrollBoxPage.removeControls(" ");
            GuiTreePartManual paper = (GuiTreePartManual) part;
            GuiModifibleTextBox box = new GuiModifibleTextBox("", part.getPage(), 0, 0, 350);
            scrollBoxPage.addControl(box);
            box.addImages();
            tree.highlightPart(paper);
            this.selected = part;
        }
    }
    
    public void getWelcomeLTMsg() {
        
    }
    
    public void getWelcomeAletMsg() {}
    
    public void getDrawShape() {
        scrollBoxPage.addControl(new GuiModifibleTextBox("", ModifierAttribute.addText(2, white, false, 0, "Draw Shapes",
            false, true, false) + ModifierAttribute.addText(1, white, false, 2, "    A ", false, false,
                false) + ModifierAttribute.addText(1.1, 0x00FFFF, false, 0, "Draw Shape", true, false,
                    false) + ModifierAttribute.addText(1, white, false, 0,
                        ", is the type of shape that the little tools can use. The draw shape can remove,add or edit tiles, depending on the tool and placement mode that is used.",
                        false, false, false), 0, 0, 350));
    }
    
    public void getDrawShapeBox() {
        scrollBoxPage.addControl(new GuiModifibleTextBox("", ModifierAttribute.addText(2, white, false, 0, "Draw Shape: Box",
            false, true, false) + ModifierAttribute.addText(1, white, false, 2,
                "    The Box draw shape is a simple shape. As the name implies this draw shape allows you draw a squares, rectangles, or cubes. To draw with this shape right click to set point A then right click again to set point B. This will then place the cube.",
                false, false, false) + ModifierAttribute.addText(1, white, false, 1,
                    "    There are two settings with the box draw shape, Hallow and Thinkness. Hallow lets you place a cube with a hallow center and thickness is how thick the walls will be. The thickness is based off of the grid size. If you are using grid size 32 than thickness of 16 would mean the wall is 16/32 or 1/2 the size of block.",
                    false, false, false), 0, 0, 350));
        scrollBoxPage.addControl(new GuiGIF("", "assets/alet/gif/box.gif", 0, 110, 1));
    }
    
    public void getDrawShapeSlice() {
        scrollBoxPage.addControl(new GuiModifibleTextBox("", ModifierAttribute.addText(2, white, false, 0,
            "Draw Shape: Slice", false, true, false) + ModifierAttribute.addText(1, white, false, 2,
                "    The Slice draw shape allows you to draw a slice or slope." + " To draw with this shape right click to set point A then right click again to set point B. This will then place the slice. You can use the arrow keys to change the" + " facing of the slope.",
                false, false, false), 0, 0, 350));
        scrollBoxPage.addControl(new GuiGIF("", "assets/alet/gif/slice.gif", 0, 65, 1));
        scrollBoxPage.addControl(new GuiModifibleTextBox("", ModifierAttribute.addText(2, white, false, 0,
            "Draw Shape: Slice Corner", false, true, false) + ModifierAttribute.addText(1, ColorUtils.WHITE, false, 2,
                "    The Corner Slices draw shape allows you to draw a slice or sloped corner." + " To draw with this shape right click to set point A then right click again to set point B. This will then place the corner slice. You can use the arrow keys to change the" + " facing of the slope.",
                false, false, false) + ModifierAttribute.addText(1, ColorUtils.WHITE, false, 1,
                    "    There is a setting for both Inner and Outer Corner Slice called second-type that will switch it to an alternate " + "corner slope. The type of corner you want to use will depend on the shape you are going for.",
                    false, false, false), 0, 255, 350));
        scrollBoxPage.addControl(new GuiGIF("", "assets/alet/gif/inner_slice.gif", 0, 360, 1));
        scrollBoxPage.addControl(new GuiGIF("", "assets/alet/gif/inner_slice_2.gif", 0, 535, 1));
    }
    
    public void getLogicGateMsg() {
        scrollBoxPage.addControl(new GuiModifibleTextBox("", ModifierAttribute.addText(2, ColorUtils.WHITE, false, 0,
            "Logic Gates", false, true, false) + ModifierAttribute.addText(1, ColorUtils.WHITE, false, 2,
                "    Logic gates are fundemental to digital circuits. They are used in majority of electronic devices in some way." + " Within a circuit the gate will determine the output by either recieving two or one signal input(s). With the results being created by boolean algebra." + " A boolean is either two values, true being 1 or false being 0. Each gate will result in a differet boolean value.",
                false, false, false) + ModifierAttribute.addText(1, ColorUtils.WHITE, false, 2,
                    "    With LittleTile's signaling logic gates too are fundemental to any opperation you wish preform." + " Signals in LittleTiles can be thought of as redstone. If you are familiar with redstone then you'd know of RS-Latch, T Flip-Flop, Counters, Radomizers, and more." + " All of these can be done with signaling however, it is completely different from redstone. As with signaling you are directly working with the logic gates" + " that are used to accomplish these circuits. Whereas, with redstone you can place some dust and pistons and you have a T Flip-Flop done. But, for those that" + " may be discuraged by this. There are premade circuits for these common redstone circuits.",
                    false, false, false), 0, 0, 350));
        
    }
    
    public void getAndMsg() {
        scrollBoxPage.addControl(new GuiModifibleTextBox("", ModifierAttribute.addText(2, ColorUtils.WHITE, false, 0,
            "Logic Gate: And", false, true, false) + ModifierAttribute.addText(1, ColorUtils.WHITE, false, 2,
                "    The logic gate, And, compares two values to see if they are true.", false, false, false), 0, 0, 350));
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
