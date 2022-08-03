package com.alet.common.util;

import javax.annotation.Nullable;

import com.creativemd.creativecore.common.utils.type.Pair;
import com.creativemd.littletiles.common.block.BlockTile;
import com.creativemd.littletiles.common.structure.LittleStructure;
import com.creativemd.littletiles.common.structure.exception.CorruptedConnectionException;
import com.creativemd.littletiles.common.structure.exception.NotYetConnectedException;
import com.creativemd.littletiles.common.tile.LittleTile;
import com.creativemd.littletiles.common.tile.math.box.LittleBox;
import com.creativemd.littletiles.common.tile.math.vec.LittleVec;
import com.creativemd.littletiles.common.tile.parent.IStructureTileList;
import com.creativemd.littletiles.common.tile.preview.LittlePreviews;
import com.creativemd.littletiles.common.tileentity.TileEntityLittleTiles;
import com.creativemd.littletiles.common.util.place.Placement;
import com.creativemd.littletiles.common.util.place.PlacementMode;
import com.creativemd.littletiles.common.util.place.PlacementPreview;
import com.creativemd.littletiles.common.util.place.PlacementResult;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class StructureUtils {
    
    /** @param worldIn
     *            The world the structure is in.
     * @param box
     *            The location you are looking for the structure at. Best to use the smallest context available and to make the box 0,0,0 by 1,1,1.
     * @param pos
     *            The block position the structure is located at.
     * @param self
     *            Can be null, if you are using this method inside a LittleStructure object you can provide this to this field. It will make sure it didn't find itself.
     * @return
     *         Returns the LittleStructure by first looking at the tiles inside a block then using box to find the exact structure located there. */
    public static LittleStructure getStructureAt(World worldIn, LittleBox box, BlockPos pos, @Nullable LittleStructure self) {
        BlockPos location = new BlockPos(pos.getX(), pos.getY(), pos.getZ());
        TileEntityLittleTiles te = BlockTile.loadTe(worldIn, location);
        if (te != null)
            for (IStructureTileList s : te.structures()) {
                try {
                    if (!s.getStructure().equals(self))
                        for (Pair<IStructureTileList, LittleTile> pair : s.getStructure().tiles()) {
                            if (LittleBox.intersectsWith(box, pair.value.getBox())) {
                                return s.getStructure();
                            }
                        }
                } catch (CorruptedConnectionException | NotYetConnectedException e) {}
            }
        return null;
    }
    
    /*
     * [0,26,0 -> 32,28,32] [0,26,0 -> 32,32,32]
       [10,8,10 -> 22,10,22] [0,0,0 -> 16,13,16]
     */
    public static LittlePreviews addChildToStructure(LittleStructure child, LittleStructure parent, boolean dynamic) {
        if (parent == null || child == null)
            return null;
        try {
            LittlePreviews ch = child.getPreviews(child.getPos());
            LittlePreviews par = parent.getPreviews(parent.getPos());
            par.addChild(ch, dynamic);
            return par.copy();
        } catch (CorruptedConnectionException | NotYetConnectedException e) {}
        return null;
    }
    
    public static void placeStructure(LittleStructure structure, World worldIn, BlockPos pos, LittleVec offset, EnumFacing facing, EntityPlayer playerIn) {
        try {
            LittlePreviews preview = structure.getPreviews(pos);
            PlacementPreview placePreview = new PlacementPreview(worldIn, preview, PlacementMode.fill, preview.getSurroundingBox(), true, pos, offset, facing);
            Placement place = new Placement(playerIn, placePreview);
            place.tryPlace();
        } catch (CorruptedConnectionException | NotYetConnectedException e) {}
    }
    
    public static boolean placePreview(LittlePreviews preview, World worldIn, BlockPos pos, LittleVec offset, EnumFacing facing, EntityPlayer playerIn) {
        PlacementPreview placePreview = new PlacementPreview(worldIn, preview, PlacementMode.fill, preview.getSurroundingBox(), true, pos, offset, facing);
        Placement place = new Placement(playerIn, placePreview);
        PlacementResult results = place.tryPlace();
        return results != null;
    }
    
    public static boolean mergeChildToStructure(LittleStructure child, LittleStructure parent, boolean dynamic, World worldIn, BlockPos pos, LittleVec offset, EnumFacing facing, EntityPlayer playerIn) {
        try {
            LittlePreviews preview = addChildToStructure(child, parent, dynamic);
            parent.onLittleTileDestroy();
            child.onLittleTileDestroy();
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
}
