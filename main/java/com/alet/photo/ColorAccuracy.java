package com.alet.photo;

import java.awt.Color;
import java.awt.image.BufferedImage;

import com.alet.ALET;

public class ColorAccuracy {
	
	private Color color;
	private int colorAccuracy = ALET.CONFIG.getColorAccuracy();
	
	public ColorAccuracy() {
	}
	
	public ColorAccuracy(BufferedImage image, int x, int y) {
		color = new Color(image.getRGB(x, y));
	}
	
	public int roundRGB() {
		int r = colorAccuracy * (Math.round(color.getRed() / colorAccuracy));
		int g = colorAccuracy * (Math.round(color.getGreen() / colorAccuracy));
		int b = colorAccuracy * (Math.round(color.getBlue() / colorAccuracy));
		int a = color.getAlpha();
		color = new Color(r, g, b, a);
		
		return color.getRGB();
	}
	
}
