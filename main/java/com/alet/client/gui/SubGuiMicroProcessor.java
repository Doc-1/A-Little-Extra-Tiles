package com.alet.client.gui;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.Color;

import com.alet.client.gui.controls.GuiBezierCurve;
import com.alet.client.gui.controls.GuiColorablePanel;
import com.alet.client.gui.controls.GuiDragablePanel;
import com.alet.client.gui.controls.GuiTree;
import com.alet.client.gui.controls.GuiTreePart;
import com.alet.client.gui.controls.GuiTreePart.EnumPartType;
import com.alet.client.gui.controls.GuiTreePartNode;
import com.alet.client.gui.controls.programmer.BluePrintConnection;
import com.alet.client.gui.controls.programmer.BlueprintCompiler;
import com.alet.client.gui.controls.programmer.blueprints.GuiBluePrintNode;
import com.alet.client.gui.controls.programmer.blueprints.GuiNodeBranch;
import com.alet.client.gui.controls.programmer.blueprints.GuiNodeEventPulse;
import com.alet.client.gui.controls.programmer.blueprints.GuiNodeGetInput;
import com.alet.client.gui.controls.programmer.blueprints.GuiNodeGetOutput;
import com.alet.client.gui.controls.programmer.blueprints.GuiNodeMethodEqualsInput;
import com.alet.client.gui.controls.programmer.blueprints.GuiNodeMethodEqualsInteger;
import com.alet.client.gui.controls.programmer.blueprints.GuiNodeMethodSetColorDisplay;
import com.alet.client.gui.controls.programmer.blueprints.GuiNodeSetInteger;
import com.alet.client.gui.controls.programmer.blueprints.GuiNodeToInteger;
import com.alet.common.packet.PacketUpdateStructureFromClient;
import com.alet.common.structure.type.premade.signal.LittleCircuitMicroprocessor;
import com.creativemd.creativecore.common.gui.GuiRenderHelper;
import com.creativemd.creativecore.common.gui.client.style.Style;
import com.creativemd.creativecore.common.gui.container.SubGui;
import com.creativemd.creativecore.common.gui.controls.gui.GuiButton;
import com.creativemd.creativecore.common.gui.controls.gui.GuiScrollBox;
import com.creativemd.creativecore.common.gui.event.gui.GuiControlChangedEvent;
import com.creativemd.creativecore.common.gui.event.gui.GuiControlClickEvent;
import com.creativemd.creativecore.common.packet.PacketHandler;
import com.n247s.api.eventapi.eventsystem.CustomEventSubscribe;

import net.minecraft.nbt.NBTTagCompound;

public class SubGuiMicroProcessor extends SubGui {
    GuiDragablePanel drag = new GuiDragablePanel("drag", 208, 0, 580, 388, 1000, 1000);
    public List<GuiBluePrintNode> bluePrint = new ArrayList<GuiBluePrintNode>();
    public GuiBluePrintNode eventNode;
    public BluePrintConnection selectedNode;
    GuiScrollBox scrollBoxLeft;
    public NBTTagCompound scriptNBT = new NBTTagCompound();
    public NBTTagCompound structureNBT = new NBTTagCompound();
    public LittleCircuitMicroprocessor structure;
    
    GuiTree tree;
    
    public SubGuiMicroProcessor(LittleCircuitMicroprocessor structure) {
        this.structure = structure;
        this.width = 800;
        this.height = 400;
    }
    
    public void openedGui() {
        structure.writeToNBT(structureNBT);
    }
    
    @Override
    public void closeGui() {
        structureNBT.setTag("script", this.scriptNBT);
        PacketHandler.sendPacketToServer(new PacketUpdateStructureFromClient(structure.getStructureLocation(), structureNBT));
        super.closeGui();
    }
    
    @Override
    protected void renderContent(GuiRenderHelper helper, Style style, int width, int height) {
        //helper.drawLine(0, 20, 10, 50, ColorUtils.BLACK);
    }
    
    @CustomEventSubscribe
    public void updateValue(GuiControlChangedEvent event) {
        if (event.source.parent instanceof GuiBluePrintNode) {
            GuiBluePrintNode parent = (GuiBluePrintNode) event.source.parent;
            parent.updateValue(event.source);
        }
    }
    
