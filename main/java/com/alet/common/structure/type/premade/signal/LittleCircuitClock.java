package com.alet.common.structure.type.premade.signal;

import com.creativemd.creativecore.common.utils.type.Pair;
import com.creativemd.littletiles.common.action.LittleActionException;
import com.creativemd.littletiles.common.action.block.LittleActionActivated;
import com.creativemd.littletiles.common.block.BlockTile;
import com.creativemd.littletiles.common.structure.LittleStructure;
import com.creativemd.littletiles.common.structure.exception.CorruptedConnectionException;
import com.creativemd.littletiles.common.structure.exception.NotYetConnectedException;
import com.creativemd.littletiles.common.structure.registry.LittleStructureType;
import com.creativemd.littletiles.common.structure.type.LittleLight;
import com.creativemd.littletiles.common.structure.type.premade.LittleStructurePremade;
import com.creativemd.littletiles.common.structure.type.premade.signal.LittleSignalOutput;
import com.creativemd.littletiles.common.tile.LittleTile;
import com.creativemd.littletiles.common.tile.parent.IParentTileList;
import com.creativemd.littletiles.common.tile.parent.IStructureTileList;
import com.creativemd.littletiles.common.tileentity.TileEntityLittleTiles;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class LittleCircuitClock extends LittleStructurePremade {
	
	boolean pulse = false;
	
	public LittleCircuitClock(LittleStructureType type, IStructureTileList mainBlock) {
		super(type, mainBlock);
		
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, LittleTile tile, BlockPos pos, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ, LittleActionActivated action) throws LittleActionException {
		
		LittleStructure structure = null;
		
		TileEntityLittleTiles te = BlockTile.loadTe(worldIn, new BlockPos(pos.getX(), pos.getY(), pos.getZ()));
		ItemStack itemHeld = playerIn.getHeldItemMainhand();
		//if (itemHeld.getItem() instanceof PremadeItemHammer) {
		if (te != null) {
			try {
				Vec3d pos1 = new Vec3d(this.getHighestCenterVec().x + 0.03125 + 0.03125, this.getHighestCenterVec().y, this.getHighestCenterVec().z);
				
				double d0 = 0.01F;
				//Vec3d look = playerIn.getLook(Minecraft.getMinecraft().getRenderPartialTicks());
				Vec3d vec32 = pos1.addVector(d0, d0, d0);
				Pair<IParentTileList, LittleTile> pair = te.getFocusedTile(pos1, vec32);
				if (pair.key != null)
					structure = pair.key.getStructure();
				
			} catch (CorruptedConnectionException | NotYetConnectedException e) {
			}
		}
		if (!worldIn.isRemote)
			if (structure != null) {
				if (structure instanceof LittleLight)
					structure.getOutput(0).toggle();
				System.out.println(structure);
			}
		return true;
	}
	
	@Override
	public void tick() {
		
		if (!isClient())
			try {
				LittleSignalOutput out = (LittleSignalOutput) this.children.get(0).getStructure();
				pulse = (!pulse);
				boolean[] state = { pulse };
				out.updateState(state);
			} catch (CorruptedConnectionException | NotYetConnectedException e) {
				e.printStackTrace();
			}
	}
	
	@Override
	protected void loadFromNBTExtra(NBTTagCompound nbt) {
		if (nbt.hasKey("pulse"))
			pulse = nbt.getBoolean("pulse");
	}
	
	@Override
	protected void writeToNBTExtra(NBTTagCompound nbt) {
		nbt.setBoolean("pulse", pulse);
	}
	
}
