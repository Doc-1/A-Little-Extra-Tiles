package com.alet.client.sounds;

import net.minecraft.client.audio.PositionedSound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;

public class StationarySound extends PositionedSound {
	
	public StationarySound(SoundEvent sound, BlockPos pos, float volume, float pitch, SoundCategory categoryIn) {
		super(sound, categoryIn);
		this.volume = volume;
		this.pitch = pitch;
		this.xPosF = pos.getX();
		this.yPosF = pos.getY();
		this.zPosF = pos.getZ();
	}
	
}
