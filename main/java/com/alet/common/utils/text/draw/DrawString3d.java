package com.alet.common.utils.text.draw;

import javax.vecmath.Point3d;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;

public class DrawString3d {
    
    public enum DrawPosition {
        Middle(),
        Left(),
        Right()
    }
    
    public static void drawStringOnLine(String text, int contextSize, DrawPosition position, Point3d point1, Point3d point2, float roll, float yaw, float pitch, int color, boolean dropShadow) {
        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
        int i = fontRenderer.getStringWidth(text);
        int h = fontRenderer.FONT_HEIGHT;
        Point3d midPoint = new Point3d((point2.x + point1.x) / 2, (point2.y + point1.y) / 2, (point2.z + point1.z) / 2);
        GlStateManager.pushMatrix();
        GlStateManager.enableTexture2D();
        GlStateManager.translate(midPoint.x, midPoint.y, midPoint.z);
        GlStateManager.rotate(roll, 1F, 0, 0);
        GlStateManager.rotate(yaw, 0, 1F, 0);
        GlStateManager.rotate(pitch, 0, 0, 1F);
        float scale = 0.143F / Math.max(16, contextSize);
        GlStateManager.scale(-scale, -scale, 0);
        GlStateManager.translate(-i / 2, 2, 0);
        fontRenderer.drawString(text, 0, 0, color, dropShadow);
        
        GlStateManager.disableTexture2D();
        GlStateManager.popMatrix();
        
    }
}
