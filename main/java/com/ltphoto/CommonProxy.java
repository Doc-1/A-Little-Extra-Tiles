package com.ltphoto;

import java.io.File;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod.EventBusSubscriber
public class CommonProxy {

    // Config instance
    public static Configuration config;

    public void preInit(FMLPreInitializationEvent e) {
    	/*
        File directory = e.getModConfigurationDirectory();
        config = new Configuration(new File(directory.getPath(), "LTPhoto.cfg"));
        Config.readConfig();
        */
    }


    public void postInit(FMLPostInitializationEvent e) {
    	/*
        if (config.hasChanged()) {
            config.save();
        }
        */
    }


	public void loadSide() {
		
	}
}
