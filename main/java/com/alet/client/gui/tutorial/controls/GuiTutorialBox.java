package com.alet.client.gui.tutorial.controls;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.Color;

import com.alet.client.gui.controls.GuiColorablePanel;
import com.creativemd.creativecore.common.gui.GuiRenderHelper;
import com.creativemd.creativecore.common.gui.client.style.ColoredDisplayStyle;
import com.creativemd.creativecore.common.gui.client.style.Style;
import com.creativemd.creativecore.common.gui.container.GuiParent;
import com.creativemd.creativecore.common.gui.container.SubGui;
import com.creativemd.creativecore.common.gui.controls.gui.GuiButton;
import com.creativemd.creativecore.common.gui.controls.gui.GuiLabel;
import com.creativemd.creativecore.common.gui.controls.gui.GuiTextBox;
import com.creativemd.creativecore.common.gui.controls.gui.GuiTextfield;
import com.creativemd.creativecore.common.utils.mc.ColorUtils;

import net.minecraft.util.text.TextFormatting;

public class GuiTutorialBox extends GuiParent {
    
    public static Style helpStyle = new Style("panel", new ColoredDisplayStyle(0, 0, 0), new ColoredDisplayStyle(0, 65, 148), new ColoredDisplayStyle(0, 45, 128), new ColoredDisplayStyle(198, 198, 198), new ColoredDisplayStyle(0, 0, 0, 100));
    public static Style backgroundStyle = new Style("panel", new ColoredDisplayStyle(0, 0, 0), new ColoredDisplayStyle(89, 89, 89), new ColoredDisplayStyle(100, 100, 100), new ColoredDisplayStyle(198, 198, 198), new ColoredDisplayStyle(0, 0, 0, 100));
    public static Style panelStyle = new Style("panel", new ColoredDisplayStyle(0, 0, 0), new ColoredDisplayStyle(198, 198, 198), new ColoredDisplayStyle(100, 100, 100), new ColoredDisplayStyle(198, 198, 198), new ColoredDisplayStyle(0, 0, 0, 100));
    public int boxPosX;
    public int boxPosY;
    public int guiWidth;
    public int guiHeight;
    public int index = 0;
    public boolean closed = true;
    public List<TutorialData> tutorialMap = new ArrayList<TutorialData>();
    public GuiTutorialBox instance;
    
    public GuiTutorialBox(String name, int x, int y, int height, int guiWidth, int guiHeight) {
        super(name, -110, -3, 100, height);
        this.boxPosX = x;
        this.boxPosY = y;
        this.guiWidth = guiWidth;
        this.guiHeight = guiHeight;
        setStyle(backgroundStyle);
        instance = this;
        TutorialData.setGuiDimensions(guiWidth, guiHeight);
        closedControls();
    }
    
    @Override
    protected void renderContent(GuiRenderHelper helper, Style style, int width, int height) {}
    
    public void updateControls() {
        if (closed) {
            this.removeControl(this.get("close"));
            this.removeControl(this.get("back"));
            this.removeControl(this.get("next"));
            this.removeControl(this.get("highlight"));
            this.removeControl(this.get("textBox"));
            this.removeControl(this.get("maxPages"));
            this.removeControl(this.get("page"));
            this.removeControl(this.get("pageBackground"));
            this.posX = -110;
            this.posY = -3;
            this.index = 0;
            closedControls();
        } else {
            this.removeControl(this.get("open"));
            openedControls();
        }
    }
    
    public void closedControls() {
        addControl(new GuiButton("open", TextFormatting.BOLD + "?", this.width - 21, -boxPosY - 3, 12, 10) {
            @Override
            public void onClicked(int x, int y, int button) {
                instance.closed = false;
                instance.updateControls();
            }
            
            @Override
            public boolean canOverlap() {
                return true;
            }
        });
        GuiButton openButton = (GuiButton) get("open");
        
        openButton.setStyle(helpStyle);
        
        openButton.color = ColorUtils.WHITE;
    }
    
