package com.alet.client.gui.controls.programmer.blueprints;

import com.alet.client.gui.controls.programmer.BluePrintNode;

public class BluePrintToInteger extends GuiBluePrint {
	private BluePrintNode<Boolean[]> input = new BluePrintNode<Boolean[]>("state", "Input", 1, BluePrintNode.PARAMETER_NODE);
	
	private BluePrintNode<Integer> integer = new BluePrintNode<Integer>("int", "Integer", 1, BluePrintNode.RETURN_NODE);
	
	public BluePrintToInteger(String name, int x, int y) {
		super(name, "To Integer", x, y);
		this.addNode(input);
		input.setValue(new Boolean[] { false });
		this.addNode(integer);
		integer.setValue(0);
	}
	
	@Override
	public void createControls() {
		
	}
	
}
