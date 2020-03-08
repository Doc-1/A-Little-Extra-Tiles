package com.ltphoto.photo;

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
import com.creativemd.littletiles.LittleTiles;
import com.creativemd.littletiles.common.block.BlockLTColored;
import com.creativemd.littletiles.common.tile.LittleTileColored;
import com.creativemd.littletiles.common.tile.combine.BasicCombiner;
import com.creativemd.littletiles.common.tile.math.box.LittleBox;
import com.creativemd.littletiles.common.tile.math.vec.LittleVec;
import com.creativemd.littletiles.common.tile.preview.LittlePreview;
import com.creativemd.littletiles.common.tile.preview.LittlePreviews;
import com.creativemd.littletiles.common.util.grid.LittleGridContext;
import com.ltphoto.LTPhoto;

import net.minecraft.item.ItemStack;
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
	
	/** @param img
	 *            Image from a website or directory
	 * @param height
	 *            height of the photo
	 * @param width
	 *            width of the photo
	 * @return
	 *         New resized photo */
	private static BufferedImage resize(BufferedImage img, int height, int width) {
		Image tmp = img.getScaledInstance(width, height, Image.SCALE_FAST);
		BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g2d = resized.createGraphics();
		g2d.drawImage(tmp, 0, 0, null);
		g2d.dispose();
		return resized;
	}
	
	public static int getPixelWidth(String input, boolean uploadOption) {
		InputStream in = null;
		File file = null;
		BufferedImage image = null;
		
		try {
			if (isBlock) {
				in = PhotoReader.class.getClassLoader().getResourceAsStream(input);
				image = ImageIO.read(in);
				isBlock = false;
			} else if (uploadOption) {
				in = load(input);
				image = ImageIO.read(in);
			} else {
				file = new File(input);
				image = ImageIO.read(file);
			}
		} catch (IOException e) {
			return 0;
		}
		return image.getWidth();
	}
	
	public static boolean imageExists(String input, boolean uploadOption) {
		InputStream in = null;
		File file = null;
		BufferedImage image = null;
		try {
			if (isBlock) {
				in = PhotoReader.class.getClassLoader().getResourceAsStream(input);
				image = ImageIO.read(in);
				isBlock = false;
			} else if (uploadOption) {
				in = load(input);
				image = ImageIO.read(in);
			} else {
				file = new File(input);
				image = ImageIO.read(file);
			}
		} catch (IOException e) {
			return false;
		}
		return true;
	}
	
	public static int getPixelLength(String input, boolean uploadOption) {
		InputStream in = null;
		File file = null;
		BufferedImage image = null;
		
		try {
			if (isBlock) {
				in = PhotoReader.class.getClassLoader().getResourceAsStream(input);
				image = ImageIO.read(in);
				isBlock = false;
			} else if (uploadOption) {
				in = load(input);
				image = ImageIO.read(in);
			} else {
				file = new File(input);
				image = ImageIO.read(file);
			}
		} catch (IOException e) {
			return 0;
		}
		return image.getHeight();
	}
	
	/** @param input
	 *            The path that the player gives from the SubGuiPhotoImport
	 * @param uploadOption
	 *            True or False if using URL
	 * @param grid
	 *            The context or grid size of the tile.
	 * @return
	 *         Returns the NBT data for the structure */
	public static NBTTagCompound photoToNBT(String input, boolean uploadOption, int grid) throws IOException {
		InputStream in = null;
		File file = null;
		BufferedImage image = null;
		int color = 0;
		ColorAccuracy roundedImage = new ColorAccuracy();
		int maxPixelAmount = LTPhoto.CONFIG.getMaxPixelAmount();
		
		try {
			
			if (isBlock) {
				in = PhotoReader.class.getClassLoader().getResourceAsStream(input);
				image = ImageIO.read(in);
				isBlock = false;
			} else if (uploadOption) {
				in = load(input);
				image = ImageIO.read(in);
			} else {
				file = new File(input);
				image = ImageIO.read(file);
			}
			
			if (isRescale) {
				if (!(scaleX < 1) || !(scaleY < 1)) {
					image = resize(image, scaleX, scaleY);
				}
				isRescale = false;
			}
			
			if (image != null) {
				int width = image.getWidth();
				int height = image.getHeight();
				
				if (((width * height) < maxPixelAmount)) {
					LittleGridContext context = LittleGridContext.get(grid);
					List<LittlePreview> tiles = new ArrayList<>();
					int expected = image.getWidth() * image.getHeight();
					for (int x = 0; x < image.getWidth(); x++) {
						for (int y = 0; y < image.getHeight(); y++) {
							
							if (LTPhoto.CONFIG.isColorAccuracy()) {
								roundedImage = new ColorAccuracy(image, x, image.getHeight() - y - 1);
								color = roundedImage.roundRGB();
							} else {
								color = image.getRGB(x, image.getHeight() - y - 1);
							}
							
							if (!ColorUtils.isInvisible(color)) { // no need to add transparent tiles
								LittleTileColored tile = new LittleTileColored(LittleTiles.coloredBlock, BlockLTColored.EnumType.clean.ordinal(), color);
								tile.box = new LittleBox(new LittleVec(x, y, 0));
								tiles.add(tile.getPreviewTile());
							}
						}
					}
					
					BasicCombiner.combinePreviews(tiles); // minimize tiles used
					
					ItemStack stack = new ItemStack(LittleTiles.recipeAdvanced); // create empty advanced recipe itemstack
					LittlePreviews previews = new LittlePreviews(context);
					for (LittlePreview tile : tiles) {
						previews.addWithoutCheckingPreview(tile);
					}
					LittlePreview.savePreview(previews, stack); // save tiles to itemstacks
					
					return stack.getTagCompound();
				}
			}
		} catch (IOException e) {
			
		} finally {
			IOUtils.closeQuietly(in);
			
		}
		isBlock = false;
		isRescale = false;
		return null;
	}
	
	public static void setScale(int x, int y) {
		isRescale = true;
		scaleX = x;
		scaleY = y;
	}
	
	public static void printBlock() {
		isBlock = true;
	}
	
}