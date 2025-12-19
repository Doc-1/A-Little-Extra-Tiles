package com.docvin.alet.components.gui.controls.drawing.photo;

import com.docvin.alet.components.gui.controls.drawing.utils.ColorUtilsAlet;
import org.lwjgl.util.Color;

public class ColorAccuracy {

    private static int colorAccuracy;

    public static void setColorAccuracy(double colorAccuracy) {
        colorAccuracy = Math.abs(colorAccuracy - 1);
        ColorAccuracy.colorAccuracy = colorAccuracy <= 0 ? 1 : (int) (colorAccuracy * 128);
    }

    public static int roundRGB(int colorInt) {
        if (colorAccuracy != 0) {
            Color color = ColorUtilsAlet.IntToRGBA(colorInt);
            int r = colorAccuracy * (Math.round((float) color.getRed() / colorAccuracy));
            int g = colorAccuracy * (Math.round((float) color.getGreen() / colorAccuracy));
            int b = colorAccuracy * (Math.round((float) color.getBlue() / colorAccuracy));
            int a = color.getAlpha();
            return ColorUtilsAlet.RGBAToInt(r, g, b, a);
        }
        return 0;
    }

}
