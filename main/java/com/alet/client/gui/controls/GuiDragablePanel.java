package com.alet.client.gui.controls;

import com.creativemd.creativecore.common.gui.GuiRenderHelper;
import com.creativemd.creativecore.common.gui.client.style.Style;
import com.creativemd.creativecore.common.gui.container.GuiParent;
import com.creativemd.creativecore.common.utils.math.SmoothValue;

import net.minecraft.util.math.MathHelper;

public class GuiDragablePanel extends GuiParent {
	public SmoothValue scrolledX = new SmoothValue(200);
	public SmoothValue scrolledY = new SmoothValue(200);
	public SmoothValue zoom = new SmoothValue(200, 1);
	public double startScrollX;
	public double startScrollY;
	public int dragX;
	public int dragY;
	public boolean scrolling;
	
	public boolean selected = false;
	
	public int maxWidth;
	public int maxHeight;
	
	public GuiDragablePanel(String name, int x, int y, int width, int height, int maxWidth, int maxHeight) {
		super(name, x, y, width, height);
		this.maxWidth = maxWidth;
		this.maxHeight = maxHeight;
	}
	
	@Override
	public float getScaleFactor() {
		return (float) zoom.current();
	}
	
	@Override
	public double getOffsetX() {
		return -scrolledX.current();
	}
	
	@Override
	public double getOffsetY() {
		return -scrolledY.current();
	}
	
	@Override
	public boolean mouseScrolled(int x, int y, int scrolled) {
		if (!super.mouseScrolled(x, y, scrolled))
			zoom.set(MathHelper.clamp(zoom.aimed() + scrolled * 0.2, 0.1, 2));
		return true;
	}
	
	@Override
	public boolean mousePressed(int x, int y, int button) {
		if (button == 2) {
			zoom.set(1);
			scrolledX.set(0);
			scrolledY.set(0);
			return true;
		}
		if (!super.mousePressed(x, y, button)) {
			scrolling = true;
			dragX = x;
			dragY = y;
			startScrollX = scrolledX.current();
			startScrollY = scrolledY.current();
		}
		return true;
	}
	
	@Override
	public void mouseMove(int x, int y, int button) {
		if (!selected && scrolling) {
			scrolledX.set(MathHelper.clamp(dragX - x + startScrollX, -40, maxWidth));
			scrolledY.set(MathHelper.clamp(dragY - y + startScrollY, -40, maxHeight));
		}
		super.mouseMove(x, y, button);
	}
	
	@Override
	protected void renderContent(GuiRenderHelper helper, Style style, int width, int height) {
		scrolledX.tick();
		scrolledY.tick();
		zoom.tick();
		
	}
	
	@Override
	public void mouseReleased(int x, int y, int button) {
		super.mouseReleased(x, y, button);
		scrolling = false;
	}
}
