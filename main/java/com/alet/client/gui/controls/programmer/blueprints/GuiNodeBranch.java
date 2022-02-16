package com.alet.client.gui.controls.programmer.blueprints;

import com.alet.client.gui.controls.programmer.BluePrintConnection;

public class GuiNodeBranch extends GuiBluePrintNode {
    
    private BluePrintConnection<Boolean> receiver = new BluePrintConnection<Boolean>("receiver", "receiver", this, 0, BluePrintConnection.METHOD_RECEIVER_CONNECTION);
    private BluePrintConnection<Boolean> t = new BluePrintConnection<Boolean>("t", "True", this, 1, BluePrintConnection.METHOD_SENDER_CONNECTION);
    private BluePrintConnection<Boolean> f = new BluePrintConnection<Boolean>("f", "False", this, 2, BluePrintConnection.METHOD_SENDER_CONNECTION);
    private BluePrintConnection<Boolean> bool = new BluePrintConnection<Boolean>("boolean", "Condition", this, 1, BluePrintConnection.PARAMETER_CONNECTION);
    
    public GuiNodeBranch(String name, int x, int y) {
        super(name, "Branch", GuiBluePrintNode.FLOW_NODE, x, y);
        this.addNode(receiver);
        receiver.setValue(false);
        this.addNode(t);
        t.setValue(false);
        this.addNode(f);
        f.setValue(false);
        this.addNode(bool);
        bool.setValue(false);
        this.height = 50;
    }
    
    @Override
    public void createControls() {
        
    }
    
}
