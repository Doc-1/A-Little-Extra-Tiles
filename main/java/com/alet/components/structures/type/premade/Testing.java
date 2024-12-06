package com.alet.components.structures.type.premade;

import com.creativemd.littletiles.common.action.LittleActionException;
import com.creativemd.littletiles.common.action.block.LittleActionActivated;
import com.creativemd.littletiles.common.structure.exception.CorruptedConnectionException;
import com.creativemd.littletiles.common.structure.exception.NotYetConnectedException;
import com.creativemd.littletiles.common.structure.registry.LittleStructureType;
import com.creativemd.littletiles.common.structure.type.premade.LittleStructurePremade;
import com.creativemd.littletiles.common.tile.LittleTile;
import com.creativemd.littletiles.common.tile.math.vec.LittleVec;
import com.creativemd.littletiles.common.tile.math.vec.LittleVecContext;
import com.creativemd.littletiles.common.tile.parent.IStructureTileList;
import com.creativemd.littletiles.common.tile.preview.LittlePreview;
import com.creativemd.littletiles.common.tile.preview.LittlePreviews;
import com.creativemd.littletiles.common.util.grid.LittleGridContext;
import com.creativemd.littletiles.common.util.place.Placement;
import com.creativemd.littletiles.common.util.place.PlacementMode;
import com.creativemd.littletiles.common.util.place.PlacementPreview;
import com.creativemd.littletiles.common.util.vec.SurroundingBox;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Testing extends LittleStructurePremade {
	
	private int seriesIndex = 13;
	private String seriesName = type.id.toString().split("_")[0];
	private int tick = 0;
	private boolean hasFuel = false;
	private String test;
	
	public Testing(LittleStructureType type, IStructureTileList mainBlock) {
		super(type, mainBlock);
	}
	
	@Override
	protected void loadFromNBTExtra(NBTTagCompound nbt) {
		tick = nbt.getInteger("tick");
	}
	
	@Override
	protected void writeToNBTExtra(NBTTagCompound nbt) {
		nbt.setInteger("tick", tick);
	}
	
	private String nextSeries() {
		int seriesAt = Integer.parseInt(type.id.toString().split("_")[1]);
		if (seriesIndex > seriesAt) {
			return seriesName + "_" + (seriesAt + 1);
		}
		return "";
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, LittleTile tile, BlockPos pos, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ, LittleActionActivated action) throws LittleActionException {
		if (getWorld().isRemote)
			return true;
		
		SurroundingBox box = new SurroundingBox(false, null).add(this.mainBlock);
		long minX = box.getMinX();
		long minY = box.getMinY();
		long minZ = box.getMinZ();
		LittleGridContext context = box.getContext();
		BlockPos min = new BlockPos(context.toBlockOffset(minX), context.toBlockOffset(minY), context.toBlockOffset(minZ));
		
		LittleVecContext minVec = new LittleVecContext(new LittleVec((int) (minX
		        - (long) min.getX() * (long) context.size), (int) (minY
		                - (long) min.getY() * (long) context.size), (int) (minZ
		                        - (long) min.getZ() * (long) context.size)), context);
		
		LittlePreviews previews = getStructurePremadeEntry("photoimporter").previews.copy(); // Change this line to support different states
		LittleVec previewMinVec = previews.getMinVec();
		LittlePreview preview = null;
		minVec.forceContext(previews);
		for (LittlePreview prev : previews) {
			prev.box.sub(previewMinVec);
			prev.box.add(minVec.getVec());
			preview = prev;
		}
		
		previews.convertToSmallest();
		
		previews.getPlacePreviews(LittleVec.ZERO);
		try {
			PlacementPreview placePreview = new PlacementPreview(this.getWorld(), previews, PlacementMode.fill, preview.box, false, this.mainBlock.getPos(), LittleVec.ZERO, EnumFacing.DOWN);
			this.onLittleTileDestroy();
			
			Placement place = new Placement(Minecraft.getMinecraft().player, placePreview);
			place.tryPlace();
		} catch (CorruptedConnectionException | NotYetConnectedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Places new structure
		//System.out.println("10 seconds "+this.getMainTile().getBlockPos());
		return true;
	}
	
	@Override
	public void tick() {
		
	}
	
}
