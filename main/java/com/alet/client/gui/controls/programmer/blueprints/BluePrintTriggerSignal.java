package com.alet.client.gui.controls.programmer.blueprints;

import com.alet.client.gui.controls.programmer.BluePrintNode;

public class BluePrintTriggerSignal extends GuiBluePrint {
	
	private BluePrintNode<Boolean> methodSenderNode = new BluePrintNode<Boolean>("methodSender", "", 0, BluePrintNode.METHOD_SENDER_NODE);
	private BluePrintNode<Boolean[]> input = new BluePrintNode<Boolean[]>("input1", "Input", 1, BluePrintNode.RETURN_NODE);
	
	private BluePrintNode<Integer> integer = new BluePrintNode<Integer>("int1", "State", 2, BluePrintNode.RETURN_NODE);
	
	public BluePrintTriggerSignal(String name, int x, int y) {
		super(name, "Event Pulse Recieved", x, y);
		methodSenderNode.setValue(false);
		this.addNode(methodSenderNode);
		input.setValue(new Boolean[] { false });
		this.addNode(input);
	}
	
	@Override
	public void createControls() {
		/*
		                          */
	}
	
}
