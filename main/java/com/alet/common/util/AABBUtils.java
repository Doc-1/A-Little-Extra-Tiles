package com.alet.common.util;

import net.minecraft.util.math.AxisAlignedBB;

public class AABBUtils {
    
    public static AxisAlignedBB setMinX(AxisAlignedBB box, double minX) {
        return new AxisAlignedBB(minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ);
    }
    
    public static AxisAlignedBB setMaxX(AxisAlignedBB box, double maxX) {
        return new AxisAlignedBB(box.minX, box.minY, box.minZ, maxX, box.maxY, box.maxZ);
    }
    
    public static AxisAlignedBB setMinY(AxisAlignedBB box, double minY) {
        return new AxisAlignedBB(box.minX, minY, box.minZ, box.maxX, box.maxY, box.maxZ);
    }
    
    public static AxisAlignedBB setMaxY(AxisAlignedBB box, double maxY) {
        return new AxisAlignedBB(box.minX, box.minY, box.minZ, box.maxX, maxY, box.maxZ);
    }
    
    public static AxisAlignedBB setMinZ(AxisAlignedBB box, double minZ) {
        return new AxisAlignedBB(box.minX, box.minY, minZ, box.maxX, box.maxY, box.maxZ);
    }
    
    public static AxisAlignedBB setMaxZ(AxisAlignedBB box, double maxZ) {
        return new AxisAlignedBB(box.minX, box.minY, box.minZ, box.maxX, box.maxY, maxZ);
    }
}
