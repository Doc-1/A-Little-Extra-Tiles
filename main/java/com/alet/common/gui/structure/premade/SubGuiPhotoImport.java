package com.alet.common.gui.structure.premade;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.util.ArrayList;
import java.util.List;

import com.alet.ALET;
import com.alet.common.gui.LittleItemSelector;
import com.alet.common.gui.controls.GuiAnimationViewerAlet;
import com.alet.common.gui.controls.GuiDepressedCheckBox;
import com.alet.common.gui.controls.GuiLongTextField;
import com.alet.common.gui.controls.GuiStack;
import com.alet.common.gui.messages.SubGuiErrorMessage;
import com.alet.common.gui.messages.SubGuiNoPathMessage;
import com.alet.common.utils.CopyUtils;
import com.alet.common.utils.GuiLayerUtils;
import com.alet.common.utils.photo.PhotoReader;
import com.creativemd.creativecore.common.gui.client.style.ColoredDisplayStyle;
import com.creativemd.creativecore.common.gui.client.style.DisplayStyle;
import com.creativemd.creativecore.common.gui.client.style.Style;
import com.creativemd.creativecore.common.gui.container.SubGui;
import com.creativemd.creativecore.common.gui.controls.gui.GuiAnalogeSlider;
import com.creativemd.creativecore.common.gui.controls.gui.GuiButton;
import com.creativemd.creativecore.common.gui.controls.gui.GuiCheckBox;
import com.creativemd.creativecore.common.gui.controls.gui.GuiComboBox;
import com.creativemd.creativecore.common.gui.controls.gui.GuiComboBoxExtension;
import com.creativemd.creativecore.common.gui.controls.gui.GuiProgressBar;
import com.creativemd.creativecore.common.gui.controls.gui.GuiTextfield;
import com.creativemd.creativecore.common.gui.event.gui.GuiControlChangedEvent;
import com.creativemd.littletiles.client.gui.LittleSubGuiUtils;
import com.creativemd.littletiles.common.entity.AnimationPreview;
import com.creativemd.littletiles.common.item.ItemMultiTiles;
import com.creativemd.littletiles.common.tile.preview.LittlePreview;
import com.creativemd.littletiles.common.tile.preview.LittlePreviews;
import com.creativemd.littletiles.common.util.grid.LittleGridContext;
import com.n247s.api.eventapi.eventsystem.CustomEventSubscribe;

import net.minecraft.item.ItemStack;

public class SubGuiPhotoImport extends SubGui {
    
    private volatile Thread thread;
    public double aspectRatio = 0;
    
    public SubGuiPhotoImport() {
        super(313, 200);
    }
    
