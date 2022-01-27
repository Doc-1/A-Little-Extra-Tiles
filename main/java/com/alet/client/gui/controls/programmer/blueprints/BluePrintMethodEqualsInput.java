package com.alet.client.gui.controls.programmer.blueprints;

import com.alet.client.gui.controls.programmer.BluePrintNode;

public class BluePrintMethodEqualsInput extends GuiBluePrint {
	
	private BluePrintNode<Boolean> methodSenderNode = new BluePrintNode<Boolean>("methodSender", "", 0, BluePrintNode.METHOD_SENDER_NODE);
	private BluePrintNode<Boolean> methodReceiverNode = new BluePrintNode<Boolean>("methodReceiver", "", 0, BluePrintNode.METHOD_RECEIVER_NODE);
	
	private BluePrintNode<Boolean> bool = new BluePrintNode<Boolean>("boolean1", "Return", 1, BluePrintNode.RETURN_NODE);
	private BluePrintNode<Boolean[]> inputA = new BluePrintNode<Boolean[]>("input1", "Input A", 1, BluePrintNode.PARAMETER_NODE);
	private BluePrintNode<Boolean[]> inputB = new BluePrintNode<Boolean[]>("input2", "input B", 2, BluePrintNode.PARAMETER_NODE);
	
	public BluePrintMethodEqualsInput(String name, int x, int y) {
		super(name, "Input Is Equal To", x, y);
		methodReceiverNode.setValue(false);
		addNode(methodReceiverNode);
		methodSenderNode.setValue(false);
		addNode(methodSenderNode);
		bool.setValue(false);
		addNode(bool);
		inputA.setValue(new Boolean[] { false });
		addNode(inputA);
		inputB.setValue(new Boolean[] { false });
		addNode(inputB);
	}
	
	@Override
	public void createControls() {
		// TODO Auto-generated method stub
		
	}
	
}
