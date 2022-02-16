package com.alet.client.gui;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.util.Color;

import com.alet.ALET;
import com.alet.client.gui.controls.GuiDepressedCheckBox;
import com.alet.client.gui.controls.GuiGlyphSelector;
import com.alet.client.gui.controls.GuiLongTextField;
import com.alet.client.gui.controls.Layer;
import com.alet.client.gui.message.SubGuiNoTextInFieldMessage;
import com.alet.client.gui.tutorial.controls.GuiTutorialBox;
import com.alet.client.gui.tutorial.controls.TutorialData;
import com.alet.common.packet.PacketUpdateStructureFromClient;
import com.alet.common.structure.type.premade.LittleTypeWriter;
import com.alet.common.util.CopyUtils;
import com.alet.font.FontReader;
import com.alet.littletiles.gui.controls.GuiAnimationViewerAlet;
import com.alet.littletiles.gui.controls.GuiColorPickerAlet;
import com.alet.photo.PhotoReader;
import com.creativemd.creativecore.common.gui.container.SubGui;
import com.creativemd.creativecore.common.gui.controls.gui.GuiAnalogeSlider;
import com.creativemd.creativecore.common.gui.controls.gui.GuiButton;
import com.creativemd.creativecore.common.gui.controls.gui.GuiCheckBox;
import com.creativemd.creativecore.common.gui.controls.gui.GuiComboBox;
import com.creativemd.creativecore.common.gui.controls.gui.GuiComboBoxExtension;
import com.creativemd.creativecore.common.gui.controls.gui.GuiTextfield;
import com.creativemd.creativecore.common.gui.event.gui.GuiControlChangedEvent;
import com.creativemd.creativecore.common.packet.PacketHandler;
import com.creativemd.creativecore.common.utils.mc.ColorUtils;
import com.creativemd.littletiles.LittleTiles;
import com.creativemd.littletiles.common.entity.AnimationPreview;
import com.creativemd.littletiles.common.structure.LittleStructure;
import com.creativemd.littletiles.common.util.grid.LittleGridContext;
import com.n247s.api.eventapi.eventsystem.CustomEventSubscribe;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;

public class SubGuiTypeWriter extends SubGui {
    
    public static List<String> names = ALET.fontTypeNames;
    public Map<TextAttribute, Object> textAttributeMap = new HashMap<TextAttribute, Object>();
    public LittleStructure structure;
    public int BLACK = ColorUtils.BLACK;
    public GuiCheckBox keepAspect = null;
    public GuiTextfield imgHeight = null;
    public GuiTextfield imgWidth = null;
    public NBTTagCompound nbt = new NBTTagCompound();
    
    public double aspectRatio = 0;
    
    public SubGuiTypeWriter(LittleStructure structure) {
        super(422, 190);
        this.structure = structure;
    }
    
