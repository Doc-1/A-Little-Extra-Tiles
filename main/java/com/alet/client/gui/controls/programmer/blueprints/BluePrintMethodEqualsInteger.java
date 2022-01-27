package com.alet.client.gui.controls.programmer.blueprints;

import com.alet.client.gui.controls.programmer.BluePrintNode;

public class BluePrintMethodEqualsInteger extends GuiBluePrint {
	private BluePrintNode<Boolean> methodSenderNode = new BluePrintNode<Boolean>("methodSender", "", 0, BluePrintNode.METHOD_SENDER_NODE);
	private BluePrintNode<Boolean> methodReceiverNode = new BluePrintNode<Boolean>("methodReceiver", "", 0, BluePrintNode.METHOD_RECEIVER_NODE);
	
	private BluePrintNode<Boolean> bool = new BluePrintNode<Boolean>("boolean1", "Return", 1, BluePrintNode.RETURN_NODE);
	private BluePrintNode<Integer> integerA = new BluePrintNode<Integer>("integer1", "Integer A", 1, BluePrintNode.PARAMETER_NODE);
	private BluePrintNode<Integer> integerB = new BluePrintNode<Integer>("integer2", "Integer B", 2, BluePrintNode.PARAMETER_NODE);
	
	public BluePrintMethodEqualsInteger(String name, int x, int y) {
		super(name, "Integer Is Equal To", x, y);
		methodReceiverNode.setValue(false);
		addNode(methodReceiverNode);
		methodSenderNode.setValue(false);
		addNode(methodSenderNode);
		bool.setValue(false);
		addNode(bool);
		integerA.setValue(0);
		addNode(integerA);
		integerB.setValue(0);
		addNode(integerB);
	}
	
	@Override
	public void createControls() {
		// TODO Auto-generated method stub
		
	}
	
}
