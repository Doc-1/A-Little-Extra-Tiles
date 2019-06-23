package com.ltphoto.photo;

import java.io.*;
import java.awt.*;
import javax.imageio.ImageIO;

import com.creativemd.creativecore.common.utils.mc.ColorUtils;

import java.awt.image.BufferedImage;

public class PhotoReader {
	
	
	public static void readPhoto(String fileName) throws IOException {
		File file = new File("A:\\Gealan.png");
		BufferedImage image = ImageIO.read(file);
	   int width = image.getWidth();
	   int height = image.getHeight();
    	int area = width * height;
    	int pixelCount = 0;
    	int y = 0;
      String bBox = "";
      String output = "";
      
    	for(int x=0; width>x; x++) {
      
    		if(pixelCount == area) {
    			break;
    		}
    		pixelCount += 1;
		 
    		// Getting pixel color by position x and y 
    		Color c = new Color(image.getRGB(x,y));
		 
    		Integer redInt = new Integer(c.getRed());
    		Integer greenInt = new Integer(c.getGreen());
    		Integer blueInt = new Integer(c.getBlue());
		 
    		int colorDecimal = RGBAToInt(c);
		    
		 	int x1 = x;
		 	int z1 = y;
		 	
		 	int x2 = x+1;
		 	int z2 = y+1;
		 	
		 	bBox = "[I;" + x1 + ",0," + z1 + "," + x2 + ",1," + z2 + "]";
         output += "{bBox:"+bBox+",tile:{color:"+colorDecimal+",meta:0,block:\"littletiles:ltcoloredblock\",tID:\"BlockTileColored\"}},";
		   
		 	if(x == (width-1)) {
		 		y += 1;
		 		x = -1;
		 	}
         
    	}
      String output2 = "{tiles:["+output+"min:[I;0,0,0],size:[I;0,0,0],count:"+area+"}";
      System.out.println(output2);
	}
	
	public static int RGBAToInt(Color color) {
		return ((int) color.getAlpha() & 255) << 24 | ((int) color.getRed() & 255) << 16 | ((int) color.getGreen() & 255) << 8 | (int) color.getBlue() & 255;
	}
}


/*
{tiles:[{bBox:[I;15,0,15,16,1,16],tile:{color:-16776961,meta:0,block:"littletiles:ltcoloredblock",tID:"BlockTileColored"}},
{bBox:[I;15,0,14,16,1,15],tile:{color:-16711936,meta:0,block:"littletiles:ltcoloredblock",tID:"BlockTileColored"}},
{bBox:[I;15,0,13,16,1,14],tile:{color:-65536,meta:0,block:"littletiles:ltcoloredblock",tID:"BlockTileColored"}},
{bBox:[I;15,0,12,16,1,13],tile:{color:-16777216,meta:0,block:"littletiles:ltcoloredblock",tID:"BlockTileColored"}}],min:[I;15,0,12],size:[I;1,1,4],count:4}



*/