package com.ltphoto.render.string.alphabet;

import java.util.ArrayList;

import com.ltphoto.render.string.Char.Facing;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;

public class Alphabet {
	
	public BufferBuilder bufferbuilder;
	public RenderWorldLastEvent event;
	public int numOfVecs;
	
	public Alphabet(BufferBuilder bufferbuilder, RenderWorldLastEvent event) {
		this.bufferbuilder = bufferbuilder;
		this.event = event;
	}
	
	public ArrayList<Vec3d> line(double startX, double startY, double startZ) {
		ArrayList<Vec3d> vec = new ArrayList<Vec3d>(1);
		vec.add(new Vec3d(0 + startX, 0 + startY, 0 + startZ));
		vec.add(new Vec3d(0.08 + startX, 0 + startY, 0 + startZ));
		return vec;
	}
	
	public ArrayList<Vec3d> halfCurve(double startX, double startY, double startZ, boolean mirror) {
		ArrayList<Vec3d> vec = new ArrayList<Vec3d>(1);
		if (mirror) {
			vec.add(new Vec3d(0.075 + startX, 0 + startY, -0.015 + startZ));
			vec.add(new Vec3d(0.06 + startX, 0 + startY, -0.025 + startZ));
			vec.add(new Vec3d(0.045 + startX, 0 + startY, -0.015 + startZ));
			vec.add(new Vec3d(0.04 + startX, 0 + startY, 0 + startZ));
		} else {
			vec.add(new Vec3d(0.075 + startX, 0 + startY, 0.033 + startZ));
			vec.add(new Vec3d(0.06 + startX, 0 + startY, 0.045 + startZ));
			vec.add(new Vec3d(0.045 + startX, 0 + startY, 0.033 + startZ));
			vec.add(new Vec3d(0.04 + startX, 0 + startY, 0 + startZ));
		}
		return vec;
	}
	
	public void fullCurve(double startX, double startY, double startZ) {
		
		//bufferbuilder.pos((minX) - d0 - 0.001, (minY) - d1 - 0.001, (minZ + 0.05) - d2 - 0.001).color(red, green, blue, alpha).endVertex();
		
		/* vec.add(new Vec3d(0.08 + startX, 0 + startY, 0 + startZ));
		 * vec.add(new Vec3d(0.073 + startX, 0 + startY, 0.025 + startZ));
		 * vec.add(new Vec3d(0.055 + startX, 0 + startY, 0.038 + startZ));
		 * vec.add(new Vec3d(0.04 + startX, 0 + startY, 0.045 + startZ));
		 * vec.add(new Vec3d(0.025 + startX, 0 + startY, 0.038 + startZ));
		 * vec.add(new Vec3d(0.007 + startX, 0 + startY, 0.025 + startZ));
		 * vec.add(new Vec3d(0 + startX, 0 + startY, 0 + startZ)); */
	}
	
	public void setBuffer(BufferBuilder bufferbuilder) {
		this.bufferbuilder = bufferbuilder;
	}
	
	public void setEvent(RenderWorldLastEvent event) {
		this.event = event;
	}
	
	public void character(char input, Vec3d start, float red, float green, float blue, float alpha) {
		EntityPlayer player = Minecraft.getMinecraft().player;
		
		double minX = start.x;
		double minY = start.y;
		double minZ = start.z;
		
		double d0 = player.lastTickPosX + (player.posX - player.lastTickPosX) * event.getPartialTicks();
		double d1 = player.lastTickPosY + (player.posY - player.lastTickPosY) * event.getPartialTicks();
		double d2 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * event.getPartialTicks();
		
		switch (input) {
		case 'A':
			A a = new A(start, Facing.UP, 1, bufferbuilder, event);
			break;
		case 'B':
			B b = new B(start, Facing.UP, 1, bufferbuilder, event);
			break;
		case 'C':
			C c = new C(start, Facing.UP, 1, bufferbuilder, event);
			break;
		case 'D':
			break;
		case 'E':
			E e = new E(start, Facing.UP, 1, bufferbuilder, event);
			break;
		case 'F':
			break;
		case 'G':
			break;
		case 'H':
			break;
		case 'I':
			I i = new I(start, Facing.UP, 1, bufferbuilder, event);
			break;
		case 'J':
			break;
		case 'K':
			K k = new K(start, Facing.UP, 1, bufferbuilder, event);
			break;
		case 'L':
			L l = new L(start, Facing.UP, 1, bufferbuilder, event);
			break;
		case 'M':
			break;
		case 'N':
			break;
		case 'O':
			O o = new O(start, Facing.UP, 1, bufferbuilder, event);
			break;
		case 'Q':
			break;
		case 'R':
			break;
		case 'S':
			break;
		case 'T':
			T t = new T(start, Facing.UP, 1, bufferbuilder, event);
			break;
		case 'U':
			break;
		case 'V':
			break;
		case 'W':
			break;
		case 'X':
			break;
		case 'Y':
			break;
		case 'Z':
			break;
		case ' ':
			break;
		default:
		
		}
		
	}
}
