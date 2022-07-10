package com.alet.client.gui.controls;

import com.alet.client.gui.SubGuiManual;
import com.creativemd.creativecore.common.gui.event.gui.GuiControlChangedEvent;

public class GuiTreePagePart extends GuiTreeManualPart {
    
    public double scroll;
    public GuiTreePart page;
    
    public GuiTreePagePart(GuiTreePart part, double scroll, GuiTreePart page) {
        super(part);
        this.page = page;
        this.scroll = scroll;
    }
    
    public GuiTreePagePart(GuiTreePart part, EnumPartType type, double scroll, GuiTreePart page) {
        super(part, type);
        this.page = page;
        this.scroll = scroll;
    }
    
    public GuiTreePagePart(String caption, EnumPartType type, double scroll, GuiTreePart page) {
        super(caption, type);
        this.page = page;
        this.scroll = scroll;
    }
    
    @Override
    public void getPage() {
        SubGuiManual manual = (SubGuiManual) this.getGui();
        if (!manual.selected.equals(page)) {
            manual.raiseEvent(new GuiControlChangedEvent(page));
            manual.tree.highlightPart(page);
        }
        manual.scrollBoxPage.scrolled.set(scroll);
    }
    
}
