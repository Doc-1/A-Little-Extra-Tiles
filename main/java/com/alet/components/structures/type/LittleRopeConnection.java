package com.alet.components.structures.type;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Vector3d;

import org.lwjgl.util.Color;

import com.alet.components.entities.RopeData;
import com.alet.components.items.ItemLittleRope;
import com.alet.components.structures.connection.RopeConnection;
import com.creativemd.creativecore.common.utils.math.vec.Vec3;
import com.creativemd.creativecore.common.utils.mc.ColorUtils;
import com.creativemd.creativecore.common.world.IOrientatedWorld;
import com.creativemd.littletiles.common.action.LittleActionException;
import com.creativemd.littletiles.common.action.block.LittleActionActivated;
import com.creativemd.littletiles.common.structure.directional.StructureDirectional;
import com.creativemd.littletiles.common.structure.exception.CorruptedConnectionException;
import com.creativemd.littletiles.common.structure.exception.NotYetConnectedException;
import com.creativemd.littletiles.common.structure.registry.LittleStructureType;
import com.creativemd.littletiles.common.structure.type.door.LittleAdvancedDoor;
import com.creativemd.littletiles.common.tile.LittleTile;
import com.creativemd.littletiles.common.tile.math.location.StructureLocation;
import com.creativemd.littletiles.common.tile.parent.IStructureTileList;
import com.creativemd.littletiles.common.util.place.Placement;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class LittleRopeConnection extends LittleAdvancedDoor {
    
    @StructureDirectional(saveKey = "conList")
    public List<RopeConnection> connections = new ArrayList<RopeConnection>();
    
    public int prevStructureIndex = -1;
    public BlockPos prevBlockPosition;
    
    public LittleRopeConnection(LittleStructureType type, IStructureTileList mainBlock) {
        super(type, mainBlock);
    }
    
    @Override
    protected void loadFromNBTExtra(NBTTagCompound nbt) {
        /*
        NBTTagList conList = nbt.getTagList("conList", Constants.NBT.TAG_COMPOUND);
        connections.clear();
        for (NBTBase n : conList) {
            RopeConnection c = new RopeConnection(this, (NBTTagCompound) n);
            connections.add(c);
        }
        */
        if (nbt.hasKey("prev_data")) {
            int[] data = nbt.getIntArray("prev_data");
            if (data.length == 4) {
                this.prevBlockPosition = new BlockPos(data[0], data[1], data[2]);
                this.prevStructureIndex = data[3];
            } else {
                this.prevBlockPosition = null;
                this.prevStructureIndex = -1;
            }
            
        } else {
            this.prevBlockPosition = null;
            this.prevStructureIndex = -1;
        }
    }
    
    @Override
    protected void writeToNBTExtra(NBTTagCompound nbt) {
        /*
        NBTTagList conList = new NBTTagList();
        for (RopeConnection con : connections) {
            con.getTarget();
            con.getTargetCenter();
            NBTTagCompound nb = con.writeToNBT(new NBTTagCompound());
            conList.appendTag(nb);
        }
        nbt.setTag("conList", conList);
        */
    }
    
    @Override
    public NBTTagCompound writeToNBTPreview(NBTTagCompound nbt, BlockPos newCenter) {
        nbt.setIntArray("prev_data", new int[] { this.getPos().getX(), this.getPos().getY(), this.getPos().getZ(), this
                .getIndex() });
        return super.writeToNBTPreview(nbt, newCenter);
    }
    
    @Override
    public void afterPlaced() {
        super.afterPlaced();
        List<RopeConnection> connectionsToRemove = new ArrayList<RopeConnection>();
        for (RopeConnection con : this.connections) {
            try {
                LittleRopeConnection rope = con.scanAfterPlace();
                
                //con.adaptStructureChange(rope);
            } catch (CorruptedConnectionException | NotYetConnectedException e) {
                connectionsToRemove.add(con);
            }
        }
        this.connections.removeAll(connectionsToRemove);
    }
    
    @Override
    public void finishedPlacement(Placement placement) {
        this.prevStructureIndex = -1;
        this.prevBlockPosition = null;
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
        if (this.isClient())
            return true;
        if (heldItem.getItem() instanceof ItemLittleRope) {
            if (!ItemLittleRope.hasSelected(heldItem)) //Tail == 1
                ItemLittleRope.addSelected(heldItem, this.getStructureLocation());
            else {
                try {
                    StructureLocation tailLocation = ItemLittleRope.getSelectedLocation(heldItem);
                    World w = this.getWorld();
                    if (w instanceof IOrientatedWorld)
                        w = ((IOrientatedWorld) w).getRealWorld();
                    LittleRopeConnection tail = (LittleRopeConnection) tailLocation.find(w);
                    RopeData data = ItemLittleRope.writeDataFromNBT(heldItem.getTagCompound());
                    
                    /*
                    if (this.matchingConnection(data, tail))
                        return true;
                    */
                    
                    tail.connections.add(new RopeConnection(tail, this.getStructureLocation(), this
                            .getIndex(), false, null));
                    this.connections.add(new RopeConnection(this, tailLocation, tail.getIndex(), true, data));
                    
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
    
    public boolean matchingConnection(RopeData data, LittleRopeConnection otherStructure) {
        for (RopeConnection con : otherStructure.connections) {
            try {
                if (con.getStructure() == this) {
                    RopeData otherData = con.IS_HEAD ? con.ropeData : this.connections.get(
                        con.targetStructureIndex).ropeData;
                    if (otherData.equals(data))
                        return true;
                }
            } catch (CorruptedConnectionException | NotYetConnectedException e) {
                e.printStackTrace();
            }
        }
        
        for (RopeConnection con : this.connections) {
            try {
                if (con.getStructure() == this) {
                    RopeData thisData = con.IS_HEAD ? con.ropeData : otherStructure.connections.get(
                        con.targetStructureIndex).ropeData;
                    if (thisData.equals(data))
                        return true;
                }
            } catch (CorruptedConnectionException | NotYetConnectedException e) {
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
        for (RopeConnection con : this.connections) {
            try {
                LittleRopeConnection structure = (LittleRopeConnection) con.getStructure();
                structure.connections.remove(con.targetStructureIndex);
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
        
        for (RopeConnection con : connections) {
            if (!con.IS_HEAD)
                continue;
            RopeData data = con.ropeData; //is Head
            Vector3d vecCenter = con.getTargetCenter();
            
            if (vecCenter != null && data != null) {
                minX = Math.min(vecCenter.x - data.thickness, minX);
                minY = Math.min(vecCenter.y - data.thickness, minY);
                minZ = Math.min(vecCenter.z - data.thickness, minZ);
                maxX = Math.max(vecCenter.x + data.thickness, maxX);
                maxY = Math.max(vecCenter.y + data.thickness, maxY);
                maxZ = Math.max(vecCenter.z + data.thickness, maxZ);
            }
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
        //System.out.println(this.connections);
        for (RopeConnection con : this.connections) {
            if (!con.IS_HEAD)
                continue;
            if (con.getTarget() == null)
                continue;
            RopeData data = con.ropeData;
            Vector3d headPos = this.axisCenter.getCenter();
            // headPos.add(new Vector3d(1, 1, 1));
            Vector3d vecPos = new Vector3d(pos.getX(), pos.getY(), pos.getZ());
            Vector3d tailPos = con.getTargetCenter();
            if (tailPos == null || data == null)
                continue;
            tailPos = (Vector3d) tailPos.clone();
            if (con.hasMoved(tailPos))
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
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO,
                GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            GlStateManager.color(red, green, blue, alpha);
            GlStateManager.disableTexture2D();
            GlStateManager.enableAlpha();
            GlStateManager.disableCull();
            GlStateManager.disableLighting();
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuffer();
            GlStateManager.translate(x, y, z);
            //GlStateManager.translate(-translatePos.x, -translatePos.y, -translatePos.z);
            int count = (int) (startPoint.distance(endPoint) * 10);
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
    
}
/*
 * 
 * 
 * 

    public void setConnectionID() throws NotYetConnectedException, CorruptedConnectionException {
        List<BlockPos> blockArea = StructureUtils.getBlockArea(this);
        Set<Integer> usedIDs = new HashSet<>();
        for (BlockPos pos : blockArea) {
            Chunk chunk = this.getWorld().getChunkFromBlockCoords(pos);
            if (!WorldUtils.checkIfChunkExists(chunk))
                throw new NotYetConnectedException();
            World world = this.getWorld();
            TileEntity tileEntity = world.getTileEntity(pos);
            collectUsedIDs(world, tileEntity, usedIDs);
        }
        int id = 0;
        for (int x = 0; x <= usedIDs.size(); x++) {
            if (!usedIDs.contains(x)) {
                id = x;
                break;
            }
        }
        this.connectionID = id;
    }
    
    public static void collectUsedIDs(World world, TileEntity tileEntity, Set<Integer> usedIDs) throws CorruptedConnectionException, NotYetConnectedException {
        if (tileEntity instanceof TileEntityLittleTiles) {
            TileEntityLittleTiles te = (TileEntityLittleTiles) tileEntity;
            for (IStructureTileList tile : te.structures()) {
                LittleStructure structure = tile.getStructure();
                if (structure instanceof LittleRopeConnectionALET) {
                    LittleRopeConnectionALET rope = (LittleRopeConnectionALET) structure;
                    if (rope.connectionID != -1)
                        usedIDs.add(rope.connectionID);
                }
            }
        }
    }
    

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
