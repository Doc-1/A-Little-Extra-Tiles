package com.alet.littletiles.common.utils.mc;

import org.lwjgl.util.Color;

import com.creativemd.creativecore.common.utils.mc.ColorUtils;

public class ColorUtilsAlet extends ColorUtils {
	
	public static enum ColorPart {
		RED {
			@Override
			public int getColor(Color color) {
				return color.getRed();
			}
			
			@Override
			public void setColor(Color color, int intenstiy) {
				color.setRed(intenstiy);
			}
			
			@Override
			public int getBrightest() {
				return 0xFF0000;
			}
		},
		GREEN {
			@Override
			public int getColor(Color color) {
				return color.getGreen();
			}
			
			@Override
			public void setColor(Color color, int intenstiy) {
				color.setGreen(intenstiy);
			}
			
			@Override
			public int getBrightest() {
				return 0x00FF00;
			}
		},
		BLUE {
			@Override
			public int getColor(Color color) {
				return color.getBlue();
			}
			
			@Override
			public void setColor(Color color, int intenstiy) {
				color.setBlue(intenstiy);
			}
			
			@Override
			public int getBrightest() {
				return 0x0000FF;
			}
		},
		ALPHA {
			@Override
			public int getColor(Color color) {
				return color.getAlpha();
			}
			
			@Override
			public void setColor(Color color, int intenstiy) {
				color.setAlpha(intenstiy);
			}
			
			@Override
			public int getBrightest() {
				return 0x000000FF;
			}
		},
		SHADE {
			@Override
			public int getColor(Color color) {
				return RGBAToInt(color.getRed(), color.getGreen(), color.getBlue(), 255);
			}
			
			@Override
			public void setColor(Color color, int intenstiy) {
			}
			
			@Override
			public int getBrightest() {
				return 0xFFFFFFFF;
			}
		};
		
		public abstract int getColor(Color color);
		
		public abstract void setColor(Color color, int intenstiy);
		
		public abstract int getBrightest();
	}
	
}
