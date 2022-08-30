package com.alet.client.gui.controls.menu;

import com.alet.common.util.text.translation.ManualTranslator;

public class GuiTreeManualPart extends GuiTreePart {
    public final String pageName;
    
    public GuiTreeManualPart(GuiTreePart part, String pageName) {
        super(part);
        this.pageName = pageName;
    }
    
    public GuiTreeManualPart(GuiTreePart part, EnumPartType type, String pageName) {
        super(part, type);
        this.pageName = pageName;
    }
    
    public GuiTreeManualPart(String caption, EnumPartType type, String pageName) {
        super(caption, type);
        this.pageName = pageName;
    }
    
    public String getPage() {
        return ManualTranslator.translateToLocal(pageName);
    }
    
}
