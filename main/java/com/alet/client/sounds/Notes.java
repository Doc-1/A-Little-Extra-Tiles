package com.alet.client.sounds;

import java.util.ArrayList;
import java.util.List;

public enum Notes {
    //
	G1((float) Math.pow(2F, -5F / 12F), (float) Math.pow(2F, -33F / 12F), -33, "G1", "alet:block.note.-low5"),
	GA1((float) Math.pow(2F, -4F / 12F), (float) Math.pow(2F, -32F / 12F), -32, "G#1", "alet:block.note.-low5"),
	A1((float) Math.pow(2F, -3F / 12F), (float) Math.pow(2F, -31F / 12F), -31, "A1", "alet:block.note.-low5"),
	AB1((float) Math.pow(2F, -2F / 12F), (float) Math.pow(2F, -30F / 12F), -30, "A#1", "alet:block.note.-low5"),
	B1((float) Math.pow(2F, -1F / 12F), (float) Math.pow(2F, -29F / 12F), -29, "B1", "alet:block.note.-low5"),
	C2((float) Math.pow(2F, 0F / 12F), (float) Math.pow(2F, -28F / 12F), -28, "C2", "alet:block.note.-low5"),
    //
	
    //
	CD2((float) Math.pow(2F, -5F / 12F), (float) Math.pow(2F, -27F / 12F), -27, "C#2", "alet:block.note.-low4"),
	D2((float) Math.pow(2F, -4F / 12F), (float) Math.pow(2F, -26F / 12F), -26, "D2", "alet:block.note.-low4"),
	DE2((float) Math.pow(2F, -3F / 12F), (float) Math.pow(2F, -25F / 12F), -25, "D#2", "alet:block.note.-low4"),
	E2((float) Math.pow(2F, -2F / 12F), (float) Math.pow(2F, -24F / 12F), -24, "E2", "alet:block.note.-low4"),
	F2((float) Math.pow(2F, -1F / 12F), (float) Math.pow(2F, -23F / 12F), -23, "F2", "alet:block.note.-low4"),
	FG2((float) Math.pow(2F, 0F / 12F), (float) Math.pow(2F, -22F / 12F), -22, "F#2", "alet:block.note.-low4"),
    //
	
    //
	G2((float) Math.pow(2F, -5F / 12F), (float) Math.pow(2F, -21F / 12F), -21, "G2", "alet:block.note.-low3"),
	GA2((float) Math.pow(2F, -4F / 12F), (float) Math.pow(2F, -20F / 12F), -20, "G#2", "alet:block.note.-low3"),
	A2((float) Math.pow(2F, -3F / 12F), (float) Math.pow(2F, -19F / 12F), -19, "A2", "alet:block.note.-low3"),
	AB2((float) Math.pow(2F, -2F / 12F), (float) Math.pow(2F, -18F / 12F), -18, "A#2", "alet:block.note.-low3"),
	B2((float) Math.pow(2F, -1F / 12F), (float) Math.pow(2F, -17F / 12F), -17, "B2", "alet:block.note.-low3"),
	C3((float) Math.pow(2F, 0F / 12F), (float) Math.pow(2F, -16F / 12F), -16, "C3", "alet:block.note.-low3"),
    //
	
    //
	CD3((float) Math.pow(2F, -5F / 12F), (float) Math.pow(2F, -15F / 12F), -15, "C#3", "alet:block.note.-low2"),
	D3((float) Math.pow(2F, -4F / 12F), (float) Math.pow(2F, -14F / 12F), -14, "D3", "alet:block.note.-low2"),
	DE3((float) Math.pow(2F, -3F / 12F), (float) Math.pow(2F, -13F / 12F), -13, "D#3", "alet:block.note.-low2"),
	E3((float) Math.pow(2F, -2F / 12F), (float) Math.pow(2F, -12F / 12F), -12, "E3", "alet:block.note.-low2"),
	F3((float) Math.pow(2F, -1F / 12F), (float) Math.pow(2F, -11F / 12F), -11, "F3", "alet:block.note.-low2"),
	FG3((float) Math.pow(2F, 0F / 12F), (float) Math.pow(2F, -10F / 12F), -10, "F#3", "alet:block.note.-low2"),
    //
	
