package com.alet.common.structure.type.premade.signal;

import java.util.ArrayList;
import java.util.List;

import com.creativemd.creativecore.client.rendering.RenderBox;
import com.creativemd.creativecore.common.utils.math.RotationUtils;
import com.creativemd.creativecore.common.utils.math.box.AlignedBox;
import com.creativemd.creativecore.common.utils.mc.ColorUtils;
import com.creativemd.littletiles.LittleTiles;
import com.creativemd.littletiles.client.render.tile.LittleRenderBox;
import com.creativemd.littletiles.common.structure.LittleStructure;
import com.creativemd.littletiles.common.structure.registry.LittleStructureType;
import com.creativemd.littletiles.common.structure.signal.component.SignalComponentType;
import com.creativemd.littletiles.common.structure.type.premade.signal.LittleSignalOutput;
import com.creativemd.littletiles.common.tile.math.box.LittleBox;
import com.creativemd.littletiles.common.tile.parent.IStructureTileList;
import com.creativemd.littletiles.common.tile.place.PlacePreview;
import com.creativemd.littletiles.common.tile.place.PlacePreviewFacing;
import com.creativemd.littletiles.common.tile.preview.LittlePreviews;
import com.creativemd.littletiles.common.util.grid.LittleGridContext;
import com.creativemd.littletiles.common.util.vec.SurroundingBox;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.EnumFacing.AxisDirection;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class LittleSignalOutputQuick extends LittleSignalOutput {
    
    public LittleSignalOutputQuick(LittleStructureType type, IStructureTileList mainBlock) {
        super(type, mainBlock);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void renderFace(EnumFacing facing, LittleGridContext context, LittleBox renderBox, int distance, Axis axis, Axis one, Axis two, boolean positive, boolean oneSidedRenderer, List<LittleRenderBox> cubes) {
        initRenderFace(facing, context, renderBox, distance, axis, one, two, positive, oneSidedRenderer, cubes);
        LittleRenderBox cube = renderBox.getRenderingCube(context, LittleTiles.outputArrow, facing.ordinal());
        //cube.color = color;
        cube.keepVU = true;
        cube.allowOverlap = true;
        
        if (positive) {
            cube.setMin(axis, cube.getMax(axis));
            cube.setMax(axis, cube.getMax(axis) + (float) context.toVanillaGrid(renderBox.getSize(axis)) * 0.7F);
        } else {
            cube.setMax(axis, cube.getMin(axis));
            cube.setMin(axis, cube.getMin(axis) - (float) context.toVanillaGrid(renderBox.getSize(axis)) * 0.7F);
        }
        float shrink = 0.14F;
        float shrinkOne = cube.getSize(one) * shrink;
        float shrinkTwo = cube.getSize(two) * shrink;
        cube.setMin(one, cube.getMin(one) + shrinkOne);
        cube.setMax(one, cube.getMax(one) - shrinkOne);
        cube.setMin(two, cube.getMin(two) + shrinkTwo);
        cube.setMax(two, cube.getMax(two) - shrinkTwo);
        cubes.add(cube);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void render(SurroundingBox box, LittleBox overallBox, List<LittleRenderBox> cubes) {
        initRender(box, overallBox, cubes);
        AlignedBox structureBox = new AlignedBox(overallBox.getBox(box.getContext()));
        LittleRenderBox block = new LittleRenderBox(structureBox, null, LittleTiles.dyeableBlock, 0).setColor(color);
        block.allowOverlap = true;
        cubes.add(block);
        
        Axis axis = facing.getAxis();
        
        RenderBox cube = new RenderBox(structureBox, LittleTiles.dyeableBlock, 0);
        
        float thickness = cube.getSize(axis) * 0.12F;
        float sizePercentage = 0.25F;
        
        Axis one = RotationUtils.getOne(axis);
        Axis two = RotationUtils.getTwo(axis);
        
        float sizeOne = cube.getSize(one);
        float sizeTwo = cube.getSize(two);
        
        float middlePart = 0.1F;
        float sizeOneInside = sizeOne - sizeOne * sizePercentage * 2;
        float sizeTwoInside = sizeTwo - sizeTwo * sizePercentage * 2;
        
        if (facing.getAxisDirection() == AxisDirection.POSITIVE) {
            cube.setMin(axis, cube.getMax(axis));
            cube.setMax(axis, cube.getMax(axis) + thickness);
        } else {
            cube.setMax(axis, cube.getMin(axis));
            cube.setMin(axis, cube.getMin(axis) - thickness);
        }
        
        LittleRenderBox left = new LittleRenderBox(cube, null, LittleTiles.dyeableBlock, 0).setColor(ColorUtils.ORANGE);
        
        left.setMin(one, left.getMin(one) + sizeOne * sizePercentage);
        left.setMax(one, left.getMin(one) + sizeOneInside * middlePart);
        
        left.setMin(two, left.getMin(two) + sizeTwo * sizePercentage);
        left.setMax(two, left.getMax(two) - sizeTwo * sizePercentage);
        
        cubes.add(left);
        
        LittleRenderBox right = new LittleRenderBox(cube, null, LittleTiles.dyeableBlock, 0).setColor(ColorUtils.ORANGE);
        
        right.setMax(one, right.getMax(one) - sizeOne * sizePercentage);
        right.setMin(one, right.getMax(one) - sizeOneInside * middlePart);
        
        right.setMin(two, right.getMin(two) + sizeTwo * sizePercentage);
        right.setMax(two, right.getMax(two) - sizeTwo * sizePercentage);
        
        cubes.add(right);
        
        LittleRenderBox bottom = new LittleRenderBox(cube, null, LittleTiles.dyeableBlock, 0).setColor(ColorUtils.ORANGE);
        
        bottom.setMin(one, bottom.getMin(one) + sizeOne * sizePercentage);
        bottom.setMax(one, bottom.getMax(one) - sizeOne * sizePercentage);
        
        bottom.setMin(two, bottom.getMin(two) + sizeTwo * sizePercentage);
        bottom.setMax(two, bottom.getMin(two) + sizeTwoInside * middlePart);
        
        cubes.add(bottom);
        
        LittleRenderBox top = new LittleRenderBox(cube, null, LittleTiles.dyeableBlock, 0).setColor(ColorUtils.ORANGE);
        
        top.setMin(one, top.getMin(one) + sizeOne * sizePercentage);
        top.setMax(one, top.getMax(one) - sizeOne * sizePercentage);
        
        top.setMax(two, top.getMax(two) - sizeTwo * sizePercentage);
        top.setMin(two, top.getMax(two) - sizeTwoInside * middlePart);
        
        cubes.add(top);
        
    }
    
    @SideOnly(Side.CLIENT)
    public void initRenderFace(EnumFacing facing, LittleGridContext context, LittleBox renderBox, int distance, Axis axis, Axis one, Axis two, boolean positive, boolean oneSidedRenderer, List<LittleRenderBox> cubes) {
        if (positive) {
            renderBox.setMin(axis, renderBox.getMax(axis));
            renderBox.setMax(axis, renderBox.getMax(axis) + distance);
        } else {
            renderBox.setMax(axis, renderBox.getMin(axis));
            renderBox.setMin(axis, renderBox.getMin(axis) - distance);
        }
        
        LittleRenderBox cube = renderBox.getRenderingCube(context, LittleTiles.singleCable, axis.ordinal());
        if (!oneSidedRenderer) {
            if (positive)
                cube.setMax(axis, cube.getMin(axis) + cube.getSize(axis) / 2);
            else
                cube.setMin(axis, cube.getMax(axis) - cube.getSize(axis) / 2);
        }
        
        cube.color = color;
        cube.keepVU = true;
        cube.allowOverlap = true;
        float shrink = 0.18F;
        float shrinkOne = cube.getSize(one) * shrink;
        float shrinkTwo = cube.getSize(two) * shrink;
        cube.setMin(one, cube.getMin(one) + shrinkOne);
        cube.setMax(one, cube.getMax(one) - shrinkOne);
        cube.setMin(two, cube.getMin(two) + shrinkTwo);
        cube.setMax(two, cube.getMax(two) - shrinkTwo);
        cubes.add(cube);
    }
    
    @SideOnly(Side.CLIENT)
    public void initRender(SurroundingBox box, LittleBox overallBox, List<LittleRenderBox> cubes) {
        
        for (int i = 0; i < faces.length; i++) {
            if (faces[i] == null)
                continue;
            
            int distance = faces[i].distance;
            EnumFacing facing = getFacing(i);
            
            Axis axis = facing.getAxis();
            Axis one = RotationUtils.getOne(axis);
            Axis two = RotationUtils.getTwo(axis);
            boolean positive = facing.getAxisDirection() == AxisDirection.POSITIVE;
            LittleGridContext context = faces[i].context;
            
            LittleBox renderBox = overallBox.copy();
            
            if (box.getContext().size > context.size) {
                distance *= box.getContext().size / context.size;
                context = box.getContext();
            } else if (context.size > box.getContext().size)
                renderBox.convertTo(box.getContext(), context);
            
            initRenderFace(facing, context, renderBox, distance, axis, one, two, positive, faces[i].oneSidedRenderer, cubes);
        }
    }
    
    public static class LittleStructureTypeOutputQuick extends LittleStructureTypeNetwork {
        
        public LittleStructureTypeOutputQuick(String id, String category, Class<? extends LittleStructure> structureClass, int attribute, String modid, int bandwidth) {
            super(id, category, structureClass, attribute, modid, bandwidth, 1);
        }
        
        @Override
        public List<PlacePreview> getSpecialTiles(LittlePreviews previews) {
            List<PlacePreview> result = super.getSpecialTiles(previews);
            EnumFacing facing = (EnumFacing) loadDirectional(previews, "facing");
            LittleBox box = previews.getSurroundingBox();
            result.add(new PlacePreviewFacing(box, facing, ColorUtils.RED));
            return result;
        }
        
        @Override
        @SideOnly(Side.CLIENT)
        public List<RenderBox> getRenderingCubes(LittlePreviews previews) {
            List<RenderBox> cubes = new ArrayList<>();
            float size = (float) ((Math.sqrt(bandwidth) * 1F / 32F + 0.05) * 1.4);
            cubes = new ArrayList<>();
            cubes.add(new RenderBox(0, 0.5F - size, 0.5F - size, size * 2, 0.5F + size, 0.5F + size, LittleTiles.dyeableBlock).setColor(getColor(previews)));
            cubes.add(new RenderBox(size * 2, 0.5F - size, 0.5F - size, size * 2.5F, 0.5F + size, 0.5F + size, LittleTiles.dyeableBlock).setColor(ColorUtils.RED));
            return cubes;
        }
        
        @Override
        public int getBandwidth() {
            return bandwidth;
        }
        
        @Override
        public void changed() {
            
        }
        
        @Override
        public boolean[] getState() {
            return null;
        }
        
        @Override
        public SignalComponentType getType() {
            return SignalComponentType.OUTPUT;
        }
        
    }
    
}
