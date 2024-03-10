package com.alet.client.gui.controls.menu;

import com.alet.common.utils.text.translation.ManualTranslator;

public class GuiTreePartManual extends GuiTreePart {
    public final String pageName;
    
    public GuiTreePartManual(GuiTreePart part, String pageName) {
        super(part);
        this.pageName = pageName;
    }
    
    public GuiTreePartManual(GuiTreePart part, EnumPartType type, String pageName) {
        super(part, type);
        this.pageName = pageName;
    }
    
    public GuiTreePartManual(String caption, EnumPartType type, String pageName) {
        super(caption, type);
        this.pageName = pageName;
    }
    
    public String getPage() {
        return ManualTranslator.translateToLocal(pageName);
    }
    
}
