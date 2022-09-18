package com.alet.common.util;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;

public class NBTUtils {
    
    public static NBTTagCompound writeAABB(AxisAlignedBB aabb) {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setDouble("x_min", aabb.minX);
        nbt.setDouble("y_min", aabb.minY);
        nbt.setDouble("z_min", aabb.minZ);
        nbt.setDouble("x_max", aabb.maxX);
        nbt.setDouble("y_max", aabb.maxY);
        nbt.setDouble("z_max", aabb.maxZ);
        return nbt;
    }
    
    public static AxisAlignedBB readAABB(NBTTagCompound nbt) {
        return new AxisAlignedBB(nbt.getDouble("x_min"), nbt.getDouble("y_min"), nbt.getDouble("z_min"), nbt.getDouble("x_max"), nbt.getDouble("y_max"), nbt.getDouble("z_max"));
    }
}