    @Override
    public void createControls() {
        GuiAnimationViewerAlet viewer = new GuiAnimationViewerAlet("renderer", 171, 0, 136, 135);
        controls.add(viewer);
        viewer.moveViewPort(0, 60);
        List<String> imageSources = new ArrayList<String>();
        imageSources.add("Print From File");
        if (ALET.CONFIG.isAllowURL())
            imageSources.add("Print From URL");
        imageSources.add("Print From Block");
        imageSources.add("Print From Item");
        controls.add(new GuiComboBox("imageSource", 50, 0, 90, imageSources));
        createPathControl();
        controls.add(new GuiButton("refresh", "Refresh Preview", 217, 142, 90) {
            
            @Override
            public void onClicked(int x, int y, int button) {
                GuiTextfield imageWidth = (GuiTextfield) get("imageWidth");
                GuiTextfield imageHeight = (GuiTextfield) get("imageHeight");
                GuiComboBox imageSource = (GuiComboBox) get("imageSource");
                int resizeX = Integer.parseInt(imageWidth.text);
                int resizeY = Integer.parseInt(imageHeight.text);
                
                if (resizeX * resizeY > ALET.CONFIG.getMaxPixelAmount()) {
                    GuiLayerUtils.addLayer(getGui(), new SubGuiErrorMessage(resizeX * resizeY));
                } else {
                    PhotoReader.setScale(resizeX, resizeY);
                    
                    GuiComboBox contextBox = (GuiComboBox) get("grid");
                    int grid = Integer.parseInt(contextBox.getCaption());
                    
                    GuiDepressedCheckBox ignoreAlpha = (GuiDepressedCheckBox) get("ignoreAlpha");
                    GuiAnalogeSlider slider = (GuiAnalogeSlider) get("color_acc");
                    String path = getPath();
                    Runnable run = (new Runnable() {
                        @Override
                        public void run() {
                            LittlePreviews pre = LittlePreview.getPreview(PhotoReader.photoToStack(path, ignoreAlpha.value,
                                imageSource.getCaption(), grid, getGui(), slider.value));
                            viewer.onLoaded(new AnimationPreview(pre));
                            if (pre == null)
                                GuiLayerUtils.addLayer(getGui(), new SubGuiNoPathMessage(".png or .jpeg"));
                        }
                        
                    });
                    if (thread != null) {
                        thread.interrupt();
                        thread = null;
                    }
                    thread = new Thread(run);
                    thread.start();
                }
                
            }
        });
        
        controls.add(new GuiTextfield("imageWidth", "0", 240, 163, 30, 14) {
            @Override
            public boolean onKeyPressed(char character, int key) {
                GuiTextfield imageWidth = (GuiTextfield) get("imageWidth");
                GuiTextfield imageHeight = (GuiTextfield) get("imageHeight");
                GuiCheckBox keepAspect = (GuiCheckBox) get("keepAspect");
                boolean result = super.onKeyPressed(character, key);
                if (result && !this.text.isEmpty() && keepAspect.value) {
                    imageHeight.text = String.valueOf((int) (aspectRatio * Double.parseDouble(imageWidth.text)));
                }
                return result;
            }
        });
        GuiTextfield imageWidth = (GuiTextfield) get("imageWidth");
        imageWidth.enabled = false;
        imageWidth.setCustomTooltip("Width Of Image");
        imageWidth.setNumbersOnly();
        
        controls.add(new GuiTextfield("imageHeight", "0", 277, 163, 30, 14) {
            
            @Override
            public boolean onKeyPressed(char character, int key) {
                GuiTextfield imageWidth = (GuiTextfield) get("imageWidth");
                GuiTextfield imageHeight = (GuiTextfield) get("imageHeight");
                GuiCheckBox keepAspect = (GuiCheckBox) get("keepAspect");
                boolean result = super.onKeyPressed(character, key);
                if (result && !imageHeight.text.isEmpty() && keepAspect.value) {
                    imageWidth.text = String.valueOf((int) (Double.parseDouble(imageHeight.text) / aspectRatio));
                }
                return result;
            }
        });
        GuiTextfield imageHeight = (GuiTextfield) get("imageHeight");
        imageHeight.enabled = false;
        imageHeight.setCustomTooltip("Height Of Image");
        imageHeight.setNumbersOnly();
        
        controls.add(new GuiDepressedCheckBox("ignoreAlpha", "Ignore Alpha", 0, 47, 70, 16, "", false, false));
        controls.add(new GuiDepressedCheckBox("keepAspect", translate(
            "Keep Aspect Ratio?"), 0, 63, 101, 16, "", false, false));
        
        controls.add(new GuiButton("autoScale", "Auto Scale Image?", 0, 80, 90) {
            @Override
            public void onClicked(int x, int y, int button) {
                GuiTextfield imageWidth = (GuiTextfield) get("imageWidth");
                GuiTextfield imageHeight = (GuiTextfield) get("imageHeight");
                
                int x1 = (int) (ALET.CONFIG.getMaxPixelAmount() * aspectRatio);
                int x2 = (int) Math.sqrt(x1);
                int y1 = (int) (x2 / aspectRatio);
                
                int xa = Integer.parseInt(imageHeight.text);
                int ya = Integer.parseInt(imageWidth.text);
                if (xa * ya > x1) {
                    if (Integer.parseInt(imageWidth.text) > Integer.parseInt(imageHeight.text)) {
                        imageHeight.text = String.valueOf(x2);
                        imageWidth.text = String.valueOf(y1);
                    } else if (Integer.parseInt(imageWidth.text) < Integer.parseInt(imageHeight.text)) {
                        imageWidth.text = String.valueOf(y1);
                        imageHeight.text = String.valueOf(x2);
                    } else {
                        imageHeight.text = String.valueOf(x2);
                        imageWidth.text = String.valueOf(x2);
                    }
                }
                
            }
        });
        GuiButton autoScale = (GuiButton) get("autoScale");
        autoScale.enabled = false;
        autoScale.setCustomTooltip("Shrinks Image down to max image size.");
        
        GuiComboBox contextBox = (new GuiComboBox("grid", 148, 0, 15, LittleGridContext.getNames()) {
            
            @Override
            protected GuiComboBoxExtension createBox() {
                return new GuiComboBoxExtension(name + "extension", this, posX, posY + height, 35 - getContentOffset() * 2, 100, lines);
            }
        });
        contextBox.select(ItemMultiTiles.currentContext.size + "");
        controls.add(contextBox);
        
        GuiButton paste = (new GuiButton("Paste", 0, 102) {
            @Override
            public void onClicked(int x, int y, int button) {
                GuiLongTextField file = (GuiLongTextField) get("file");
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                String path = CopyUtils.getCopiedFilePath(clipboard);
                if (path == null)
                    return;
                file.text = path;
                
                updatePhotoData();
            }
        });
        controls.add(paste);
        
        GuiButton print = (new GuiButton("Print", 38, 102) {
            
            @Override
            public void onClicked(int x, int y, int button) {
                GuiComboBox imageSource = (GuiComboBox) get("imageSource");
                int resizeX = Integer.parseInt(imageWidth.text);
                int resizeY = Integer.parseInt(imageHeight.text);
                String path = getPath();
                if (PhotoReader.photoExists(path, imageSource.getCaption()))
                    if (resizeX * resizeY > ALET.CONFIG.getMaxPixelAmount()) {
                        GuiLayerUtils.addLayer(getGui(), new SubGuiErrorMessage(resizeX * resizeY));
                    } else {
                        PhotoReader.setScale(resizeX, resizeY);
                        GuiDepressedCheckBox ignoreAlpha = (GuiDepressedCheckBox) get("ignoreAlpha");
                        GuiAnalogeSlider slider = (GuiAnalogeSlider) get("color_acc");
                        GuiComboBox contextBox = (GuiComboBox) get("grid");
                        int grid = Integer.parseInt(contextBox.getCaption());
                        Runnable run = new Runnable() {
                            @Override
                            public void run() {
                                ItemStack stack = PhotoReader.photoToStack(path, ignoreAlpha.value, imageSource.getCaption(),
                                    grid, getGui(), slider.value);
                                if (!stack.equals(ItemStack.EMPTY))
                                    getGui().sendPacketToServer(stack.getTagCompound());
                                
                            }
                        };
                        if (thread != null) {
                            thread.interrupt();
                            thread = null;
                        }
                        thread = new Thread(run);
                        thread.start();
                    }
                else
                    GuiLayerUtils.addLayer(getGui(), new SubGuiNoPathMessage(".png or .jpeg"));
            }
        });
        controls.add(print);
        
        GuiProgressBar progress = new GuiProgressBar("progress", 165, 184, 142, 10, 150, 0);
        progress.setStyle(
            new Style("s", new ColoredDisplayStyle(0x11111111), new ColoredDisplayStyle(0xdddddddd), DisplayStyle.emptyDisplay, new ColoredDisplayStyle(0x2d9912), DisplayStyle.emptyDisplay));
        controls.add(progress);
        
        GuiAnalogeSlider slider = new GuiAnalogeSlider("color_acc", 165, 163, 68, 14, 100, 0, 1);
        slider.setCustomTooltip("Color Accuracy");
        controls.add(slider);
    }
    
