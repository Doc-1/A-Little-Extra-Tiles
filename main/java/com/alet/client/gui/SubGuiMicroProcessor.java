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
import com.alet.client.gui.controls.programmer.blueprints.GuiBluePrintNode;
import com.alet.client.gui.controls.programmer.blueprints.GuiNodeBranch;
import com.alet.client.gui.controls.programmer.blueprints.GuiNodeGetInput;
import com.alet.client.gui.controls.programmer.blueprints.GuiNodeGetOutput;
import com.alet.client.gui.controls.programmer.blueprints.GuiNodeMethodEqualsInput;
import com.alet.client.gui.controls.programmer.blueprints.GuiNodeMethodEqualsInteger;
import com.alet.client.gui.controls.programmer.blueprints.GuiNodeMethodSetColorDisplay;
import com.alet.client.gui.controls.programmer.blueprints.GuiNodeSetInteger;
import com.alet.client.gui.controls.programmer.blueprints.GuiNodeToInteger;
import com.alet.client.gui.controls.programmer.blueprints.GuiNodeTriggerSignal;
import com.creativemd.creativecore.common.gui.GuiRenderHelper;
import com.creativemd.creativecore.common.gui.client.style.Style;
import com.creativemd.creativecore.common.gui.container.SubGui;
import com.creativemd.creativecore.common.gui.controls.gui.GuiButton;
import com.creativemd.creativecore.common.gui.controls.gui.GuiScrollBox;
import com.creativemd.creativecore.common.gui.event.gui.GuiControlClickEvent;
import com.creativemd.littletiles.common.structure.LittleStructure;
import com.n247s.api.eventapi.eventsystem.CustomEventSubscribe;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class SubGuiMicroProcessor extends SubGui {
    GuiDragablePanel drag = new GuiDragablePanel("drag", 208, 0, 380, 388, 1000, 1000);
    public List<GuiBluePrintNode> bluePrint = new ArrayList<GuiBluePrintNode>();
    public GuiBluePrintNode eventNode;
    public BluePrintConnection selectedNode;
    GuiScrollBox scrollBoxLeft;
    NBTTagCompound nbt = new NBTTagCompound();
    
    GuiTree tree;
    
    public SubGuiMicroProcessor(LittleStructure structure) {
        this.width = 600;
        this.height = 400;
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
                var = new GuiNodeTriggerSignal("evPulse" + count, 0, 0);
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
                NBTTagList nbtListObject = new NBTTagList();
                NBTTagList nbtListSender = new NBTTagList();
                SubGuiMicroProcessor subGui = ((SubGuiMicroProcessor) this.getGui());
                subGui.nbt = new NBTTagCompound();
                if (!subGui.eventNode.methodSenderConn.receiver.isEmpty()) {
                    NBTTagCompound nbtSender = new NBTTagCompound();
                    NBTTagCompound nbtReturn = new NBTTagCompound();
                    BluePrintConnection conn = (BluePrintConnection) subGui.eventNode.methodSenderConn.receiver.get(0);
                    GuiBluePrintNode receiver = conn.parentNode;
                    for (BluePrintConnection c : subGui.eventNode.connections) {
                        if (BluePrintConnection.METHOD_SENDER_CONNECTION == c.connectionType) {
                            if (c.receiver.size() != 0) {
                                BluePrintConnection cRec = (BluePrintConnection) c.receiver.get(0);
                                nbtSender.setString(c.name, cRec.parentNode.name + "." + cRec.name);
                            }
                        }
                        
                    }
                    nbtListSender.appendTag(nbtSender);
                    
                    nbtListObject.appendTag(nbtListSender);
                    
                    subGui.nbt.setTag(subGui.eventNode.name, nbtListObject);
                    nextNode(receiver, nbtListObject);
                }
                System.out.println(subGui.nbt);
            }
            
            public String getValue(Object value) {
                if (value instanceof Boolean[]) {
                    Boolean[] io = (Boolean[]) value;
                    String v = "[";
                    
                    for (int i = 0; i < io.length; i++) {
                        v += String.valueOf(io[i]);
                        if (i != io.length - 1)
                            v += ",";
                    }
                    
                    return v + "]";
                }
                if (value instanceof Integer) {
                    Integer i = (Integer) value;
                    return String.valueOf(i.intValue());
                }
                return "";
            }
            
            public void nextNode(GuiBluePrintNode node, NBTTagList ignore) {
                NBTTagList nbtListObject = new NBTTagList();
                NBTTagList nbtListSender = new NBTTagList();
                NBTTagList nbtListParameter = new NBTTagList();
                SubGuiMicroProcessor subGui = ((SubGuiMicroProcessor) this.getGui());
                NBTTagCompound nbtSender = new NBTTagCompound();
                NBTTagCompound nbtParameter = new NBTTagCompound();
                for (BluePrintConnection c : node.connections) {
                    if (BluePrintConnection.METHOD_SENDER_CONNECTION == c.connectionType) {
                        if (c.receiver.size() != 0) {
                            BluePrintConnection cRec = (BluePrintConnection) c.receiver.get(0);
                            nbtSender.setString(c.name, cRec.parentNode.name + "." + cRec.name);
                        }
                    }
                    if (BluePrintConnection.PARAMETER_CONNECTION == c.connectionType) {
                        if (c.sender != null) {
                            BluePrintConnection cSend = (BluePrintConnection) c.sender;
                            nbtParameter.setString(c.name, cSend.parentNode.name + "." + cSend.name);
                        } else {
                            nbtParameter.setString(c.name, getValue(c.value));
                        }
                    }
                }
                nbtListSender.appendTag(nbtSender);
                nbtListParameter.appendTag(nbtParameter);
                
                nbtListObject.appendTag(nbtListSender);
                nbtListObject.appendTag(nbtListParameter);
                subGui.nbt.setTag(node.name, nbtListObject);
                
            }
            
            public void connections(GuiBluePrintNode node) {
                
                NBTTagList nbtListObject = new NBTTagList();
                NBTTagList nbtListParameter = new NBTTagList();
                SubGuiMicroProcessor subGui = ((SubGuiMicroProcessor) this.getGui());
                NBTTagCompound nbtParameter = new NBTTagCompound();
                for (BluePrintConnection c : node.connections) {
                    if (BluePrintConnection.PARAMETER_CONNECTION == c.connectionType) {
                        if (c.sender != null) {
                            BluePrintConnection cSend = (BluePrintConnection) c.sender;
                            nbtParameter.setString(c.name, cSend.parentNode.name + "." + cSend.name);
                        } else {
                            nbtParameter.setString(c.name, getValue(c.value));
                        }
                    }
                }
                nbtListParameter.appendTag(nbtParameter);
                
                nbtListObject.appendTag(nbtListParameter);
                subGui.nbt.setTag(node.name, nbtListObject);
                
            }
            
        });
    }
    
}
