package com.alet;

import java.util.Arrays;
import java.util.List;

import com.creativemd.creativecore.common.config.api.CreativeConfig;
import com.creativemd.creativecore.common.config.sync.ConfigSynchronization;

public class ALETConfig {
	
	private static final String CATEGORY_GENERAL = "general";

	//CLIENT Only read from Client. Server doesn't need to know about it
	//SERVER Only read from Server. Client doesn't need to know about it
	//UNIVERSAL Server will push it's config to Client
	@CreativeConfig(type = ConfigSynchronization.CLIENT)
	public static Client client = new Client();
	
	@CreativeConfig(type = ConfigSynchronization.CLIENT)
	public static TapeMeasure tapeMeasure = new TapeMeasure();
	
	//@CreativeConfig(type = ConfigSynchronization.SERVER)
	//public static Server server = new Server();
	
	//@CreativeConfig
	//public static Recipe recipe = new Recipe();
	
	@CreativeConfig(type = ConfigSynchronization.UNIVERSAL)
	public static Universal universal = new Universal();
	
	public static class Server {
		
	}
	
	public static class Universal {
		@CreativeConfig
		public boolean allowURL = true;
		//@CreativeConfig
		//public List<String> blockedSites = Arrays.asList("www.url.com", "www.url2.com");
		@CreativeConfig
		public int maxPixelAmount = 9604;
		@CreativeConfig
		public int maxPixelText = 250000;
	}
	
	public static class Client {
		@CreativeConfig
		@CreativeConfig.IntRange(min = 0, max = 239)
		public int colorAccuracy = 0;
	}
	
	public static class TapeMeasure {
		@CreativeConfig
		public List<String> measurementName = Arrays.asList("m", "mm", "cm", "in", "feet");
		@CreativeConfig
		public List<String> measurementEquation = Arrays.asList("M * 1", "M * 1000", "M * 100", "M * 39.3700787", "M * 3.28084");
	}
	
	/*
	public static class Recipe {
		@CreativeConfig
		@CreativeConfig.IntRange(min = 0, max = 239)
		public int colorAccuracy = 0;
		@CreativeConfig
		public List<String> measurementName = Arrays.asList("mm", "cm", "in");
		@CreativeConfig
		public List<String> measurementEquation = Arrays.asList("M", "M/10", "M*(1/25.4)");
		@CreativeConfig
		public List<String> measurementDisplay = Arrays.asList("Mmm", "Mcm", "Min");
	}
	*/
	public boolean isAllowURL() {
		return universal.allowURL;
	}
	
	public List<String> getBlockedSites() {
		return null;
	}
	
	public int getMaxPixelAmount() {
		return universal.maxPixelAmount;
	}
	
	public int getColorAccuracy() {
		return client.colorAccuracy;
	}
	
	public int getMaxPixelText() {
		return universal.maxPixelText;
	}
	
	public boolean isColorAccuracy() {
		if (client.colorAccuracy != 0) {
			return true;
		}
		return false;
	}
	
	/*
	public static void readConfig() {
	    Configuration cfg = CommonProxy.config;
	    try {
	        cfg.load();
	        initGeneralConfig(cfg);
	    } catch (Exception e1) {
	    
	    } finally {
	        if (cfg.hasChanged()) {
	            cfg.save();
	        }
	    }
	}o
	
	private static void initGeneralConfig(Configuration cfg) {
	    cfg.addCustomCategoryComment(CATEGORY_GENERAL, "General configuration");
	    // cfg.getBoolean() will get the value in the config if it is already specified there. If not it will create the value.
	    allowURL = cfg.getBoolean("allowURL", CATEGORY_GENERAL, allowURL, "Set to false if you do not want people to import from any website.");
	    blockedSites = cfg.getString("blockedSites", CATEGORY_GENERAL, blockedSites, "Any URL listed here will be blocked.(WIP not working)");
	    maxPixelAmount = cfg.getInt("maxPixelAmount", CATEGORY_GENERAL, maxPixelAmount, 1, 921600, "Strongly recommended not to increase this value. Large photos will take an extremely long time to compile.");
	    colorAccuracy = cfg.getInt("colorAccuracy", CATEGORY_GENERAL, colorAccuracy, 0, 239, "This will make it so colors will not be as accurate. For example, if set to 10 any color within 211-219 will go to 210.");
	    maxPixelText = cfg.getInt("maxPixelText", CATEGORY_GENERAL, maxPixelText, 1, 921600, "Increasing this value is not recommended. This value sets the limit on how many pixels the printing press can export.");
	
	}
	*/
}