    @CustomEventSubscribe
    public void changed(GuiControlChangedEvent event) {
        if (event.source.name.equals("imageSource")) {
            wipeControls();
            GuiComboBox imageSource = (GuiComboBox) get("imageSource");
            if (imageSource.getCaption().equals("Print From File") || imageSource.getCaption().equals("Print From URL")) {
                GuiButton paste = (GuiButton) get("Paste");
                paste.setEnabled(true);
                createPathControl();
            } else if (imageSource.getCaption().equals("Print From Block")) {
                GuiButton paste = (GuiButton) get("Paste");
                paste.setEnabled(false);
                createBlockControl();
                GuiStack listBlock = (GuiStack) get("listBlock");
                listBlock.getTexture(listBlock.getSelected());
                updatePhotoData();
            } else if (imageSource.getCaption().equals("Print From Item")) {
                GuiButton paste = (GuiButton) get("Paste");
                paste.setEnabled(false);
                createItemControl();
                GuiStack listItem = (GuiStack) get("listItem");
                listItem.getTexture(listItem.getSelected());
                updatePhotoData();
            }
        } else if (event.source.name.equals("listItem")) {
            GuiStack listItem = (GuiStack) get("listItem");
            listItem.getTexture(listItem.getSelected());
            updatePhotoData();
        } else if (event.source.name.equals("listBlock")) {
            GuiStack listBlock = (GuiStack) get("listBlock");
            listBlock.getTexture(listBlock.getSelected());
            updatePhotoData();
        }
        if (event.source instanceof GuiDepressedCheckBox) {
            GuiDepressedCheckBox north = (GuiDepressedCheckBox) get("north");
            GuiDepressedCheckBox east = (GuiDepressedCheckBox) get("east");
            GuiDepressedCheckBox south = (GuiDepressedCheckBox) get("south");
            GuiDepressedCheckBox west = (GuiDepressedCheckBox) get("west");
            GuiDepressedCheckBox top = (GuiDepressedCheckBox) get("top");
            GuiDepressedCheckBox bottom = (GuiDepressedCheckBox) get("bottom");
            GuiStack listBlock = (GuiStack) get("listBlock");
            if (event.source.name.equals("north")) {
                listBlock.setEnumFacing("north");
                north.value = true;
                east.value = false;
                south.value = false;
                west.value = false;
                top.value = false;
                bottom.value = false;
            }
            if (event.source.name.equals("east")) {
                listBlock.setEnumFacing("east");
                north.value = false;
                east.value = true;
                south.value = false;
                west.value = false;
                top.value = false;
                bottom.value = false;
            }
            if (event.source.name.equals("south")) {
                listBlock.setEnumFacing("south");
                north.value = false;
                east.value = false;
                south.value = true;
                west.value = false;
                top.value = false;
                bottom.value = false;
            }
            if (event.source.name.equals("west")) {
                listBlock.setEnumFacing("west");
                north.value = false;
                east.value = false;
                south.value = false;
                west.value = true;
                top.value = false;
                bottom.value = false;
            }
            if (event.source.name.equals("top")) {
                listBlock.setEnumFacing("up");
                north.value = false;
                east.value = false;
                south.value = false;
                west.value = false;
                top.value = true;
                bottom.value = false;
            }
            if (event.source.name.equals("bottom")) {
                listBlock.setEnumFacing("down");
                north.value = false;
                east.value = false;
                south.value = false;
                west.value = false;
                top.value = false;
                bottom.value = true;
            }
        }
    }
    
