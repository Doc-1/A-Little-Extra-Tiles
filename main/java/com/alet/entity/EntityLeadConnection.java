package com.alet.entity;

import javax.annotation.Nullable;
import javax.vecmath.Vector3d;

import com.alet.common.packet.PacketConnectLead;
import com.alet.common.structure.type.LittleLeadConnectionALET;
import com.creativemd.creativecore.common.packet.PacketHandler;
import com.creativemd.creativecore.common.world.IOrientatedWorld;
import com.creativemd.littletiles.common.structure.LittleStructure;
import com.creativemd.littletiles.common.structure.connection.IWorldPositionProvider;
import com.creativemd.littletiles.common.structure.connection.StructureChildConnection;
import com.creativemd.littletiles.common.structure.exception.CorruptedConnectionException;
import com.creativemd.littletiles.common.structure.exception.NotYetConnectedException;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLeashKnot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class EntityLeadConnection extends EntityLeashKnot implements IWorldPositionProvider {
	
	public static final DataParameter<NBTTagCompound> CONNECTION = EntityDataManager.createKey(EntityLeadConnection.class, DataSerializers.COMPOUND_TAG);
	public static final DataParameter<Float> CHAIRX = EntityDataManager.createKey(EntityLeadConnection.class, DataSerializers.FLOAT);
	public static final DataParameter<Float> CHAIRY = EntityDataManager.createKey(EntityLeadConnection.class, DataSerializers.FLOAT);
	public static final DataParameter<Float> CHAIRZ = EntityDataManager.createKey(EntityLeadConnection.class, DataSerializers.FLOAT);
	private StructureChildConnection temp;
	
	public boolean isLeashed;
	public Entity leashHolder;
	public float prevRenderYawOffset;
	public float renderYawOffset;
	
	public EntityLeadConnection(World worldIn) {
		super(worldIn);
		noClip = true;
		preventEntitySpawning = true;
		isLeashed = false;
	}
	
	/*
	public EntityLeadConnection(World worldIn, BlockPos hangingPositionIn) {
		super(worldIn, hangingPositionIn);
		this.setPosition((double) hangingPositionIn.getX() + 0.5D, (double) hangingPositionIn.getY() + 0.5D, (double) hangingPositionIn.getZ() + 0.5D);
		float f = 0.125F;
		float f1 = 0.1875F;
		float f2 = 0.25F;
		this.forceSpawn = true;
	}
	*/
	public EntityLeadConnection(LittleLeadConnectionALET chair, World world, double x, double y, double z) {
		super(world, new BlockPos(x, y, z));
		dataManager.set(CHAIRX, (float) x);
		dataManager.set(CHAIRY, (float) y);
		dataManager.set(CHAIRZ, (float) z);
		this.temp = chair.generateConnection(this);
		this.dataManager.set(CONNECTION, temp.writeToNBT(new NBTTagCompound()));
		isLeashed = false;
		forceSpawn = true;
		noClip = true;
		preventEntitySpawning = false;
		this.setPosition(x, y, z);
	}
	
	@Override
	public boolean canBePushed() {
		return false;
	}
	
	@Override
	public void setPosition(double x, double y, double z) {
		
		this.posX = x;
		this.posY = y;
		this.posZ = z;
		if (this.isAddedToWorld() && !this.world.isRemote)
			this.world.updateEntityWithOptionalForce(this, false); // Forge - Process chunk registration after moving.
		float f = 0.2F;
		float f1 = 0.4F;
		this.setEntityBoundingBox(new AxisAlignedBB(x - (double) f, y - 0.1D, z - (double) f, x + (double) f, y + (double) f1, z + (double) f));
	}
	
	@Override
	public boolean hitByEntity(Entity entityIn) {
		return super.hitByEntity(entityIn);
	}
	
	@Override
	public boolean onValidSurface() {
		return true;
	}
	
	@Override
	public void onEntityUpdate() {
		super.onEntityUpdate();
	}
	
	@Override
	public void onUpdate() {
		if (this.getLeashHolder() == null) {
			this.isLeashed = false;
		}
		StructureChildConnection connection = StructureChildConnection.loadFromNBT(this, dataManager.get(CONNECTION), false);
		if (!world.isRemote && !isBeingRidden()) {
			try {
				
				LittleStructure structure = connection.getStructure();
				if (structure instanceof LittleLeadConnectionALET)
					((LittleLeadConnectionALET) structure).setPlayer(null);
			} catch (CorruptedConnectionException | NotYetConnectedException e) {
			}
			
		} else {
			try {
				LittleStructure structure = connection.getStructure();
				if (structure.getWorld() instanceof IOrientatedWorld) {
					Vector3d vec = new Vector3d(dataManager.get(CHAIRX), dataManager.get(CHAIRY), dataManager.get(CHAIRZ));
					((IOrientatedWorld) structure.getWorld()).getOrigin().transformPointToWorld(vec);
					setPosition(vec.x, vec.y, vec.z);
				}
			} catch (CorruptedConnectionException | NotYetConnectedException e) {
			}
		}
		super.onUpdate();
		this.prevRenderYawOffset = this.renderYawOffset;
		
	}
	
	@Override
	public double getMountedYOffset() {
		return 0;
	}
	
	@Override
	protected void entityInit() {
		this.dataManager.register(CONNECTION, new NBTTagCompound());
		this.dataManager.register(CHAIRX, 0F);
		this.dataManager.register(CHAIRY, 0F);
		this.dataManager.register(CHAIRZ, 0F);
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound nbt) {
		super.readEntityFromNBT(nbt);
		this.isLeashed = nbt.getBoolean("Leashed");
		
		dataManager.set(CONNECTION, nbt.getCompoundTag("connection"));
		dataManager.set(CHAIRX, nbt.getFloat("chairX"));
		dataManager.set(CHAIRY, nbt.getFloat("chairY"));
		dataManager.set(CHAIRZ, nbt.getFloat("chairZ"));
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound nbt) {
		super.writeEntityToNBT(nbt);
		nbt.setBoolean("Leashed", this.isLeashed);
		nbt.setTag("connection", dataManager.get(CONNECTION));
		nbt.setFloat("chairX", dataManager.get(CHAIRX));
		nbt.setFloat("chairY", dataManager.get(CHAIRY));
		nbt.setFloat("chairZ", dataManager.get(CHAIRZ));
	}
	
	public void setLeashHolder(Entity entityIn, @Nullable EntityPlayerMP receiverPlayer, boolean sendAttachNotification) {
		this.isLeashed = true;
		this.leashHolder = entityIn;
		
		if (!this.world.isRemote && sendAttachNotification && this.world instanceof WorldServer && this.leashHolder instanceof EntityLeadConnection) {
			PacketHandler.sendPacketToPlayer(new PacketConnectLead(this, this.leashHolder), receiverPlayer);
		}
		
		if (this.isRiding()) {
			this.dismountRidingEntity();
		}
	}
	
	public boolean getLeashed() {
		return this.isLeashed;
	}
	
	public Entity getLeashHolder() {
		return this.leashHolder;
	}
	
	public void clearLeashed(boolean sendPacket, boolean dropLead) {
		if (this.isLeashed) {
			this.isLeashed = false;
			this.leashHolder = null;
			
			if (!this.world.isRemote && dropLead) {
				this.dropItem(Items.LEAD, 1);
			}
			
			if (!this.world.isRemote && sendPacket && this.world instanceof WorldServer && this.leashHolder instanceof EntityPlayerMP) {
				PacketHandler.sendPacketToPlayer(new PacketConnectLead(this, null), (EntityPlayerMP) this.leashHolder);
			}
		}
	}
	
	@Override
	public boolean processInitialInteract(EntityPlayer player, EnumHand hand) {
		if (this.getLeashed() && this.getLeashHolder() == player) {
			this.clearLeashed(true, !player.capabilities.isCreativeMode);
			return true;
		} else {
			ItemStack itemstack = player.getHeldItem(hand);
			
			if (itemstack.getItem() == Items.LEAD) {
				this.setLeashHolder(player, (EntityPlayerMP) player, true);
				itemstack.shrink(1);
				return true;
			} else {
				return this.processInteract(player, hand) ? true : super.processInitialInteract(player, hand);
			}
		}
	}
	
	protected boolean processInteract(EntityPlayer player, EnumHand hand) {
		return false;
	}
	
	@Override
	public World getWorld() {
		return world;
	}
	
	@Override
	public BlockPos getPos() {
		return BlockPos.ORIGIN;
	}
	
	@Override
	public void onStructureDestroyed() {
	}
	
	@Override
	public int getWidthPixels() {
		return 9;
	}
	
	@Override
	public int getHeightPixels() {
		return 9;
	}
	
	/** Called when this entity is broken. Entity parameter may be null. */
	public void onBroken(@Nullable Entity brokenEntity) {
		this.playSound(SoundEvents.ENTITY_LEASHKNOT_BREAK, 1.0F, 1.0F);
	}
	
	public void playPlaceSound() {
		this.playSound(SoundEvents.ENTITY_LEASHKNOT_PLACE, 1.0F, 1.0F);
	}
	
	protected float updateDistance(float p_110146_1_, float p_110146_2_) {
		float f = MathHelper.wrapDegrees(p_110146_1_ - this.renderYawOffset);
		this.renderYawOffset += f * 0.3F;
		float f1 = MathHelper.wrapDegrees(this.rotationYaw - this.renderYawOffset);
		boolean flag = f1 < -90.0F || f1 >= 90.0F;
		
		if (f1 < -75.0F) {
			f1 = -75.0F;
		}
		
		if (f1 >= 75.0F) {
			f1 = 75.0F;
		}
		
		this.renderYawOffset = this.rotationYaw - f1;
		
		if (f1 * f1 > 2500.0F) {
			this.renderYawOffset += f1 * 0.2F;
		}
		
		if (flag) {
			p_110146_2_ *= -1.0F;
		}
		
		return p_110146_2_;
	}
	
}
