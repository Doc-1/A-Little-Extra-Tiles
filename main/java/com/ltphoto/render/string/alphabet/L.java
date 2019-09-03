package com.ltphoto.render.string.alphabet;

import com.ltphoto.render.string.Char;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;

public class L extends Char {
	
	public L(Facing facing, BufferBuilder bufferbuilder, RenderWorldLastEvent event) {
		
	}
	
	public L(Vec3d start, Facing facing, double scale, BufferBuilder bufferbuilder, RenderWorldLastEvent event) {
		this.scale = scale;
		this.facing = facing;
		this.bufferbuilder = bufferbuilder;
		d0 = player.lastTickPosX + (player.posX - player.lastTickPosX) * event.getPartialTicks();
		d1 = player.lastTickPosY + (player.posY - player.lastTickPosY) * event.getPartialTicks();
		d2 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * event.getPartialTicks();
		
		morph();
		printChar(start);
	}
	
	@Override
	public void printChar(Vec3d start) {
		x = start.x;
		y = start.y;
		z = start.z;
		
		bufferbuilder.pos((x) - d0 - 0.001, (y) - d1 - 0.001, (z) - d2 - 0.001).color(red, green, blue, 0.0F).endVertex();
		bufferbuilder.pos((x + 0.08) - d0 - 0.001, (y) - d1 - 0.001, (z) - d2 - 0.001).color(red, green, blue, alpha).endVertex();
		bufferbuilder.pos((x) - d0 - 0.001, (y) - d1 - 0.001, (z) - d2 - 0.001).color(red, green, blue, 0.0F).endVertex();
		bufferbuilder.pos((x) - d0 - 0.001, (y) - d1 - 0.001, (z + 0.05) - d2 - 0.001).color(red, green, blue, alpha).endVertex();
		bufferbuilder.pos((x) - d0 - 0.001, (y) - d1 - 0.001, (z + 0.05) - d2 - 0.001).color(red, green, blue, 0.0F).endVertex();
	}
	
}
