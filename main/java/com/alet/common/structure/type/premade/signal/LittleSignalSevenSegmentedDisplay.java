package com.alet.common.structure.type.premade.signal;

import javax.vecmath.Vector3f;

import org.lwjgl.opengl.GL11;

import com.creativemd.creativecore.common.utils.math.VectorUtils;
import com.creativemd.creativecore.common.utils.math.box.AlignedBox;
import com.creativemd.creativecore.common.utils.math.box.BoxCorner;
import com.creativemd.creativecore.common.utils.math.box.BoxFace;
import com.creativemd.creativecore.common.utils.mc.ColorUtils;
import com.creativemd.littletiles.client.render.overlay.PreviewRenderer;
import com.creativemd.littletiles.common.structure.directional.StructureDirectional;
import com.creativemd.littletiles.common.structure.registry.LittleStructureType;
import com.creativemd.littletiles.common.structure.relative.StructureRelative;
import com.creativemd.littletiles.common.structure.signal.output.InternalSignalOutput;
import com.creativemd.littletiles.common.structure.type.premade.LittleStructurePremade;
import com.creativemd.littletiles.common.tile.parent.IStructureTileList;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.CullFace;
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

public class LittleSignalSevenSegmentedDisplay extends LittleStructurePremade {
    
    public static final int renderDistance = 64;
    
    @StructureDirectional(color = ColorUtils.CYAN)
    public StructureRelative frame;
    
    @StructureDirectional
    public EnumFacing facing;
    
    @StructureDirectional
    public Vector3f topRight;
    
    public LittleSignalSevenSegmentedDisplay(LittleStructureType type, IStructureTileList mainBlock) {
        super(type, mainBlock);
    }
    
    @Override
    protected void loadFromNBTExtra(NBTTagCompound nbt) {}
    
    @Override
    protected void writeToNBTExtra(NBTTagCompound nbt) {}
    
    @Override
    public void receiveInternalOutputChange(InternalSignalOutput output) {
        
    }
    
    public void drawHorizontalLine(double x, double y, double z) {
        
        AlignedBox box = frame.getBox().getCube(frame.getContext());
        BoxFace face = BoxFace.get(facing);
        if (facing.getAxisDirection() == AxisDirection.POSITIVE)
            box.setMax(facing.getAxis(), box.getMin(facing.getAxis()) + 0.01F);
        else
            box.setMin(facing.getAxis(), box.getMax(facing.getAxis()) - 0.01F);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder builder = tessellator.getBuffer();
        
        PreviewRenderer.mc.renderEngine.bindTexture(PreviewRenderer.WHITE_TEXTURE);
        
        builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_NORMAL);
        BoxCorner corner = face.corners[0];
        
        Axis uAxis = face.getTexUAxis();
        Axis vAxis = face.getTexVAxis();
        double modX1 = 0;
        double modX2 = 0;
        double modZ1 = 0;
        double modZ2 = 0;
        switch (facing) {
        case NORTH:
            corner = face.corners[0];
            modX1 = -0.0155;
            modX2 = -0.0465;
            break;
        case EAST:
            break;
        case SOUTH:
            modX1 = -0.0465;
            modX2 = -0.0155;
            corner = face.corners[3];
            break;
        case WEST:
            
            break;
        case UP:
            
            break;
        case DOWN:
            
            break;
        
        default:
            break;
        }
        builder.pos(box.getValueOfFacing(corner.x) - x + modX1, box.getValueOfFacing(corner.y) - y, box.getValueOfFacing(corner.z) - z)
                
                .tex(corner.isFacingPositive(uAxis) != (VectorUtils.get(uAxis, topRight) > 0) ? 1 : 0, corner
                        .isFacingPositive(vAxis) != (VectorUtils.get(vAxis, topRight) > 0) ? 1 : 0)
                .endVertex();
        builder.pos(box.getValueOfFacing(corner.x) - x + modX1, box.getValueOfFacing(corner.y) - 0.0155 - y, box.getValueOfFacing(corner.z) - z)
                
                .tex(corner.isFacingPositive(uAxis) != (VectorUtils.get(uAxis, topRight) > 0) ? 1 : 0, corner
                        .isFacingPositive(vAxis) != (VectorUtils.get(vAxis, topRight) > 0) ? 1 : 0)
                .endVertex();
        builder.pos(box.getValueOfFacing(corner.x) + modX2 - x, box.getValueOfFacing(corner.y) - 0.0155 - y, box.getValueOfFacing(corner.z) - z)
                
