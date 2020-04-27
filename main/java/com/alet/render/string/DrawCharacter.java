package com.alet.render.string;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;

public class DrawCharacter {
	
	public Vec3d start;
	public Facing facing;
	public double scale = 1;
	
	public double x;
	public double y;
	public double z;
	
	public float red;
	public float green = 1.0F;
	public float blue;
	public float alpha = 1.0F;
	
	public int count;
	
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
	
	public void morph(ArrayList<Vec3d> vecs) {
		ArrayList<Double> pos_1 = new ArrayList<Double>();
		ArrayList<Double> pos_2 = new ArrayList<Double>();
		ArrayList<Double> pos_3 = new ArrayList<Double>();
		
		switch (facing) {
		case NORTH:
			
			break;
		case EAST:
			for (int i = 0; i < vecs.size(); i++) {
				pos_1.add(vecs.get(i).z);
				pos_2.add(vecs.get(i).y);
				pos_3.add(vecs.get(i).x*-1);
			}
			break;
		case SOUTH:
			
			break;
		case WEST:
			for (int i = 0; i < vecs.size(); i++) {
				pos_1.add(vecs.get(i).z);
				pos_2.add(vecs.get(i).y);
				pos_3.add(vecs.get(i).x*-1);
			}
			break;
		case UP:
			for (int i = 0; i < vecs.size(); i++) {
				pos_1.add(vecs.get(i).x);
				pos_2.add(vecs.get(i).y);
				pos_3.add(vecs.get(i).z);
			}
			break;
		case DOWN:
			
			break;
		default:
			break;
		}
		printChar(pos_1, pos_2, pos_3, vecs.size());
	}
	
	public void printChar(ArrayList<Double> pos_1, ArrayList<Double> pos_2, ArrayList<Double> pos_3, int size) {
		x = start.x;
		y = start.y;
		z = start.z;
		
		for (int i = 0; i < size; i++) {
			if (i == 0) {
				bufferbuilder.pos((x + pos_1.get(i)) - d0 - 0.001, (y + pos_2.get(i)) - d1 - 0.001, (z + pos_3.get(i)) - d2 - 0.001).color(red, green, blue, 0.0F).endVertex();
			} else {
				bufferbuilder.pos((x + pos_1.get(i)) - d0 - 0.001, (y + pos_2.get(i)) - d1 - 0.001, (z + pos_3.get(i)) - d2 - 0.001).color(red, green, blue, alpha).endVertex();
			}
			
		}
		
	}
}
