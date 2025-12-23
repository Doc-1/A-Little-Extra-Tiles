package com.docvin.alet.components.gui.controls.override;

import com.creativemd.creativecore.common.gui.GuiRenderHelper;
import com.creativemd.creativecore.common.gui.client.style.Style;
import com.creativemd.creativecore.common.gui.controls.gui.GuiSteppedSlider;
import com.docvin.alet.common.utils.ColorUtils;
import org.lwjgl.util.Color;

public class GuiColoredSteppedSlider extends GuiSteppedSlider {

    public GuiColorPicker picker;
    public ColorUtils.ColorPart part;

    public GuiColoredSteppedSlider(String name, int x, int y, int width, int height, GuiColorPicker guiColorPickerAlet, ColorUtils.ColorPart alpha) {
        super(name, x, y, width, height, alpha.getColor(guiColorPickerAlet.color), 0, 255);
        this.picker = guiColorPickerAlet;
        this.part = alpha;
    }

    @Override
    public void setValue(double value) {
        super.setValue((int) value);
        if (part != null) {
            part.setColor(picker.color, (int) this.value);
            picker.onColorChanged();
        }
    }

    @Override
    protected void renderContent(GuiRenderHelper helper, Style style, int width, int height) {
        if (part == ColorUtils.ColorPart.ALPHA) {
            Color startColor = new Color(picker.color);
            startColor.setAlpha(0);
            Color endColor = new Color(picker.color);
            endColor.setAlpha(255);
            helper.drawHorizontalGradientRect(0, 0, width, height, ColorUtils.RGBAToInt(startColor), ColorUtils.RGBAToInt(endColor));
        } else
            helper.drawHorizontalChannelMaskGradientRect(0, 0, width, height, ColorUtils.RGBAToInt(picker.color), part.getBrightest());

        super.renderContent(helper, style, width, height);
    }
}