    public void openedControls() {
        
        addControl(new GuiButton("close", TextFormatting.DARK_GRAY + "" + TextFormatting.BOLD + "X", width - 24, 0, 12, 10) {
            @Override
            public void onClicked(int x, int y, int button) {
                instance.closed = true;
                instance.updateControls();
            }
        });
        addControl(new GuiTextBox("textBox", "Hello, How are you today?", 0, 15, 94) {
            @Override
            public boolean hasBackground() {
                return false;
            }
            
            @Override
            public boolean hasBorder() {
                return false;
            }
        });
        addControl(new GuiButton("next", TextFormatting.DARK_GRAY + "" + TextFormatting.BOLD + ">", width - 23, height - 22, 11, 10) {
            
            @Override
            public void onClicked(int x, int y, int button) {
                System.out.println(index);
                if (index < tutorialMap.size() - 1) {
                    index++;
                    updateHighlightedControl();
                } else {
                    GuiTextBox textBox = (GuiTextBox) instance.get("textBox");
                    textBox.text = "This completes the instructions. Please click close or back to view something you want to look over again. You can click on the question mark again after closing.";
                    instance.removeControl(instance.get("highlight"));
                }
            }
        });
        addControl(new GuiButton("back", TextFormatting.DARK_GRAY + "" + TextFormatting.BOLD + "<", 0, height - 22, 11, 10) {
            
            @Override
            public void onClicked(int x, int y, int button) {
                System.out.println(index);
                if (!instance.has("highlight")) {
                    GuiControlHighlighter highlighter = new GuiControlHighlighter("highlight", null, instance.boxPosX, instance.boxPosY);
                    addControl(highlighter);
                    updateHighlightedControl();
                    return;
                }
                if (index > 0) {
                    index--;
                    updateHighlightedControl();
                }
            }
        });
        addControl(new GuiLabel("maxPages", TextFormatting.DARK_GRAY + "" + TextFormatting.BOLD + "/" + tutorialMap.size(), 50, height - 21));
        addControl(new GuiTextfield("page", "1", 25, height - 23, 20, 10) {
            @Override
            public boolean hasBackground() {
                return false;
            }
            
            @Override
            public boolean hasBorder() {
                return false;
            }
        });
        addControl(new GuiColorablePanel("pageBackground", 23, this.width + 58, 48, 10, new Color(0, 0, 0), new Color(198, 198, 198)));
        GuiControlHighlighter highlighter = new GuiControlHighlighter("highlight", null, this.boxPosX, this.boxPosY);
        addControl(highlighter);
        updateHighlightedControl();
        get("next").setStyle(panelStyle);
        get("close").setStyle(panelStyle);
        get("back").setStyle(panelStyle);
    }
    
    public void updateHighlightedControl() {
        GuiControlHighlighter highlight = (GuiControlHighlighter) this.get("highlight");
        ((GuiTextfield) this.get("page")).text = TextFormatting.DARK_GRAY + "" + TextFormatting.BOLD + (index + 1) + "";
        TutorialData data = (TutorialData) tutorialMap.get(index);
        GuiTextBox textBox = (GuiTextBox) this.getGui().get("textBox");
        textBox.text = data.tutorial;
        data.setPos(this.posX, this.posY);
        this.posX = data.tutBoxPosX;
        highlight.setControl(this.getGui().get(data.control.name));
        highlight.posX = (-10 - this.posX) + highlight.control.posX;
        highlight.posY = (-10 - this.posY) + highlight.control.posY + data.y;
        if (data.control.getParent() instanceof GuiParent && !(data.control.getParent() instanceof SubGui)) {
            highlight.posX += data.control.getParent().posX + 2;
            highlight.posY += data.control.getParent().posY + 2;
        }
    }
    
    @Override
    public boolean hasBackground() {
        return !closed;
    }
    
    @Override
    public boolean hasBorder() {
        return !closed;
    }
    
    @Override
    public boolean canOverlap() {
        return true;
    }
    
    @Override
    public boolean hasMouseOverEffect() {
        return false;
    }
}
