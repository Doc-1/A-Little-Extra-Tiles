package com.ltphoto.config;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import org.apache.logging.log4j.Level;

import com.ltphoto.CommonProxy;

import net.minecraftforge.common.config.Configuration;

public class Config {

    private static final String CATEGORY_GENERAL = "general";

    private static boolean allowURL = true;
    private static String blockedSites = "www.url.com,www.url2.com";
    private static int maxPixelAmount = 9604;
    private static int colorAccuracy = 0;
    private static int maxPixelText = 250000;
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

	public static boolean isAllowURL() {
		return allowURL;
	}

	public static String getBlockedSites() {
		return blockedSites;
	}

	public static int getMaxPixelAmount() {
		return maxPixelAmount;
	}

	public static int getColorAccuracy() {
		return colorAccuracy;
	}
	
	public static int getMaxPixelText() {
		return maxPixelText;
	}

	public static boolean isColorAccuracy() {
		if(colorAccuracy != 0) {
			return true;
		}
		return false;
	}

}
