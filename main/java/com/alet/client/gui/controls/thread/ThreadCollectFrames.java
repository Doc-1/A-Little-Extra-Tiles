package com.alet.client.gui.controls.thread;

import java.awt.image.BufferedImage;
import java.util.List;

import com.madgag.gif.fmsware.GifDecoder;

import net.minecraft.client.Minecraft;

public class ThreadCollectFrames implements Runnable {
	List<BufferedImage> textures;
	Minecraft mc = Minecraft.getMinecraft();
	
	public ThreadCollectFrames(List<BufferedImage> textures) {
		this.textures = textures;
	}
	
	@Override
	public void run() {
		GifDecoder d = new GifDecoder();
		d.read(getClass().getClassLoader().getResource("assets/alet/gif/box.gif").toString());
		
		int n = d.getFrameCount();
		for (int i = 0; i < n; i++) {
			BufferedImage f = d.getFrame(i); // frame i
			textures.add(f);
		}
	}
	
}
