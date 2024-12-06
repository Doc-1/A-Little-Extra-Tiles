package com.alet.components.structures.type.premade.signal;

import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import org.lwjgl.opengl.GL11;

import com.creativemd.creativecore.common.utils.math.Rotation;
import com.creativemd.creativecore.common.utils.math.RotationUtils;
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
        double x1 = -0.0155;
        double x2 = -0.0465;
        double y1 = 0;
        double y2 = -0.0155;
        double z1 = 0;
        double z2 = 0;
        Vector3d vec1 = new Vector3d(x1 - x, y1 - y, z1 - z);
        Vector3d vec2 = new Vector3d(x1 - x, y2 - y, z1 - z);
        Vector3d vec3 = new Vector3d(x2 - x, y2 - y, z2 - z);
        Vector3d vec4 = new Vector3d(x2 - x, y1 - y, z2 - z);
        switch (facing) {
        case NORTH:
            break;
        case EAST:
            RotationUtils.rotate(vec1, Rotation.Y_COUNTER_CLOCKWISE);
            RotationUtils.rotate(vec2, Rotation.Y_COUNTER_CLOCKWISE);
            RotationUtils.rotate(vec3, Rotation.Y_COUNTER_CLOCKWISE);
            RotationUtils.rotate(vec4, Rotation.Y_COUNTER_CLOCKWISE);
            break;
        case SOUTH:
            
            RotationUtils.rotate(vec1, Rotation.Y_COUNTER_CLOCKWISE);
            RotationUtils.rotate(vec2, Rotation.Y_COUNTER_CLOCKWISE);
            RotationUtils.rotate(vec3, Rotation.Y_COUNTER_CLOCKWISE);
            RotationUtils.rotate(vec4, Rotation.Y_COUNTER_CLOCKWISE);
            RotationUtils.rotate(vec1, Rotation.Y_COUNTER_CLOCKWISE);
            RotationUtils.rotate(vec2, Rotation.Y_COUNTER_CLOCKWISE);
            RotationUtils.rotate(vec3, Rotation.Y_COUNTER_CLOCKWISE);
            RotationUtils.rotate(vec4, Rotation.Y_COUNTER_CLOCKWISE);
            break;
        case WEST:
            RotationUtils.rotate(vec1, Rotation.Y_CLOCKWISE);
            RotationUtils.rotate(vec2, Rotation.Y_CLOCKWISE);
            RotationUtils.rotate(vec3, Rotation.Y_CLOCKWISE);
            RotationUtils.rotate(vec4, Rotation.Y_CLOCKWISE);
            break;
        case UP:
            
            break;
        case DOWN:
            
            break;
        
        default:
            break;
        }
        builder.pos(box.getValueOfFacing(corner.x) + vec1.x, box.getValueOfFacing(corner.y) + vec1.y, box.getValueOfFacing(corner.z) + vec1.z)
                
                .tex(corner.isFacingPositive(uAxis) != (VectorUtils.get(uAxis, topRight) > 0) ? 1 : 0, corner
                        .isFacingPositive(vAxis) != (VectorUtils.get(vAxis, topRight) > 0) ? 1 : 0)
                .endVertex();
        builder.pos(box.getValueOfFacing(corner.x) + vec2.x, box.getValueOfFacing(corner.y) + vec2.y, box.getValueOfFacing(corner.z) + vec2.z)
                
                .tex(corner.isFacingPositive(uAxis) != (VectorUtils.get(uAxis, topRight) > 0) ? 1 : 0, corner
                        .isFacingPositive(vAxis) != (VectorUtils.get(vAxis, topRight) > 0) ? 1 : 0)
                .endVertex();
        builder.pos(box.getValueOfFacing(corner.x) + vec3.x, box.getValueOfFacing(corner.y) + vec3.y, box.getValueOfFacing(corner.z) + vec3.z)
                
                .tex(corner.isFacingPositive(uAxis) != (VectorUtils.get(uAxis, topRight) > 0) ? 1 : 0, corner
                        .isFacingPositive(vAxis) != (VectorUtils.get(vAxis, topRight) > 0) ? 1 : 0)
                .endVertex();
        builder.pos(box.getValueOfFacing(corner.x) + vec4.x, box.getValueOfFacing(corner.y) + vec4.y, box.getValueOfFacing(corner.z) + vec4.z)
                
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
        double x1 = 0;
        double x2 = -0.0155;
        double y1 = 0;
        double y2 = -0.0155;
        double z1 = 0;
        double z2 = 0;
        Vector3d vec1 = new Vector3d(x1 - x, y1 - y, z1 - z);
        Vector3d vec2 = new Vector3d(x1 - x, y2 - y, z1 - z);
        Vector3d vec3 = new Vector3d(x2 - x, y2 - y, z2 - z);
        Vector3d vec4 = new Vector3d(x2 - x, y1 - y, z2 - z);
        switch (facing) {
        case NORTH:
            break;
        case EAST:
            RotationUtils.rotate(vec1, Rotation.Y_COUNTER_CLOCKWISE);
            RotationUtils.rotate(vec2, Rotation.Y_COUNTER_CLOCKWISE);
            RotationUtils.rotate(vec3, Rotation.Y_COUNTER_CLOCKWISE);
            RotationUtils.rotate(vec4, Rotation.Y_COUNTER_CLOCKWISE);
            break;
        case SOUTH:
            
            RotationUtils.rotate(vec1, Rotation.Y_COUNTER_CLOCKWISE);
            RotationUtils.rotate(vec2, Rotation.Y_COUNTER_CLOCKWISE);
            RotationUtils.rotate(vec3, Rotation.Y_COUNTER_CLOCKWISE);
            RotationUtils.rotate(vec4, Rotation.Y_COUNTER_CLOCKWISE);
            RotationUtils.rotate(vec1, Rotation.Y_COUNTER_CLOCKWISE);
            RotationUtils.rotate(vec2, Rotation.Y_COUNTER_CLOCKWISE);
            RotationUtils.rotate(vec3, Rotation.Y_COUNTER_CLOCKWISE);
            RotationUtils.rotate(vec4, Rotation.Y_COUNTER_CLOCKWISE);
            break;
        case WEST:
            RotationUtils.rotate(vec1, Rotation.Y_CLOCKWISE);
            RotationUtils.rotate(vec2, Rotation.Y_CLOCKWISE);
            RotationUtils.rotate(vec3, Rotation.Y_CLOCKWISE);
            RotationUtils.rotate(vec4, Rotation.Y_CLOCKWISE);
            break;
        case UP:
            
            break;
        case DOWN:
            
            break;
        
        default:
            break;
        }
        builder.pos(box.getValueOfFacing(corner.x) + vec1.x, box.getValueOfFacing(corner.y) + vec1.y, box.getValueOfFacing(corner.z) + vec1.z)
                
                .tex(corner.isFacingPositive(uAxis) != (VectorUtils.get(uAxis, topRight) > 0) ? 1 : 0, corner
                        .isFacingPositive(vAxis) != (VectorUtils.get(vAxis, topRight) > 0) ? 1 : 0)
                .endVertex();
        builder.pos(box.getValueOfFacing(corner.x) + vec2.x, box.getValueOfFacing(corner.y) + vec2.y, box.getValueOfFacing(corner.z) + vec2.z)
                
                .tex(corner.isFacingPositive(uAxis) != (VectorUtils.get(uAxis, topRight) > 0) ? 1 : 0, corner
                        .isFacingPositive(vAxis) != (VectorUtils.get(vAxis, topRight) > 0) ? 1 : 0)
                .endVertex();
        builder.pos(box.getValueOfFacing(corner.x) + vec3.x, box.getValueOfFacing(corner.y) + vec3.y, box.getValueOfFacing(corner.z) + vec3.z)
                
                .tex(corner.isFacingPositive(uAxis) != (VectorUtils.get(uAxis, topRight) > 0) ? 1 : 0, corner
                        .isFacingPositive(vAxis) != (VectorUtils.get(vAxis, topRight) > 0) ? 1 : 0)
                .endVertex();
        builder.pos(box.getValueOfFacing(corner.x) + vec4.x, box.getValueOfFacing(corner.y) + vec4.y, box.getValueOfFacing(corner.z) + vec4.z)
                
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
        double x1 = -0.0155;
        double x2 = -0.031;
        double y1 = 0;
        double y2 = -0.031;
        double z1 = 0;
        double z2 = 0;
        Vector3d vec1 = new Vector3d(x1 - x, y1 - y, z1 - z);
        Vector3d vec2 = new Vector3d(x1 - x, y2 - y, z1 - z);
        Vector3d vec3 = new Vector3d(x2 - x, y2 - y, z2 - z);
        Vector3d vec4 = new Vector3d(x2 - x, y1 - y, z2 - z);
        switch (facing) {
        case NORTH:
            break;
        case EAST:
            RotationUtils.rotate(vec1, Rotation.Y_COUNTER_CLOCKWISE);
            RotationUtils.rotate(vec2, Rotation.Y_COUNTER_CLOCKWISE);
            RotationUtils.rotate(vec3, Rotation.Y_COUNTER_CLOCKWISE);
            RotationUtils.rotate(vec4, Rotation.Y_COUNTER_CLOCKWISE);
            break;
        case SOUTH:
            
            RotationUtils.rotate(vec1, Rotation.Y_COUNTER_CLOCKWISE);
            RotationUtils.rotate(vec2, Rotation.Y_COUNTER_CLOCKWISE);
            RotationUtils.rotate(vec3, Rotation.Y_COUNTER_CLOCKWISE);
            RotationUtils.rotate(vec4, Rotation.Y_COUNTER_CLOCKWISE);
            RotationUtils.rotate(vec1, Rotation.Y_COUNTER_CLOCKWISE);
            RotationUtils.rotate(vec2, Rotation.Y_COUNTER_CLOCKWISE);
            RotationUtils.rotate(vec3, Rotation.Y_COUNTER_CLOCKWISE);
            RotationUtils.rotate(vec4, Rotation.Y_COUNTER_CLOCKWISE);
            break;
        case WEST:
            RotationUtils.rotate(vec1, Rotation.Y_CLOCKWISE);
            RotationUtils.rotate(vec2, Rotation.Y_CLOCKWISE);
            RotationUtils.rotate(vec3, Rotation.Y_CLOCKWISE);
            RotationUtils.rotate(vec4, Rotation.Y_CLOCKWISE);
            break;
        case UP:
            
            break;
        case DOWN:
            
            break;
        
        default:
            break;
        }
        builder.pos(box.getValueOfFacing(corner.x) + vec1.x, box.getValueOfFacing(corner.y) + vec1.y, box.getValueOfFacing(corner.z) + vec1.z)
                
                .tex(corner.isFacingPositive(uAxis) != (VectorUtils.get(uAxis, topRight) > 0) ? 1 : 0, corner
                        .isFacingPositive(vAxis) != (VectorUtils.get(vAxis, topRight) > 0) ? 1 : 0)
                .endVertex();
        builder.pos(box.getValueOfFacing(corner.x) + vec2.x, box.getValueOfFacing(corner.y) + vec2.y, box.getValueOfFacing(corner.z) + vec2.z)
                
                .tex(corner.isFacingPositive(uAxis) != (VectorUtils.get(uAxis, topRight) > 0) ? 1 : 0, corner
                        .isFacingPositive(vAxis) != (VectorUtils.get(vAxis, topRight) > 0) ? 1 : 0)
                .endVertex();
        builder.pos(box.getValueOfFacing(corner.x) + vec3.x, box.getValueOfFacing(corner.y) + vec3.y, box.getValueOfFacing(corner.z) + vec3.z)
                
                .tex(corner.isFacingPositive(uAxis) != (VectorUtils.get(uAxis, topRight) > 0) ? 1 : 0, corner
                        .isFacingPositive(vAxis) != (VectorUtils.get(vAxis, topRight) > 0) ? 1 : 0)
                .endVertex();
        builder.pos(box.getValueOfFacing(corner.x) + vec4.x, box.getValueOfFacing(corner.y) + vec4.y, box.getValueOfFacing(corner.z) + vec4.z)
                
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
        if (this.getOutput(0).getState()[0])
            GlStateManager.color(1.0F, 1.0F, 1.0F);
        else
            GlStateManager.color(c, c, c);
        drawHorizontalLine(0.0155, 0.0155, 0);
        
        if (this.getOutput(1).getState()[0])
            GlStateManager.color(1.0F, 1.0F, 1.0F);
        else
            GlStateManager.color(c, c, c);
        drawVerticalLine(0.0465, 0.031, 0);
        
        if (this.getOutput(2).getState()[0])
            GlStateManager.color(1.0F, 1.0F, 1.0F);
        else
            GlStateManager.color(c, c, c);
        drawVerticalLine(0.0465, 0.0775, 0);
        
        if (this.getOutput(3).getState()[0])
            GlStateManager.color(1.0F, 1.0F, 1.0F);
        else
            GlStateManager.color(c, c, c);
        drawHorizontalLine(0.0155, 0.1085, 0);
        
        if (this.getOutput(4).getState()[0])
            GlStateManager.color(1.0F, 1.0F, 1.0F);
        else
            GlStateManager.color(c, c, c);
        drawVerticalLine(0, 0.0775, 0);
        
        if (this.getOutput(5).getState()[0])
            GlStateManager.color(1.0F, 1.0F, 1.0F);
        else
            GlStateManager.color(c, c, c);
        drawVerticalLine(0, 0.031, 0);
        
        if (this.getOutput(6).getState()[0])
            GlStateManager.color(1.0F, 1.0F, 1.0F);
        else
            GlStateManager.color(c, c, c);
        drawHorizontalLine(0.0155, 0.062, 0);
        
        if (this.getOutput(7).getState()[0])
            GlStateManager.color(1.0F, 1.0F, 1.0F);
        else
            GlStateManager.color(c, c, c);
        drawDecimalPoint(0.0775, 0.124, 0);
        
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
