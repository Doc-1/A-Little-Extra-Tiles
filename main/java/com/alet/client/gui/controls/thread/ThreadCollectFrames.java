package com.alet.client.gui.controls.thread;

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
    public String gif;
    
    public ThreadCollectFrames(String gif) {
        start();
        this.gif = gif;
    }
    
    @Override
    public void run() {
        GifDecoder d = new GifDecoder();
        d.read(getClass().getClassLoader().getResourceAsStream(this.gif));
        int n = d.getFrameCount();
        for (int i = 0; i < n; i++) {
            BufferedImage f = d.getFrame(i); // frame i
            textures.add(f);
            delay.add(d.getDelay(i) / 15);
        }
        finished = true;
    }
    
}
