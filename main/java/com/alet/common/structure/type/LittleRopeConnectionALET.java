package com.alet.common.structure.type;

import java.util.BitSet;
import java.util.HashMap;

import javax.vecmath.Vector3d;

import org.lwjgl.util.Color;

import com.alet.common.entity.RopeData;
import com.alet.common.structure.connection.RopeConnection;
import com.alet.items.ItemLittleRope;
import com.creativemd.creativecore.common.gui.container.GuiParent;
import com.creativemd.creativecore.common.gui.controls.gui.GuiCheckBox;
import com.creativemd.creativecore.common.gui.controls.gui.GuiIconButton;
import com.creativemd.creativecore.common.gui.controls.gui.GuiStateButton;
import com.creativemd.creativecore.common.gui.event.gui.GuiControlChangedEvent;
import com.creativemd.creativecore.common.gui.event.gui.GuiControlClickEvent;
import com.creativemd.creativecore.common.utils.math.vec.Vec3;
import com.creativemd.creativecore.common.utils.mc.ColorUtils;
import com.creativemd.creativecore.common.world.IOrientatedWorld;
import com.creativemd.littletiles.client.gui.controls.GuiTileViewer;
import com.creativemd.littletiles.common.action.LittleActionException;
import com.creativemd.littletiles.common.action.block.LittleActionActivated;
import com.creativemd.littletiles.common.structure.LittleStructure;
import com.creativemd.littletiles.common.structure.animation.AnimationGuiHandler;
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
import com.creativemd.littletiles.common.tile.math.location.StructureLocation;
import com.creativemd.littletiles.common.tile.parent.IStructureTileList;
import com.creativemd.littletiles.common.tile.preview.LittlePreviews;
import com.creativemd.littletiles.common.util.grid.LittleGridContext;
import com.n247s.api.eventapi.eventsystem.CustomEventSubscribe;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class LittleRopeConnectionALET extends LittleAdvancedDoor {
    
    public HashMap<Integer, RopeData> ropeData = new HashMap<Integer, RopeData>();
    public HashMap<Integer, RopeConnection> connections = new HashMap<Integer, RopeConnection>();
    
    public LittleRopeConnectionALET(LittleStructureType type, IStructureTileList mainBlock) {
        super(type, mainBlock);
        
    }
    
    @Override
    protected void loadFromNBTExtra(NBTTagCompound nbt) {
        NBTTagList conList = nbt.getTagList("conList", Constants.NBT.TAG_COMPOUND);
        for (NBTBase n : conList) {
            connections.put(((NBTTagCompound) n).getInteger("ropeID"), new RopeConnection(this, (NBTTagCompound) n));
        }
        NBTTagList dataList = nbt.getTagList("dataList", Constants.NBT.TAG_COMPOUND);
        for (NBTBase n : dataList) {
            ropeData.put(((NBTTagCompound) n).getInteger("ropeID"), new RopeData((NBTTagCompound) n));
        }
    }
    
    @Override
    protected void writeToNBTExtra(NBTTagCompound nbt) {
        NBTTagList conList = new NBTTagList();
        NBTTagList dataList = new NBTTagList();
        for (RopeConnection con : connections.values()) {
            con.getTarget();
            con.getTargetCenter();
            conList.appendTag(con.writeToNBT(new NBTTagCompound()));
            if (!con.IS_HEAD)
                continue;
            RopeData data = this.ropeData.get(con.ropeID);
            NBTTagCompound n = data.writeData();
            n.setInteger("ropeID", con.ropeID);
            dataList.appendTag(n);
        }
        nbt.setTag("conList", conList);
        nbt.setTag("dataList", dataList);
        
    }
    
    @Override
    protected void afterPlaced() {
        for (RopeConnection d : this.connections.values()) {
            try {
                LittleStructure struct = d.getStructure();
                if (struct instanceof LittleRopeConnectionALET) {
                    LittleRopeConnectionALET rope = (LittleRopeConnectionALET) struct;
                    RopeConnection con = rope.connections.get(d.ropeID);
                    if (con != null && con.adaptStructureChange(this)) {
                        struct.updateStructure();
                    }
                }
            } catch (CorruptedConnectionException | NotYetConnectedException e) {
                e.printStackTrace();
            }
            
        }
    }
    
    public void bezier(Vec3 pFinal, Vec3 p0, Vec3 p1, Vec3 p2, float t) {
        pFinal.x = Math.pow(1 - t, 2) * p0.x + (1 - t) * 2 * t * p1.x + t * t * p2.x;
        pFinal.y = Math.pow(1 - t, 2) * p0.y + (1 - t) * 2 * t * p1.y + t * t * p2.y;
        pFinal.z = Math.pow(1 - t, 2) * p0.z + (1 - t) * 2 * t * p1.z + t * t * p2.z;
    }
    
    @Override
    public double getMaxRenderDistanceSquared() {
        return 2500;
    }
    
    @Override
    public boolean onBlockActivated(World world, LittleTile tile, BlockPos pos, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ, LittleActionActivated action) {
        if (heldItem.getItem() instanceof ItemLittleRope) {
            if (!ItemLittleRope.hasSelected(heldItem)) //Tail == 1
                ItemLittleRope.addSelected(heldItem, this.getStructureLocation());
            else {
                try {
                    StructureLocation tailLocation = ItemLittleRope.getSelectedLocation(heldItem);
                    World w = this.getWorld();
                    if (w instanceof IOrientatedWorld)
                        w = ((IOrientatedWorld) w).getRealWorld();
                    LittleRopeConnectionALET tail = (LittleRopeConnectionALET) tailLocation.find(w);
                    BitSet set = new BitSet();
                    this.fillInIDs(set);
                    tail.fillInIDs(set);
                    int ropeID = set.nextClearBit(0);
                    RopeData data = ItemLittleRope.writeDataFromNBT(heldItem.getTagCompound());
                    
                    if (this.matchingConnection(data, tail))
                        return true;
                    
                    tail.connections.put(ropeID, new RopeConnection(tail, this.getStructureLocation(), ropeID, false));
                    this.connections.put(ropeID, new RopeConnection(this, tailLocation, ropeID, true));
                    this.ropeData.put(ropeID, data);
                    this.updateStructure();
                    tail.updateStructure();
                } catch (LittleActionException e) {
                    e.printStackTrace();
                }
                ItemLittleRope.removeSelected(heldItem);
            }
        }
        
        return true;
        
    }
    
    public void fillInIDs(BitSet set) {
        for (RopeConnection con : connections.values())
            set.set(con.ropeID);
    }
    
    public boolean matchingConnection(RopeData data, LittleRopeConnectionALET otherStructure) {
        for (RopeConnection con : otherStructure.connections.values()) {
            try {
                if (con.getStructure() == this) {
                    RopeData otherData = con.IS_HEAD ? otherStructure.ropeData.get(con.ropeID) : this.ropeData.get(con.ropeID);
                    if (otherData.equals(data))
                        return true;
                }
            } catch (CorruptedConnectionException | NotYetConnectedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        
        for (RopeConnection con : this.connections.values()) {
            try {
                if (con.getStructure() == this) {
                    RopeData thisData = con.IS_HEAD ? this.ropeData.get(con.ropeID) : otherStructure.ropeData.get(con.ropeID);
                    if (thisData.equals(data))
                        return true;
                }
            } catch (CorruptedConnectionException | NotYetConnectedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return false;
    }
    
    @Override
    public void unload() {
        connections.clear();
        super.unload();
    }
    
    @Override
    public void onStructureDestroyed() {
        super.onStructureDestroyed();
        for (RopeConnection con : this.connections.values()) {
            try {
                LittleRopeConnectionALET structure = (LittleRopeConnectionALET) con.getStructure();
                structure.connections.remove(con.ropeID);
                structure.ropeData.remove(con.ropeID);
                structure.updateStructure();
            } catch (CorruptedConnectionException | NotYetConnectedException e) {
                e.printStackTrace();
            }
        }
    }
    
    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        Vector3d center = this.axisCenter.getCenter();
        center.x += getPos().getX();
        center.y += getPos().getY();
        center.z += getPos().getZ();
        double minX = center.x - 0.5;
        double minY = center.y - 0.5;
        double minZ = center.z - 0.5;
        double maxX = center.x + 0.5;
        double maxY = center.y + 0.5;
        double maxZ = center.z + 0.5;
        
        for (RopeConnection con : connections.values()) {
            if (!con.IS_HEAD)
                continue;
            RopeData data = ropeData.get(con.ropeID); //is Head
            minX = Math.min(con.getTargetCenter().x - data.thickness, minX);
            minY = Math.min(con.getTargetCenter().y - data.thickness, minY);
            minZ = Math.min(con.getTargetCenter().z - data.thickness, minZ);
            maxX = Math.max(con.getTargetCenter().x + data.thickness, maxX);
            maxY = Math.max(con.getTargetCenter().y + data.thickness, maxY);
            maxZ = Math.max(con.getTargetCenter().z + data.thickness, maxZ);
        }
        minX -= getPos().getX();
        minY -= getPos().getY();
        minZ -= getPos().getZ();
        maxX -= getPos().getX();
        maxY -= getPos().getY();
        maxZ -= getPos().getZ();
        
        return new AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ);
    }
    
    @Override
    public void renderTick(BlockPos pos, double x, double y, double z, float partialTickTime) {
        
        for (RopeConnection connection : this.connections.values()) {
            if (!connection.IS_HEAD)
                continue;
            RopeData data = this.ropeData.get(connection.ropeID);
            Vector3d headPos = this.axisCenter.getCenter();
            // headPos.add(new Vector3d(1, 1, 1));
            Vector3d vecPos = new Vector3d(pos.getX(), pos.getY(), pos.getZ());
            LittleStructure target = connection.getTarget();
            Vector3d tailPos = (Vector3d) connection.getTargetCenter().clone();
            if (connection.hasMoved(tailPos))
                this.mainBlock.getTe().render.markRenderBoundingBoxDirty();
            tailPos.sub(vecPos);
            double p0x = headPos.x;
            double p0y = headPos.y;
            double p0z = headPos.z;
            double p2x = tailPos.x;
            double p2y = tailPos.y;
            double p2z = tailPos.z;
            double p1x = (p0x + p2x) / 2; //Middle
            double p1y = ((p0y + p2y) / 2) - data.tautness;//Middle
            double p1z = (p0z + p2z) / 2;//Middle
            Vec3 startPoint = new Vec3(p0x, p0y, p0z);
            Vec3 midPoint = new Vec3(p1x, p1y, p1z);
            Vec3 endPoint = new Vec3(p2x, p2y, p2z);
            Vec3 drawPoint = new Vec3(0, 0, 0);
            Color c = ColorUtils.IntToRGBA(data.color);
            float red = c.getRed() / 255F;
            float green = c.getGreen() / 255F;
            float blue = c.getBlue() / 255F;
            float alpha = c.getAlpha() / 255F;
            
            GlStateManager.pushMatrix();
            //GlStateManager.disableLighting();
            GlStateManager.enableBlend();
            GlStateManager
                    .tryBlendFuncSeparate(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO, GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            GlStateManager.color(red, green, blue, alpha);
            GlStateManager.disableTexture2D();
            GlStateManager.enableAlpha();
            GlStateManager.disableCull();
            GlStateManager.disableLighting();
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuffer();
            GlStateManager.translate(x, y, z);
            //GlStateManager.translate(-translatePos.x, -translatePos.y, -translatePos.z);
            int count = (int) (startPoint.distance(endPoint) * 3);
            bufferbuilder.begin(5, DefaultVertexFormats.POSITION_COLOR);
            for (int j = 0; j <= count; ++j) {
                float f3 = (float) j / count;
                bezier(drawPoint, startPoint, midPoint, endPoint, f3);
                float r = red;
                float g = green;
                float b = blue;
                if (j % 2 == 0) {
                    r = Math.max(0, red - 0.1F);
                    g = Math.max(0, green - 0.1F);
                    b = Math.max(0, blue - 0.1F);
                }
                
                bufferbuilder.pos(drawPoint.x - data.thickness, drawPoint.y, drawPoint.z).color(r, g, b, alpha).endVertex();
                bufferbuilder.pos(drawPoint.x + data.thickness, drawPoint.y, drawPoint.z).color(r, g, b, alpha).endVertex();
                
            }
            tessellator.draw();
            bufferbuilder.begin(5, DefaultVertexFormats.POSITION_COLOR);
            
            for (int j = 0; j <= count; ++j) {
                
                float f3 = (float) j / count;
                
                bezier(drawPoint, startPoint, midPoint, endPoint, f3);
                float r = red;
                float g = green;
                float b = blue;
                if (j % 2 == 0) {
                    r = Math.max(0, red - 0.1F);
                    g = Math.max(0, green - 0.1F);
                    b = Math.max(0, blue - 0.1F);
                }
                
                bufferbuilder.pos(drawPoint.x, drawPoint.y, drawPoint.z - data.thickness).color(r, g, b, alpha).endVertex();
                bufferbuilder.pos(drawPoint.x, drawPoint.y, drawPoint.z + data.thickness).color(r, g, b, alpha).endVertex();
            }
            
            tessellator.draw();
            bufferbuilder.begin(5, DefaultVertexFormats.POSITION_COLOR);
            
            for (int j = 0; j <= count; ++j) {
                
                float f3 = (float) j / count;
                
                bezier(drawPoint, startPoint, midPoint, endPoint, f3);
                float r = red;
                float g = green;
                float b = blue;
                if (j % 2 == 0) {
                    r = Math.max(0, red - 0.1F);
                    g = Math.max(0, green - 0.1F);
                    b = Math.max(0, blue - 0.1F);
                }
                
                bufferbuilder.pos(drawPoint.x, drawPoint.y - data.thickness, drawPoint.z).color(r, g, b, alpha).endVertex();
                bufferbuilder.pos(drawPoint.x, drawPoint.y + data.thickness, drawPoint.z).color(r, g, b, alpha).endVertex();
            }
            tessellator.draw();
            GlStateManager.popMatrix();
            GlStateManager.enableTexture2D();
        }
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
            
            GuiStateButton contextBox = new GuiStateButton("grid", LittleGridContext.getNames().indexOf(viewer.getAxisContext() + ""), 107, 80, 20, 12, LittleGridContext.getNames()
                    .toArray(new String[0]));
            parent.controls.add(contextBox);
        }
        
        private void setViewer(GuiTileViewer viewer, LittleAdvancedDoor door, LittleGridContext axisContext) {
            viewer.visibleAxis = true;
            if (door != null) {
                viewer.setEven(door.axisCenter.isEven());
                
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
            LittleRopeConnectionALET structure = createStructure(LittleRopeConnectionALET.class, null);
            GuiTileViewer viewer = ((GuiTileViewer) parent.get("tileviewer"));
            structure.axisCenter = new StructureRelative(viewer.getBox(), viewer.getAxisContext());
            
            return structure;
        }
        
        @Override
        protected LittleStructureType getStructureType() {
            return LittleStructureRegistry.getStructureType(LittleRopeConnectionALET.class);
        }
        
    }
    
}
/*
 * 
                Vector3d vec3D = this.axisCenter.getCenter();
                ItemLittleRope rope = (ItemLittleRope) heldItem.getItem();
                if (vec3D != null && rope instanceof ItemLittleRope) {
                    double i1 = vec3D.x;
                    double j1 = vec3D.y;
                    double k1 = vec3D.z;
                    if (rope.hasSelected(heldItem)) {
                        if (rope.hasSameSelected(heldItem, i1, j1, k1)) {
                            rope.removeSelected(heldItem);
                        } else {
                            RopeConnectionData data = new RopeConnectionData(ColorUtils.BLACK, 0.1, 0, 10);
                            data.position1 = rope.getSelected(heldItem);
                            this.connectionsMap.add(data);
                        }
                    } else {
                        BlockPos loc = this.getStructureLocation().pos;
                        rope.addSelected(heldItem, loc.getX(), loc.getY(), loc.getZ(), i1, j1, k1);
                    }
                }
                */
