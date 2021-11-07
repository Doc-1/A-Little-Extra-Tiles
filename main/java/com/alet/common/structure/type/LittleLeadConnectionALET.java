package com.alet.common.structure.type;

import java.util.UUID;

import com.alet.entity.EntityLeadConnection;
import com.creativemd.creativecore.common.gui.container.GuiParent;
import com.creativemd.creativecore.common.world.CreativeWorld;
import com.creativemd.creativecore.common.world.IOrientatedWorld;
import com.creativemd.littletiles.common.action.block.LittleActionActivated;
import com.creativemd.littletiles.common.structure.LittleStructure;
import com.creativemd.littletiles.common.structure.animation.AnimationGuiHandler;
import com.creativemd.littletiles.common.structure.connection.StructureChildConnection;
import com.creativemd.littletiles.common.structure.exception.CorruptedConnectionException;
import com.creativemd.littletiles.common.structure.exception.NotYetConnectedException;
import com.creativemd.littletiles.common.structure.registry.LittleStructureGuiParser;
import com.creativemd.littletiles.common.structure.registry.LittleStructureRegistry;
import com.creativemd.littletiles.common.structure.registry.LittleStructureType;
import com.creativemd.littletiles.common.tile.LittleTile;
import com.creativemd.littletiles.common.tile.math.vec.LittleAbsoluteVec;
import com.creativemd.littletiles.common.tile.parent.IStructureTileList;
import com.creativemd.littletiles.common.tile.preview.LittlePreviews;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class LittleLeadConnectionALET extends LittleStructure {
	
	private UUID connectionUUID;
	private EntityPlayer player;
	
	public LittleLeadConnectionALET(LittleStructureType type, IStructureTileList mainBlock) {
		super(type, mainBlock);
	}
	
	@Override
	protected void loadFromNBTExtra(NBTTagCompound nbt) {
		if (nbt.hasKey("connection"))
			connectionUUID = UUID.fromString(nbt.getString("connection"));
		else
			connectionUUID = null;
	}
	
	@Override
	protected void writeToNBTExtra(NBTTagCompound nbt) {
		if (connectionUUID != null)
			nbt.setString("connection", connectionUUID.toString());
		else
			nbt.removeTag("connection");
	}
	
	/*
	@Override
	public boolean onBlockActivated(World worldIn, LittleTile tile, BlockPos pos, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ, LittleActionActivated action) throws LittleActionException {
		if (!worldIn.isRemote) {
			System.out.println("not remote");
			return ItemJumpTool.attachToFence(playerIn, worldIn, pos);
		} else {
			System.out.println("remote");
			return heldItem.getItem() == ALET.jumpRod || heldItem.isEmpty();
		}
	}
	*/
	public void setPlayer(EntityPlayer player) {
	}
	
	@Override
	protected void afterPlaced() {
		super.afterPlaced();
		if (connectionUUID != null) {
			World world = getWorld();
			if (world instanceof IOrientatedWorld) {
				if (world instanceof CreativeWorld && ((CreativeWorld) world).parent == null)
					return;
				world = ((IOrientatedWorld) world).getRealWorld();
			}
			for (Entity entity : world.loadedEntityList)
				if (entity.getUniqueID().equals(connectionUUID) && entity instanceof EntityLeadConnection) {
					EntityLeadConnection connection = (EntityLeadConnection) entity;
					StructureChildConnection temp = this.generateConnection(connection);
					connection.getDataManager().set(EntityLeadConnection.CONNECTION, temp.writeToNBT(new NBTTagCompound()));
					break;
				}
		}
	}
	
	@Override
	public boolean onBlockActivated(World world, LittleTile tile, BlockPos pos, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ, LittleActionActivated action) {
		if (!world.isRemote) {
			if (this.player != null)
				return true;
			try {
				
				LittleAbsoluteVec vec = getHighestCenterPoint();
				if (vec != null) {
					if (world instanceof IOrientatedWorld)
						world = ((IOrientatedWorld) world).getRealWorld();
					EntityLeadConnection connection = new EntityLeadConnection(this, world, vec.getPosX(), vec.getPosY() - 0.25, vec.getPosZ());
					double i = vec.getPosX();
					double j = vec.getPosY();
					double k = vec.getPosZ();
					if (!connection.isLeashed && this.connectionUUID == null) {
						this.connectionUUID = connection.getPersistentID();
						//((EntityLiving) player).setLeashHolder(connection, true);
						System.out.println(i + " " + j + " " + k);
						for (EntityLiving entityliving : world.getEntitiesWithinAABB(EntityLiving.class, new AxisAlignedBB((double) i - 7.0D, (double) j - 7.0D, (double) k - 7.0D, (double) i + 7.0D, (double) j + 7.0D, (double) k + 7.0D))) {
							//System.out.println(entityliving);
						}
						world.spawnEntity(connection);
						connection.playPlaceSound();
						connection.setLeashHolder(player, true);
						
					}
				}
			} catch (CorruptedConnectionException | NotYetConnectedException e) {
				e.printStackTrace();
			}
			
		}
		return true;
	}
	
	public static class LittleLeadConnectionParserALET extends LittleStructureGuiParser {
		
		public LittleLeadConnectionParserALET(GuiParent parent, AnimationGuiHandler handler) {
			super(parent, handler);
		}
		
		@Override
		protected void createControls(LittlePreviews previews, LittleStructure structure) {
			
		}
		
		@Override
		protected LittleStructure parseStructure(LittlePreviews previews) {
			LittleLeadConnectionALET structure = createStructure(LittleLeadConnectionALET.class, null);
			return structure;
		}
		
		@Override
		protected LittleStructureType getStructureType() {
			return LittleStructureRegistry.getStructureType(LittleLeadConnectionALET.class);
		}
		
	}
}
