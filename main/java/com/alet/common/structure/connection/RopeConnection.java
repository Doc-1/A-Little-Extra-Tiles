package com.alet.common.structure.connection;

import java.security.InvalidParameterException;
import java.util.UUID;

import javax.vecmath.Matrix3d;
import javax.vecmath.Vector3d;

import com.alet.common.structure.type.LittleRopeConnectionALET;
import com.creativemd.creativecore.common.utils.math.collision.MatrixUtils;
import com.creativemd.creativecore.common.utils.math.vec.IVecOrigin;
import com.creativemd.creativecore.common.utils.mc.TickUtils;
import com.creativemd.creativecore.common.utils.mc.WorldUtils;
import com.creativemd.creativecore.common.world.IOrientatedWorld;
import com.creativemd.littletiles.common.entity.EntityAnimation;
import com.creativemd.littletiles.common.structure.LittleStructure;
import com.creativemd.littletiles.common.structure.connection.IWorldPositionProvider;
import com.creativemd.littletiles.common.structure.exception.CorruptedConnectionException;
import com.creativemd.littletiles.common.structure.exception.CorruptedLinkException;
import com.creativemd.littletiles.common.structure.exception.MissingAnimationException;
import com.creativemd.littletiles.common.structure.exception.MissingBlockException;
import com.creativemd.littletiles.common.structure.exception.MissingStructureException;
import com.creativemd.littletiles.common.structure.exception.MissingWorldException;
import com.creativemd.littletiles.common.structure.exception.NotYetConnectedException;
import com.creativemd.littletiles.common.tile.math.location.StructureLocation;
import com.creativemd.littletiles.common.tile.parent.IStructureTileList;
import com.creativemd.littletiles.common.tileentity.TileEntityLittleTiles;
import com.creativemd.littletiles.common.world.WorldAnimationHandler;
import com.google.common.base.Objects;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class RopeConnection {
    
    private UUID worldUUID;
    public final int ropeID;
    private final IWorldPositionProvider parent;
    
    private int structureIndex;
    private BlockPos relativePos;
    private TileEntityLittleTiles cachedTe;
    private Vector3d targetCenter;
    public Vector3d backupTargetCenter;
    private Vector3d lastTargetCenter;
    private LittleRopeConnectionALET target;
    public final boolean IS_HEAD;
    
    public RopeConnection(IWorldPositionProvider parent, StructureLocation location, int ropeID, boolean isHead) {
        this.parent = parent;
        this.structureIndex = location.index;
        this.IS_HEAD = isHead;
        this.relativePos = location.pos.subtract(this.parent.getPos());
        if (location.worldUUID != null)
            worldUUID = location.worldUUID;
        if (location.worldUUID == null) {}
        this.ropeID = ropeID;
    }
    
    public RopeConnection(IWorldPositionProvider parent, NBTTagCompound nbt) {
        this.IS_HEAD = nbt.getBoolean("isHead");
        this.parent = parent;
        this.structureIndex = nbt.getInteger("index");
        int[] array = nbt.getIntArray("coord");
        if (array.length == 3)
            relativePos = new BlockPos(array[0], array[1], array[2]);
        else
            throw new InvalidParameterException("No valid coord given " + nbt);
        if (nbt.hasKey("world"))
            this.worldUUID = UUID.fromString(nbt.getString("world"));
        this.ropeID = nbt.getInteger("ropeID");
        if (nbt.hasKey("xCenter") && nbt.hasKey("yCenter") && nbt.hasKey("zCenter"))
            backupTargetCenter = new Vector3d(nbt.getDouble("xCenter"), nbt.getDouble("yCenter"), nbt.getDouble("zCenter"));
    }
    
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        if (this.worldUUID != null)
            nbt.setString("world", worldUUID.toString());
        nbt.setIntArray("coord", new int[] { relativePos.getX(), relativePos.getY(), relativePos.getZ() });
        if (targetCenter != null) {
            nbt.setDouble("xCenter", targetCenter.x);
            nbt.setDouble("yCenter", targetCenter.y);
            nbt.setDouble("zCenter", targetCenter.z);
        }
        nbt.setInteger("index", structureIndex);
        nbt.setInteger("ropeID", this.ropeID);
        nbt.setBoolean("isHead", this.IS_HEAD);
        return nbt;
    }
    
    public static void transformPointToWorld(Vector3d vec, IVecOrigin origin, float partialTicks) {
        double rotX = origin.rotXLast() + (origin.rotX() - origin.rotXLast()) * partialTicks;
        double rotY = origin.rotYLast() + (origin.rotY() - origin.rotYLast()) * partialTicks;
        double rotZ = origin.rotZLast() + (origin.rotZ() - origin.rotZLast()) * partialTicks;
        
        double offX = origin.offXLast() + (origin.offX() - origin.offXLast()) * partialTicks;
        double offY = origin.offYLast() + (origin.offY() - origin.offYLast()) * partialTicks;
        double offZ = origin.offZLast() + (origin.offZ() - origin.offZLast()) * partialTicks;
        
        Matrix3d rotation = MatrixUtils.createRotationMatrix(rotX, rotY, rotZ);
        
        vec.sub(origin.center());
        rotation.transform(vec);
        vec.add(origin.center());
        
        vec.x += offX;
        vec.y += offY;
        vec.z += offZ;
    }
    
    public static void transformPointToFakeWorld(Vector3d vec, IVecOrigin origin, float partialTicks) {
        double rotX = origin.rotXLast() + (origin.rotX() - origin.rotXLast()) * partialTicks;
        double rotY = origin.rotYLast() + (origin.rotY() - origin.rotYLast()) * partialTicks;
        double rotZ = origin.rotZLast() + (origin.rotZ() - origin.rotZLast()) * partialTicks;
        
        double offX = origin.offXLast() + (origin.offX() - origin.offXLast()) * partialTicks;
        double offY = origin.offYLast() + (origin.offY() - origin.offYLast()) * partialTicks;
        double offZ = origin.offZLast() + (origin.offZ() - origin.offZLast()) * partialTicks;
        
        vec.x -= offX;
        vec.y -= offY;
        vec.z -= offZ;
        
        Matrix3d rotation = MatrixUtils.createRotationMatrix(rotX, rotY, rotZ);
        rotation.invert();
        vec.sub(origin.center());
        rotation.transform(vec);
        vec.add(origin.center());
    }
    
    public boolean adaptStructureChange(LittleStructure struct) {
        int lastIndex = this.structureIndex;
        BlockPos lastRelPos = this.relativePos;
        UUID lastWorldUUID = this.worldUUID;
        target = null;
        lastTargetCenter = null;
        
        this.structureIndex = struct.getIndex();
        this.relativePos = struct.getPos().subtract(this.parent.getPos());
        if (struct.getWorld() instanceof IOrientatedWorld)
            worldUUID = ((IOrientatedWorld) struct.getWorld()).getParentEntity().getUniqueID();
        else
            worldUUID = null;
        return lastIndex != this.structureIndex || !lastRelPos.equals(this.relativePos) || !Objects.equal(lastWorldUUID, this.worldUUID);
    }
    
    public LittleRopeConnectionALET getTarget() {
        if (this.target == null)
            try {
                target = (LittleRopeConnectionALET) getStructure();
            } catch (CorruptedConnectionException | NotYetConnectedException e) {}
        return target;
    }
    
    public boolean hasMoved(Vector3d vec) {
        if (lastTargetCenter != null && lastTargetCenter.epsilonEquals(vec, 0.1D))
            return false;
        lastTargetCenter = vec;
        return true;
    }
    
    public Vector3d getTargetCenter() {
        if (target != null) {
            targetCenter = target.axisCenter.getCenter();
            BlockPos pos = getStructurePosition();
            targetCenter.x += pos.getX();
            targetCenter.y += pos.getY();
            targetCenter.z += pos.getZ();
            
        } else if (backupTargetCenter != null)
            return backupTargetCenter;
        else
            return new Vector3d(0, 0, 0);
        Vector3d vec = new Vector3d(targetCenter);
        
        try {
            if (this.getWorld() instanceof IOrientatedWorld) {
                IOrientatedWorld w = (IOrientatedWorld) this.getWorld();
                transformPointToWorld(vec, w.getOrigin(), TickUtils.getPartialTickTime());
            }
        } catch (CorruptedConnectionException | NotYetConnectedException e) {}
        if (parent.getWorld() instanceof IOrientatedWorld) {
            IOrientatedWorld w = (IOrientatedWorld) parent.getWorld();
            transformPointToFakeWorld(vec, w.getOrigin(), TickUtils.getPartialTickTime());
        }
        
        return vec;
    }
    
    public BlockPos getStructurePosition() {
        return relativePos.add(parent.getPos());
    }
    
    public LittleStructure getStructure() throws CorruptedConnectionException, NotYetConnectedException {
        return getStructure(getWorld());
    }
    
    public LittleStructure getStructure(World world) throws CorruptedConnectionException, NotYetConnectedException {
        TileEntityLittleTiles te = getTileEntity(world);
        if (!te.hasLoaded())
            throw new NotYetConnectedException();
        IStructureTileList structure = te.getStructure(structureIndex);
        if (structure != null)
            return structure.getStructure();
        throw new MissingStructureException(te.getPos());
    }
    
    protected TileEntityLittleTiles getTileEntity() throws CorruptedConnectionException, NotYetConnectedException {
        return getTileEntity(getWorld());
    }
    
    protected TileEntityLittleTiles getTileEntity(World world) throws CorruptedConnectionException, NotYetConnectedException {
        if (cachedTe != null && !cachedTe.isInvalid() && !cachedTe.unloaded())
            return cachedTe;
        
        if (relativePos == null)
            throw new CorruptedLinkException();
        
        if (world == null)
            throw new MissingWorldException();
        
        BlockPos absoluteCoord = getStructurePosition();
        Chunk chunk = world.getChunkFromBlockCoords(absoluteCoord);
        if (WorldUtils.checkIfChunkExists(chunk)) {
            TileEntity te = world.getTileEntity(absoluteCoord);
            if (te instanceof TileEntityLittleTiles)
                return cachedTe = (TileEntityLittleTiles) te;
            else
                throw new MissingBlockException(absoluteCoord);
        } else
            throw new NotYetConnectedException();
    }
    
    protected World getWorld() throws CorruptedConnectionException, NotYetConnectedException {
        if (this.worldUUID == null) {
            World world = parent.getWorld();
            if (world instanceof IOrientatedWorld) {
                IOrientatedWorld w = (IOrientatedWorld) world;
                return w.getRealWorld();
            }
            return world;
        } else if (parent.getWorld() instanceof IOrientatedWorld) {
            IOrientatedWorld world = (IOrientatedWorld) parent.getWorld();
            if (world.getParentEntity().getUniqueID().equals(this.worldUUID))
                return (World) world;
        }
        EntityAnimation animation = WorldAnimationHandler.getHandler(parent.getWorld()).findAnimation(worldUUID);
        if (animation != null)
            return animation.fakeWorld;
        throw new MissingAnimationException(worldUUID);
        
    }
    
    public boolean isLinkToAnotherWorld() {
        return this.worldUUID != null;
    }
    
    //Server to sub. Sub to server
    public void reconnect() {
        try {
            cachedTe = null;
            getStructure();
            return;
        } catch (CorruptedConnectionException | NotYetConnectedException e) {
            
        }
        try {
            World world = parent.getWorld();
            if (world instanceof IOrientatedWorld) {
                IOrientatedWorld w = (IOrientatedWorld) world;
                LittleStructure struct = getStructure(w.getParent());
                if (struct instanceof LittleRopeConnectionALET) {
                    LittleRopeConnectionALET ropeStruct = (LittleRopeConnectionALET) struct;
                    
                }
            }
        } catch (CorruptedConnectionException | NotYetConnectedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
    /*
    public void updateChildConnection(int i, LittleStructure child, boolean dynamic) {
        World world = getWorld();
        World childWorld = child.getWorld();
        
        StructureChildConnection connector;
        if (childWorld == world)
            connector = new StructureChildConnection(this, false, dynamic, i, child.getPos().subtract(getPos()), child.getIndex(), child.getAttribute());
        else if (childWorld instanceof SubWorld && ((SubWorld) childWorld).parent != null)
            connector = new StructureChildToSubWorldConnection(this, dynamic, i, child.getPos().subtract(getPos()), child.getIndex(), child
                    .getAttribute(), ((SubWorld) childWorld).parent.getUniqueID());
        else
            throw new RuntimeException("Invalid connection between to structures!");
        
        children.set(connector);
    }
    */
}
