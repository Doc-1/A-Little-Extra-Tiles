package com.alet.common.tile;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class TilePos extends Vec3d {
	
	/** Similar to BlockPos, It is the position where a tile is relative to the block position.
	 * 
	 * @param pos
	 *            The block position the tiles are located in */
	public TilePos(BlockPos pos, double xIn, double yIn, double zIn) {
		super(xIn, yIn, zIn);
	}
	
}
