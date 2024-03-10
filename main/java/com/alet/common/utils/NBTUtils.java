package com.alet.common.utils;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.util.Constants.NBT;

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
        return new AxisAlignedBB(nbt.getDouble("x_min"), nbt.getDouble("y_min"), nbt.getDouble("z_min"), nbt.getDouble(
            "x_max"), nbt.getDouble("y_max"), nbt.getDouble("z_max"));
    }
    
    public static NBTTagList writeDoubleArrayFrom(double... doubles) {
        return writeDoubleArray(doubles);
    }
    
    public static NBTTagList writeDoubleArray(double[] doubleArr) {
        NBTTagList list = new NBTTagList();
        for (int i = 0; i < doubleArr.length; i++) {
            NBTTagCompound n = new NBTTagCompound();
            n.setDouble("", doubleArr[i]);
            list.appendTag(n);
        }
        return list;
    }
    
    public static double[] readDoubleArray(NBTTagCompound nbt, String key) {
        NBTTagList list = nbt.getTagList(key, NBT.TAG_COMPOUND);
        double[] array = new double[list.tagCount()];
        for (int i = 0; i < list.tagCount(); i++)
            array[i] = ((NBTTagDouble) list.get(i)).getDouble();
        
        return array;
    }
}
