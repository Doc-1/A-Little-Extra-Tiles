package com.ltphoto.photo;

import java.io.*;
import java.util.ArrayList;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import javax.imageio.ImageIO;

import com.creativemd.creativecore.common.utils.mc.ColorUtils;
import com.creativemd.littletiles.common.tiles.vec.LittleTileBox;
import com.creativemd.littletiles.common.tiles.vec.LittleTileVec;
import com.creativemd.littletiles.LittleTiles;
import com.creativemd.littletiles.common.blocks.BlockLTColored;
import com.creativemd.littletiles.common.tiles.LittleTileBlockColored;
import com.creativemd.littletiles.common.tiles.combine.BasicCombiner;
import com.creativemd.littletiles.common.tiles.preview.LittlePreviews;
import com.creativemd.littletiles.common.tiles.preview.LittleTilePreview;
import com.creativemd.littletiles.common.utils.grid.LittleGridContext;
import com.ltphoto.container.SubContainerPhotoImport;

import net.minecraft.item.ItemStack;


public class PhotoReader {

	// Coordinates, pretty sure you can z
	private static int x = 0;
	private static int z = 0;
	
	// Color, RGBA
	private static int r = 0;
	private static int g = 0;
	private static int b = 0;
	private static int a = 255;
	
	public static void printPhoto(String fileName) throws IOException {
		File file = new File(fileName);
		BufferedImage image = ImageIO.read(file);
		int width = image.getWidth();
		int height = image.getHeight();
		int area = width * height;
		int pixelCount = 0;
		ItemStack stack = null; // create empty advanced recipe itemstack
		String bBox = "";
		String output = "";
		
		LittleGridContext context = LittleGridContext.get(); // the context you want to use, LittleGridContext.get() returns the default one (which is 16 in most cases)
		List<LittleTilePreview> tiles = new ArrayList<>();

		stack = new ItemStack(LittleTiles.recipeAdvanced);
		LittlePreviews previews = new LittlePreviews(context);
		
		
		
		
		for(; width>x; x++) {

			if(pixelCount == area) {
				break;
			}
			pixelCount += 1;

			// Getting pixel color by position x and y 
			Color c = new Color(image.getRGB(x,z));

			r = c.getRed();
			g  = c.getGreen();
			b  = c.getBlue();
			System.out.println(x+" "+z);
			System.out.println(r+" "+g+" "+b);
			
			if (a > 0) { // no need to add transparent tiles
				int color = ColorUtils.RGBAToInt(r, g, b, a); // Converts the rgba to color
				LittleTileBlockColored tile = new LittleTileBlockColored(LittleTiles.coloredBlock, BlockLTColored.EnumType.clean.ordinal(), color);
				tile.box = new LittleTileBox(new LittleTileVec(x, 1, z));
				tiles.add(tile.getPreviewTile());
			}
			
			BasicCombiner.combinePreviews(tiles); // minimize tiles used
			
			for (LittleTilePreview tile : tiles) {
				previews.addWithoutCheckingPreview(tile);
			}
			LittleTilePreview.savePreview(previews, stack); // save tiles to itemstacks
			
			//Goes down to the next line of pixels
			
			if(x == (width-1)) {
				z += 1;
				x = -1;
			}
		}
		System.out.println(stack.getTagCompound());
	}

}