    //
	G3((float) Math.pow(2F, -5F / 12F), (float) Math.pow(2F, -9F / 12F), -9, "G3", "alet:block.note.-low"),
	GA3((float) Math.pow(2F, -4F / 12F), (float) Math.pow(2F, -8F / 12F), -8, "G#3", "alet:block.note.-low"),
	A3((float) Math.pow(2F, -3F / 12F), (float) Math.pow(2F, -7F / 12F), -7, "A3", "alet:block.note.-low"),
	AB3((float) Math.pow(2F, -2F / 12F), (float) Math.pow(2F, -6F / 12F), -6, "A#3", "alet:block.note.-low"),
	B3((float) Math.pow(2F, -1F / 12F), (float) Math.pow(2F, -5F / 12F), -5, "B3", "alet:block.note.-low"),
	C4((float) Math.pow(2F, 0F / 12F), (float) Math.pow(2F, -4F / 12F), -4, "C4", "alet:block.note.-low"),
	
    //
	CD4((float) Math.pow(2F, -5F / 12F), (float) Math.pow(2F, -3F / 12F), -3, "C#4", "alet:block.note.-"),
	D4((float) Math.pow(2F, -4F / 12F), (float) Math.pow(2F, -2F / 12F), -2, "D4", "alet:block.note.-"),
	DE4((float) Math.pow(2F, -3F / 12F), (float) Math.pow(2F, -1F / 12F), -1, "D#4", "alet:block.note.-"),
	E4((float) Math.pow(2F, -2F / 12F), (float) Math.pow(2F, 0F / 12F), 0, "E4", "alet:block.note.-"),
	F4((float) Math.pow(2F, -1F / 12F), (float) Math.pow(2F, 1F / 12F), 1, "F4", "alet:block.note.-"),
	FG4((float) Math.pow(2F, 0F / 12F), (float) Math.pow(2F, 2F / 12F), 2, "F#4", "alet:block.note.-"),
	G4((float) Math.pow(2F, 1F / 12F), (float) Math.pow(2F, 3F / 12F), 3, "G4", "alet:block.note.-"),
	GA4((float) Math.pow(2F, 2F / 12F), (float) Math.pow(2F, 4F / 12F), 4, "G#4", "alet:block.note.-"),
	A4((float) Math.pow(2F, 3F / 12F), (float) Math.pow(2F, 5F / 12F), 5, "A4", "alet:block.note.-"),
	AB4((float) Math.pow(2F, 4F / 12F), (float) Math.pow(2F, 6F / 12F), 6, "A#4", "alet:block.note.-"),
	B4((float) Math.pow(2F, 5F / 12F), (float) Math.pow(2F, 7F / 12F), 7, "B4", "alet:block.note.-"),
    //
	
    //
	C5((float) Math.pow(2F, 0F / 12F), (float) Math.pow(2F, 8F / 12F), 8, "C5", "alet:block.note.-high"),
	CD5((float) Math.pow(2F, 1F / 12F), (float) Math.pow(2F, 9F / 12F), 9, "C#5", "alet:block.note.-high"),
	D5((float) Math.pow(2F, 2F / 12F), (float) Math.pow(2F, 10F / 12F), 10, "D5", "alet:block.note.-high"),
	DE5((float) Math.pow(2F, 3F / 12F), (float) Math.pow(2F, 11F / 12F), 11, "D#5", "alet:block.note.-high"),
	E5((float) Math.pow(2F, 4F / 12F), (float) Math.pow(2F, 12F / 12F), 12, "E5", "alet:block.note.-high"),
	F5((float) Math.pow(2F, 5F / 12F), (float) Math.pow(2F, 13F / 12F), 13, "F5", "alet:block.note.-high"),
    //
	
    //
	FG5((float) Math.pow(2F, 0F / 12F), (float) Math.pow(2F, 14F / 12F), 14, "F#5", "alet:block.note.-high2"),
	G5((float) Math.pow(2F, 1F / 12F), (float) Math.pow(2F, 15F / 12F), 15, "G5", "alet:block.note.-high2"),
	GA5((float) Math.pow(2F, 2F / 12F), (float) Math.pow(2F, 16F / 12F), 16, "G#5", "alet:block.note.-high2"),
	A5((float) Math.pow(2F, 3F / 12F), (float) Math.pow(2F, 17F / 12F), 17, "A5", "alet:block.note.-high2"),
	AB5((float) Math.pow(2F, 4F / 12F), (float) Math.pow(2F, 18F / 12F), 18, "A#5", "alet:block.note.-high2"),
	B5((float) Math.pow(2F, 5F / 12F), (float) Math.pow(2F, 19F / 12F), 19, "B5", "alet:block.note.-high2"),
    //
	
