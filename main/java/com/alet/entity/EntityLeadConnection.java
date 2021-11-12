package com.alet.entity;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.vecmath.Vector3d;

import com.alet.common.packet.PacketConnectLead;
import com.alet.common.structure.type.LittleLeadConnectionALET;
import com.creativemd.creativecore.common.packet.PacketHandler;
import com.creativemd.creativecore.common.world.IOrientatedWorld;
import com.creativemd.littletiles.common.entity.INoPushEntity;
import com.creativemd.littletiles.common.structure.LittleStructure;
import com.creativemd.littletiles.common.structure.connection.IWorldPositionProvider;
import com.creativemd.littletiles.common.structure.connection.StructureChildConnection;
import com.creativemd.littletiles.common.structure.exception.CorruptedConnectionException;
import com.creativemd.littletiles.common.structure.exception.NotYetConnectedException;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.Constants;

public class EntityLeadConnection extends Entity implements IWorldPositionProvider, INoPushEntity {
	
	public static final DataParameter<NBTTagCompound> CONNECTION = EntityDataManager.createKey(EntityLeadConnection.class, DataSerializers.COMPOUND_TAG);
	public static final DataParameter<NBTTagCompound> CONNECTIONS = EntityDataManager.createKey(EntityLeadConnection.class, DataSerializers.COMPOUND_TAG);
	public static final DataParameter<Float> CONNECTIONX = EntityDataManager.createKey(EntityLeadConnection.class, DataSerializers.FLOAT);
	public static final DataParameter<Float> CONNECTIONY = EntityDataManager.createKey(EntityLeadConnection.class, DataSerializers.FLOAT);
	public static final DataParameter<Float> CONNECTIONZ = EntityDataManager.createKey(EntityLeadConnection.class, DataSerializers.FLOAT);
	private StructureChildConnection temp;
	
	public boolean isLeashed;
	public Entity leashHolder;
	public Set<Integer> connectIDs = new HashSet<Integer>();
	public Set<UUID> connectUUIDs = new HashSet<UUID>();
	public float prevRenderYawOffset;
	public float renderYawOffset;
	
