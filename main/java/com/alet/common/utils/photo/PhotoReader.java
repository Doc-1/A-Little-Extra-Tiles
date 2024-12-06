package com.alet.common.utils.photo;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;
import org.lwjgl.util.Color;

import com.alet.ALET;
import com.alet.common.gui.origins.SubGuiPhotoImport;
import com.creativemd.creativecore.common.gui.container.SubGui;
import com.creativemd.creativecore.common.gui.controls.gui.GuiProgressBar;
import com.creativemd.creativecore.common.utils.mc.ColorUtils;
import com.creativemd.creativecore.common.utils.type.HashMapList;
import com.creativemd.littletiles.LittleTiles;
import com.creativemd.littletiles.common.block.BlockLittleDyeable;
import com.creativemd.littletiles.common.tile.LittleTileColored;
import com.creativemd.littletiles.common.tile.math.box.LittleBox;
import com.creativemd.littletiles.common.tile.math.vec.LittleVec;
import com.creativemd.littletiles.common.tile.preview.LittleAbsolutePreviews;
import com.creativemd.littletiles.common.tile.preview.LittlePreview;
import com.creativemd.littletiles.common.tile.preview.LittlePreviews;
import com.creativemd.littletiles.common.util.grid.LittleGridContext;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;

public class PhotoReader {
    
    public static int scaleX = 1;
    public static int scaleY = 1;
    public static boolean isRescale = false;
    
