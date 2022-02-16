package com.alet.client.gui.controls.programmer.blueprints;

import java.util.ArrayList;
import java.util.List;

import com.alet.client.gui.controls.programmer.BluePrintConnection;
import com.creativemd.creativecore.common.gui.controls.gui.GuiComboBox;
import com.creativemd.creativecore.common.gui.controls.gui.GuiComboBoxExtension;
import com.creativemd.creativecore.common.gui.controls.gui.GuiTextfield;

public class GuiNodeMethodSetColorDisplay extends GuiBluePrintNode {
    private BluePrintConnection<Boolean> methodSenderNode = new BluePrintConnection<Boolean>("methodSender", "", this, 0, BluePrintConnection.METHOD_SENDER_CONNECTION);
    private BluePrintConnection<Boolean> methodReceiverNode = new BluePrintConnection<Boolean>("methodReceiver", "", this, 0, BluePrintConnection.METHOD_RECEIVER_CONNECTION);
    
    private BluePrintConnection<Integer[]> out = new BluePrintConnection<Integer[]>("output", "Output", this, 1, BluePrintConnection.PARAMETER_CONNECTION);
    private BluePrintConnection<Integer> red = new BluePrintConnection<Integer>("red", "Red", this, 3, BluePrintConnection.PARAMETER_CONNECTION);
    private BluePrintConnection<Integer> green = new BluePrintConnection<Integer>("green", "Green", this, 5, BluePrintConnection.PARAMETER_CONNECTION);
    private BluePrintConnection<Integer> blue = new BluePrintConnection<Integer>("blue", "Blue", this, 7, BluePrintConnection.PARAMETER_CONNECTION);
    
    public GuiNodeMethodSetColorDisplay(String name, int x, int y) {
        super(name, "Set Color Monitor", GuiBluePrintNode.FLOW_NODE, x, y);
        addNode(methodReceiverNode);
        methodReceiverNode.setValue(false);
        addNode(methodSenderNode);
        methodSenderNode.setValue(false);
        addNode(out);
        out.setValue(new Integer[] { 0 });
        addNode(red);
        red.setValue(0);
        addNode(green);
        green.setValue(0);
        addNode(blue);
        blue.setValue(0);
        this.height = 123;
    }
    
    @Override
    public void createControls() {
        List<String> list = new ArrayList<String>();
        list.add("o0");
        list.add("o1");
        list.add("o2");
        GuiComboBox box = new GuiComboBox("", 0, 24, 32, list) {
            @Override
            public boolean canOverlap() {
                return false;
            }
            
            @Override
            protected GuiComboBoxExtension createBox() {
                return new GuiComboBoxExtension(name + "extension", this, posX, posY + height, 50 - getContentOffset() * 2, 100, lines) {
                    @Override
                    public boolean canOverlap() {
                        // TODO Auto-generated method stub
                        return false;
                    }
                };
            }
        };
        
        box.height = 12;
        this.addControl(box);
        GuiTextfield red = new GuiTextfield("0", 0, 50, 32, 7);
        red.setNumbersIncludingNegativeOnly();
        GuiTextfield green = new GuiTextfield("0", 0, 76, 32, 7);
        green.setNumbersIncludingNegativeOnly();
        GuiTextfield blue = new GuiTextfield("0", 0, 102, 32, 7);
        blue.setNumbersIncludingNegativeOnly();
        this.addControl(red);
        this.addControl(green);
        this.addControl(blue);
    }
    
}
