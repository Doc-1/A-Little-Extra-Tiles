package com.alet.common.structure.connection;

import java.security.InvalidParameterException;
import java.util.UUID;

import com.creativemd.creativecore.common.utils.mc.WorldUtils;
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
import com.creativemd.littletiles.common.tile.parent.IStructureTileList;
import com.creativemd.littletiles.common.tileentity.TileEntityLittleTiles;
import com.creativemd.littletiles.common.world.WorldAnimationHandler;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class RopeConnection {
    
    private UUID entityUUID;
    
    IWorldPositionProvider parent;
    
    private final int structureIndex;
    private final BlockPos relativePos;
    private TileEntityLittleTiles cachedTe;
    
    public RopeConnection(IWorldPositionProvider parent, BlockPos relative, int index, UUID entityUUID) {
        this.parent = parent;
        this.structureIndex = index;
        this.relativePos = relative;
        this.entityUUID = entityUUID;
    }
    
    public RopeConnection(IWorldPositionProvider parent, NBTTagCompound nbt) {
        this.parent = parent;
        this.structureIndex = nbt.getInteger("index");
        int[] array = nbt.getIntArray("coord");
        if (array.length == 3)
            relativePos = new BlockPos(array[0], array[1], array[2]);
        else
            throw new InvalidParameterException("No valid coord given " + nbt);
        if (nbt.hasKey("entity"))
            this.entityUUID = UUID.fromString(nbt.getString("entity"));
    }
    
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        if (this.entityUUID == null)
            nbt.setBoolean("subWorld", true);
        else
            nbt.setString("entity", entityUUID.toString());
        nbt.setIntArray("coord", new int[] { relativePos.getX(), relativePos.getY(), relativePos.getZ() });
        nbt.setInteger("index", structureIndex);
        return nbt;
    }
    
    public BlockPos getStructurePosition() {
        return relativePos;
    }
    
    public LittleStructure getStructure() throws CorruptedConnectionException, NotYetConnectedException {
        TileEntityLittleTiles te = getTileEntity();
        if (!te.hasLoaded())
            throw new NotYetConnectedException();
        IStructureTileList structure = te.getStructure(structureIndex);
        if (structure != null)
            return structure.getStructure();
        throw new MissingStructureException(te.getPos());
    }
    
    protected TileEntityLittleTiles getTileEntity() throws CorruptedConnectionException, NotYetConnectedException {
        if (cachedTe != null && !cachedTe.isInvalid() && !cachedTe.unloaded())
            return cachedTe;
        
        if (relativePos == null)
            throw new CorruptedLinkException();
        
        World world = getWorld();
        
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
        if (this.entityUUID == null)
            return parent.getWorld();
        EntityAnimation animation = WorldAnimationHandler.getHandler(parent.getWorld()).findAnimation(entityUUID);
        if (animation != null)
            return animation.fakeWorld;
        throw new MissingAnimationException(entityUUID);
        
    }
    
    public boolean isLinkToAnotherWorld() {
        return this.entityUUID != null;
    }
    
}
