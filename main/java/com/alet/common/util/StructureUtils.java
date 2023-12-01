package com.alet.common.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import javax.annotation.Nullable;

import com.creativemd.creativecore.common.utils.type.HashMapList;
import com.creativemd.creativecore.common.utils.type.Pair;
import com.creativemd.littletiles.common.block.BlockTile;
import com.creativemd.littletiles.common.structure.LittleStructure;
import com.creativemd.littletiles.common.structure.exception.CorruptedConnectionException;
import com.creativemd.littletiles.common.structure.exception.NotYetConnectedException;
import com.creativemd.littletiles.common.structure.relative.StructureRelative;
import com.creativemd.littletiles.common.tile.LittleTile;
import com.creativemd.littletiles.common.tile.math.box.LittleBox;
import com.creativemd.littletiles.common.tile.math.vec.LittleVec;
import com.creativemd.littletiles.common.tile.parent.IStructureTileList;
import com.creativemd.littletiles.common.tile.preview.LittlePreviews;
import com.creativemd.littletiles.common.tileentity.TileEntityLittleTiles;
import com.creativemd.littletiles.common.util.grid.LittleGridContext;
import com.creativemd.littletiles.common.util.place.Placement;
import com.creativemd.littletiles.common.util.place.PlacementMode;
import com.creativemd.littletiles.common.util.place.PlacementPreview;
import com.creativemd.littletiles.common.util.place.PlacementResult;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class StructureUtils {
    
    /** @param worldIn
     *            The world the structure is in.
     * @param box
     *            The location you are looking for the structure at.
     * @param pos
     *            The block position the structure is located at.
     * @param self
     *            Can be null, if you are using this method inside a LittleStructure object you can provide this to this field. It will make sure it didn't find itself.
     * @return
     *         Returns the LittleStructure by first looking at the tiles inside a block then using box to find the exact structure located there. */
    public static LittleStructure getStructureAt(World worldIn, LittleBox box, BlockPos pos, LittleGridContext context, @Nullable LittleStructure self, @Nullable Class<? extends LittleStructure> structureClass) {
        TileEntityLittleTiles te = BlockTile.loadTe(worldIn, pos);
        if (te != null) {
            for (IStructureTileList s : te.structures()) {
                try {
                    LittleStructure structure = s.getStructure();
                    if (structureClass != null && structure.getClass() != structureClass)
                        continue;
                    
                    if (structure != self && !structure.isChildOf(self)) {
                        for (Pair<IStructureTileList, LittleTile> tile : structure.tiles()) {
                            if (tile.key.getPos().equals(pos)) {
                                LittleBox copy = tile.value.getBox().copy();
                                copy.convertTo(tile.key.getContext().size, context.size);
                                if (intersectsWith(copy, box)) {
                                    return s.getStructure();
                                }
                            }
                        }
                    }
                } catch (CorruptedConnectionException | NotYetConnectedException e) {}
            }
        }
        return null;
    }
    
    public static Vec3d facingOffset(double x, double y, double z, int contextSize, EnumFacing facing) {
        double offset = 1D / contextSize;
        switch (facing) {
            case UP:
                y -= offset;
                break;
            case EAST:
                x -= offset;
                break;
            case SOUTH:
                z -= offset;
                break;
            default:
                break;
        }
        Vec3d vec = new Vec3d(x, y, z);
        return vec;
    }
    
    public static LittleStructure findConnection(World worldIn, BlockPos structurePos, StructureRelative searchArea, @Nullable LittleStructure self, @Nullable Class<? extends LittleStructure> structureClass) {
        
        LittleBox foundBox = searchArea.getBox().copy();
        double searchX = searchArea.getCenter().x;
        double searchY = searchArea.getCenter().y;
        double searchZ = searchArea.getCenter().z;
        BlockPos posSearch = new BlockPos(structurePos.getX() + searchX, structurePos.getY() + searchY, structurePos
                .getZ() + searchZ);
        HashMapList<BlockPos, LittleBox> boxesSearch = new HashMapList<BlockPos, LittleBox>();
        foundBox.split(searchArea.getContext(), structurePos, boxesSearch, null);
        for (Entry<BlockPos, ArrayList<LittleBox>> b : boxesSearch.entrySet())
            if (b.getKey().equals(posSearch))
                foundBox = b.getValue().get(0);
        return getStructureAt(worldIn, foundBox, posSearch, searchArea.getContext(), self, structureClass);
        
    }
    
    public static boolean intersectsWith(LittleBox box, LittleBox box2) {
        return box.maxX > box2.minX && box.minX < box2.maxX && box.maxY > box2.minY && box.minY < box2.maxY && box.maxZ > box2.minZ && box.minZ < box2.maxZ;
    }
    
    /*
     * 
     * 
                            LittleAbsoluteBox absolute = new LittleAbsoluteBox(pos, pair.getValue().getBox(), s.getContext());
                            System.out.println(box + " " + absolute.);
                            System.out.println(LittleBox.intersectsWith(box, pair.value.getBox()));
                            if (LittleBox.intersectsWith(box, pair.value.getBox())) {
                                
                                System.out.println(s.getStructure());
                                return s.getStructure();
                            }
     * [0,15,0 -> 32,18,32] [0,7,0 -> 16,16,16]
    true
     * [0,26,0 -> 32,28,32] [0,26,0 -> 32,32,32]
       [10,8,10 -> 22,10,22] [0,0,0 -> 16,13,16]
     */
    public static LittlePreviews addChildToStructure(LittleStructure child, LittleStructure parent, boolean dynamic) {
        if (parent == null || child == null)
            return null;
        try {
            LittlePreviews ch = child.getPreviews(parent.getPos());
            LittlePreviews par = parent.getPreviews(parent.getPos());
            par.addChild(ch, dynamic);
            return par.copy();
        } catch (CorruptedConnectionException | NotYetConnectedException e) {}
        return null;
    }
    
    public static LittlePreviews addPreviewToStructure(LittlePreviews previews, LittleStructure parent, boolean dynamic) {
        if (parent == null || previews == null)
            return null;
        try {
            LittlePreviews par = parent.getPreviews(parent.getPos());
            par.addChild(previews, dynamic);
            return par.copy();
        } catch (CorruptedConnectionException | NotYetConnectedException e) {}
        return null;
    }
    
    public static void placeStructure(LittleStructure structure, World worldIn, BlockPos pos, LittleVec offset, EnumFacing facing, EntityPlayer playerIn) {
        try {
            LittlePreviews preview = structure.getPreviews(pos);
            PlacementPreview placePreview = new PlacementPreview(worldIn, preview, PlacementMode.fill, preview
                    .getSurroundingBox(), true, pos, offset, facing);
            Placement place = new Placement(playerIn, placePreview);
            place.tryPlace();
        } catch (CorruptedConnectionException | NotYetConnectedException e) {}
    }
    
    public static boolean placePreview(LittlePreviews preview, World worldIn, BlockPos pos, LittleVec offset, EnumFacing facing, EntityPlayer playerIn) {
        PlacementPreview placePreview = new PlacementPreview(worldIn, preview, PlacementMode.fill, preview
                .getSurroundingBox(), true, pos, offset, facing);
        Placement place = new Placement(playerIn, placePreview);
        PlacementResult results = place.tryPlace();
        return results != null;
    }
    
    public static boolean mergeChildToStructure(LittleStructure child, LittleStructure parent, boolean dynamic, World worldIn, LittleVec offset, EnumFacing facing, EntityPlayer playerIn) {
        try {
            LittlePreviews preview = addChildToStructure(child, parent, dynamic);
            BlockPos pos = parent.getPos();
            parent.onLittleTileDestroy();
            child.onLittleTileDestroy();
            return placePreview(preview, worldIn, pos, offset, facing, playerIn);
        } catch (CorruptedConnectionException | NotYetConnectedException e) {}
        return false;
    }
    
    public static boolean mergePreviewToStructure(LittlePreviews previews, LittleStructure parent, boolean dynamic, World worldIn, LittleVec offset, EnumFacing facing, EntityPlayer playerIn) {
        try {
            LittlePreviews preview = addPreviewToStructure(previews, parent, dynamic);
            BlockPos pos = parent.getPos();
            parent.onLittleTileDestroy();
            return placePreview(preview, worldIn, pos, offset, facing, playerIn);
        } catch (CorruptedConnectionException | NotYetConnectedException e) {}
        return false;
    }
    
    public static void replaceStructure(LittleStructure replace, LittleStructure original, World worldIn, BlockPos pos, EnumFacing facing, EntityPlayer playerIn) {
        try {
            LittlePreviews preview = replace.getPreviews(replace.getPos()).copy();
            replace.onLittleTileDestroy();
            original.onLittleTileDestroy();
            placePreview(preview, worldIn, pos, new LittleVec(0, 0, 0), facing, playerIn);
        } catch (CorruptedConnectionException | NotYetConnectedException e) {
            e.printStackTrace();
        }
    }
    
    public static boolean removeThisDynamicChild(LittleStructure child) {
        try {
            LittleStructure parent = child.getParent().getStructure();
            for (int i = 0; i < parent.countChildren(); i++) {
                if (parent.getChild(i).getStructure().equals(child)) {
                    parent.removeDynamicChild(i);
                    parent.updateStructure();
                    return true;
                }
            }
        } catch (CorruptedConnectionException | NotYetConnectedException e) {}
        return false;
    }
    
    public static List<BlockPos> getBlockArea(LittleStructure structure) {
        try {
            List<BlockPos> blockPositions = new ArrayList<BlockPos>();
            for (TileEntityLittleTiles te : structure.blocks()) {
                blockPositions.add(te.getPos());
            }
            return blockPositions;
        } catch (CorruptedConnectionException | NotYetConnectedException e) {}
        return null;
    }
}