    public static InputStream load(String url) throws IOException {
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
    public static BufferedImage resize(BufferedImage img, int height, int width) {
        Image tmp = img.getScaledInstance(width, height, Image.SCALE_FAST);
        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g2d = resized.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return resized;
    }
    
    public static BufferedImage getImage(String input, String uploadOption) {
        InputStream in = null;
        File file = null;
        BufferedImage image = null;
        try {
            if (uploadOption.equals("Print From Item") || uploadOption.equals("Print From Block")) {
                in = PhotoReader.class.getClassLoader().getResourceAsStream(input);
                image = ImageIO.read(in);
            } else if (uploadOption.equals("Print From URL")) {
                in = PhotoReader.load(input);
                image = ImageIO.read(in);
            } else if (uploadOption.equals("Print From File")) {
                file = new File(input);
                image = ImageIO.read(file);
            }
        } catch (IOException e) {
            
        } finally {
            IOUtils.closeQuietly(in);
        }
        return image;
    }
    
    public static int getPixelWidth(String input, String uploadOption) {
        BufferedImage image = null;
        
        image = getImage(input, uploadOption);
        
        return image.getWidth();
    }
    
    public static int getPixelLength(String input, String uploadOption) {
        BufferedImage image = null;
        
        image = getImage(input, uploadOption);
        
        return image.getHeight();
    }
    
    public static boolean photoExists(String input, String uploadOption) {
        BufferedImage image = null;
        image = getImage(input, uploadOption);
        return image != null;
    }
    
    /** @param input
     *            The path that the player gives from the SubGuiPhotoImport
     * @param uploadOption
     *            True or False if using URL
     * @param grid
     *            The context or grid size of the tile.
     * @return
     *         Returns the NBT data for the structure */
    public static ItemStack photoToStack(String input, boolean ignoreAlpha, String uploadOption, int grid, SubGui gui, double colorPerc) {
        GuiProgressBar progress = (GuiProgressBar) ((SubGuiPhotoImport) gui).get("progress");
        
        BufferedImage image = null;
        int color = 0;
        int maxPixelAmount = ALET.CONFIG.getMaxPixelAmount();
        ColorAccuracy.setColorAccuracy(colorPerc);
        
        image = getImage(input, uploadOption);
        
        if (PhotoReader.isRescale) {
            if (!(PhotoReader.scaleX < 1) || !(PhotoReader.scaleY < 1)) {
                image = PhotoReader.resize(image, PhotoReader.scaleY, PhotoReader.scaleX);
            }
            PhotoReader.isRescale = false;
        }
        
        if (image != null) {
            int width = image.getWidth();
            int height = image.getHeight();
            
            byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
            boolean hasAlphaChannel = image.getAlphaRaster() != null;
            int[][] result = new int[height][width];
            //System.out.println(width + ", " + height + ", " + maxPixelAmount);
            if (((width * height) <= maxPixelAmount)) {
                
                if (hasAlphaChannel) {
                    final int pixelLength = 4;
                    for (int pixel = 0, row = height - 1, col = 0; pixel + 3 < pixels.length; pixel += pixelLength) {
                        int argb = 0;
                        argb += ((pixels[pixel] & 0xff) << 24); // alpha
                        argb += (pixels[pixel + 1] & 0xff); // blue
                        argb += ((pixels[pixel + 2] & 0xff) << 8); // green
                        argb += ((pixels[pixel + 3] & 0xff) << 16); // red
                        result[row][col] = argb;
                        col++;
                        if (col == width) {
                            col = 0;
                            row--;
                        }
                        //progress.pos = Math.abs((((double) row / height) * 100) - 100) / 3;
                    }
                } else {
                    final int pixelLength = 3;
                    for (int pixel = 0, row = height - 1, col = 0; pixel + 2 < pixels.length; pixel += pixelLength) {
                        int argb = 0;
                        argb += -16777216; // 255 alpha
                        argb += (pixels[pixel] & 0xff); // blue
                        argb += ((pixels[pixel + 1] & 0xff) << 8); // green
                        argb += ((pixels[pixel + 2] & 0xff) << 16); // red
                        result[row][col] = argb;
                        col++;
                        if (col == width) {
                            col = 0;
                            row--;
                        }
                    }
                }
            }
            LittleGridContext context = LittleGridContext.get(grid);
            List<LittlePreview> tiles = new ArrayList<>();
            LittleTileColored colorTile;
            for (int row = 0; row < result.length; row++) {
                for (int col = 0; col < result[row].length; col++) {
                    
                    color = ColorAccuracy.roundRGB(result[row][col]);
                    
                    if (!ColorUtils.isInvisible(color)) { // no need to add transparent tiles
                        if (ignoreAlpha) {
                            Color c = ColorUtils.IntToRGBA(color);
                            c.setAlpha(255);
                            color = ColorUtils.RGBAToInt(c);
                        }
                        colorTile = new LittleTileColored(LittleTiles.dyeableBlock, BlockLittleDyeable.LittleDyeableType.CLEAN
                                .getMetadata(), color);
                        colorTile.setBox(new LittleBox(new LittleVec(col, row, 0)));
                        tiles.add(colorTile.getPreviewTile());
                    }
                }
                //progress.pos += (33 / (double) result.length);
            }
            ItemStack stack = new ItemStack(LittleTiles.recipeAdvanced); // create empty advanced recipe itemstack
            LittlePreviews previews = new LittlePreviews(context);
            for (LittlePreview tile : tiles) {
                previews.addWithoutCheckingPreview(tile);
            }
            savePreview(previews, stack, progress); // save tiles to itemstacks
            return stack;
        }
        
        PhotoReader.isRescale = false;
        return ItemStack.EMPTY;
    }
    
    public static void setScale(int x, int y) {
        isRescale = true;
        scaleX = x;
        scaleY = y;
    }
    
    public static void savePreview(LittlePreviews previews, ItemStack stack, GuiProgressBar progress) {
        if (previews instanceof LittleAbsolutePreviews)
            throw new IllegalArgumentException("Absolute positions cannot be saved!");
        
        if (!stack.hasTagCompound())
            stack.setTagCompound(new NBTTagCompound());
        
        if (previews.hasStructure())
            stack.getTagCompound().setTag("structure", previews.structureNBT);
        
        previews.getContext().set(stack.getTagCompound());
        
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int minZ = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;
        int maxZ = Integer.MIN_VALUE;
        
        for (LittlePreview preview : previews.allPreviews()) {
            minX = Math.min(minX, preview.box.minX);
            minY = Math.min(minY, preview.box.minY);
            minZ = Math.min(minZ, preview.box.minZ);
            maxX = Math.max(maxX, preview.box.maxX);
            maxY = Math.max(maxY, preview.box.maxY);
            maxZ = Math.max(maxZ, preview.box.maxZ);
        }
        
        new LittleVec(maxX - minX, maxY - minY, maxZ - minZ).writeToNBT("size", stack.getTagCompound());
        new LittleVec(minX, minY, minZ).writeToNBT("min", stack.getTagCompound());
        
        if (previews.totalSize() >= LittlePreview.lowResolutionMode) {
            NBTTagList list = new NBTTagList();
            HashSet<BlockPos> positions = new HashSet<>();
            
            for (int i = 0; i < previews.size(); i++) { // Will not be sorted after rotating
                BlockPos pos = previews.get(i).box.getMinVec().getBlockPos(previews.getContext());
                if (!positions.contains(pos)) {
                    positions.add(pos);
                    list.appendTag(new NBTTagIntArray(new int[] { pos.getX(), pos.getY(), pos.getZ() }));
                }
            }
            stack.getTagCompound().setTag("pos", list);
        } else
            stack.getTagCompound().removeTag("pos");
        
        NBTTagList list = writePreviews(previews, progress);
        stack.getTagCompound().setTag("tiles", list);
        stack.getTagCompound().setInteger("count", previews.size());
        stack.getTagCompound().removeTag("children");
    }
    
    public static NBTTagList writePreviews(LittlePreviews previews, GuiProgressBar progress) {
        HashMapList<String, LittlePreview> groups = new HashMapList<>();
        
        for (Iterator iterator = previews.iterator(); iterator.hasNext();) {
            
            LittlePreview preview = (LittlePreview) iterator.next();
            groups.add(preview.getTypeIdToSave(), preview);
        }
        
        NBTTagList list = new NBTTagList();
        
        for (Iterator<ArrayList<LittlePreview>> iterator = groups.values().iterator(); iterator.hasNext();) {
            ArrayList<LittlePreview> classList = iterator.next();
            int classListMax = classList.size();
            progress.max = classList.size();
            while (classList.size() > 0) {
                LittlePreview grouping = classList.remove(0);
                NBTTagCompound groupNBT = null;
                
                for (Iterator iterator2 = classList.iterator(); iterator2.hasNext();) {
                    LittlePreview preview = (LittlePreview) iterator2.next();
                    if (grouping.canBeNBTGrouped(preview)) {
                        if (groupNBT == null)
                            groupNBT = grouping.startNBTGrouping();
                        grouping.groupNBTTile(groupNBT, preview);
                        iterator2.remove();
                    }
                }
                
                progress.pos = classListMax - classList.size();
                if (groupNBT == null) {
                    NBTTagCompound nbt = new NBTTagCompound();
                    grouping.writeToNBT(nbt);
                    list.appendTag(nbt);
                } else
                    list.appendTag(groupNBT);
            }
        }
        
        return list;
    }
}

/*
 * 
 */