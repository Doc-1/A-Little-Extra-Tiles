package com.alet.client.gui.controls;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.lwjgl.util.Color;

import com.creativemd.creativecore.common.gui.GuiRenderHelper;
import com.creativemd.creativecore.common.gui.client.style.Style;
import com.creativemd.creativecore.common.gui.controls.gui.GuiTextBox;
import com.creativemd.creativecore.common.utils.mc.ColorUtils;

import net.minecraft.client.renderer.GlStateManager;

public class GuiModifibleTextBox extends GuiTextBox {
	
	List<String> listOfText = new ArrayList<String>();
	List<ModifyText> listOfModifyText = new ArrayList<ModifyText>();
	ModifyTextMap map = new ModifyTextMap();
	public static final Pattern FORMATTING_NEWLINE_PATTERN = Pattern.compile("\\{newLines:[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?\\}");
	public static final Pattern FORMATTING_SCALE_PATTERN = Pattern.compile("\\{scale:[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?\\}");
	public static final Pattern FORMATTING_CLICKABLE_PATTERN = Pattern.compile("\\{clickable\\}");
	public static final Pattern FORMATTING_COLOR_PATTERN = Pattern.compile("\\{color:[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?\\}");
	public static final Pattern FORMATTING_END_PATTERN = Pattern.compile("\\{end\\}");
	public static final Pattern FORMATTING_NUMBER_PATTERN = Pattern.compile("[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?");
	
	public GuiModifibleTextBox(String name, String text, int x, int y, int width) {
		super(name, text, x, y, width);
		textToArray(this.text);
		readText();
		breakDownText();
	}
	
	private void textToArray(String text) {
		Matcher matcherEnd = FORMATTING_END_PATTERN.matcher(text);
		if (matcherEnd.find()) {
			String foundText = text.substring(0, matcherEnd.end());
			listOfText.add(foundText);
			this.textToArray(text.replaceFirst(Pattern.quote(foundText), ""));
		}
	}
	
	private void readText() {
		
		for (String text : listOfText) {
			Matcher matcherScale = FORMATTING_SCALE_PATTERN.matcher(text);
			Matcher matcherNewLine = FORMATTING_NEWLINE_PATTERN.matcher(text);
			Matcher matcherClickable = FORMATTING_CLICKABLE_PATTERN.matcher(text);
			Matcher matcherColor = FORMATTING_COLOR_PATTERN.matcher(text);
			Matcher matcherEnd = FORMATTING_END_PATTERN.matcher(text);
			
			ModifyText modText = new ModifyText(0, ColorUtils.WHITE, false, 0, "");
			
			if (matcherScale.find()) {
				modText.scale = ModifierAttribute.getScale(matcherScale.group());
				text = text.replaceAll(FORMATTING_SCALE_PATTERN.pattern(), "");
			}
			
			if (matcherNewLine.find()) {
				modText.newLines = ModifierAttribute.getNewLines(matcherNewLine.group());
				text = text.replaceAll(FORMATTING_NEWLINE_PATTERN.pattern(), "");
			}
			
			if (matcherClickable.find()) {
				modText.clickable = ModifierAttribute.isClickable(matcherClickable.group());
				text = text.replaceAll(FORMATTING_CLICKABLE_PATTERN.pattern(), "");
			}
			
			if (matcherColor.find()) {
				modText.color = ModifierAttribute.getColor(matcherColor.group());
				text = text.replaceAll(FORMATTING_COLOR_PATTERN.pattern(), "");
			}
			
			if (matcherEnd.find()) {
				text = text.replaceAll(FORMATTING_END_PATTERN.pattern(), "");
				modText.text = text;
			}
			listOfModifyText.add(modText);
		}
	}
	
	public void breakDownText() {
		List<ModifyText> list = new ArrayList<ModifyText>();
		for (ModifyText modText : listOfModifyText) {
			boolean flag1 = false;
			for (String s : modText.text.split("((?<= )|(?= ))")) {
				ModifyText copy = modText.copy();
				if (flag1)
					copy.newLines = 0;
				flag1 = true;
				list.add(copy.setText(s));
			}
		}
		int currentWidth = this.width;
		int widthCount = 0;
		float i = 0;
		
		for (ModifyText modText : list) {
			double scale = modText.scale;
			widthCount += font.getStringWidth(modText.text) * scale;
			i += modText.newLines;
			if (modText.newLines != 0) {
				widthCount = 0;
			}
			if (widthCount > currentWidth - 15) {
				widthCount = 0;
				i++;
			}
			System.out.println(i + " " + modText.text + " " + widthCount + " " + currentWidth);
			this.map.add(i, modText);
		}
		//max w = 90
		//"Little Tiles is a great mod!" w = 100
		//"Little Tiles is a great" w = 90
		//"mod!" w = 10
		
		/*
		
		for (ModifyText modText : listOfModifyText) {
			
			String storage = modText.text;
			float scale = (float) modText.scale;
			
			while (!storage.equals("")) {
				int textWidth = (int) Math.ceil((font.getStringWidth(storage) * scale));
				if (textWidth > currentWidth) {
					
					
				} else {
					System.out.println(storage);
					storage = "";
				}
			}
		}*/
	}
	