    //
	C6((float) Math.pow(2F, 0F / 12F), (float) Math.pow(2F, 20F / 12F), 20, "C6", "alet:block.note.-high3"),
	CD6((float) Math.pow(2F, 1F / 12F), (float) Math.pow(2F, 21F / 12F), 21, "C#6", "alet:block.note.-high3"),
	D6((float) Math.pow(2F, 2F / 12F), (float) Math.pow(2F, 22F / 12F), 22, "D6", "alet:block.note.-high3"),
	DE6((float) Math.pow(2F, 3F / 12F), (float) Math.pow(2F, 23F / 12F), 23, "D#6", "alet:block.note.-high3"),
	E6((float) Math.pow(2F, 4F / 12F), (float) Math.pow(2F, 24F / 12F), 24, "E6", "alet:block.note.-high3"),
	F6((float) Math.pow(2F, 5F / 12F), (float) Math.pow(2F, 25F / 12F), 25, "F6", "alet:block.note.-high3"),
    //
	
    //
	FG6((float) Math.pow(2F, 0F / 12F), (float) Math.pow(2F, 26F / 12F), 26, "F#6", "alet:block.note.-high4"),
	G6((float) Math.pow(2F, 1F / 12F), (float) Math.pow(2F, 27F / 12F), 27, "G6", "alet:block.note.-high4"),
	GA6((float) Math.pow(2F, 2F / 12F), (float) Math.pow(2F, 28F / 12F), 28, "G#6", "alet:block.note.-high4"),
	A6((float) Math.pow(2F, 3F / 12F), (float) Math.pow(2F, 29F / 12F), 29, "A6", "alet:block.note.-high4"),
	AB6((float) Math.pow(2F, 4F / 12F), (float) Math.pow(2F, 30F / 12F), 30, "A#6", "alet:block.note.-high4"),
	B6((float) Math.pow(2F, 5F / 12F), (float) Math.pow(2F, 31F / 12F), 31, "B6", "alet:block.note.-high4"),
    //
	
    //
	C7((float) Math.pow(2F, 0F / 12F), (float) Math.pow(2F, 32F / 12F), 32, "C7", "alet:block.note.-high5"),
	CD7((float) Math.pow(2F, 1F / 12F), (float) Math.pow(2F, 33F / 12F), 33, "C#7", "alet:block.note.-high5"),
	D7((float) Math.pow(2F, 2F / 12F), (float) Math.pow(2F, 34F / 12F), 34, "D7", "alet:block.note.-high5"),
	DE7((float) Math.pow(2F, 3F / 12F), (float) Math.pow(2F, 35F / 12F), 35, "D#7", "alet:block.note.-high5"),
	E7((float) Math.pow(2F, 4F / 12F), (float) Math.pow(2F, 36F / 12F), 36, "E7", "alet:block.note.-high5"),
	F7((float) Math.pow(2F, 5F / 12F), (float) Math.pow(2F, 37F / 12F), 37, "F7", "alet:block.note.-high5");
	//
	
	private float pitchOut;
	private float pitchIn;
	private String pitchName;
	private String resourceLocation;
	private int pos;
	
	private Notes(float pitchOut, float pitchIn, int pos, String pitchName, String resourceLocation) {
		this.pitchOut = pitchOut;
		this.pitchName = pitchName;
		this.pitchIn = pitchIn;
		this.resourceLocation = resourceLocation;
		this.pos = pos;
	}
	
	public static Notes getNoteFromPitch(int pitch) {
		float pitchC = (float) Math.pow(2F, pitch / 12F);
		for (Notes n : values()) {
			if (pitchC == n.pitchIn) {
				return n;
			}
		}
		return null;
	}
	
	public static Notes getNoteFromPos(int pos) {
		for (Notes n : values()) {
			if (pos == n.pos) {
				return n;
			}
		}
		return null;
	}
	
	public static Notes getNote(String pitchName) {
		for (Notes n : values()) {
			if (pitchName.equals(n.getPitchName())) {
				return n;
			}
		}
		return null;
	}
	
	public static List<String> allNotes() {
		List<String> allNotes = new ArrayList<String>();
		for (Notes n : values()) {
			allNotes.add(n.getPitchName());
		}
		return allNotes;
	}
	
	@Override
	public String toString() {
		return this.pitchName;
	}
	
	public String getResourceLocation(String insturmentLocation) {
		return this.resourceLocation.replaceAll("-", insturmentLocation);
	}
	
	public int getPos() {
		return pos;
	}
	
	public String getPitchName() {
		return pitchName;
	}
	
	public float getPitchIn() {
		return pitchIn;
	}
	
	public float getPitch() {
		return pitchOut;
	}
}
