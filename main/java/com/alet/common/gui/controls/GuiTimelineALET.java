package com.alet.common.gui.controls;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.Color;

import com.creativemd.creativecore.common.gui.GuiControl;
import com.creativemd.creativecore.common.gui.GuiRenderHelper;
import com.creativemd.creativecore.common.gui.client.style.ColoredDisplayStyle;
import com.creativemd.creativecore.common.gui.client.style.DisplayStyle;
import com.creativemd.creativecore.common.gui.client.style.Style;
import com.creativemd.creativecore.common.gui.container.GuiParent;
import com.creativemd.creativecore.common.gui.controls.gui.timeline.IAnimationHandler;
import com.creativemd.creativecore.common.gui.event.gui.GuiControlChangedEvent;
import com.creativemd.creativecore.common.gui.event.gui.GuiControlEvent;
import com.creativemd.creativecore.common.utils.math.SmoothValue;
import com.creativemd.creativecore.common.utils.mc.ColorUtils;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class GuiTimelineALET extends GuiParent {
    
    protected double ticksPerPixel;
    protected double basePixelWidth;
    protected static final double maxZoom = 10;
    protected SmoothValue zoom = new SmoothValue(100);
    protected SmoothValue scrollX = new SmoothValue(100);
    protected SmoothValue scrollY = new SmoothValue(100);
    protected int duration;
    
    public DisplayStyle timelineBackground = new ColoredDisplayStyle(255, 255, 255);
    
    public IAnimationHandler handler;
    public List<TimelineChannelALET> channels;
    public int sidebarWidth = 50;
    private int channelHeight = 10;
    private int timelineHeight = 11;
    protected List<KeyControlALET> dragging = new ArrayList<KeyControlALET>();
    
    public GuiTimelineALET(String name, int x, int y, int width, int height, int duration, List<TimelineChannelALET> channels, IAnimationHandler handler) {
        super(name, x, y, width, height);
        this.handler = handler;
        marginWidth = 0;
        this.channels = channels;
        refreshChannels();
        setDuration(duration);
    }
    
    public void refreshChannels() {
        int i = 0;
        this.removeControls("");
        for (TimelineChannelALET channel : channels) {
            channel.index = i;
            for (Object control : channel.controls) {
                adjustKeyPositionX((KeyControlALET) control);
                adjustKeyPositionY((KeyControlALET) control);
                addControl((GuiControl) control);
            }
            i++;
        }
    }
    
    public GuiTimelineALET setSidebarWidth(int width) {
        this.sidebarWidth = width;
        return this;
    }
    
    public void adjustKeyPositionY(KeyControlALET control) {
        System.out.println(control.channel.index);
        control.posY = timelineHeight + control.height / 2 + channelHeight * control.channel.index;
    }
    
    public void adjustKeyPositionX(KeyControlALET control) {
        control.posX = (int) (control.tick * getTickWidth()) - control.width / 2;
    }
    
    public void adjustKeysPositionX() {
        double tickWidth = getTickWidth();
        for (TimelineChannelALET channel : channels) {
            for (Object control : channel.controls) {
                ((KeyControlALET) control).posX = (int) (((KeyControlALET) control).tick * tickWidth) - ((KeyControlALET) control).width / 2;
            }
        }
    }
    
    @Override
    public double getOffsetX() {
        return sidebarWidth - scrollX.current();
    }
    
    @Override
    public void mouseMove(int x, int y, int button) {
        if (dragging != null) {
            if (!movedSelected)
                movedSelected = Math.abs(movedStart - x) > 5;
            
            if (movedSelected) {
                int tick = Math.max(0, getTickAt(x));
                for (KeyControlALET dragged : dragging)
                    if (dragged.channel.isSpaceFor(dragged, tick)) {
                        dragged.tick = tick;
                        adjustKeyPositionX(dragged);
                    }
                
            }
        } else if (draggedTimeline)
            handler.set(MathHelper.clamp((int) ((x - sidebarWidth + getTickWidth() / 2 + scrollX.current()) / getTickWidth()), 0, duration));
        
        super.mouseMove(x, y, button);
    }
    
    private List<KeyControlALET> selected = new ArrayList<KeyControlALET>();
    private boolean movedSelected = false;
    private int movedStart = 0;
    
    private boolean draggedTimeline = false;
    
    @Override
    public boolean mousePressed(int x, int y, int button) {
        selected.clear();
        boolean result = super.mousePressed(x, y, button);
        for (GuiControl control : controls) {
            if (control instanceof KeyControlALET && selected != control && ((KeyControlALET) control).selected) {
                ((KeyControlALET) control).selected = false;
                if (selected.isEmpty())
                    raiseEvent(new KeyALETDeselectedEvent((KeyControlALET) control));
            }
        }
        
        if (!result && selected.isEmpty()) {
            int channel = getChannelAt((int) getMousePos().y);
            if (channel != -1 && button == 1) {
                int tick = getTickAt(x);
                KeyControlALET control = channels.get(channel).addKey(tick, channels.get(channel).getValueAt(tick));
                if (control == null)
                    return result;
                adjustKeyPositionX(control);
                adjustKeyPositionY(control);
                this.addControl(control);
                raiseEvent(new GuiControlChangedEvent(this));
            } else if (channel != -1) {
                int tick = getTickAt(x);
                if (tick >= 0 && tick <= duration) {
                    handler.set(tick);
                    draggedTimeline = true;
                }
            }
            
        }
        
        return result;
    }
    
    @Override
    protected void clickControl(GuiControl control, int x, int y, int button) {
        if (control instanceof KeyControlALET) {
            
            if (button == 1) {
                if (!((KeyControlALET) control).modifiable)
                    return;
                ((KeyControlALET) control).removeKey();
                selected = new ArrayList<KeyControlALET>();
                raiseEvent(new GuiControlChangedEvent(this));
                return;
            }
            
            if (GuiScreen.isCtrlKeyDown()) {
                selected.add((KeyControlALET) control);
                ((KeyControlALET) control).selected = true;
            } else {
                if (!selected.isEmpty())
                    for (KeyControlALET select : selected) {
                        select.selected = false;
                    }
                dragging.clear();
                selected.clear();
                selected.add((KeyControlALET) control);
                ((KeyControlALET) control).selected = true;
            }
            
            for (KeyControlALET select : selected) {
                if (select.modifiable) {
                    dragging.add(select);
                    movedSelected = false;
                    movedStart = x;
                }
            }
            
            raiseEvent(new KeyALETSelectedEvent((KeyControlALET) control));
            playSound(SoundEvents.UI_BUTTON_CLICK);
        }
        super.clickControl(control, x, y, button);
    }
    
    @Override
    public void mouseReleased(int x, int y, int button) {
        super.mouseReleased(x, y, button);
        if (!dragging.isEmpty()) {
            for (KeyControlALET dragged : dragging) {
                dragged.channel.movedKey(dragged);
                dragged = null;
            }
            dragging.clear();
            raiseEvent(new GuiControlChangedEvent(this));
        }
        
        draggedTimeline = false;
    }
    
    public GuiTimelineALET setDuration(int duration) {
        int useableWidth = width - sidebarWidth - 2 - getContentOffset() * 2;
        ticksPerPixel = (double) duration / useableWidth;
        basePixelWidth = 1D / ticksPerPixel;
        zoom.setStart(0);
        this.duration = duration;
        scrollX.setStart(0);
        scrollY.setStart(0);
        adjustKeysPositionX();
        raiseEvent(new GuiControlChangedEvent(this));
        return this;
    }
    
    public int getDuration() {
        return duration;
    }
    
    @Override
    public boolean mouseScrolled(int x, int y, int scrolled) {
        if (GuiScreen.isShiftKeyDown())
            scrollX.set(MathHelper.clamp(scrollX.aimed() - scrolled * 10, 0, maxScrollX));
        else if (GuiScreen.isCtrlKeyDown())
            scrollY.set(MathHelper.clamp(scrollY.aimed() + scrolled, 0, maxScrollY));
        else {
            int focusedTick = Math.max(0, getTickAtAimed(x));
            zoom.set(MathHelper.clamp(zoom.aimed() + scrolled * basePixelWidth * 2 * Math.max(basePixelWidth * 2, zoom.aimed()) / maxZoom, 0, maxZoom));
            int currentTick = Math.max(0, getTickAtAimed(x));
            double aimedTickWidth = getTickWidthAimed();
            
            double sizeX = aimedTickWidth * duration;
            double maxScrollX = Math.max(0, sizeX - (width - getContentOffset() * 2));
            scrollX.set(MathHelper.clamp(scrollX.aimed() + (focusedTick - currentTick) * aimedTickWidth, 0, maxScrollX));
        }
        return true;
    }
    
    public int getChannelAt(int y) {
        int channel = ((y) - timelineHeight) / channelHeight;
        if (channel < 0 || channel >= channels.size())
            return -1;
        return channel;
    }
    
    public int getTickAt(int x) {
        x -= this.posX;
        if (x <= sidebarWidth)
            return -1;
        return MathHelper.clamp((int) ((x - sidebarWidth + getTickWidth() / 2 + scrollX.current()) / getTickWidth()), 0, duration);
    }
    
    public int getTickAtAimed(int x) {
        x -= this.posX;
        if (x <= sidebarWidth)
            return -1;
        return MathHelper.clamp((int) ((x - sidebarWidth + getTickWidth() / 2 + scrollX.aimed()) / getTickWidthAimed()), 0, duration);
    }
    
    private double lastZoom = 0;
    protected double sizeX;
    protected double maxScrollX;
    protected double sizeY;
    protected double maxScrollY;
    
    protected double getTickWidth() {
        return basePixelWidth + zoom.current();
    }
    
    protected double getTickWidthAimed() {
        return basePixelWidth + zoom.aimed();
    }
    
    @Override
    public List<String> getTooltip() {
        Vec3d mouse = getMousePos();
        double x = mouse.x + getOffsetX();
        List<String> lines = new ArrayList<>();
        if (x > sidebarWidth && x < width - getContentOffset() * 2) {
            int channelId = getChannelAt((int) mouse.y);
            if (channelId >= 0) {
                TimelineChannelALET channel = channels.get(channelId);
                int tick = getTickAt((int) x);
                lines.add("" + tick + ". " + channel.name);
            }
        } else if (x > 0 && x < sidebarWidth) {
            int channelId = getChannelAt((int) mouse.y);
            if (channelId >= 0) {
                TimelineChannelALET channel = channels.get(channelId);
                lines.add(channel.name);
            }
        }
        
        return lines;
    }
    
    @Override
    protected void renderContent(GuiRenderHelper helper, Style style, int width, int height) {
        zoom.tick();
        scrollX.tick();
        scrollY.tick();
        int usuableWidth = width - sidebarWidth;
        
        double tickWidth = getTickWidth();
        if (lastZoom != zoom.current()) {
            sizeX = tickWidth * duration;
            maxScrollX = Math.max(0, sizeX - usuableWidth);
            lastZoom = zoom.current();
            adjustKeysPositionX();
        }
        
        int ticks = (int) (usuableWidth / tickWidth);
        int area = 5;
        int halfArea = 5;
        int smallestStep = 0;
        while (Math.pow(area, smallestStep) * tickWidth < 3) {
            smallestStep++;
        }
        smallestStep = (int) Math.pow(area, smallestStep);
        
        double stepWidth = tickWidth * smallestStep;
        int stepOffset = (int) (scrollX.current() / stepWidth);
        int stamps = ticks / smallestStep;
        int begin = Math.max(0, stepOffset);
        int end = stepOffset + stamps + 1;
        
        int pointerWidth = Math.max((int) tickWidth, 1);
        GlStateManager.pushMatrix();
        GlStateManager.translate(sidebarWidth, 0, 0);
        Color a = new Color(31, 78, 120, 150);
        Color b = new Color(47, 117, 181, 150);
        Color c = new Color(155, 194, 230, 150);
        Color d = new Color(189, 215, 238, 150);
        Color e = new Color(221, 235, 247, 150);
        Color color = a;
        int counter = 0;
        int stop = (int) (Math.ceil(scrollX.current() / stepWidth) + ticks + 1);
        for (int i = begin; i <= stop; i++) {
            counter = i % 5;
            if (counter == 0)
                color = a;
            else if (counter == 1)
                color = b;
            else if (counter == 2)
                color = c;
            else if (counter == 3)
                color = d;
            else if (counter == 4) {
                color = e;
                counter = -1;
            }
            GlStateManager.pushMatrix();
            GlStateManager.translate(tickWidth * i - pointerWidth / 2D - scrollX.current(), 0, 0);
            new ColoredDisplayStyle(color).renderStyle(helper, pointerWidth, timelineHeight * this.height);
            GlStateManager.popMatrix();
            
        }
        GlStateManager.popMatrix();
        
        // Render sidebar
        GlStateManager.pushMatrix();
        timelineBackground.renderStyle(helper, sidebarWidth, height);
        GlStateManager.translate(0, timelineHeight, 0);
        for (int i = 0; i < channels.size(); i++) {
            
            int textW = font.getStringWidth(channels.get(i).name);
            if (textW > this.sidebarWidth)
                font.drawString(channels.get(i).name.substring(0, 5).replaceAll(" ", "") + "...", 1, 1, ColorUtils.BLACK);
            else
                font.drawString(channels.get(i).name, 1, 1, ColorUtils.BLACK);
            GlStateManager.translate(0, channelHeight, 0);
        }
        GlStateManager.popMatrix();
        
        // Render timeline
        GlStateManager.translate(sidebarWidth, 0, 0);
        
        GlStateManager.pushMatrix();
        timelineBackground.renderStyle(helper, width, timelineHeight);
        GlStateManager.translate(0, timelineHeight, 0);
        getStyle().getBorder(this).renderStyle(helper, width, 1);
        
        GlStateManager.pushMatrix();
        pointerWidth = Math.max((int) tickWidth, 1);
        GlStateManager.translate(tickWidth * handler.get() - pointerWidth / 2D - scrollX.current(), -timelineHeight, 0);
        new ColoredDisplayStyle(200, 200, 0, 150).renderStyle(helper, pointerWidth, timelineHeight);
        GlStateManager.popMatrix();
        
        GlStateManager.pushMatrix();
        GlStateManager.translate(-scrollX.current() + begin * stepWidth, -2, 0);
        
        for (int i = begin; i < end; i++) {
            if (i % halfArea == 0) {
                
                getStyle().getBorder(this).renderStyle(helper, 1, 2);
                getStyle().getDisableEffect(this).renderStyle(0, 2, helper, 1, height - 2);
                String text = "" + (i * smallestStep);
                font.drawString(text, 0 - font.getStringWidth(text) / 2, -8, ColorUtils.BLACK);
            } else
                getStyle().getBorder(this).renderStyle(helper, 1, 1);
            
            GlStateManager.translate(stepWidth, 0, 0);
        }
        GlStateManager.popMatrix();
        
        GlStateManager.popMatrix();
        
        // Render channels
        GlStateManager.pushMatrix();
        GlStateManager.translate(0, timelineHeight, 0);
        for (int i = 0; i < channels.size(); i++) {
            GlStateManager.translate(0, channelHeight, 0);
            getStyle().getBorder(this).renderStyle(helper, usuableWidth, 1);
        }
        GlStateManager.popMatrix();
        
        // Render scrollbar
        if (maxScrollX > 0) {
            GlStateManager.pushMatrix();
            GlStateManager.translate((scrollX.current() / sizeX) * usuableWidth, height - 2, 0);
            getStyle().getBorder(this).renderStyle(helper, (int) Math.max(1, (1 - (maxScrollX / sizeX)) * usuableWidth), 1);
            GlStateManager.popMatrix();
        }
    }
    
    @Override
    public boolean hasMouseOverEffect() {
        return false;
    }
    
    public static class KeyALETSelectedEvent extends GuiControlEvent {
        
        public KeyALETSelectedEvent(KeyControlALET source) {
            super(source);
        }
        
        @Override
        public boolean isCancelable() {
            return false;
        }
        
    }
    
    public static class KeyALETDeselectedEvent extends GuiControlEvent {
        
        public KeyALETDeselectedEvent(KeyControlALET source) {
            super(source);
        }
        
        @Override
        public boolean isCancelable() {
            return false;
        }
        
    }
}
