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
		List<String> soundList = new ArrayList<String>();
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
		
		//Yes, yes I know. I could have done this with a for loop. Realized this after finished.
		GuiComboBoxHeight sounds1 = new GuiComboBoxHeight("sounds1", 255, 0, 85, 7, soundList);
		sounds1.select(littleSoundPlayerParserALET.CHSound1);
		controls.add(sounds1);
		
		GuiComboBoxHeight sounds2 = new GuiComboBoxHeight("sounds2", 255, 14, 85, 7, soundList);
		sounds2.select(littleSoundPlayerParserALET.CHSound2);
		controls.add(sounds2);
		
		GuiComboBoxHeight sounds3 = new GuiComboBoxHeight("sounds3", 255, 28, 85, 7, soundList);
		sounds3.select(littleSoundPlayerParserALET.CHSound3);
		controls.add(sounds3);
		
		GuiComboBoxHeight sounds4 = new GuiComboBoxHeight("sounds4", 255, 42, 85, 7, soundList);
		sounds4.select(littleSoundPlayerParserALET.CHSound4);
		controls.add(sounds4);
		
		GuiComboBoxHeight sounds5 = new GuiComboBoxHeight("sounds5", 255, 56, 85, 7, soundList);
		sounds5.select(littleSoundPlayerParserALET.CHSound5);
		controls.add(sounds5);
		
		GuiComboBoxHeight sounds6 = new GuiComboBoxHeight("sounds6", 255, 70, 85, 7, soundList);
		sounds6.select(littleSoundPlayerParserALET.CHSound6);
		controls.add(sounds6);
		
		GuiComboBoxHeight sounds7 = new GuiComboBoxHeight("sounds7", 255, 84, 85, 7, soundList);
		sounds7.select(littleSoundPlayerParserALET.CHSound7);
		controls.add(sounds7);
		
		GuiComboBoxHeight sounds8 = new GuiComboBoxHeight("sounds8", 255, 98, 85, 7, soundList);
		sounds8.select(littleSoundPlayerParserALET.CHSound8);
		controls.add(sounds8);
		
		GuiComboBoxHeight sounds9 = new GuiComboBoxHeight("sounds9", 255, 112, 85, 7, soundList);
		sounds9.select(littleSoundPlayerParserALET.CHSound9);
		controls.add(sounds9);
		
		GuiComboBoxHeight sounds10 = new GuiComboBoxHeight("sounds10", 255, 126, 85, 7, soundList);
		sounds10.select(littleSoundPlayerParserALET.CHSound10);
		controls.add(sounds10);
		
		GuiComboBoxHeight sounds11 = new GuiComboBoxHeight("sounds11", 255, 140, 85, 7, soundList);
		sounds11.select(littleSoundPlayerParserALET.CHSound11);
		controls.add(sounds11);
		
		GuiComboBoxHeight sounds12 = new GuiComboBoxHeight("sounds12", 255, 154, 85, 7, soundList);
		sounds12.select(littleSoundPlayerParserALET.CHSound12);
		controls.add(sounds12);
		
		GuiComboBoxHeight sounds13 = new GuiComboBoxHeight("sounds13", 255, 168, 85, 7, soundList);
		sounds13.select(littleSoundPlayerParserALET.CHSound13);
		controls.add(sounds13);
		
		GuiComboBoxHeight sounds14 = new GuiComboBoxHeight("sounds14", 255, 182, 85, 7, soundList);
		sounds14.select(littleSoundPlayerParserALET.CHSound14);
		controls.add(sounds14);
		
		GuiComboBoxHeight sounds15 = new GuiComboBoxHeight("sounds15", 255, 196, 85, 7, soundList);
		sounds15.select(littleSoundPlayerParserALET.CHSound15);
		controls.add(sounds15);
		
		GuiComboBoxHeight sounds16 = new GuiComboBoxHeight("sounds16", 255, 210, 85, 7, soundList);
		sounds16.select(littleSoundPlayerParserALET.CHSound16);
		controls.add(sounds16);
		
		GuiTextfield search1 = new GuiTextfieldSearch("search", "", 167, 0, 80, 7, "sounds1");
		controls.add(search1);
		
		GuiTextfield search2 = new GuiTextfieldSearch("search", "", 167, 14, 80, 7, "sounds2");
		controls.add(search2);
		
		GuiTextfield search3 = new GuiTextfieldSearch("search", "", 167, 28, 80, 7, "sounds3");
		controls.add(search3);
		
		GuiTextfield search4 = new GuiTextfieldSearch("search", "", 167, 42, 80, 7, "sounds4");
		controls.add(search4);
		
		GuiTextfield search5 = new GuiTextfieldSearch("search", "", 167, 56, 80, 7, "sounds5");
		controls.add(search5);
		
		GuiTextfield search6 = new GuiTextfieldSearch("search", "", 167, 70, 80, 7, "sounds6");
		controls.add(search6);
		
		GuiTextfield search7 = new GuiTextfieldSearch("search", "", 167, 84, 80, 7, "sounds7");
		controls.add(search7);
		
		GuiTextfield search8 = new GuiTextfieldSearch("search", "", 167, 98, 80, 7, "sounds8");
		controls.add(search8);
		
		GuiTextfield search9 = new GuiTextfieldSearch("search", "", 167, 112, 80, 7, "sounds9");
		controls.add(search9);
		
		GuiTextfield search10 = new GuiTextfieldSearch("search", "", 167, 126, 80, 7, "search10");
		controls.add(search10);
		
		GuiTextfield search11 = new GuiTextfieldSearch("search", "", 167, 140, 80, 7, "search11");
		controls.add(search11);
		
		GuiTextfield search12 = new GuiTextfieldSearch("search", "", 167, 154, 80, 7, "search12");
		controls.add(search12);
		
		GuiTextfield search13 = new GuiTextfieldSearch("search", "", 167, 168, 80, 7, "search13");
		controls.add(search13);
		
		GuiTextfield search14 = new GuiTextfieldSearch("search", "", 167, 182, 80, 7, "search14");
		controls.add(search14);
		
		GuiTextfield search15 = new GuiTextfieldSearch("search", "", 167, 196, 80, 7, "search15");
		controls.add(search15);
		
		GuiTextfield search16 = new GuiTextfieldSearch("search", "", 167, 210, 80, 7, "search16");
		controls.add(search16);
		
		controls.add(new GuiLabel("CH1:", 140, 0));
		controls.add(new GuiLabel("CH2:", 140, 14));
		controls.add(new GuiLabel("CH3:", 140, 28));
		controls.add(new GuiLabel("CH4:", 140, 42));
		controls.add(new GuiLabel("CH5:", 140, 56));
		controls.add(new GuiLabel("CH6:", 140, 70));
		controls.add(new GuiLabel("CH7:", 140, 84));
		controls.add(new GuiLabel("CH8:", 140, 98));
		controls.add(new GuiLabel("CH9:", 140, 112));
		controls.add(new GuiLabel("CH10:", 134, 126));
		controls.add(new GuiLabel("CH11:", 134, 140));
		controls.add(new GuiLabel("CH12:", 134, 154));
		controls.add(new GuiLabel("CH13:", 134, 168));
		controls.add(new GuiLabel("CH14:", 134, 182));
		controls.add(new GuiLabel("CH15:", 134, 196));
		controls.add(new GuiLabel("CH16:", 134, 210));
		
		controls.add(new GuiLabel("Volume: ", 0, 0));
		controls.add(new GuiTextfield("Volume", littleSoundPlayerParserALET.volume + "", 40, 0, 40, 9).setFloatOnly());
		controls.add(new GuiCheckBox("local", "Play From Block", 0, 18, littleSoundPlayerParserALET.local).setCustomTooltip("True: Play sound from the Structure", "False: Play globally to all players"));
	}
	
	@Override
	public void onClosed() {
		GuiComboBoxHeight CH1 = (GuiComboBoxHeight) get("sounds1");
		GuiComboBoxHeight CH2 = (GuiComboBoxHeight) get("sounds2");
		GuiComboBoxHeight CH3 = (GuiComboBoxHeight) get("sounds3");
		GuiComboBoxHeight CH4 = (GuiComboBoxHeight) get("sounds4");
		GuiComboBoxHeight CH5 = (GuiComboBoxHeight) get("sounds5");
		GuiComboBoxHeight CH6 = (GuiComboBoxHeight) get("sounds6");
		GuiComboBoxHeight CH7 = (GuiComboBoxHeight) get("sounds7");
		GuiComboBoxHeight CH8 = (GuiComboBoxHeight) get("sounds8");
		GuiComboBoxHeight CH9 = (GuiComboBoxHeight) get("sounds9");
		GuiComboBoxHeight CH10 = (GuiComboBoxHeight) get("sounds10");
		GuiComboBoxHeight CH11 = (GuiComboBoxHeight) get("sounds11");
		GuiComboBoxHeight CH12 = (GuiComboBoxHeight) get("sounds12");
		GuiComboBoxHeight CH13 = (GuiComboBoxHeight) get("sounds13");
		GuiComboBoxHeight CH14 = (GuiComboBoxHeight) get("sounds14");
		GuiComboBoxHeight CH15 = (GuiComboBoxHeight) get("sounds15");
		GuiComboBoxHeight CH16 = (GuiComboBoxHeight) get("sounds16");
		
		GuiTextfield volume = (GuiTextfield) get("volume");
		GuiCheckBox local = (GuiCheckBox) get("local");
		
		if (CH1.getCaption() != null)
			littleSoundPlayerParserALET.CHSound1 = CH1.getCaption();
		if (CH2.getCaption() != null)
			littleSoundPlayerParserALET.CHSound2 = CH2.getCaption();
		if (CH3.getCaption() != null)
			littleSoundPlayerParserALET.CHSound3 = CH3.getCaption();
		if (CH4.getCaption() != null)
			littleSoundPlayerParserALET.CHSound4 = CH4.getCaption();
		if (CH5.getCaption() != null)
			littleSoundPlayerParserALET.CHSound5 = CH5.getCaption();
		if (CH6.getCaption() != null)
			littleSoundPlayerParserALET.CHSound6 = CH6.getCaption();
		if (CH7.getCaption() != null)
			littleSoundPlayerParserALET.CHSound7 = CH7.getCaption();
		if (CH8.getCaption() != null)
			littleSoundPlayerParserALET.CHSound8 = CH8.getCaption();
		if (CH9.getCaption() != null)
			littleSoundPlayerParserALET.CHSound9 = CH9.getCaption();
		if (CH10.getCaption() != null)
			littleSoundPlayerParserALET.CHSound10 = CH10.getCaption();
		if (CH11.getCaption() != null)
			littleSoundPlayerParserALET.CHSound11 = CH11.getCaption();
		if (CH12.getCaption() != null)
			littleSoundPlayerParserALET.CHSound12 = CH12.getCaption();
		if (CH13.getCaption() != null)
			littleSoundPlayerParserALET.CHSound13 = CH13.getCaption();
		if (CH14.getCaption() != null)
			littleSoundPlayerParserALET.CHSound14 = CH14.getCaption();
		if (CH15.getCaption() != null)
			littleSoundPlayerParserALET.CHSound15 = CH15.getCaption();
		if (CH16.getCaption() != null)
			littleSoundPlayerParserALET.CHSound16 = CH16.getCaption();
		
		littleSoundPlayerParserALET.volume = Integer.parseInt(volume.text);
		littleSoundPlayerParserALET.local = local.value;
		
		super.onClosed();
	}
	
	@Override
	public boolean raiseEvent(ControlEvent event) {
		if (event.source instanceof GuiListBox) {
			GuiListBox box = (GuiListBox) event.source;
			String sound = box.get(box.selected);
			if (!sound.equals("nosound")) {
				Notes note = Notes.getNoteFromPitch(0);
				this.playSound(new SoundEvent(new ResourceLocation(note.getResourceLocation(sound))));
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
		
		public GuiTextfieldSearch(String name, String text, int x, int y, int width, int height, String comboBox) {
			super(name, text, x, y, width, height);
			this.comboBox = comboBox;
		}
		
		@Override
		public boolean onKeyPressed(char character, int key) {
			boolean result = false;
			if (this.focused) {
				result = super.onKeyPressed(character, key);
				GuiComboBoxHeight sounds = (GuiComboBoxHeight) getParent().get(comboBox);
				
				List<String> soundList = new ArrayList<String>();
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