	public EntityLeadConnection(World worldIn) {
		super(worldIn);
		noClip = true;
		preventEntitySpawning = true;
		isLeashed = false;
		width = 0;
		height = 0;
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
	public EntityLeadConnection(LittleLeadConnectionALET connection, World world, double x, double y, double z) {
		super(world);
		//EntityLeashKnot
		dataManager.set(CONNECTIONX, (float) x);
		dataManager.set(CONNECTIONY, (float) y);
		dataManager.set(CONNECTIONZ, (float) z);
		this.temp = connection.generateConnection(this);
		this.dataManager.set(CONNECTION, temp.writeToNBT(new NBTTagCompound()));
		isLeashed = false;
		noClip = true;
		preventEntitySpawning = true;
		
		width = 1F;
		height = 1F;
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
		float f = 0.03F;
		this.setEntityBoundingBox(new AxisAlignedBB(x + f, y + f, z + f, x - f, y - f, z - f));
		
	}
	
	@Override
	public void setLocationAndAngles(double x, double y, double z, float yaw, float pitch) {
		this.posX = x;
		this.posY = y;
		this.posZ = z;
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		this.lastTickPosX = this.posX;
		this.lastTickPosY = this.posY;
		this.lastTickPosZ = this.posZ;
		this.rotationYaw = yaw;
		this.rotationPitch = pitch;
		this.setPosition(this.posX, this.posY, this.posZ);
	}
	
	@Override
	public float getEyeHeight() {
		return 0.0F;
	}
	
	public boolean onValidSurface() {
		return true;
	}
	
	@Override
	public void onUpdate() {
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		super.onUpdate();
		if (this.getLeashHolder() == null) {
			this.isLeashed = false;
		}
		StructureChildConnection connection = StructureChildConnection.loadFromNBT(this, dataManager.get(CONNECTION), false);
		if (world.isRemote) {
			try {
				LittleStructure structure = connection.getStructure();
				if (structure.getWorld() instanceof IOrientatedWorld) {
					Vector3d vec = new Vector3d(dataManager.get(CONNECTIONX), dataManager.get(CONNECTIONY), dataManager.get(CONNECTIONZ));
					((IOrientatedWorld) structure.getWorld()).getOrigin().transformPointToWorld(vec);
					setPosition(vec.x, vec.y, vec.z);
				}
			} catch (CorruptedConnectionException | NotYetConnectedException e) {
			}
		}
		this.prevRenderYawOffset = this.renderYawOffset;
		
	}
	
	@Override
	public double getMountedYOffset() {
		return 0;
	}
	
	@Override
	protected void entityInit() {
		this.dataManager.register(CONNECTION, new NBTTagCompound());
		this.dataManager.register(CONNECTIONS, new NBTTagCompound());
		this.dataManager.register(CONNECTIONX, 0F);
		this.dataManager.register(CONNECTIONY, 0F);
		this.dataManager.register(CONNECTIONZ, 0F);
	}
	
	@Override
	public boolean writeToNBTOptional(NBTTagCompound nbt) {
		
		return super.writeToNBTOptional(nbt);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		NBTTagList list = new NBTTagList();
		if (this.connectUUIDs != null)
			if (this.connectUUIDs.isEmpty()) {
				if (!this.connectIDs.isEmpty())
					for (int id : this.connectIDs) {
						Entity e = this.world.getEntityByID(id);
						if (e != null) {
							NBTTagCompound nbt2 = NBTUtil.createUUIDTag(e.getPersistentID());
							list.appendTag(nbt2);
						}
					}
			} else
				for (UUID uuid : connectUUIDs) {
					NBTTagCompound nbt2 = NBTUtil.createUUIDTag(uuid);
					list.appendTag(nbt2);
				}
		NBTTagCompound n = new NBTTagCompound();
		n.setTag("c", list);
		nbt.setTag("connections", n);
		return nbt;
	}
	
	@Override
	protected void readEntityFromNBT(NBTTagCompound nbt) {
		
		this.isLeashed = nbt.getBoolean("Leashed");
		dataManager.set(CONNECTION, nbt.getCompoundTag("connection"));
		dataManager.set(CONNECTIONS, nbt.getCompoundTag("connections"));
		dataManager.set(CONNECTIONX, nbt.getFloat("chairX"));
		dataManager.set(CONNECTIONY, nbt.getFloat("chairY"));
		dataManager.set(CONNECTIONZ, nbt.getFloat("chairZ"));
		NBTTagCompound n = nbt.getCompoundTag("connections");
		for (NBTBase nbt2 : n.getTagList("c", Constants.NBT.TAG_COMPOUND)) {
			WorldServer serverWorld = (WorldServer) this.getWorld();
			UUID uuid = NBTUtil.getUUIDFromTag((NBTTagCompound) nbt2);
			this.connectUUIDs.add(uuid);
		}
	}
	
	@Override
	protected void writeEntityToNBT(NBTTagCompound nbt) {
		nbt.setBoolean("Leashed", this.isLeashed);
		nbt.setTag("connection", dataManager.get(CONNECTION));
		nbt.setFloat("chairX", dataManager.get(CONNECTIONX));
		nbt.setFloat("chairY", dataManager.get(CONNECTIONY));
		nbt.setFloat("chairZ", dataManager.get(CONNECTIONZ));
		nbt.setTag("connections", dataManager.get(CONNECTIONS));
	}
	
	/*
	/entitydata @e[type=!player,r=3] {}
	 */
	public void updateLeashHolders(EntityPlayerMP receiverPlayer, boolean sendAttachNotification) {
		this.isLeashed = true;
		if (!this.world.isRemote && sendAttachNotification && this.world instanceof WorldServer) {
			PacketHandler.sendPacketToPlayer(new PacketConnectLead(this), receiverPlayer);
		}
		
		if (this.isRiding()) {
			this.dismountRidingEntity();
		}
	}
	
	public void updateLeashHolders(boolean sendAttachNotification) {
		this.isLeashed = true;
		System.out.println("why?");
		if (!this.world.isRemote && sendAttachNotification && this.world instanceof WorldServer) {
			PacketHandler.sendPacketToAllPlayers(new PacketConnectLead(this));
		}
	}
	
	public boolean getLeashed() {
		return this.isLeashed;
	}
	
	public Entity getLeashHolder() {
		return this.leashHolder;
	}
	
	/*
	public void clearLeashed(boolean sendPacket, boolean dropLead) {
		if (this.isLeashed) {
			this.isLeashed = false;
			this.leashHolder = null;
			if (!this.world.isRemote) {
				this.onBroken(null);
				this.setDead();
				if (dropLead)
					this.dropItem(Items.LEAD, 1);
			}
			
			if (!this.world.isRemote && sendPacket && this.world instanceof WorldServer && (this.leashHolder instanceof EntityLeadConnection || this.leashHolder instanceof EntityPlayerMP)) {
				PacketHandler.sendPacketToPlayer(new PacketConnectLead(this), (EntityPlayerMP) this.leashHolder);
			}
		}
	}
	*/
	@Override
	public boolean processInitialInteract(EntityPlayer player, EnumHand hand) {
		if (this.getLeashed() && this.getLeashHolder() == player) {
			//this.clearLeashed(true, !player.capabilities.isCreativeMode);
			return true;
		} else {
			ItemStack itemstack = player.getHeldItem(hand);
			
			if (itemstack.getItem() == Items.LEAD) {
				//this.setLeashHolder(player, (EntityPlayerMP) player, true);
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
	
}
