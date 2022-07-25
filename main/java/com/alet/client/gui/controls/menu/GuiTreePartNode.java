package com.alet.client.gui.controls.menu;

import com.alet.client.gui.controls.programmer.blueprints.GuiBluePrintNode;

public class GuiTreePartNode extends GuiTreePart {
    
    public Class<? extends GuiBluePrintNode> nodeClass;
    
    public GuiTreePartNode(String caption, EnumPartType type, Class<? extends GuiBluePrintNode> nodeClass) {
        super(caption, type);
        this.nodeClass = nodeClass;
    }
    
}
