package com.alet.common.utils.photo;

import org.lwjgl.util.Color;

import com.alet.littletiles.common.utils.mc.ColorUtilsAlet;

public class ColorAccuracy {
    
    private static int colorAccuracy;
    
    public static void setColorAccuracy(double colorAccuracy) {
        ColorAccuracy.colorAccuracy = colorAccuracy <= 0 ? 1 : (int) (colorAccuracy * 239);
    }
    
    public static int roundRGB(int colorInt) {
        if (colorAccuracy != 0) {
            Color color = ColorUtilsAlet.IntToRGBA(colorInt);
            int r = colorAccuracy * (Math.round(color.getRed() / colorAccuracy));
            int g = colorAccuracy * (Math.round(color.getGreen() / colorAccuracy));
            int b = colorAccuracy * (Math.round(color.getBlue() / colorAccuracy));
            int a = color.getAlpha();
            return ColorUtilsAlet.RGBAToInt(r, g, b, a);
        }
        return 0;
    }
    
}