                .tex(corner.isFacingPositive(uAxis) != (VectorUtils.get(uAxis, topRight) > 0) ? 1 : 0, corner
                        .isFacingPositive(vAxis) != (VectorUtils.get(vAxis, topRight) > 0) ? 1 : 0)
                .endVertex();
        builder.pos(box.getValueOfFacing(corner.x) + modX2 - x, box.getValueOfFacing(corner.y) - y, box.getValueOfFacing(corner.z) - z)
                
                .tex(corner.isFacingPositive(uAxis) != (VectorUtils.get(uAxis, topRight) > 0) ? 1 : 0, corner
                        .isFacingPositive(vAxis) != (VectorUtils.get(vAxis, topRight) > 0) ? 1 : 0)
                .endVertex();
        
        tessellator.draw();
    }
    
    public void drawDecimalPoint(double x, double y, double z) {
        
        AlignedBox box = frame.getBox().getCube(frame.getContext());
        BoxFace face = BoxFace.get(facing);
        if (facing.getAxisDirection() == AxisDirection.POSITIVE)
            box.setMax(facing.getAxis(), box.getMin(facing.getAxis()) + 0.01F);
        else
            box.setMin(facing.getAxis(), box.getMax(facing.getAxis()) - 0.01F);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder builder = tessellator.getBuffer();
        
        PreviewRenderer.mc.renderEngine.bindTexture(PreviewRenderer.WHITE_TEXTURE);
        
        builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_NORMAL);
        BoxCorner corner = face.corners[0];
        Axis uAxis = face.getTexUAxis();
        Axis vAxis = face.getTexVAxis();
        double modX1 = 0;
        double modX2 = 0;
        double modZ1 = 0;
        double modZ2 = 0;
        switch (facing) {
        case NORTH:
            modX1 = 0;
            modX2 = -0.0155;
            break;
        case EAST:
            
            break;
        case SOUTH:
            corner = face.corners[3];
            modX1 = 0;
            modX2 = -0.0155;
            break;
        case WEST:
            
            break;
        case UP:
            
            break;
        case DOWN:
            
            break;
        
        default:
            break;
        }
        
        builder.pos(box.getValueOfFacing(corner.x) - x + modX1, box.getValueOfFacing(corner.y) - y, box.getValueOfFacing(corner.z) - z)
                
                .tex(corner.isFacingPositive(uAxis) != (VectorUtils.get(uAxis, topRight) > 0) ? 1 : 0, corner
                        .isFacingPositive(vAxis) != (VectorUtils.get(vAxis, topRight) > 0) ? 1 : 0)
                .endVertex();
        builder.pos(box.getValueOfFacing(corner.x) - x + modX1, box.getValueOfFacing(corner.y) - 0.0155 - y, box.getValueOfFacing(corner.z) - z)
                
                .tex(corner.isFacingPositive(uAxis) != (VectorUtils.get(uAxis, topRight) > 0) ? 1 : 0, corner
                        .isFacingPositive(vAxis) != (VectorUtils.get(vAxis, topRight) > 0) ? 1 : 0)
                .endVertex();
        builder.pos(box.getValueOfFacing(corner.x) + modX2 - x, box.getValueOfFacing(corner.y) - 0.0155 - y, box.getValueOfFacing(corner.z) - z)
                
                .tex(corner.isFacingPositive(uAxis) != (VectorUtils.get(uAxis, topRight) > 0) ? 1 : 0, corner
                        .isFacingPositive(vAxis) != (VectorUtils.get(vAxis, topRight) > 0) ? 1 : 0)
                .endVertex();
        builder.pos(box.getValueOfFacing(corner.x) + modX2 - x, box.getValueOfFacing(corner.y) - y, box.getValueOfFacing(corner.z) - z)
                
                .tex(corner.isFacingPositive(uAxis) != (VectorUtils.get(uAxis, topRight) > 0) ? 1 : 0, corner
                        .isFacingPositive(vAxis) != (VectorUtils.get(vAxis, topRight) > 0) ? 1 : 0)
                .endVertex();
        
        tessellator.draw();
        
    }
    
    public void drawVerticalLine(double x, double y, double z) {
        
        AlignedBox box = frame.getBox().getCube(frame.getContext());
        BoxFace face = BoxFace.get(facing);
        if (facing.getAxisDirection() == AxisDirection.POSITIVE)
            box.setMax(facing.getAxis(), box.getMin(facing.getAxis()) + 0.01F);
        else
            box.setMin(facing.getAxis(), box.getMax(facing.getAxis()) - 0.01F);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder builder = tessellator.getBuffer();
        
        PreviewRenderer.mc.renderEngine.bindTexture(PreviewRenderer.WHITE_TEXTURE);
        
        builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_NORMAL);
        BoxCorner corner = face.corners[0];
        Axis uAxis = face.getTexUAxis();
        Axis vAxis = face.getTexVAxis();
        double modX1 = 0;
        double modX2 = 0;
        double modZ1 = 0;
        double modZ2 = 0;
        switch (facing) {
        case NORTH:
            modX1 = -0.0155;
            modX2 = -0.031;
            break;
        case EAST:
            
            break;
        case SOUTH:
            corner = face.corners[3];
            modX1 = -0.031;
            modX2 = -0.0155;
            break;
        case WEST:
            
            break;
        case UP:
            
            break;
        case DOWN:
            
            break;
        
        default:
            break;
        }
        
        builder.pos(box.getValueOfFacing(corner.x) - x + modX1, box.getValueOfFacing(corner.y) - y, box.getValueOfFacing(corner.z) - z)
                
                .tex(corner.isFacingPositive(uAxis) != (VectorUtils.get(uAxis, topRight) > 0) ? 1 : 0, corner
                        .isFacingPositive(vAxis) != (VectorUtils.get(vAxis, topRight) > 0) ? 1 : 0)
                .endVertex();
        builder.pos(box.getValueOfFacing(corner.x) - x + modX1, box.getValueOfFacing(corner.y) - 0.031 - y, box.getValueOfFacing(corner.z) - z)
                
                .tex(corner.isFacingPositive(uAxis) != (VectorUtils.get(uAxis, topRight) > 0) ? 1 : 0, corner
                        .isFacingPositive(vAxis) != (VectorUtils.get(vAxis, topRight) > 0) ? 1 : 0)
                .endVertex();
        builder.pos(box.getValueOfFacing(corner.x) + modX2 - x, box.getValueOfFacing(corner.y) - 0.031 - y, box.getValueOfFacing(corner.z) - z)
                
                .tex(corner.isFacingPositive(uAxis) != (VectorUtils.get(uAxis, topRight) > 0) ? 1 : 0, corner
                        .isFacingPositive(vAxis) != (VectorUtils.get(vAxis, topRight) > 0) ? 1 : 0)
                .endVertex();
        builder.pos(box.getValueOfFacing(corner.x) + modX2 - x, box.getValueOfFacing(corner.y) - y, box.getValueOfFacing(corner.z) - z)
                
                .tex(corner.isFacingPositive(uAxis) != (VectorUtils.get(uAxis, topRight) > 0) ? 1 : 0, corner
                        .isFacingPositive(vAxis) != (VectorUtils.get(vAxis, topRight) > 0) ? 1 : 0)
                .endVertex();
        
        tessellator.draw();
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void renderTick(BlockPos pos, double x, double y, double z, float partialTickTime) {
        
        GlStateManager.enableBlend();
        GlStateManager.disableLighting();
        
        GlStateManager.pushMatrix();
        
        GlStateManager.translate(x, y, z);
        
        GlStateManager.enableRescaleNormal();
        float c = 0.135F;
        GlStateManager.color(1.0F, 1.0F, 1.0F);
        drawHorizontalLine(0.0155, 0.0155, 0);
        GlStateManager.color(c, c, c);
        drawVerticalLine(0, 0.031, 0);
        GlStateManager.color(1.0F, 1.0F, 1.0F);
        drawVerticalLine(0.0465, 0.031, 0);
        GlStateManager.color(1.0F, 1.0F, 1.0F);
        drawHorizontalLine(0.0155, 0.062, 0);
        GlStateManager.color(c, c, c);
        drawVerticalLine(0, 0.0775, 0);
        
        GlStateManager.color(1.0F, 1.0F, 1.0F);
        drawVerticalLine(0.0465, 0.0775, 0);
        drawHorizontalLine(0.0155, 0.1085, 0);
        drawDecimalPoint(0.0775, 0.124, 0);
        // drawHorizontalLine(box, face, 0, 0.025, 0);
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
    
}
