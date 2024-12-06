package com.alet.common.structures.sound;

public class Sound {
	public String sound;
	public int pitch;
	public int tick;
	
	public Sound() {
		
	}
	
	public Sound(String sound, double pitch, int tick) {
		this.sound = sound;
		this.pitch = (int) pitch;
		this.tick = tick;
	}
	
	public Sound setSound(String sound, double pitch, int tick) {
		this.sound = sound;
		this.pitch = (int) pitch;
		this.tick = tick;
		return this;
	}
	
}
