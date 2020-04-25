package com.ltphoto.render.tapemeasure;

import java.util.ArrayList;
import java.util.List;

import com.creativemd.littletiles.common.tile.math.vec.LittleAbsoluteVec;
import com.creativemd.littletiles.common.util.grid.LittleGridContext;
import com.ltphoto.tiles.SelectLittleTile;

import net.minecraft.util.EnumFacing;

public class Measurement extends SelectLittleTile {
	
	public Shape shapeType = Shape.Line;
	public enum Shape {
		Box,
		Line,
		Circle
	}
	
	public Measurement(LittleAbsoluteVec firstPos, LittleGridContext context, EnumFacing facing) {
		super(firstPos, context, facing);
	}
	
	public void setShapeType(int sType) {
		Shape type = null;
		switch (sType) {
		case 0:
			type = type.Line;
			break;
		case 1:
			type = type.Box;
			break;
		case 2:
			type = type.Circle;
			break;
		case 3:
			
			break;
		default:
			
			break;
		}
		shapeType = type;
	}
	
}
