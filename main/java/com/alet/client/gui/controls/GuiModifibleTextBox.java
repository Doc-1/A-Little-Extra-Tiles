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
import net.minecraft.init.SoundEvents;
import net.minecraft.util.text.TextFormatting;

public class GuiModifibleTextBox extends GuiTextBox {
	
	List<String> listOfText = new ArrayList<String>();
	List<ModifyText> listOfModifyText = new ArrayList<ModifyText>();
	Map<Float[], ModifyText> locationTextMap = new LinkedHashMap<Float[], ModifyText>();
	Map<Float[], ModifyText> locationClickableMap = new LinkedHashMap<Float[], ModifyText>();
	ModifyTextMap map = new ModifyTextMap();
	public static final Pattern FORMATTING_NEWLINE_PATTERN = Pattern.compile("\\{newLines:[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?\\}");
	public static final Pattern FORMATTING_SCALE_PATTERN = Pattern.compile("\\{scale:[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?\\}");
	public static final Pattern FORMATTING_ITALIC_PATTERN = Pattern.compile("\\{italic\\}");
	public static final Pattern FORMATTING_BOLD_PATTERN = Pattern.compile("\\{bold\\}");
	public static final Pattern FORMATTING_UNDERLINED_PATTERN = Pattern.compile("\\{underline\\}");
	public static final Pattern FORMATTING_CLICKABLE_PATTERN = Pattern.compile("\\{clickable\\}");
	public static final Pattern FORMATTING_COLOR_PATTERN = Pattern.compile("\\{color:[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?\\}");
	public static final Pattern FORMATTING_END_PATTERN = Pattern.compile("\\{end\\}");
	public static final Pattern FORMATTING_NUMBER_PATTERN = Pattern.compile("[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?");
	
