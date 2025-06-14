package com.alet.components.structures.type;

import java.util.UUID;

import com.alet.components.entities.EntitySit;
import com.creativemd.creativecore.common.utils.math.BooleanUtils;
import com.creativemd.creativecore.common.world.CreativeWorld;
import com.creativemd.creativecore.common.world.IOrientatedWorld;
import com.creativemd.creativecore.common.world.SubWorld;
import com.creativemd.littletiles.common.action.block.LittleActionActivated;
import com.creativemd.littletiles.common.structure.connection.StructureChildConnection;
import com.creativemd.littletiles.common.structure.exception.CorruptedConnectionException;
import com.creativemd.littletiles.common.structure.exception.NotYetConnectedException;
import com.creativemd.littletiles.common.structure.registry.LittleStructureType;
import com.creativemd.littletiles.common.structure.type.door.LittleAdvancedDoor;
import com.creativemd.littletiles.common.tile.LittleTile;
import com.creativemd.littletiles.common.tile.math.vec.LittleAbsoluteVec;
import com.creativemd.littletiles.common.tile.parent.IStructureTileList;
import com.creativemd.littletiles.common.world.LittleNeighborUpdateCollector;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class LittleAdvancedSeat extends LittleAdvancedDoor {
    
    private UUID sitUUID;
    private EntityPlayer player;
    
    public LittleAdvancedSeat(LittleStructureType type, IStructureTileList mainBlock) {
        super(type, mainBlock);
    }
    
    @Override
    protected void loadFromNBTExtra(NBTTagCompound nbt) {
        if (nbt.hasKey("connection"))
            sitUUID = UUID.fromString(nbt.getString("connection"));
        else
            sitUUID = null;
    }
    
    @Override
    protected void writeToNBTExtra(NBTTagCompound nbt) {
        if (sitUUID != null)
            nbt.setString("connection", sitUUID.toString());
        else
            nbt.removeTag("connection");
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
                    EntitySit sit = new EntitySit(this, world, vec.getPosX(), vec.getPosY() - 0.25, vec.getPosZ());
                    sitUUID = sit.getPersistentID();
                    player.startRiding(sit);
                    world.spawnEntity(sit);
                    setPlayer(player);
                }
            } catch (CorruptedConnectionException | NotYetConnectedException e) {
                e.printStackTrace();
            }
            
        }
        return true;
    }
    
    public void setPlayer(EntityPlayer player) {
        this.player = player;
        if (!getWorld().isRemote)
            getInput(0).updateState(BooleanUtils.asArray(player != null));
        if (this.player == null)
            sitUUID = null;
    }
    
    @Override
    public void afterPlaced() {
        super.afterPlaced();
        if (sitUUID != null) {
            World world = getWorld();
            if (world instanceof IOrientatedWorld) {
                if (world instanceof CreativeWorld && ((CreativeWorld) world).parent == null)
                    return;
                world = ((IOrientatedWorld) world).getRealWorld();
            }
            for (Entity entity : world.loadedEntityList)
                if (entity.getUniqueID().equals(sitUUID) && entity instanceof EntitySit) {
                    EntitySit sit = (EntitySit) entity;
                    StructureChildConnection temp = this.generateConnection(sit);
                    sit.getDataManager().set(EntitySit.CONNECTION, temp.writeToNBT(new NBTTagCompound()));
                    break;
                }
        }
    }
    
    @Override
    public void removeStructure(LittleNeighborUpdateCollector neighbor) throws CorruptedConnectionException, NotYetConnectedException {
        if (!this.getWorld().isRemote) {
            World world = this.getWorld();
            WorldServer serverWorld = null;
            
            if (this.getWorld() instanceof SubWorld) {
                SubWorld subWorld = (SubWorld) world;
                serverWorld = (WorldServer) subWorld.getRealWorld();
            } else if (this.getWorld() instanceof WorldServer)
                serverWorld = (WorldServer) this.getWorld();
            
            if (serverWorld != null) {
                Entity entity = serverWorld.getEntityFromUuid(sitUUID);
                if (entity != null && entity instanceof EntitySit) {
                    EntitySit connection = (EntitySit) entity;
                    connection.setDead();
                }
            }
        }
        super.removeStructure(neighbor);
    }
    
    @Override
    public void onStructureDestroyed() {
        
        super.onStructureDestroyed();
    }
    
}

/*
 * 
							
							if (entity instanceof EntitySit) {
								EntitySit lead = (EntitySit) entity;
								if (nbt.getInteger("connectionID") != connection.getEntityId()) {
									if (entity.getEntityId() != connection.getEntityId()) {
										nbt.setBoolean("playerIsHolding", false);
										nbt.setInteger("connectorID", entity.getEntityId());
										nbt.setInteger("connectionID", connection.getEntityId());
										connection.leashHolder = null;
										heldItem.setTagCompound(nbt);
										System.out.println("line 185");
									}
								} else {
									nbt.setBoolean("playerIsHolding", true);
									heldItem.setTagCompound(nbt);
									System.out.println("line 190");
								}
								break;
							}
						} else {
							if (entity instanceof EntityPlayer) {
								nbt.setBoolean("playerIsHolding", true);
								nbt.setInteger("connectorID", entity.getEntityId());
								nbt.setInteger("connectionID", connection.getEntityId());
								
								connection.leashHolder = entity;
								//connection.connectIDs.add(player.getEntityId());
								heldItem.setTagCompound(nbt);
								
								System.out.println("line 204");
								break;
							}
 */

/*
 * 
					for (Entity entity : world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB((double) i2 - 7.0D, (double) j2 - 7.0D, (double) k2 - 7.0D, (double) i2 + 7.0D, (double) j2 + 7.0D, (double) k2 + 7.0D))) {
						EntitySit entityToConnect = null;
						if (entity instanceof EntitySit) {
							entityToConnect = (EntitySit) entity;
							if (!entityToConnect.equals(connection)) {
								if (entityToConnect.leashHolder != null)
									System.out.println("not Null");
								connection.leashHolder = null;
								System.out.println("Entity" + entityToConnect.getEntityId());
								removeID(entityToConnect, player.getEntityId());
								removeID(connection, player.getEntityId());
								connection.connectIDs.add(entityToConnect.getEntityId());
								break;
							}
						} else if (entity instanceof EntityPlayer) {
							System.out.println("Player: " + connection.leashHolder);
							connection.leashHolder = entity;
							connection.connectIDs.add(player.getEntityId());
						}
					}
					
					connection.updateLeashHolders((EntityPlayerMP) player, true);
 */
