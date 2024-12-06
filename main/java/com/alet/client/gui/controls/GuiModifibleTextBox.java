package com.alet.client.gui.controls;

import java.awt.Desktop;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.lwjgl.util.Color;

import com.alet.client.gui.origins.SubGuiManual;
import com.creativemd.creativecore.common.gui.GuiRenderHelper;
import com.creativemd.creativecore.common.gui.client.style.Style;
import com.creativemd.creativecore.common.gui.controls.gui.GuiTextBox;
import com.creativemd.creativecore.common.utils.mc.ColorUtils;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.text.TextFormatting;

public class GuiModifibleTextBox extends GuiTextBox {
    
    List<String> listOfText = new ArrayList<String>();
    List<ModifyText> listOfModifyText = new ArrayList<ModifyText>();
    Map<Float[], ModifyText> locationTextMap = new LinkedHashMap<Float[], ModifyText>();
    Map<Float[], ModifyText> locationImageMap = new LinkedHashMap<Float[], ModifyText>();
    Map<Float[], ModifyText> locationClickableMap = new LinkedHashMap<Float[], ModifyText>();
    
    ModifyTextMap map = new ModifyTextMap();
    public static final Pattern FORMATTING_NEWLINE_PATTERN = Pattern.compile(
        "\\{newLines:[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?\\}");
    public static final Pattern FORMATTING_SCALE_PATTERN = Pattern.compile(
        "\\{scale:[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?\\}");
    public static final Pattern FORMATTING_ITALIC_PATTERN = Pattern.compile("\\{italic\\}");
    public static final Pattern FORMATTING_BOLD_PATTERN = Pattern.compile("\\{bold\\}");
    public static final Pattern FORMATTING_UNDERLINED_PATTERN = Pattern.compile("\\{underline\\}");
    public static final Pattern FORMATTING_CLICKABLE_PATTERN = Pattern.compile("\\{clickable\\}");
    public static final Pattern FORMATTING_COLOR_PATTERN = Pattern.compile(
        "\\{color:[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?\\}");
    public static final Pattern FORMATTING_END_PATTERN = Pattern.compile("\\{end\\}");
    public static final Pattern FORMATTING_NUMBER_PATTERN = Pattern.compile("[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?");
    public static final Pattern FORMATTING_STRING_PARTERN = Pattern.compile("[a-zA-Z]+");
    public static final Pattern FORMATTING_SIMPLE_NUMBER_PATTERN = Pattern.compile("[0-9]+");
    public static final Pattern FORMATTING_DOUBLE_PATTERN = Pattern.compile("[-+]?[0-9]+(\\.+[0-9]+)?");
    public static final Pattern FORMATTING_IMAGE_PATTERN = Pattern.compile("\\{image:[a-zA-Z]+\\.[a-zA-Z]+\\}");
    public static final Pattern FORMATTING_FILE_PATTERN = Pattern.compile("[a-zA-Z]+\\.[a-zA-Z]+");
    public static final Pattern FORMATTING_CENTER_PATTERN = Pattern.compile("\\{center\\}");
    
    public static final Pattern FORMATTING_KEYWORD_PATTERN = Pattern.compile("\\{key\\}");
    
    public static final Pattern FORMATTING_REFRENCE_PATTERN = Pattern.compile("\\{ref:[a-zA-Z]+\\}");
    public static final Pattern FORMATTING_URL_PATTERN = Pattern.compile(
        "\\{url:\"https?:\\/\\/(?:www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b(?:[-a-zA-Z0-9()@:%_\\+.~#?&\\/=]*)\"\\}");
    public static final Pattern FORMATTING_URL_ADDRESS_PATTERN = Pattern.compile(
        "https?:\\/\\/(?:www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b(?:[-a-zA-Z0-9()@:%_\\+.~#?&\\/=]*)");
    
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
            Matcher matcherKeyword = FORMATTING_KEYWORD_PATTERN.matcher(text);
            Matcher matcherCenter = FORMATTING_CENTER_PATTERN.matcher(text);
            Matcher matcherColor = FORMATTING_COLOR_PATTERN.matcher(text);
            Matcher matcherImage = FORMATTING_IMAGE_PATTERN.matcher(text);
            Matcher matcherEnd = FORMATTING_END_PATTERN.matcher(text);
            Matcher matcherRef = FORMATTING_REFRENCE_PATTERN.matcher(text);
            Matcher matcherURL = FORMATTING_URL_PATTERN.matcher(text);
            
