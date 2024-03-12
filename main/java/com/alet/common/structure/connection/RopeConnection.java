package com.alet.common.structure.connection;

import java.security.InvalidParameterException;
import java.util.UUID;

import javax.annotation.Nullable;
import javax.vecmath.Matrix3d;
import javax.vecmath.Vector3d;

import com.alet.common.entity.RopeData;
import com.alet.common.structure.type.LittleRopeConnectionALET;
import com.creativemd.creativecore.common.utils.math.Rotation;
import com.creativemd.creativecore.common.utils.math.RotationUtils;
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
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class RopeConnection {
    
    private UUID worldUUID;
    private final IWorldPositionProvider parent;
    
    private BlockPos relativePos;
    private BlockPos unrotatedPos;
    private TileEntityLittleTiles cachedTe;
    private Vector3d targetCenter;
    public Vector3d backupTargetCenter;
    private Vector3d lastTargetCenter;
    private LittleRopeConnectionALET targetStructure;
    public final boolean IS_HEAD;
    
    public int targetStructureIndex;
    public RopeData ropeData;
    
    public RopeConnection(IWorldPositionProvider parent, StructureLocation location, int targetStructureIndex, boolean isHead, @Nullable RopeData ropeData) {
        this.parent = parent;
        this.IS_HEAD = isHead;
        this.relativePos = location.pos.subtract(this.parent.getPos());
        this.unrotatedPos = relativePos;
        if (location.worldUUID != null)
            worldUUID = location.worldUUID;
        this.targetStructureIndex = targetStructureIndex;
        this.ropeData = ropeData;
    }
    
    public RopeConnection(IWorldPositionProvider parent, NBTTagCompound nbt) {
        this.IS_HEAD = nbt.getBoolean("isHead");
        this.parent = parent;
        int[] array = nbt.getIntArray("coord");
        if (array.length == 3)
            relativePos = new BlockPos(array[0], array[1], array[2]);
        else
            throw new InvalidParameterException("No valid coord given " + nbt);
        array = nbt.getIntArray("unrotCoord");
        if (array.length == 3)
            unrotatedPos = new BlockPos(array[0], array[1], array[2]);
        else
            unrotatedPos = relativePos;
        if (nbt.hasKey("world"))
            this.worldUUID = UUID.fromString(nbt.getString("world"));
        if (nbt.hasKey("xCenter") && nbt.hasKey("yCenter") && nbt.hasKey("zCenter"))
            backupTargetCenter = new Vector3d(nbt.getDouble("xCenter"), nbt.getDouble("yCenter"), nbt.getDouble("zCenter"));
        
        if (nbt.hasKey("target_rope_id"))
            this.targetStructureIndex = nbt.getInteger("target_rope_id");
        if (this.IS_HEAD && nbt.hasKey("data")) {
            NBTTagCompound dataNBT = (NBTTagCompound) nbt.getTag("data");
            this.ropeData = new RopeData(dataNBT);
        }
    }
    
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        if (this.worldUUID != null)
            nbt.setString("world", worldUUID.toString());
        nbt.setIntArray("coord", new int[] { relativePos.getX(), relativePos.getY(), relativePos.getZ() });
        nbt.setIntArray("unrotCoord", new int[] { unrotatedPos.getX(), unrotatedPos.getY(), unrotatedPos.getZ() });
        if (targetCenter != null) {
            nbt.setDouble("xCenter", targetCenter.x);
            nbt.setDouble("yCenter", targetCenter.y);
            nbt.setDouble("zCenter", targetCenter.z);
        }
        nbt.setBoolean("isHead", this.IS_HEAD);
        nbt.setInteger("target_rope_id", targetStructureIndex);
        if (this.IS_HEAD && this.ropeData != null)
            nbt.setTag("data", this.ropeData.writeData());
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
    
    public void adaptStructureChange(LittleStructure struct) {
        UUID newWorldUUID = null;
        BlockPos newRelPos = struct.getPos().subtract(this.parent.getPos());
        if (struct.getWorld() instanceof IOrientatedWorld)
            newWorldUUID = ((IOrientatedWorld) struct.getWorld()).getParentEntity().getUniqueID();
        else
            newWorldUUID = null;
        if (shouldStructureChange(newWorldUUID, newRelPos)) {
            this.relativePos = newRelPos;
            this.worldUUID = newWorldUUID;
        }
    }
    
    public boolean shouldStructureChange(UUID newWorldUUID, BlockPos newRelPos) {
        BlockPos lastRelPos = this.relativePos;
        UUID lastWorldUUID = this.worldUUID;
        targetStructure = null;
        lastTargetCenter = null;
        
        return !lastRelPos.equals(newRelPos) || !Objects.equal(lastWorldUUID, newWorldUUID);
    }
    
    public LittleRopeConnectionALET getTarget() {
        if (this.targetStructure == null)
            try {
                LittleStructure tar = getStructure();
                if (tar instanceof LittleRopeConnectionALET) {
                    this.targetStructure = (LittleRopeConnectionALET) tar;
                }
            } catch (CorruptedConnectionException | NotYetConnectedException e) {}
        return targetStructure;
    }
    
    public boolean hasMoved(Vector3d vec) {
        if (lastTargetCenter != null && lastTargetCenter.epsilonEquals(vec, 0.1D))
            return false;
        lastTargetCenter = vec;
        return true;
    }
    
    public Vector3d getTargetCenter() {
        if (targetStructure != null) {
            targetCenter = targetStructure.axisCenter.getCenter();
            BlockPos pos = getStructurePosition();
            targetCenter.x += pos.getX();
            targetCenter.y += pos.getY();
            targetCenter.z += pos.getZ();
            
        } else if (backupTargetCenter != null)
            return backupTargetCenter;
        else
            return null;
        Vector3d vec = new Vector3d(targetCenter);
        
        try {
            if (this.getWorld() instanceof IOrientatedWorld) {
                IOrientatedWorld w = (IOrientatedWorld) this.getWorld();
                transformPointToWorld(vec, w.getOrigin(), TickUtils.getPartialTickTime());
            }
        } catch (CorruptedConnectionException | NotYetConnectedException e) {
            return null;
        }
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
        for (IStructureTileList struct : te.structures()) {
            LittleStructure structure = struct.getStructure();
            if (structure != null && structure instanceof LittleRopeConnectionALET) {
                LittleRopeConnectionALET rope = (LittleRopeConnectionALET) structure;
                if (rope.getIndex() == this.targetStructureIndex)
                    return rope;
            }
        }
        throw new MissingStructureException(te.getPos());
    }
    
    public LittleRopeConnectionALET scanAfterPlace() throws CorruptedConnectionException, NotYetConnectedException {
        
        this.backupTargetCenter = null;
        if (((LittleRopeConnectionALET) parent).prevBlockPosition == null)
            throw new MissingBlockException(null);
        BlockPos absoluteCoord = getStructurePosition();
        World world = getWorld();
        if (world == null)
            throw new MissingStructureException(absoluteCoord);
        BlockPos original = unrotatedPos.add(((LittleRopeConnectionALET) parent).prevBlockPosition);
        MutableBlockPos mPos = new MutableBlockPos();
        for (int x = absoluteCoord.getX() - 1; x <= absoluteCoord.getX() + 1; x++)
            for (int y = absoluteCoord.getY() - 1; y <= absoluteCoord.getY() + 1; y++)
                for (int z = absoluteCoord.getZ() - 1; z <= absoluteCoord.getZ() + 1; z++) {
                    mPos.setPos(x, y, z);
                    LittleRopeConnectionALET st = scanAfterPlaceBlock(world, mPos, original);
                    if (st != null) {
                        return st;
                    }
                }
            
        throw new MissingStructureException(absoluteCoord);
    }
    
    public LittleRopeConnectionALET scanAfterPlaceBlock(World world, BlockPos pos, BlockPos original) throws CorruptedConnectionException, NotYetConnectedException {
        Chunk chunk = world.getChunkFromBlockCoords(pos);
        if (!WorldUtils.checkIfChunkExists(chunk))
            throw new NotYetConnectedException();
        
        TileEntity t = world.getTileEntity(pos);
        
        if (t instanceof TileEntityLittleTiles) {
            TileEntityLittleTiles te = (TileEntityLittleTiles) t;
            for (IStructureTileList tile : te.structures()) {
                LittleStructure s = tile.getStructure();
                if (s instanceof LittleRopeConnectionALET) {
                    LittleRopeConnectionALET rope = (LittleRopeConnectionALET) s;
                    if (rope.prevStructureIndex == targetStructureIndex && rope.prevBlockPosition.equals(original)) {
                        this.targetStructureIndex = rope.getIndex();
                        this.relativePos = rope.getPos().subtract(parent.getPos());
                        return rope;
                    }
                }
            }
        }
        return null;
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
    
    public void rotateConnection(Rotation rot) {
        relativePos = rot.getMatrix().transform(relativePos);
    }
    
    public void mirrorConnection(Axis axis) {
        relativePos = RotationUtils.flip(relativePos, axis);
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
