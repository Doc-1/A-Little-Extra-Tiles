package com.alet.common.structure.type;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.Track;

import com.alet.client.gui.SubGuiSoundChannelSettings;
import com.alet.client.gui.controls.GuiLongTextField;
import com.alet.client.gui.controls.Layer;
import com.alet.client.sounds.Notes;
import com.alet.common.packet.PacketSendSound;
import com.creativemd.creativecore.common.gui.GuiControl;
import com.creativemd.creativecore.common.gui.container.GuiParent;
import com.creativemd.creativecore.common.gui.controls.gui.GuiButton;
import com.creativemd.creativecore.common.gui.controls.gui.GuiComboBox;
import com.creativemd.creativecore.common.gui.controls.gui.GuiTextfield;
import com.creativemd.creativecore.common.gui.controls.gui.timeline.GuiTimeline;
import com.creativemd.creativecore.common.gui.controls.gui.timeline.GuiTimeline.KeyDeselectedEvent;
import com.creativemd.creativecore.common.gui.controls.gui.timeline.GuiTimeline.KeySelectedEvent;
import com.creativemd.creativecore.common.gui.controls.gui.timeline.KeyControl;
import com.creativemd.creativecore.common.gui.controls.gui.timeline.TimelineChannel;
import com.creativemd.creativecore.common.gui.controls.gui.timeline.TimelineChannel.TimelineChannelDouble;
import com.creativemd.creativecore.common.gui.event.gui.GuiControlChangedEvent;
import com.creativemd.creativecore.common.packet.PacketHandler;
import com.creativemd.creativecore.common.utils.math.BooleanUtils;
import com.creativemd.littletiles.common.action.LittleActionException;
import com.creativemd.littletiles.common.action.block.LittleActionActivated;
import com.creativemd.littletiles.common.structure.LittleStructure;
import com.creativemd.littletiles.common.structure.animation.AnimationGuiHandler;
import com.creativemd.littletiles.common.structure.animation.ValueTimeline;
import com.creativemd.littletiles.common.structure.registry.LittleStructureGuiParser;
import com.creativemd.littletiles.common.structure.registry.LittleStructureRegistry;
import com.creativemd.littletiles.common.structure.registry.LittleStructureType;
import com.creativemd.littletiles.common.structure.signal.output.InternalSignalOutput;
import com.creativemd.littletiles.common.tile.LittleTile;
import com.creativemd.littletiles.common.tile.parent.IStructureTileList;
import com.creativemd.littletiles.common.tile.preview.LittlePreviews;
import com.n247s.api.eventapi.eventsystem.CustomEventSubscribe;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class LittleSoundPlayerALET extends LittleStructure {
	
	public ValueTimeline CH1, CH2, CH3, CH4, CH5, CH6, CH7, CH8, CH9;
	public String CHSound1, CHSound2, CHSound3, CHSound4, CHSound5, CHSound6, CHSound7, CHSound8, CHSound9;
	public int duration = 50;
	public int tick = 0;
	public BlockPos pos;
	public boolean play = false;
	public int coursor;
	
	public LittleSoundPlayerALET(LittleStructureType type, IStructureTileList mainBlock) {
		super(type, mainBlock);
	}
	
	@Override
	protected void writeToNBTExtra(NBTTagCompound nbt) {
		NBTTagCompound audio = new NBTTagCompound();
		if (CH1 != null)
			audio.setIntArray("ch1", CH1.write());
		if (CH2 != null)
			audio.setIntArray("ch2", CH2.write());
		if (CH3 != null)
			audio.setIntArray("ch3", CH3.write());
		if (CH4 != null)
			audio.setIntArray("ch4", CH4.write());
		if (CH5 != null)
			audio.setIntArray("ch5", CH5.write());
		if (CH6 != null)
			audio.setIntArray("ch6", CH6.write());
		if (CH7 != null)
			audio.setIntArray("ch7", CH7.write());
		if (CH8 != null)
			audio.setIntArray("ch8", CH8.write());
		if (CH9 != null)
			audio.setIntArray("ch9", CH9.write());
		
		NBTTagCompound audioSetting = new NBTTagCompound();
		if (CHSound1 != null)
			audioSetting.setString("chs1", CHSound1);
		if (CHSound2 != null)
			audioSetting.setString("chs2", CHSound2);
		if (CHSound3 != null)
			audioSetting.setString("chs3", CHSound3);
		if (CHSound4 != null)
			audioSetting.setString("chs4", CHSound4);
		if (CHSound5 != null)
			audioSetting.setString("chs5", CHSound5);
		if (CHSound6 != null)
			audioSetting.setString("chs6", CHSound6);
		if (CHSound7 != null)
			audioSetting.setString("chs7", CHSound7);
		if (CHSound8 != null)
			audioSetting.setString("chs8", CHSound8);
		if (CHSound9 != null)
			audioSetting.setString("chs9", CHSound9);
		
		nbt.setTag("audio", audio);
		nbt.setTag("audioSetting", audioSetting);
		nbt.setInteger("duration", duration);
	}
	
	@Override
	public void performInternalOutputChange(InternalSignalOutput output) {
		if (output.component.is("play")) {
			play = !play;
		}
	}
	
	@Override
	protected void loadFromNBTExtra(NBTTagCompound nbt) {
		if (nbt.hasKey("audio")) {
			NBTTagCompound audio = nbt.getCompoundTag("audio");
			if (audio.hasKey("ch1"))
				CH1 = ValueTimeline.read(audio.getIntArray("ch1"));
			if (audio.hasKey("ch2"))
				CH2 = ValueTimeline.read(audio.getIntArray("ch2"));
			if (audio.hasKey("ch3"))
				CH3 = ValueTimeline.read(audio.getIntArray("ch3"));
			if (audio.hasKey("ch4"))
				CH4 = ValueTimeline.read(audio.getIntArray("ch4"));
			if (audio.hasKey("ch5"))
				CH5 = ValueTimeline.read(audio.getIntArray("ch5"));
			if (audio.hasKey("ch6"))
				CH6 = ValueTimeline.read(audio.getIntArray("ch6"));
			if (audio.hasKey("ch7"))
				CH7 = ValueTimeline.read(audio.getIntArray("ch7"));
			if (audio.hasKey("ch8"))
				CH8 = ValueTimeline.read(audio.getIntArray("ch8"));
			if (audio.hasKey("ch9"))
				CH9 = ValueTimeline.read(audio.getIntArray("ch9"));
		}
		if (nbt.hasKey("audioSetting")) {
			
			NBTTagCompound audioSetting = nbt.getCompoundTag("audioSetting");
			if (audioSetting.hasKey("chs1"))
				CHSound1 = audioSetting.getString("chs1");
			if (audioSetting.hasKey("chs2"))
				CHSound2 = audioSetting.getString("chs2");
			if (audioSetting.hasKey("chs3"))
				CHSound3 = audioSetting.getString("chs3");
			if (audioSetting.hasKey("chs4"))
				CHSound4 = audioSetting.getString("chs4");
			if (audioSetting.hasKey("chs5"))
				CHSound5 = audioSetting.getString("chs5");
			if (audioSetting.hasKey("chs6"))
				CHSound6 = audioSetting.getString("chs6");
			if (audioSetting.hasKey("chs7"))
				CHSound7 = audioSetting.getString("chs7");
			if (audioSetting.hasKey("chs8"))
				CHSound8 = audioSetting.getString("chs8");
			if (audioSetting.hasKey("chs9"))
				CHSound9 = audioSetting.getString("chs9");
		}
		if (nbt.hasKey("duration"))
			duration = nbt.getInteger("duration");
		else
			duration = 10;
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, LittleTile tile, BlockPos pos, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ, LittleActionActivated action) throws LittleActionException {
		if (worldIn.isRemote)
			return true;
		if (!play) {
			this.play = true;
		}
		
		return true;
	}
	
	@Override
	public void tick() {
		if (play) {
			this.pos = getPos();
			tick++;
			playSound(tick);
			getInput(0).updateState(BooleanUtils.toBits(0, 1));
			if (tick > duration) {
				tick = 0;
				play = false;
				getInput(0).updateState(BooleanUtils.toBits(1, 1));
			}
		} else {
			tick = 0;
			
		}
		
	}
	
	public void playSound(int tick) {
		double p;
		List<Integer> pitch = new ArrayList<Integer>();
		List<ValueTimeline> timeLine = new ArrayList<ValueTimeline>();
		List<String> sound = new ArrayList<String>();
		if (CH1 != null && CH1.getPointsCopy().containsKey(tick)) {
			p = CH1.getPointsCopy().getValue(tick);
			pitch.add((int) p);
			timeLine.add(CH1);
			sound.add(CHSound1);
		}
		if (CH2 != null && CH2.getPointsCopy().containsKey(tick)) {
			p = CH2.getPointsCopy().getValue(tick);
			pitch.add((int) p);
			timeLine.add(CH2);
			sound.add(CHSound2);
		}
		if (CH3 != null && CH3.getPointsCopy().containsKey(tick)) {
			p = CH3.getPointsCopy().getValue(tick);
			pitch.add((int) p);
			timeLine.add(CH3);
			sound.add(CHSound3);
		}
		if (CH4 != null && CH4.getPointsCopy().containsKey(tick)) {
			p = CH4.getPointsCopy().getValue(tick);
			pitch.add((int) p);
			timeLine.add(CH4);
			sound.add(CHSound4);
		}
		if (CH5 != null && CH5.getPointsCopy().containsKey(tick)) {
			p = CH5.getPointsCopy().getValue(tick);
			pitch.add((int) p);
			timeLine.add(CH5);
			sound.add(CHSound5);
		}
		if (CH6 != null && CH6.getPointsCopy().containsKey(tick)) {
			p = CH6.getPointsCopy().getValue(tick);
			pitch.add((int) p);
			timeLine.add(CH6);
			sound.add(CHSound6);
		}
		if (CH7 != null && CH7.getPointsCopy().containsKey(tick)) {
			p = CH7.getPointsCopy().getValue(tick);
			pitch.add((int) p);
			timeLine.add(CH7);
			sound.add(CHSound7);
		}
		if (CH8 != null && CH8.getPointsCopy().containsKey(tick)) {
			p = CH8.getPointsCopy().getValue(tick);
			pitch.add((int) p);
			timeLine.add(CH8);
			sound.add(CHSound8);
		}
		if (CH9 != null && CH9.getPointsCopy().containsKey(tick)) {
			p = CH9.getPointsCopy().getValue(tick);
			pitch.add((int) p);
			timeLine.add(CH9);
			sound.add(CHSound9);
		}
		if (timeLine != null && !timeLine.isEmpty())
			for (int i = 0; i < timeLine.size(); i++) {
				if (timeLine.get(i).getPointsCopy().containsKey(tick)) {
					Notes note = Notes.getNoteFromPitch(pitch.get(i));
					
					if (note != null)
						PacketHandler.sendPacketToAllPlayers(new PacketSendSound(pitch.get(i), pos, sound.get(i) != null ? sound.get(i) : "harp"));
					
				}
			}
	}
	
	public static class LittleSoundPlayerParserALET extends LittleStructureGuiParser {
		
		public String selectedSound;
		public List<String> possibleSounds;
		public String CHSound1, CHSound2, CHSound3, CHSound4, CHSound5, CHSound6, CHSound7, CHSound8, CHSound9;
		
		public LittleSoundPlayerParserALET(GuiParent parent, AnimationGuiHandler handler) {
			super(parent, handler);
		}
		
		@Override
		protected void createControls(LittlePreviews previews, LittleStructure structure) {
			LittleSoundPlayerALET soundPlayer = structure instanceof LittleSoundPlayerALET ? (LittleSoundPlayerALET) structure : null;
			List<TimelineChannel> channels = new ArrayList<>();
			channels.add(new TimelineChannelDouble("CH1"));
			channels.add(new TimelineChannelDouble("CH2"));
			channels.add(new TimelineChannelDouble("CH3"));
			channels.add(new TimelineChannelDouble("CH4"));
			channels.add(new TimelineChannelDouble("CH5"));
			channels.add(new TimelineChannelDouble("CH6"));
			channels.add(new TimelineChannelDouble("CH7"));
			channels.add(new TimelineChannelDouble("CH8"));
			channels.add(new TimelineChannelDouble("CH9"));
			if (soundPlayer != null && soundPlayer.CH1 != null)
				channels.get(0).addKeys(soundPlayer.CH1.getPointsCopy());
			if (soundPlayer != null && soundPlayer.CH2 != null)
				channels.get(1).addKeys(soundPlayer.CH2.getPointsCopy());
			if (soundPlayer != null && soundPlayer.CH3 != null)
				channels.get(2).addKeys(soundPlayer.CH3.getPointsCopy());
			if (soundPlayer != null && soundPlayer.CH4 != null)
				channels.get(3).addKeys(soundPlayer.CH4.getPointsCopy());
			if (soundPlayer != null && soundPlayer.CH5 != null)
				channels.get(4).addKeys(soundPlayer.CH5.getPointsCopy());
			if (soundPlayer != null && soundPlayer.CH6 != null)
				channels.get(5).addKeys(soundPlayer.CH6.getPointsCopy());
			if (soundPlayer != null && soundPlayer.CH7 != null)
				channels.get(6).addKeys(soundPlayer.CH7.getPointsCopy());
			if (soundPlayer != null && soundPlayer.CH8 != null)
				channels.get(7).addKeys(soundPlayer.CH8.getPointsCopy());
			if (soundPlayer != null && soundPlayer.CH9 != null)
				channels.get(8).addKeys(soundPlayer.CH9.getPointsCopy());
			
			if (soundPlayer != null && soundPlayer.CHSound1 != null)
				CHSound1 = soundPlayer.CHSound1;
			if (soundPlayer != null && soundPlayer.CHSound2 != null)
				CHSound2 = soundPlayer.CHSound2;
			if (soundPlayer != null && soundPlayer.CHSound3 != null)
				CHSound3 = soundPlayer.CHSound3;
			if (soundPlayer != null && soundPlayer.CHSound4 != null)
				CHSound4 = soundPlayer.CHSound4;
			if (soundPlayer != null && soundPlayer.CHSound5 != null)
				CHSound5 = soundPlayer.CHSound5;
			if (soundPlayer != null && soundPlayer.CHSound6 != null)
				CHSound6 = soundPlayer.CHSound6;
			if (soundPlayer != null && soundPlayer.CHSound7 != null)
				CHSound7 = soundPlayer.CHSound7;
			if (soundPlayer != null && soundPlayer.CHSound8 != null)
				CHSound8 = soundPlayer.CHSound8;
			if (soundPlayer != null && soundPlayer.CHSound9 != null)
				CHSound9 = soundPlayer.CHSound9;
			
			parent.controls.add(new GuiTimeline("timeline", 0, 0, 150, 97, soundPlayer != null ? soundPlayer.duration : 10, channels, handler).setSidebarWidth(20));
			//parent.controls.add(new GuiTextfield("keyValue", "", 158, 0, 35, 10).setFloatOnly().setEnabled(false).setCustomTooltip("Pitch"));
			
			parent.controls.add(new GuiComboBox("keyValue", 158, 0, 35, Notes.allNotes()).setCustomTooltip("Pitch"));
			parent.controls.add(new GuiButtonSoundChannel("Channel Settings", this, 0, 105, 80, 8));
			
			parent.controls.add(new GuiTextfield("keyPosition", "", 158, 20, 35, 10).setNumbersOnly().setEnabled(false).setCustomTooltip("Tick"));
			parent.controls.add(new GuiTextfield("duration_s", structure instanceof LittleSoundPlayerALET ? "" + ((LittleSoundPlayerALET) structure).duration : "" + 10, 158, 40, 35, 8).setNumbersOnly().setLangTooltip("gui.door.duration"));
			
			parent.controls.add(new GuiButton("importMid", "Import Midi", 144, 105, 50, 8) {
				@Override
				public void onClicked(int x, int y, int button) {
					importMidi();
				}
			});
			parent.get("importMid").setCustomTooltip("Enter File Path of a Midi File Below.", "Example: C:\\MIDIs\\Sound.mid");
			parent.controls.add(new GuiButton("clearSounds", "Wipe", 107, 105, 30, 8) {
				@Override
				public void onClicked(int x, int y, int button) {
					clearKeys();
				}
			});
			parent.get("clearSounds").setCustomTooltip("Removes all sounds from each channel");
			parent.controls.add(new GuiLongTextField("file", "", 48, 121, 145, 8));
			
		}
		
		@SideOnly(Side.CLIENT)
		private KeyControl selected;
		
		@CustomEventSubscribe
		@SideOnly(Side.CLIENT)
		public void onKeySelected(KeySelectedEvent event) {
			
			GuiComboBox textfield = (GuiComboBox) parent.get("keyValue");
			System.out.println(handler.get());
			selected = (KeyControl) event.source;
			
			if (((KeyControl) event.source).value instanceof Double) {
				textfield.setEnabled(true);
				textfield.setVisible(true);
				Notes note = Notes.getNoteFromPitch((int) Double.parseDouble("" + selected.value));
				textfield.select(note.getPitchName());
			} else {
				textfield.setVisible(false);
				textfield.setEnabled(false);
			}
			
			GuiTextfield position = (GuiTextfield) parent.get("keyPosition");
			position.setEnabled(true);
			position.text = "" + selected.tick;
			
		}
		
		@CustomEventSubscribe
		@SideOnly(Side.CLIENT)
		public void onChange(GuiControlChangedEvent event) {
			if (event.source.is("keyValue")) {
				if (!selected.modifiable)
					return;
				
				try {
					String pitchName = ((GuiComboBox) event.source).getCaption();
					
					Notes note = Notes.getNote(pitchName);
					selected.value = Double.parseDouble(note.getPos() + "");
				} catch (NumberFormatException e) {
					
				}
			} else if (event.source.is("keyPosition")) {
				if (!selected.modifiable)
					return;
				
				try {
					GuiTimeline timeline = (GuiTimeline) parent.get("timeline");
					
					int tick = selected.tick;
					int newTick = Integer.parseInt(((GuiTextfield) event.source).text);
					if (selected.channel.isSpaceFor(selected, newTick)) {
						selected.tick = newTick;
						selected.channel.movedKey(selected);
						if (tick != selected.tick)
							timeline.adjustKeysPositionX();
					}
				} catch (NumberFormatException e) {
					
				}
			} else if (event.source.is("duration_s")) {
				try {
					GuiTimeline timeline = (GuiTimeline) parent.get("timeline");
					timeline.setDuration(Integer.parseInt(((GuiTextfield) event.source).text));
				} catch (NumberFormatException e) {
					
				}
			}
		}
		
		public void clearKeys() {
			GuiTimeline channelList = (GuiTimeline) this.parent.get("timeline");
			
			List<KeyControl> controlsToRemove = new ArrayList<KeyControl>();
			
			for (GuiControl control : channelList.controls)
				if (control instanceof KeyControl)
					controlsToRemove.add((KeyControl) control);
				
			for (KeyControl k : controlsToRemove) {
				channelList.removeControl(k);
				for (TimelineChannel ch : channelList.channels)
					ch.removeKey(k);
			}
			
		}
		
		public void importMidi() {
			GuiLongTextField file = (GuiLongTextField) this.parent.get("file");
			File midiFile = new File(file.text);
			String extension = "";
			if (midiFile.exists()) {
				extension = midiFile.getName().substring(midiFile.getName().lastIndexOf(".") + 1, midiFile.getName().length());
				if (!file.text.equals(""))
					if (extension.equals("mid"))
						try {
							String[] NOTE_NAMES = { "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B" };
							
							Sequence sequence = MidiSystem.getSequence(midiFile);
							Synthesizer synth = MidiSystem.getSynthesizer();
							Sequencer s = MidiSystem.getSequencer();
							s.setSequence(sequence);
							int trackNumber = 0;
							int tick = 0;
							int usedChannels = 0;
							int previousUsedChannels = 0;
							int previousChannel = -1;
							for (Track track : s.getSequence().getTracks()) {
								
								trackNumber++;
								for (int i = 0; i < track.size(); i++) {
									MidiEvent event = track.get(i);
									
									double mod = (s.getTempoInBPM() * sequence.getResolution()) / 60D;
									tick = (int) ((event.getTick() / mod) * 20D);
									MidiMessage message = event.getMessage();
									if (message instanceof ShortMessage) {
										ShortMessage sm = (ShortMessage) message;
										if (sm.getCommand() == 0x90) {
											int key = sm.getData1();
											
											int octave = (key / 12) - 1;
											int note = key % 12;
											String noteName = NOTE_NAMES[note];
											double pitch = 0;
											if (key > 64) {
												pitch = key % 64;
											} else if (key < 64) {
												pitch = -(64 % key);
											} else if (key == 64) {
												pitch = 0;
											}
											
											GuiTimeline channelList = (GuiTimeline) this.parent.get("timeline");
											KeyControl control = null;
											if (previousChannel == -1)
												previousChannel = sm.getChannel();
											
											if (previousChannel != sm.getChannel())
												usedChannels = previousUsedChannels;
											
											for (int n = usedChannels; n < 9; n++) {
												if (channelList.channels.get(n).isSpaceFor(null, tick)) {
													control = channelList.channels.get(n).addKey(tick, pitch);
													
													previousUsedChannels = previousUsedChannels < n + 1 ? n + 1 : previousUsedChannels;
													break;
												}
											}
											
											previousChannel = sm.getChannel();
											
											if (control != null) {
												channelList.adjustKeyPositionX(control);
												channelList.adjustKeyPositionY(control);
												channelList.addControl(control);
												channelList.raiseEvent(new GuiControlChangedEvent(channelList));
											}
										}
									}
									
								}
							}
							
							GuiTimeline channelList = (GuiTimeline) this.parent.get("timeline");
							GuiTextfield duration = (GuiTextfield) this.parent.get("duration_s");
							duration.text = tick + "";
							channelList.setDuration(tick);
						} catch (InvalidMidiDataException | IOException | MidiUnavailableException e) {
							e.printStackTrace();
						}
			}
		}
		
		@CustomEventSubscribe
		@SideOnly(Side.CLIENT)
		public void onKeyDeselected(KeyDeselectedEvent event) {
			selected = null;
			GuiTextfield textfield = (GuiTextfield) parent.get("keyValue");
			textfield.setEnabled(false);
			textfield.text = "";
			textfield.setCursorPositionZero();
			
			textfield = (GuiTextfield) parent.get("keyPosition");
			textfield.setEnabled(false);
			textfield.text = "";
			textfield.setCursorPositionZero();
			
		}
		
		@Override
		protected LittleStructure parseStructure(LittlePreviews previews) {
			LittleSoundPlayerALET structure = createStructure(LittleSoundPlayerALET.class, null);
			
			GuiTimeline timeline = (GuiTimeline) parent.get("timeline");
			
			structure.duration = timeline.getDuration();
			structure.CH1 = ValueTimeline.create(0, timeline.channels.get(0).getPairs());
			structure.CH2 = ValueTimeline.create(0, timeline.channels.get(1).getPairs());
			structure.CH3 = ValueTimeline.create(0, timeline.channels.get(2).getPairs());
			structure.CH4 = ValueTimeline.create(0, timeline.channels.get(3).getPairs());
			structure.CH5 = ValueTimeline.create(0, timeline.channels.get(4).getPairs());
			structure.CH6 = ValueTimeline.create(0, timeline.channels.get(5).getPairs());
			structure.CH7 = ValueTimeline.create(0, timeline.channels.get(6).getPairs());
			structure.CH8 = ValueTimeline.create(0, timeline.channels.get(7).getPairs());
			structure.CH9 = ValueTimeline.create(0, timeline.channels.get(8).getPairs());
			
			structure.CHSound1 = CHSound1;
			structure.CHSound2 = CHSound2;
			structure.CHSound3 = CHSound3;
			structure.CHSound4 = CHSound4;
			structure.CHSound5 = CHSound5;
			structure.CHSound6 = CHSound6;
			structure.CHSound7 = CHSound7;
			structure.CHSound8 = CHSound8;
			structure.CHSound9 = CHSound9;
			return structure;
		}
		
		@Override
		protected LittleStructureType getStructureType() {
			return LittleStructureRegistry.getStructureType(LittleSoundPlayerALET.class);
		}
		
		private class GuiButtonSoundChannel extends GuiButton {
			
			LittleSoundPlayerParserALET littleSoundPlayerParserALET;
			
			public GuiButtonSoundChannel(String caption, LittleSoundPlayerParserALET littleSoundPlayerParserALET, int x, int y, int width, int height) {
				super(caption, x, y, width, height);
				this.littleSoundPlayerParserALET = littleSoundPlayerParserALET;
			}
			
			@Override
			public void onClicked(int x, int y, int button) {
				SubGuiSoundChannelSettings channelSettings = new SubGuiSoundChannelSettings(littleSoundPlayerParserALET);
				Layer.addLayer(getGui(), channelSettings);
				
			}
			
		}
	}
	
}
