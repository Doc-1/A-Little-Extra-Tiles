package com.alet.client.renders;

import java.util.HashMap;

import javax.vecmath.Point3f;

import org.lwjgl.util.Color;

import com.alet.client.shapes.measurements.MeasurementShape;
import com.alet.client.shapes.measurements.MeasurementShapeBox;
import com.alet.client.shapes.measurements.MeasurementShapeRegistar;
import com.alet.common.utils.NBTUtils;
import com.alet.components.items.ItemTapeMeasure;
import com.creativemd.creativecore.common.utils.mc.ColorUtils;
import com.creativemd.littletiles.common.util.grid.LittleGridContext;
import com.creativemd.littletiles.common.util.ingredient.LittleInventory;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TapeRenderer {
    public static int slotID = -1;
    public static Minecraft mc = Minecraft.getMinecraft();
    public static Tessellator tessellator = Tessellator.getInstance();
    public static BufferBuilder bufferbuilder = tessellator.getBuffer();
    public static ItemStack tapemeasure = ItemStack.EMPTY;
    public static HashMap<Integer, MeasurementShape> cachedMeasurements = new HashMap<>();
    public static Point3f lastKnownCursorPos;
    
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void render(RenderWorldLastEvent event) {
        EntityPlayer player = mc.player;
        LittleInventory inventory = new LittleInventory(player);
        
        //Sees if the tape measure is in player's inventory
        
        if (slotID == -1) {
            for (int i = 0; i < inventory.size(); i++) {
                if (inventory.get(i).getItem() instanceof ItemTapeMeasure) {
                    tapemeasure = inventory.get(i);
                    slotID = i;
                    break;
                }
            }
        }
        if (!(inventory.get(slotID).getItem() instanceof ItemTapeMeasure)) {
            slotID = -1;
            tapemeasure = ItemStack.EMPTY;
        } else {
            tapemeasure = inventory.get(slotID);
        }
        if (!tapemeasure.isEmpty() && tapemeasure.hasTagCompound()) {
            NBTTagCompound stackNBT = tapemeasure.getTagCompound();
            int selected = stackNBT.hasKey("index") ? stackNBT.getInteger("index") : 0;
            if (stackNBT.hasKey("measurements")) {
                NBTTagCompound measurements = (NBTTagCompound) stackNBT.getTag("measurements");
                for (String key : measurements.getKeySet()) {
                    NBTTagCompound measurement = (NBTTagCompound) measurements.getTag(key);
                    int index = Integer.parseInt(key);
                    String shapeName = measurement.getString("shape");
                    Color color = ColorUtils.IntToRGBA(measurement.getInteger("color"));
                    LittleGridContext context = ItemTapeMeasure.getContextAt(stackNBT, index);
                    
                    Point3f point1 = lastKnownCursorPos;
                    Point3f point2 = lastKnownCursorPos;
                    Point3f point3 = lastKnownCursorPos;
                    Point3f point4 = lastKnownCursorPos;
                    
                    if (measurement.hasKey("positions")) {
                        NBTTagCompound positions = measurement.getCompoundTag("positions");
                        if (positions.hasKey("0")) {
                            double[] p0 = NBTUtils.readDoubleArray(positions.getCompoundTag("0"), "pos", NBT.TAG_DOUBLE);
                            point1 = new Point3f((float) p0[0], (float) p0[1], (float) p0[2]);
                        }
                        if (positions.hasKey("1")) {
                            double[] p1 = NBTUtils.readDoubleArray(positions.getCompoundTag("1"), "pos", NBT.TAG_DOUBLE);
                            point2 = new Point3f((float) p1[0], (float) p1[1], (float) p1[2]);
                        }
                        if (positions.hasKey("2")) {
                            double[] p2 = NBTUtils.readDoubleArray(positions.getCompoundTag("2"), "pos", NBT.TAG_DOUBLE);
                            point3 = new Point3f((float) p2[0], (float) p2[1], (float) p2[2]);
                        }
                        if (positions.hasKey("3")) {
                            double[] p3 = NBTUtils.readDoubleArray(positions.getCompoundTag("3"), "pos", NBT.TAG_DOUBLE);
                            point4 = new Point3f((float) p3[0], (float) p3[1], (float) p3[2]);
                        }
                        //NBTUtils.readDoubleArray(positions, key)
                    }
                    HashMap<Integer, Point3f> listOfP = new HashMap<>();
                    if (point1 != null && (point1 != lastKnownCursorPos || index == selected))
                        listOfP.put(0, point1);
                    if (point2 != null && point2 != lastKnownCursorPos || index == selected)
                        listOfP.put(1, point2);
                    if (point3 != null && point3 != lastKnownCursorPos || index == selected)
                        listOfP.put(2, point3);
                    if (point4 != null && point4 != lastKnownCursorPos || index == selected)
                        listOfP.put(3, point4);
                    MeasurementShape shape = MeasurementShapeRegistar.getMeasurementShape(shapeName);
                    float r = color.getRed() / 255F;
                    float g = color.getGreen() / 255F;
                    float b = color.getBlue() / 255F;
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
                    shape.tryDrawShape(listOfP, context, r, g, b, 1F);
                    tessellator.draw();
                    
                    GlStateManager.enableTexture2D();
                    GlStateManager.enableDepth();
                    GlStateManager.depthMask(true);
                    GlStateManager.disableBlend();
                    GlStateManager.popMatrix();
                }
            }
        }
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
        
        MeasurementShapeBox.drawCube(posCursor, context.size, r, g, b, 1.0F);
        lastKnownCursorPos = posCursor;
        tessellator.draw();
        
        GlStateManager.enableDepth();
        GlStateManager.depthMask(true);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }
}
