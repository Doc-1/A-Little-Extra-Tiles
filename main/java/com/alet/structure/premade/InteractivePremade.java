package com.alet.structure.premade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.creativemd.littletiles.common.action.LittleActionException;
import com.creativemd.littletiles.common.action.block.LittleActionActivated;
import com.creativemd.littletiles.common.structure.LittleStructure;
import com.creativemd.littletiles.common.structure.directional.StructureDirectional;
import com.creativemd.littletiles.common.structure.directional.StructureDirectionalField;
import com.creativemd.littletiles.common.structure.exception.CorruptedConnectionException;
import com.creativemd.littletiles.common.structure.exception.NotYetConnectedException;
import com.creativemd.littletiles.common.structure.registry.LittleStructureType;
import com.creativemd.littletiles.common.structure.type.premade.LittleStructurePremade;
import com.creativemd.littletiles.common.tile.LittleTile;
import com.creativemd.littletiles.common.tile.math.box.LittleBox;
import com.creativemd.littletiles.common.tile.math.vec.LittleVec;
import com.creativemd.littletiles.common.tile.parent.IStructureTileList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public abstract class InteractivePremade extends LittleStructurePremade {
	
	protected int seriesMaxium;
	protected int seriesAt = 0;
	
	protected Map<LittleBox, LittleTile> tilePosList = new HashMap<LittleBox, LittleTile>();
	protected LittleBox editArea;
	public AxisAlignedBB absolutePos;
	public LittleBox relativeBox;
	
	public LittleBox linkedBox;
	
	public InteractivePremade(LittleStructureType type, IStructureTileList mainBlock) {
		super(type, mainBlock);
		
		//seriesName = type.id.toString().split("_")[0];
		//seriesAt = Integer.parseInt(this.type.id.toString().split("_")[1]);
	}
	
	@StructureDirectional
	public EnumFacing direction;
	
	@StructureDirectional
	public EnumFacing facing;
	
	public void linkStructure(LittleStructure structure, EnumFacing direction) throws CorruptedConnectionException, NotYetConnectedException {
		int childIndex = children.findFreeIndex();
		updateChildConnection(childIndex, structure, true);
		structure.updateParentConnection(childIndex, this, true);
	}
	
	@Override
	protected void loadFromNBTExtra(NBTTagCompound nbt) {
		
	}
	
	@Override
	protected void writeToNBTExtra(NBTTagCompound nbt) {
		
	}
	
	/** @param box
	 *            Set it to the area you want to edit. */
	public void setEditArea(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
		editArea = new LittleBox(minX, minY, minZ, maxX, maxY, maxZ);
	}
	
	/** @return
	 *         Returns the adjusted vec */
	private Vec3d[] adjustRelativeTileVec(BlockPos pos, LittleBox box) {
		return adjustRelativeTileVec(pos, box, absolutePos, this.direction);
	}
	
	public static Vec3d[] adjustRelativeTileVec(BlockPos pos, LittleBox box, AxisAlignedBB absolutePos, EnumFacing direction) {
		Vec3d relativeVec[] = new Vec3d[2];
		
		double aMinX = 0, aMinY = 0, aMinZ = 0;
		double aMaxX = 0, aMaxY = 0, aMaxZ = 0;
		
		switch (direction) {
		case NORTH:
			aMinX = (box.maxZ / 16D) + pos.getZ() - absolutePos.maxZ;
			aMinY = (box.minY / 16D) + pos.getY() - absolutePos.minY;
			aMinZ = (box.minX / 16D) + pos.getX() - absolutePos.minX;
			
			aMaxX = (box.minZ / 16D) + pos.getZ() - absolutePos.maxZ;
			aMaxY = (box.maxY / 16D) + pos.getY() - absolutePos.minY;
			aMaxZ = (box.maxX / 16D) + pos.getX() - absolutePos.minX;
			break;
		case EAST:
			aMinX = (box.minX / 16D) + pos.getX() - absolutePos.minX;
			aMinY = (box.minY / 16D) + pos.getY() - absolutePos.minY;
			aMinZ = (box.minZ / 16D) + pos.getZ() - absolutePos.minZ;
			
			aMaxX = (box.maxX / 16D) + pos.getX() - absolutePos.minX;
			aMaxY = (box.maxY / 16D) + pos.getY() - absolutePos.minY;
			aMaxZ = (box.maxZ / 16D) + pos.getZ() - absolutePos.minZ;
			break;
		case SOUTH:
			aMinX = (box.minZ / 16D) + pos.getZ() - absolutePos.minZ;
			aMinY = (box.minY / 16D) + pos.getY() - absolutePos.minY;
			aMinZ = (box.maxX / 16D) + pos.getX() - absolutePos.maxX;
			
			aMaxX = (box.maxZ / 16D) + pos.getZ() - absolutePos.minZ;
			aMaxY = (box.maxY / 16D) + pos.getY() - absolutePos.minY;
			aMaxZ = (box.minX / 16D) + pos.getX() - absolutePos.maxX;
			break;
		case WEST:
			aMinX = (box.maxX / 16D) + pos.getX() - absolutePos.maxX;
			aMinY = (box.minY / 16D) + pos.getY() - absolutePos.minY;
			aMinZ = (box.maxZ / 16D) + pos.getZ() - absolutePos.maxZ;
			
			aMaxX = (box.minX / 16D) + pos.getX() - absolutePos.maxX;
			aMaxY = (box.maxY / 16D) + pos.getY() - absolutePos.minY;
			aMaxZ = (box.minZ / 16D) + pos.getZ() - absolutePos.maxZ;
			break;
		default:
			break;
		}
		relativeVec[0] = new Vec3d(Math.abs(aMinX), Math.abs(aMinY), Math.abs(aMinZ));
		relativeVec[1] = new Vec3d(Math.abs(aMaxX), Math.abs(aMaxY), Math.abs(aMaxZ));
		return relativeVec;
	}
	
	/** Collects all tiles within the structure. It assigns each tile a box relative to the structure itself.
	 * Meaning minimum corner of the structure is considered 0,0,0. */
	public void collectAllTiles() throws CorruptedConnectionException, NotYetConnectedException {
		for (IStructureTileList iStructureTileList : this.blocksList()) {
			iStructureTileList.getTe().updateTiles((a) -> {
				IStructureTileList list = a.get(iStructureTileList);
				List<LittleTile> tileLs = new ArrayList<LittleTile>();
				BlockPos pos = iStructureTileList.getTe().getPos();
				for (LittleTile littleTile : list) {
					Vec3d relativeVec[] = adjustRelativeTileVec(pos, littleTile.getBox());
					Vec3d relativeMin = relativeVec[0];
					Vec3d relativeMax = relativeVec[1];
					LittleBox relativeBox = new LittleBox(new LittleVec((int) (relativeMin.x * 16), (int) (relativeMin.y * 16), (int) (relativeMin.z * 16)), new LittleVec((int) (relativeMax.x * 16), (int) (relativeMax.y * 16), (int) (relativeMax.z * 16)));
					
					tilePosList.put(relativeBox, littleTile);
				}
			});
		}
	}
	
	public void getRelativeBox() throws CorruptedConnectionException, NotYetConnectedException {
		
		for (IStructureTileList iStructureTileList : this.blocksList()) {
			iStructureTileList.getTe().updateTiles((a) -> {
				IStructureTileList list = a.get(iStructureTileList);
				List<LittleTile> tileLs = new ArrayList<LittleTile>();
				BlockPos pos = iStructureTileList.getTe().getPos();
				
				for (LittleTile littleTile : list) {
					Vec3d relativeVec[] = adjustRelativeTileVec(pos, littleTile.getBox());
					Vec3d relativeMin = relativeVec[0];
					Vec3d relativeMax = relativeVec[1];
					relativeBox = new LittleBox(new LittleVec((int) (relativeMin.x * 16), (int) (relativeMin.y * 16), (int) (relativeMin.z * 16)), new LittleVec((int) (relativeMax.x * 16), (int) (relativeMax.y * 16), (int) (relativeMax.z * 16)));
				}
			});
			
		}
	}
	
	@Override
	protected Object failedLoadingRelative(NBTTagCompound nbt, StructureDirectionalField field) {
		if (field.key.equals("facing"))
			return EnumFacing.UP;
		if (field.key.equals("east"))
			return EnumFacing.EAST;
		if (field.key.equals("west"))
			return EnumFacing.WEST;
		return super.failedLoadingRelative(nbt, field);
	}
	
	protected String nextSeries() {
		//if (seriesMaxium > seriesAt) {
		//	return seriesName + "_" + (seriesAt + 1);
		//}
		return "";
	}
	
	@Override
	public void tick() {
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, LittleTile tile, BlockPos pos, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ, LittleActionActivated action) throws LittleActionException {
		if (worldIn.isRemote)
			return true;
		
		if (facing != EnumFacing.UP) {
			playerIn.sendStatusMessage(new TextComponentTranslation("structure.interaction.wrongfacing"), true);
			return true;
		} else {
			absolutePos = this.getSurroundingBox().getAABB();
			try {
				collectAllTiles();
			} catch (CorruptedConnectionException | NotYetConnectedException e) {
				e.printStackTrace();
			}
			
			onPremadeActivated(heldItem);
		}
		
		return true;
	}
	
	public abstract void onPremadeActivated(ItemStack heldItem);
	
	public LittleBox getEditArea() {
		return editArea;
	}
	
	public Map<LittleBox, LittleTile> getTilePosList() {
		return tilePosList;
	}
	
	public int getSeriesAt() {
		return seriesAt;
	}
	
	public int getSeriesMaxium() {
		return seriesMaxium;
	}
}