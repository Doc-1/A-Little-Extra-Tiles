package com.alet.client.gui.controls.programmer.blueprints;

import com.alet.client.gui.controls.programmer.BluePrintConnection;

public class GuiNodeToInteger extends GuiBluePrintNode {
    private BluePrintConnection<Boolean[]> input = new BluePrintConnection<Boolean[]>("input", "Input", this, 1, BluePrintConnection.PARAMETER_CONNECTION);
    
    private BluePrintConnection<Integer> integer = new BluePrintConnection<Integer>("int", "Integer", this, 1, BluePrintConnection.RETURN_CONNECTION);
    
    public GuiNodeToInteger(String name, int x, int y) {
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
