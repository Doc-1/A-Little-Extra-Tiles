package com.alet.client.gui.controls.programmer.blueprints;

import com.alet.client.gui.controls.programmer.BluePrintConnection;
import com.creativemd.creativecore.common.gui.controls.gui.GuiTextfield;

public class GuiNodeSetInteger extends GuiBluePrintNode {
    
    private BluePrintConnection<Boolean> methodSenderNode = new BluePrintConnection<Boolean>("methodSender", "", this, 0, BluePrintConnection.METHOD_SENDER_CONNECTION);
    private BluePrintConnection<Boolean> methodReceiverNode = new BluePrintConnection<Boolean>("methodReceiver", "", this, 0, BluePrintConnection.METHOD_RECEIVER_CONNECTION);
    
    private BluePrintConnection<Integer[]> output = new BluePrintConnection<Integer[]>("output", "Source Output", this, 1, BluePrintConnection.PARAMETER_CONNECTION);
    private BluePrintConnection<Integer> integer = new BluePrintConnection<Integer>("integer", "Integer", this, 2, BluePrintConnection.PARAMETER_CONNECTION);
    
    public GuiNodeSetInteger(String name, int x, int y) {
        super(name, "Set Integer", x, y);
        
        methodReceiverNode.setValue(false);
        addNode(methodReceiverNode);
        methodSenderNode.setValue(false);
        addNode(methodSenderNode);
        this.addNode(integer);
        addNode(output);
        output.setValue(new Integer[] { 0 });
        integer.setValue(0);
        this.height = 57;
    }
    
    @Override
    public void createControls() {
        GuiTextfield red = new GuiTextfield("0", 0, 37, 32, 7);
        red.setNumbersIncludingNegativeOnly();
        
        this.addControl(red);
    }
    
}
