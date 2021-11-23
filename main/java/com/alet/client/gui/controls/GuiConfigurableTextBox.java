package com.alet.client.gui.controls;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.creativemd.creativecore.common.gui.GuiRenderHelper;
import com.creativemd.creativecore.common.gui.client.style.Style;
import com.creativemd.creativecore.common.gui.controls.gui.GuiTextBox;
import com.creativemd.creativecore.common.utils.mc.ColorUtils;

import net.minecraft.client.renderer.GlStateManager;

public class GuiConfigurableTextBox extends GuiTextBox {
	
	public GuiConfigurableTextBox(String name, String text, int x, int y, int width) {
		super(name, text, x, y, width);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void setText(String text) {
		super.setText(text);
	}
	
	@Override
	protected void renderContent(GuiRenderHelper helper, Style style, int width, int height) {
		
		if (!text.isEmpty() && !text.equals("")) {
			int y = 0;
			boolean flag = false;
			double scale = 1;
			List<TextData> listOfStrings = new ArrayList<TextData>();
			
			Pattern sizePattern = Pattern.compile("\\{size:\\d+\\}", Pattern.DOTALL);
			Pattern endPattern = Pattern.compile("\\{end\\}", Pattern.DOTALL);
			Matcher sizeMatcher = sizePattern.matcher(text);
			
			Pattern numberPattern = Pattern.compile("\\d+");
			int x = 0;
			int length = 0;
			String cleanText = text.replaceAll("\\{size:\\d+\\}", "").replaceAll("\\{end\\}", "");
			while (sizeMatcher.find()) {
				String sub = text.substring(sizeMatcher.start(), text.length());
				Matcher endMatcher = endPattern.matcher(sub);
				Matcher numberMatcher = numberPattern.matcher(sizeMatcher.group());
				if (numberMatcher.find()) {
					scale = Integer.parseInt(numberMatcher.group());
				}
				if (endMatcher.find()) {
					int start = text.length() - sub.length();
					String sub2 = text.substring(start, endMatcher.end() + start);
					length += sub2.length();
					
					sub2 = sub2.replaceAll("\\{size:\\d+\\}", "").replaceAll("\\{end\\}", "");
					listOfStrings.add(new TextData(sub2, scale, ColorUtils.WHITE));
				}
			}
			if (length != text.length()) {
				String sub3 = text.substring(length + 1);
				listOfStrings.add(new TextData(sub3, 1, ColorUtils.WHITE));
			}
			for (TextData data : listOfStrings) {
				int w = (int) ((width / data.size));
				w -= 5 + Math.floor(data.size);
				for (String s : font.listFormattedStringToWidth(data.text, w)) {
					GlStateManager.pushMatrix();
					GlStateManager.scale(data.size, data.size, 1);
					if (y > 1)
						GlStateManager.translate(0, -data.size * 9, 0);
					font.drawString(s, x, y, color, true);
					GlStateManager.popMatrix();
					y += font.FONT_HEIGHT;
				}
			}
			this.height = (int) ((y + getContentOffset() * 2));
			
		}
		
	}
	/*
	 * 
				GlStateManager.pushMatrix();
				GlStateManager.scale(scale, scale, 1);
				font.drawString(c + "", x, y, color, true);
				GlStateManager.popMatrix();
				x += font.getCharWidth(c);
	 */
	
	public class TextData {
		
		String text;
		double size;
		int color;
		
		public TextData(String text, double scale, int color) {
			this.text = text;
			this.size = scale;
			this.color = color;
		}
	}
}
