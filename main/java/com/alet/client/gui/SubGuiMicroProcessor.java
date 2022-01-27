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
import com.alet.client.gui.controls.programmer.BluePrintNode;
import com.alet.client.gui.controls.programmer.blueprints.BluePrintMethodEqualsInput;
import com.alet.client.gui.controls.programmer.blueprints.BluePrintMethodEqualsInteger;
import com.alet.client.gui.controls.programmer.blueprints.GuiBluePrint;
import com.creativemd.creativecore.common.gui.GuiRenderHelper;
import com.creativemd.creativecore.common.gui.client.style.Style;
import com.creativemd.creativecore.common.gui.container.SubGui;
import com.creativemd.creativecore.common.gui.controls.gui.GuiScrollBox;
import com.creativemd.creativecore.common.gui.event.gui.GuiControlClickEvent;
import com.creativemd.littletiles.common.structure.LittleStructure;
import com.n247s.api.eventapi.eventsystem.CustomEventSubscribe;

public class SubGuiMicroProcessor extends SubGui {
	GuiDragablePanel drag = new GuiDragablePanel("drag", 208, 0, 300, 300, 600, 600);
	public List<GuiBluePrint> bluePrint = new ArrayList<GuiBluePrint>();
	public BluePrintNode selectedNode;
	GuiScrollBox scrollBoxLeft;
	
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
			String caption = tree.listOfParts.get(Integer.parseInt(event.source.name)).caption;
			if (caption.contains("Integer Is Equal")) {
				GuiBluePrint var = new BluePrintMethodEqualsInteger("", 0, 0);
				drag.addControl(var);
				bluePrint.add(var);
				var.selected = true;
			} else if (caption.contains("Input Is Equal")) {
				GuiBluePrint var = new BluePrintMethodEqualsInput("", 0, 0);
				drag.addControl(var);
				bluePrint.add(var);
				var.selected = true;
			}
		}
		/*		
				if (event.source.is("add"))
					if (event.button == 0) {
						GuiBluePrint var = new BluePrintTriggerSignal("", 0, 0);
						this.addControl(var);
						bluePrint.add(var);
						var.selected = true;
					} else if (event.button == 1) {
						GuiBluePrint var = new BluePrintMethodEqualsInteger("", 0, 0);
						this.addControl(var);
						bluePrint.add(var);
						var.selected = true;
					}
				if (event.source.is("add2"))
					if (event.button == 0) {
						GuiBluePrint var = new BluePrintGetInput("", 0, 0);
						this.addControl(var);
						bluePrint.add(var);
						var.selected = true;
					} else if (event.button == 1) {
						GuiBluePrint var = new BluePrintMethodEqualsInput("", 0, 0);
						this.addControl(var);
						bluePrint.add(var);
						var.selected = true;
					}
					*/
		if (event.source instanceof BluePrintNode) {
			if (selectedNode == null) {
				selectedNode = (BluePrintNode) event.source;
				selectedNode.isSelected = true;
			} else if (selectedNode.canAddConnection((BluePrintNode) event.source)) {
				selectedNode.addConnection((BluePrintNode) event.source);
				((BluePrintNode) event.source).isSelected = true;
				this.addControl(new GuiBezierCurve("", 0, 0, selectedNode, (BluePrintNode) event.source, 400, 400));
				selectedNode = null;
			} else {
				selectedNode.isSelected = false;
				((BluePrintNode) event.source).isSelected = false;
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
		listOfMenus.add(blueprint);
		tree = new GuiTree("tree", 0, 0, 194, listOfMenus, true, 0, 0, 50);
		scrollBox.controls.add(tree);
		this.controls.add(drag);
	}
	
}