	@Override
	protected void renderContent(GuiRenderHelper helper, Style style, int width, int height) {
		
		Iterator<Map.Entry<Float, List<ModifyText>>> itr = map.entrySet().iterator();
		float y = 0;
		while (itr.hasNext()) {
			Entry<Float, List<ModifyText>> entry = itr.next();
			float key = entry.getKey();
			int x = 0;
			List<ModifyText> modTextList = entry.getValue();
			for (ModifyText modText : modTextList) {
				GlStateManager.pushMatrix();
				GlStateManager.scale(modText.scale, modText.scale, 1);
				//System.out.println(heightPos + " " + maxHeight + " " + modText.text + " " + addY);
				float textHeight = (float) (font.FONT_HEIGHT * modText.scale);
				float maxTextHeight = map.maxHeight(key);
				float addY = textHeight - maxTextHeight;
				font.drawString(modText.text, (float) (x / modText.scale), (float) ((y - addY)
				        / modText.scale), modText.color, true);
				x += font.getStringWidth(modText.text) * modText.scale;
				GlStateManager.popMatrix();
			}
			y += map.maxHeight(key);
			
		}
		/*
		int textWidth = (int) (font.getStringWidth(modText.text) * scale);
		y += modText.newLines * font.FONT_HEIGHT;
		
		int count = 0;
		
		
		for (String s : listOfFormatedText(modText.text, scale, currentWidth)) {
			GlStateManager.pushMatrix();
			GlStateManager.scale(scale, scale, 1);
			y += (font.FONT_HEIGHT * scale);
			font.drawString(s, (x / scale), y / scale, modText.color, true);
			
			x += (font.getStringWidth(s) * scale);
			GlStateManager.popMatrix();
			count++;
		}
		currentWidth -= textWidth;
		*/
		
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
		
		public static String newLines(int numberOf) {
			return "{newLines:" + numberOf + "}";
		}
		
		public static String space() {
			return end() + scale(1) + " " + end();
		}
		
		public static String tab() {
			return end() + scale(1) + "     " + end();
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
		
		public static int getNewLines(String newLines) {
			Matcher matcher = FORMATTING_NUMBER_PATTERN.matcher(newLines);
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
		public int newLines = 0;
		public int linePos = 0;
		
		public ModifyText(double scale, int color, boolean clickable, int newLines, String text) {
			this.scale = scale;
			this.color = color;
			this.clickable = clickable;
			this.text = text;
			this.newLines = newLines;
		}
		
		public ModifyText setText(String text) {
			this.text = text;
			return this;
		}
		
		public ModifyText copy() {
			return new ModifyText(this.scale, this.color, this.clickable, this.newLines, this.text);
		}
		
		@Override
		public boolean equals(Object obj) {
			ModifyText text = ModifyText.class.cast(obj);
			
			return this.scale == text.scale && this.clickable == text.clickable && this.color == text.color
			        && this.newLines == text.newLines && this.text.equals(text.text);
		}
		
		@Override
		public String toString() {
			return this.text + ": {scale:" + this.scale + "}," + "{color:" + this.color + "}," + "{clickable:"
			        + this.clickable + "}," + "{newLines:" + this.newLines + "}";
		}
	}
	
	private class ModifyTextMap<K, V> extends LinkedHashMap<Float, List<ModifyText>> {
		
		public void add(float key, ModifyText singleValue) {
			if (this.containsKey(key)) {
				this.get(key).add(singleValue);
			} else {
				List<ModifyText> modText = new ArrayList<ModifyText>();
				modText.add(singleValue);
				this.put(key, modText);
			}
		}
		
		public float maxHeight(float key) {
			float max = font.FONT_HEIGHT;
			for (ModifyText modText : this.get(key))
				max = (float) Math.max(modText.scale * font.FONT_HEIGHT, max);
			
			return max;
		}
	}
}