    public void wipeControls() {
        if (has("file"))
            removeControl(get("file"));
        if (has("north"))
            removeControl(get("north"));
        if (has("east"))
            removeControl(get("east"));
        if (has("south"))
            removeControl(get("south"));
        if (has("west"))
            removeControl(get("west"));
        if (has("bottom"))
            removeControl(get("bottom"));
        if (has("top"))
            removeControl(get("top"));
        if (has("listItem"))
            removeControl(get("listItem"));
        if (has("listBlock"))
            removeControl(get("listBlock"));
    }
    
    public void createPathControl() {
        addControl(new GuiLongTextField("file", "", 0, 26, 162, 14) {
            @Override
            public boolean onKeyPressed(char character, int key) {
                boolean result = super.onKeyPressed(character, key);
                if (result)
                    updatePhotoData();
                return result;
            }
        });
    }
    
    public void createBlockControl() {
        addControl(new GuiStack("listBlock", 0, 26, 140, getPlayer(), LittleSubGuiUtils.getCollector(
            getPlayer()), true, true));
        addControl(new GuiDepressedCheckBox("north", "N", 119, 67, 17, 17, "", true));
        addControl(new GuiDepressedCheckBox("east", "E", 136, 83, 17, 17, "", false));
        addControl(new GuiDepressedCheckBox("south", "S", 119, 99, 17, 17, "", false));
        addControl(new GuiDepressedCheckBox("west", "W", 102, 83, 17, 17, "", false));
        addControl(new GuiDepressedCheckBox("bottom", "B", 119, 83, 17, 17, "", false));
        addControl(new GuiDepressedCheckBox("top", "T", 153, 83, 17, 17, "", false));
    }
    
