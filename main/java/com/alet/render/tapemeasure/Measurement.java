package com.alet.render.tapemeasure;

import java.util.ArrayList;
import java.util.List;

import com.alet.tiles.SelectLittleTile;
import com.creativemd.littletiles.common.tile.math.vec.LittleAbsoluteVec;
import com.creativemd.littletiles.common.util.grid.LittleGridContext;

import net.minecraft.util.EnumFacing;

public class Measurement extends SelectLittleTile {
	
	public Shape shapeType = Shape.Line;
	public enum Shape {
		Box,
		Line,
		Circle
	}
	
	public Measurement() {
		
	}
	
	public Measurement(LittleAbsoluteVec firstPos, LittleGridContext context, EnumFacing facing) {
		super(firstPos, context, facing);
	}
	
	public int getShapeType() {
		int type = 0;
		switch (shapeType) {
		case Box:
			type = 0;
			break;
		case Line:
			type = 1;
			break;
		case Circle:
			type = 2;
			break;
		default:
			type = 0;
			break;
		}
		return type;
	}
	
	public void setShapeType(int sType) {
		Shape type = Shape.Line;
		switch (sType) {
		case 0:
			type = type.Box;
			break;
		case 1:
			type = type.Line;
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
