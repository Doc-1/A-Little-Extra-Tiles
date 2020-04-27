package com.alet.render.string.alphabet;

import java.util.ArrayList;

import com.alet.render.string.DrawCharacter;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;

public class O extends DrawCharacter {
	
	public O(Facing facing, BufferBuilder bufferbuilder, RenderWorldLastEvent event) {
		
	}
	
	public O(Vec3d start, Facing facing, double scale, BufferBuilder bufferbuilder, RenderWorldLastEvent event) {
		this.scale = scale;
		this.facing = facing;
		this.bufferbuilder = bufferbuilder;
		this.start = start;
		d0 = player.lastTickPosX + (player.posX - player.lastTickPosX) * event.getPartialTicks();
		d1 = player.lastTickPosY + (player.posY - player.lastTickPosY) * event.getPartialTicks();
		d2 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * event.getPartialTicks();
		
		morph(vecList());
	}
	
	public ArrayList<Vec3d> vecList() {
		ArrayList<Vec3d> vec = new ArrayList<Vec3d>();
		
		vec.add(new Vec3d(0.04, 0, 0));
		vec.add(new Vec3d(0.055, 0, 0.002));
		vec.add(new Vec3d(0.065, 0, 0.006));
		vec.add(new Vec3d(0.075, 0, 0.012));
		vec.add(new Vec3d(0.08, 0, 0.025));
		vec.add(new Vec3d(0.075, 0, 0.038));
		vec.add(new Vec3d(0.065, 0, 0.044));
		vec.add(new Vec3d(0.055, 0, 0.048));
		vec.add(new Vec3d(0.04, 0, 0.05));
		vec.add(new Vec3d(0.025, 0, 0.048));
		vec.add(new Vec3d(0.015, 0, 0.044));
		vec.add(new Vec3d(0.005, 0, 0.038));
		vec.add(new Vec3d(0, 0, 0.025));
		vec.add(new Vec3d(0.005, 0, 0.012));
		vec.add(new Vec3d(0.015, 0, 0.006));
		vec.add(new Vec3d(0.025, 0, 0.002));
		vec.add(new Vec3d(0.04, 0, 0));
		
		return vec;
	}
	
}
