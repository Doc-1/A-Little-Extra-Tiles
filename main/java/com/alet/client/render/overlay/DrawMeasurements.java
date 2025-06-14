package com.alet.client.render.overlay;

import javax.vecmath.Point3f;

import org.lwjgl.util.Color;

import com.alet.client.tool.shaper.MeasurementHandler;
import com.alet.common.placement.measurments.type.LittleMeasurementBox;
import com.alet.common.placement.measurments.type.LittleMeasurementType;
import com.creativemd.creativecore.common.utils.mc.ColorUtils;
import com.creativemd.littletiles.common.util.grid.LittleGridContext;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class DrawMeasurements {
    
    public static Minecraft mc = Minecraft.getMinecraft();
    public static Tessellator tessellator = Tessellator.getInstance();
    public static BufferBuilder bufferbuilder = tessellator.getBuffer();
    public static Point3f lastKnownCursorPos = new Point3f();
    
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void renderShape(RenderWorldLastEvent event) {
        EntityPlayer player = mc.player;
        
        MeasurementHandler.getTapeMeasures().forEach(tapeMeasure -> {
            LittleMeasurementType measurement = tapeMeasure.getOrCreateMeasurement(tapeMeasure.index);
            
            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
                GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
                GlStateManager.DestFactor.ZERO);
            GlStateManager.glLineWidth(2.0F);
            GlStateManager.enableAlpha();
            
            double d0 = player.lastTickPosX + (player.posX - player.lastTickPosX) * mc.getRenderPartialTicks();
            double d1 = player.lastTickPosY + (player.posY - player.lastTickPosY) * mc.getRenderPartialTicks();
            double d2 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * mc.getRenderPartialTicks();
            GlStateManager.translate(-d0, -d1, -d2);
            GlStateManager.disableTexture2D();
            GlStateManager.depthMask(false);
            GlStateManager.disableDepth();
            
            bufferbuilder.begin(2, DefaultVertexFormats.POSITION_COLOR);
            measurement.tryDrawShape();
            tessellator.draw();
            
            GlStateManager.enableTexture2D();
            GlStateManager.enableDepth();
            GlStateManager.depthMask(true);
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
            
        });
        
    }
    
    public static void renderCursor(Point3f posCursor, int color, LittleGridContext context) {
        
        Color c = ColorUtils.IntToRGBA(color);
        float r = c.getRed() / 255F;
        float g = c.getGreen() / 255F;
        float b = c.getBlue() / 255F;
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
            GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.glLineWidth(2.0F);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        GlStateManager.disableDepth();
        EntityPlayer player = Minecraft.getMinecraft().player;
        double d0 = player.lastTickPosX + (player.posX - player.lastTickPosX) * mc.getRenderPartialTicks();
        double d1 = player.lastTickPosY + (player.posY - player.lastTickPosY) * mc.getRenderPartialTicks();
        double d2 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * mc.getRenderPartialTicks();
        GlStateManager.translate(-d0, -d1, -d2);
        bufferbuilder.begin(2, DefaultVertexFormats.POSITION_COLOR);
        
        //SelectLittleTile tilePosCursor = new SelectLittleTile(posCursor, context);
        
        LittleMeasurementBox.drawCube(posCursor, context.size, r, g, b, 1.0F);
        lastKnownCursorPos = posCursor;
        tessellator.draw();
        
        GlStateManager.enableDepth();
        GlStateManager.depthMask(true);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }
}
