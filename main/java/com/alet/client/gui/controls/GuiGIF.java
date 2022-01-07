package com.alet.client.gui.controls;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import com.alet.ALET;
import com.alet.client.gui.controls.thread.ThreadCollectFrames;
import com.creativemd.creativecore.common.gui.GuiControl;
import com.creativemd.creativecore.common.gui.GuiRenderHelper;
import com.creativemd.creativecore.common.gui.client.style.Style;

import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;

public class GuiGIF extends GuiControl {
	
	public String gif;
	Thread thread;
	
	public List<BufferedImage> textures = new ArrayList<BufferedImage>();
	public List<ResourceLocation> locations = new ArrayList<ResourceLocation>();
	public int tick = 0;
	
	public GuiGIF(String name, String gif, int x, int y, double scale) {
		super(name, x, y, 0, 0);
		
		this.gif = gif;
		ThreadCollectFrames collectFrames = new ThreadCollectFrames(textures);
		thread = new Thread(collectFrames);
		thread.start();
	}
	
	@Override
	protected void renderContent(GuiRenderHelper helper, Style style, int width, int height) {
		
		if (!locations.isEmpty()) {
			this.mc.getTextureManager().bindTexture(locations.get(tick));
			GuiIngame.drawModalRectWithCustomSizedTexture(0, 0, 0, 0, this.width, this.height / 3, this.width
			        / 3, this.height / 3);
			
			if (tick >= textures.size() - 1)
				tick = 0;
			else
				tick++;
		} else if (!thread.isAlive()) {
			for (BufferedImage f : textures) {
				DynamicTexture frame = new DynamicTexture(f);
				ResourceLocation location = mc.getTextureManager().getDynamicTextureLocation(ALET.MODID, frame);
				locations.add(location);
			}
		}
	}
	
	@Override
	public void onClosed() {
		for (ResourceLocation frame : this.locations) {
			this.mc.getTextureManager().deleteTexture(frame);
		}
		thread.interrupt();
		super.onClosed();
	}
}