            ModifyText modText = new ModifyText(0, ColorUtils.WHITE, false, 0, "");
            modText.scale = 1;
            modText.color = -1;
            modText.newLines = 0;
            if (matcherScale.find()) {
                Matcher matcher = FORMATTING_NUMBER_PATTERN.matcher(matcherScale.group());
                matcher.find();
                modText.scale = Double.parseDouble(matcher.group());
                text = text.replaceAll(FORMATTING_SCALE_PATTERN.pattern(), "");
            }
            if (matcherNewLine.find()) {
                Matcher matcher = FORMATTING_NUMBER_PATTERN.matcher(matcherNewLine.group());
                matcher.find();
                modText.newLines = Integer.parseInt(matcher.group());
                text = text.replaceAll(FORMATTING_NEWLINE_PATTERN.pattern(), "");
            }
            if (matcherURL.find()) {
                System.out.println("da");
                Matcher matcher = FORMATTING_URL_ADDRESS_PATTERN.matcher(matcherURL.group());
                matcher.find();
                modText.clickable = true;
                modText.italic = true;
                modText.underline = true;
                modText.color = 0x2222FF;
                modText.url = matcher.group();
                text = text.replaceAll(FORMATTING_URL_PATTERN.pattern(), "");
            }
            if (matcherClickable.find()) {
                modText.clickable = true;
                modText.italic = true;
                modText.underline = true;
                modText.color = 0x2222FF;
                text = text.replaceAll(FORMATTING_CLICKABLE_PATTERN.pattern(), "");
            }
            if (matcherRef.find()) {
                Matcher matcher = FORMATTING_STRING_PARTERN.matcher(matcherRef.group().split(":")[1]);
                matcher.find();
                modText.clickable = true;
                modText.italic = true;
                modText.underline = true;
                modText.color = 0x2222FF;
                modText.refrence = matcher.group();
                text = text.replaceAll(FORMATTING_REFRENCE_PATTERN.pattern(), "");
            }
            if (matcherColor.find()) {
                Matcher matcher = FORMATTING_NUMBER_PATTERN.matcher(matcherColor.group());
                matcher.find();
                modText.color = Integer.parseInt(matcher.group());
                text = text.replaceAll(FORMATTING_COLOR_PATTERN.pattern(), "");
            }
            if (matcherBold.find()) {
                modText.bold = true;
                text = text.replaceAll(FORMATTING_BOLD_PATTERN.pattern(), "");
            }
            if (matcherCenter.find()) {
                modText.center = true;
                text = text.replaceAll(FORMATTING_CENTER_PATTERN.pattern(), "");
            }
            if (matcherItalic.find()) {
                modText.italic = true;
                text = text.replaceAll(FORMATTING_ITALIC_PATTERN.pattern(), "");
            }
            if (matcherUnderline.find()) {
                modText.underline = true;
                text = text.replaceAll(FORMATTING_UNDERLINED_PATTERN.pattern(), "");
            }
            if (matcherKeyword.find()) {
                modText.scale = 1.1D;
                modText.italic = true;
                modText.color = 65535;
                text = text.replaceAll(FORMATTING_KEYWORD_PATTERN.pattern(), "");
            }
            if (matcherImage.find()) {
                String imageName = matcherImage.group();
                Matcher matcher = FORMATTING_FILE_PATTERN.matcher(imageName);
                matcher.find();
                imageName = matcher.group();
                String fileType = matcher.group().split("\\.")[1];
                if (fileType.equals("png") || fileType.equals("jpeg"))
                    modText.setImagePath("assets/alet/images/" + imageName);
                
                modText.newLines = 2;
                modText.scale = 0.28;
                text = text.replaceAll(FORMATTING_IMAGE_PATTERN.pattern(), "");
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
            if (!modText.clickable && modText.imagePath == null && !modText.center)
                for (String s : modText.text.split("((?<= )|(?= ))")) {
                    ModifyText copy = modText.copy();
                    if (flag1)
                        copy.newLines = 0;
                    flag1 = true;
                    
                    list.add(copy.setText(s));
                }
            else
                list.add(modText);
        }
        int widthCount = 0;
        int line = 0;
        
