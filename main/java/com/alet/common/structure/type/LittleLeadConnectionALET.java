package com.alet.common.structure.type;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.vecmath.Vector3d;

import com.alet.entity.EntityLeadConnection;
import com.alet.entity.LeadConnectionData;
import com.alet.items.ItemLittleRope;
import com.creativemd.creativecore.common.gui.container.GuiParent;
import com.creativemd.creativecore.common.gui.controls.gui.GuiCheckBox;
import com.creativemd.creativecore.common.gui.controls.gui.GuiIconButton;
import com.creativemd.creativecore.common.gui.controls.gui.GuiStateButton;
import com.creativemd.creativecore.common.gui.event.gui.GuiControlChangedEvent;
import com.creativemd.creativecore.common.gui.event.gui.GuiControlClickEvent;
import com.creativemd.creativecore.common.world.CreativeWorld;
import com.creativemd.creativecore.common.world.IOrientatedWorld;
import com.creativemd.creativecore.common.world.SubWorld;
import com.creativemd.littletiles.client.gui.controls.GuiTileViewer;
import com.creativemd.littletiles.common.action.block.LittleActionActivated;
import com.creativemd.littletiles.common.structure.LittleStructure;
import com.creativemd.littletiles.common.structure.animation.AnimationGuiHandler;
import com.creativemd.littletiles.common.structure.connection.StructureChildConnection;
import com.creativemd.littletiles.common.structure.registry.LittleStructureGuiParser;
import com.creativemd.littletiles.common.structure.registry.LittleStructureRegistry;
import com.creativemd.littletiles.common.structure.registry.LittleStructureType;
import com.creativemd.littletiles.common.structure.relative.StructureAbsolute;
import com.creativemd.littletiles.common.structure.relative.StructureRelative;
import com.creativemd.littletiles.common.structure.type.door.LittleAdvancedDoor;
import com.creativemd.littletiles.common.tile.LittleTile;
import com.creativemd.littletiles.common.tile.math.box.LittleBox;
import com.creativemd.littletiles.common.tile.parent.IStructureTileList;
import com.creativemd.littletiles.common.tile.preview.LittlePreviews;
import com.creativemd.littletiles.common.util.grid.LittleGridContext;
import com.n247s.api.eventapi.eventsystem.CustomEventSubscribe;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class LittleLeadConnectionALET extends LittleAdvancedDoor {
	
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
		} else {
			
		}
	}
	
	public List<Entity> sortEntityList(List<Entity> entityList) {
		List<Entity> sortedEntityList = new ArrayList<Entity>();
		List<Entity> playerList = new ArrayList<Entity>();
		List<Entity> leadList = new ArrayList<Entity>();
		List<Entity> otherList = new ArrayList<Entity>();
		for (Entity entity : entityList) {
			if (entity instanceof EntityPlayer)
				playerList.add(entity);
			else if (entity instanceof EntityLeadConnection)
				leadList.add(entity);
			else if (entity instanceof IMob)
				otherList.add(entity);
		}
		
		if (leadList != null)
			for (Entity en : leadList) {
				sortedEntityList.add(en);
			}
		if (otherList != null)
			for (Entity en : otherList) {
				sortedEntityList.add(en);
			}
		if (playerList != null)
			for (Entity en : playerList) {
				sortedEntityList.add(en);
			}
		return sortedEntityList;
	}
	
	@Override
	public boolean onBlockActivated(World world, LittleTile tile, BlockPos pos, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ, LittleActionActivated action) {
		if (!world.isRemote) {
			if (this.player != null)
				return true;
			Vector3d vec3D = this.axisCenter.getCenter();
			if (vec3D != null && heldItem.getItem() instanceof ItemLittleRope) {
				if (world instanceof IOrientatedWorld)
					world = ((IOrientatedWorld) world).getRealWorld();
				double i1 = vec3D.x;
				double j1 = vec3D.y;
				double k1 = vec3D.z;
				double i2 = this.getStructureLocation().pos.getX() + i1;
				double j2 = this.getStructureLocation().pos.getY() + j1;
				double k2 = this.getStructureLocation().pos.getZ() + k1;
				WorldServer serverWorld = (WorldServer) world;
				
				EntityLeadConnection connection;
				
				if (serverWorld.getEntityFromUuid(connectionUUID) == null) {
					connection = new EntityLeadConnection(this, world, i2, j2, k2);
					world.spawnEntity(connection);
					if (heldItem.hasTagCompound()) {
						NBTTagCompound nbt = heldItem.getTagCompound();
						if (nbt.hasKey("playerIsHolding") && nbt.getBoolean("playerIsHolding")) {
							//nbt.setBoolean("playerIsHolding", false);
							heldItem.setTagCompound(nbt);
						}
					}
				} else {
					connection = (EntityLeadConnection) serverWorld.getEntityFromUuid(connectionUUID);
				}
				
				this.connectionUUID = connection.getPersistentID();
				NBTTagCompound nbt = new NBTTagCompound();
				boolean sameConnectionID = false;
				boolean playerIsHolding = false;
				int selectedID = -1;
				int prevSelectedID = -1;
				
				if (heldItem.hasTagCompound()) {
					nbt = heldItem.getTagCompound();
					if (nbt.hasKey("selectedID")) {
						sameConnectionID = nbt.getInteger("selectedID") == connection.getEntityId();
						nbt.setInteger("prevSelectedID", nbt.getInteger("selectedID"));
					}
				} else {
					nbt.setBoolean("playerIsHolding", false);
					heldItem.setTagCompound(nbt);
				}
				nbt.setInteger("selectedID", connection.getEntityId());
				selectedID = connection.getEntityId();
				if (nbt.hasKey("prevSelectedID"))
					prevSelectedID = nbt.getInteger("prevSelectedID");
				
				EntityLeadConnection en0 = (EntityLeadConnection) world.getEntityByID(selectedID);
				EntityLeadConnection en1 = (EntityLeadConnection) world.getEntityByID(prevSelectedID);
				
				if (en1 == null)
					en1 = (EntityLeadConnection) world.getEntityByID(selectedID);
				playerIsHolding = nbt.getBoolean("playerIsHolding");
				
				List<Entity> entityList = sortEntityList(world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB((double) i2 - 7.0D, (double) j2 - 7.0D, (double) k2 - 7.0D, (double) i2 + 7.0D, (double) j2 + 7.0D, (double) k2 + 7.0D)));
				for (Entity entity : entityList) {
					
					if (playerIsHolding) {
						if (!sameConnectionID) {
							/*
							en0.data.connectIDs.remove(player.getEntityId());
							en1.data.connectIDs.remove(player.getEntityId());
							en0.data.connectIDs.add(en1.getEntityId());*/
							//en0.connectionsMap.clear();
							//en0.connectionsMap.clear();
							
							int color = nbt.getInteger("color");
							double thickness = nbt.getDouble("thickness");
							double tautness = nbt.getDouble("tautness");
							LeadConnectionData data = null;
							for (LeadConnectionData d : en1.connectionsMap) {
								if (d.equals(new LeadConnectionData(color, thickness, tautness))) {
									data = d;
									break;
								}
							}
							if (data == null) {
								LeadConnectionData newData = new LeadConnectionData(color, thickness, tautness);
								newData.uuidsConnected.add(en0.getPersistentID());
								newData.idsConnected.add(en0.getEntityId());
								en1.connectionsMap.add(newData);
							} else {
								data.uuidsConnected.add(en0.getPersistentID());
								data.uuidsConnected.remove(player.getPersistentID());
								data.idsConnected.add(en0.getEntityId());
								data.idsConnected.remove(player.getEntityId());
							}
							nbt.setBoolean("playerIsHolding", false);
						} else {
							//remove player from en0
							boolean hasMatch = false;
							LeadConnectionData data = null;
							for (LeadConnectionData d : en0.connectionsMap)
								for (UUID uuid : d.uuidsConnected) {
									if (uuid.equals(player.getPersistentID())) {
										data = d;
										hasMatch = true;
										break;
									}
								}
							if (hasMatch) {
								data.uuidsConnected.remove(player.getPersistentID());
								data.idsConnected.remove(player.getEntityId());
							}
							nbt.setBoolean("playerIsHolding", false);
						}
					} else {
						int color = nbt.getInteger("color");
						double thickness = nbt.getDouble("thickness");
						double tautness = nbt.getDouble("tautness");
						LeadConnectionData data = null;
						for (LeadConnectionData d : en0.connectionsMap) {
							if (d.equals(new LeadConnectionData(color, thickness, tautness))) {
								data = d;
								break;
							}
						}
						if (data == null) {
							Set<UUID> connectUUIDs = new HashSet<UUID>();
							connectUUIDs.add(player.getPersistentID());
							LeadConnectionData newData = new LeadConnectionData(color, thickness, tautness);
							newData.uuidsConnected.add(player.getPersistentID());
							newData.idsConnected.add(player.getEntityId());
							en0.connectionsMap.add(newData);
						} else {
							data.uuidsConnected.add(player.getPersistentID());
							data.idsConnected.add(player.getEntityId());
						}
						nbt.setBoolean("playerIsHolding", true);
					}
				}
				
				en0.updateLeashHolders((EntityPlayerMP) player, true);
				en1.updateLeashHolders((EntityPlayerMP) player, true);
			}
			
		}
		return true;
		
	}
	
	/*connections:{group4:[
	 * [{tautness:8.699999809265137d,color:-16777216,thickness:0.4399999976158142d},{L:-4822490291572358863L,M:-6434875717049955617L}],
	 * [{tautness:8.699999809265137d,color:-16777216,thickness:0.4399999976158142d},{L:-4822490291572358863L,M:-6434875717049955617L}],
	 * [{tautness:8.699999809265137d,color:-16777216,thickness:0.4399999976158142d},{L:-4822490291572358863L,M:-6434875717049955617L}],
	 * [{tautness:8.699999809265137d,color:-16777216,thickness:0.4399999976158142d},{L:-4822490291572358863L,M:-6434875717049955617L}],
	 * [{tautness:8.699999809265137d,color:-16777216,thickness:0.4399999976158142d},{L:-4822490291572358863L,M:-6434875717049955617L}]]}}
	*/
	@Override
	public void onStructureDestroyed() {
		if (!this.getWorld().isRemote) {
			World world = this.getWorld();
			WorldServer serverWorld = null;
			
			if (this.getWorld() instanceof SubWorld) {
				SubWorld subWorld = (SubWorld) world;
				serverWorld = (WorldServer) subWorld.getRealWorld();
			} else if (this.getWorld() instanceof WorldServer)
				serverWorld = (WorldServer) this.getWorld();
			
			if (serverWorld != null) {
				Entity entity = serverWorld.getEntityFromUuid(connectionUUID);
				if (entity != null && entity instanceof EntityLeadConnection) {
					EntityLeadConnection connection = (EntityLeadConnection) entity;
					if (connection.connectionsMap != null && !connection.connectionsMap.isEmpty())
						for (LeadConnectionData data : connection.connectionsMap)
							for (int id : data.idsConnected) {
								Entity en = serverWorld.getEntityByID(id);
								if (en instanceof EntityLeadConnection) {
									EntityLeadConnection connected = (EntityLeadConnection) en;
									data.idsConnected.remove(connection.getEntityId());
									//connection.setDead();
								}
							}
				}
			}
		}
		
		super.onStructureDestroyed();
	}
	
	public static class LittleLeadConnectionParserALET extends LittleStructureGuiParser {
		
		public LittleLeadConnectionParserALET(GuiParent parent, AnimationGuiHandler handler) {
			super(parent, handler);
		}
		
		@Override
		protected void createControls(LittlePreviews previews, LittleStructure structure) {
			LittleAdvancedDoor door = structure instanceof LittleAdvancedDoor ? (LittleAdvancedDoor) structure : null;
			
			GuiTileViewer viewer = new GuiTileViewer("tileviewer", 0, 0, 100, 100, previews.getContext());
			setViewer(viewer, door, previews.getContext());
			parent.controls.add(viewer);
			parent.controls.add(new GuiIconButton("reset view", 20, 107, 8) {
				
				@Override
				public void onClicked(int x, int y, int button) {
					viewer.offsetX.set(0);
					viewer.offsetY.set(0);
					viewer.scale.set(40);
				}
			}.setCustomTooltip("reset view"));
			parent.controls.add(new GuiIconButton("change view", 40, 107, 7) {
				
				@Override
				public void onClicked(int x, int y, int button) {
					switch (viewer.getAxis()) {
					case X:
						viewer.setViewAxis(EnumFacing.Axis.Y);
						break;
					case Y:
						viewer.setViewAxis(EnumFacing.Axis.Z);
						break;
					case Z:
						viewer.setViewAxis(EnumFacing.Axis.X);
						break;
					default:
						break;
					}
				}
			}.setCustomTooltip("change view"));
			parent.controls.add(new GuiIconButton("flip view", 60, 107, 4) {
				
				@Override
				public void onClicked(int x, int y, int button) {
					viewer.setViewDirection(viewer.getViewDirection().getOpposite());
				}
			}.setCustomTooltip("flip view"));
			
			parent.controls.add(new GuiIconButton("up", 124, 33, 1) {
				
				@Override
				public void onClicked(int x, int y, int button) {
					viewer.moveY(GuiScreen.isCtrlKeyDown() ? viewer.context.size : 1);
				}
			});
			
			parent.controls.add(new GuiIconButton("right", 141, 50, 0) {
				
				@Override
				public void onClicked(int x, int y, int button) {
					viewer.moveX(GuiScreen.isCtrlKeyDown() ? viewer.context.size : 1);
				}
			});
			
			parent.controls.add(new GuiIconButton("left", 107, 50, 2) {
				
				@Override
				public void onClicked(int x, int y, int button) {
					viewer.moveX(-(GuiScreen.isCtrlKeyDown() ? viewer.context.size : 1));
				}
			});
			
			parent.controls.add(new GuiIconButton("down", 124, 50, 3) {
				
				@Override
				public void onClicked(int x, int y, int button) {
					viewer.moveY(-(GuiScreen.isCtrlKeyDown() ? viewer.context.size : 1));
				}
			});
			parent.controls.add(new GuiCheckBox("even", 107, 0, viewer.isEven()));
			
			GuiStateButton contextBox = new GuiStateButton("grid", LittleGridContext.getNames().indexOf(viewer.getAxisContext() + ""), 107, 80, 20, 12, LittleGridContext.getNames().toArray(new String[0]));
			parent.controls.add(contextBox);
		}
		
		private void setViewer(GuiTileViewer viewer, LittleAdvancedDoor door, LittleGridContext axisContext) {
			viewer.visibleAxis = true;
			boolean even = false;
			if (door != null) {
				even = door.axisCenter.isEven();
				viewer.setEven(even);
				
				door.axisCenter.convertToSmallest();
				axisContext = door.axisCenter.getContext();
				viewer.setAxis(door.axisCenter.getBox(), axisContext);
				
			} else {
				viewer.setEven(false);
				viewer.setAxis(new LittleBox(0, 0, 0, 1, 1, 1), viewer.context);
			}
			handler.setCenter(new StructureAbsolute(new BlockPos(0, 75, 0), viewer.getBox().copy(), viewer.getAxisContext()));
		}
		
		@CustomEventSubscribe
		@SideOnly(Side.CLIENT)
		public void onButtonClicked(GuiControlClickEvent event) {
			GuiTileViewer viewer = (GuiTileViewer) event.source.parent.get("tileviewer");
			if (event.source.is("even")) {
				viewer.setEven(((GuiCheckBox) event.source).value);
			}
		}
		
		@CustomEventSubscribe
		@SideOnly(Side.CLIENT)
		public void onStateChange(GuiControlChangedEvent event) {
			if (event.source.is("grid")) {
				GuiStateButton contextBox = (GuiStateButton) event.source;
				LittleGridContext context;
				try {
					context = LittleGridContext.get(Integer.parseInt(contextBox.getCaption()));
				} catch (NumberFormatException e) {
					context = LittleGridContext.get();
				}
				
				GuiTileViewer viewer = (GuiTileViewer) event.source.parent.get("tileviewer");
				LittleBox box = viewer.getBox();
				box.convertTo(viewer.getAxisContext(), context);
				
				if (viewer.isEven())
					box.maxX = box.minX + 2;
				else
					box.maxX = box.minX + 1;
				
				if (viewer.isEven())
					box.maxY = box.minY + 2;
				else
					box.maxY = box.minY + 1;
				
				if (viewer.isEven())
					box.maxZ = box.minZ + 2;
				else
					box.maxZ = box.minZ + 1;
				
				viewer.setAxis(box, context);
			}
		}
		
		@Override
		protected LittleStructure parseStructure(LittlePreviews previews) {
			LittleLeadConnectionALET structure = createStructure(LittleLeadConnectionALET.class, null);
			GuiTileViewer viewer = ((GuiTileViewer) parent.get("tileviewer"));
			structure.axisCenter = new StructureRelative(viewer.getBox(), viewer.getAxisContext());
			
			return structure;
		}
		
		@Override
		protected LittleStructureType getStructureType() {
			return LittleStructureRegistry.getStructureType(LittleLeadConnectionALET.class);
		}
		
	}
	
}

/*
 * 
							
							if (entity instanceof EntityLeadConnection) {
								EntityLeadConnection lead = (EntityLeadConnection) entity;
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
						EntityLeadConnection entityToConnect = null;
						if (entity instanceof EntityLeadConnection) {
							entityToConnect = (EntityLeadConnection) entity;
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
