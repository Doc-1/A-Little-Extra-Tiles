package com.alet.client.gui.controls.programmer.blueprints;

import com.alet.client.gui.controls.programmer.BluePrintConnection;

public class GuiNodeMethodEqualsInput extends GuiBluePrintNode {
    
    private BluePrintConnection<Boolean> methodSenderNode = new BluePrintConnection<Boolean>("methodSender", "", this, 0, BluePrintConnection.METHOD_SENDER_CONNECTION);
    private BluePrintConnection<Boolean> methodReceiverNode = new BluePrintConnection<Boolean>("methodReceiver", "", this, 0, BluePrintConnection.METHOD_RECEIVER_CONNECTION);
    
    private BluePrintConnection<Boolean> bool = new BluePrintConnection<Boolean>("boolean1", "Return", this, 1, BluePrintConnection.RETURN_CONNECTION);
    private BluePrintConnection<Boolean[]> inputA = new BluePrintConnection<Boolean[]>("EqInput1", "Input A", this, 1, BluePrintConnection.PARAMETER_CONNECTION);
    private BluePrintConnection<Boolean[]> inputB = new BluePrintConnection<Boolean[]>("input2", "input B", this, 2, BluePrintConnection.PARAMETER_CONNECTION);
    
    public GuiNodeMethodEqualsInput(String name, int x, int y) {
        super(name, "Input Is Equal To", GuiBluePrintNode.FLOW_NODE, x, y);
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
