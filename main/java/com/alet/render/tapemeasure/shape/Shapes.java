package com.alet.render.tapemeasure.shape;

import com.creativemd.creativecore.common.gui.GuiRenderHelper;
import com.creativemd.creativecore.common.gui.Rect;
import com.creativemd.creativecore.common.gui.client.style.Style;
import com.creativemd.littletiles.common.util.grid.LittleGridContext;

import net.minecraft.client.Minecraft;
import net.minecraft.util.math.Vec3d;

public abstract class Shapes {

	Vec3d pos;
	Vec3d pos2;
	int contextSize;
	public static Minecraft mc = Minecraft.getMinecraft();

	public Shapes(Vec3d p, Vec3d p2, int contextSz) {
		pos = p;
		pos2 = p2;
		contextSize = contextSz;
	}
	
	public Shapes(double x1, double y1, double z1, double x2, double y2, double z2, int contextSize) {
		this(new Vec3d(x1, y1, z1), new Vec3d(x2, y2, z2), contextSize);
	}
	
	public static double getDistence(double pos_1, double pos_2, int contextSize) {
		LittleGridContext context = LittleGridContext.get(contextSize);
		
		double contDecimal = 1D / context.size;
		double distence = (Math.abs(pos_1 - pos_2)) + contDecimal;
		
		return distence;
	}
	
	private static double cleanDouble(double doub) {
		String clean = String.format("%.2f", doub);
		doub = Double.parseDouble(clean);
		return doub;
	}
	
	protected abstract void calculateDistance(Vec3d pos, Vec3d pos2, int contextSize);
	
	public void calculateDistance() {
		calculateDistance(pos, pos2, contextSize);
	}
}
