package com.alet.client.gui.thread;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import com.madgag.gif.fmsware.GifDecoder;

import net.minecraft.client.Minecraft;

public class ThreadCollectFrames extends Thread {
    
    public List<Integer> delay = new ArrayList<Integer>();
    public List<BufferedImage> textures = new ArrayList<BufferedImage>();
    Minecraft mc = Minecraft.getMinecraft();
    public boolean finished = false;
    GifDecoder decoder = new GifDecoder();
    
    public ThreadCollectFrames(String gif) {
        start();
        decoder.read(getClass().getClassLoader().getResourceAsStream(gif));
    }
    
    public int getWidth() {
        
        return decoder.getImage().getWidth();
    }
    
    @Override
    public void run() {
        int n = decoder.getFrameCount();
        for (int i = 0; i < n; i++) {
            BufferedImage f = decoder.getFrame(i); // frame i
            textures.add(f);
            delay.add(decoder.getDelay(i) / 15);
        }
        finished = true;
    }
    
    public int getHeight() {
        return decoder.getImage().getHeight();
    }
    
}
