package com.alet.common.gui.origins;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.Color;

import com.alet.common.gui.controls.GuiColorablePanel;
import com.alet.common.gui.controls.GuiModifibleTextBox;
import com.alet.common.gui.controls.menu.GuiTree;
import com.alet.common.gui.controls.menu.GuiTreePart;
import com.alet.common.gui.controls.menu.GuiTreePartManual;
import com.alet.common.gui.controls.menu.GuiTreePart.EnumPartType;
import com.creativemd.creativecore.common.gui.container.SubGui;
import com.creativemd.creativecore.common.gui.controls.gui.GuiLabel;
import com.creativemd.creativecore.common.gui.controls.gui.GuiScrollBox;
import com.creativemd.creativecore.common.gui.event.gui.GuiControlChangedEvent;
import com.creativemd.creativecore.common.utils.mc.ColorUtils;
import com.n247s.api.eventapi.eventsystem.CustomEventSubscribe;

public class SubGuiManual extends SubGui {
    
    public GuiTreePart selected = new GuiTreePart("", EnumPartType.Branch);
    //ModifierAttribute.addText(1, ColorUtils.WHITE, false, 0, "", false, false, false)
    public GuiScrollBox scrollBoxPage;
    private final int white = ColorUtils.WHITE;
    
    GuiTreePart welcome = (new GuiTreePartManual("Welcome To LittleTiles & ALET", EnumPartType.Title, "welcometitle"));
    GuiTreePart littleTile = (new GuiTreePartManual("LittleTiles", EnumPartType.Leaf, "welcomelt"));
    GuiTreePart alet = (new GuiTreePartManual("A Little Extra Tiles", EnumPartType.Leaf, "welcomealet"));
    GuiTreePart report = (new GuiTreePartManual("How To Report Issues", EnumPartType.Leaf, "reportissues"));
    GuiTreePart buildingWTile = (new GuiTreePartManual("Building With Tiles", EnumPartType.Title, "buildingtileslt"));
    GuiTreePart whatIsTile = (new GuiTreePartManual("Defining Terms", EnumPartType.Leaf, "definingterms"));
    GuiTreePart measuring = (new GuiTreePartManual("Measuring Tiles", EnumPartType.Branch, "measuringalet"));
    GuiTreePart tapeMeasure = (new GuiTreePartManual("Little Tape Measure", EnumPartType.Leaf, "tapemeasurealet"));
    GuiTreePart measurements = (new GuiTreePartManual("Measurement Shapes", EnumPartType.Branch, "measurementsalet"));
    GuiTreePart box = (new GuiTreePartManual("Box", EnumPartType.Leaf, "boxmeasurmentalet"));
    GuiTreePart line = (new GuiTreePartManual("Line", EnumPartType.Leaf, "linemeasurmentalet"));
    GuiTreePart compass = (new GuiTreePartManual("Compass", EnumPartType.Leaf, "compassmeasurmentalet"));
    GuiTreePart buildingTool = (new GuiTreePartManual("Building Tool", EnumPartType.Branch, "buildingtoolslt"));
    GuiTreePart chisel = (new GuiTreePartManual("Little Chisel", EnumPartType.Leaf, "chiseltoollt"));
    GuiTreePart hammer = (new GuiTreePartManual("Little Hammer", EnumPartType.Leaf, "hammertoollt"));
    GuiTreePart glove = (new GuiTreePartManual("Little Glove", EnumPartType.Leaf, "glovetoollt"));
    GuiTreePart paintBrush = (new GuiTreePartManual("Little Paint Brush", EnumPartType.Leaf, "paintbrushtoollt"));
    GuiTreePart saw = (new GuiTreePartManual("Little Saw", EnumPartType.Leaf, "sawtoollt"));
    GuiTreePart screwdriver = (new GuiTreePartManual("Little Screwdriver", EnumPartType.Leaf, "screwdrivertoollt"));
    GuiTreePart drawShape = (new GuiTreePartManual("Draw Shapes", EnumPartType.Branch, "drawshapelt"));
    GuiTreePart dragBox = (new GuiTreePartManual("Box", EnumPartType.Leaf, "drawshapeboxlt"));
    GuiTreePart dragSlice = (new GuiTreePartManual("Slice", EnumPartType.Leaf, "drawshapeslicelt"));
    GuiTreePart dragPolygon = (new GuiTreePartManual("Polygon", EnumPartType.Leaf, "drawshapepolygonlt"));
    GuiTreePart dragWall = (new GuiTreePartManual("Wall", EnumPartType.Leaf, "drawshapewalllt"));
    GuiTreePart dragPillar = (new GuiTreePartManual("Pillar", EnumPartType.Leaf, "drawshapepillarlt"));
    GuiTreePart dragCurves = (new GuiTreePartManual("Curves", EnumPartType.Leaf, "drawshapecurveslt"));
    GuiTreePart dragCylinder = (new GuiTreePartManual("Cylinder", EnumPartType.Leaf, "drawshapecylinder"));
    GuiTreePart dragSphere = (new GuiTreePartManual("Shpere", EnumPartType.Leaf, "drawshapespherelt"));
    GuiTreePart dragPyramid = (new GuiTreePartManual("Pyramid", EnumPartType.Leaf, "drawshapepyramid"));
    GuiTreePart dragTile = (new GuiTreePartManual("Tile", EnumPartType.Leaf, "drawshapetilelt"));
    GuiTreePart dragType = (new GuiTreePartManual("Type", EnumPartType.Leaf, "drawshapetypelt"));
    GuiTreePart dragConnected = (new GuiTreePartManual("Connected", EnumPartType.Leaf, "drawshapeconnectedlt"));
    GuiTreePart dragCetered = (new GuiTreePartManual("Centered Drag Shapes", EnumPartType.Leaf, "drawshapecenteredalet"));
    GuiTreePart dragMagicWand = (new GuiTreePartManual("Magic Wand", EnumPartType.Leaf, "drawshapemagicwandalet"));
    GuiTreePart drawMode = (new GuiTreePartManual("Draw Modes", EnumPartType.Branch, "drawmodelt"));
    GuiTreePart markMode = (new GuiTreePartManual("Mark Mode", EnumPartType.Leaf, "markmode"));
    GuiTreePart defaultMode = (new GuiTreePartManual("Default", EnumPartType.Leaf, "modedefaultlt"));
    GuiTreePart fillMode = (new GuiTreePartManual("Fill", EnumPartType.Leaf, "modefilllt"));
    GuiTreePart allMode = (new GuiTreePartManual("All", EnumPartType.Leaf, "modealllt"));
    GuiTreePart overwriteMode = (new GuiTreePartManual("Overwrite", EnumPartType.Leaf, "modeoverwritelt"));
    GuiTreePart overwriteAllMode = (new GuiTreePartManual("Overwrite All", EnumPartType.Leaf, "modeoverwriteallllt"));
    GuiTreePart replaceMode = (new GuiTreePartManual("Replace", EnumPartType.Leaf, "modereplacelt"));
    GuiTreePart stencilMode = (new GuiTreePartManual("Stencil", EnumPartType.Leaf, "modestencillt"));
    GuiTreePart colorizeMode = (new GuiTreePartManual("Colorize", EnumPartType.Leaf, "modecolorizelt"));
    GuiTreePart materialWhitelist = (new GuiTreePartManual("Material Whitelist", EnumPartType.Leaf, "materialwhitelistlt"));
    GuiTreePart usingStructure = (new GuiTreePartManual("Using Structures", EnumPartType.Title, "usingstructureslt"));
    GuiTreePart savingStructure = (new GuiTreePartManual("Saving Structures", EnumPartType.Branch, "savingstructureslt"));
    GuiTreePart blueprint = (new GuiTreePartManual("Little Blueprint", EnumPartType.Leaf, "blueprintlt"));
    GuiTreePart staticStructure = (new GuiTreePartManual("Static Structures", EnumPartType.Branch, "staticstructurelt"));
    GuiTreePart fixedStructure = (new GuiTreePartManual("Fixed", EnumPartType.Leaf, "fixedstructurelt"));
    GuiTreePart ladderStructure = (new GuiTreePartManual("Ladder", EnumPartType.Leaf, "ladderstructurelt"));
    GuiTreePart bedStructure = (new GuiTreePartManual("Bed", EnumPartType.Leaf, "bedstructurelt"));
    GuiTreePart chairStructure = (new GuiTreePartManual("Chair", EnumPartType.Leaf, "chairstructurelt"));
    GuiTreePart storageStructure = (new GuiTreePartManual("Chair", EnumPartType.Leaf, "storagestructurelt"));
    GuiTreePart noclipStructure = (new GuiTreePartManual("No-Clip", EnumPartType.Leaf, "noclipstructurelt"));
    GuiTreePart lightStructure = (new GuiTreePartManual("Light", EnumPartType.Leaf, "lightstructurelt"));
    GuiTreePart messageStructure = (new GuiTreePartManual("Message", EnumPartType.Leaf, "messagestructurelt"));
    GuiTreePart alwaysOnLightStructure = (new GuiTreePartManual("Always On Light", EnumPartType.Leaf, "alwaysonlightstructurealet"));
    GuiTreePart ropeStructure = (new GuiTreePartManual("Rope Connection", EnumPartType.Leaf, "ropestructurealet"));
    GuiTreePart doorStructures = (new GuiTreePartManual("Door Structures", EnumPartType.Branch, "doorstructurelt"));
    GuiTreePart axisDoor = (new GuiTreePartManual("Axis Door", EnumPartType.Leaf, "axisdoorlt"));
    GuiTreePart slidingDoor = (new GuiTreePartManual("Sliding Door", EnumPartType.Leaf, "slidingdoorlt"));
    GuiTreePart advancedDoor = (new GuiTreePartManual("Advanced Door", EnumPartType.Leaf, "advanceddoorlt"));
    GuiTreePart doorActivator = (new GuiTreePartManual("Door Activator", EnumPartType.Leaf, "dooractivatordoorlt"));
    GuiTreePart doorLock = (new GuiTreePartManual("Door Lock", EnumPartType.Leaf, "doorlockdooralet"));
    GuiTreePart advancedStructures = (new GuiTreePartManual("Advanced Structures", EnumPartType.Branch, "advancedstructuresalet"));
    GuiTreePart triggerStructures = (new GuiTreePartManual("Trigger", EnumPartType.Leaf, "triggerstructurealet"));
    GuiTreePart stateMutatorStructures = (new GuiTreePartManual("State Mutator", EnumPartType.Leaf, "statemutatorstructurealet"));
    GuiTreePart camPlayerStructures = (new GuiTreePartManual("Camera Player", EnumPartType.Leaf, "camplayerstructurealet"));
    GuiTreePart audioStructures = (new GuiTreePartManual("Audio Structures", EnumPartType.Branch, "audiostructurelt"));
    GuiTreePart musicComposerStructure = (new GuiTreePartManual("Music Composer", EnumPartType.Leaf, "musiccomposerstructurealet"));
    GuiTreePart usingSignaling = (new GuiTreePartManual("Using Signaling", EnumPartType.Title, "usingsignalinglt"));
    GuiTreePart whatIsSignal = (new GuiTreePartManual("What is Signaling?", EnumPartType.Branch, "whatissignalinglt"));
    GuiTreePart signalingGUI = (new GuiTreePartManual("Signaling Interface", EnumPartType.Leaf, "signalinginterfacelt"));
    GuiTreePart logicGates = (new GuiTreePartManual("Logic Gates", EnumPartType.Branch, "logicgateslt"));
    GuiTreePart and = (new GuiTreePartManual("AND", EnumPartType.Leaf, "andlogicgatelt"));
    GuiTreePart or = (new GuiTreePartManual("OR", EnumPartType.Leaf, "orlogicgatelt"));
    GuiTreePart not = (new GuiTreePartManual("NOT", EnumPartType.Leaf, "notlogicgatelt"));
    GuiTreePart nand = (new GuiTreePartManual("NAND", EnumPartType.Leaf, "nandlogicgatelt"));
    GuiTreePart nor = (new GuiTreePartManual("NOR", EnumPartType.Leaf, "norlogicgatelt"));
    GuiTreePart xor = (new GuiTreePartManual("XOR", EnumPartType.Leaf, "xorlogicgatelt"));
    GuiTreePart xnor = (new GuiTreePartManual("XNOR", EnumPartType.Leaf, "xnorlogicgatelt"));
    GuiTreePart bitwiseGates = (new GuiTreePartManual("Logic Bitwise-Gates", EnumPartType.Branch, "logicbitwisegateslt"));
    GuiTreePart andB = (new GuiTreePartManual("B-AND", EnumPartType.Leaf, "bandlogicgatelt"));
    GuiTreePart orB = (new GuiTreePartManual("B-OR", EnumPartType.Leaf, "borlogicgatelt"));
    GuiTreePart notB = (new GuiTreePartManual("B-NOT", EnumPartType.Leaf, "bnotlogicgatelt"));
    GuiTreePart xorB = (new GuiTreePartManual("B-XOR", EnumPartType.Leaf, "bxorlogicgatelt"));
    GuiTreePart math = (new GuiTreePartManual("Mathematical Operators", EnumPartType.Branch, "mathematicaloperatorslt"));
    GuiTreePart add = (new GuiTreePartManual("Add", EnumPartType.Leaf, "mathaddlt"));
    GuiTreePart sub = (new GuiTreePartManual("Sub", EnumPartType.Leaf, "mathsublt"));
    GuiTreePart multi = (new GuiTreePartManual("Multi", EnumPartType.Leaf, "mathmultilt"));
    GuiTreePart div = (new GuiTreePartManual("Div", EnumPartType.Leaf, "mathdivlt"));
    GuiTreePart structureSignal = (new GuiTreePartManual("Structure's Signaling", EnumPartType.Branch, "structuresignalinglt"));
    GuiTreePart staticStructureSignal = (new GuiTreePartManual("Static Structures", EnumPartType.Branch, "staticstructuresignalinglt"));
    GuiTreePart bedSignal = (new GuiTreePartManual("Bed", EnumPartType.Leaf, "bedstructuresignalinglt"));
    GuiTreePart chairSignal = (new GuiTreePartManual("Chair", EnumPartType.Leaf, "chairstructuresignalinglt"));
    GuiTreePart storageSignal = (new GuiTreePartManual("Storage", EnumPartType.Leaf, "storagestructuresignalinglt"));
    GuiTreePart noclipSignal = (new GuiTreePartManual("No-Clip", EnumPartType.Leaf, "noclipstructuresignalinglt"));
    GuiTreePart lightSignal = (new GuiTreePartManual("Light", EnumPartType.Leaf, "lightstructuresignalinglt"));
    GuiTreePart messageSignal = (new GuiTreePartManual("Message", EnumPartType.Leaf, "messagestructuresignalinglt"));
    GuiTreePart doorStructureSignal = (new GuiTreePartManual("Door Structures", EnumPartType.Branch, "doorstructuresignalinglt"));
    GuiTreePart axisDoorSignal = (new GuiTreePartManual("Axis Door", EnumPartType.Leaf, "axisdoorstructuresignalinglt"));
    GuiTreePart slidingDoorSignal = (new GuiTreePartManual("Sliding Door", EnumPartType.Leaf, "slidingdoorstructuresignalinglt"));
    GuiTreePart advancedDoorSignal = (new GuiTreePartManual("Advanced Door", EnumPartType.Leaf, "advanceddoorstructuresignalinglt"));
    GuiTreePart doorActivatorSignal = (new GuiTreePartManual("Door Activator", EnumPartType.Leaf, "dooractivatorstructuresignalinglt"));
    GuiTreePart doorLockSignal = (new GuiTreePartManual("Door Lock", EnumPartType.Leaf, "doorlockstructuresignalinglt"));
    GuiTreePart advancedStructureSignal = (new GuiTreePartManual("Advanced Structures", EnumPartType.Branch, "advancedstructuresignalingalet"));
    GuiTreePart triggerSignal = (new GuiTreePartManual("Trigger", EnumPartType.Leaf, "triggerstructuresignalingalet"));
    GuiTreePart stateMutatorSignal = (new GuiTreePartManual("State Mutator", EnumPartType.Leaf, "statemutatorstructuresignalingalet"));
    GuiTreePart audioStructureSignal = (new GuiTreePartManual("Audio Structures", EnumPartType.Branch, "audiostructuresignalinglt"));
    GuiTreePart musicComposerSignal = (new GuiTreePartManual("Music Composer", EnumPartType.Leaf, "musiccomposerstructuresignalingalet"));
    GuiTreePart camPlayerSignal = (new GuiTreePartManual("Camera Player", EnumPartType.Leaf, "camplayerstructuresignalingalet"));
    GuiTreePart tutorial = (new GuiTreePartManual("Tutorial", EnumPartType.Title, "tutorialslt"));
    GuiTreePart littleCircuit = new GuiTreePartManual("Little Circuits", EnumPartType.Title, "circuitalet");
    GuiTreePart circuitClock = new GuiTreePartManual("Clock", EnumPartType.Leaf, "circuitclockalet");
    GuiTreePart circuitColorMon = new GuiTreePartManual("Colored ASCII Monitor", EnumPartType.Leaf, "circuitcolormonalet");
    GuiTreePart circuitMath = new GuiTreePartManual("Algebraic Operation", EnumPartType.Leaf, "circuitmathalet");
    GuiTreePart circuitMemory = new GuiTreePartManual("Memory", EnumPartType.Leaf, "circuitmemoryalet");
    GuiTreePart circuitPulser = new GuiTreePartManual("Pulser", EnumPartType.Leaf, "circuitpulseralet");
    GuiTreePart circuitRand = new GuiTreePartManual("Random Number Generator", EnumPartType.Leaf, "circuitrandalet");
    GuiTreePart circuitSwitch = new GuiTreePartManual("Switch", EnumPartType.Leaf, "circuitswitchalet");
    GuiTreePart circuitMagnitdue = new GuiTreePartManual("Magnitude Comparator", EnumPartType.Leaf, "circuitcomparatoralet");
    GuiTreePart circuitSplitter = new GuiTreePartManual("Splitter", EnumPartType.Leaf, "circuitsplitteralet");
    GuiTreePart circuitCombiner = new GuiTreePartManual("Combiner", EnumPartType.Leaf, "circuitcombineralet");
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
        //ahah funny joke.
        scrollBox.addControl(new GuiLabel("Hello, we've been trying to reach you", 0, 1540));
        scrollBox.addControl(new GuiLabel("about your car's extended warranty.", 0, 1550));
        controls.add(panelLeft);
        controls.add(panelRight);
        //GuiImage image = new GuiImage("image", testing(), 0, 0, 1);
        //scrollBoxLeft.addControl(image);
        panelLeft.controls.add(scrollBox);
        
