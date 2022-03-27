package com.alet.client.gui.controls.programmer.blueprints;

import com.alet.client.gui.controls.programmer.BluePrintConnection;
import com.creativemd.creativecore.common.gui.CoreControl;

public class GuiNodeEventPulse extends GuiBluePrintNode {
    
    private BluePrintConnection<Boolean> methodSenderNode = new BluePrintConnection<Boolean>("methodSender", "", this, 0, BluePrintConnection.METHOD_SENDER_CONNECTION);
    private BluePrintConnection<Boolean[]> input = new BluePrintConnection<Boolean[]>("EventInput1", "Input", this, 1, BluePrintConnection.RETURN_CONNECTION);
    
    private BluePrintConnection<Integer> integer = new BluePrintConnection<Integer>("int1", "State", this, 2, BluePrintConnection.RETURN_CONNECTION);
    
    public GuiNodeEventPulse(int index) {
        super("evPulse" + index, "Event Pulse Recieved", GuiBluePrintNode.EVENT_NODE);
        methodSenderNode.setValue(false);
        this.addNode(methodSenderNode);
        input.setValue(new Boolean[] { false, false, false, true });
        this.addNode(input);
    }
    
    @Override
    public void createControls() {}
    
    @Override
    public void updateValue(CoreControl control) {
        // TODO Auto-generated method stub
        
    }
    
}
