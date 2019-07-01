package com.ltphoto.photo;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;

import com.creativemd.creativecore.common.utils.mc.ColorUtils;
import com.creativemd.littletiles.common.tiles.vec.LittleTileBox;
import com.creativemd.littletiles.common.tiles.vec.LittleTileVec;
import com.creativemd.littletiles.LittleTiles;
import com.creativemd.littletiles.common.blocks.BlockLTColored;
import com.creativemd.littletiles.common.structure.premade.LittleStructurePremade;
import com.creativemd.littletiles.common.tiles.LittleTileBlockColored;
import com.creativemd.littletiles.common.tiles.combine.BasicCombiner;
import com.creativemd.littletiles.common.tiles.preview.LittlePreviews;
import com.creativemd.littletiles.common.tiles.preview.LittleTilePreview;
import com.creativemd.littletiles.common.utils.grid.LittleGridContext;
import com.google.common.base.Charsets;
import com.ltphoto.config.Config;
import com.ltphoto.container.SubContainerPhotoImport;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTTagCompound;


public class PhotoReader {

	private static int scaleX = 1;
	private static int scaleY = 1;
	private static boolean isRescale = false;
	private static boolean isBlock = false;
	
	private static InputStream load(String url) throws IOException {
		long requestTime = System.currentTimeMillis();
		URLConnection connection = new URL(url).openConnection();
		connection.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
		return connection.getInputStream();
	}
	
	public static NBTTagCompound photoToNBT(String input, boolean uploadOption, int grid) throws IOException {
		InputStream in = null;
		File file = null;
		BufferedImage image = null;
		int maxPixelAmount = Config.maxPixelAmount;
		
		if(isBlock) {
			in = PhotoReader.class.getClassLoader().getResourceAsStream(input);
		}else if(uploadOption) {
			 in = load(input);
		}else {
			 file = new File(input);
		}
		try {
			try {
				if(isBlock) {
					image = ImageIO.read(in);
				}else if(uploadOption) {
					image = ImageIO.read(in);
				}else {
					image = ImageIO.read(file);
				}
				
				if(isRescale) {
					if(!(scaleX < 1) || !(scaleY < 1)) {
						image = resize(image, scaleX, scaleY);
					}
				}
				
				if (image != null) {
					int width = image.getWidth();
					int height = image.getHeight();
					
					if(!((width*height)>maxPixelAmount)) {
						LittleGridContext context = LittleGridContext.get(grid);
						List<LittleTilePreview> tiles = new ArrayList<>();
						int expected = image.getWidth() * image.getHeight();
						for (int x = 0; x < image.getWidth(); x++) {
							for (int y = 0; y < image.getHeight(); y++) {
								int color = image.getRGB(x, image.getHeight() - y - 1);

								if (!ColorUtils.isInvisible(color)) { // no need to add transparent tiles
									LittleTileBlockColored tile = new LittleTileBlockColored(LittleTiles.coloredBlock, BlockLTColored.EnumType.clean.ordinal(), color);
									tile.box = new LittleTileBox(new LittleTileVec(x, y, 0));
									tiles.add(tile.getPreviewTile());
								}
							}
						}

						//BasicCombiner.combinePreviews(tiles); // minimize tiles used

						ItemStack stack = new ItemStack(LittleTiles.recipeAdvanced); // create empty advanced recipe itemstack
						LittlePreviews previews = new LittlePreviews(context);
						for (LittleTilePreview tile : tiles)
							previews.addWithoutCheckingPreview(tile);
						LittleTilePreview.savePreview(previews, stack); // save tiles to itemstacks
						isRescale = false;
						isBlock  = false;
						return stack.getTagCompound();
					}
				}
			} finally {
				IOUtils.closeQuietly(in);
			}
		} catch (IOException e) {
			
		}
		isBlock  = false;
		isRescale = false;
		return null;
	}
	
	public static void setScale(int x, int y) {
		isRescale = true;
		scaleX = x;
		scaleY = y;
	}
	
	public static void printBlock() {
		isBlock  = true;
	}
	
	private static BufferedImage resize(BufferedImage img, int height, int width) {
        Image tmp = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resized.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return resized;
    }

}