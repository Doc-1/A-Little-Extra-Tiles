package com.ltphoto.render.string;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;

public class Char {
	
	public Facing facing;
	public double scale = 1;
	
	public double x;
	public double y;
	public double z;
	
	public float red;
	public float green = 1.0F;
	public float blue;
	public float alpha = 1.0F;
	
	public BufferBuilder bufferbuilder;
	public RenderWorldLastEvent event;
	public EntityPlayer player = Minecraft.getMinecraft().player;
	
	public double d0;
	public double d1;
	public double d2;
	
	public enum Facing {
		NORTH, EAST, SOUTH, WEST, UP, DOWN
	}
	
	public void setBuffer(BufferBuilder bufferbuilder) {
		this.bufferbuilder = bufferbuilder;
	}
	
	public void setEvent(RenderWorldLastEvent event) {
		this.event = event;
	}
	
	public void morph() {
		switch (facing) {
		case NORTH:
			
			break;
		case EAST:
			
			break;
		case SOUTH:
			
			break;
		case WEST:
			
			break;
		case UP:
			
			break;
		case DOWN:
			
			break;
		default:
			break;
		}
	}
	
	public void printChar(Vec3d start) {
	}
}
