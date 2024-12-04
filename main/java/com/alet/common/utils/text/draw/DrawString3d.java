package com.alet.common.utils.text.draw;

import javax.vecmath.Point3f;

import com.alet.client.tapemeasure.measurement.MeasurementShape;

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
    
    public static void drawStringOnLine(String text, int contextSize, DrawPosition position, Point3f startPoint, Point3f endPoint, int color, boolean dropShadow, int yOffset) {
        //thank you David T. for your help with the math
        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        
        int i = fontRenderer.getStringWidth(text);
        Point3f midPoint = MeasurementShape.getMidPoint(startPoint, endPoint);
        
        Vec3d vecEye = player.getPositionEyes(Minecraft.getMinecraft().getRenderPartialTicks());
        
        float xDiff = (float) (vecEye.x - midPoint.getX());
        float yDiff = (float) -(midPoint.getY() - vecEye.y);
        float zDiff = (float) (vecEye.z - midPoint.getZ());
        Vec3d sameYLevelPoint = new Vec3d(vecEye.x, vecEye.y + yDiff * 2, vecEye.z);
        double diagDiff = sameYLevelPoint.distanceTo(vecEye);
        GlStateManager.pushMatrix();
        GlStateManager.enableTexture2D();
        GlStateManager.translate(midPoint.x, midPoint.y, midPoint.z);
        GlStateManager.rotate(0, 0, 1, 0);
        GlStateManager.pushMatrix();
        GlStateManager.rotate(90, 0, 1, 0);
        GlStateManager.rotate((float) Math.toDegrees(Math.atan2(zDiff, -xDiff)), 0, 1, 0);
        GlStateManager.rotate((float) Math.toDegrees(Math.atan2(yDiff, diagDiff)), 1, 0, 0);
        
        float scale = 0.143F / Math.max(16, contextSize);
        GlStateManager.scale(-scale, -scale, 0);
        GlStateManager.translate((-i * 0.5), yOffset, 0);
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
