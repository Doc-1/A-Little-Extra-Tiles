package com.alet.common.structure.type.premade.signal;

import java.util.ArrayList;
import java.util.List;

import com.alet.common.util.StructureUtils;
import com.creativemd.creativecore.client.rendering.RenderBox;
import com.creativemd.creativecore.common.utils.math.RotationUtils;
import com.creativemd.creativecore.common.utils.math.box.AlignedBox;
import com.creativemd.creativecore.common.utils.mc.ColorUtils;
import com.creativemd.littletiles.LittleTiles;
import com.creativemd.littletiles.client.gui.handler.LittleStructureGuiHandler;
import com.creativemd.littletiles.client.render.tile.LittleRenderBox;
import com.creativemd.littletiles.common.action.LittleActionException;
import com.creativemd.littletiles.common.action.block.LittleActionActivated;
import com.creativemd.littletiles.common.structure.LittleStructure;
import com.creativemd.littletiles.common.structure.directional.StructureDirectional;
import com.creativemd.littletiles.common.structure.registry.LittleStructureType;
import com.creativemd.littletiles.common.structure.relative.StructureRelative;
import com.creativemd.littletiles.common.structure.signal.component.SignalComponentType;
import com.creativemd.littletiles.common.structure.type.premade.signal.LittleSignalInput;
import com.creativemd.littletiles.common.tile.LittleTile;
import com.creativemd.littletiles.common.tile.math.box.LittleBox;
import com.creativemd.littletiles.common.tile.math.vec.LittleVec;
import com.creativemd.littletiles.common.tile.parent.IStructureTileList;
import com.creativemd.littletiles.common.tile.place.PlacePreview;
import com.creativemd.littletiles.common.tile.place.PlacePreviewFacing;
import com.creativemd.littletiles.common.tile.preview.LittlePreviews;
import com.creativemd.littletiles.common.util.grid.LittleGridContext;
import com.creativemd.littletiles.common.util.vec.SurroundingBox;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.EnumFacing.AxisDirection;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class LittleSignalInputQuick extends LittleSignalInput {
    
    @StructureDirectional(color = ColorUtils.GREEN)
    public StructureRelative frame;
    
    private BlockPos location = null;
    private LittleStructure listeningStructure;
    
    public LittleSignalInputQuick(LittleStructureType type, IStructureTileList mainBlock) {
        super(type, mainBlock);
    }
    
    @Override
    public boolean onBlockActivated(World worldIn, LittleTile tile, BlockPos pos, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ, LittleActionActivated action) throws LittleActionException {
        if (this.getParent() == null) {
            findConnection();
            if (listeningStructure != null) {
                if (StructureUtils.mergeChildToStructure(this, listeningStructure, true, worldIn, pos, new LittleVec(0, 0, 0), side, playerIn))
                    playerIn.sendStatusMessage(new TextComponentString("Connection was succesful, right to open interface."), true);
                
            }
        }
        if (!playerIn.isSneaking()) {
            if (!worldIn.isRemote && this.getParent() != null)
                LittleStructureGuiHandler.openGui("signal_interface", new NBTTagCompound(), playerIn, this);
        } else if (this.getParent() != null) {
            if (StructureUtils.removeThisDynamicChild(this))
                playerIn.sendStatusMessage(new TextComponentString("Connection succesfully disconnected."), true);
            else
                playerIn.sendStatusMessage(new TextComponentString("Connection failed to disconnected."), true);
            //   System.out.println(SignalingUtils.randState(4));
        }
        return true;
    }
    
    public void findConnection() {
        World worldIn = this.getWorld();
        LittleBox box = this.frame.getBox();
        if (location == null) {
            this.location = new BlockPos(this.getPos().getX(), this.getPos().getY(), this.getPos().getZ());
        }
        if (this.facing.equals(EnumFacing.WEST)) {
            if (box.maxX >= (this.frame.getContext().size + 1)) {
                box.maxX = 1;
                box.minX = 0;
                location = location.east();
            }
        } else if (this.facing.equals(EnumFacing.EAST)) {
            if (box.minX <= -1) {
                box.maxX = 32;
                box.minX = 31;
                location = location.west();
            }
        } else if (this.facing.equals(EnumFacing.NORTH)) {
            if (box.maxZ >= (this.frame.getContext().size + 1)) {
                box.maxZ = 1;
                box.minZ = 0;
                location = location.south();
            }
        } else if (this.facing.equals(EnumFacing.SOUTH)) {
            if (box.minZ <= -1) {
                box.maxZ = 32;
                box.minZ = 31;
                location = location.north();
            }
        } else if (this.facing.equals(EnumFacing.UP)) {
            if (box.minY <= -1) {
                box.maxY = 32;
                box.minY = 31;
                location = location.down();
            }
        } else if (this.facing.equals(EnumFacing.DOWN)) {
            if (box.minY >= (this.frame.getContext().size + 1)) {
                box.maxY = 1;
                box.minY = 0;
                location = location.up();
            }
        }
        listeningStructure = StructureUtils.getStructureAt(worldIn, box, location, this);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void renderFace(EnumFacing facing, LittleGridContext context, LittleBox renderBox, int distance, Axis axis, Axis one, Axis two, boolean positive, boolean oneSidedRenderer, List<LittleRenderBox> cubes) {
        super.renderFace(facing, context, renderBox.copy(), distance, axis, one, two, positive, oneSidedRenderer, cubes);
        LittleRenderBox cube = renderBox.getRenderingCube(context, LittleTiles.inputArrow, facing.ordinal());
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
            
            renderFace(facing, context, renderBox, distance, axis, one, two, positive, faces[i].oneSidedRenderer, cubes);
        }
        AlignedBox cube = new AlignedBox(overallBox.getBox(box.getContext()));
        
        Axis axis = facing.getAxis();
        
        float sizePercentage = 0.25F;
        
        Axis one = RotationUtils.getOne(axis);
        Axis two = RotationUtils.getTwo(axis);
        
        float sizeOne = cube.getSize(one);
        float sizeTwo = cube.getSize(two);
        float sizeAxis = cube.getSize(axis);
        
        LittleRenderBox top = (LittleRenderBox) new LittleRenderBox(cube, null, LittleTiles.dyeableBlock, 0).setColor(color);
        top.allowOverlap = true;
        top.setMin(one, top.getMax(one) - sizeOne * sizePercentage);
        cubes.add(top);
        
        LittleRenderBox bottom = (LittleRenderBox) new LittleRenderBox(cube, null, LittleTiles.dyeableBlock, 0).setColor(color);
        bottom.allowOverlap = true;
        bottom.setMax(one, bottom.getMin(one) + sizeOne * sizePercentage);
        cubes.add(bottom);
        
        LittleRenderBox left = (LittleRenderBox) new LittleRenderBox(cube, null, LittleTiles.dyeableBlock, 0).setColor(color);
        left.allowOverlap = true;
        left.setMin(two, top.getMax(two) - sizeTwo * sizePercentage);
        cubes.add(left);
        
        LittleRenderBox right = (LittleRenderBox) new LittleRenderBox(cube, null, LittleTiles.dyeableBlock, 0).setColor(color);
        right.allowOverlap = true;
        right.setMax(two, right.getMin(two) + sizeTwo * sizePercentage);
        cubes.add(right);
        
        LittleRenderBox behind = (LittleRenderBox) new LittleRenderBox(cube, null, LittleTiles.dyeableBlock, 0).setColor(color);
        behind.allowOverlap = true;
        
        float depth = sizeAxis * 0.12F;
        
        behind.setMin(one, behind.getMin(one) + sizeOne * sizePercentage);
        behind.setMax(one, behind.getMax(one) - sizeOne * sizePercentage);
        
        behind.setMin(two, behind.getMin(two) + sizeTwo * sizePercentage);
        behind.setMax(two, behind.getMax(two) - sizeTwo * sizePercentage);
        
        if (facing.getAxisDirection() == AxisDirection.POSITIVE)
            behind.setMax(axis, behind.getMin(axis) + sizeAxis * 0.5F);
        else
            behind.setMin(axis, behind.getMax(axis) - sizeAxis * 0.5F);
        cubes.add(behind);
        
        LittleRenderBox front = (LittleRenderBox) new LittleRenderBox(cube, null, LittleTiles.dyeableBlock, 0).setColor(ColorUtils.LIGHT_BLUE);
        
        front.allowOverlap = true;
        
        front.setMin(one, front.getMin(one) + sizeOne * sizePercentage);
        front.setMax(one, front.getMax(one) - sizeOne * sizePercentage);
        
        front.setMin(two, front.getMin(two) + sizeTwo * sizePercentage);
        front.setMax(two, front.getMax(two) - sizeTwo * sizePercentage);
        
        if (facing.getAxisDirection() == AxisDirection.POSITIVE) {
            front.setMin(axis, front.getMax(axis) - sizeAxis * 0.5F);
            front.setMax(axis, front.getMax(axis) - depth);
        } else {
            front.setMax(axis, front.getMin(axis) + sizeAxis * 0.5F);
            front.setMin(axis, front.getMin(axis) + depth);
        }
        cubes.add(front);
        
        float thickness = 0.0001F;
        LittleRenderBox frontTop = (LittleRenderBox) new LittleRenderBox(cube, null, LittleTiles.dyeableBlock, 0).setColor(ColorUtils.LIGHT_BLUE);
        
        frontTop.allowOverlap = true;
        
        frontTop.setMin(one, frontTop.getMin(one) + sizeOne * sizePercentage);
        frontTop.setMax(one, frontTop.getMin(one) + thickness);
        
        frontTop.setMin(two, frontTop.getMin(two) + sizeTwo * sizePercentage);
        frontTop.setMax(two, frontTop.getMax(two) - sizeTwo * sizePercentage);
        
        if (facing.getAxisDirection() == AxisDirection.POSITIVE)
            frontTop.setMin(axis, frontTop.getMax(axis) - sizeAxis * sizePercentage);
        else
            frontTop.setMax(axis, frontTop.getMin(axis) + sizeAxis * sizePercentage);
        
        cubes.add(frontTop);
        
        LittleRenderBox frontBottom = (LittleRenderBox) new LittleRenderBox(cube, null, LittleTiles.dyeableBlock, 0).setColor(ColorUtils.LIGHT_BLUE);
        
        frontBottom.allowOverlap = true;
        frontBottom.setMax(one, frontBottom.getMax(one) - sizeOne * sizePercentage);
        frontBottom.setMin(one, frontBottom.getMax(one) - thickness);
        
        frontBottom.setMin(two, frontBottom.getMin(two) + sizeTwo * sizePercentage);
        frontBottom.setMax(two, frontBottom.getMax(two) - sizeTwo * sizePercentage);
        
        if (facing.getAxisDirection() == AxisDirection.POSITIVE)
            frontBottom.setMin(axis, frontBottom.getMax(axis) - sizeAxis * sizePercentage);
        else
            frontBottom.setMax(axis, frontBottom.getMin(axis) + sizeAxis * sizePercentage);
        
        cubes.add(frontBottom);
        
        LittleRenderBox frontRight = (LittleRenderBox) new LittleRenderBox(cube, null, LittleTiles.dyeableBlock, 0).setColor(ColorUtils.LIGHT_BLUE);
        
        frontRight.allowOverlap = true;
        frontRight.setMin(one, frontRight.getMin(one) + sizeOne * sizePercentage);
        frontRight.setMax(one, frontRight.getMax(one) - sizeOne * sizePercentage);
        
        frontRight.setMin(two, frontRight.getMin(two) + sizeTwo * sizePercentage);
        frontRight.setMax(two, frontRight.getMin(two) + thickness);
        
        if (facing.getAxisDirection() == AxisDirection.POSITIVE)
            frontRight.setMin(axis, frontRight.getMax(axis) - sizeAxis * sizePercentage);
        else
            frontRight.setMax(axis, frontRight.getMin(axis) + sizeAxis * sizePercentage);
        
        cubes.add(frontRight);
        
        LittleRenderBox frontLeft = (LittleRenderBox) new LittleRenderBox(cube, null, LittleTiles.dyeableBlock, 0).setColor(ColorUtils.LIGHT_BLUE);
        
        frontLeft.allowOverlap = true;
        frontLeft.setMin(one, frontLeft.getMin(one) + sizeOne * sizePercentage);
        frontLeft.setMax(one, frontLeft.getMax(one) - sizeOne * sizePercentage);
        
        frontLeft.setMax(two, frontLeft.getMax(two) - sizeTwo * sizePercentage);
        frontLeft.setMin(two, frontLeft.getMax(two) - thickness);
        
        if (facing.getAxisDirection() == AxisDirection.POSITIVE)
            frontLeft.setMin(axis, frontLeft.getMax(axis) - sizeAxis * sizePercentage);
        else
            frontLeft.setMax(axis, frontLeft.getMin(axis) + sizeAxis * sizePercentage);
        
        cubes.add(frontLeft);
        
        float middlePart = 0.1F;
        float sizeOneInside = sizeOne - sizeOne * sizePercentage * 2;
        float sizeTwoInside = sizeTwo - sizeTwo * sizePercentage * 2;
        
        LittleRenderBox middleBack = (LittleRenderBox) new LittleRenderBox(cube, null, LittleTiles.dyeableBlock, 0).setColor(ColorUtils.LIGHT_BLUE);
        
        middleBack.allowOverlap = true;
        middleBack.setMin(one, middleBack.getMin(one) + sizeOne * sizePercentage + sizeOneInside * middlePart);
        middleBack.setMax(one, middleBack.getMax(one) - sizeOne * sizePercentage - sizeOneInside * middlePart);
        
        middleBack.setMin(two, middleBack.getMin(two) + sizeTwo * sizePercentage + sizeTwoInside * middlePart);
        middleBack.setMax(two, middleBack.getMax(two) - sizeTwo * sizePercentage - sizeTwoInside * middlePart);
        
        if (facing.getAxisDirection() == AxisDirection.POSITIVE) {
            middleBack.setMin(axis, middleBack.getMax(axis) - sizeAxis * sizePercentage);
            middleBack.setMax(axis, middleBack.getMax(axis) - thickness);
        } else {
            middleBack.setMin(axis, middleBack.getMin(axis) + thickness);
            middleBack.setMax(axis, middleBack.getMin(axis) + sizeAxis * sizePercentage);
        }
        
        cubes.add(middleBack);
        
        LittleRenderBox middleFront = (LittleRenderBox) new LittleRenderBox(middleBack, null, LittleTiles.dyeableBlock, 0).setColor(color);
        
        middleFront.allowOverlap = true;
        
        if (facing.getAxisDirection() == AxisDirection.POSITIVE)
            middleFront.setMin(axis, middleFront.getMax(axis) - thickness);
        else
            middleFront.setMax(axis, middleFront.getMin(axis) + thickness);
        
        cubes.add(middleFront);
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
    
    public static class LittleStructureTypeInputQuick extends LittleStructureTypeNetwork {
        
        public LittleStructureTypeInputQuick(String id, String category, Class<? extends LittleStructure> structureClass, int attribute, String modid, int bandwidth) {
            super(id, category, structureClass, attribute, modid, bandwidth, 1);
        }
        
        @Override
        public List<PlacePreview> getSpecialTiles(LittlePreviews previews) {
            List<PlacePreview> result = super.getSpecialTiles(previews);
            EnumFacing facing = (EnumFacing) loadDirectional(previews, "facing");
            LittleBox box = previews.getSurroundingBox();
            result.add(new PlacePreviewFacing(box, facing, 0x800080));
            return result;
        }
        
        @Override
        @SideOnly(Side.CLIENT)
        public List<RenderBox> getRenderingCubes(LittlePreviews previews) {
            List<RenderBox> cubes = new ArrayList<>();
            float size = (float) ((Math.sqrt(bandwidth) * 1F / 32F + 0.05) * 1.4);
            cubes = new ArrayList<>();
            cubes.add(new RenderBox(0, 0.5F - size, 0.5F - size, size * 2, 0.5F + size, 0.5F + size, LittleTiles.dyeableBlock).setColor(getColor(previews)));
            cubes.add(new RenderBox(size * 2, 0.5F - size, 0.5F - size, size * 2.5F, 0.5F + size, 0.5F + size, LittleTiles.dyeableBlock).setColor(0x800080));
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
            return SignalComponentType.INPUT;
        }
        
    }
}
