package com.alet.client.gui.controls;

import com.alet.ALET;
import com.creativemd.creativecore.common.gui.GuiRenderHelper;
import com.creativemd.creativecore.common.gui.client.style.ColoredTextureDisplayStyle;
import com.creativemd.creativecore.common.gui.client.style.DisplayStyle;
import com.creativemd.creativecore.common.gui.client.style.Style;
import com.creativemd.creativecore.common.utils.mc.ColorUtils;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiIconDepressedButton extends GuiDepressedCheckBox {
    
    public static final ResourceLocation guiIconImage = new ResourceLocation(ALET.MODID, "textures/gui/icons.png");
    private int iconIndex;
    private ColoredTextureDisplayStyle icon;
    private ColoredTextureDisplayStyle iconBackground;
    public boolean selected = false;
    
    public GuiIconDepressedButton(int x, int y, int iconIndex, boolean value) {
        super("icon" + iconIndex, "", x, y, 19, 19, "", value);
        setIcon(iconIndex);
    }
    
    public GuiIconDepressedButton(int x, int y, int width, int iconIndex, boolean value) {
        super("icon" + iconIndex, "", x, y, width, 12, "", value);
        setIcon(iconIndex);
    }
    
    public GuiIconDepressedButton(String name, int x, int y, int width, int iconIndex, boolean value) {
        super(name, "", x, y, width, 12, "", value);
        setIcon(iconIndex);
    }
    
    public GuiIconDepressedButton(String name, int x, int y, int iconIndex, boolean value) {
        super(name, "", x, y, 12, 12, "", value);
        setIcon(iconIndex);
    }
    
    public int getIconIndex() {
        return iconIndex;
    }
    
    public DisplayStyle getIcon() {
        return icon;
    }
    
    public void setIcon(int iconIndex) {
        this.width = 19;
        this.height = 19;
        this.iconIndex = iconIndex;
        this.iconBackground = new ColoredTextureDisplayStyle(ColorUtils.RGBAToInt(0, 0, 0, 100), guiIconImage, 0, iconIndex * 16);
        this.icon = new ColoredTextureDisplayStyle(ColorUtils.WHITE, guiIconImage, 0, iconIndex * 16);
    }
    
    @Override
    protected void renderContent(GuiRenderHelper helper, Style style, int width, int height) {
        super.renderContent(helper, style, width, height);
        GlStateManager.translate(-2, -2, 0);
        GlStateManager.pushMatrix();
        GlStateManager.translate(1, 1, 0);
        iconBackground.renderStyle(helper, 16, 16);
        GlStateManager.popMatrix();
        icon.renderStyle(helper, 16, 16);
        
    }
    
}
