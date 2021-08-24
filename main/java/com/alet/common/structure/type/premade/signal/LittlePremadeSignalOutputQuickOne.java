package com.alet.common.structure.type.premade.signal;

import com.creativemd.creativecore.common.utils.type.Pair;
import com.creativemd.littletiles.common.block.BlockTile;
import com.creativemd.littletiles.common.structure.LittleStructure;
import com.creativemd.littletiles.common.structure.exception.CorruptedConnectionException;
import com.creativemd.littletiles.common.structure.exception.NotYetConnectedException;
import com.creativemd.littletiles.common.structure.registry.LittleStructureType;
import com.creativemd.littletiles.common.structure.type.premade.LittleStructurePremade;
import com.creativemd.littletiles.common.tile.LittleTile;
import com.creativemd.littletiles.common.tile.math.box.LittleBox;
import com.creativemd.littletiles.common.tile.math.vec.LittleVec;
import com.creativemd.littletiles.common.tile.parent.IParentTileList;
import com.creativemd.littletiles.common.tile.parent.IStructureTileList;
import com.creativemd.littletiles.common.tileentity.TileEntityLittleTiles;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class LittlePremadeSignalOutputQuickOne extends LittleStructurePremade {
	
	public LittlePremadeSignalOutputQuickOne(LittleStructureType type, IStructureTileList mainBlock) {
		super(type, mainBlock);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void loadFromNBTExtra(NBTTagCompound nbt) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected void writeToNBTExtra(NBTTagCompound nbt) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void tick() {
		if (!isClient()) {
			World worldIn = this.getWorld();
			LittleSignalOutputQuick out = null;
			LittleBox hitBox = null;
			double y = 0;
			double x = 0;
			double z = 0;
			try {
				out = (LittleSignalOutputQuick) this.children.get(0).getStructure();
				
				x = this.getHighestCenterVec().x;
				y = this.getSurroundingBox().getAABB().minY;
				z = this.getHighestCenterVec().z;
				
				LittleVec vec = this.getSurroundingBox().getAbsoluteBox().box.getCenter();
				switch (out.facing) {
				case NORTH:
					vec.add(new LittleVec(0, 0, 1));
					break;
				case EAST:
					vec.add(new LittleVec(-1, 0, 0));
					break;
				case SOUTH:
					vec.add(new LittleVec(0, 0, -1));
					break;
				case WEST:
					vec.add(new LittleVec(1, 0, 0));
					break;
				case UP:
					vec.add(new LittleVec(0, -1, 0));
					break;
				case DOWN:
					vec.add(new LittleVec(0, 1, 0));
					break;
				
				default:
					break;
				}
				hitBox = new LittleBox(vec);
			} catch (CorruptedConnectionException | NotYetConnectedException e) {
				
			}
			
			TileEntityLittleTiles te = BlockTile.loadTe(worldIn, new BlockPos(x, y, z));
			if (te != null) {
				LittleStructure structure = null;
				for (Pair<IParentTileList, LittleTile> pair : te.allTiles()) {
					if (LittleBox.intersectsWith(pair.value.getBox(), hitBox)) {
						try {
							structure = pair.key.getStructure();
							if (structure.getOutput(0) == null)
								continue;
						} catch (CorruptedConnectionException | NotYetConnectedException e) {
							continue;
						}
						break;
					}
				}
				if (structure != null && structure.getOutput(0) != null)
					out.updateState(structure.getOutput(0).getState());
				
			}
			/*
			TileEntityLittleTiles te = BlockTile.loadTe(worldIn, new BlockPos(x, y, z));
			if (te != null) {
				Vec3d pos1 = null;
				
				for (Pair<IParentTileList, LittleTile> pair : te.allTiles()) {
					try {
						
						if(LittleBox.intersectsWith(pair.value.getBox(), )) {
							
						}
						pos1 = new Vec3d(x + 0.03125, pair.key.getStructure().getSurroundingBox().getAABB().minY, z);
					} catch (CorruptedConnectionException | NotYetConnectedException e) {
						out.updateState(new boolean[] { false });
						continue;
					}
				}
				try {
					double d0 = 0.005F;
					Vec3d vec32 = pos1.addVector(d0, d0, d0);
					Pair<IParentTileList, LittleTile> pair2 = te.getFocusedTile(pos1, vec32);
					if (pair2.key != null)
						structure = pair2.key.getStructure();
					if (structure != null && structure.getOutput(0) != null) {
						out.updateState(structure.getOutput(0).getState());
					}
				} catch (CorruptedConnectionException | NotYetConnectedException e) {
					
				}
				System.out.println(structure);
				if (structure == null)
					out.updateState(new boolean[] { false });
			}
			*/
		}
	}
	
}
