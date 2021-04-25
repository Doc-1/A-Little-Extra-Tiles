package com.alet.tiles;

import com.creativemd.littletiles.common.tile.math.vec.LittleAbsoluteVec;
import com.creativemd.littletiles.common.util.grid.LittleGridContext;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3d;

public class SelectLittleTile {
	
	public double boxMinX;
	public double boxMinY;
	public double boxMinZ;
	
	public double boxMaxX;
	public double boxMaxY;
	public double boxMaxZ;
	
	public double centerX;
	public double centerY;
	public double centerZ;
	public Vec3d center;
	
	public Vec3d corner_1;
	public Vec3d corner_2;
	public Vec3d corner_3;
	public Vec3d corner_4;
	public Vec3d corner_5;
	public Vec3d corner_6;
	public Vec3d corner_7;
	public Vec3d corner_8;
	
	public Vec3d boxCorner_1;
	public Vec3d boxCorner_2;
	
	private double gridOffSet;
	public LittleGridContext context;
	
	public SelectLittleTile() {
		
	}
	
	public SelectLittleTile(Vec3d center, LittleGridContext con) {
		context = con;
		gridOffSet = context.pixelSize;
		
		boxMinX = center.x;
		boxMinY = center.y;
		boxMinZ = center.z;
		
		boxMaxX = boxMinX + gridOffSet;
		boxMaxY = boxMinY + gridOffSet;
		boxMaxZ = boxMinZ + gridOffSet;
		
		boxCorner_1 = new Vec3d(boxMaxX, boxMaxY, boxMaxZ);
		boxCorner_2 = new Vec3d(boxMinX, boxMinY, boxMinZ);
		
		getCenter();
		getCorners();
	}
	
	public SelectLittleTile(LittleAbsoluteVec corner, LittleGridContext con, EnumFacing side) {
		context = con;
		gridOffSet = context.pixelSize;
		
		boxMinY = corner.getPosY();
		boxMinX = corner.getPosX();
		boxMinZ = corner.getPosZ();
		
		boxMaxY = boxMinY + gridOffSet;
		boxMaxX = boxMinX + gridOffSet;
		boxMaxZ = boxMinZ + gridOffSet;
		
		boxCorner_1 = new Vec3d(boxMaxX, boxMaxY, boxMaxZ);
		boxCorner_2 = new Vec3d(boxMinX, boxMinY, boxMinZ);
		
		getCenter();
		getCorners();
	}
	
	public void getCorners() {
		corner_1 = new Vec3d(boxMinX, boxMinY, boxMinZ);
		corner_2 = new Vec3d(boxMinX + gridOffSet, boxMinY, boxMinZ);
		corner_3 = new Vec3d(boxMinX + gridOffSet, boxMinY, boxMinZ + gridOffSet);
		corner_4 = new Vec3d(boxMinX, boxMinY, boxMinZ + gridOffSet);
		
		corner_5 = new Vec3d(boxMaxX, boxMaxY, boxMaxZ);
		corner_6 = new Vec3d(boxMaxX - gridOffSet, boxMaxY, boxMaxZ);
		corner_7 = new Vec3d(boxMaxX - gridOffSet, boxMaxY, boxMaxZ - gridOffSet);
		corner_8 = new Vec3d(boxMaxX, boxMaxY, boxMaxZ - gridOffSet);
	}
	
	public void getCenter() {
		int doubleContext = context.size * 2;
		double centerOffSet = 1D / doubleContext;
		centerX = boxMinX + centerOffSet;
		centerY = boxMinY + centerOffSet;
		centerZ = boxMinZ + centerOffSet;
		center = new Vec3d(centerX, centerY, centerZ);
	}
	
}
