package com.alet.client.render.tapemeasure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.lwjgl.util.Color;

import com.alet.common.utils.NBTUtils;
import com.alet.common.utils.shape.TapemeasureShapeRegistar;
import com.alet.common.utils.shape.tapemeasure.Box;
import com.alet.common.utils.shape.tapemeasure.TapeMeasureShape;
import com.alet.items.ItemTapeMeasure;
import com.alet.tiles.SelectLittleTile;
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
import net.minecraft.util.math.Vec3d;
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
    public static HashMap<Integer, TapeMeasureShape> cachedMeasurements = new HashMap<>();
    public static Vec3d lastKnownCursorPos;
    
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
            if (stackNBT.hasKey("measurements")) {
                NBTTagCompound measurements = (NBTTagCompound) stackNBT.getTag("measurements");
                for (String key : measurements.getKeySet()) {
                    NBTTagCompound measurement = (NBTTagCompound) measurements.getTag(key);
                    int index = Integer.parseInt(key);
                    String shapeName = measurement.getString("shape");
                    Color color = ColorUtils.IntToRGBA(measurement.getInteger("color"));
                    LittleGridContext context = ItemTapeMeasure.getContext(measurement);
                    
                    Vec3d tilePosMin = lastKnownCursorPos;
                    Vec3d tilePosMax = lastKnownCursorPos;
                    Vec3d secondTilePosMin = lastKnownCursorPos;
                    Vec3d secondTilePosMax = lastKnownCursorPos;
                    
                    if (measurement.hasKey("positions")) {
                        NBTTagCompound positions = measurement.getCompoundTag("positions");
                        if (positions.hasKey("0")) {
                            double[] p0 = NBTUtils.readDoubleArray(positions.getCompoundTag("0"), "pos", NBT.TAG_DOUBLE);
                            tilePosMin = new Vec3d(p0[0], p0[1], p0[2]);
                        }
                        if (positions.hasKey("1")) {
                            double[] p1 = NBTUtils.readDoubleArray(positions.getCompoundTag("1"), "pos", NBT.TAG_DOUBLE);
                            tilePosMax = new Vec3d(p1[0], p1[1], p1[2]);
                        }
                        if (positions.hasKey("2")) {
                            double[] p2 = NBTUtils.readDoubleArray(positions.getCompoundTag("2"), "pos", NBT.TAG_DOUBLE);
                            secondTilePosMin = new Vec3d(p2[0], p2[1], p2[2]);
                        }
                        if (positions.hasKey("3")) {
                            double[] p3 = NBTUtils.readDoubleArray(positions.getCompoundTag("3"), "pos", NBT.TAG_DOUBLE);
                            secondTilePosMax = new Vec3d(p3[0], p3[1], p3[2]);
                        }
                        //NBTUtils.readDoubleArray(positions, key)
                    }
                    List<Vec3d> listOfPos = new ArrayList<>();
                    listOfPos.add(tilePosMin);
                    listOfPos.add(tilePosMax);
                    listOfPos.add(secondTilePosMin);
                    listOfPos.add(secondTilePosMax);
                    
                    TapeMeasureShape shape = TapemeasureShapeRegistar.createNewShape(shapeName, listOfPos, context);
                    cachedMeasurements.put(index, shape);
                    float r = color.getRed() / 255F;
                    float g = color.getGreen() / 255F;
                    float b = color.getBlue() / 255F;
                    
                    GlStateManager.enableBlend();
                    GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
                        GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
                        GlStateManager.DestFactor.ZERO);
                    GlStateManager.glLineWidth(2.0F);
                    GlStateManager.enableAlpha();
                    
                    GlStateManager.disableTexture2D();
                    GlStateManager.depthMask(false);
                    GlStateManager.disableDepth();
                    
                    bufferbuilder.begin(2, DefaultVertexFormats.POSITION_COLOR);
                    shape.tryDrawShape(r, g, b, 1F);
                    tessellator.draw();
                    
                    GlStateManager.enableDepth();
                    GlStateManager.depthMask(true);
                    GlStateManager.enableTexture2D();
                    GlStateManager.disableBlend();
                }
                
            }
        }
        /*
        if (!tapemeasure.isEmpty()) {
            PosData data = ItemTapeMeasure.data;
            NBTTagCompound stackNBT = (tapemeasure.hasTagCompound()) ? tapemeasure.getTagCompound() : new NBTTagCompound();
            //Makes sure it has at least one context. Without a context it will not render anything.
            if (stackNBT.hasNoTags()) {
                stackNBT.setInteger("index", 0);
                tapemeasure.setTagCompound(stackNBT);
            }
            if (data != null) {
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
                    GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
                    GlStateManager.DestFactor.ZERO);
                GlStateManager.glLineWidth(2.0F);
                GlStateManager.enableAlpha();
                
                GlStateManager.disableTexture2D();
                GlStateManager.depthMask(false);
                GlStateManager.disableDepth();
                
                bufferbuilder.begin(2, DefaultVertexFormats.POSITION_COLOR);
                NBTTagList list = new NBTTagList();
                for (int i = 0; i < 10; i++) {
                    list = stackNBT.getTagList("measurement_" + i, NBT.TAG_COMPOUND);
                    NBTTagCompound nbt = list.getCompoundTagAt(0);
                    String shapeName = nbt.getString("shape");
                    
                    if (!shapeName.equals("")) {
                        int contextSize = ItemTapeMeasure.getContext(nbt);
                        int colorInt = nbt.hasKey("color") ? nbt.getInteger("color") : ColorUtils.WHITE;
                        Color color = ColorUtils.IntToRGBA(colorInt);
                        
                        float r = color.getRed() / 255F;
                        float g = color.getGreen() / 255F;
                        float b = color.getBlue() / 255F;
                        
                        SelectLittleTile tilePosMin = data.tilePosCursor;
                        SelectLittleTile tilePosMax = data.tilePosCursor;
                        SelectLittleTile secondartTilePosMin = data.tilePosCursor;
                        SelectLittleTile secondartTilePosMax = data.tilePosCursor;
                        
                        if (nbt.hasKey("x1")) //If the nbt has x then it will have y and z. If it does use the constant nbt value to display measurement
                            tilePosMin = new SelectLittleTile(new Vec3d(nbt.getDouble("x1"), nbt.getDouble("y1"), nbt
                                    .getDouble("z1")), LittleGridContext.get(contextSize));
                        
                        if (nbt.hasKey("x2")) //If the nbt has x then it will have y and z. If it does use the constant nbt value to display measurement
                            tilePosMax = new SelectLittleTile(new Vec3d(nbt.getDouble("x2"), nbt.getDouble("y2"), nbt
                                    .getDouble("z2")), LittleGridContext.get(contextSize));
                        
                        if (nbt.hasKey("x3")) //If the nbt has x then it will have y and z. If it does use the constant nbt value to display measurement
                            secondartTilePosMin = new SelectLittleTile(new Vec3d(nbt.getDouble("x3"), nbt.getDouble(
                                "y3"), nbt.getDouble("z3")), LittleGridContext.get(contextSize));
                        
                        if (nbt.hasKey("x4")) //If the nbt has x then it will have y and z. If it does use the constant nbt value to display measurement
                            secondartTilePosMax = new SelectLittleTile(new Vec3d(nbt.getDouble("x4"), nbt.getDouble(
                                "y4"), nbt.getDouble("z4")), LittleGridContext.get(contextSize));
                        
                        try {
                            List<SelectLittleTile> listOfTilePos = new ArrayList<SelectLittleTile>();
                            listOfTilePos.add(tilePosMin);
                            listOfTilePos.add(tilePosMax);
                            listOfTilePos.add(secondartTilePosMin);
                            listOfTilePos.add(secondartTilePosMax);
                            TapeMeasureShape shape = TapemeasureShapeRegistar.registeredShapes.get(shapeName).newInstance();
                            shape.drawShape(listOfTilePos, contextSize, r, g, b, 1.0F);
                        } catch (InstantiationException | IllegalAccessException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
                tessellator.draw();
                
                GlStateManager.enableDepth();
                GlStateManager.depthMask(true);
                GlStateManager.enableTexture2D();
                GlStateManager.disableBlend();
                
            }
        }
        */
    }
    
    public static void renderCursor(Vec3d posCursor, LittleGridContext context) {
        
        Color color = ColorUtils.IntToRGBA(ColorUtils.WHITE);
        float r = color.getRed() / 255F;
        float g = color.getGreen() / 255F;
        float b = color.getBlue() / 255F;
        
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
            GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.glLineWidth(2.0F);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        GlStateManager.disableDepth();
        bufferbuilder.begin(2, DefaultVertexFormats.POSITION_COLOR);
        
        SelectLittleTile tilePosCursor = new SelectLittleTile(posCursor, context);
        Box.drawBox(tilePosCursor, context.size, r, g, b, 1.0F);
        lastKnownCursorPos = posCursor;
        tessellator.draw();
        
        GlStateManager.enableDepth();
        GlStateManager.depthMask(true);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
}