    public void openedGui() {
        LittleTypeWriter typeWriter = (LittleTypeWriter) structure;
        typeWriter.writeToNBT(nbt);
        if (nbt.hasKey("font")) {
            GuiComboBox fontBox = (GuiComboBox) get("fontType");
            fontBox.select(nbt.getString("font"));
        }
        if (nbt.hasKey("fontSize")) {
            GuiTextfield fontSize = (GuiTextfield) get("fontSize");
            fontSize.text = nbt.getString("fontSize");
        }
        if (nbt.hasKey("color")) {
            GuiColorPickerAlet picker = (GuiColorPickerAlet) get("picker");
            picker.setColor(ColorUtils.IntToRGBA(nbt.getInteger("color")));
        }
        if (nbt.hasKey("grid")) {
            GuiComboBox gridBox = (GuiComboBox) get("grid");
            gridBox.select(nbt.getString("grid"));
        }
        if (nbt.hasKey("rotation")) {
            GuiAnalogeSlider rotation = (GuiAnalogeSlider) get("rotation");
            rotation.value = nbt.getDouble("rotation");
        }
        if (nbt.hasKey("italic")) {
            GuiDepressedCheckBox italic = (GuiDepressedCheckBox) get("italic");
            italic.value = nbt.getBoolean("italic");
            if (italic.value)
                this.textAttributeMap.put(TextAttribute.POSTURE, TextAttribute.POSTURE_OBLIQUE);
            else
                this.textAttributeMap.remove(TextAttribute.POSTURE);
        }
        if (nbt.hasKey("bold")) {
            GuiDepressedCheckBox bold = (GuiDepressedCheckBox) get("bold");
            bold.value = nbt.getBoolean("bold");
            if (bold.value)
                this.textAttributeMap.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
            else
                this.textAttributeMap.remove(TextAttribute.WEIGHT);
        }
        if (nbt.hasKey("underline")) {
            GuiDepressedCheckBox underline = (GuiDepressedCheckBox) get("underline");
            underline.value = nbt.getBoolean("underline");
            if (underline.value)
                this.textAttributeMap.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
            else
                this.textAttributeMap.remove(TextAttribute.UNDERLINE);
        }
        if (nbt.hasKey("strikethrough")) {
            GuiDepressedCheckBox strikethrough = (GuiDepressedCheckBox) get("strikethrough");
            strikethrough.value = nbt.getBoolean("strikethrough");
            if (strikethrough.value)
                this.textAttributeMap.put(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
            else
                this.textAttributeMap.remove(TextAttribute.STRIKETHROUGH);
        }
    }
    
    @Override
    public void onClosed() {
        GuiColorPickerAlet picker = (GuiColorPickerAlet) get("picker");
        GuiTextfield fontSize = (GuiTextfield) get("fontSize");
        GuiComboBox fontBox = (GuiComboBox) get("fontType");
        GuiComboBox gridBox = (GuiComboBox) get("grid");
        GuiAnalogeSlider rotation = (GuiAnalogeSlider) get("rotation");
        GuiDepressedCheckBox italic = (GuiDepressedCheckBox) get("italic");
        GuiDepressedCheckBox bold = (GuiDepressedCheckBox) get("bold");
        GuiDepressedCheckBox underline = (GuiDepressedCheckBox) get("underline");
        GuiDepressedCheckBox strikethrough = (GuiDepressedCheckBox) get("strikethrough");
        
        LittleTypeWriter typeWriter = (LittleTypeWriter) structure;
        nbt.setString("font", fontBox.getCaption());
        nbt.setString("fontSize", fontSize.text);
        nbt.setInteger("color", ColorUtils.RGBAToInt(picker.color));
        nbt.setString("grid", gridBox.getCaption());
        nbt.setDouble("rotation", rotation.value);
        nbt.setBoolean("italic", italic.value);
        nbt.setBoolean("bold", bold.value);
        nbt.setBoolean("underline", underline.value);
        nbt.setBoolean("strikethrough", strikethrough.value);
        
        System.out.println(nbt);
        
        PacketHandler.sendPacketToServer(new PacketUpdateStructureFromClient(typeWriter.getStructureLocation(), nbt));
        
        super.onClosed();
    }
    
    @Override
    public void createControls() {
        LittleTypeWriter typeWriter = (LittleTypeWriter) structure;
        typeWriter.writeToNBT(nbt);
        //System.out.println(nbt);
        Color color = ColorUtils.IntToRGBA(BLACK);
        if (nbt.hasKey("color"))
            color = ColorUtils.IntToRGBA(nbt.getInteger("color"));
        
        GuiAnimationViewerAlet viewer = new GuiAnimationViewerAlet("renderer", 280, 0, 136, 135);
        controls.add(viewer);
        viewer.moveViewPort(0, 50);
        
        //GuiCheckBox keepAspect = new GuiCheckBox("keepAspect", translate("Keep Aspect Ratio?"), -3, 65, false);
        //controls.add(keepAspect);
        
        imgWidth = (new GuiTextfield("imgWidth", "0", 347, 170, 30, 14));
        imgWidth.setCustomTooltip("Width Of Image");
        imgWidth.setNumbersOnly();
        controls.add(imgWidth);
        imgHeight = (new GuiTextfield("imgHeight", "0", 386, 170, 30, 14));
        imgHeight.setCustomTooltip("Height Of Image");
        imgHeight.setNumbersOnly();
        controls.add(imgHeight);
        
        controls.add(new GuiDepressedCheckBox("italic", "I", 140, 44, 19, 19, TextFormatting.ITALIC + "", false));
        controls.add(new GuiDepressedCheckBox("bold", "B", 160, 44, 19, 19, TextFormatting.BOLD + "", false));
        controls.add(new GuiDepressedCheckBox("underline", "U", 180, 44, 19, 19, TextFormatting.UNDERLINE + "", false));
        controls.add(new GuiDepressedCheckBox("strikethrough", "S", 200, 44, 19, 19, TextFormatting.STRIKETHROUGH + "", false));
        
        controls.add(new GuiColorPickerAlet("picker", -2, 42, color, LittleTiles.CONFIG.isTransparencyEnabled(getPlayer()), LittleTiles.CONFIG
                .getMinimumTransparency(getPlayer())));
        
        controls.add(new GuiComboBox("grid", 256, 0, 15, LittleGridContext.getNames()) {
            
            @Override
            protected GuiComboBoxExtension createBox() {
                return new GuiComboBoxExtension(name + "extension", this, posX, posY + height, 30 - getContentOffset() * 2, 100, lines);
            }
        });
        
        GuiTextfield fontSize = new GuiTextfield("fontSize", "48", 229, 0, 20, 14);
        controls.add(fontSize);
        
        //this.moveControlAbove(fontSize, get("hi"));
        controls.add(new GuiLongTextField("input", "", 0, 21, 271, 15));
        
        controls.add(new GuiTextfield("search", "", 0, 0, 115, 14) {
            
            @Override
            public boolean onKeyPressed(char character, int key) {
                boolean result = super.onKeyPressed(character, key);
                if (!result)
                    return result;
                
                GuiComboBox fontType = (GuiComboBox) get("fontType");
                
                List<String> foundFonts = new ArrayList<>();
                for (int i = 0; i < ALET.fontTypeNames.size(); i++) {
                    if (ALET.fontTypeNames.get(i).toLowerCase().contains(this.text.toLowerCase()))
                        foundFonts.add(ALET.fontTypeNames.get(i));
                }
                if (!foundFonts.isEmpty()) {
                    fontType.lines = foundFonts;
                    int index = ALET.fontTypeNames.indexOf(foundFonts.get(0));
                    fontType.select(ALET.fontTypeNames.get(index));
                }
                
                return result;
            }
        });
        
        GuiTextfield fontSearch = (GuiTextfield) get("search");
        fontSearch.setCustomTooltip("Search for Font");
        
        controls.add(new GuiComboBox("fontType", 122, 0, 100, ALET.fontTypeNames) {
            
            @Override
            protected GuiComboBoxExtension createBox() {
                GuiComboBoxExtension extend = new GuiComboBoxExtension(name + "extension", this, posX, posY + height, 200 - getContentOffset() * 2, 100, lines);
                extend.scrolled.set(15 * this.index);
                return extend;
            }
        });
        GuiComboBox fontCombo = (GuiComboBox) get("fontType");
        fontCombo.setCustomTooltip("Font");
        
        controls.add(new GuiAnalogeSlider("rotation", 0, 96, 150, 10, 0, 0, 360));
        
        controls.add(new GuiButton("refresh", "Refresh Preview", 326, 145, 90) {
            
            @Override
            public void onClicked(int x, int y, int button) {
                GuiLongTextField input = (GuiLongTextField) get("input");
                if (input.text.equals(""))
                    Layer.addLayer(getGui(), new SubGuiNoTextInFieldMessage("for the text that will be exported", "digit(s) and or character(s)"));
                else {
                    GuiColorPickerAlet picker = (GuiColorPickerAlet) get("picker");
                    int color = ColorUtils.RGBAToInt(picker.color);
                    
                    GuiTextfield contextField = (GuiTextfield) get("fontSize");
                    int fontSize = Integer.parseInt(contextField.text);
                    
                    GuiComboBox contextBox = (GuiComboBox) get("fontType");
                    String font = contextBox.getCaption();
                    
                    GuiComboBox contextBox_2 = (GuiComboBox) get("grid");
                    int grid = Integer.parseInt(contextBox_2.getCaption());
                    
                    GuiAnalogeSlider rotation = (GuiAnalogeSlider) get("rotation");
                    try {
                        PhotoReader.setScale(Integer.parseInt(imgWidth.text), Integer.parseInt(imgHeight.text));
                        viewer.onLoaded(new AnimationPreview(FontReader.photoToPreviews(input.text, font, textAttributeMap, grid, fontSize, color, rotation.value)));
                    } catch (NullPointerException | IOException e) {}
                }
            }
        });
        
        controls.add(new GuiButton("Paste", "Paste", 221, 43, 50) {
            
            @Override
            public void onClicked(int x, int y, int button) {
                
                GuiLongTextField input = (GuiLongTextField) get("input");
                
                StringSelection stringSelection = new StringSelection(input.text);
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                String path = CopyUtils.getCopiedFilePath(clipboard);
                if (path == null)
                    return;
                try {
                    input.text = path;
                } catch (Exception e) {
                
                }
            }
        });
                
        GuiComboBox contextBox = (GuiComboBox) get("fontType");
        String font = contextBox.getCaption();
        GuiAnalogeSlider rotation = (GuiAnalogeSlider) get("rotation");
        
        controls.add(new GuiGlyphSelector("glyph", font, textAttributeMap, 165, 85, 106));
        controls.add(new GuiButton("Print", 243, 64, 28) {
            
            @Override
            public void onClicked(int x, int y, int button) {
                GuiLongTextField input = (GuiLongTextField) get("input");
                if (input.text.equals(""))
                    Layer.addLayer(getGui(), new SubGuiNoTextInFieldMessage("for the text that will be exported", "digit(s) and or character(s)"));
                else {
                    GuiColorPickerAlet picker = (GuiColorPickerAlet) get("picker");
                    int color = ColorUtils.RGBAToInt(picker.color);
                    
                    GuiTextfield contextField = (GuiTextfield) get("fontSize");
                    int fontSize = Integer.parseInt(contextField.text);
                    
                    GuiComboBox contextBox = (GuiComboBox) get("fontType");
                    String font = contextBox.getCaption();
                    
                    GuiComboBox contextBox_2 = (GuiComboBox) get("grid");
                    int grid = Integer.parseInt(contextBox_2.getCaption());
                    
                    GuiAnalogeSlider rotation = (GuiAnalogeSlider) get("rotation");
                    
                    try {
                        PhotoReader.setScale(Integer.parseInt(imgWidth.text), Integer.parseInt(imgHeight.text));
                        NBTTagCompound nbt = FontReader.photoToNBT(input.text, font, textAttributeMap, grid, fontSize, color, rotation.value);
                        if (nbt != null)
                            sendPacketToServer(nbt);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        
        GuiTutorialBox boxx = new GuiTutorialBox("ih", -110, 0, 180, width, height);
        System.out.println(((GuiColorPickerAlet) get("picker")).get("r"));
        GuiColorPickerAlet picker = ((GuiColorPickerAlet) get("picker"));
        boxx.tutorialMap
                .add(new TutorialData(get("fontType"), "right", "Clicking on this will open a dropdown menu that lets you select the type of font your text will print with. You can add custom font(s) by going to your Minecraft directory, the same place you add mods too, and look for a folder called Fonts."));
        boxx.tutorialMap.add(new TutorialData(get("fontType"), "right", "The readme.txt file inside will explain further on how to bring your new fonts into the game."));
        boxx.tutorialMap.add(new TutorialData(get("search"), "right", "This is the search text field. It allows you to search for specific font(s)."));
        boxx.tutorialMap.add(new TutorialData(get("fontSize"), "right", "This is the font size text field. It allows you to change the font size that your text will print in."));
        boxx.tutorialMap
                .add(new TutorialData(get("grid"), "right", "This is the grid drop down menu. Clicking on this will open a dropdown menu that allows you to change the grid size that your text will print with."));
        boxx.tutorialMap
                .add(new TutorialData(get("grid"), "right", "The grid size is how small or large each tile will be. Grid 8 is 1/8th the size of a block. Grid 16 is 1/16th the size of a block. Grid 32 is 1/32nd the size of a block and so on."));
        boxx.tutorialMap.add(new TutorialData(get("input"), "right", "This is the input text field where you type whatever text you want to print."));
        boxx.tutorialMap.add(new TutorialData(get("Paste"), "right", "This is the paste button. It will allow you to paste text into the text field that you have copied."));
        boxx.tutorialMap
                .add(new TutorialData(get("glyph"), "right", "This is the glyph drop down menu. Clicking on this will open a dropdown menu that displays all glyphs (or characters) that the font you have selected has. You can click on the glyphs to add it into the input text field. However, some glyphs are unusable, such as Wingding."));
        boxx.tutorialMap.add(new TutorialData(get("italic"), "right", "This is the italic button. Clicking on this will cause the text to print with the italic attribute."));
        boxx.tutorialMap.add(new TutorialData(get("bold"), "right", "This is the bold button. Clicking on this will cause the text to print with the bold attribute."));
        boxx.tutorialMap
                .add(new TutorialData(get("underline"), "right", "This is the underline button. Clicking on this will cause the text to print with the underline attribute."));
        boxx.tutorialMap
                .add(new TutorialData(get("strikethrough"), "right", "This is the strikethrough button. Clicking on this will cause the text to print with the strikethrough attribute."));
        boxx.tutorialMap.add(new TutorialData(get("rotation"), "right", "This is the rotation slider. You can click and drag to change the roation of the text."));
        boxx.tutorialMap.add(new TutorialData(get("picker"), "right", "This is the color picker. With it you can set the color your font will print in."));
        boxx.tutorialMap
                .add(new TutorialData(get("picker"), "right", "Each color slider can go from 0 to 255. To change the value:            -You can use the arrows on either side of the sliders.            -You can click and drag on a slider. -You can right click a slider and enter a value manualy."));
        boxx.tutorialMap.add(new TutorialData(picker.get("r"), "right", "This is the red color slider."));
        boxx.tutorialMap.add(new TutorialData(picker.get("g"), "right", "This is the green color slider."));
        boxx.tutorialMap.add(new TutorialData(picker.get("b"), "right", "This is the blue color slider."));
        boxx.tutorialMap.add(new TutorialData(picker.get("a"), "right", "This is the alpha slider. How transparent the color is."));
        boxx.tutorialMap.add(new TutorialData(picker.get("s"), "right", "This is the shader slider. It allows you to easly change how dark or light a color is."));
        boxx.tutorialMap
                .add(new TutorialData(picker.get("more"), "right", "Click on this to open the color palette. It allows you to save your currently selected color for later use."));
        boxx.tutorialMap
                .add(new TutorialData(get("imgWidth"), "left", "This is the width text field. It will display how many tiles the structure's width will be. You can also change the value."));
        boxx.tutorialMap
                .add(new TutorialData(get("imgHeight"), "left", "This is the height text field. It will display how many tiles the structure's height will be. You can also change the value."));
        boxx.tutorialMap
                .add(new TutorialData(get("renderer"), "left", "This is the preview viewer. Here you can view what the structure will look like before placing it into the world."));
        boxx.tutorialMap.add(new TutorialData(get("refresh"), "left", "This is the refresh button. Click on this to update the preview in the preview viewer."));
        boxx.tutorialMap.add(new TutorialData(get("Print"), "left", "This is the print button. Click on this to print your structure."));
        controls.add(boxx);
        moveControlToTop(get("ih"));
        openedGui();
    }
    
    @CustomEventSubscribe
    public void onChanged(GuiControlChangedEvent event) {
        //System.out.println(event.source.name);
        GuiComboBox contextBox = (GuiComboBox) get("fontType");
        String font = contextBox.getCaption();
        //System.out.println(font);
        GuiGlyphSelector na = (GuiGlyphSelector) get("glyph");
        na.fontr = font;
        if (event.source.is("italic")) {
            GuiDepressedCheckBox box = (GuiDepressedCheckBox) event.source;
            if (box.value)
                this.textAttributeMap.put(TextAttribute.POSTURE, TextAttribute.POSTURE_OBLIQUE);
            else
                this.textAttributeMap.remove(TextAttribute.POSTURE);
        } else if (event.source.is("bold")) {
            GuiDepressedCheckBox box = (GuiDepressedCheckBox) event.source;
            if (box.value)
                this.textAttributeMap.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
            else
                this.textAttributeMap.remove(TextAttribute.WEIGHT);
        } else if (event.source.is("underline")) {
            GuiDepressedCheckBox box = (GuiDepressedCheckBox) event.source;
            if (box.value)
                this.textAttributeMap.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
            else
                this.textAttributeMap.remove(TextAttribute.UNDERLINE);
        } else if (event.source.is("strikethrough")) {
            GuiDepressedCheckBox box = (GuiDepressedCheckBox) event.source;
            if (box.value)
                this.textAttributeMap.put(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
            else
                this.textAttributeMap.remove(TextAttribute.STRIKETHROUGH);
        }
        if (event.source.is("searchBar")) {
            refreshControls();
        } else if (event.source.is("input") || event.source instanceof GuiDepressedCheckBox) {
            GuiLongTextField text = (GuiLongTextField) get("input");
            GuiComboBox fontCombo = (GuiComboBox) get("fontType");
            GuiTextfield fontSize = (GuiTextfield) get("fontSize");
            GuiColorPickerAlet color = (GuiColorPickerAlet) get("picker");
            BufferedImage image = FontReader
                    .fontToPhoto(text.text, fontCombo.getCaption(), textAttributeMap, Integer.parseInt(fontSize.text), ColorUtils.RGBAToInt(color.color), BLACK);
            imgHeight.text = FontReader.getTextPixelHeight(text.text, fontCombo.getCaption(), textAttributeMap, Integer.parseInt(fontSize.text)) + "";
            imgWidth.text = FontReader.getTextPixelWidth(text.text, fontCombo.getCaption(), textAttributeMap, Integer.parseInt(fontSize.text)) + "";
        }
        
    }
}
