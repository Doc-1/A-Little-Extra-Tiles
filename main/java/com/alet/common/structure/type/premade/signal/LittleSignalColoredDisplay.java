package com.alet.common.structure.type.premade.signal;

import java.nio.ByteBuffer;
import java.util.BitSet;

import javax.vecmath.Vector3f;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Color;

import com.creativemd.creativecore.common.utils.math.VectorUtils;
import com.creativemd.creativecore.common.utils.math.box.AlignedBox;
import com.creativemd.creativecore.common.utils.math.box.BoxCorner;
import com.creativemd.creativecore.common.utils.math.box.BoxFace;
import com.creativemd.creativecore.common.utils.mc.ColorUtils;
import com.creativemd.littletiles.common.structure.directional.StructureDirectional;
import com.creativemd.littletiles.common.structure.registry.LittleStructureType;
import com.creativemd.littletiles.common.structure.relative.StructureRelative;
import com.creativemd.littletiles.common.structure.signal.output.InternalSignalOutput;
import com.creativemd.littletiles.common.structure.type.premade.LittleStructurePremade;
import com.creativemd.littletiles.common.tile.parent.IStructureTileList;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.CullFace;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.EnumFacing.AxisDirection;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class LittleSignalColoredDisplay extends LittleStructurePremade {
    
    public static final int renderDistance = 64;
    
    public final int WHITE = ColorUtils.RGBAToInt(255, 255, 255, 0);
    public final int LIGHT_GRAY = ColorUtils.RGBAToInt(170, 170, 170, 0);
    public final int DARK_GRAY = ColorUtils.RGBAToInt(85, 85, 85, 0);
    public final int BLACK = ColorUtils.RGBAToInt(0, 0, 0, 0);
    public final int YELLOW = ColorUtils.RGBAToInt(255, 255, 85, 0);
    public final int GREEN = ColorUtils.RGBAToInt(0, 170, 0, 0);
    public final int LIME = ColorUtils.RGBAToInt(85, 255, 85, 0);
    public final int PERSIMMON = ColorUtils.RGBAToInt(255, 85, 85, 0);
    public final int RED = ColorUtils.RGBAToInt(170, 0, 0, 0);
    public final int BROWN = ColorUtils.RGBAToInt(170, 85, 0, 0);
    public final int PURPLE = ColorUtils.RGBAToInt(170, 0, 170, 0);
    public final int MAGENTA = ColorUtils.RGBAToInt(255, 85, 255, 0);
    public final int CYAN = ColorUtils.RGBAToInt(85, 255, 255, 0);
    public final int DARK_CYAN = ColorUtils.RGBAToInt(0, 170, 170, 0);
    public final int BLUE = ColorUtils.RGBAToInt(0, 0, 170, 0);
    public final int LIGHT_BLUE = ColorUtils.RGBAToInt(85, 85, 255, 0);
    
    @StructureDirectional(color = ColorUtils.CYAN)
    public StructureRelative frame;
    
    @StructureDirectional
    public EnumFacing facing;
    
    @StructureDirectional
    public Vector3f topRight;
    
    private int textureId = -1;
    
    public LittleSignalColoredDisplay(LittleStructureType type, IStructureTileList mainBlock) {
        super(type, mainBlock);
    }
    
    @Override
    protected void loadFromNBTExtra(NBTTagCompound nbt) {}
    
    @Override
    protected void writeToNBTExtra(NBTTagCompound nbt) {}
    
    @Override
    public void receiveInternalOutputChange(InternalSignalOutput output) {
        if (isClient()) {
            updateTexture();
        }
    }
    
    public Color get4BitColor(boolean[] state) {
        BitSet bits = new BitSet(4);
        int i = 0;
        for (boolean bit : state) {
            bits.set(i, bit);
            i++;
        }
        byte nibble = 0;
        if (bits.toByteArray().length != 0)
            nibble = bits.toByteArray()[0];
        
        switch (nibble) {
        case 0:
            return ColorUtils.IntToRGBA(BLACK);
        case 1:
            return ColorUtils.IntToRGBA(LIGHT_GRAY);
        case 2:
            return ColorUtils.IntToRGBA(DARK_GRAY);
        case 3:
            return ColorUtils.IntToRGBA(WHITE);
        case 4:
            return ColorUtils.IntToRGBA(YELLOW);
        case 5:
            return ColorUtils.IntToRGBA(GREEN);
        case 6:
            return ColorUtils.IntToRGBA(LIME);
        case 7:
            return ColorUtils.IntToRGBA(PERSIMMON);
        case 8:
            return ColorUtils.IntToRGBA(RED);
        case 9:
            return ColorUtils.IntToRGBA(BROWN);
        case 10:
            return ColorUtils.IntToRGBA(PURPLE);
        case 11:
            return ColorUtils.IntToRGBA(MAGENTA);
        case 12:
            return ColorUtils.IntToRGBA(CYAN);
        case 13:
            return ColorUtils.IntToRGBA(DARK_CYAN);
        case 14:
            return ColorUtils.IntToRGBA(BLUE);
        case 15:
            return ColorUtils.IntToRGBA(LIGHT_BLUE);
        default:
            return ColorUtils.IntToRGBA(BLACK);
        }
    }
    
    @SideOnly(Side.CLIENT)
    public void updateTexture() {
        if (textureId == -1)
            textureId = GlStateManager.generateTexture();
        GlStateManager.bindTexture(textureId);
        ByteBuffer buffer = ByteBuffer.allocateDirect(16 * 3);
        for (int i = 0; i < 16; i++) {
            boolean[] state = this.getOutput(i).getState();
            Color pixel = get4BitColor(state);
            buffer.put(pixel.getRedByte());
            buffer.put(pixel.getGreenByte());
            buffer.put(pixel.getBlueByte());
            
        }
        
        buffer.rewind();
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA2, 4, 4, 0, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, buffer);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void renderTick(BlockPos pos, double x, double y, double z, float partialTickTime) {
        if (textureId == -1)
            updateTexture();
        
        GlStateManager.enableBlend();
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GlStateManager.disableLighting();
        GlStateManager.bindTexture(textureId);
        GlStateManager.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GlStateManager.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        
        GlStateManager.pushMatrix();
        
        GlStateManager.translate(x, y, z);
        
        AlignedBox box = frame.getBox().getCube(frame.getContext());
        BoxFace face = BoxFace.get(facing);
        if (facing.getAxisDirection() == AxisDirection.POSITIVE)
            box.setMax(facing.getAxis(), box.getMin(facing.getAxis()) + 0.01F);
        else
            box.setMin(facing.getAxis(), box.getMax(facing.getAxis()) - 0.01F);
        Axis uAxis = face.getTexUAxis();
        Axis vAxis = face.getTexVAxis();
        
        GlStateManager.enableRescaleNormal();
        
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder builder = tessellator.getBuffer();
        builder.begin(GL11.GL_POLYGON, DefaultVertexFormats.POSITION_TEX);
        for (BoxCorner corner : face.corners)
            builder.pos(box.getValueOfFacing(corner.x), box.getValueOfFacing(corner.y), box.getValueOfFacing(corner.z))
                    
                    .tex(corner.isFacingPositive(uAxis) != (VectorUtils.get(uAxis, topRight) > 0) ? 1 : 0, corner
                            .isFacingPositive(vAxis) != (VectorUtils.get(vAxis, topRight) > 0) ? 1 : 0)
                    .endVertex();
        tessellator.draw();
        
        GlStateManager.popMatrix();
        
        GlStateManager.cullFace(CullFace.BACK);
        
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public double getMaxRenderDistanceSquared() {
        return Math.pow(renderDistance, 2);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        return frame.getBox().getBox(frame.getContext());
    }
    
    @Override
    public void unload() {
        super.unload();
        if (getWorld().isRemote && textureId != -1)
            GlStateManager.deleteTexture(textureId);
    }
    
}
