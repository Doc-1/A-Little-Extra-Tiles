package com.alet.common.util;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.nio.IntBuffer;
import java.util.HashMap;

import javax.imageio.ImageIO;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Cursor;
import org.lwjgl.input.Mouse;

import com.alet.ALET;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class MouseUtils {
    
    private static final Cursor DEFAULT_CURSOR = Mouse.getNativeCursor();
    private static HashMap<String, Cursor> listOfCursors = new HashMap<String, Cursor>();
    static {
        addCursor("move", ALET.MODID, "textures/cursors/move.png");
        addCursor("open_hand", ALET.MODID, "textures/cursors/open_hand.png");
        addCursor("dotted_line", ALET.MODID, "textures/cursors/dotted_line.png");
        addCursor("close", 16, 16, ALET.MODID, "textures/cursors/close.png");
        
    }
    
    public static void addCursor(String name, int width, int height, String modid, String location) {
        
        try {
            BufferedImage image = ImageIO.read(Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation(modid, location)).getInputStream());
            BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics2D = resizedImage.createGraphics();
            //For some reason the cursor renders upside down. Rotating it to flip it back over.
            graphics2D.translate((resizedImage.getHeight() - resizedImage.getWidth()) / 2, (resizedImage.getHeight() - resizedImage.getWidth()) / 2);
            graphics2D.rotate(Math.toRadians(180), resizedImage.getHeight() / 2, resizedImage.getWidth() / 2);
            
            graphics2D.drawImage(image, 0, 0, width, height, null);
            graphics2D.dispose();
            Cursor cursor = new Cursor(resizedImage.getWidth(), resizedImage.getHeight(), resizedImage.getWidth() / 2, resizedImage.getHeight() / 2, 1, IntBuffer
                    .wrap(resizedImage.getRGB(0, 0, resizedImage.getWidth(), resizedImage.getHeight(), null, 0, resizedImage.getWidth())), null);
            listOfCursors.put(name, cursor);
        } catch (Exception e) {
            
            e.printStackTrace();
        }
        
    }
    
    public static void addCursor(String name, String modid, String location) {
        addCursor(name, 32, 32, modid, location);
    }
    
    public static void setCursor(String name) {
        try {
            Mouse.setNativeCursor(listOfCursors.get(name));
        } catch (LWJGLException e) {
            e.printStackTrace();
        }
    }
    
    public static void resetCursor() {
        try {
            Mouse.setNativeCursor(DEFAULT_CURSOR);
        } catch (LWJGLException e) {
            e.printStackTrace();
        }
    }
}
