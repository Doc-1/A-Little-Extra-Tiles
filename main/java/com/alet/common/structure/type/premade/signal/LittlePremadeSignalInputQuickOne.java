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

public class LittlePremadeSignalInputQuickOne extends LittleStructurePremade {
	
	public LittlePremadeSignalInputQuickOne(LittleStructureType type, IStructureTileList mainBlock) {
		super(type, mainBlock);
	}
	
	@Override
	protected void loadFromNBTExtra(NBTTagCompound nbt) {
		
	}
	
	@Override
	protected void writeToNBTExtra(NBTTagCompound nbt) {
		
	}
	
	@Override
	public void tick() {
		if (!isClient()) {
			
			World worldIn = this.getWorld();
			LittleSignalInputQuick in = null;
			LittleBox hitBox = null;
			double y = 0;
			double x = 0;
			double z = 0;
			try {
				in = (LittleSignalInputQuick) this.children.get(0).getStructure();
				
				x = this.getHighestCenterVec().x;
				y = this.getSurroundingBox().getAABB().minY;
				z = this.getHighestCenterVec().z;
				
				LittleVec vec = this.getSurroundingBox().getAbsoluteBox().box.getCenter();
				switch (in.facing) {
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
				if (structure != null && structure.getOutput(0) != null) {
					boolean[] signalMode = this.getOutput(0).getState();
					System.out.println(signalMode);
					//structure.getOutput(0).updateState(in.getState());
				}
				
			}
			
			/*
			World worldIn = this.getWorld();
			LittleSignalInputQuick in = null;
			LittleStructure structure = null;
			
			double y = 0;
			double x = 0;
			double z = 0;
			try {
				x = this.getHighestCenterVec().x;
				y = this.getSurroundingBox().getAABB().minY;
				z = this.getHighestCenterVec().z;
				in = (LittleSignalInputQuick) this.children.get(0).getStructure();
			} catch (CorruptedConnectionException | NotYetConnectedException e) {
				
			}
			boolean[] state = in.getState();
			boolean activate = false;
			for (int i = 0; i < state.length; i++) {
				if (state[i]) {
					activate = true;
					break;
				}
			}
			if (in != null) {
				TileEntityLittleTiles te = BlockTile.loadTe(worldIn, new BlockPos(x, y, z));
				if (te != null) {
					
					Vec3d pos1 = null;
					
					for (Pair<IParentTileList, LittleTile> pair : te.allTiles()) {
						try {
							pos1 = new Vec3d(x + 0.03125, pair.key.getStructure().getSurroundingBox().getAABB().minY, z);
						} catch (CorruptedConnectionException | NotYetConnectedException e) {
							continue;
						}
					}
					try {
						double d0 = 0.005F;
						Vec3d vec32 = pos1.addVector(d0, d0, d0);
						Pair<IParentTileList, LittleTile> pair2 = te.getFocusedTile(pos1, vec32);
						if (pair2.key != null)
							structure = pair2.key.getStructure();
						
						System.out.println(structure);
					} catch (CorruptedConnectionException | NotYetConnectedException e) {
						
					}
					if (structure != null && structure.getOutput(0) != null) {
						
						if (activate)
							structure.getOutput(0).updateState(state);
						else
							structure.getOutput(0).updateState(new boolean[] { false });
						structure.updateStructure();
						
					}
				}
				
			}
			}
			*/
		}
	}
	
}
