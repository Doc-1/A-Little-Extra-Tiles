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

public class Config {

	private static File file = new File("config/LTPhoto.cfg");
	public static int maxPixelAmount;
	public static boolean allowURL;
	
	public static void checkConfigFile() {
		if(file.exists()) {
			readConfigFile();
		}else {
			createConfigFile();
		}
	}
	
	public static void createConfigFile() {
		
		System.out.println("Create------------------------------------------------------");
		try (OutputStream output = new FileOutputStream("config/LTPhoto.cfg")) {

			file.createNewFile();
			
            Properties prop = new Properties();
            
            prop.setProperty("maxPixelAmount", "16384");
			prop.setProperty("allowURL", "true");

            prop.store(output, "For the maxPixelAmount it is not recommend to increase this value. Larger image files will take a very long time to load.");

            System.out.println(prop);

        } catch (IOException io) {
            io.printStackTrace();
        }

		readConfigFile();
	}
	
	public static void readConfigFile() {

		System.out.println("Read----------------------------------------------------------");
		try {
		InputStream input = new FileInputStream("config/LTPhoto.cfg");
        Properties prop = new Properties();

        prop.load(input);

        maxPixelAmount = Integer.parseInt(prop.getProperty("maxPixelAmount"));
        allowURL = Boolean.parseBoolean(prop.getProperty("allowURL"));

		} catch (IOException ex) {
			ex.printStackTrace();
		}

	}
}
