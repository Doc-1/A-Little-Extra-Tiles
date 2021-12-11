package com.alet.common.structure.type;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
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

import com.alet.client.gui.SubGuiSoundSettings;
import com.alet.client.gui.controls.GuiLongTextField;
import com.alet.client.gui.controls.Layer;
import com.alet.client.gui.message.SubGuiNoPathMessage;
import com.alet.client.sounds.Notes;
import com.alet.common.packet.PacketSendSound;
import com.alet.common.structure.sound.Sound;
import com.alet.common.util.CopyUtils;
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
import com.creativemd.creativecore.common.utils.type.PairList;
import com.creativemd.littletiles.common.action.LittleActionException;
import com.creativemd.littletiles.common.action.block.LittleActionActivated;
import com.creativemd.littletiles.common.structure.LittleStructure;
import com.creativemd.littletiles.common.structure.animation.AnimationGuiHandler;
import com.creativemd.littletiles.common.structure.animation.AnimationTimeline;
import com.creativemd.littletiles.common.structure.animation.ValueTimeline;
import com.creativemd.littletiles.common.structure.animation.event.AnimationEvent;
import com.creativemd.littletiles.common.structure.animation.event.AnimationEventGuiParser;
import com.creativemd.littletiles.common.structure.animation.event.PlaySoundEvent;
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
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class LittleSoundPlayerALET extends LittleStructure {
	
	public ValueTimeline CH1, CH2, CH3, CH4, CH5, CH6, CH7, CH8, CH9, CH10, CH11, CH12, CH13, CH14, CH15, CH16;
	public String CHSound1, CHSound2, CHSound3, CHSound4, CHSound5, CHSound6, CHSound7, CHSound8, CHSound9, CHSound10,
	        CHSound11, CHSound12, CHSound13, CHSound14, CHSound15, CHSound16;
	public int duration = 10;
	public int tick = 0;
	public BlockPos pos;
	public boolean play = false;
	public int coursor;
	
	public boolean playLocal = false;
	public int volume = 1;
	
	public LittleSoundPlayerALET(LittleStructureType type, IStructureTileList mainBlock) {
		super(type, mainBlock);
	}
	
	@Override
	protected void writeToNBTExtra(NBTTagCompound nbt) {
		//Yes, yes I know. I could have done this with a for loop. Realized this after finished.
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
		if (CH10 != null)
			audio.setIntArray("ch10", CH10.write());
		if (CH11 != null)
			audio.setIntArray("ch11", CH11.write());
		if (CH12 != null)
			audio.setIntArray("ch12", CH12.write());
		if (CH13 != null)
			audio.setIntArray("ch13", CH13.write());
		if (CH14 != null)
			audio.setIntArray("ch14", CH14.write());
		if (CH15 != null)
			audio.setIntArray("ch15", CH15.write());
		if (CH16 != null)
			audio.setIntArray("ch16", CH16.write());
		
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
		if (CHSound10 != null)
			audioSetting.setString("chs10", CHSound10);
		if (CHSound11 != null)
			audioSetting.setString("chs11", CHSound11);
		if (CHSound12 != null)
			audioSetting.setString("chs12", CHSound12);
		if (CHSound13 != null)
			audioSetting.setString("chs13", CHSound13);
		if (CHSound14 != null)
			audioSetting.setString("chs14", CHSound14);
		if (CHSound15 != null)
			audioSetting.setString("chs15", CHSound15);
		if (CHSound16 != null)
			audioSetting.setString("chs16", CHSound16);
		
		nbt.setTag("audio", audio);
		nbt.setTag("audioSetting", audioSetting);
		nbt.setInteger("duration", duration);
		
		nbt.setInteger("volume", volume);
		nbt.setBoolean("local", playLocal);
	}
	
	@Override
	public void performInternalOutputChange(InternalSignalOutput output) {
		if (output.component.is("play")) {
			play = !play;
		}
	}
	
	@Override
	protected void loadFromNBTExtra(NBTTagCompound nbt) {
		//Yes, yes I know. I could have done this with a for loop. Realized this after finished.
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
			if (audio.hasKey("ch10"))
				CH10 = ValueTimeline.read(audio.getIntArray("ch10"));
			if (audio.hasKey("ch11"))
				CH11 = ValueTimeline.read(audio.getIntArray("ch11"));
			if (audio.hasKey("ch12"))
				CH12 = ValueTimeline.read(audio.getIntArray("ch12"));
			if (audio.hasKey("ch13"))
				CH13 = ValueTimeline.read(audio.getIntArray("ch13"));
			if (audio.hasKey("ch14"))
				CH14 = ValueTimeline.read(audio.getIntArray("ch14"));
			if (audio.hasKey("ch15"))
				CH15 = ValueTimeline.read(audio.getIntArray("ch15"));
			if (audio.hasKey("ch16"))
				CH16 = ValueTimeline.read(audio.getIntArray("ch16"));
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
			if (audioSetting.hasKey("chs10"))
				CHSound10 = audioSetting.getString("chs10");
			if (audioSetting.hasKey("chs11"))
				CHSound11 = audioSetting.getString("chs11");
			if (audioSetting.hasKey("chs12"))
				CHSound12 = audioSetting.getString("chs12");
			if (audioSetting.hasKey("chs13"))
				CHSound13 = audioSetting.getString("chs13");
			if (audioSetting.hasKey("chs14"))
				CHSound14 = audioSetting.getString("chs14");
			if (audioSetting.hasKey("chs15"))
				CHSound15 = audioSetting.getString("chs15");
			if (audioSetting.hasKey("chs16"))
				CHSound16 = audioSetting.getString("chs16");
		}
		if (nbt.hasKey("duration"))
			duration = nbt.getInteger("duration");
		
		if (nbt.hasKey("volume"))
			volume = nbt.getInteger("volume");
		
		if (nbt.hasKey("local"))
			playLocal = nbt.getBoolean("local");
		
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
		
		//Yes, yes I know. I could have done this with a for loop. Realized this after finished.
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
		if (CH10 != null && CH10.getPointsCopy().containsKey(tick)) {
			p = CH10.getPointsCopy().getValue(tick);
			pitch.add((int) p);
			timeLine.add(CH10);
			sound.add(CHSound10);
		}
		if (CH11 != null && CH11.getPointsCopy().containsKey(tick)) {
			p = CH11.getPointsCopy().getValue(tick);
			pitch.add((int) p);
			timeLine.add(CH11);
			sound.add(CHSound11);
		}
		if (CH12 != null && CH12.getPointsCopy().containsKey(tick)) {
			p = CH12.getPointsCopy().getValue(tick);
			pitch.add((int) p);
			timeLine.add(CH12);
			sound.add(CHSound12);
		}
		if (CH13 != null && CH13.getPointsCopy().containsKey(tick)) {
			p = CH13.getPointsCopy().getValue(tick);
			pitch.add((int) p);
			timeLine.add(CH13);
			sound.add(CHSound13);
		}
		if (CH14 != null && CH14.getPointsCopy().containsKey(tick)) {
			p = CH14.getPointsCopy().getValue(tick);
			pitch.add((int) p);
			timeLine.add(CH14);
			sound.add(CHSound14);
		}
		if (CH15 != null && CH15.getPointsCopy().containsKey(tick)) {
			p = CH15.getPointsCopy().getValue(tick);
			pitch.add((int) p);
			timeLine.add(CH15);
			sound.add(CHSound15);
		}
		if (CH16 != null && CH16.getPointsCopy().containsKey(tick)) {
			p = CH16.getPointsCopy().getValue(tick);
			pitch.add((int) p);
			timeLine.add(CH16);
			sound.add(CHSound16);
		}
		if (timeLine != null && !timeLine.isEmpty())
			for (int i = 0; i < timeLine.size(); i++) {
				if (timeLine.get(i).getPointsCopy().containsKey(tick)) {
					Notes note = Notes.getNoteFromPitch(pitch.get(i));
					if (sound.get(i) == "no sound")
						return;
					if (note != null)
						PacketHandler.sendPacketToAllPlayers(new PacketSendSound(pitch.get(i), volume, playLocal, pos, sound.get(i) != null ? sound.get(i) : "harp"));
				}
			}
	}
	
	public static class LittleSoundPlayerParserALET extends LittleStructureGuiParser {
		
		public String selectedSound;
		public int volume = 1;
		public boolean local = false;
		public boolean pauseUpdate = false;
		public List<String> possibleSounds;
		public String CHSound1 = "harp", CHSound2 = "harp", CHSound3 = "harp", CHSound4 = "harp", CHSound5 = "harp",
		        CHSound6 = "harp", CHSound7 = "harp", CHSound8 = "harp", CHSound9 = "harp", CHSound10 = "harp",
		        CHSound11 = "harp", CHSound12 = "harp", CHSound13 = "harp", CHSound14 = "harp", CHSound15 = "harp",
		        CHSound16 = "harp";
		
		public LittleSoundPlayerParserALET(GuiParent parent, AnimationGuiHandler handler) {
			super(parent, handler);
		}
		
		@Override
		protected void createControls(LittlePreviews previews, LittleStructure structure) {
			LittleSoundPlayerALET soundPlayer = structure instanceof LittleSoundPlayerALET ? (LittleSoundPlayerALET) structure : null;
			List<TimelineChannel> channels = new ArrayList<>();
			
			//Yes, yes I know. I could have done this with a for loop. Realized this after finished.
			channels.add(new TimelineChannelDouble("CH1"));
			channels.add(new TimelineChannelDouble("CH2"));
			channels.add(new TimelineChannelDouble("CH3"));
			channels.add(new TimelineChannelDouble("CH4"));
			channels.add(new TimelineChannelDouble("CH5"));
			channels.add(new TimelineChannelDouble("CH6"));
			channels.add(new TimelineChannelDouble("CH7"));
			channels.add(new TimelineChannelDouble("CH8"));
			channels.add(new TimelineChannelDouble("CH9"));
			channels.add(new TimelineChannelDouble("CH10"));
			channels.add(new TimelineChannelDouble("CH11"));
			channels.add(new TimelineChannelDouble("CH12"));
			channels.add(new TimelineChannelDouble("CH13"));
			channels.add(new TimelineChannelDouble("CH14"));
			channels.add(new TimelineChannelDouble("CH15"));
			channels.add(new TimelineChannelDouble("CH16"));
			if (soundPlayer != null) {
				if (soundPlayer.CH1 != null)
					channels.get(0).addKeys(soundPlayer.CH1.getPointsCopy());
				if (soundPlayer.CH2 != null)
					channels.get(1).addKeys(soundPlayer.CH2.getPointsCopy());
				if (soundPlayer.CH3 != null)
					channels.get(2).addKeys(soundPlayer.CH3.getPointsCopy());
				if (soundPlayer.CH4 != null)
					channels.get(3).addKeys(soundPlayer.CH4.getPointsCopy());
				if (soundPlayer.CH5 != null)
					channels.get(4).addKeys(soundPlayer.CH5.getPointsCopy());
				if (soundPlayer.CH6 != null)
					channels.get(5).addKeys(soundPlayer.CH6.getPointsCopy());
				if (soundPlayer.CH7 != null)
					channels.get(6).addKeys(soundPlayer.CH7.getPointsCopy());
				if (soundPlayer.CH8 != null)
					channels.get(7).addKeys(soundPlayer.CH8.getPointsCopy());
				if (soundPlayer.CH9 != null)
					channels.get(8).addKeys(soundPlayer.CH9.getPointsCopy());
				if (soundPlayer.CH10 != null)
					channels.get(9).addKeys(soundPlayer.CH10.getPointsCopy());
				if (soundPlayer.CH11 != null)
					channels.get(10).addKeys(soundPlayer.CH11.getPointsCopy());
				if (soundPlayer.CH12 != null)
					channels.get(11).addKeys(soundPlayer.CH12.getPointsCopy());
				if (soundPlayer.CH13 != null)
					channels.get(12).addKeys(soundPlayer.CH13.getPointsCopy());
				if (soundPlayer.CH14 != null)
					channels.get(13).addKeys(soundPlayer.CH14.getPointsCopy());
				if (soundPlayer.CH15 != null)
					channels.get(14).addKeys(soundPlayer.CH15.getPointsCopy());
				if (soundPlayer.CH16 != null)
					channels.get(15).addKeys(soundPlayer.CH16.getPointsCopy());
				
				if (soundPlayer.CHSound1 != null)
					CHSound1 = soundPlayer.CHSound1;
				if (soundPlayer.CHSound2 != null)
					CHSound2 = soundPlayer.CHSound2;
				if (soundPlayer.CHSound3 != null)
					CHSound3 = soundPlayer.CHSound3;
				if (soundPlayer.CHSound4 != null)
					CHSound4 = soundPlayer.CHSound4;
				if (soundPlayer.CHSound5 != null)
					CHSound5 = soundPlayer.CHSound5;
				if (soundPlayer.CHSound6 != null)
					CHSound6 = soundPlayer.CHSound6;
				if (soundPlayer.CHSound7 != null)
					CHSound7 = soundPlayer.CHSound7;
				if (soundPlayer.CHSound8 != null)
					CHSound8 = soundPlayer.CHSound8;
				if (soundPlayer.CHSound9 != null)
					CHSound9 = soundPlayer.CHSound9;
				if (soundPlayer.CHSound10 != null)
					CHSound10 = soundPlayer.CHSound10;
				if (soundPlayer.CHSound11 != null)
					CHSound11 = soundPlayer.CHSound11;
				if (soundPlayer.CHSound12 != null)
					CHSound12 = soundPlayer.CHSound12;
				if (soundPlayer.CHSound13 != null)
					CHSound13 = soundPlayer.CHSound13;
				if (soundPlayer.CHSound14 != null)
					CHSound14 = soundPlayer.CHSound14;
				if (soundPlayer.CHSound15 != null)
					CHSound15 = soundPlayer.CHSound15;
				if (soundPlayer.CHSound16 != null)
					CHSound16 = soundPlayer.CHSound16;
				
				volume = soundPlayer.volume;
				
				local = soundPlayer.playLocal;
			}
			parent.controls.add(new GuiTimeline("timeline", 0, 0, 200, 167, soundPlayer != null ? soundPlayer.duration : 10, channels, handler).setSidebarWidth(25));
			//parent.controls.add(new GuiTextfield("keyValue", "", 158, 0, 35, 10).setFloatOnly().setEnabled(false).setCustomTooltip("Pitch"));
			
			parent.controls.add(new GuiComboBox("keyValue", 208, 0, 35, Notes.allNotes()) {
				@Override
				public void closeBox() {
					updateTimeLine();
					super.closeBox();
				}
			});
			GuiComboBox keyValue = (GuiComboBox) parent.get("keyValue");
			keyValue.setCustomTooltip("Pitch").setEnabled(false);
			parent.controls.add(new GuiButton("Paste", 108, 175, 28, 8) {
				
				@Override
				public void onClicked(int x, int y, int button) {
					
					GuiLongTextField input = (GuiLongTextField) parent.get("file");
					
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
			
			parent.controls.add(new GuiTextfield("keyPosition", "", 208, 22, 35, 10).setNumbersOnly().setEnabled(false).setCustomTooltip("Tick"));
			
			parent.controls.add(new GuiTextfield("duration_s", structure instanceof LittleSoundPlayerALET ? "" + ((LittleSoundPlayerALET) structure).duration : "" + 10, 208, 40, 35, 8).setNumbersOnly().setLangTooltip("gui.door.duration"));
			
			parent.controls.add(new GuiButtonSoundChannel("Sound Settings", this, 0, 175, 75, 8));
			parent.controls.add(new GuiButton("importMid", "Import Midi", 150, 175, 50, 8) {
				@Override
				public void onClicked(int x, int y, int button) {
					importMidi();
					updateTimeLine();
				}
			});
			parent.get("importMid").setCustomTooltip("Enter File Path of a Midi File Below.", "Example: C:\\MIDIs\\Sound.mid");
			parent.controls.add(new GuiButton("clearSounds", "Wipe", 209, 175, 35, 8) {
				@Override
				public void onClicked(int x, int y, int button) {
					clearKeys();
					updateTimeLine();
				}
			});
			parent.get("clearSounds").setCustomTooltip("Removes all sounds from each channel");
			parent.controls.add(new GuiLongTextField("file", "", 48, 191, 196, 8).setCustomTooltip("Enter File Path Here"));
			updateTimeLine();
		}
		
		@SideOnly(Side.CLIENT)
		private KeyControl selected;
		
		@CustomEventSubscribe
		@SideOnly(Side.CLIENT)
		public void onKeySelected(KeySelectedEvent event) {
			
			GuiComboBox textfield = (GuiComboBox) parent.get("keyValue");
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
			
			updateTimeLine();
		}
		
		@CustomEventSubscribe
		@SideOnly(Side.CLIENT)
		public void onKeyDeselected(KeyDeselectedEvent event) {
			selected = null;
			GuiComboBox comboBox = (GuiComboBox) parent.get("keyValue");
			comboBox.setEnabled(false);
			comboBox.select(0);
			
			GuiTextfield textField = (GuiTextfield) parent.get("keyPosition");
			
			textField.setEnabled(false);
			textField.text = "";
			textField.setCursorPositionZero();
			
			updateTimeLine();
		}
		
		public void updateTimeLine() {
			if (!pauseUpdate) {
				GuiTimeline timeline = (GuiTimeline) parent.get("timeline");
				AnimationTimeline animation = new AnimationTimeline(timeline.getDuration(), new PairList<>());
				List<AnimationEvent> events = new ArrayList<AnimationEvent>();
				AnimationEventGuiParser parser = AnimationEvent.getParser("sound-event");
				List<Sound> sounds = generateArrayOfSounds();
				
				for (Sound sound : sounds) {
					PlaySoundEvent event = new PlaySoundEvent(sound.tick);
					Notes note = Notes.getNoteFromPitch(sound.pitch);
					event.opening = true;
					event.pitch = note.getPitch();
					event.sound = new SoundEvent(new ResourceLocation(note.getResourceLocation(sound.sound)));
					event.volume = 1.0F;
					events.add(event);
				}
				handler.setTimeline(animation, events);
			}
		}
		
		@CustomEventSubscribe
		@SideOnly(Side.CLIENT)
		public void onChange(GuiControlChangedEvent event) {
			if (event.source.is("keyValue")) {
				
				if (selected == null)
					return;
				
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
						updateTimeLine();
					}
				} catch (NumberFormatException e) {
					
				}
			} else if (event.source.is("duration_s")) {
				try {
					GuiTimeline timeline = (GuiTimeline) parent.get("timeline");
					timeline.setDuration(Integer.parseInt(((GuiTextfield) event.source).text));
				} catch (NumberFormatException e) {
					
				}
			} else if (event.source.is("timeline") || event.source.is("keyValue"))
				updateTimeLine();
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
							int tick = 0;
							double mod = (s.getTempoInBPM() * sequence.getResolution()) / 60D;
							pauseUpdate = true;
							for (Track track : s.getSequence().getTracks()) {
								
								for (int i = 0; i < track.size(); i++) {
									MidiEvent event = track.get(i);
									DecimalFormat df = new DecimalFormat("#####.##");
									double d = Double.parseDouble(df.format(event.getTick() / mod));
									tick = (int) (d * 20D);
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
											Notes note2 = Notes.getNoteFromPitch((int) pitch);
											
											//System.out.println(note2.getPitchName() + " : " + event.getTick() + " : " + tick);
											GuiTimeline channelList = (GuiTimeline) this.parent.get("timeline");
											KeyControl control = null;
											
											if (channelList.channels.get(sm.getChannel()).isSpaceFor(null, tick))
												control = channelList.channels.get(sm.getChannel()).addKey(tick, pitch);
											
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
							pauseUpdate = false;
							updateTimeLine();
						} catch (InvalidMidiDataException | IOException | MidiUnavailableException e) {
							e.printStackTrace();
						}
			} else
				Layer.addLayer(parent.getGui(), new SubGuiNoPathMessage(".mid"));
			
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
			structure.CH10 = ValueTimeline.create(0, timeline.channels.get(9).getPairs());
			structure.CH11 = ValueTimeline.create(0, timeline.channels.get(10).getPairs());
			structure.CH12 = ValueTimeline.create(0, timeline.channels.get(11).getPairs());
			structure.CH13 = ValueTimeline.create(0, timeline.channels.get(12).getPairs());
			structure.CH14 = ValueTimeline.create(0, timeline.channels.get(13).getPairs());
			structure.CH15 = ValueTimeline.create(0, timeline.channels.get(14).getPairs());
			structure.CH16 = ValueTimeline.create(0, timeline.channels.get(15).getPairs());
			
			structure.CHSound1 = CHSound1;
			structure.CHSound2 = CHSound2;
			structure.CHSound3 = CHSound3;
			structure.CHSound4 = CHSound4;
			structure.CHSound5 = CHSound5;
			structure.CHSound6 = CHSound6;
			structure.CHSound7 = CHSound7;
			structure.CHSound8 = CHSound8;
			structure.CHSound9 = CHSound9;
			structure.CHSound10 = CHSound10;
			structure.CHSound11 = CHSound11;
			structure.CHSound12 = CHSound12;
			structure.CHSound13 = CHSound13;
			structure.CHSound14 = CHSound14;
			structure.CHSound15 = CHSound15;
			structure.CHSound16 = CHSound16;
			
			structure.volume = volume;
			structure.playLocal = local;
			return structure;
		}
		
		public List<Sound> generateArrayOfSounds() {
			List<Sound> sounds = new ArrayList<Sound>();
			GuiTimeline timeline = (GuiTimeline) parent.get("timeline");
			
			ValueTimeline CH1 = ValueTimeline.create(0, timeline.channels.get(0).getPairs());
			ValueTimeline CH2 = ValueTimeline.create(0, timeline.channels.get(1).getPairs());
			ValueTimeline CH3 = ValueTimeline.create(0, timeline.channels.get(2).getPairs());
			ValueTimeline CH4 = ValueTimeline.create(0, timeline.channels.get(3).getPairs());
			ValueTimeline CH5 = ValueTimeline.create(0, timeline.channels.get(4).getPairs());
			ValueTimeline CH6 = ValueTimeline.create(0, timeline.channels.get(5).getPairs());
			ValueTimeline CH7 = ValueTimeline.create(0, timeline.channels.get(6).getPairs());
			ValueTimeline CH8 = ValueTimeline.create(0, timeline.channels.get(7).getPairs());
			ValueTimeline CH9 = ValueTimeline.create(0, timeline.channels.get(8).getPairs());
			ValueTimeline CH10 = ValueTimeline.create(0, timeline.channels.get(9).getPairs());
			ValueTimeline CH11 = ValueTimeline.create(0, timeline.channels.get(10).getPairs());
			ValueTimeline CH12 = ValueTimeline.create(0, timeline.channels.get(11).getPairs());
			ValueTimeline CH13 = ValueTimeline.create(0, timeline.channels.get(12).getPairs());
			ValueTimeline CH14 = ValueTimeline.create(0, timeline.channels.get(13).getPairs());
			ValueTimeline CH15 = ValueTimeline.create(0, timeline.channels.get(14).getPairs());
			ValueTimeline CH16 = ValueTimeline.create(0, timeline.channels.get(15).getPairs());
			
			for (int i = 0; i < timeline.getDuration(); i++) {
				
				//Yes, yes I know. I could have done this with a for loop. Realized this after finished.
				if (CH1 != null && CH1.getPointsCopy().containsKey(i) && !CHSound1.equals("nosound")) {
					Sound sound = new Sound();
					sound.setSound(CHSound1, CH1.getPointsCopy().getValue(i), i);
					sounds.add(sound);
				}
				if (CH2 != null && CH2.getPointsCopy().containsKey(i) && !CHSound2.equals("nosound")) {
					Sound sound = new Sound();
					sound.setSound(CHSound2, CH2.getPointsCopy().getValue(i), i);
					sounds.add(sound);
				}
				if (CH3 != null && CH3.getPointsCopy().containsKey(i) && !CHSound3.equals("nosound")) {
					Sound sound = new Sound();
					sound.setSound(CHSound3, CH3.getPointsCopy().getValue(i), i);
					sounds.add(sound);
				}
				if (CH4 != null && CH4.getPointsCopy().containsKey(i) && !CHSound4.equals("nosound")) {
					Sound sound = new Sound();
					sound.setSound(CHSound4, CH4.getPointsCopy().getValue(i), i);
					sounds.add(sound);
				}
				if (CH5 != null && CH5.getPointsCopy().containsKey(i) && !CHSound5.equals("nosound")) {
					Sound sound = new Sound();
					sound.setSound(CHSound5, CH5.getPointsCopy().getValue(i), i);
					sounds.add(sound);
				}
				if (CH6 != null && CH6.getPointsCopy().containsKey(i) && !CHSound6.equals("nosound")) {
					Sound sound = new Sound();
					sound.setSound(CHSound6, CH6.getPointsCopy().getValue(i), i);
					sounds.add(sound);
				}
				if (CH7 != null && CH7.getPointsCopy().containsKey(i) && !CHSound7.equals("nosound")) {
					Sound sound = new Sound();
					sound.setSound(CHSound7, CH7.getPointsCopy().getValue(i), i);
					sounds.add(sound);
				}
				if (CH8 != null && CH8.getPointsCopy().containsKey(i) && !CHSound8.equals("nosound")) {
					Sound sound = new Sound();
					sound.setSound(CHSound8, CH8.getPointsCopy().getValue(i), i);
					sounds.add(sound);
				}
				if (CH9 != null && CH9.getPointsCopy().containsKey(i) && !CHSound9.equals("nosound")) {
					Sound sound = new Sound();
					sound.setSound(CHSound9, CH9.getPointsCopy().getValue(i), i);
					sounds.add(sound);
				}
				if (CH10 != null && CH10.getPointsCopy().containsKey(i) && !CHSound10.equals("nosound")) {
					Sound sound = new Sound();
					sound.setSound(CHSound10, CH10.getPointsCopy().getValue(i), i);
					sounds.add(sound);
				}
				if (CH11 != null && CH11.getPointsCopy().containsKey(i) && !CHSound11.equals("nosound")) {
					Sound sound = new Sound();
					sound.setSound(CHSound11, CH11.getPointsCopy().getValue(i), i);
					sounds.add(sound);
				}
				if (CH12 != null && CH12.getPointsCopy().containsKey(i) && !CHSound12.equals("nosound")) {
					Sound sound = new Sound();
					sound.setSound(CHSound12, CH12.getPointsCopy().getValue(i), i);
					sounds.add(sound);
				}
				if (CH13 != null && CH13.getPointsCopy().containsKey(i) && !CHSound13.equals("nosound")) {
					Sound sound = new Sound();
					sound.setSound(CHSound13, CH13.getPointsCopy().getValue(i), i);
					sounds.add(sound);
				}
				if (CH14 != null && CH14.getPointsCopy().containsKey(i) && !CHSound14.equals("nosound")) {
					Sound sound = new Sound();
					sound.setSound(CHSound14, CH14.getPointsCopy().getValue(i), i);
					sounds.add(sound);
				}
				if (CH15 != null && CH15.getPointsCopy().containsKey(i) && !CHSound15.equals("nosound")) {
					Sound sound = new Sound();
					sound.setSound(CHSound15, CH15.getPointsCopy().getValue(i), i);
					sounds.add(sound);
				}
				if (CH16 != null && CH16.getPointsCopy().containsKey(i) && !CHSound16.equals("nosound")) {
					Sound sound = new Sound();
					sound.setSound(CHSound16, CH16.getPointsCopy().getValue(i), i);
					sounds.add(sound);
				}
				
			}
			
			return sounds;
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
				SubGuiSoundSettings channelSettings = new SubGuiSoundSettings(littleSoundPlayerParserALET);
				Layer.addLayer(getGui(), channelSettings);
				
			}
			
		}
	}
	
}
