package com.ltphoto;

import java.util.Arrays;
import java.util.List;

import com.creativemd.creativecore.common.config.api.CreativeConfig;

public class LTPhotoConfig {
	
	private static final String CATEGORY_GENERAL = "general";
	
	@CreativeConfig
	public boolean allowURL = true;
	@CreativeConfig
	public List<String> blockedSites = Arrays.asList("www.url.com", "www.url2.com");
	@CreativeConfig
	public int maxPixelAmount = 9604;
	@CreativeConfig
	@CreativeConfig.IntRange(min = 0, max = 239)
	public int colorAccuracy = 0;
	@CreativeConfig
	public int maxPixelText = 250000;
	
	public boolean isAllowURL() {
		return allowURL;
	}
	
	public List<String> getBlockedSites() {
		return blockedSites;
	}
	
	public int getMaxPixelAmount() {
		return maxPixelAmount;
	}
	
	public int getColorAccuracy() {
		return colorAccuracy;
	}
	
	public int getMaxPixelText() {
		return maxPixelText;
	}
	
	public boolean isColorAccuracy() {
		if (colorAccuracy != 0) {
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
	}
	
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