        List<GuiTreePart> listOfMenus = new ArrayList<GuiTreePart>();
        welcome.addMenu(littleTile).addMenu(alet).addMenu(report);
        measuring.addMenu(tapeMeasure).addMenu(measurements.addMenu(box).addMenu(line).addMenu(compass));
        buildingTool.addMenu(chisel).addMenu(hammer).addMenu(glove).addMenu(paintBrush).addMenu(saw).addMenu(screwdriver);
        drawShape.addMenu(dragBox).addMenu(dragSlice).addMenu(dragPolygon).addMenu(dragWall).addMenu(dragPillar).addMenu(
            dragCurves).addMenu(dragCylinder).addMenu(dragSphere).addMenu(dragPyramid).addMenu(dragTile).addMenu(dragType)
                .addMenu(dragConnected).addMenu(dragCetered).addMenu(dragMagicWand);
        drawMode.addMenu(defaultMode).addMenu(fillMode).addMenu(allMode).addMenu(overwriteMode).addMenu(overwriteAllMode)
                .addMenu(replaceMode).addMenu(stencilMode).addMenu(colorizeMode);
        buildingWTile.addMenu(whatIsTile).addMenu(measuring).addMenu(drawShape).addMenu(drawMode).addMenu(markMode).addMenu(
            buildingTool).addMenu(materialWhitelist);
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
    
    public void gotoPage(String page) {
        for (GuiTreePart part : tree.listOfParts) {
            GuiTreePartManual partMan = ((GuiTreePartManual) part);
            if (partMan.pageName.equals(page)) {
                tree.openTo(partMan);
                this.raiseEvent(new GuiControlChangedEvent(partMan));
                break;
            }
        }
    }
    
    public void updateMessage(GuiTreePartManual part) {
        scrollBoxPage.scrolled.set(0);
        if (!part.equals(this.selected)) {
            scrollBoxPage.removeControls(" ");
            GuiTreePartManual paper = part;
            GuiModifibleTextBox box = new GuiModifibleTextBox("", part.getPage(), 0, 0, 350);
            
            scrollBoxPage.addControl(box);
            box.addImages();
            tree.highlightPart(paper);
            this.selected = part;
        }
    }
    
}
