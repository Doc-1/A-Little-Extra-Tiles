package com.alet.client.gui.controls.programmer.blueprints;

import java.util.ArrayList;
import java.util.List;

import com.alet.client.gui.controls.programmer.BluePrintConnection;
import com.creativemd.creativecore.common.gui.controls.gui.GuiComboBox;
import com.creativemd.creativecore.common.gui.controls.gui.GuiComboBoxExtension;

public class GuiNodeGetOutput extends GuiBluePrintNode {
    
    private BluePrintConnection<Integer[]> input = new BluePrintConnection<Integer[]>("output", "Output", this, 1, BluePrintConnection.RETURN_CONNECTION);
    
    public GuiNodeGetOutput(String name, int x, int y) {
        super(name, "Get Output", GuiBluePrintNode.FLOWLESS_NODE, x, y);
        this.addNode(input);
        input.setValue(new Integer[] { 0 });
        this.height = 42;
    }
    
    @Override
    public void createControls() {
        List<String> list = new ArrayList<String>();
        list.add("o0");
        list.add("o1");
        list.add("o2");
        GuiComboBox box = new GuiComboBox("", this.width - 45, 24, 32, list) {
            
            @Override
            protected GuiComboBoxExtension createBox() {
                return super.createBox();
            }
        };
        box.height = 12;
        this.addControl(box);
    }
    
}
