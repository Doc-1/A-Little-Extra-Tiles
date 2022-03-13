package com.alet.common.entity;

import java.util.UUID;

import com.creativemd.creativecore.common.world.CreativeWorld;
import com.creativemd.littletiles.common.entity.EntityAnimation;
import com.creativemd.littletiles.common.entity.EntityAnimationController;
import com.creativemd.littletiles.common.structure.relative.StructureAbsolute;
import com.creativemd.littletiles.common.tile.math.location.LocalStructureLocation;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldNameable;
import net.minecraft.world.World;

public abstract class EntityAnimationMinecart extends EntityAnimation implements IWorldNameable {
    
    public EntityAnimationMinecart(World world, CreativeWorld fakeWorld, EntityAnimationController controller, BlockPos absolutePreviewPos, UUID uuid, StructureAbsolute center, LocalStructureLocation location) {
        super(world, fakeWorld, controller, absolutePreviewPos, uuid, center, location);
    }
    
}