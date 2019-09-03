package com.ltphoto.render.string;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;

public interface CharInterface {
	
	public enum facing {
		NORTH, EAST, SOUTH, WEST, UP, DOWN
	}
	
	public void printChar(Vec3d start, BufferBuilder bufferbuilder, RenderWorldLastEvent event);
}
