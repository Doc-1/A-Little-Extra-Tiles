package com.alet.common.structure.type;

import java.util.UUID;

import com.alet.common.entity.EntitySit;
import com.creativemd.creativecore.common.gui.container.GuiParent;
import com.creativemd.creativecore.common.gui.controls.gui.GuiCheckBox;
import com.creativemd.creativecore.common.gui.controls.gui.GuiIconButton;
import com.creativemd.creativecore.common.gui.controls.gui.GuiStateButton;
import com.creativemd.creativecore.common.gui.event.gui.GuiControlChangedEvent;
import com.creativemd.creativecore.common.gui.event.gui.GuiControlClickEvent;
import com.creativemd.creativecore.common.utils.math.BooleanUtils;
import com.creativemd.creativecore.common.world.CreativeWorld;
import com.creativemd.creativecore.common.world.IOrientatedWorld;
import com.creativemd.creativecore.common.world.SubWorld;
import com.creativemd.littletiles.client.gui.controls.GuiTileViewer;
import com.creativemd.littletiles.common.action.block.LittleActionActivated;
import com.creativemd.littletiles.common.structure.LittleStructure;
import com.creativemd.littletiles.common.structure.animation.AnimationGuiHandler;
import com.creativemd.littletiles.common.structure.connection.StructureChildConnection;
import com.creativemd.littletiles.common.structure.exception.CorruptedConnectionException;
import com.creativemd.littletiles.common.structure.exception.NotYetConnectedException;
import com.creativemd.littletiles.common.structure.registry.LittleStructureGuiParser;
import com.creativemd.littletiles.common.structure.registry.LittleStructureRegistry;
import com.creativemd.littletiles.common.structure.registry.LittleStructureType;
import com.creativemd.littletiles.common.structure.relative.StructureAbsolute;
import com.creativemd.littletiles.common.structure.relative.StructureRelative;
import com.creativemd.littletiles.common.structure.type.door.LittleAdvancedDoor;
import com.creativemd.littletiles.common.tile.LittleTile;
import com.creativemd.littletiles.common.tile.math.box.LittleBox;
import com.creativemd.littletiles.common.tile.math.vec.LittleAbsoluteVec;
import com.creativemd.littletiles.common.tile.parent.IStructureTileList;
import com.creativemd.littletiles.common.tile.preview.LittlePreviews;
import com.creativemd.littletiles.common.util.grid.LittleGridContext;
import com.creativemd.littletiles.common.world.LittleNeighborUpdateCollector;
import com.n247s.api.eventapi.eventsystem.CustomEventSubscribe;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
    
    public static class LittleAdvancedSeatParserALET extends LittleStructureGuiParser {
        
        public LittleAdvancedSeatParserALET(GuiParent parent, AnimationGuiHandler handler) {
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
            
            GuiStateButton contextBox = new GuiStateButton("grid", LittleGridContext.getNames().indexOf(viewer
                    .getAxisContext() + ""), 107, 80, 20, 12, LittleGridContext.getNames().toArray(new String[0]));
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
            handler.setCenter(new StructureAbsolute(new BlockPos(0, 75, 0), viewer.getBox().copy(), viewer
                    .getAxisContext()));
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
            LittleAdvancedSeat structure = createStructure(LittleAdvancedSeat.class, null);
            GuiTileViewer viewer = ((GuiTileViewer) parent.get("tileviewer"));
            structure.axisCenter = new StructureRelative(viewer.getBox(), viewer.getAxisContext());
            
            return structure;
        }
        
        @Override
        protected LittleStructureType getStructureType() {
            return LittleStructureRegistry.getStructureType(LittleAdvancedSeat.class);
        }
        
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
