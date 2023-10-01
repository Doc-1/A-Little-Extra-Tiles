package com.alet.client.gui;

import java.util.ArrayList;
import java.util.List;

import com.alet.client.sounds.Notes;
import com.alet.common.structure.type.LittleMusicComposerALET.LittleMusicComposerParserALET;
import com.creativemd.creativecore.common.gui.container.SubGui;
import com.creativemd.creativecore.common.gui.controls.gui.GuiButton;
import com.creativemd.creativecore.common.gui.controls.gui.GuiCheckBox;
import com.creativemd.creativecore.common.gui.controls.gui.GuiLabel;
import com.creativemd.creativecore.common.gui.controls.gui.GuiListBox;
import com.creativemd.creativecore.common.gui.controls.gui.GuiTextfield;
import com.creativemd.creativecore.common.gui.event.ControlEvent;
import com.creativemd.creativecore.common.gui.event.gui.GuiControlChangedEvent;
import com.creativemd.creativecore.common.utils.mc.ColorUtils;

import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.Vec3d;

public class SubGuiSoundSettings extends SubGui {
    
    public LittleMusicComposerParserALET littleSoundPlayerParserALET;
    public static List<String> soundList = new ArrayList<String>();
    static {
        soundList.add("nosound");
        soundList.add("banjo");
        soundList.add("bdrum");
        soundList.add("bell");
        soundList.add("bit");
        soundList.add("click");
        soundList.add("cow_bell");
        soundList.add("dbass");
        soundList.add("didgeridoo");
        soundList.add("DonK4rmas_Piano");
        soundList.add("flute");
        soundList.add("guitar");
        soundList.add("harp");
        soundList.add("icechime");
        soundList.add("iron_xylophone");
        soundList.add("pling");
        soundList.add("sdrum");
        soundList.add("xylobone");
    }
    
    public SubGuiSoundSettings(LittleMusicComposerParserALET littleSoundPlayerParserALET) {
        super(350, 224);
        this.littleSoundPlayerParserALET = littleSoundPlayerParserALET;
    }
    
    @Override
    public void createControls() {
        
        controls.add(new GuiButton("Done", 0, 180, 40) {
            @Override
            public void onClicked(int x, int y, int button) {
                closeGui();
            }
        });
        
        String[] channelsounds = littleSoundPlayerParserALET.channelSounds;
        int i = 0;
        for (String sound : channelsounds) {
            i++;
            GuiComboBoxHeight sounds = new GuiComboBoxHeight("sounds" + i, 255, (i - 1) * 14, 85, 7, soundList);
            sounds.select(sound);
            controls.add(sounds);
            
            GuiTextfield search = new GuiTextfieldSearch("search", "", 167, (i - 1) * 14, 80, 7, "sounds" + i);
            controls.add(search);
            
            controls.add(new GuiLabel("CH" + i + ":", 140, (i - 1) * 14));
        }
        
        controls.add(new GuiLabel("Volume: ", 0, 0));
        controls.add(new GuiTextfield("Volume", littleSoundPlayerParserALET.volume + "", 40, 0, 40, 9).setFloatOnly());
        controls.add(new GuiCheckBox("local", "Play From Block", 0, 18, littleSoundPlayerParserALET.local).setCustomTooltip(
            "True: Play sound from the Structure", "False: Play globally to all players"));
    }
    
    @Override
    public void onClosed() {
        super.onClosed();
    }
    
    @Override
    public boolean raiseEvent(ControlEvent event) {
        if (event.source instanceof GuiListBox) {
            GuiListBox box = (GuiListBox) event.source;
            String sound = box.get(box.selected);
            if (!sound.equals("nosound")) {
                Notes note = Notes.getNoteFromPitch(0);
                playSound(new SoundEvent(new ResourceLocation(note.getResourceLocation(sound))));
                
                GuiComboBoxHeight[] comboBoxes = new GuiComboBoxHeight[16];
                for (int i = 0; i < 16; i++)
                    comboBoxes[i] = (GuiComboBoxHeight) get("sounds" + (i + 1));
                
                GuiTextfield volume = (GuiTextfield) get("volume");
                GuiCheckBox local = (GuiCheckBox) get("local");
                
                for (int i = 0; i < 16; i++)
                    littleSoundPlayerParserALET.channelSounds[i] = comboBoxes[i].getCaption();
                
                littleSoundPlayerParserALET.volume = Integer.parseInt(volume.text);
                littleSoundPlayerParserALET.local = local.value;
                littleSoundPlayerParserALET.updateTimeLine();
            }
        }
        return super.raiseEvent(event);
    }
    
