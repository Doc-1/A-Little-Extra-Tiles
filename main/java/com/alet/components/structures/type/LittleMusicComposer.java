package com.alet.components.structures.type;

import java.util.ArrayList;
import java.util.List;

import com.alet.client.sounds.Notes;
import com.alet.common.packets.PacketSendSound;
import com.creativemd.creativecore.common.packet.PacketHandler;
import com.creativemd.creativecore.common.utils.math.BooleanUtils;
import com.creativemd.littletiles.common.action.LittleActionException;
import com.creativemd.littletiles.common.action.block.LittleActionActivated;
import com.creativemd.littletiles.common.structure.LittleStructure;
import com.creativemd.littletiles.common.structure.animation.ValueTimeline;
import com.creativemd.littletiles.common.structure.registry.LittleStructureType;
import com.creativemd.littletiles.common.structure.signal.output.InternalSignalOutput;
import com.creativemd.littletiles.common.tile.LittleTile;
import com.creativemd.littletiles.common.tile.parent.IStructureTileList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class LittleMusicComposer extends LittleStructure {
    
    public static final int LENGTH = 18;
    public ValueTimeline[] channels = new ValueTimeline[LENGTH];
    public String[] channelSounds = new String[LENGTH];
    public int duration = 10;
    public int tick = 0;
    public BlockPos pos;
    public boolean play = false;
    public int coursor;
    public boolean playLocal = false;
    public int volume = 1;
    
    public int tempo = 0;
    public boolean changeTempo = false;
    
    public LittleMusicComposer(LittleStructureType type, IStructureTileList mainBlock) {
        super(type, mainBlock);
    }
    
    @Override
    protected void writeToNBTExtra(NBTTagCompound nbt) {
        NBTTagCompound audio = new NBTTagCompound();
        for (int i = 0; i < LENGTH; i++) {
            ValueTimeline channel = channels[i];
            if (channel != null)
                audio.setIntArray("ch" + i, channel.write());
        }
        
        NBTTagCompound audioSetting = new NBTTagCompound();
        for (int j = 0; j < LENGTH; j++) {
            String sound = channelSounds[j];
            if (sound != null)
                audioSetting.setString("chs" + j, sound);
        }
        nbt.setTag("audio", audio);
        nbt.setTag("audioSetting", audioSetting);
        nbt.setInteger("duration", duration);
        
        nbt.setInteger("volume", volume);
        nbt.setBoolean("local", playLocal);
        
        nbt.setBoolean("change_tempo", changeTempo);
        nbt.setInteger("tempo", tempo);
        nbt.setBoolean("playing", play);
    }
    
    @Override
    public void performInternalOutputChange(InternalSignalOutput output) {
        if (output.component.is("play") && output.getState()[0] != play) {
            play = !play;
        }
    }
    
    @Override
    protected void loadFromNBTExtra(NBTTagCompound nbt) {
        if (nbt.hasKey("audio")) {
            NBTTagCompound audio = nbt.getCompoundTag("audio");
            for (int i = 0; i < LENGTH; i++)
                if (audio.hasKey("ch" + i))
                    channels[i] = ValueTimeline.read(audio.getIntArray("ch" + i));
        }
        
        if (nbt.hasKey("audioSetting")) {
            NBTTagCompound audioSetting = nbt.getCompoundTag("audioSetting");
            for (int j = 0; j < LENGTH; j++)
                if (audioSetting.hasKey("chs" + j))
                    channelSounds[j] = audioSetting.getString("chs" + j);
        }
        
        if (nbt.hasKey("playing"))
            play = nbt.getBoolean("playing");
        
        if (nbt.hasKey("duration"))
            duration = nbt.getInteger("duration");
        
        if (nbt.hasKey("volume"))
            volume = nbt.getInteger("volume");
        
        if (nbt.hasKey("local"))
            playLocal = nbt.getBoolean("local");
        
        if (nbt.hasKey("change_tempo"))
            changeTempo = nbt.getBoolean("change_tempo");
        
        if (nbt.hasKey("tempo"))
            tempo = nbt.getInteger("tempo");
    }
    
    @Override
    public boolean onBlockActivated(World worldIn, LittleTile tile, BlockPos pos, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ, LittleActionActivated action) throws LittleActionException {
        if (!worldIn.isRemote) {
            this.getOutput(0).toggle();
        }
        return true;
    }
    
    @Override
    public void tick() {
        if (this.isClient())
            return;
        if (play) {
            this.pos = getPos();
            tick++;
            playSound(tick);
            this.getInput(0).updateState(BooleanUtils.toBits(0, 1));
            if (tick > duration) {
                tick = 0;
                play = false;
                this.getInput(0).updateState(BooleanUtils.toBits(1, 1));
                this.getOutput(0).toggle();
            }
        } else {
            tick = 0;
        }
        
    }
    
    public void playSound(int tick) {
        List<Integer> pitch = new ArrayList<Integer>();
        List<ValueTimeline> timeLine = new ArrayList<ValueTimeline>();
        List<String> sound = new ArrayList<String>();
        
        for (int i = 0; i < channels.length - 1; i++) {
            ValueTimeline channel = channels[i];
            String channelSound = channelSounds[i];
            if (channel != null && channel.getPointsCopy().containsKey(tick)) {
                pitch.add(channel.getPointsCopy().getValue(tick).intValue());
                timeLine.add(channel);
                sound.add(channelSound);
            }
        }
        
        if (timeLine != null && !timeLine.isEmpty())
            for (int i = 0; i < timeLine.size(); i++) {
                if (timeLine.get(i).getPointsCopy().containsKey(tick)) {
                    Notes note = Notes.getNoteFromPitch(pitch.get(i));
                    if (sound.get(i) == "no sound")
                        return;
                    if (note != null)
                        PacketHandler.sendPacketToAllPlayers(new PacketSendSound(pitch.get(i), volume, playLocal, pos, sound
                                .get(i) != null ? sound.get(i) : "harp"));
                }
            }
    }
    
}
