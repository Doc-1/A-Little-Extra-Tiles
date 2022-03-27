package com.alet.client.gui.controls.programmer.blueprints;

import java.util.ArrayList;
import java.util.List;

import com.alet.client.gui.controls.programmer.BluePrintConnection;
import com.creativemd.creativecore.common.gui.CoreControl;
import com.creativemd.creativecore.common.gui.controls.gui.GuiComboBox;
import com.creativemd.creativecore.common.gui.controls.gui.GuiComboBoxExtension;

public class GuiNodeGetInput extends GuiBluePrintNode {
    
    private BluePrintConnection<Boolean[]> input = new BluePrintConnection<Boolean[]>("input", "Input", this, 1, BluePrintConnection.RETURN_CONNECTION);
    
    public GuiNodeGetInput(int index) {
        super("getInput" + index, "Get Input", GuiBluePrintNode.GETTER_NODE);
        this.addNode(input);
        input.setValue(new Boolean[] { false });
        this.height = 42;
    }
    
    @Override
    public void createControls() {
        List<String> list = new ArrayList<String>();
        list.add("i0");
        list.add("i1");
        list.add("i2");
        GuiComboBox box = new GuiComboBox("", this.width - 45, 24, 32, list) {
            
            @Override
            protected GuiComboBoxExtension createBox() {
                return super.createBox();
            }
        };
        box.height = 12;
        this.addControl(box);
    }
    
    @Override
    public void updateValue(CoreControl control) {
        // TODO Auto-generated method stub
        
    }
    
}
