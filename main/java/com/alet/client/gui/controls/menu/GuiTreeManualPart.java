package com.alet.client.gui.controls.menu;

public abstract class GuiTreeManualPart extends GuiTreePart {
    
    public GuiTreeManualPart(GuiTreePart part) {
        super(part);
        // TODO Auto-generated constructor stub
    }
    
    public GuiTreeManualPart(GuiTreePart part, EnumPartType type) {
        super(part, type);
    }
    
    public GuiTreeManualPart(String caption, EnumPartType type) {
        super(caption, type);
        // TODO Auto-generated constructor stub
    }
    
    public abstract void getPage();
    
}
