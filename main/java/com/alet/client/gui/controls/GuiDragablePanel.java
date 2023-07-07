package com.alet.client.gui.controls;

import java.util.ArrayList;

import com.creativemd.creativecore.common.gui.GuiControl;
import com.creativemd.creativecore.common.gui.GuiRenderHelper;
import com.creativemd.creativecore.common.gui.client.style.Style;
import com.creativemd.creativecore.common.gui.container.GuiParent;
import com.creativemd.creativecore.common.gui.event.gui.GuiControlChangedEvent;
import com.creativemd.creativecore.common.gui.event.gui.GuiControlClickEvent;
import com.creativemd.creativecore.common.utils.math.SmoothValue;
import com.n247s.api.eventapi.eventsystem.CustomEventSubscribe;

import net.minecraft.util.math.MathHelper;

public class GuiDragablePanel extends GuiParent {
    public SmoothValue scrolledX = new SmoothValue(200);
    public SmoothValue scrolledY = new SmoothValue(200);
    public SmoothValue zoom = new SmoothValue(200, 1);
    public double startScrollX;
    public double startScrollY;
    public int dragX;
    public int dragY;
    public boolean scrolling;
    public boolean somethingSelected = false;
    
    public boolean selected = false;
    
    public int maxWidth;
    public int maxHeight;
    
    public GuiDragablePanel(String name, int x, int y, int width, int height, int maxWidth, int maxHeight) {
        super(name, x, y, width, height);
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
    }
    
    @Override
    public float getScaleFactor() {
        return (float) zoom.current();
    }
    
    @Override
    public double getOffsetX() {
        return -scrolledX.current();
    }
    
    @Override
    public double getOffsetY() {
        return -scrolledY.current();
    }
    
    @Override
    public boolean mouseScrolled(int x, int y, int scrolled) {
        if (!super.mouseScrolled(x, y, scrolled))
            zoom.set(MathHelper.clamp(zoom.aimed() + scrolled * 0.04, 0.1, 2));
        return true;
    }
    
    @CustomEventSubscribe
    public void update(GuiControlChangedEvent event) {
        System.out.println(event);
    }
    
    @Override
    public boolean mousePressed(int x, int y, int button) {
        boolean results = super.mousePressed(x, y, button);
        this.somethingSelected = controlOver(x, y, button, controls, GuiBezierCurve.class) != null;
        if (button == 2) {
            zoom.set(1);
            scrolledX.set(0);
            scrolledY.set(0);
            return true;
        }
        if (!results) {
            scrolling = true;
            dragX = x;
            dragY = y;
            startScrollX = scrolledX.current();
            startScrollY = scrolledY.current();
        }
        this.raiseEvent(new GuiControlClickEvent(this, x, y, button));
        
        return results;
    }
    
    public GuiControl controlOver(int x, int y, int button, ArrayList<GuiControl> controls, Class<? extends GuiControl> exclude) {
        for (GuiControl control : controls) {
            if (!control.getClass().isAssignableFrom(exclude) && control.isMouseOver()) {
                return control;
            }
            if (control instanceof GuiParent) {
                GuiControl found = controlOver(x, y, button, ((GuiParent) control).controls, exclude);
                if (found != null) {
                    return found;
                }
            }
        }
        return null;
    }
    
    @Override
    public void mouseDragged(int x, int y, int button, long time) {
        if (!this.somethingSelected && this.isMouseOver() && controlOver(x, y, button, controls, GuiBezierCurve.class) == null)
            if (!selected && scrolling) {
                scrolledX.set(MathHelper.clamp(dragX - x + startScrollX, 0, maxWidth));
                scrolledY.set(MathHelper.clamp(dragY - y + startScrollY, 0, maxHeight));
            }
        super.mouseDragged(x, y, button, time);
    }
    
    @Override
    protected void renderContent(GuiRenderHelper helper, Style style, int width, int height) {
        scrolledX.tick();
        scrolledY.tick();
        zoom.tick();
        
    }
    
    @Override
    public void mouseReleased(int x, int y, int button) {
        super.mouseReleased(x, y, button);
        scrolling = false;
        this.somethingSelected = false;
    }
}