    private class GuiComboBoxHeight extends GuiLabel {
        
        public GuiBoxExtension extension;
        public List<String> lines;
        
        public int index;
        
        public GuiComboBoxHeight(String name, int x, int y, int width, int height, List<String> lines) {
            super(name, x, y, width, height, ColorUtils.WHITE);
            this.lines = lines;
            
            if (lines.size() > 0) {
                this.caption = getDisplay(0);
                this.index = 0;
            } else {
                this.caption = "";
                this.index = -1;
            }
        }
        
        public String getDisplay(int index) {
            return lines.get(index);
        }
        
        @Override
        public void setCaption(String caption) {
            this.caption = caption;
        }
        
        @SuppressWarnings("unused")
        public boolean select(int index) {
            if (index >= 0 && index < lines.size()) {
                caption = getDisplay(index);
                this.index = index;
                raiseEvent(new GuiControlChangedEvent(this));
                return true;
            }
            return false;
        }
        
        public boolean select(String line) {
            
            index = lines.indexOf(line);
            if (index != -1) {
                caption = lines.get(index);
                raiseEvent(new GuiControlChangedEvent(this));
                return true;
            }
            return false;
        }
        
        @Override
        public boolean hasBorder() {
            return true;
        }
        
        @Override
        public boolean hasBackground() {
            return true;
        }
        
        @Override
        public boolean mousePressed(int posX, int posY, int button) {
            if (extension == null)
                openBox();
            else
                closeBox();
            playSound(SoundEvents.UI_BUTTON_CLICK);
            return true;
        }
        
        public void openBox() {
            this.extension = createBox();
            getGui().controls.add(extension);
            
            extension.parent = getGui();
            extension.moveControlToTop();
            extension.onOpened();
            getGui().refreshControls();
            extension.rotation = rotation;
            extension.posX = getPixelOffsetX() - getGui().getPixelOffsetX() - getContentOffset();
            extension.posY = getPixelOffsetY() - getGui().getPixelOffsetY() - getContentOffset() + height;
            
            if (extension.posY + extension.height > getParent().height && this.posY >= extension.height)
                extension.posY -= this.height + extension.height;
        }
        
        protected GuiBoxExtension createBox() {
            return new GuiBoxExtension(name + "extension", this, posX, posY + height, width - getContentOffset() * 2, 100, lines);
        }
        
        public void closeBox() {
            if (extension != null) {
                getGui().controls.remove(extension);
                extension = null;
            }
        }
    }
    
    private class GuiBoxExtension extends GuiListBox {
        
        public GuiComboBoxHeight comboBox;
        
        public GuiBoxExtension(String name, GuiComboBoxHeight comboBox, int x, int y, int width, int height, List<String> lines) {
            super(name, x, y, width, height, lines);
            this.comboBox = comboBox;
            this.selected = comboBox.index;
            reloadControls();
        }
        
        @Override
        public Vec3d getCenterOffset() {
            return new Vec3d(width / 2, -comboBox.height / 2, 0);
        }
        
        @Override
        public void onLoseFocus() {
            if (!comboBox.isMouseOver() && !isMouseOver())
                comboBox.closeBox();
        }
        
        @Override
        public void onSelectionChange() {
            if (selected != -1 && selected < lines.size()) {
                comboBox.setCaption(comboBox.getDisplay(selected));
                comboBox.index = selected;
                comboBox.raiseEvent(new GuiControlChangedEvent(comboBox));
            }
            comboBox.closeBox();
        }
        
        @Override
        public boolean canOverlap() {
            return true;
        }
        
    }
    
    private class GuiTextfieldSearch extends GuiTextfield {
        String comboBox;
        
        public GuiTextfieldSearch(String name, String text, int x, int y, int width, int height, String linkToComboBox) {
            super(name, text, x, y, width, height);
            this.comboBox = linkToComboBox;
        }
        
        @Override
        public boolean onKeyPressed(char character, int key) {
            boolean result = false;
            if (this.focused) {
                result = super.onKeyPressed(character, key);
                GuiComboBoxHeight sounds = (GuiComboBoxHeight) getParent().get(comboBox);
                
                List<String> foundSounds = new ArrayList<>();
                for (int i = 0; i < soundList.size(); i++) {
                    if (soundList.get(i).toLowerCase().contains(text.toLowerCase()))
                        foundSounds.add(soundList.get(i));
                }
                if (!foundSounds.isEmpty()) {
                    sounds.lines = foundSounds;
                    int index = soundList.indexOf(foundSounds.get(0));
                    sounds.select(soundList.get(index));
                }
            }
            return result;
        }
    }
    
}
