package com.alet.render.tapemeasure;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.Color;

import com.alet.items.ItemTapeMeasure;
import com.alet.items.ItemTapeMeasure.PosData;
import com.alet.render.tapemeasure.shape.Box;
import com.alet.render.tapemeasure.shape.TapeMeasureShape;
import com.alet.tiles.SelectLittleTile;
import com.creativemd.creativecore.common.utils.mc.ColorUtils;
import com.creativemd.littletiles.common.item.ItemMultiTiles;
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
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TapeRenderer {
    
    public static int counter = 0;
    public static boolean inInv = false;
    public static Minecraft mc = Minecraft.getMinecraft();
    public static EntityPlayer player;
    public static Tessellator tessellator = Tessellator.getInstance();
    public static BufferBuilder bufferbuilder = tessellator.getBuffer();
    
    @SubscribeEvent(priority = EventPriority.LOW)
    @SideOnly(Side.CLIENT)
    public void render(RenderWorldLastEvent event) {
        player = mc.player;
        
        List<String> contextNames = LittleGridContext.getNames();
        
        ItemStack stack = ItemStack.EMPTY;
        LittleInventory inventory = new LittleInventory(player);
        
        //Sees if the tape measure is in player's inventory
        inInv = false;
        for (int i = 0; i < inventory.size(); i++) {
            if (inventory.get(i).getItem() instanceof ItemTapeMeasure) {
                stack = inventory.get(i);
                inInv = true;
                break;
            }
        }
        
        if (inInv) {
            PosData data = ItemTapeMeasure.data;
            NBTTagCompound stackNBT = (stack.hasTagCompound()) ? stack.getTagCompound() : new NBTTagCompound();
            //Makes sure it has at least one context. Without a context it will not render anything.
            if (stackNBT.hasNoTags() || !stackNBT.hasKey("context0")) {
                stackNBT.setInteger("context0", contextNames.indexOf(ItemMultiTiles.currentContext.size + ""));
                stackNBT.setInteger("index", 0);
                stack.setTagCompound(stackNBT);
            }
            if (data != null) {
                GlStateManager.enableBlend();
                GlStateManager
                        .tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
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
                            tilePosMin = new SelectLittleTile(new Vec3d(nbt.getDouble("x1"), nbt.getDouble("y1"), nbt.getDouble("z1")), LittleGridContext.get(contextSize));
                        
                        if (nbt.hasKey("x2")) //If the nbt has x then it will have y and z. If it does use the constant nbt value to display measurement
                            tilePosMax = new SelectLittleTile(new Vec3d(nbt.getDouble("x2"), nbt.getDouble("y2"), nbt.getDouble("z2")), LittleGridContext.get(contextSize));
                        
                        if (nbt.hasKey("x3")) //If the nbt has x then it will have y and z. If it does use the constant nbt value to display measurement
                            secondartTilePosMin = new SelectLittleTile(new Vec3d(nbt.getDouble("x3"), nbt.getDouble("y3"), nbt.getDouble("z3")), LittleGridContext
                                    .get(contextSize));
                        
                        if (nbt.hasKey("x4")) //If the nbt has x then it will have y and z. If it does use the constant nbt value to display measurement
                            secondartTilePosMax = new SelectLittleTile(new Vec3d(nbt.getDouble("x4"), nbt.getDouble("y4"), nbt.getDouble("z4")), LittleGridContext
                                    .get(contextSize));
                        
                        try {
                            List<SelectLittleTile> listOfTilePos = new ArrayList<SelectLittleTile>();
                            listOfTilePos.add(tilePosMin);
                            listOfTilePos.add(tilePosMax);
                            listOfTilePos.add(secondartTilePosMin);
                            listOfTilePos.add(secondartTilePosMax);
                            TapeMeasureShape shape = TapeMeasureShape.registeredShapes.get(shapeName).newInstance();
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
        
    }
    
    public static void renderCursor(NBTTagCompound nbt, int index1, int contextSize, SelectLittleTile tilePosCursor) {
        int color = (nbt.hasKey("color" + index1)) ? nbt.getInteger("color" + index1) : ColorUtils.WHITE;
        Color colour = ColorUtils.IntToRGBA(color);
        float r = colour.getRed() / 255F;
        float g = colour.getGreen() / 255F;
        float b = colour.getBlue() / 255F;
        
        GlStateManager.enableBlend();
        GlStateManager
                .tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.glLineWidth(2.0F);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        GlStateManager.disableDepth();
        bufferbuilder.begin(2, DefaultVertexFormats.POSITION_COLOR);
        
        Box.drawBox(tilePosCursor, contextSize, r, g, b, 1.0F);
        
        tessellator.draw();
        
        GlStateManager.enableDepth();
        GlStateManager.depthMask(true);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
}
