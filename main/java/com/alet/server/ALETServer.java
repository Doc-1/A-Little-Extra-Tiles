package com.alet.server;

import com.alet.common.measurment.management.LittleMeasurementsList;
import com.creativemd.littletiles.server.LittleTilesServer;

import net.minecraftforge.common.MinecraftForge;

public class ALETServer extends LittleTilesServer {
    
    @Override
    public void loadSidePre() {
        
    }
    
    @Override
    public void loadSide() {
        
    }
    
    @Override
    public void loadSidePost() {
        MinecraftForge.EVENT_BUS.register(new LittleMeasurementsList());
        
    }
}