        for (ModifyText modText : list) {
            double scale = modText.scale;
            line += modText.newLines;
            if (modText.newLines != 0)
                widthCount = 0;
            
            widthCount += font.getStringWidth(modText.text) * scale;
            
            if (widthCount >= this.width + 10) {
                if (modText.text.equals(" "))
                    modText.text = "";
                
                widthCount = (int) (font.getStringWidth(modText.text) * scale);
                line++;
            }
            if (modText.bold)
                modText.text = TextFormatting.BOLD + modText.text + TextFormatting.RESET;
            if (modText.italic)
                modText.text = TextFormatting.ITALIC + modText.text + TextFormatting.RESET;
            if (modText.underline)
                modText.text = TextFormatting.UNDERLINE + modText.text + TextFormatting.RESET;
            this.map.add(line, modText);
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
                
                int height = font.FONT_HEIGHT;
                float scaledHeight = (float) (height * modText.scale);
                float maxHeight = map.maxHeight(key);
                float addY = scaledHeight - maxHeight;
                
                if (modText.imagePath != null) {
                    this.locationImageMap.put(
                        new Float[] { (float) ((this.width / 2) - ((modText.imageWidth * modText.scale) / 2)), y + 2 },
                        modText);
                } else if (modText.clickable)
                    this.locationClickableMap.put(
                        new Float[] { (float) (x / modText.scale), (float) ((y - addY) / modText.scale) }, modText);
                else {
                    float nX = (float) (x / modText.scale);
                    if (modText.center)
                        nX = (float) ((this.width / 2) - ((font.getStringWidth(modText.text) * modText.scale) / 2));
                    this.locationTextMap.put(new Float[] { nX, (float) ((y - addY) / modText.scale) }, modText);
                }
                
                if (modText.imagePath != null)
                    y += (float) (modText.imageHeight * modText.scale);
                
                x += font.getStringWidth(modText.text) * modText.scale;
                
            }
            y += map.maxHeight(key) + 1;
        }
        this.height = (int) y + 9;
        
    }
    
    public boolean isMouseOverClickable(int posX, int posY) {
        for (Entry<Float[], ModifyText> entry : this.locationClickableMap.entrySet()) {
            float x = entry.getKey()[0];
            float y = entry.getKey()[1];
            ModifyText modText = entry.getValue();
            modText.mouseOver = (posX >= x && posX < x + ((font.getStringWidth(
                modText.text) + 5) * modText.scale) && posY >= y + 5 && posY < y + 2 + (font.FONT_HEIGHT * modText.scale));
            if (modText.mouseOver && !modText.url.isEmpty()) {
                customTooltip = new ArrayList<>(Arrays.asList(modText.url));
                break;
            } else
                customTooltip = null;
        }
        return false;
    }
    
    @Override
    public boolean isMouseOver(int posX, int posY) {
        boolean results = super.isMouseOver(posX, posY);
        isMouseOverClickable(posX, posY);
        return results;
    }
    
    public void addImages() {
        for (Entry<Float[], ModifyText> entry : this.locationImageMap.entrySet()) {
            String fileType = entry.getValue().imagePath.split("\\.")[1];
            if (fileType.equals("png") || fileType.equals("jpeg")) {
                this.getParent().addControl(new GuiImage("", entry.getValue().imagePath, entry.getKey()[0].intValue(), entry
                        .getKey()[1].intValue(), entry.getValue().scale));
            }
        }
        
        /* for (ModifyText text : this.locationImageList) {
            String fileType = text.imageName.split("\\.")[1];
            if (fileType.equals("png") || fileType.equals("jpeg"))
                GuiModifibleTextBox.this.getParent().addControl(
                    new GuiImage("", TextureUtil.readBufferedImage(getClass().getClassLoader().getResourceAsStream(path)), text.imageX, text.imageY, text.imageScale));
            else if (fileType.equals("gif"))
                GuiModifibleTextBox.this.getParent().addControl(
                    new GuiGIF("", "assets/alet/images/" + text.imageName, text.imageX, text.imageY, text.imageScale));
            GuiModifibleTextBox.this.getParent().refreshControls();
        }*/
        
    }
    
    @Override
    public void mouseReleased(int x, int y, int button) {
        for (ModifyText modText : this.locationClickableMap.values()) {
            if (modText.mouseOver) {
                playSound(SoundEvents.UI_BUTTON_CLICK);
                clickedOn(modText.text);
                if (!modText.refrence.isEmpty()) {
                    SubGuiManual gui = (SubGuiManual) this.getParent().getOrigin();
                    gui.gotoPage(modText.refrence);
                }
                if (!modText.url.isEmpty()) {
                    try {
                        Desktop.getDesktop().browse(new URL(modText.url).toURI());
                    } catch (IOException | URISyntaxException e) {
                        e.printStackTrace();
                    }
                }
                modText.mouseOver = false;
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
            modText.text = modText.text.replaceAll("_", " ");
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
        
        public static String addTitle(String text, int line) {
            return addText(2, ColorUtils.WHITE, false, line, text, false, true, false);
        }
        
        public static String addSubTitle(String text, int line) {
            return addText(1.5, ColorUtils.WHITE, false, line, text, false, true, false);
        }
        
        public static String addSecondSubTitle(String text, int line) {
            return addText(1.2, ColorUtils.WHITE, false, line, text, false, true, false);
        }
        
        public static String addDefaultText(String text, int line) {
            return addText(1, ColorUtils.WHITE, false, line, text, false, false, false);
        }
        
        public static String addDefintionText(String text) {
            return addText(1.1, 0x00FFFF, false, 0, text, false, true, false);
        }
        
        public static String addDefintionText(String text, int line) {
            return addText(1.1, 0x00FFFF, false, line, text, false, true, false);
        }
        
        public static String addClickableText(String text) {
            return addText(1, ColorUtils.WHITE, true, 0, text, true, true, true);
        }
        
    }
    
    private class ModifyText {
        public double scale = 1;
        public int color = ColorUtils.WHITE;
        public boolean clickable = false;
        public boolean italic = false;
        public boolean center = false;
        public boolean underline = false;
        public boolean bold = false;
        public String text = "";
        public int newLines = 0;
        public boolean mouseOver = false;
        private String imagePath;
        public float imageWidth;
        public float imageHeight;
        public String refrence = "";
        public String url = "";
        
        @SuppressWarnings("unused")
        public ModifyText() {
            
        }
        
        public ModifyText(double scale, int color, boolean clickable, int newLines, String text) {
            this.scale = scale;
            this.color = color;
            this.clickable = clickable;
            this.text = text;
            this.newLines = newLines;
        }
        
        public ModifyText setImagePath(String imagePath) {
            this.imagePath = imagePath;
            BufferedImage image = null;
            InputStream stream = null;
            stream = getClass().getClassLoader().getResourceAsStream(imagePath);
            try {
                image = TextureUtil.readBufferedImage(stream);
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.imageWidth = image.getWidth();
            this.imageHeight = image.getHeight();
            return this;
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
            
            return this.scale == text.scale && this.clickable == text.clickable && this.color == text.color && this.newLines == text.newLines && this.text
                    .equals(text.text);
        }
        
        @Override
        public String toString() {
            return this.text + ": {scale:" + this.scale + "}," + "{color:" + this.color + "}," + "{clickable:" + this.clickable + "}," + "{newLines:" + this.newLines + "}";
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
        
        public int maxImageHeight(int key) {
            int max = 0;
            for (ModifyText modText : this.get(key)) {
                max = (int) Math.max(modText.scale * modText.imageHeight, max);
            }
            
            return max;
            
        }
        
        public float maxHeight(int key) {
            float max = font.FONT_HEIGHT;
            for (ModifyText modText : this.get(key)) {
                max = (float) Math.max(modText.scale * font.FONT_HEIGHT, max);
            }
            
            return max;
        }
    }
}
