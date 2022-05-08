package com.alet.render.tapemeasure;

import java.util.List;

import org.lwjgl.util.Color;

import com.alet.ALET;
import com.alet.items.ItemTapeMeasure;
import com.alet.items.ItemTapeMeasure.PosData;
import com.alet.render.tapemeasure.shape.Box;
import com.alet.render.tapemeasure.shape.Line;
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
import net.minecraft.util.EnumFacing;
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
        ItemStack ingredient = new ItemStack(ALET.tapeMeasure, 1);
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
                    int shape = nbt.hasKey("shape") ? nbt.getInteger("shape") : 0;
                    int contextSize = ItemTapeMeasure.getContext(nbt);
                    int colorInt = nbt.hasKey("color") ? nbt.getInteger("color") : ColorUtils.WHITE;
                    Color color = ColorUtils.IntToRGBA(colorInt);
                    
                    float r = color.getRed() / 255F;
                    float g = color.getGreen() / 255F;
                    float b = color.getBlue() / 255F;
                    
                    EnumFacing facingMin = nbt.hasKey("facing") ? EnumFacing.byName(nbt.getString("facing")) : EnumFacing.UP;
                    EnumFacing facingMax = nbt.hasKey("facing") ? EnumFacing.byName(nbt.getString("facing")) : EnumFacing.UP;
                    
                    SelectLittleTile tilePosMin = data.tilePosCursor;
                    SelectLittleTile tilePosMax = data.tilePosCursor;
                    
                    if (nbt.hasKey("x1")) //If the nbt has x then it will have y and z. If it does use the constant nbt value to display measurement
                        tilePosMin = new SelectLittleTile(new Vec3d(nbt.getDouble("x1"), nbt.getDouble("y1"), nbt.getDouble("z1")), LittleGridContext.get(contextSize));
                    
                    if (nbt.hasKey("x2")) //If the nbt has x then it will have y and z. If it does use the constant nbt value to display measurement
                        tilePosMax = new SelectLittleTile(new Vec3d(nbt.getDouble("x2"), nbt.getDouble("y2"), nbt.getDouble("z2")), LittleGridContext.get(contextSize));
                    
                    if (nbt.getInteger("shape") == 0) {
                        Box.drawBox(tilePosMin, tilePosMax, r, g, b, 1.0F);
                    } else if (nbt.getInteger("shape") == 1) {
                        Line.drawLine(tilePosMin, tilePosMax, r, g, b, 1.0F);
                        Box.drawBox(tilePosMin, contextSize, r, g, b, 1.0F);
                        Box.drawBox(tilePosMax, contextSize, r, g, b, 1.0F);
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
