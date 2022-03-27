package com.alet.client.gui.controls;

import java.util.ArrayList;
import java.util.List;

import com.creativemd.creativecore.common.gui.GuiControl;
import com.creativemd.creativecore.common.gui.GuiRenderHelper;
import com.creativemd.creativecore.common.gui.client.style.DisplayStyle;
import com.creativemd.creativecore.common.gui.client.style.Style;

public class KeyControlALET<T> extends GuiControl implements Comparable<KeyControlALET> {
    
    public TimelineChannelALET channel;
    public boolean modifiable = true;
    public int tick;
    public boolean selected = false;
    public T value;
    
    public KeyControlALET(TimelineChannelALET channel, int index, int tick, T value) {
        super("" + index + ".", 0, 0, 0, 0);
        this.channel = channel;
        this.rotation = 45;
        this.tick = tick;
        this.value = value;
    }
    
    @Override
    public DisplayStyle getBorderDisplay(DisplayStyle display) {
        return super.getBorderDisplay(display);
    }
    
    @Override
    protected void renderContent(GuiRenderHelper helper, Style style, int width, int height) {
        
    }
    
    @Override
    public boolean mousePressed(int x, int y, int button) {
        return true;
    }
    
    @Override
    public List<String> getTooltip() {
        List<String> tooltip = new ArrayList<>();
        tooltip.add("" + value);
        return tooltip;
    }
    
    public void removeKey() {
        channel.removeKey(this);
        getParent().removeControl(this);
    }
    
    @Override
    public int compareTo(KeyControlALET o) {
        return Integer.compare(this.tick, o.tick);
    }
}
