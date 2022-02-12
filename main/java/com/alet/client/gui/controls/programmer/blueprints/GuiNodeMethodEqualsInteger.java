package com.alet.client.gui.controls.programmer.blueprints;

import com.alet.client.gui.controls.programmer.BluePrintConnection;

public class GuiNodeMethodEqualsInteger extends GuiBluePrintNode {
    private BluePrintConnection<Boolean> methodSenderNode = new BluePrintConnection<Boolean>("methodSender", "", this, 0, BluePrintConnection.METHOD_SENDER_CONNECTION);
    private BluePrintConnection<Boolean> methodReceiverNode = new BluePrintConnection<Boolean>("methodReceiver", "", this, 0, BluePrintConnection.METHOD_RECEIVER_CONNECTION);
    
    private BluePrintConnection<Boolean> bool = new BluePrintConnection<Boolean>("boolean1", "Return", this, 1, BluePrintConnection.RETURN_CONNECTION);
    private BluePrintConnection<Integer> integerA = new BluePrintConnection<Integer>("integer1", "Integer A", this, 1, BluePrintConnection.PARAMETER_CONNECTION);
    private BluePrintConnection<Integer> integerB = new BluePrintConnection<Integer>("integer2", "Integer B", this, 2, BluePrintConnection.PARAMETER_CONNECTION);
    
    public GuiNodeMethodEqualsInteger(String name, int x, int y) {
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
