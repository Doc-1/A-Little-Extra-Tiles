package com.ltphoto.render.string;

import java.util.ArrayList;

import net.minecraft.util.math.Vec3d;

public class Alphabet {
	
	public char input;
	
	public Alphabet(char in) {
		input = in;
	}
	
	public ArrayList<Vec3d> charaters() {
		ArrayList<Vec3d> vec = new ArrayList<Vec3d>(10);
		switch (input) {
		
		case 'A':
			vec.add(new Vec3d(0, 0, 0.05));
			vec.add(new Vec3d(0.08, 0, 0.025));
			vec.add(new Vec3d(0, 0, 0));
			vec.add(new Vec3d(0.03, 0, 0.01));
			vec.add(new Vec3d(0.03, 0, 0.04));
		case 'B':
		
		case 'C':
		
		case 'D':
		
		case 'E':
		
		case 'F':
		
		case 'G':
		
		case 'H':
		
		case 'I':
		
		case 'J':
		
		case 'K':
		
		case 'L':
		
		case 'M':
		
		case 'N':
		
		case 'O':
		
		case 'Q':
		
		case 'R':
		
		case 'S':
		
		case 'T':
		
		case 'U':
		
		case 'V':
		
		case 'W':
		
		case 'X':
		
		case 'Y':
		
		case 'Z':
		
		default:
		
		}
		return vec;
	}
}
