package com.ltphoto.font;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;

import com.creativemd.creativecore.common.utils.mc.ColorUtils;
import com.creativemd.littletiles.LittleTiles;
import com.creativemd.littletiles.common.blocks.BlockLTColored;
import com.creativemd.littletiles.common.tiles.LittleTileBlockColored;
import com.creativemd.littletiles.common.tiles.combine.BasicCombiner;
import com.creativemd.littletiles.common.tiles.preview.LittlePreviews;
import com.creativemd.littletiles.common.tiles.preview.LittleTilePreview;
import com.creativemd.littletiles.common.tiles.vec.LittleTileBox;
import com.creativemd.littletiles.common.tiles.vec.LittleTileVec;
import com.creativemd.littletiles.common.utils.grid.LittleGridContext;
import com.ltphoto.config.Config;
import com.ltphoto.photo.ColorAccuracy;
import com.ltphoto.photo.PhotoReader;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class FontReader {

	public static int fontSize;
	public static String fontType;
	
	public static BufferedImage fontToPhoto(String text, String fontType, int fontSize, int fontColor) {
		if(text.equalsIgnoreCase("When Redstone?")) {
			text = "In " + Math.random()*100 + " months";
		}
		Color color = new Color(fontColor, true);
        BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        Font font = new Font(fontType, Font.PLAIN, fontSize);
        g2d.setFont(font);
        FontMetrics fm = g2d.getFontMetrics();
        int width = fm.stringWidth(text)+10;
        int height = fm.getHeight();
        g2d.dispose();

        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        g2d = image.createGraphics();
        g2d.setFont(font);
        fm = g2d.getFontMetrics();
        g2d.setColor(color);
        g2d.drawString(text, 0, fm.getAscent());
        g2d.dispose();
        
        return image;
	}
	
	public static NBTTagCompound photoToNBT(String input, String font, int grid, int fontSize, int fontColor) throws IOException {
		InputStream in = null;
		File file = null;
		BufferedImage image = null;
		int color = 0;
		int maxPixelAmount = Config.getMaxPixelText();
		
		try {
			image = fontToPhoto(input, font, fontSize, fontColor);
			
			if (image != null) {
				int width = image.getWidth();
				int height = image.getHeight();
				
				if(((width*height)<maxPixelAmount)) {
					LittleGridContext context = LittleGridContext.get(grid);
					List<LittleTilePreview> tiles = new ArrayList<>();
					int expected = image.getWidth() * image.getHeight();
					for (int x = 0; x < image.getWidth(); x++) {
						for (int y = 0; y < image.getHeight(); y++) {
							
							color = image.getRGB(x, image.getHeight() - y - 1);
							
							if (!ColorUtils.isInvisible(color)) { // no need to add transparent tiles
								LittleTileBlockColored tile = new LittleTileBlockColored(LittleTiles.coloredBlock, BlockLTColored.EnumType.clean.ordinal(), color);
								tile.box = new LittleTileBox(new LittleTileVec(x, y, 0));
								tiles.add(tile.getPreviewTile());
							}
						}
					}

					BasicCombiner.combinePreviews(tiles); // minimize tiles used

					ItemStack stack = new ItemStack(LittleTiles.recipeAdvanced); // create empty advanced recipe itemstack
					LittlePreviews previews = new LittlePreviews(context);
					for (LittleTilePreview tile : tiles) {
						previews.addWithoutCheckingPreview(tile);
					}
					LittleTilePreview.savePreview(previews, stack); // save tiles to itemstacks
					
					return stack.getTagCompound();
				}
			}
		} finally {
			IOUtils.closeQuietly(in);
		}
		return null;
	}
	
}