package com.alet.client.gui.controls.programmer.blueprints;

import com.alet.client.gui.controls.programmer.BluePrintConnection;
import com.creativemd.creativecore.common.gui.CoreControl;
import com.creativemd.creativecore.common.gui.controls.gui.GuiTextfield;

public class GuiNodeSetInteger extends GuiBluePrintNode {
    
    private BluePrintConnection<Boolean> methodSenderNode = new BluePrintConnection<Boolean>("methodSender", "", this, 0, BluePrintConnection.METHOD_SENDER_CONNECTION);
    private BluePrintConnection<Boolean> methodReceiverNode = new BluePrintConnection<Boolean>("methodReceiver", "", this, 0, BluePrintConnection.METHOD_RECEIVER_CONNECTION);
    
    private BluePrintConnection<String> output = new BluePrintConnection<String>("output", "Source Output", this, 1, BluePrintConnection.PARAMETER_CONNECTION);
    private BluePrintConnection<Integer> integer = new BluePrintConnection<Integer>("integer", "Integer", this, 2, BluePrintConnection.PARAMETER_CONNECTION);
    
    public GuiNodeSetInteger(int index) {
        super("setInteger" + index, "Set Integer", GuiBluePrintNode.FLOW_NODE);
        
        methodReceiverNode.setValue(false);
        addNode(methodReceiverNode);
        methodSenderNode.setValue(false);
        addNode(methodSenderNode);
        addNode(integer);
        addNode(output);
        output.setValue("");
        integer.setValue(0);
        this.height = 57;
    }
    
    @Override
    public void createControls() {
        GuiTextfield int_0 = new GuiTextfield("int_0", "0", 0, 37, 32, 7);
        int_0.setNumbersIncludingNegativeOnly();
        this.addControl(int_0);
    }
    
    @Override
    public void updateValue(CoreControl control) {
        if (control.is("int_0") && !((GuiTextfield) get("int_0")).text.equals("")) {
            this.integer.setValue(Integer.parseInt(((GuiTextfield) get("int_0")).text));
        }
    }
    
}
