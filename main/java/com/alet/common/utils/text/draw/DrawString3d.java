package com.alet.common.utils.text.draw;

import javax.vecmath.Point3f;

import com.alet.common.utils.shape.tapemeasure.MeasurementShape;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.Vec3d;

public class DrawString3d {
    
    public enum DrawPosition {
        Middle(),
        Left(),
        Right()
    }
    
    public static void drawStringOnLine(String text, int contextSize, DrawPosition position, Point3f startPoint, Point3f endPoint, float roll, float yaw, float pitch, int color, boolean dropShadow) {
        
        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        
        int i = fontRenderer.getStringWidth(text);
        int h = fontRenderer.FONT_HEIGHT;
        Point3f midPoint = MeasurementShape.getMidPoint(startPoint, endPoint);
        Vec3d vecEye = player.getPositionEyes(Minecraft.getMinecraft().getRenderPartialTicks());
        
        GlStateManager.pushMatrix();
        GlStateManager.enableTexture2D();
        GlStateManager.translate(midPoint.x, midPoint.y, midPoint.z);
        GlStateManager.rotate(0, 0, 1, 0);
        // GlStateManager.rotate(yaw, 0, 1F, 0);
        // GlStateManager.rotate(pitch, 0, 0, 1F);
        // GlStateManager.rotate(new Quaternion((float) look.x, (float) look.y, (float) look.z, 0));
        
        float scale = 0.143F / Math.max(32, contextSize + contextSize);
        //GlStateManager.rotate(90, 0, 1, 0);
        GlStateManager.pushMatrix();
        GlStateManager.rotate(-player.rotationYaw, 0, 1, 0);
        GlStateManager.rotate(player.rotationPitch, 1, 0, 0);
        //GlStateManager.rotate(-playerHeadAngle[0], 1F, 0F, 0F);
        GlStateManager.scale(-scale, -scale, 0);
        GlStateManager.translate(-i / 2, 0, 0);
        fontRenderer.drawString(text, 0, 0, color, dropShadow);
        GlStateManager.popMatrix();
        GlStateManager.disableTexture2D();
        GlStateManager.popMatrix();
        
    }
}

/*
 * 
        GlStateManager.pushMatrix();
        GlStateManager.rotate(180, 0F, 1F, 0F);
        //GlStateManager.rotate(playerHeadAngle[0], 1F, 0F, 0);
        GlStateManager.scale(-scale, -scale, 0);
        GlStateManager.translate(-i / 2, 2, 0);
        fontRenderer.drawString(text, 0, 0, color, dropShadow);
        GlStateManager.popMatrix();
        
 */