    public void createItemControl() {
        addControl(new GuiStack("listItem", 0, 26, 140, getPlayer(), LittleItemSelector.getCollector(
            getPlayer()), true, false));
    }
    
    public String getPath() {
        GuiComboBox imageSource = (GuiComboBox) get("imageSource");
        GuiLongTextField file = (GuiLongTextField) get("file");
        String path = "";
        if (imageSource.getCaption().equals("Print From File") || imageSource.getCaption().equals("Print From URL"))
            path = file.text;
        else if (imageSource.getCaption().equals("Print From Item")) {
            GuiStack listItem = (GuiStack) get("listItem");
            String[] pathArray = listItem.texture.split("'");
            pathArray = pathArray[1].split(":");
            path = "assets/" + pathArray[0] + "/textures/" + pathArray[1] + ".png";
        } else if (imageSource.getCaption().equals("Print From Block")) {
            GuiStack listBlock = (GuiStack) get("listBlock");
            String[] pathArray = listBlock.texture.split("'");
            pathArray = pathArray[1].split(":");
            path = "assets/" + pathArray[0] + "/textures/" + pathArray[1] + ".png";
        }
        return path;
    }
    
    public void updatePhotoData() {
        GuiTextfield imageWidth = (GuiTextfield) get("imageWidth");
        GuiTextfield imageHeight = (GuiTextfield) get("imageHeight");
        GuiButton autoScale = (GuiButton) get("autoScale");
        GuiComboBox imageSource = (GuiComboBox) get("imageSource");
        if (PhotoReader.photoExists(getPath(), imageSource.getCaption())) {
            String path = getPath();
            imageHeight.text = Integer.toString(PhotoReader.getPixelLength(path, imageSource.getCaption()));
            imageWidth.text = Integer.toString(PhotoReader.getPixelWidth(path, imageSource.getCaption()));
            imageHeight.enabled = true;
            imageWidth.enabled = true;
            autoScale.enabled = true;
            aspectRatio = Float.parseFloat(imageHeight.text) / Float.parseFloat(imageWidth.text);
        } else {
            imageHeight.text = "0";
            imageWidth.text = "0";
            imageHeight.enabled = false;
            imageWidth.enabled = false;
            autoScale.enabled = false;
        }
    }
    
    @Override
    public void onClosed() {
        if (thread != null) {
            thread.interrupt();
            thread = null;
        }
        super.onClosed();
    }
}
