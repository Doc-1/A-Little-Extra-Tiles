package com.alet.client.gui.controls;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.lwjgl.util.Color;

import com.creativemd.creativecore.common.gui.GuiRenderHelper;
import com.creativemd.creativecore.common.gui.client.style.Style;
import com.creativemd.creativecore.common.gui.controls.gui.GuiTextBox;
import com.creativemd.creativecore.common.utils.mc.ColorUtils;

public class GuiModifibleTextBox extends GuiTextBox {
	
	public List<ModifyText> listOfText = new ArrayList<ModifyText>();
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
		Map<String, ModifyText> map = new LinkedHashMap<String, ModifyText>();
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
				map.put(s, new ModifyText(d, ColorUtils.BLACK, false, s));
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
				map.put(s, new ModifyText(0, ColorUtils.BLACK, b, s));
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
		for (int i = arr.length - 1; i >= 0; i--) {
			if (i == arr.length - 1) {
				System.out.println(arr[i]);
			} else {
				String z = arr[i].text.replaceAll(arr[i + 1].text, "");
				arr[i + 1].text = z;
				System.out.println(arr[i + 1]);
			}
			
		}
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
		
	}
	
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
		public int color = ColorUtils.BLACK;
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
