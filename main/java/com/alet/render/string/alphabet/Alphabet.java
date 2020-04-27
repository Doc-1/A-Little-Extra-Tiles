package com.alet.render.string.alphabet;

import com.alet.render.string.DrawCharacter.Facing;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;

public class Alphabet {
	
	public BufferBuilder bufferbuilder;
	public RenderWorldLastEvent event;
	public int numOfVecs;
	public Facing facing;
	
	public Alphabet(BufferBuilder bufferbuilder, RenderWorldLastEvent event, Facing facing) {
		this.bufferbuilder = bufferbuilder;
		this.event = event;
		this.facing = facing;
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
			A a = new A(start, this.facing, 1, bufferbuilder, event);
			break;
		case 'B':
			B b = new B(start, this.facing, 1, bufferbuilder, event);
			break;
		case 'C':
			C c = new C(start, this.facing, 1, bufferbuilder, event);
			break;
		case 'D':
			break;
		case 'E':
			E e = new E(start, this.facing, 1, bufferbuilder, event);
			break;
		case 'F':
			break;
		case 'G':
			break;
		case 'H':
			break;
		case 'I':
			I i = new I(start, this.facing, 1, bufferbuilder, event);
			break;
		case 'J':
			break;
		case 'K':
			K k = new K(start, this.facing, 1, bufferbuilder, event);
			break;
		case 'L':
			L l = new L(start, this.facing, 1, bufferbuilder, event);
			break;
		case 'M':
			break;
		case 'N':
			break;
		case 'O':
			O o = new O(start, this.facing, 1, bufferbuilder, event);
			break;
		case 'Q':
			break;
		case 'R':
			break;
		case 'S':
			break;
		case 'T':
			T t = new T(start, this.facing, 1, bufferbuilder, event);
			break;
		case 'U':
			break;
		case 'V':
			break;
		case 'W':
			break;
		case 'X':
			X x = new X(start, this.facing, 1, bufferbuilder, event);
			break;
		case 'Y':
			Y y = new Y(start, this.facing, 1, bufferbuilder, event);
			break;
		case 'Z':
			Z z = new Z(start, this.facing, 1, bufferbuilder, event);
			break;
		default:
		
		}
		
	}
}