    @CustomEventSubscribe
    public void controlClicked(GuiControlClickEvent event) {
        if (event.source instanceof GuiTreePartNode) {
            int count = bluePrint.size();
            
            try {
                GuiBluePrintNode var = ((GuiTreePartNode) event.source).nodeClass.getConstructor(int.class).newInstance(count);
                if (var != null) {
                    if (var.nodeType == GuiBluePrintNode.EVENT_NODE)
                        eventNode = var;
                    drag.addControl(var);
                    bluePrint.add(var);
                    var.selected = true;
                }
            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
        }
        
        if (event.source instanceof BluePrintConnection) {
            if (selectedNode == null) {
                selectedNode = (BluePrintConnection) event.source;
                selectedNode.isSelected = true;
            } else if (selectedNode.canAddConnection((BluePrintConnection) event.source)) {
                selectedNode.addConnection((BluePrintConnection) event.source);
                ((BluePrintConnection) event.source).isSelected = true;
                drag.addControl(new GuiBezierCurve("", 0, 0, selectedNode, (BluePrintConnection) event.source, 1000, 1000));
                selectedNode = null;
            } else {
                selectedNode.isSelected = false;
                ((BluePrintConnection) event.source).isSelected = false;
                selectedNode = null;
            }
            
        }
    }
    
    @Override
    public void createControls() {
        GuiColorablePanel panelLeft = new GuiColorablePanel("tableLeft", 0, 0, 200, 388, new Color(0, 0, 0), new Color(198, 198, 198));
        scrollBoxLeft = new GuiScrollBox("scrollBoxLeft", 0, 0, 379, 382);
        GuiScrollBox scrollBox = new GuiScrollBox("scrollBox", 0, 0, 194, 382);
        panelLeft.controls.add(scrollBox);
        scrollBox.setStyle(defaultStyle);
        
        controls.add(panelLeft);
        List<GuiTreePart> listOfMenus = new ArrayList<GuiTreePart>();
        GuiTreePart blueprint = new GuiTreePart("Blueprints", EnumPartType.Title);
        GuiTreePart events = new GuiTreePart("Events", EnumPartType.Branch);
        GuiTreePartNode eventPulse = new GuiTreePartNode("Event Pulse Recieved", EnumPartType.Leaf, GuiNodeEventPulse.class);
        blueprint.addMenu(events.addMenu(eventPulse));
        
        GuiTreePart math = new GuiTreePart("Math", EnumPartType.Branch);
        GuiTreePart equals = new GuiTreePart("Equals", EnumPartType.Branch);
        GuiTreePartNode intEquals = new GuiTreePartNode("Integer Is Equal", EnumPartType.Leaf, GuiNodeMethodEqualsInteger.class);
        //GuiTreePartNode boolEquals = new GuiTreePartNode("Boolean Is Equal", EnumPartType.Leaf, guinodeeq);
        GuiTreePartNode inputEquals = new GuiTreePartNode("Input Is Equal", EnumPartType.Leaf, GuiNodeMethodEqualsInput.class);
        equals.addMenu(intEquals).addMenu(inputEquals);
        math.addMenu(equals);
        blueprint.addMenu(math);
        
        GuiTreePart wrapper = new GuiTreePart("Wrapper", EnumPartType.Branch);
        GuiTreePart wrapperInput = new GuiTreePart("Input Wrappers", EnumPartType.Branch);
        GuiTreePartNode inputToInteger = new GuiTreePartNode("Input to Integer", EnumPartType.Leaf, GuiNodeToInteger.class);
        //GuiTreePartNode inputToBoolean = new GuiTreePartNode("Input to Boolean", EnumPartType.Leaf, GuiNodeToInteger);
        blueprint.addMenu(wrapper.addMenu(wrapperInput.addMenu(inputToInteger)));
        
        GuiTreePart var = new GuiTreePart("Variable", EnumPartType.Branch);
        GuiTreePart get = new GuiTreePart("Getter", EnumPartType.Branch);
        GuiTreePartNode getInput = new GuiTreePartNode("Get Input", EnumPartType.Leaf, GuiNodeGetInput.class);
        GuiTreePartNode getOutput = new GuiTreePartNode("Get Output", EnumPartType.Leaf, GuiNodeGetOutput.class);
        var.addMenu(get.addMenu(getInput).addMenu(getOutput));
        
        GuiTreePart set = new GuiTreePart("Setter", EnumPartType.Branch);
        GuiTreePartNode setInteger = new GuiTreePartNode("Set Integer", EnumPartType.Leaf, GuiNodeSetInteger.class);
        var.addMenu(set.addMenu(setInteger));
        
        GuiTreePart flow = new GuiTreePart("Flow Control", EnumPartType.Branch);
        GuiTreePartNode branch = new GuiTreePartNode("Branch", EnumPartType.Leaf, GuiNodeBranch.class);
        
        blueprint.addMenu(flow.addMenu(branch));
        
        GuiTreePart display = new GuiTreePart("Display", EnumPartType.Branch);
        GuiTreePartNode color = new GuiTreePartNode("Set Color Monitor", EnumPartType.Leaf, GuiNodeMethodSetColorDisplay.class);
        blueprint.addMenu(display.addMenu(color));
        blueprint.addMenu(var);
        
        listOfMenus.add(blueprint);
        tree = new GuiTree("tree", 0, 0, 194, listOfMenus, true, 0, 0, 50);
        scrollBox.controls.add(tree);
        this.controls.add(drag);
        drag.addControl(new GuiButton("test", 0, 0) {
            
            @Override
            public void onClicked(int x, int y, int button) {
                BlueprintCompiler.compileScriptToNBT((SubGuiMicroProcessor) this.getGui());
            }
        });
        openedGui();
    }
    
}
