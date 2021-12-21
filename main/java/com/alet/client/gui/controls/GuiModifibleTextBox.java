package com.alet.client.gui.controls;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.lwjgl.util.Color;

import com.creativemd.creativecore.common.gui.GuiRenderHelper;
import com.creativemd.creativecore.common.gui.client.style.Style;
import com.creativemd.creativecore.common.gui.controls.gui.GuiTextBox;
import com.creativemd.creativecore.common.utils.mc.ColorUtils;

import net.minecraft.client.renderer.GlStateManager;

public class GuiModifibleTextBox extends GuiTextBox {
	
	Map<String, ModifyText> map = new LinkedHashMap<String, ModifyText>();
	public static final Pattern FORMATTING_SCALE_PATTERN = Pattern.compile("\\{scale:[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?\\}");
	public static final Pattern FORMATTING_CLICKABLE_PATTERN = Pattern.compile("\\{clickable\\}");
	public static final Pattern FORMATTING_COLOR_PATTERN = Pattern.compile("\\{color:[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?\\}");
	public static final Pattern FORMATTING_END_PATTERN = Pattern.compile("\\{end\\}");
	public static final Pattern FORMATTING_NUMBER_PATTERN = Pattern.compile("[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?");
	
	public GuiModifibleTextBox(String name, String text, int x, int y, int width) {
		super(name, text, x, y, width);
		readText();
	}
	
	private void readText() {
		
		String tempText = this.text;
		Matcher matcherScale = FORMATTING_SCALE_PATTERN.matcher(tempText);
		Matcher matcherClickable = FORMATTING_CLICKABLE_PATTERN.matcher(tempText);
		Matcher matcherColor = FORMATTING_COLOR_PATTERN.matcher(tempText);
		
		while (matcherScale.find()) {
			String s = cleanText(this.text.substring(matcherScale.start(), this.text.length()));
			double d = ModifierAttribute.getScale(matcherScale.group());
			if (map.containsKey(s)) {
				ModifyText temp = map.get(s);
				temp.scale = d;
				map.replace(s, temp);
			} else {
				map.put(s, new ModifyText(d, ColorUtils.WHITE, false, s));
			}
		}
		
		while (matcherClickable.find()) {
			String s = cleanText(this.text.substring(matcherClickable.start(), this.text.length()));
			boolean b = ModifierAttribute.isClickable(matcherClickable.group());
			if (map.containsKey(s)) {
				ModifyText temp = map.get(s);
				temp.clickable = b;
				map.replace(s, temp);
			} else {
				map.put(s, new ModifyText(0, ColorUtils.WHITE, b, s));
			}
		}
		
		while (matcherColor.find()) {
			String s = cleanText(this.text.substring(matcherColor.start(), this.text.length()));
			int c = ModifierAttribute.getColor(matcherColor.group());
			if (map.containsKey(s)) {
				ModifyText temp = map.get(s);
				temp.color = c;
				map.replace(s, temp);
			} else {
				map.put(s, new ModifyText(0, c, false, s));
			}
		}
		ModifyText[] arr = map.values().toArray(new ModifyText[map.values().size()]);
		for (int i = 0; i <= arr.length - 2; i++)
			arr[i].text = arr[i].text.replaceAll(arr[i + 1].text, "");
		
	}
	
	public String cleanText(String text) {
		
		text = text.replaceAll(FORMATTING_SCALE_PATTERN.pattern(), "");
		text = text.replaceAll(FORMATTING_CLICKABLE_PATTERN.pattern(), "");
		text = text.replaceAll(FORMATTING_COLOR_PATTERN.pattern(), "");
		text = text.replaceAll(FORMATTING_END_PATTERN.pattern(), "");
		
		return text;
	}
	
	@Override
	protected void renderContent(GuiRenderHelper helper, Style style, int width, int height) {
		
		ModifyText[] arr = map.values().toArray(new ModifyText[map.values().size()]);
		float xPos = 1.0F;
		float yPos = 0.0F;
		for (int i = 0; i <= arr.length - 1; i++) {
			double scale = arr[i].scale - 1D;
			double s = arr[i].scale;
			double textWidth = font.getStringWidth(arr[i].text);
			double textHeight = font.FONT_HEIGHT;
			
			GlStateManager.pushMatrix();
			xPos += textWidth;
			GlStateManager.scale(arr[i].scale, arr[i].scale, 1);
			font.drawString(arr[i].text, xPos, 0, arr[i].color, true);
			
			GlStateManager.popMatrix();
		}
	}
	
	/*
	 * 
			
	 */
	public final static class ModifierAttribute {
		
		public static String scale(double scale) {
			return "{scale:" + scale + "}";
		}
		
		public static String color(int color) {
			return "{color:" + color + "}";
		}
		
		public static String color(Color color) {
			return "{color:" + ColorUtils.RGBAToInt(color) + "}";
		}
		
		public static String clickable() {
			return "{clickable}";
		}
		
		public static String end() {
			return "{end}";
		}
		
		public static double getScale(String scale) {
			Matcher matcher = FORMATTING_NUMBER_PATTERN.matcher(scale);
			matcher.find();
			String s = matcher.group();
			return Double.parseDouble(s);
		}
		
		public static int getColor(String color) {
			Matcher matcher = FORMATTING_NUMBER_PATTERN.matcher(color);
			matcher.find();
			String s = matcher.group();
			return Integer.parseInt(s);
		}
		
		public static boolean isClickable(String clickable) {
			Matcher matcher = FORMATTING_CLICKABLE_PATTERN.matcher(clickable);
			return matcher.find();
		}
	}
	
	private class ModifyText {
		public double scale = 0;
		public int color = ColorUtils.WHITE;
		public boolean clickable = false;
		public String text = "";
		
		public ModifyText(double scale, int color, boolean clickable, String text) {
			this.scale = scale;
			this.color = color;
			this.clickable = clickable;
			this.text = text;
		}
		
		@Override
		public String toString() {
			return this.text + ": {scale:" + this.scale + "}," + "{color:" + this.color + "}," + "{clickable:"
			        + this.clickable + "}";
		}
	}
	
}
