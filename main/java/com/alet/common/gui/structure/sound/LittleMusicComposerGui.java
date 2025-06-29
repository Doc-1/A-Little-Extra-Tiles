package com.alet.common.gui.structure.sound;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.annotation.Nullable;
import javax.sound.midi.Instrument;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Soundbank;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.Track;

import com.alet.client.sounds.Notes;
import com.alet.client.sounds.Sound;
import com.alet.common.gui.controls.GuiLongTextField;
import com.alet.common.gui.messages.SubGuiNoPathMessage;
import com.alet.common.gui.messages.SubGuiTooManyChannels;
import com.alet.common.utils.CopyUtils;
import com.alet.common.utils.GuiLayerUtils;
import com.alet.components.structures.type.LittleMusicComposer;
import com.creativemd.creativecore.common.gui.GuiControl;
import com.creativemd.creativecore.common.gui.container.GuiParent;
import com.creativemd.creativecore.common.gui.controls.gui.GuiButton;
import com.creativemd.creativecore.common.gui.controls.gui.GuiCheckBox;
import com.creativemd.creativecore.common.gui.controls.gui.GuiComboBox;
import com.creativemd.creativecore.common.gui.controls.gui.GuiScrollBox;
import com.creativemd.creativecore.common.gui.controls.gui.GuiTextfield;
import com.creativemd.creativecore.common.gui.controls.gui.timeline.GuiTimeline;
import com.creativemd.creativecore.common.gui.controls.gui.timeline.GuiTimeline.KeyDeselectedEvent;
import com.creativemd.creativecore.common.gui.controls.gui.timeline.GuiTimeline.KeySelectedEvent;
import com.creativemd.creativecore.common.gui.controls.gui.timeline.KeyControl;
import com.creativemd.creativecore.common.gui.controls.gui.timeline.TimelineChannel;
import com.creativemd.creativecore.common.gui.controls.gui.timeline.TimelineChannel.TimelineChannelDouble;
import com.creativemd.creativecore.common.gui.event.gui.GuiControlChangedEvent;
import com.creativemd.creativecore.common.utils.type.PairList;
import com.creativemd.littletiles.client.gui.dialogs.SubGuiSignalEvents.GuiSignalEventsButton;
import com.creativemd.littletiles.common.structure.LittleStructure;
import com.creativemd.littletiles.common.structure.animation.AnimationGuiHandler;
import com.creativemd.littletiles.common.structure.animation.AnimationTimeline;
import com.creativemd.littletiles.common.structure.animation.ValueTimeline;
import com.creativemd.littletiles.common.structure.animation.event.AnimationEvent;
import com.creativemd.littletiles.common.structure.animation.event.PlaySoundEvent;
import com.creativemd.littletiles.common.structure.registry.LittleStructureGuiParser;
import com.creativemd.littletiles.common.structure.registry.LittleStructureRegistry;
import com.creativemd.littletiles.common.structure.registry.LittleStructureType;
import com.creativemd.littletiles.common.tile.preview.LittlePreviews;
import com.n247s.api.eventapi.eventsystem.CustomEventSubscribe;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class LittleMusicComposerGui extends LittleStructureGuiParser {
    
    public String selectedSound;
    public int volume = 1;
    public boolean local = true;
    public boolean pauseUpdate = false;
    public List<String> possibleSounds;
    public String[] channelSounds = new String[LittleMusicComposer.LENGTH];
    public int tempo = 120;
    public boolean changeTempo = false;
    
    public LittleMusicComposerGui(GuiParent parent, AnimationGuiHandler handler) {
        super(parent, handler);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void create(LittlePreviews previews, @Nullable LittleStructure structure) {
        createControls(previews, structure);
        parent.controls.add(new GuiSignalEventsButton("signal", 0, 191, previews, structure, getStructureType()));
    }
    
    @Override
    protected void createControls(LittlePreviews previews, LittleStructure structure) {
        LittleMusicComposer soundPlayer = structure instanceof LittleMusicComposer ? (LittleMusicComposer) structure : null;
        List<TimelineChannel> channels = new ArrayList<>();
        for (int i = 0; i < LittleMusicComposer.LENGTH; i++) {
            channels.add(new TimelineChannelDouble("CH" + (i + 1)));
            if (soundPlayer != null) {
                ValueTimeline channel = soundPlayer.channels[i];
                if (channel != null)
                    channels.get(i).addKeys(channel.getPointsCopy());
                if (soundPlayer.channelSounds[i] != null && !soundPlayer.channelSounds[i].isEmpty())
                    this.channelSounds[i] = soundPlayer.channelSounds[i];
            } else
                this.channelSounds[i] = "harp";
        }
        if (soundPlayer != null) {
            volume = soundPlayer.volume;
            local = soundPlayer.playLocal;
            tempo = soundPlayer.tempo;
            changeTempo = soundPlayer.changeTempo;
        }
        GuiScrollBox box = new GuiScrollBox("scroll_box", 0, 0, 200, 150);
        box.addControl(
            new GuiTimeline("timeline", 0, 0, 192, 187, soundPlayer != null ? soundPlayer.duration : 100, channels, handler)
                    .setSidebarWidth(25));
        parent.controls.add(box);
        //parent.controls.add(new GuiTextfield("keyValue", "", 158, 0, 35, 10).setFloatOnly().setEnabled(false).setCustomTooltip("Pitch"));
        
        parent.controls.add(new GuiComboBox("keyValue", 209, 0, 35, Notes.allNotes()) {
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
        
        parent.controls.add(new GuiCheckBox("changeTempo", "Change Import Tempo", 85, 158, changeTempo));
        
        parent.controls.add(new GuiTextfield("keyPosition", "", 209, 22, 35, 10).setNumbersOnly().setEnabled(false)
                .setCustomTooltip("Tick"));
        
        parent.controls.add(new GuiTextfield("tempo", "0", 209, 157, 35, 10).setNumbersOnly().setEnabled(changeTempo)
                .setCustomTooltip("Import Tempo"));
        parent.controls.add(
            new GuiTextfield("duration_s", structure instanceof LittleMusicComposer ? "" + ((LittleMusicComposer) structure).duration : "" + 10, 209, 40, 35, 8)
                    .setNumbersOnly().setLangTooltip("gui.door.duration"));
        
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
        else if (event.source.is("changeTempo")) {
            GuiCheckBox box = (GuiCheckBox) parent.get("changeTempo");
            GuiTextfield text = (GuiTextfield) parent.get("tempo");
            text.setEnabled(box.value);
            changeTempo = box.value;
        } else if (event.source.is("tempo")) {
            GuiTextfield text = (GuiTextfield) parent.get("tempo");
            if (!text.text.isEmpty())
                tempo = Integer.parseInt(text.text);
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
                        //String[] NOTE_NAMES = { "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B" };
                        
                        Sequence sequence = MidiSystem.getSequence(midiFile);
                        Sequencer s = MidiSystem.getSequencer();
                        s.setSequence(sequence);
                        if (changeTempo)
                            s.setTempoInBPM(tempo);
                        double mod = (s.getTempoInBPM() * sequence.getResolution()) / 60D;
                        pauseUpdate = true;
                        Track[] tracks = s.getSequence().getTracks();
                        Instrument instrument_bank[] = null;
                        String instrument_names[] = new String[tracks.length];
                        Synthesizer synthesizer = MidiSystem.getSynthesizer();
                        Soundbank sb = synthesizer.getDefaultSoundbank();
                        List<List<NoteData>> notes = new ArrayList<List<NoteData>>();
                        if (sb != null)
                            instrument_bank = sb.getInstruments();
                        HashSet<Integer> skipChannels = new HashSet<Integer>();
                        for (int j = 0; j < tracks.length; j++) {
                            Track track = tracks[j];
                            List<NoteData> nd = new ArrayList<>();
                            for (int i = 0; i < track.size(); i++) {
                                MidiEvent event = track.get(i);
                                MidiMessage message = event.getMessage();
                                if (message instanceof ShortMessage) {
                                    ShortMessage shortMesg = (ShortMessage) message;
                                    skipChannels.add(shortMesg.getChannel());
                                    if (instrument_bank != null && shortMesg.getCommand() == 192) {
                                        Instrument instrument = instrument_bank[shortMesg.getData1()];
                                        instrument_names[j] = instrument.getName();
                                    }
                                    if (shortMesg.getCommand() == 0x90) {
                                        DecimalFormat df = new DecimalFormat("#####.##");
                                        double d = Double.parseDouble(df.format(event.getTick() / mod));
                                        int tick = (int) (d * 20D);
                                        nd.add(new NoteData(tick, shortMesg.getData1()));
                                    }
                                }
                            }
                            notes.add(nd);
                        }
                        GuiTimeline guiChannelList = (GuiTimeline) this.parent.get("timeline");
                        int duration = 0;
                        
                        List<List<NoteData>> organizedList = organize(notes);
                        if (organizedList.size() <= LittleMusicComposer.LENGTH) {
                            for (int i = 0; i < organizedList.size(); i++) {
                                List<NoteData> channelList = organizedList.get(i);
                                for (int j = 0; j < channelList.size(); j++) {
                                    NoteData data = channelList.get(j);
                                    int key = data.key;
                                    int tick = data.tick;
                                    duration = Math.max(duration, tick);
                                    double pitch = 0;
                                    if (key > 64) {
                                        pitch = key % 64;
                                    } else if (key < 64) {
                                        pitch = -(64 % key);
                                    } else if (key == 64) {
                                        pitch = 0;
                                    }
                                    
                                    KeyControl control = null;
                                    if (guiChannelList.channels.get(i).isSpaceFor(null, tick))
                                        control = guiChannelList.channels.get(i).addKey(tick, pitch);
                                    
                                    if (control != null) {
                                        guiChannelList.adjustKeyPositionX(control);
                                        guiChannelList.adjustKeyPositionY(control);
                                        guiChannelList.addControl(control);
                                        guiChannelList.raiseEvent(new GuiControlChangedEvent(guiChannelList));
                                    }
                                }
                            }
                            
                            GuiTextfield guiDuration = (GuiTextfield) this.parent.get("duration_s");
                            guiDuration.text = duration + "";
                            guiChannelList.setDuration(duration);
                            pauseUpdate = false;
                            updateTimeLine();
                        } else {
                            GuiLayerUtils.addLayer(parent.getGui(), new SubGuiTooManyChannels(midiFile.getName()));
                        }
                    } catch (InvalidMidiDataException | IOException | MidiUnavailableException e) {
                        e.printStackTrace();
                    }
        } else
            GuiLayerUtils.addLayer(parent.getGui(), new SubGuiNoPathMessage(".mid"));
        
    }
    
    public List<List<NoteData>> organize(List<List<NoteData>> notes) {
        List<List<NoteData>> newNotes = new ArrayList<List<NoteData>>();
        boolean flag = true;
        for (List<NoteData> list : notes) {
            List<NoteData> newList = new ArrayList<>();
            List<NoteData> overflowList = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                if (i != 0 && list.get(i).tick == list.get(i - 1).tick) {
                    overflowList.add(list.get(i));
                } else {
                    newList.add(list.get(i));
                }
            }
            if (!overflowList.isEmpty()) {
                flag = false;
                newNotes.add(overflowList);
            }
            newNotes.add(newList);
        }
        if (flag)
            return newNotes;
        else
            return organize(newNotes);
    }
    
    @Override
    protected LittleStructure parseStructure(LittlePreviews previews) {
        LittleMusicComposer structure = createStructure(LittleMusicComposer.class, null);
        
        GuiTimeline timeline = (GuiTimeline) parent.get("timeline");
        structure.duration = timeline.getDuration();
        
        for (int i = 0; i < LittleMusicComposer.LENGTH; i++) {
            structure.channels[i] = ValueTimeline.create(0, timeline.channels.get(i).getPairs());
            structure.channelSounds[i] = this.channelSounds[i];
        }
        structure.volume = volume;
        structure.playLocal = local;
        return structure;
    }
    
    public List<Sound> generateArrayOfSounds() {
        List<Sound> sounds = new ArrayList<Sound>();
        GuiTimeline timeline = (GuiTimeline) parent.get("timeline");
        
        ValueTimeline[] channels = new ValueTimeline[LittleMusicComposer.LENGTH];
        
        for (int i = 0; i < LittleMusicComposer.LENGTH; i++)
            channels[i] = ValueTimeline.create(0, timeline.channels.get(i).getPairs());
        for (int i = 0; i < timeline.getDuration(); i++) {
            for (int j = 0; j < LittleMusicComposer.LENGTH; j++) {
                ValueTimeline channel = channels[j];
                if (channel != null && channel.getPointsCopy().containsKey(i) && !channelSounds[j].equals("nosound"))
                    sounds.add(new Sound().setSound(channelSounds[j], channel.getPointsCopy().getValue(i), i));
            }
        }
        
        return sounds;
    }
    
    @Override
    protected LittleStructureType getStructureType() {
        return LittleStructureRegistry.getStructureType(LittleMusicComposer.class);
    }
    
    private class GuiButtonSoundChannel extends GuiButton {
        
        LittleMusicComposerGui littleSoundPlayerParserALET;
        
        public GuiButtonSoundChannel(String caption, LittleMusicComposerGui littleSoundPlayerParserALET, int x, int y, int width, int height) {
            super(caption, x, y, width, height);
            this.littleSoundPlayerParserALET = littleSoundPlayerParserALET;
        }
        
        @Override
        public void onClicked(int x, int y, int button) {
            SubGuiSoundSettings channelSettings = new SubGuiSoundSettings(littleSoundPlayerParserALET);
            GuiLayerUtils.addLayer(getGui(), channelSettings);
            
        }
        
    }
    
    public static class NoteData {
        public int tick;
        public int key;
        
        public NoteData(int tick, int key) {
            this.tick = tick;
            this.key = key;
        }
    }
}
