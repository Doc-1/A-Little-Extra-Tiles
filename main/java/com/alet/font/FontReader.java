package com.alet.font;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;

import com.alet.ALET;
import com.creativemd.creativecore.common.utils.mc.ColorUtils;
import com.creativemd.littletiles.LittleTiles;
import com.creativemd.littletiles.common.block.BlockLittleDyeable;
import com.creativemd.littletiles.common.tile.LittleTileColored;
import com.creativemd.littletiles.common.tile.combine.BasicCombiner;
import com.creativemd.littletiles.common.tile.math.box.LittleBox;
import com.creativemd.littletiles.common.tile.math.vec.LittleVec;
import com.creativemd.littletiles.common.tile.preview.LittlePreview;
import com.creativemd.littletiles.common.tile.preview.LittlePreviews;
import com.creativemd.littletiles.common.util.grid.LittleGridContext;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class FontReader {
	
	public static int fontSize;
	public static String fontType;
	
	public static BufferedImage fontToPhoto(String text, String fontType, int fontSize, int fontColor, double rotation) {
		if (text.equalsIgnoreCase("When Redstone?")) {
			text = "Please read the FAQ in TeamCreative's Discord";
		}
		Color color = new Color(fontColor, true);
		
		BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		
		Graphics2D g2d = image.createGraphics();
		
		Font font = new Font(fontType, Font.PLAIN, fontSize);
		g2d.setFont(font);
		FontMetrics fm = g2d.getFontMetrics();
		int w = fm.stringWidth(text) + 10;
		int h = fm.getHeight();
		g2d.dispose();
		
		double rads = Math.toRadians(rotation);
		double sin = Math.abs(Math.sin(rads)), cos = Math.abs(Math.cos(rads));
		
		int newWidth = (int) Math.floor(w * cos + h * sin);
		int newHeight = (int) Math.floor(h * cos + w * sin);
		
		image = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
		
		g2d = image.createGraphics();
		AffineTransform at = new AffineTransform();
		at.translate((newWidth - w) / 2, (newHeight - h) / 2);
		
		int x = w / 2;
		int y = h / 2;
		
		at.rotate(rads, x, y);
		
		g2d.setTransform(at);
		g2d.setFont(font);
		fm = g2d.getFontMetrics();
		g2d.setColor(color);
		
		g2d.drawString(text, 0, fm.getAscent());
		
		g2d.dispose();
		
		return image;
	}
	
	public static NBTTagCompound photoToNBT(String input, String font, int grid, int fontSize, int fontColor, double rotation) throws IOException {
		InputStream in = null;
		File file = null;
		BufferedImage image = null;
		int color = 0;
		int maxPixelAmount = ALET.CONFIG.getMaxPixelText();
		
		try {
			image = fontToPhoto(input, font, fontSize, fontColor, rotation);
			//System.out.println("Image: " + image);
			if (image != null) {
				int width = image.getWidth();
				int height = image.getHeight();
				
				if (((width * height) < maxPixelAmount)) {
					LittleGridContext context = LittleGridContext.get(grid);
					List<LittlePreview> tiles = new ArrayList<>();
					int expected = image.getWidth() * image.getHeight();
					for (int x = 0; x < image.getWidth(); x++) {
						for (int y = 0; y < image.getHeight(); y++) {
							
							color = image.getRGB(x, image.getHeight() - y - 1);
							
							if (!ColorUtils.isInvisible(color)) { // no need to add transparent tiles
								LittleTileColored tile = new LittleTileColored(LittleTiles.dyeableBlock, BlockLittleDyeable.LittleDyeableType.CLEAN.getMetadata(), color);
								tile.setBox(new LittleBox(new LittleVec(x, y, 0)));
								tiles.add(tile.getPreviewTile());
							}
						}
					}
					
					ItemStack stack = new ItemStack(LittleTiles.recipeAdvanced); // create empty advanced recipe itemstack
					LittlePreviews previews = new LittlePreviews(context);
					BasicCombiner.combine(tiles);
					for (LittlePreview tile : tiles) {
						previews.addWithoutCheckingPreview(tile);
					}
					
					LittlePreview.savePreview(previews, stack); // save tiles to itemstacks
					
					return stack.getTagCompound();
				}
			}
		} finally {
			IOUtils.closeQuietly(in);
		}
		return null;
	}
	
}