	public GuiModifibleTextBox(String name, String text, int x, int y, int width) {
		super(name, text, x, y, width);
		textToArray(this.text);
		readText();
		breakDownText();
		positionText();
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
			Matcher matcherBold = FORMATTING_BOLD_PATTERN.matcher(text);
			Matcher matcherItalic = FORMATTING_ITALIC_PATTERN.matcher(text);
			Matcher matcherUnderline = FORMATTING_UNDERLINED_PATTERN.matcher(text);
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
			if (matcherBold.find()) {
				modText.bold = ModifierAttribute.isBold(matcherBold.group());
				text = text.replaceAll(FORMATTING_BOLD_PATTERN.pattern(), "");
			}
			if (matcherItalic.find()) {
				modText.italic = ModifierAttribute.isItalic(matcherItalic.group());
				System.out.println(modText.italic);
				text = text.replaceAll(FORMATTING_ITALIC_PATTERN.pattern(), "");
			}
			if (matcherUnderline.find()) {
				modText.underline = ModifierAttribute.isUnderline(matcherUnderline.group());
				text = text.replaceAll(FORMATTING_UNDERLINED_PATTERN.pattern(), "");
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
		int i = 0;
		
		for (ModifyText modText : list) {
			double scale = modText.scale;
			widthCount += font.getStringWidth(modText.text) * scale;
			i += modText.newLines;
			if (modText.newLines != 0) {
				widthCount = 0;
			}
			if (widthCount >= currentWidth - 15) {
				widthCount = 0;
				i++;
			}
			
			if (modText.bold)
				modText.text = TextFormatting.BOLD + modText.text + TextFormatting.RESET;
			if (modText.italic)
				modText.text = TextFormatting.ITALIC + modText.text + TextFormatting.RESET;
			if (modText.underline)
				modText.text = TextFormatting.UNDERLINE + modText.text + TextFormatting.RESET;
			this.map.add(i, modText);
		}
	}
	
	private void positionText() {
		Iterator<Map.Entry<Integer, List<ModifyText>>> itr = map.entrySet().iterator();
		float y = 0;
		while (itr.hasNext()) {
			Entry<Integer, List<ModifyText>> entry = itr.next();
			int key = entry.getKey();
			int x = 0;
			if (map.maxNewLine(key) != 1 && map.maxNewLine(key) != 0)
				y += (map.maxNewLine(key) - 1) * map.maxHeight(key);
			List<ModifyText> modTextList = entry.getValue();
			for (ModifyText modText : modTextList) {
				float textHeight = (float) (font.FONT_HEIGHT * modText.scale);
				float maxTextHeight = map.maxHeight(key);
				float addY = textHeight - maxTextHeight;
				if (modText.clickable)
					this.locationClickableMap.put(new Float[] { (float) (x / modText.scale), (float) ((y - addY)
					        / modText.scale) }, modText);
				else
					this.locationTextMap.put(new Float[] { (float) (x / modText.scale), (float) ((y - addY)
					        / modText.scale) }, modText);
				x += font.getStringWidth(modText.text) * modText.scale;
			}
			y += map.maxHeight(key);
		}
		this.height = (int) y + 9;
	}
	
	public boolean isMouseOverClickable(int posX, int posY) {
		for (Entry<Float[], ModifyText> entry : this.locationClickableMap.entrySet()) {
			float x = entry.getKey()[0];
			float y = entry.getKey()[1];
			ModifyText modText = entry.getValue();
			modText.mouseOver = (posX >= x && posX < x + ((font.getStringWidth(modText.text) + 7) * modText.scale)
			        && posY >= y && posY < y + (font.FONT_HEIGHT * modText.scale));
		}
		return false;
	}
	
	@Override
	public boolean isMouseOver(int posX, int posY) {
		boolean results = super.isMouseOver(posX, posY);
		if (results)
			isMouseOverClickable(posX, posY);
		return results;
	}
	
	@Override
	public void mouseReleased(int x, int y, int button) {
		for (ModifyText modText : this.locationClickableMap.values()) {
			if (modText.mouseOver) {
				playSound(SoundEvents.UI_BUTTON_CLICK);
				clickedOn(modText.text);
			}
		}
	}
	
	public void clickedOn(String text) {
		
	}
	
	@Override
	protected void renderContent(GuiRenderHelper helper, Style style, int width, int height) {
		for (Entry<Float[], ModifyText> entry : this.locationTextMap.entrySet()) {
			float x = entry.getKey()[0];
			float y = entry.getKey()[1];
			ModifyText modText = entry.getValue();
			GlStateManager.pushMatrix();
			GlStateManager.scale(modText.scale, modText.scale, 1);
			font.drawString(modText.text, x, y, modText.color, true);
			GlStateManager.popMatrix();
		}
		for (Entry<Float[], ModifyText> entry : this.locationClickableMap.entrySet()) {
			float x = entry.getKey()[0];
			float y = entry.getKey()[1];
			ModifyText modText = entry.getValue();
			GlStateManager.pushMatrix();
			GlStateManager.scale(modText.scale, modText.scale, 1);
			if (modText.mouseOver)
				font.drawString(modText.text, x, y, ColorUtils.LIGHT_BLUE, true);
			else
				font.drawString(modText.text, x, y, modText.color, true);
			GlStateManager.popMatrix();
		}
	}
	
	@Override
	public boolean hasBackground() {
		return false;
	}
	
	@Override
	public boolean hasBorder() {
		return false;
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
		
		public static String bold() {
			return "{bold}";
		}
		
		public static String italic() {
			return "{italic}";
		}
		
		public static String underline() {
			return "{underline}";
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
		
		public static String tab() {
			return scale(1) + "     " + end();
		}
		
		public static String addText(double scale, int color, boolean clickable, int newLines, String text, boolean italic, boolean bold, boolean underline) {
			String attribute = italic ? italic() : "";
			attribute += bold ? bold() : "";
			attribute += underline ? underline() : "";
			String click = clickable ? clickable() : "";
			return scale(scale) + click + color(color) + newLines(newLines) + attribute + text + end();
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
		
		public static boolean isBold(String clickable) {
			Matcher matcher = FORMATTING_BOLD_PATTERN.matcher(clickable);
			return matcher.find();
		}
		
		public static boolean isItalic(String clickable) {
			Matcher matcher = FORMATTING_ITALIC_PATTERN.matcher(clickable);
			return matcher.find();
		}
		
		public static boolean isUnderline(String clickable) {
			Matcher matcher = FORMATTING_UNDERLINED_PATTERN.matcher(clickable);
			return matcher.find();
		}
	}
	
	private class ModifyText {
		public double scale = 0;
		public int color = ColorUtils.WHITE;
		public boolean clickable = false;
		public boolean italic = false;
		public boolean underline = false;
		public boolean bold = false;
		public String text = "";
		public int newLines = 0;
		public boolean mouseOver = false;
		
		public ModifyText() {
			
		}
		
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
			ModifyText copy = new ModifyText(this.scale, this.color, this.clickable, this.newLines, this.text);
			copy.bold = this.bold;
			copy.italic = this.italic;
			copy.underline = this.underline;
			return copy;
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
	
	private class ModifyTextMap<K, V> extends LinkedHashMap<Integer, List<ModifyText>> {
		
		public void add(int key, ModifyText singleValue) {
			if (this.containsKey(key)) {
				this.get(key).add(singleValue);
			} else {
				List<ModifyText> modText = new ArrayList<ModifyText>();
				modText.add(singleValue);
				this.put(key, modText);
			}
		}
		
		public int maxNewLine(int key) {
			int max = 0;
			for (ModifyText modText : this.get(key))
				max = Math.max(modText.newLines, max);
			return max;
		}
		
		public float maxHeight(int key) {
			float max = font.FONT_HEIGHT;
			for (ModifyText modText : this.get(key))
				max = (float) Math.max(modText.scale * font.FONT_HEIGHT, max);
			
			return max;
		}
	}
}
