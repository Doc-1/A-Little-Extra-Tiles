package com.alet.client.gui;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;
import org.lwjgl.util.Color;

import com.alet.client.gui.controls.GuiBezierCurve;
import com.alet.client.gui.controls.GuiColorablePanel;
import com.alet.client.gui.controls.GuiDragablePanel;
import com.alet.client.gui.controls.GuiTree;
import com.alet.client.gui.controls.GuiTreePart;
import com.alet.client.gui.controls.GuiTreePart.EnumPartType;
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
    public void controlClicked(GuiControlClickEvent event) {
        if (NumberUtils.isCreatable(event.source.name)) {
            int count = bluePrint.size();
            String caption = tree.listOfParts.get(Integer.parseInt(event.source.name)).caption;
            GuiBluePrintNode var = null;
            if (caption.contains("Integer Is Equal")) {
                var = new GuiNodeMethodEqualsInteger("eqInt" + count, 0, 0);
            } else if (caption.contains("Input Is Equal")) {
                var = new GuiNodeMethodEqualsInput("eqInput" + count, 0, 0);
            } else if (caption.contains("Event Pulse Recieved")) {
                var = new GuiNodeEventPulse("evPulse" + count, 0, 0);
                eventNode = var;
            } else if (caption.contains("Get Input")) {
                var = new GuiNodeGetInput("getInput" + count, 0, 0);
            } else if (caption.contains("Get Output")) {
                var = new GuiNodeGetOutput("getOutput" + count, 0, 0);
            } else if (caption.contains("Branch")) {
                var = new GuiNodeBranch("branch" + count, 0, 0);
            } else if (caption.contains("Set Color Monitor")) {
                var = new GuiNodeMethodSetColorDisplay("setCMonitor" + count, 0, 0);
            } else if (caption.contains("Set Integer")) {
                var = new GuiNodeSetInteger("setInteger" + count, 0, 0);
            } else if (caption.contains("Input to Integer")) {
                var = new GuiNodeToInteger("inputToInt" + count, 0, 0);
            }
            if (var != null) {
                drag.addControl(var);
                bluePrint.add(var);
                var.selected = true;
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
        GuiTreePart eventPulse = new GuiTreePart("Event Pulse Recieved", EnumPartType.Leaf);
        blueprint.addMenu(events.addMenu(eventPulse));
        
        GuiTreePart math = new GuiTreePart("Math", EnumPartType.Branch);
        GuiTreePart equals = new GuiTreePart("Equals", EnumPartType.Branch);
        GuiTreePart intEquals = new GuiTreePart("Integer Is Equal", EnumPartType.Leaf);
        GuiTreePart boolEquals = new GuiTreePart("Boolean Is Equal", EnumPartType.Leaf);
        GuiTreePart inputEquals = new GuiTreePart("Input Is Equal", EnumPartType.Leaf);
        equals.addMenu(intEquals).addMenu(boolEquals).addMenu(inputEquals);
        math.addMenu(equals);
        blueprint.addMenu(math);
        
        GuiTreePart wrapper = new GuiTreePart("Wrapper", EnumPartType.Branch);
        GuiTreePart wrapperInput = new GuiTreePart("Input Wrappers", EnumPartType.Branch);
        GuiTreePart inputToInteger = new GuiTreePart("Input to Integer", EnumPartType.Leaf);
        GuiTreePart inputToBoolean = new GuiTreePart("Input to Boolean", EnumPartType.Leaf);
        blueprint.addMenu(wrapper.addMenu(wrapperInput.addMenu(inputToInteger).addMenu(inputToBoolean)));
        
        GuiTreePart var = new GuiTreePart("Variable", EnumPartType.Branch);
        GuiTreePart get = new GuiTreePart("Getter", EnumPartType.Branch);
        GuiTreePart getInput = new GuiTreePart("Get Input", EnumPartType.Leaf);
        GuiTreePart getOutput = new GuiTreePart("Get Output", EnumPartType.Leaf);
        var.addMenu(get.addMenu(getInput).addMenu(getOutput));
        
        GuiTreePart set = new GuiTreePart("Setter", EnumPartType.Branch);
        GuiTreePart setInteger = new GuiTreePart("Set Integer", EnumPartType.Leaf);
        var.addMenu(set.addMenu(setInteger));
        
        GuiTreePart flow = new GuiTreePart("Flow Control", EnumPartType.Branch);
        GuiTreePart branch = new GuiTreePart("Branch", EnumPartType.Leaf);
        
        blueprint.addMenu(flow.addMenu(branch));
        
        GuiTreePart display = new GuiTreePart("Display", EnumPartType.Branch);
        GuiTreePart color = new GuiTreePart("Set Color Monitor", EnumPartType.Leaf);
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
