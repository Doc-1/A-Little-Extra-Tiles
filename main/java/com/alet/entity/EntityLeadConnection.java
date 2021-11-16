package com.alet.entity;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Nullable;
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

import io.netty.buffer.ByteBuf;
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
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

public class EntityLeadConnection extends Entity implements IWorldPositionProvider, INoPushEntity, IEntityAdditionalSpawnData {
	
	public static final DataParameter<NBTTagCompound> CONNECTION = EntityDataManager.createKey(EntityLeadConnection.class, DataSerializers.COMPOUND_TAG);
	public static final DataParameter<Float> CONNECTIONX = EntityDataManager.createKey(EntityLeadConnection.class, DataSerializers.FLOAT);
	public static final DataParameter<Float> CONNECTIONY = EntityDataManager.createKey(EntityLeadConnection.class, DataSerializers.FLOAT);
	public static final DataParameter<Float> CONNECTIONZ = EntityDataManager.createKey(EntityLeadConnection.class, DataSerializers.FLOAT);
	private StructureChildConnection temp;
	
	public Set<LeadConnectionData> connectionsMap = new HashSet<LeadConnectionData>();
	public float prevRenderYawOffset;
	public float renderYawOffset;
	
	public EntityLeadConnection(World worldIn) {
		super(worldIn);
		noClip = true;
		preventEntitySpawning = true;
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
	public float getEyeHeight() {
		return 0.0F;
	}
	
	public boolean onValidSurface() {
		return true;
	}
	
	@Override
	public void onUpdate() {
		super.onUpdate();
		this.prevRenderYawOffset = this.renderYawOffset;
		
		this.entityFollowDoor();
	}
	
	public void entityFollowDoor() {
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
	}
	
	@Override
	public double getMountedYOffset() {
		return 0;
	}
	
	@Override
	protected void entityInit() {
		this.dataManager.register(CONNECTION, new NBTTagCompound());
		this.dataManager.register(CONNECTIONX, 0F);
		this.dataManager.register(CONNECTIONY, 0F);
		this.dataManager.register(CONNECTIONZ, 0F);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		return super.writeToNBT(compound);
	}
	
	/*
	 * 
	 * Every connection needs its own Color, Thickness, and Tautness
	 * If two or more connections have the same color, thickness, and tautness, put them in the same group.
	 * 
	 * connections:{c1:[data:[color:red,thickness:0.5,tautness:5],{L:-8980113312732706074L,M:-3781636954186697522L}],
	 * c2:[data:[color:red,thickness:0.5,tautness:5],{L:-8980113312732706074L,M:-3781636954186697522L}]}
	 */
	
	public void breakDownNBT(NBTTagList list) {
		NBTTagCompound nbt = new NBTTagCompound();
		NBTTagCompound nbtData = (NBTTagCompound) list.get(0);
		double tautness = nbtData.getDouble("tautness");
		double thickness = nbtData.getDouble("thickness");
		int color = nbtData.getInteger("color");
		System.out.println(nbtData);
		list.removeTag(0);
		Set<UUID> uuids = new HashSet<UUID>();
		for (NBTBase base : list) {
			NBTTagCompound ML = (NBTTagCompound) base;
			long M = ML.getLong("M");
			long L = ML.getLong("L");
			uuids.add(new UUID(M, L));
		}
		LeadConnectionData data = new LeadConnectionData(color, thickness, tautness);
		data.uuidsConnected = uuids;
		this.connectionsMap.add(data);
	}
	
	@Override
	protected void readEntityFromNBT(NBTTagCompound nbt) {
		dataManager.set(CONNECTION, nbt.getCompoundTag("connection"));
		dataManager.set(CONNECTIONX, nbt.getFloat("chairX"));
		dataManager.set(CONNECTIONY, nbt.getFloat("chairY"));
		dataManager.set(CONNECTIONZ, nbt.getFloat("chairZ"));
		System.out.println("Da");
		if (nbt.getTagId("connections") == Constants.NBT.TAG_LIST)
			for (NBTBase base : nbt.getTagList("connections", Constants.NBT.TAG_LIST)) {
				NBTTagList list = (NBTTagList) base;
				breakDownNBT(list);
			}
	}
	
	@Override
	protected void writeEntityToNBT(NBTTagCompound nbt) {
		nbt.setTag("connection", dataManager.get(CONNECTION));
		nbt.setFloat("chairX", dataManager.get(CONNECTIONX));
		nbt.setFloat("chairY", dataManager.get(CONNECTIONY));
		nbt.setFloat("chairZ", dataManager.get(CONNECTIONZ));
		
		NBTTagList list = new NBTTagList();
		
		if (!this.connectionsMap.isEmpty()) {
			int i = 0;
			for (LeadConnectionData data : this.connectionsMap) {
				NBTTagList connectionList = new NBTTagList();
				NBTTagCompound connectionData = new NBTTagCompound();
				connectionData.setInteger("color", data.color);
				connectionData.setDouble("thickness", data.thickness);
				connectionData.setDouble("tautness", data.tautness);
				connectionList.appendTag(connectionData);
				for (UUID uuid : data.uuidsConnected) {
					NBTTagCompound uuidNBT = NBTUtil.createUUIDTag(uuid);
					connectionList.appendTag(uuidNBT);
				}
				list.appendTag(connectionList);
				nbt.setTag("connections", list);
				i++;
			}
		}
		//System.out.println(nbt);
	}
	
	/*
	/entitydata @e[type=!player,r=3] {}
	 */
	public void updateLeashHolders(@Nullable EntityPlayerMP receiverPlayer, boolean sendAttachNotification) {
		if (!this.world.isRemote && sendAttachNotification && this.world instanceof WorldServer) {
			Object[] arrayData = this.connectionsMap.toArray();
			if (receiverPlayer != null) {
				for (int i = 0; i < arrayData.length; i++) {
					PacketHandler.sendPacketToPlayer(new PacketConnectLead(this, (LeadConnectionData) arrayData[i], i), receiverPlayer);
				}
			} else {
				for (int i = 0; i < arrayData.length; i++) {
					PacketHandler.sendPacketToPlayer(new PacketConnectLead(this, (LeadConnectionData) arrayData[i], i), receiverPlayer);
				}
			}
		}
		if (this.isRiding()) {
			this.dismountRidingEntity();
		}
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
		ItemStack itemstack = player.getHeldItem(hand);
		
		if (itemstack.getItem() == Items.LEAD) {
			//this.setLeashHolder(player, (EntityPlayerMP) player, true);
			itemstack.shrink(1);
			return true;
		} else {
			return this.processInteract(player, hand) ? true : super.processInitialInteract(player, hand);
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
	public void writeSpawnData(ByteBuf buf) {
		buf.writeByte(this.connectionsMap.size());
		for (LeadConnectionData data : this.connectionsMap) {
			System.out.println(data.color + " " + data.thickness + " " + data.tautness);
			buf.writeByte(data.uuidsConnected.size());
			buf.writeInt(data.color);
			buf.writeDouble(data.thickness);
			buf.writeDouble(data.tautness);
			for (UUID uuid : data.uuidsConnected) {
				buf.writeLong(uuid.getMostSignificantBits());
				buf.writeLong(uuid.getLeastSignificantBits());
			}
		}
	}
	
	@Override
	public void readSpawnData(ByteBuf buf) {
		byte dataSize = buf.readByte();
		LeadConnectionData data = null;
		for (int i = 0; i < dataSize; i++) {
			byte uuidSize = buf.readByte();
			int color = buf.readInt();
			double thickness = buf.readDouble();
			double tautness = buf.readDouble();
			data = new LeadConnectionData(color, thickness, tautness);
			for (int j = 0; j < uuidSize; j++) {
				long M = buf.readLong();
				long L = buf.readLong();
				data.uuidsConnected.add(new UUID(M, L));
			}
			this.connectionsMap.add(data);
			
		}
		
	}
	
}
