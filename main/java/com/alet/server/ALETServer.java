package com.alet.server;

import com.alet.common.measurment.management.LittleMeasurementsList;
import com.creativemd.littletiles.server.LittleTilesServer;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(Side.SERVER)
public class ALETServer extends LittleTilesServer {
    
    static {
        System.out.println("STATIC INIT - Should print even if Forge fails later.");
    }
    
    @Override
    @EventHandler
    public void loadSidePre() {
        System.out.println("hello");
        
    }
    
    @Override
    @EventHandler
    public void loadSide() {
        System.out.println("hello");
        MinecraftForge.EVENT_BUS.register(LittleMeasurementsList.class);
    }
    
    @Override
    @EventHandler
    public void loadSidePost() {
        System.out.println("hello");
        
    }
}
