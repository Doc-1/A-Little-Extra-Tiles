package com.alet.server.level.data;

import com.alet.ALET;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;

public class MeasurementLevelSavedData extends WorldSavedData {
    private static final String DATA_NAME = ALET.MODID + "_MeasurmentData";
    
    public MeasurementLevelSavedData() {
        super(DATA_NAME);
    }
    
    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        // TODO Auto-generated method stub
        return null;
    }
    
    public static MeasurementLevelSavedData get(World world) {
        MapStorage storage = world.getPerWorldStorage();
        MeasurementLevelSavedData instance = (MeasurementLevelSavedData) storage.getOrLoadData(
            MeasurementLevelSavedData.class, DATA_NAME);
        
        if (instance == null) {
            instance = new MeasurementLevelSavedData();
            storage.setData(DATA_NAME, instance);
        }
        return instance;
    }
}
